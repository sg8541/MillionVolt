<template>
  <div v-if="isVisible" class="modal-overlay">
    <div class="modal-container">
      <button class="close-button" @click="closeModal">X</button>

      <!-- 충전소 사진 -->
      <div class="station-image">
        <img :src="stationInfo.filePath || defaultImage" alt="충전소 이미지" />
      </div>

      <!-- 충전소 기본 정보 -->
      <div class="station-info">
        <h2>{{ stationInfo.name || "정보 없음" }}</h2>
        <p>{{ stationInfo.address || "정보 없음" }}</p>
        <p>
          사용 가능한 충전기: 
          {{ stationInfo.availableCharger || 0 }} / {{ stationInfo.totalCharger || 0 }}
        </p>
      </div>

      <!-- 충전소 상세 정보 -->
      <div class="station-details">
        <p><strong>사업자:</strong> {{ stationInfo.operator || "정보 없음" }}</p>
        <p><strong>사용 제한:</strong> {{ stationInfo.restrictions || "정보 없음" }}</p>
        <p><strong>운영 시간:</strong> {{ stationInfo.operatingHours || "정보 없음" }}</p>
        <p><strong>전화번호:</strong> {{ stationInfo.phoneNumber || "정보 없음" }}</p>
      </div>

      <!-- 충전기 블록 -->
      <div class="chargers-container">
        <div
          v-for="(charger, index) in chargers"
          :key="charger.chargerId"
          class="charger-block"
        >
          <p><strong>충전기 {{ index + 1 }}</strong></p>
          <p><strong>상태:</strong> {{ getStatusText(charger.chargerStatusId) }}</p>
          <p><strong>충전기 타입:</strong> {{ charger.type || "정보 없음" }}</p>
          <p><strong>속도:</strong> {{ getSpeedText(charger.chargerSpeedId) }}</p>

          <!-- 버튼: 상태에 따라 활성화/비활성화 -->
          <button 
            :disabled="charger.chargerStatusId !== 1" 
            @click="startCharging(charger.chargerId, charger.type, getSpeedText(charger.chargerSpeedId))"
            class="action-button"
          >
            {{ charger.chargerStatusId === 1 ? '충전 예약' : '사용 불가' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";

const router = useRouter();

// Props 정의
const props = defineProps({
  stationId: {
    type: Number,
    required: true,
  },
  isVisible: Boolean,
});

// Emit 정의
const emit = defineEmits(["close"]);

// 데이터 저장
const stationInfo = ref({});
const chargers = ref([]);
const defaultImage = "/images/default-station.png";

// Close modal
const closeModal = () => {
  emit("close");
};

// API 호출 함수
const fetchStationDetails = async (id) => {
  try {
    const response = await fetch(
      `http://localhost:8081/api/v1/charging-stations/station/${id}`
    );
    if (!response.ok) throw new Error("충전소 정보를 불러오는 데 실패했습니다.");
    stationInfo.value = await response.json();
  } catch (error) {
    console.error("Error fetching station details:", error);
  }
};

const fetchChargers = async (id) => {
  try {
    const response = await fetch(
      `http://localhost:8081/api/v1/charging-stations/station/${id}/chargers`
    );
    if (!response.ok) throw new Error("충전기 정보를 불러오는 데 실패했습니다.");
    chargers.value = await response.json();
  } catch (error) {
    console.error("Error fetching chargers:", error);
    chargers.value = [];
  }
};

// 충전기 상태 변경 로직
const updateChargerStatus = async (chargerId) => {
  try {
    await axios.put(
      `http://localhost:8081/api/v1/charging-stations/chargers/${chargerId}/status`,
      {
        stationId: props.stationId, // props에서 stationId 가져오기
        status: 2, // "in_use" 대신 정수 2 사용
      }
    );

    // 상태 변경 후 화면 갱신
    const chargerIndex = chargers.value.findIndex((c) => c.chargerId === chargerId);
    if (chargerIndex !== -1) {
      chargers.value[chargerIndex].chargerStatusId = 2; // 2: 사용 중 상태
    }
  } catch (error) {
    console.error("Error updating charger status:", error);
    alert("충전기 상태 업데이트에 실패했습니다.");
  }
};

// 충전 시작 함수: 충전 예약 페이지로 이동
const startCharging = async (chargerId, chargerType, chargerSpeed) => {
  try {
    // 상태 업데이트 완료 후 페이지 이동
    await updateChargerStatus(chargerId);

    router.push({
      path: "/Reservation",
      query: {
        chargerId,
        chargerType,
        chargerSpeed,
        stationName: stationInfo.value.name || "정보 없음",
        stationAddress: stationInfo.value.address || "정보 없음",
      },
    });
  } catch (error) {
    alert("충전기 상태 업데이트 중 문제가 발생했습니다. 다시 시도해주세요.");
    console.error("Error in startCharging:", error);
  }
};


// 상태 텍스트 반환
const getStatusText = (chargerStatusId) => {
  switch (chargerStatusId) {
    case 1: return "사용 가능";
    case 2: return "사용 중";
    case 3: return "점검 중";
    default: return "알 수 없음";
  }
};

// 속도 텍스트 반환
const getSpeedText = (chargerSpeedId) => {
  switch (chargerSpeedId) {
    case 1: return "7kW";
    case 2: return "50kW";
    case 3: return "100kW";
    case 4: return "200kW";
    case 5: return "300kW 이상";
    default: return "알 수 없음";
  }
};

// stationId 변화 감지 및 데이터 로드
watch(
  () => props.stationId,
  (newId) => {
    if (newId) {
      fetchStationDetails(newId);
      fetchChargers(newId);
    }
  },
  { immediate: true }
);
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-container {
  background: #fff;
  width: 80%;
  max-width: 800px;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  position: relative;
}

.close-button {
  position: absolute;
  top: 10px;
  right: 10px;
  background: #f00;
  color: #fff;
  border: none;
  border-radius: 50%;
  width: 30px;
  height: 30px;
  font-size: 18px;
  cursor: pointer;
}

.action-button {
  margin-top: 10px;
  padding: 8px 12px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.action-button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.chargers-container {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.charger-block {
  flex: 1 0 21%;
  background: #f0f0f0;
  padding: 10px;
  text-align: center;
  border-radius: 8px;
  border: 1px solid #ddd;
}
</style>

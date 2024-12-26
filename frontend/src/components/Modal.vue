<!-- 기존 코드에서 모바일 전환시 추가되는 코드들은 "모바일 추가"라고 명시해 놓음. -->
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
        <p >{{ stationInfo.address || "정보 없음" }}</p>
        <p>
          사용 가능한 충전기: 
          {{ availableChargersCount }} / {{ chargers.length || 0 }}
        </p>
      </div>

      <!-- 충전소 상세 정보 -->
      <div class="station-details">
        <p><strong>기종 정보:</strong> {{ stationInfo.deviceType || "정보 없음" }}</p>
        <p><strong>시설 구분:</strong> {{ stationInfo.facilityType || "정보 없음" }}</p>
        <p><strong>충전 금액:</strong> {{ stationInfo.pricePerKWh+'원' || "정보 없음" }}</p>
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
import { ref, watch, onMounted } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import { chargerStateChangeWebSocketStore } from '@/stores/webSocketChargerChangeStore';

const token = localStorage.getItem('user');
const store = chargerStateChangeWebSocketStore();

onMounted(()=> {
  store.connectCharger(props.stationId);
  console.log("온마운트 됨?")
});

store.chargeState.forEach = (item,index) => {
  console.log(item);
}

onBeforeUnmount(()=>{
  store.disconnectWebSocket();
})


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
const defaultImage = "https://ssyenc.co.kr/file/record/815/%EA%B0%80%EB%9D%BD%EC%8C%8D%EC%9A%A91%EC%B0%A8%20%EC%95%84%ED%8C%8C%ED%8A%B8.jpg";

// Close modal
const closeModal = () => {
  emit("close");
};

// 사용 가능한 충전기 수 계산
const availableChargersCount = computed(() => {
  return chargers.value.filter((charger) => charger.chargerStatusId === 1).length;
});

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
    console.log("충전기 데이터:", chargers.value); // 데이터 확인
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
        status: 2, // "in_use" 대신 정수 2 전달
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

    if(token) {
    router.push({
      path: "/Reservation",
      query: {
        stationId:props.stationId,
        chargerId,
        chargerType,
        chargerSpeed,
        stationId: props.stationId,
        stationName: stationInfo.value.name || "정보 없음",
        stationAddress: stationInfo.value.address || "정보 없음",
      },
    });
  }else{
    alert('로그인 해주세요');
    router.push({
      path:'/login',
      name:'Login'
    })
  } 
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

watch(
  () => store.chargeState,
  (newChargeState) => {
    if (newChargeState && Array.isArray(newChargeState)) {
      chargers.value = chargers.value.map((charger) => {
        const updatedCharger = newChargeState.find(
          (state) => state.chargerId === charger.chargerId
        );
        if (updatedCharger) {
          // 새 객체로 반환하여 새로운 참조를 생성
          return {
            ...charger,
            chargerStatusId: updatedCharger.chargerStatusId,
          };
        }
        return charger;
      });
    }
  },
  { deep: true, immediate: true }
);
</script>

<style scoped>
.station-info{
  margin-top: 10px;
  
}

.station-info h2{
  margin-bottom: 20px;
}

.station-info p {
  margin-bottom: 10px;
}

.station-image img{
  width: 100%;
}

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

b, strong{
  color: #52616a;
}

.modal-container {
  background: #f0f5f9;
  width: 80%;
  max-width: 770px;
  max-height: 80vh;
  overflow-y: auto;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  position: relative;
}

.close-button {
  position: absolute;
  top: 10px;
  right: 10px;
  background: #1E2022;
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
  background-color: #C3C3C3;
  color: #333;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.action-button:hover {
  background-color: #52616a;
  color: #F0F5F9;
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
  background: #fff;
  padding: 10px;
  text-align: center;
  border-radius: 8px;
  border: 1px solid #c9d6de;
}
</style>

<template>
  <div class="sidebar">
    <!-- 검색창 -->
    <div class="search-bar mb-4">
      <input
        type="text"
        v-model="searchQuery"
        placeholder="충전소 이름 또는 지역 검색"
        class="search-input"
        @keyup.enter="fetchStations(1)"
      />
      <button @click="fetchStations(1)">검색</button>
    </div>

    <!-- 필터링 버튼 -->
    <div class="filter-options mb-4">
      <button
        v-for="(option, index) in filterOptions"
        :key="index"
        class="filter-button"
        :class="{ active: selectedFilter === option.id }"
        @click="applyFilter(option.id)"
      >
        {{ option.name }}
      </button>
    </div>

    <!-- 충전소 목록 -->
    <h2 class="text-lg font-bold mb-4">검색 결과</h2>
    <div v-if="loading" class="loading">
      <p>충전소 데이터를 불러오는 중입니다...</p>
    </div>
    <ul v-if="stations.length > 0">
      <li
        v-for="station in stations"
        :key="station.stationId"
        class="station-item"
        @click="openModal(station.stationId)"
      >
        <p class="font-bold">{{ station.name }}</p>
        <p>주소: {{ station.address }}</p>
        <p>
          사용 가능한 충전기: {{ station.availableChargerCount || 0 }} / 
          {{ station.totalCharger || 0 }}
        </p>
        <p>충전 속도: {{ station.chargeSpeed || "정보 없음" }}</p>
        <p>충전 요금: {{ station.pricePerKWh || "정보 없음" }}</p>
      </li>
    </ul>
    <p v-else>조건에 맞는 충전소가 없습니다.</p>

    <!-- 페이징 -->
    <div class="pagination">
  <button 
    v-if="currentPage > 1" 
    @click="changePage(1)"
  >
    « 처음
  </button>
  <button 
    v-if="currentPage > 1" 
    @click="changePage(currentPage - 1)"
  >
    ‹ 이전
  </button>

  <button 
    v-for="page in visiblePages" 
    :key="page" 
    :class="{ active: currentPage === page }" 
    @click="changePage(page)"
  >
    {{ page }}
  </button>

  <button 
    v-if="currentPage < totalPages" 
    @click="changePage(currentPage + 1)"
  >
    다음 ›
  </button>
  <button 
    v-if="currentPage < totalPages" 
    @click="changePage(totalPages)"
  >
    마지막 »
  </button>
</div>

    <!-- 모달 컴포넌트 -->
    <Modal
      v-if="isModalVisible"
      :stationId="selectedStationId"
      :isVisible="isModalVisible"
      @close="closeModal"
    />
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import Modal from "@/components/Modal.vue";

const stations = ref([]); // 충전소 데이터
const loading = ref(false);
const searchQuery = ref(""); // 검색어
const selectedFilter = ref(null); // 선택된 필터
const itemsPerPage = 5; // 페이지당 데이터 수
const currentPage = ref(1); // 현재 페이지
const totalItems = ref(0); // 전체 아이템 수
const isModalVisible = ref(false); // 모달 표시 여부
const selectedStationId = ref(null); // 선택된 충전소 ID

// 필터 옵션
const filterOptions = [
  { id: null, name: "전체 보기" },
  { id: 1, name: "7kW" },
  { id: 2, name: "50kW" },
  { id: 3, name: "100kW" },
  { id: 4, name: "200kW" },
  { id: 5, name: "300kW 이상" },
];

// 총 페이지 수 계산
const totalPages = computed(() => {
  return totalItems.value > 0 ? Math.ceil(totalItems.value / itemsPerPage) : 0;
});

// 사용 가능한 충전기 개수 계산
const calculateAvailableChargers = (chargers) => {
  return chargers.filter((charger) => charger.chargerStatusId === 1).length;
};

// 충전소 데이터 불러오기
const fetchStations = async (page) => {
  currentPage.value = page; // 현재 페이지 설정
  loading.value = true;

  try {
    const response = await fetch(
      `http://localhost:8081/api/v1/charging-stations/sidebar?query=${encodeURIComponent(
        searchQuery.value
      )}&chargerSpeedId=${selectedFilter.value || ""}&page=${currentPage.value}&size=${itemsPerPage}`
    );

    if (!response.ok) throw new Error("충전소 데이터를 불러오는데 실패했습니다.");

    const data = await response.json(); // API 응답 데이터

    // 데이터 업데이트
    stations.value = data.stations.map((station) => {
      // 충전기 데이터가 없는 경우 빈 배열로 처리
      const chargers = station.chargers || [];
      return {
        ...station,
        availableChargerCount: calculateAvailableChargers(chargers),
      };
    });
    totalItems.value = data.totalCount;

    console.log("Stations Updated:", stations.value);
    console.log("Total Items Updated:", totalItems.value);
  } catch (error) {
    console.error("Error fetching stations:", error);
  } finally {
    loading.value = false; // 로딩 상태 해제
  }
};

// 필터 적용
const applyFilter = (filterId) => {
  selectedFilter.value = filterId;
  fetchStations(1); // 필터 적용 시 페이지를 1로 초기화
};

// 페이징 버튼 범위 계산
const visiblePages = computed(() => {
  const total = totalPages.value; // 총 페이지 수
  const current = currentPage.value; // 현재 페이지
  const range = 2; // 현재 페이지 기준 양옆으로 표시할 버튼 수

  const start = Math.max(current - range, 1); // 시작 페이지
  const end = Math.min(current + range, total); // 끝 페이지

  const pages = [];
  for (let i = start; i <= end; i++) {
    pages.push(i);
  }
  return pages;
});

// 페이지 변경
const changePage = (page) => {
  if (page !== currentPage.value) {
    fetchStations(page);
  }
};

// 모달 열기
const openModal = (stationId) => {
  selectedStationId.value = stationId;
  isModalVisible.value = true;
};

// 모달 닫기
const closeModal = () => {
  isModalVisible.value = false;
  selectedStationId.value = null;
};

// 초기 데이터 로드
fetchStations(1);
</script>

<style scoped>
.sidebar {
  width: 100%;
  height: 100%;
  background-color: #f9f9f9;
  overflow-y: auto;
  padding: 20px;
}

.search-bar {
  display: flex;
  gap: 10px;
}

.search-input {
  flex: 1;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.filter-options {
  display: flex;
  flex-wrap: wrap; /* 버튼이 여러 줄로 내려갈 수 있도록 설정 */
  gap: 10px; /* 버튼 사이의 간격 */
  justify-content: space-between; /* 버튼을 균등하게 분포 */
}

.filter-button {
  flex: 1 1 calc(20% - 10px); /* 각 버튼의 너비를 20%로 설정 (5개 버튼 기준) */
  min-width: 100px; /* 버튼 최소 너비 */
  padding: 10px 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: #fff;
  text-align: center; /* 텍스트 중앙 정렬 */
  cursor: pointer;
  box-sizing: border-box; /* 패딩 포함한 크기 계산 */
}

.filter-button.active {
  background-color: #007bff;
  color: #fff;
  border-color: #007bff;
}

.station-item {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-bottom: 10px;
  background-color: #fff;
  cursor: pointer;
  transition: background 0.3s;
}

.station-item:hover {
  background: #f0f0f0;
}
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 5px;
  margin-top: 10px;
}

.pagination button {
  padding: 5px 10px;
  border: 1px solid #ddd;
  background-color: #fff;
  cursor: pointer;
  transition: background-color 0.3s;
}

.pagination button.active {
  background-color: #007bff;
  color: #fff;
  font-weight: bold;
  cursor: not-allowed;
}

.pagination button:hover:not(.active) {
  background-color: #e6e6e6;
}

</style>

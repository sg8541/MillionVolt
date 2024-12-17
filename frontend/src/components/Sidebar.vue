<template>
  <div class="sidebar">
    <!-- 검색창 -->
    <div class="search-bar mb-4">
      <input
        type="text"
        v-model="searchQuery"
        placeholder="충전소 이름 또는 지역 검색"
        class="search-input"
      />
      <button @click="fetchStations">검색</button>
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
        v-for="station in paginatedStations"
        :key="station.stationId"
        class="station-item"
      >
        <p class="font-bold">{{ station.name }}</p>
        <p>주소: {{ station.address }}</p>
        <p>사용 가능한 충전기: {{ station.availableCharger || 0 }} / {{ station.totalCharger || 0 }}</p>
        <p>충전 속도: {{ station.chargeSpeed || '정보 없음' }}</p>
        <p>충전 요금: {{ station.pricePerKWh || '정보 없음' }}</p>
      </li>
    </ul>
    <p v-else>조건에 맞는 충전소가 없습니다.</p>

    <!-- 페이징 -->
    <div class="pagination" v-if="stations.length > itemsPerPage">
      <button
        v-for="page in totalPages"
        :key="page"
        :class="{ active: currentPage === page }"
        @click="changePage(page)"
      >
        {{ page }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";

const stations = ref([]); // 충전소 데이터
const loading = ref(false);
const searchQuery = ref(""); // 검색어
const selectedFilter = ref(null); // 선택된 필터
const error = ref(null);

const itemsPerPage = 5; // 페이지당 표시할 충전소 수
const currentPage = ref(1); // 현재 페이지

// 필터 옵션
const filterOptions = [
  { id: null, name: "전체 보기" },
  { id: 1, name: "완속 충전" },
  { id: 2, name: "급속 충전" },
];

// 현재 페이지에 표시할 데이터 계산
const paginatedStations = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return stations.value.slice(start, end);
});

// 총 페이지 수 계산
const totalPages = computed(() => {
  return Math.ceil(stations.value.length / itemsPerPage);
});

// API 호출 함수
const fetchStations = async () => {
  loading.value = true;
  error.value = null;

  try {
    // API 요청
    const response = await fetch(
      `http://localhost:8081/api/v1/charging-stations/sidebar?query=${encodeURIComponent(
        searchQuery.value
      )}${selectedFilter.value !== null ? `&chargerSpeedId=${selectedFilter.value}` : ""}&page=${currentPage.value}&size=${itemsPerPage}`
    );

    if (!response.ok) {
      throw new Error(`HTTP 에러: ${response.status}`);
    }

    const data = await response.json();
    stations.value = data;
    console.log("API 응답 데이터:", data);

    if (stations.value.length === 0) {
      console.warn("조건에 맞는 충전소 데이터가 없습니다.");
    }
  } catch (err) {
    console.error("충전소 데이터를 불러오는 중 오류:", err);
    error.value = "충전소 데이터를 불러오는 중 문제가 발생했습니다.";
  } finally {
    loading.value = false;
  }
};

// 필터 적용
const applyFilter = (filterId) => {
  selectedFilter.value = filterId; // 필터 업데이트
  currentPage.value = 1; // 페이지 초기화
  fetchStations(); // API 호출
};

// 페이지 변경
const changePage = (page) => {
  currentPage.value = page;
  fetchStations(); // API 호출
};

// 초기 데이터 로드
fetchStations();
</script>

<style scoped>
.sidebar {
  width: 100%;
  height: 100%;
  background-color: #f9f9f9;
  overflow-y: auto;
  padding: 20px;
  border-left: 1px solid #ddd;
}

.search-bar {
  display: flex;
  align-items: center;
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
  gap: 10px;
}

.filter-button {
  padding: 10px 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: #fff;
  cursor: pointer;
}

.filter-button.active {
  background-color: #007bff;
  color: #fff;
  border-color: #007bff;
}

.station-item {
  margin-bottom: 15px;
  padding: 10px;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 10px;
}

.pagination button {
  margin: 0 5px;
  padding: 5px 10px;
  border: 1px solid #ddd;
  background-color: #fff;
  cursor: pointer;
}

.pagination button.active {
  background-color: #007bff;
  color: #fff;
  border-color: #007bff;
}

.loading {
  text-align: center;
  font-size: 14px;
  color: #555;
}
</style>

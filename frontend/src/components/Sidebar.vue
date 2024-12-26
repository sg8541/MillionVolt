<template>
<div class="sidebar" :class="{ visible: isSidebarVisible }">

    <!-- 사용자 정보 -->
    <div class="user-info">
      <template v-if="isLoggedIn">
        <p>안녕하세요, <strong>{{ userName }}</strong> 님!</p>
      </template>
      <template v-else>
        <p>로그인 후 더 많은 기능을 이용할 수 있습니다.</p>
        <div class="actions">
          <RouterLink to="/login" class="user-action">로그인</RouterLink>
          <RouterLink to="/agreement" class="user-action">회원가입</RouterLink>
        </div>
      </template>
    </div>

    <!-- 닫기 버튼 -->
    <button class="close-button" @click="$emit('toggleSidebar')">닫기</button>

    <!-- 네비게이션 메뉴 -->
    <nav class="navigation">
      <template v-if="isLoggedIn">
        <RouterLink :to="`/myinfo/dashboard/${userId}`" class="nav-item">마이페이지</RouterLink>
        <button @click="handleLogout" class="nav-item logout">로그아웃</button>
      </template>
    </nav>

    <!-- 검색창 -->
    <div class="search-bar mb-4">
      <input
        type="text"
        v-model="searchQuery"
        placeholder="충전소 이름 또는 지역 검색"
        class="search-input"
        @keyup.enter="fetchStations(1)"
      />
      <button class="search-button" @click="fetchStations(1)">검색</button>
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
        @click="$emit('stationSelected', station)"
      >
        <p class="font-bold" style="font-size: larger; font-weight: bold; color: #52616A;">{{ station.name }}</p>
        <p>주소: {{ station.address }}</p>
        <p>
          사용 가능한 충전기: {{ station.availableChargerCount || 0 }} / 
          {{ station.totalCharger || 0 }}
        </p>
        <p>충전 속도: 
      {{ station.chargeSpeedIds && station.chargeSpeedIds.length > 0 
      ? station.chargeSpeedIds.map(getSpeedText).join(", ") 
      : "정보 없음" }}
    </p>

        <p>충전 요금: {{ station.pricePerKWh+'원' || "정보 없음" }}</p>
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
  </div>
</template>

<script setup>

import { ref, computed } from "vue";
import Modal from "@/components/Modal.vue";

// props 정의
const props = defineProps({
  isSidebarVisible: {
    type: Boolean,
    required: true,
  },
});

const isLoggedIn = ref(false);
const userName = ref("");
const userId = ref("");
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
  return {
    ...station.station, // 충전소 데이터
    chargers: station.chargers || [], // 충전기 데이터
    availableChargerCount: station.availableChargerCount || 0, // 사용 가능한 충전기 개수
    chargeSpeedIds: station.chargeSpeedIds || [], // 충전 속도 ID 리스트 추가
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

onMounted(() => {
  const user = localStorage.getItem("user"); // 로컬스토리지에서 사용자 정보 가져오기
  if (user) {
    try {
      const parsedUser = JSON.parse(user); // JSON 형식의 사용자 정보 파싱
      console.log("파싱된 사용자 정보:", parsedUser);
      isLoggedIn.value = true;
      userName.value = parsedUser.userName || "사용자"; // 이름 설정
      userId.value = parsedUser.id || ""; // 사용자 ID 설정
    } catch (error) {
      console.error("사용자 정보 파싱 오류:", error);
    }
  } else {
    console.log("로그인된 사용자 정보가 없습니다.");
  }
});

const handleLogout = () => {
  localStorage.removeItem("user"); // 사용자 정보 삭제
  isLoggedIn.value = false;
  userName.value = "";
  userId.value = "";
  alert("로그아웃 되었습니다.");
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
  position: fixed; /* 화면에 고정 */
  top: 0;
  right: 0;
  width: 400px;
  height: 100%; /* 화면 전체 높이 */
  background-color: #f9f9f9;
  z-index: 1000; /* 상위 요소 위에 나타나도록 */
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
  padding: 20px;
  transition: transform 0.3s ease-in-out; /* 애니메이션 추가 */
  transform: translateX(-100%); /* 기본적으로 화면 밖에 숨김 */
}

.sidebar.visible {
  transform: translateX(0); /* 화면 안으로 슬라이드 */
}

.search-bar {
  display: flex;
  gap: 10px;
}

.search-input {
  flex: 1;
  background-color: #fff;
  padding: 8px;
  border: 1px solid #C9D6DE;
  border-radius: 4px;
}

.search-input:focus {
  outline: none;
  border: 1px solid #52616a;
}

.search-button {
  border: 1px solid #C9D6DE;
  border-radius: 4px;
  border-color: #C9D6DE;
  background-color: #52616a;
  color: #fff;
  width: 20%;
}

.search-button:hover {
  background-color: #C9D6DE;
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

.filter-button:hover {
  background-color: #C9D6DE;
}

.filter-button.active {
  background-color: #52616a;
  color: #fff;
  border-color: #52616a;
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
  background: #E3E3E3;
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
  background-color: #52616a;
  color: #fff;
  font-weight: bold;
  cursor: not-allowed;
}

.pagination button:hover:not(.active) {
  background-color: #e6e6e6;
}

.close-button {
  position: absolute;
  top: 15px;
  left: 15px;
  font-size: 18px;
  color: #333;
  background: none;
  border: none;
  cursor: pointer;
  padding: 10px;
  border-radius: 50%;
  transition: background-color 0.3s;
}

.close-button:hover {
  background-color: rgba(0, 0, 0, 0.1);
}

/* 사용자 정보 스타일 */
.user-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
  gap: 15px; /* 요소 간 간격 */
  background-color: #f5f5f5;
  border-radius: 10px;
  width: 100%;
  max-width: 400px;
  margin: 0 auto;
  text-align: center;
}

.user-info p {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin: 0;
}

/* 카테고리 스타일 */
.navigation {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px; /* 카테고리 간 간격 */
  margin-top: 20px;
  width: 100%;
}

.nav-item {
  font-size: 16px;
  color: #007bff;
  text-decoration: none;
  padding: 5px 0;
  width: 100%;
  text-align: center;
}

.nav-item:hover {
  color: #0056b3;
  text-decoration: underline;
}

.actions {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px; /* 카테고리 간 간격 */
  margin-top: 20px;
  width: 100%;
}

.user-action {
  font-size: 16px;
  color: #007bff;
  text-decoration: none;
  padding: 5px 0;
  width: 100%;
  text-align: center;
  gap: 20px;
}

.user-action:hover {
  color: #0056b3;
  text-decoration: underline;
}
</style>
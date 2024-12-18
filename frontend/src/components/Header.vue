<template>
  <!-- 헤더 -->
  <header id="header" class="flex items-center justify-between">
    <!-- 로고 -->
    <div class="logo" @click="reloadPage">
      <img src="/src/assets/images/logo/logo-yellow-white.png" alt="로고" style="width:100%;">
    </div>

    <!-- 네비게이션 메뉴 -->
    <nav class="navigation">
      <a href="#" class="nav-item">이용방법</a>
      <template v-if="isLoggedIn">
        <a href="#" class="nav-item">결제 및 예약</a>
        <RouterLink :to="`/myinfo/dashboard/${id}`">
          <button type="button" class="nav-item">마이페이지</button>
      </RouterLink>
      <RouterLink to="/logout">
        <a href="#" class="nav-item" @click="logout">로그아웃</a>
        </RouterLink>
      </template>
      <template v-else>
        <RouterLink to="/login">
        <a href="#" class="nav-item">로그인</a>
      </RouterLink>
        <RouterLink to="/agreement">
        <a href="#" class="nav-item">회원가입</a>
        </RouterLink>
      </template>
    </nav>

    <!-- 알림 아이콘 -->
    <div class="relative alert-container">
      <span class="icon cursor-pointer" @click="toggleAlertBox">🔔</span>
      <!-- 알림창 -->
      <div v-if="isAlertBoxVisible" class="alert-box">
        <div class="alert-header">
          <span>📢 알림</span>
          <button class="close-btn" @click="toggleAlertBox">X</button>
        </div>
        <div class="alert-content">
          <p>{{ alertMessage || "새로운 알림이 없습니다." }}</p>
          <div v-if="currentSpeed" class="speed-indicator">
            <label><strong>충전 속도: {{ currentSpeed }}</strong></label>
            <div class="speed-bar">
              <div
                class="speed-progress"
                :style="{
                  width: getSpeedPercentage(currentSpeed) + '%',
                  backgroundColor: getSpeedColor(currentSpeed),
                }"
              ></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref } from "vue";
import { useAuthStore } from '@/stores/auth';

const authStore = useAuthStore();
const user = computed(() => authStore.user);
const id = user.value.id;

console.log("=====" + user.value.id);
// 로그인 상태 관리
const isLoggedIn = ref(true);

// 알림창 상태
const isAlertBoxVisible = ref(false);

// 알림 메시지 예시
const alertMessage = ref("충전기 1번이 충전을 시작합니다.");

// 충전 속도 예시
const currentSpeed = ref("100kW");

// 페이지 새로고침
const reloadPage = () => {
  window.location.reload();
};

// 로그아웃 함수
const logout = () => {
  console.log("로그아웃 실행");
  isLoggedIn.value = false;
  
};

// 알림창 토글
const toggleAlertBox = () => {
  isAlertBoxVisible.value = !isAlertBoxVisible.value;
};

// 충전 속도에 따른 퍼센트 반환
const getSpeedPercentage = (speed) => {
  switch (speed) {
    case "7kW":
      return 20;
    case "50kW":
      return 40;
    case "100kW":
      return 60;
    case "200kW":
      return 80;
    case "300kW 이상":
      return 100;
    default:
      return 0;
  }
};

// 충전 속도에 따른 색상 반환
const getSpeedColor = (speed) => {
  switch (speed) {
    case "7kW":
      return "#4caf50";
    case "50kW":
      return "#ffeb3b";
    case "100kW":
      return "#ff9800";
    case "200kW":
      return "#f44336";
    case "300kW 이상":
      return "#9c27b0";
    default:
      return "#ddd";
  }
};
</script>

<style scoped>
/* 헤더 스타일 */
#header {
  background-color: #1E2022;
  color: #000;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
}

.logo {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.logo img {
  height: 60px;
  margin-right: 10px;
}

.navigation {
  display: flex;
  gap: 15px;
}

.nav-item {
  text-decoration: none;
  color: white;
  font-weight: bold;
}

.icon {
  font-size: 28px;
}

/* 알림창 스타일 */
.alert-container {
  position: relative;
}

.alert-box {
  position: absolute;
  top: 40px;
  right: 0;
  width: 250px;
  background-color: #fff;
  border: 1px solid #ddd;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  z-index: 1000;
  animation: slide-down 0.3s ease-in-out;
}

.alert-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #f5f5f5;
  padding: 10px;
  font-weight: bold;
}

.alert-content {
  color: #000;
  padding: 10px;
  font-size: 14px;
}

.close-btn {
  background: none;
  border: none;
  color: #f00;
  cursor: pointer;
  font-weight: bold;
  font-size: 14px;
}

.speed-indicator {
  margin-top: 10px;
}

.speed-bar {
  width: 100%;
  background-color: #eee;
  height: 10px;
  border-radius: 5px;
  overflow: hidden;
}

.speed-progress {
  height: 100%;
  transition: width 0.3s ease-in-out;
}

/* 슬라이드 다운 애니메이션 */
@keyframes slide-down {
  from {
    transform: translateY(-10px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}
</style>

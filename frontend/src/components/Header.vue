<template>
  <!-- 헤더 -->
  <header id="header" class="flex items-center justify-between">
    <!-- 로고 -->
    <div class="logo" @click="reloadPage">
      <img src="/public/images/logo2.png" alt="로고" style="width: 100px;">
    </div>

    <!-- 네비게이션 메뉴 -->
    <nav class="navigation">
      <a href="#" class="nav-item">이용방법</a>
      <template v-if="isLoggedIn">
        <a href="#" class="nav-item">결제 및 예약</a>
        <a href="#" class="nav-item">마이페이지</a>
        <a href="#" class="nav-item" @click="logout">로그아웃</a>
      </template>
      <template v-else>
        <a href="#" class="nav-item">로그인</a>
        <a href="#" class="nav-item">회원가입</a>
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

        <div v-if="store.chargingData.chargerType" class="alert-content">
          <p>{{ store.finishAlarm.message }}</p>
          <div v-if="store.chargingData.chargerType" class="speed-indicator">
            <label><strong>충전 속도: {{ store.chargingData.chargerType }}</strong></label>
            <div class="speed-bar">
              <div
                class="speed-progress"
                :style="{
                  width: getSpeedPercentage(currentBatteryPercent) + '%',
                  backgroundColor: getSpeedColor(currentBatteryPercent),
                }"
              ></div>
            </div>
            <div>
            {{ currentBatteryPercent }}%
            </div>
            <div v-if="currentBatteryPercent<100" @click="movePaymentPrice">
              <label><strong>충전정보 보기</strong></label>
            </div>
            <div v-else @click="movePaymentPrice">
              <label><strong>결제하러가기</strong></label>
            </div>
          </div>
        </div>

        <div v-if="storeAlarm.alarm.message" class="alert-content">
          <p>{{ storeAlarm.alarm.message }}</p>
          <div v-if="storeAlarm.alarm.message !='새로운 알람이 없습니다.'" class="speed-indicator">
            <label @click="showModal"><strong>충전하러가기</strong></label>
          </div>
        </div>
      </div>

    </div>
    <div v-if="isModalVisible" class ="alert-modal" @click.self="hideModal">
        <div class="modal-content">
            
            <img class="modal-image" src="/images/charger-info.png">
            <h2>충전을 시작하시겠습니까?</h2>
            <p>주의! 충전기를 꽂아 둔 상태여야 합니다. </p>
            <button @click="moveCharginStatus" class="modal-click">충전시작</button>
            <button @click="hideModal" class="modal-btn">취소</button>
        </div>
    </div>

  </header>
</template>

<script setup>
import { onMounted, ref , watch, computed } from "vue";
import { useWebSocketStore } from '@/stores/webSocketChargingStore';
import { useAlarmWebSocketStore } from '@/stores/webSocketAlarmStore';
import { useRouter } from 'vue-router';

const router = useRouter();
const isAlertBoxVisible = ref(false);  

const store = useWebSocketStore();
const storeAlarm = useAlarmWebSocketStore();

const isModalVisible = ref(false);


storeAlarm.alarm = { message: "새로운 알람이 없습니다." };

storeAlarm.alarm = storeAlarm.alarm || { message: null };
store.finishAlarm = store.finishAlarm || { message: null };

  onMounted(() => {
    isAlertBoxVisible.value = false;
    store.connectWebSocket();
    storeAlarm.connectAlarmWebSocket()
    console.log("온마운트");
  });

//   watch(
//   () => store.chargingData.chargerType, // 감지할 값
//   (newValue) => {
//     console.log("변경된 chargerType 값:", newValue);
//     if (newValue) {
//       store.finishAlarm.message = "충전중";
//     } else {
//       store.finishAlarm.message = null;
//     }
//     console.log("store.finishAlarm.message:", store.finishAlarm.message);
//     isAlertBoxVisible.value = true
//   }
// );

watch(
  () => storeAlarm.alarm.message,
  (newValue) => {
    console.log("새로운 알림 메시지:", newValue);
    if (newValue && newValue !== "새로운 알람이 없습니다." && newValue !== null) {
      isAlertBoxVisible.value = true; // 알림 창 표시
    }else{
      isAlertBoxVisible.value = false;
    }
  }
);

watch(
  () => store.finishAlarm.message,
  (newValue) => {
    console.log("새로운 알림 메시지:", newValue);
    if (newValue && newValue !== "새로운 알람이 없습니다.") {
      isAlertBoxVisible.value = true; // 알림 창 표시
    }
  }
);


  // 알림창 토글
const toggleAlertBox = () => {
  isAlertBoxVisible.value = !isAlertBoxVisible.value;
};



const currentBatteryPercent = ref(0); 

watch(
  () => store.chargingData.batteryPercent,
  (newValue) => {
    console.log("업데이트된 배터리 퍼센트:", newValue);
    currentBatteryPercent.value = newValue || 0; // 값이 없을 경우 0으로 처리
    store.finishAlarm.message = "충전 중인 상태입니다.";
    if(currentBatteryPercent.value == 100){
      store.finishAlarm.message="충전 완료";
      isAlertBoxVisible.value = true;
    }
  },
  { immediate: true } // 처음 마운트 시에도 실행
);


// 로그인 상태 관리
const isLoggedIn = ref(true);



// 페이지 새로고침
const reloadPage = () => {
  window.location.reload();
};

// 로그아웃 함수
const logout = () => {
  console.log("로그아웃 실행");
  isLoggedIn.value = false;
};



// 충전 속도에 따른 퍼센트 반환
const getSpeedPercentage = (batteryPercent) => Math.min(batteryPercent, 100);


// 충전 속도에 따른 색상 반환
const getSpeedColor = (percent) => {
  if (percent < 20) return "#f44336"; // 빨간색
  if (percent < 50) return "#ff9800"; // 노란색
  if (percent < 80) return "#ffeb3b" ; // 주황색
  return "#4caf50" ; // 녹색
};

const showModal = () => {
    isModalVisible.value = true;
}

const hideModal = () => {
    isModalVisible.value = false;
};

const moveCharginStatus = () => {
  router.push({
    name: 'ChargingStatus',
    query: {
      reservationId: storeAlarm.alarm.reservationId,
    },
  }).then(() => {
    storeAlarm.clearAlarmMessage();
    window.location.reload(); // 페이지 새로고침 -> 웹소켓이 연결되어있으므로 리로딩.
    // 웹소켓 세션 종료니 다시 연결함.(다른곳에선 쓰이면 안됨.)
    
  });
  
};

const movePaymentPrice = () => {
  router.push({
    name:'ChargingStatus'
  })
}



</script>

<style scoped>
/* 헤더 스타일 */
#header {
  background-color: #333;
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
  height: 40px;
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


.alert-modal{
    display: none;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.6);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1001;
}

.modal-content{
    background-color: #fff;
    padding: 20px;
    border-radius: 10px;
    width: 400px;
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
    text-align: center;
}
.modal-btn {
  margin-top: 15px;
  padding: 8px 36px;
  background-color: #333;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}
.modal-btn:hover {
  background-color: #555;
}
.modal-image{
    width: 50%;
}
.modal-click{
  margin-top: 15px;
  margin-right: 20px;
  padding: 5px 16px;
  background-color: #fff;
  color: #333;
  border: 3px solid;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;

}
.modal-click:hover {
  background-color: #e6e6e6;
}
</style>

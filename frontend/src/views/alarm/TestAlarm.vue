<template>
    <!-- 이동 버튼 -->
    <button @click="moveToTestAlarm">ChargingStatus 이동</button>
    
    <!-- 알림창 -->
    <div class="alert-box" v-if="store.finishAlarm.message && showAlert">
      {{ store.finishAlarm.message }}
      <button class="close-btn" @click="closeAlert">X</button>
    </div>
  </template>
  
  <script setup>
  import { onMounted, ref } from 'vue';
  import { useWebSocketStore } from '@/stores/webSocketChargingStore';
  import { useRouter } from 'vue-router';
  
  const router = useRouter();
  
  // 상태 변수: 알림창 상태 컨트롤
  const showAlert = ref(true);
  
  const moveToTestAlarm = () => {
    router.push({
      name: 'ChargingStatus',
    });
  };
  
  const store = useWebSocketStore();
  
  onMounted(() => {
    store.connectWebSocket();
  });
  
  // 알림창 닫기
  const closeAlert = () => {
    showAlert.value = false;
  };
</script>
  
<style scoped>
  /* 알림 스타일 */
.alert-box {
    position: fixed;
    top: 20px;
    right: 20px;
    background-color: #ffffff;
    color: #000000;
    padding: 10px 20px;
    border: 1px solid #3f3f3f;
    border-radius: 5px;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
    font-size: 14px;
    animation: fadeIn 0.5s ease-in-out;
    z-index: 1000;
    display: flex;
    align-items: center;
}

  /* 닫기 버튼 스타일 */
.close-btn {
    margin-left: 10px;
    background: transparent;
    border: none;
    color: #3f3f3f;
    font-size: 16px;
    cursor: pointer;
}

  /* fadeIn 애니메이션 */
@keyframes fadeIn {
    from {
    opacity: 0;
    transform: translateY(-10px);
    }
    to {
    opacity: 1;
    transform: translateY(0);
    }
}
</style>
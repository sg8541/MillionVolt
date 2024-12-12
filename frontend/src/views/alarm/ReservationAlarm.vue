<template>
    <button @click="moveToTestAlarm">TestAlarm으로 이동</button>
    <div class="alert-box" v-if="store.alarm.message && showAlert">
           <div> {{store.alarm.message}} </div> <br>
            <div @click="moveCharginStatus">충전하러 가기</div>
            <button class="close-btn" @click="closeAlert">X</button>
    </div>
    <div></div>
    <div></div>

</template>

<script setup>
import { onMounted } from 'vue';
import { useAlarmWebSocketStore } from '@/stores/webSocketAlarmStore';
import { useRouter } from 'vue-router';

const store = useAlarmWebSocketStore();
const router = useRouter();
const showAlert = ref(true);
onMounted(()=>{
    store.connectAlarmWebSocket();
    console.log("온마운트됨.");
})


const moveToTestAlarm = () => {
    router.push({
        name:'TestAlarm',
    })
}

const moveCharginStatus = () => {
    router.push({
        name:'ChargingStatus',
        query : {
            reservationId : store.alarm.reservationId,
        }
    })
}
const closeAlert = () => {
    showAlert.value = false;
    store.clearAlarmMessage();
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
    border: 1px solid #c7c6c6;
    border-radius: 15px;
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
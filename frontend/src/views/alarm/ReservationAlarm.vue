<template>
    <button @click="moveToTestAlarm">TestAlarm으로 이동</button>
    <div class="alert-box" v-if="store.alarm.message && showAlert">
            <div class="message">
                {{store.alarm.message}} 
            </div> <br>
            <!-- <div @click="moveCharginStatus" class="">
                충전하러 가기
            </div> -->
            <div @click="showModal" class="moveCharging">
                충전하러 가기
            </div>
            <button class="close-btn" @click="closeAlert">X</button>
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
    

</template>

<script setup>
import { onMounted } from 'vue';
import { useAlarmWebSocketStore } from '@/stores/webSocketAlarmStore';
import { useRouter } from 'vue-router';

const store = useAlarmWebSocketStore();
const router = useRouter();
const isModalVisible = ref(false);

const showAlert = ref(true);
onMounted(()=>{
    store.connectAlarmWebSocket();
    console.log("온마운트됨.");
})

const showModal = () => {
    isModalVisible.value = true;
}

const moveToTestAlarm = () => {
    router.push({
        name:'TestAlarm',
    })
}

const hideModal = () => {
    isModalVisible.value = false;
};

const moveCharginStatus = () => {
    router.push({
        name:'ChargingStatus',
        query : {
            reservationId : store.alarm.reservationId,
        }
    })
    store.clearAlarmMessage();
}



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

.message{
    margin : 10px;
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
<template>
    <div class="charger-cards">
        <div class="charger-card-detail" v-for="(item, index) 
        in store.chargeState" :key="index" @click="showModal(index)">
            <div class="charger-num">
                충전기 {{item.chargerId}}
            </div>
            <div class="charger-type">
                <div v-if="item.chargerSpeedId === 1">
                    충전기 타입 : 7kW
                </div>
                <div v-else-if="item.chargerSpeedId === 2">
                    충전기 타입 : 50kW
                </div>
                <div v-else-if="item.chargerSpeedId === 3">
                    충전기 타입 : 100kW
                </div>
                <div v-else-if="item.chargerSpeedId === 4">
                    충전기 타입 : 200kW
                </div>
                <div v-else="item.chargerSpeedId">
                    충전기 타입 : 300kW 이상
                </div>
            </div>
            <div class ="charger-state">
                <div v-if="item.chargerStatusId === 1" class="available">
                    사용가능
                </div>
                <div v-else-if="item.chargerStatusId === 2" class="in-use">
                    사용중
                </div>
                <div v-else="item.chargerStatusId" class="maintenance">
                    사용불가
                </div>
            </div>
        </div>
    </div>

    <div v-if="isModalVisible" class="charger-modal" @click.self="hideModal">
        <div class="modal-content">
            <h3>충전기 {{chargerId }}({{ chargerSpeedId }}) </h3>
    
            <img class ="modal-image" src="/images/charger2.png">
            
            <h2>충전 예약을 하시겠어요?</h2>
            <button @click="reservationMove" class="modal-click">예약하러가기</button>
            <button @click="hideModal" class="modal-btn">취소</button>
        </div>
    </div>
</template>

<script setup>
import { onMounted, onBeforeUnmount , ref   } from 'vue'; 
import { chargerStateChangeWebSocketStore } from '@/stores/webSocketChargerChangeStore';
import { useRouter } from 'vue-router';

const store = chargerStateChangeWebSocketStore();
const isModalVisible = ref(false);

const router = useRouter();

const chargerId = ref('');
const chargerSpeedId = ref('');
const stationId = ref('');

onMounted(()=> {
    store.connectCharger();
});

onBeforeUnmount(()=>{
    store.disconnectWebSocket();
})

const showModal =(index) => {
    if(store.chargeState[index].chargerStatusId === 3){
        alert("사용할 수 없는 충전기 입니다.");
        return;
    }
    chargerId.value = store.chargeState[index].chargerId;
    if(store.chargeState[index].chargerSpeedId === 1){
        chargerSpeedId.value='7kW';
    }
    if(store.chargeState[index].chargerSpeedId === 2){
        chargerSpeedId.value='50kW';
    }
    if(store.chargeState[index].chargerSpeedId === 3){
        chargerSpeedId.value='100kW';
    }
    if(store.chargeState[index].chargerSpeedId === 4){
        chargerSpeedId.value='200kW';
    }
    if(store.chargeState[index].chargerSpeedId === 5){
        chargerSpeedId.value='300kW 이상';
    }
    isModalVisible.value = true;
};
const hideModal = () => {
    isModalVisible.value = false;
};

const reservationMove = () => {
    router.push({
        name: 'Reservation',
        query : {
            stationId :1, // 동적으로 부여할 예정
            chargerId:chargerId.value, // 충전기번호
            chargerSpeedId : chargerSpeedId.value
        }
    })
}

</script>

<style scoped>
@font-face {
    font-family: 'NoonnuBasicGothicRegular';
    src: url('https://fastly.jsdelivr.net/gh/projectnoonnu/noon-2410@1.0/NoonnuBasicGothicRegular.woff2') format('woff2');
    font-weight: normal;
    font-style: normal;
}
* {
    font-family: 'NoonnuBasicGothicRegular';
}

.charger-cards {
  display: flex;
  flex-direction: row; /* 가로로 나열 */
  flex-wrap: wrap; /* 화면 너비를 넘어가면 다음 줄로 이동 */
  gap: 16px; /* 각 카드 사이 간격 */
  justify-content: flex-start;
  padding: 10px;
}

.charger-card-detail {
  border: 3px solid #333; /* 테두리 추가 */
  border-radius: 8px; /* 모서리 둥글게 */
  padding: 10px;
  width: 250px; /* 각 카드의 너비 고정 */
  box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.2); /* 그림자 효과 */
  background-color: #f9f9f9; /* 배경색 */
  transition: all 0.2s ease-in-out; /* 마우스 호버 시 부드러운 애니메이션 */
}

.charger-card-detail:hover {
  transform: scale(1.05); /* 마우스 호버 시 확대 */
}

.charger-num,
.charger-state,
.charger-type {
  margin-bottom: 8px;
  font-size: 14px;
  line-height: 1.5;
}

.charger-state div,
.charger-type div {
  font-weight: bold;
}

.charger-num {
    color: #3e3e3e;
    font-weight: bold;
}

/* 상태별 색상 스타일링 */
.available {
  color: rgb(32, 82, 32);
}

.in-use {
  color: rgb(219, 43, 43);
}

.maintenance {
  color: rgb(175, 175, 175);
}

.charger-type div:nth-child(1) {
  color: #5c5c5c;
}

.charger-modal {
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
  z-index: 1000;
}

.modal-content {
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
  padding: 5px 8px;
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
<template>
    <div class="pay-info-title">결제 상세 정보</div>
    
        <div class="pay-info-place-title">주소</div>
        <div class="pay-info-place-print">
            <div v-if="stationInfo">
                <h5>{{stationInfo.address}}</h5>
                <h5>{{stationInfo.name}}</h5>
            </div>
        </div>

        <div class="pay-info-amount-title">결제금액</div>
        <div class="pay-info-amount-print">
            <label>이용요금</label>
            <strong class="amount-label">{{amount}}</strong>
            <br>
            <label>이용 초과금</label>
            <strong class="penalty-label">-{{penaltyAmount}}</strong>
            <hr>
            <label>총 결제금액</label>
            <strong class="finaleAmount-label">{{finaleAmount}}</strong>
        </div>


        <div class="pay-info-hourAndCharge-title">충전 시간 및 충전량</div>
        <div class="pay-info-hourAndCharge-print">
            <h5>{{formatDate(chargeStart)}} ~ {{formatDate(chargeEnd)}}</h5>
        </div>

        <div>
            <button class="pay-button" @click="payment">결제하기</button>
        </div>
</template>

<script setup>
import { onMounted } from 'vue';
import axios from 'axios';
import { useRoute } from 'vue-router';

const route = useRoute();

function formatDate(timestamp) {
    const date = new Date(timestamp);
    const year = date.getFullYear();
    const month = date.getMonth() + 1; // 월은 0부터 시작하므로 +1
    const day = date.getDate();
    const hours = date.getHours();
    const minutes = date.getMinutes();
    return `${year}년 ${month}월 ${day}일 ${hours}시 ${minutes}분`;
}


const userId = ref(null);
const stationId = ref(null);
const reservationId = ref(null);
const amount = ref(0);
const chargeStart = ref(null);
const chargeEnd = ref(null);
const chargingKwh = ref(null);
const chargerId = ref(null);

const finaleAmount = computed(() => {
    return amount.value - penaltyAmount.value; // 총 결제 금액 계산
});

onMounted(() => {
console.log('route.query:', route.query);
    userId.value = route.query.userId;
    stationId.value = route.query.stationId;
    reservationId.value = route.query.reservationId;
    chargeStart.value = route.query.chargeStart;
    chargeEnd.value = route.query.chargeEnd;
    chargerId.value = route.query.chargerId;
    chargingKwh.value = route.query.chargingKwh;
    amount.value = parseFloat(route.query.amount);
    stationAdress();
});

const stationAdress = async() => {
    const stationInfoResponse = await axios.get(
        `http://localhost:8081/api/v1/payment/printStationInfo/2`
    )
    stationInfo.value = stationInfoResponse.data;
};

const stationInfo = ref(null);
const payment = () => {
    const imp = window.IMP;
    imp.init("imp50578251");
    imp.request_pay(
    {
        pg: "html5_inicis",
        pay_method: "카카오페이",
        merchant_uid: "order_" + new Date().getTime(),
        amount: 100,
        name: "람보르기니 우라칸",
    },
    async (rsp) => {
        if (rsp.success) {
        console.log("결제 성공", rsp);
          // 결제 성공 시 서버로 결제 정보 전송
        try {
            const response = await axios.post(
            `http://localhost:8081/api/v1/payment/save/${rsp.imp_uid}`,
            {
                imp_uid: rsp.imp_uid, // Iamport 결제 고유 ID
                paymentMethod: rsp.pay_method, // 결제 방법
                paymentStatus: rsp.status, // 결제 상태 (성공/실패)
                amount: rsp.paid_amount, // 결제 금액
                userId: userId.value, // 유저의 아이디 
                reservationId: reservationId.value, // 예약번호
                stationId: stationId.value, // 충전소 번호
                chargerId: chargerId.value, // 충전기 번호 
                chargeStart: chargeStart.value, // 충전 시작 시간
                chargeEnd:  chargeEnd.value,// 충전 종료 시간
            });
            window.location.href = "/";
            console.log("서버 응답:", response.data);
        } catch (error) {
            console.error("서버 전송 실패:", error);
        }
        } else {
        console.log("결제 실패", rsp);
        }
    }
    );
};
</script>

<style>
.amount-label {
    padding-left: 450px;
}

.penalty-label {
    padding-left: 450px;
}

.final-amount {
    padding-left: 450px;
}

.pay-button {
  background-color: black; /* 배경색 검은색 */
  color: white; /* 텍스트 색상 흰색 */
  border: none; /* 기본 테두리 제거 */
  padding: 10px 20px; /* 여백 설정 (상하, 좌우) */
  font-size: 16px; /* 폰트 크기 */
  font-weight: bold; /* 텍스트 굵게 */
  border-radius: 10px; /* 모서리 둥글게 */
  cursor: pointer; /* 마우스를 올렸을 때 포인터 모양 */
  transition: background-color 0.3s ease; /* 배경색 변화 애니메이션 */
  width: 600px;
  margin: 20px auto; /* 가로축 가운데 정렬 */
  display: block; /* block 속성으로 설정 */
  text-align: center;
}

.pay-info-title {
  font-size: 30px; /* 원하는 폰트 크기 */
  font-weight: bold; /* 폰트 두께 */
  color: #333; /* 텍스트 색상 */
  margin: 20px auto 5px;
}

.pay-info-amount-title, .pay-info-hourAndCharge-title, .pay-info-place-title{
  font-size: 24px; /* 원하는 폰트 크기 */
  font-weight: bold; /* 폰트 두께 */
  color: #333; /* 텍스트 색상 */
  margin-bottom: 5px; /* 아래쪽 마진 추가 */
  margin-top: 20px;
  margin-left: calc(28.5vw + 0px);
}

.pay-info-place-print, .pay-info-amount-print,.pay-info-hourAndCharge-print {
  background-color: #ffffff; /* 컨테이너 배경색 */
  border-radius: 10px; /* 모서리를 둥글게 */
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); /* 부드러운 그림자 효과 */
  padding: 20px; /* 내부 여백 */
  margin: 15px auto; /* 세로 간격 + 중앙 정렬 */
  border: 1px solid #ccc; /* 얇은 테두리 */
  width: 600px; /* 넓이를 화면의 80%로 설정 */
}
</style>


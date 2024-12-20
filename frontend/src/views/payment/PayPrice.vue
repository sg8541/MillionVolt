<template>
    <div class="wrap">
        <div class="header">
            <div class="logo">
                <router-link to="/main">
                    <img src="images/logo.png" alt="백만볼트 로고">
                </router-link>
            </div>
            <div class="container">
                <div class="pay-info-title">결제 상세 정보</div>

                <div class="pay-info-place-title">주소</div>
                <div class="pay-info-place-print">
                    <div v-if="stationInfo">
                        <h5>{{ stationInfo.address }}</h5>
                        <h5>{{ stationInfo.name }}</h5>
                    </div>
                </div>

                <div class="pay-info-amount-title">이용금액</div>
                <div class="pay-info-amount-print">
                    <!-- <label>이용요금</label> -->
                    <strong class="amount-label">{{ amount }}</strong>
                    <!-- <label>이용 초과금</label>
                    <strong class="penalty-label">-{{ penaltyAmount }}</strong>
                    <hr> -->
                </div>
                <div class="pay-info-amount-title">선결제 금액</div>
                <div class="pay-info-amount-print">
                    <strong class="amount-label">{{ amount }}</strong>
                </div>
                <div class="pay-info-amount-title">결제 금액</div>
                <div class="pay-info-amount-print">
                    <strong class="amount-label">{{ amount }}</strong>
                </div>
                <div class="pay-info-hourAndCharge-title">충전 시간 및 충전량</div>
                <div class="pay-info-hourAndCharge-print">
                    <h5>{{ formatDate(chargeStart) }} ~ {{ formatDate(chargeEnd) }}</h5>
                </div>

                <div>
                    <button class="pay-button" @click="payment">결제하기</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { onMounted } from 'vue';
import axios from 'axios';
import { useRoute } from 'vue-router';

const route = useRoute();

//날짜와 시간 형식 변환
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
const stationInfo = ref(null);

// const finaleAmount = computed(() => {
//     return amount.value - penaltyAmount.value; // 총 결제 금액 계산
// });

onMounted(() => {
    console.log('route.query:', route.query);
    userId.value = route.query.userId;
    stationId.value = route.query.stationId;
    //reservationId.value = route.query.reservationId;
    chargeStart.value = route.query.chargeStart;
    chargeEnd.value = route.query.chargeEnd;
    chargerId.value = route.query.chargerId;
    chargingKwh.value = route.query.chargingKwh;
    amount.value = parseFloat(route.query.amount);
    stationAdress();
});


const stationAdress = async () => {
    const stationInfoResponse = await axios.get(
        `http://localhost:8081/api/v1/payment/printStationInfo/${stationId}`
    )
    stationInfo.value = stationInfoResponse.data;
};

//결제 API
const payment = () => {
    const imp = window.IMP;
    imp.init("imp50578251");
    imp.request_pay(
        {
            pg: "html5_inicis",
            pay_method: "카카오페이",
            merchant_uid: "order_" + new Date().getTime(),
            paid_amount: amount.value,
            // name: "람보르기니 우라칸",
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
                            chargeEnd: chargeEnd.value, // 충전 종료 시간
                        }
                    );
                    window.location.href = "/";
                    console.log("서버 응답:", response.data);
                } catch (error) {
                    console.error("서버 전송 실패:", error);
                }
            }
        }
    }
}
    );
};

</script>
<style>
.wrap {
    font-family: Arial, sans-serif;
    display: flex;
    justify-content: center;
    margin: 0;
    background-color: white;
}

.container {
    width: 650px;
    background-color: #fff;
    border: 1px solid #969696;
    padding: 30px 24px 28px 24px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    border-radius: 10px;
    text-align: center;
    align-content: center;
}

.header {
    text-align: center;
    margin-bottom: 10%;
}

.logo img {
    width: 150px;
}

.amount-label {
    float: inline-end;
    font-weight: 500;
}

.penalty-label {
    padding-left: 450px;
}

.final-amount {
    padding-left: 450px;
}

.pay-button {
    background-color: #52616A;
    width: 100%;
    height: 68px;
    color: white;
    border: none;
    padding: 10px 20px;
    /* 여백 설정 (상하, 좌우) */
    font-size: 17px;
    /* 폰트 크기 */
    font-weight: bold;
    /* 텍스트 굵게 */
    border-radius: 10px;
    /* 모서리 둥글게 */
    cursor: pointer;
    transition: background-color 0.3s ease;
    display: block;
    text-align: center;
    margin-top: 30px;

}

.pay-button:hover {
    background-color: #1E2022;
}

.pay-info-title {
    font-size: 28px;
    font-weight: bold;
    color: #52616A;
    margin: 10px 0px 48px 0px;
    white-space: nowrap;
}

.pay-info-amount-title,
.pay-info-hourAndCharge-title,
.pay-info-place-title {
    width: 95%;
    font-size: 17px;
    font-weight: bold;
    color: #1E2022;
    margin-bottom: 5px;
    margin-top: 20px;
    white-space: nowrap;
    display: flex;
}

p {
    margin: 0px;
    padding: 0px;
}

.pay-info-place-print,
.pay-info-amount-print,
.pay-info-hourAndCharge-print {
    background-color: #ffffff;
    border-radius: 10px;
    padding: 20px;
    margin: 0px 0px 22px 0px;
    border: 1px solid #C9D6DE;
    color: #52616A;
    height: 60px;
}

h5 {
    font-size: 16px;
    font-weight: 400;
}
</style>

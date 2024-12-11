<!-- <template>
    <div>
    <button @click="payWithIamport">결제하기</button>
    </div>
</template>

<script>
import axios from 'axios';
import { WebSocketCharginStore } from "@/stores/WebSocketCharginStore";

export default {
    name: "Payment",
    methods: {
    payWithIamport() {
        const imp = window.IMP; // Iamport 객체

        imp.init("imp50578251");

        imp.request_pay(
        {
            pg: "html5_inicis",
            pay_method: "카카오페이", 
            merchant_uid: "order_" + new Date().getTime(),
            amount: 100, // 결제 금액
            name: "람보르기니 우라칸", // 상품명
        },
        async (rsp) => {
            if (rsp.success) {
            console.log("결제 성공", rsp);

              // 결제 성공 시 서버로 결제 정보 전송
            try {
                const response = await axios.post(`http://localhost:8081/api/v1/payment/save/${rsp.imp_uid}`, {
                  imp_uid: rsp.imp_uid,  // Iamport 결제 고유 ID
                  paymentId: rsp.merchant_uid,  // 주문 고유 ID
                  payment_method: rsp.pay_method,  // 결제 방법
                  payment_status: rsp.status,// 결제 상태 (성공/실패)
                  amount: rsp.paid_amount,  // 결제 금액
                  userId: , //유저의 아이디
                  reservationId: , //예약번호
                  stationId: , //충전기 번호
                  chargeStart: ,//충전시작 시간
                  chargeEnd: //충전종료 시간
                });

                console.log('서버 응답:', response.data);
            } catch (error) {
                console.error('서버 전송 실패:', error);
            }
            } else {
            console.log("결제 실패", rsp);
            }
        }
        );
    }
    }
};
</script> -->

<template>
    <div>
    <button @click="payment">결제하기</button>
    </div>
</template>

<script setup>
import { onMounted } from 'vue';
import axios from 'axios';
import { useRoute } from 'vue-router';

const route = useRoute();


const userId = ref(null);
const stationId = ref(null);
const reservationId = ref(null);
const amount = ref(0);

onMounted(() => {

console.log('route.query:', route.query);
    userId.value = route.query.userId;
    stationId.value = route.query.stationId;
    reservationId.value = route.query.reservationId;
    amount.value = parseFloat(route.query.amount);
});


const payment = () => {
    const imp = window.IMP; // Iamport 객체

    imp.init("imp50578251");

    imp.request_pay(
    {
        pg: "html5_inicis",
        pay_method: "카카오페이",
        merchant_uid: "order_" + new Date().getTime(),
        amount: 100, // 결제 금액
        name: "람보르기니 우라칸", // 상품명
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
                paymentId: 2, // 주문 고유 ID
                payment_method: rsp.pay_method, // 결제 방법
                payment_status: rsp.status, // 결제 상태 (성공/실패)
                amount: rsp.paid_amount, // 결제 금액
                userId: userId.value, // 유저의 아이디 (필요 시 채워넣기)
                reservationId: reservationId.value, // 예약번호
                stationId: stationId.value, // 충전기 번호
                chargeStart: "", // 충전 시작 시간
                chargeEnd: "" // 충전 종료 시간
            }
            );

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


<template>
    <div>
    <button @click="payWithIamport">결제하기</button>
    </div>
</template>

<script>
import axios from 'axios';

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
                  merchant_uid: rsp.merchant_uid,  // 주문 고유 ID
                  amount: rsp.paid_amount,  // 결제 금액
                  payment_method: rsp.pay_method,  // 결제 방법
                  payment_status: rsp.status,// 결제 상태 (성공/실패)
                  user_id: 3,  
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
</script>

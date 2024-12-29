<script setup>
import PaymentTable from '@/views/pages/tables/PaymentTable.vue'
import { onMounted, ref} from 'vue';
import api from '@/axios'
import { useRoute } from 'vue-router'
import axios from 'axios';


const route = useRoute()
const id = route.params.id;

const info = ref(null)

const fetcPaymentInfo = async (id) => {
 
 try{
   const response = await api.get('/myinfo/payment/'+id)
   console.log(response);
   console.log("payment id : " + id);
   info.value = response.data;
 }catch(error){
   console.error("Error fetching Payment info:", error);
 }
}

const refundDeposit = async (reservationId) => {
  try {
    const refundDepositResponse = await axios.post(`http://localhost:8081/api/v1/refundDeposit`,{ reservationId });
    console.log(reservationId);
    console.log("환불 응답 확인: ", refundDepositResponse);
    // 환불 상태 업데이트
    alert(refundDepositResponse.data.message); 
  } catch (error) {
    console.error("Error processing refund:", error);
  }
};

onMounted(()=>{
  fetcPaymentInfo(id);
})

</script>

<template>
  <VRow>
    <VCol cols="12">
      <VCard title="나의 결제 내역">
        <PaymentTable   v-if="Array.isArray(info) && info.length" :payments="info" @refund="refundDeposit"/>
      </VCard>
    </VCol>
  </VRow>
</template>

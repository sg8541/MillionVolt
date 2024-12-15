<script setup>
import AnalyticsCongratulations from '@/views/dashboard/AnalyticsCongratulations.vue'
import AnalyticsTotalRevenue from '@/views/dashboard/AnalyticsTotalRevenue.vue'
import AnalyticsPaymentTable from '@/views/dashboard/AnalyticsPaymentTable.vue'
import AnalyticsReservationTable from '@/views/dashboard/AnalyticsReservationTable.vue'

import { defineProps } from 'vue'
import api from '@/axios'

const props = defineProps({
  id: {
    type: Number,
    required: true
  }
})

const info = ref(null)

const fetchMyInfo = async (id) => {
 
 try{
   const response = await api.get(`/myinfo/dashboard/${id}`)
   console.log(response);
   info.value = response.data;
   console.log("dashboard id : " + id);
 }catch(error){
   console.error("Error fetching my info:", error);
 }
}
onMounted(()=>{
  fetchMyInfo(props.id);
})

</script>

<template>
  <VRow>
    <!-- 👉 Congratulations -->
    <VCol cols="12" md="12">
      <AnalyticsCongratulations v-if="info && info.userCarInfo" :user-car-info="info.userCarInfo" />
    </VCol>

    <!-- 👉 Total Revenue -->
    <VCol
      cols="12"
      md="12"
      order="2"
      order-md="1"
    >
      <!-- info.totalRevenue가 있을 때만 AnalyticsTotalRevenue 렌더링 -->
      <!-- <AnalyticsTotalRevenue  /> -->
      <AnalyticsTotalRevenue v-if="info" :payment-chart-list="info.paymentChartList" />
    </VCol>

    <!-- 👉 ReservationTable -->
    <VCol
      cols="12"
      md="12"
      order="3"
    >
      <!-- info.reservationList가 있을 때만 AnalyticsReservationTable 렌더링 -->
      <AnalyticsReservationTable v-if="info && info.reservationList" :reservations="info.reservationList" />
    </VCol>

    <!-- 👉 PaymentTable -->
    <VCol
      cols="12"
      md="12"
      order="3"
    >
      <!-- info.paymentList가 있을 때만 AnalyticsPaymentTable 렌더링 -->
      <AnalyticsPaymentTable v-if="info && info.paymentList" :payments="info.paymentList" />
    </VCol>
  </VRow>
</template>

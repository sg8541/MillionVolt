<script setup>
import AnalyticsCongratulations from '@/views/dashboard/AnalyticsCongratulations.vue'
import AnalyticsTotalRevenue from '@/views/dashboard/AnalyticsTotalRevenue.vue'
import AnalyticsPaymentTable from '@/views/dashboard/AnalyticsPaymentTable.vue'
import AnalyticsReservationTable from '@/views/dashboard/AnalyticsReservationTable.vue'

// 👉 Images
</script>

<template>
  <VRow>
    <!-- 👉 Congratulations -->
    <VCol
      cols="12"
      md="12"
    >
      <AnalyticsCongratulations />
    </VCol>

    <!-- 👉 Total Revenue -->
    <VCol
      cols="12"
      md="12"
      order="2"
      order-md="1"
    >
      <AnalyticsTotalRevenue />
    </VCol>

    <!-- 👉 ReservationTable -->
    <VCol
      cols="12"
      md="12"
      order="3"
    >
      <AnalyticsReservationTable />
    </VCol>

    <!-- 👉 PaymentTable -->
    <VCol
      cols="12"
      md="12"
      order="3"
    >
      <AnalyticsPaymentTable />
    </VCol>
    
  </VRow>
</template>

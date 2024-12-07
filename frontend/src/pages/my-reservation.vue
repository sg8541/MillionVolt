<script setup>
import ReservationTable from '@/views/pages/tables/ReservationTable.vue'

</script>

<template>
  <VRow>
    <VCol cols="12">
      <VCard title="나의 예약 내역">
        <ReservationTable />
      </VCard>
    </VCol>
  </VRow>
</template>

<script setup>
defineProps({
  reservations: {
    type: Array,
    required: true
  }
})


const getStatusText = (status) => {
  const statusMap = {
    completed: '예약완료',
    pending: '예약대기',
    cancelled: '예약취소',
    confirmed: '예약확정'
  };
  return statusMap[status] || '알 수 없음';
};

</script>

<template>
  <VTable>
    <thead>
      <tr>
        <th class="text-uppercase">
          충전소 명
        </th>
        <th>
          충전기
        </th>
        <th>
          예약일
        </th>
        <th>
          예약 시간
        </th>
        <th>
          예약 상태
        </th>
        <th>
          예약 금액
        </th>
      </tr>
    </thead>

    <tbody>
      <tr
        v-for="item in reservations"
        :key="item.id"
      >
        <td>
          {{ item.name }}
        </td>
        <td class="text-center">
          {{ item.chargerId }}
        </td>
        <td class="text-center">
          {{ item.createdAt }}
        </td>
        <td class="text-center">
          {{ item.startTime }} - {{ item.endTime }}
        </td>
        <td class="text-center">
          {{ getStatusText(item.status) }}
        </td>
        <td class="text-center">
          5,000원
        </td>
      </tr>
    </tbody>
  </VTable>
</template>

<script setup>
defineProps({
  payments: {
    type: Array,
    required: true
  }
})

const emit = defineEmits(['refund']);

const getStatusText = (status) => {
  const statusMap = {
    completed: '결제완료',
    pending: '결제대기',
    failed: '결제실패',
  };
  return statusMap[status] || '알 수 없음';
};

const showAlert = () => {
  alert('보증금 환불 신청이 완료되었습니다');
};
</script>

<template>
  <VTable>
    <thead>
      <tr>
        <th class="text-uppercase">
          결제 장소
        </th>
        <th>
          충전기
        </th>
        <th>
          결제 날짜
        </th>
        <th>
          충전 시작 - 끝난 시간
        </th>
        <th>
          충전량
        </th>
        <th>
          결제 상태
        </th>
        <th>
          결제 금액
        </th>
        <th>
          보증금 환불
        </th>
      </tr>
    </thead>

    <tbody>
      <tr
        v-for="item in payments"
        :key="item.id"
      >
        <td>
          {{ item.name }}
        </td>
        <td class="text-center">
          {{ item.chargerId }}
        </td>
        <td class="text-center">
          {{ item.updatedAt }}
        </td>
        <td class="text-center">
          {{ item.chargeStart }} - {{ item.chargeEnd }}
        </td>
        <td class="text-center">
          {{ item.chargedEnergy }}
        </td>
        <td class="text-center">
          {{ getStatusText(item.paymentStatus) }}
        </td>
        <td class="text-center">
          {{ item.amount }}원
        </td>
        <td class="text-center">
          <button @click="showAlert">
            보증금 환불
          </button>
          <!-- <button @click="emit('refund', item.reservationId)">
            보증금 환불
          </button> -->
        </td>
      </tr>
    </tbody>
  </VTable>
</template>

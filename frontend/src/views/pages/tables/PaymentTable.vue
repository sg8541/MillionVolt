<script setup>
const payment = [
{
    name: 'Frozen Yogurt', // 결제장소
    chargerId: 1, // 충전기번호
    updatedAt: '24.12.05', // 결제완료시간
    chargeStart: '12:00', // 충전 시작시간
    chargeEnd: '15:00', // 충전완료시간
    paymentStatus: 'completed', // 결제 상태
    amount: 10000 // 결제금액
  },
  {
    name: 'Ice Cream', // 결제장소
    chargerId: 2, // 충전기번호
    updatedAt: '24.12.05', // 결제완료시간
    chargeStart: '13:00', // 충전 시작시간
    chargeEnd: '16:00', // 충전완료시간
    paymentStatus: 'pending', // 결제 상태
    amount: 8000 // 결제금액
  },
  {
    name: 'Brownie', // 결제장소
    chargerId: 3, // 충전기번호
    updatedAt: '24.12.05', // 결제완료시간
    chargeStart: '14:00', // 충전 시작시간
    chargeEnd: '17:00', // 충전완료시간
    paymentStatus: 'failed', // 결제 상태
    amount: 5000 // 결제금액
  },
];

const getStatusText = (status) => {
  const statusMap = {
    completed: '결제성공',
    pending: '결제중지',
    failed: '결제실패',
  };
  return statusMap[status] || '알 수 없음';
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
          충전기 번호
        </th>
        <th>
          결제 날짜
        </th>
        <th>
          충전 시작 - 끝난 시간
        </th>
        <th>
          결제 상태
        </th>
        <th>
          결제 금액
        </th>
      </tr>
    </thead>

    <tbody>
      <tr
        v-for="item in payment"
        :key="item.payment"
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
          {{ getStatusText(item.paymentStatus) }}
        </td>
        <td class="text-center">
          {{ item.amount }}
        </td>
      </tr>
    </tbody>
  </VTable>
</template>

<script setup>
const desserts = [
{
    name: 'Frozen Yogurt',
    chargerId: 1,
    createdAt : '24.12.05',
    startTime: '12:00',
    endTime: '15:00',
    status: 24,
    amount: 4,
  },
{
    name: 'Frozen Yogurt',
    chargerId: 1,
    createdAt : '24.12.05',
    startTime: '12:00',
    endTime: '15:00',
    status: 24,
    amount: 4,
  },
{
    name: 'Frozen Yogurt',
    chargerId: 1,
    createdAt : '24.12.05',
    startTime: '12:00',
    endTime: '15:00',
    status: 24,
    amount: 4,
  },
{
    name: 'Frozen Yogurt',
    chargerId: 1,
    createdAt : '24.12.05',
    startTime: '12:00',
    endTime: '15:00',
    status: 24,
    amount: 4,
  },

]
</script>

<template>
  <VTable>
    <thead>
      <tr>
        <th class="text-uppercase">
          예약 장소
        </th>
        <th>
          충전기 번호
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
        v-for="item in desserts"
        :key="item.dessert"
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
          {{ item.status }}
        </td>
        <td class="text-center">
          {{ item.amount }}
        </td>
      </tr>
    </tbody>
  </VTable>
</template>

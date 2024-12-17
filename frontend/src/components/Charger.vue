<template>
    <div
      class="charger"
      :style="{
        cursor: isAvailable ? 'pointer' : 'not-allowed',
        backgroundColor: isAvailable ? '#e0f7fa' : '#f5f5f5',
      }"
      @click="handleClick"
    >
      <div><strong>{{ `충전기 ${index + 1}` }}</strong></div>
      <div>타입: {{ charger.type || '정보 없음' }}</div>
      <div>속도: {{ charger.speed || '정보 없음' }}</div>
      <div>상태: {{ getChargerStatus(charger.status) }}</div>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      charger: {
        type: Object,
        required: true,
      },
      index: {
        type: Number,
        required: true,
      },
    },
    computed: {
      isAvailable() {
        return this.charger.status === 1; // 1: 사용 가능
      },
    },
    methods: {
      handleClick() {
        if (this.isAvailable) {
          this.$emit('reserve', this.charger.id);
        } else {
          alert('충전기가 사용 중이거나 점검 중입니다.');
        }
      },
      getChargerStatus(status) {
        switch (status) {
          case 1:
            return '사용 가능';
          case 2:
            return '충전 중';
          case 3:
            return '점검 중';
          default:
            return '정보 없음';
        }
      },
    },
  };
  </script>
  
  <style scoped>
  .charger {
    flex: 1 1 calc(25% - 10px);
    padding: 15px;
    border: 1px solid #ddd;
    border-radius: 8px;
    text-align: center;
    font-size: 14px;
    background-color: #f9f9f9;
    transition: background-color 0.3s ease;
  }
  
  .charger:hover {
    background-color: #f0f8ff;
  }
  </style>
  
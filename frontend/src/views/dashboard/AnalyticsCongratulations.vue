<script setup>
import { useTheme } from 'vuetify'
import illustrationJohnDark from '@images/cards/illustration-john-dark.png'
import illustrationJohnLight from '@images/cards/illustration-john-light.png'
// import VerticalNavLink from '@layouts/components/VerticalNavLink.vue'

import { defineProps, ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import api from '@/axios'
const { global } = useTheme()
const illustrationJohn = computed(() => global.name.value === 'dark' ? illustrationJohnDark : illustrationJohnLight)

const route = useRoute()

const id = route.params.id;

defineProps({
  userCarInfo: {
    type: Object,
    required: true
  }
})

// 상태 관리
const showModal = ref(false)
const carBattery = ref('')

// 모달 닫기 함수
const closeModal = () => {
  showModal.value = false
}

const updateBattery = async () => {
  try {
    const response = await api.put('/myinfo/car/' + id, {
      carBattery: carBattery.value
    })
    if(response.status == 200){
      alert("배터리 정보가 성공적으로 변경되었습니다.");
      window.location.reload(true);
    }
    
  } catch (error) {
    console.error('Error fetching my info:', error);
  }
};


</script>


<template>
  <div>
    <!-- 다른 컴포넌트들 -->
    <VCard class="text-center text-sm-start">
      <VRow no-gutters>
        <VCol cols="12" sm="8" order="2" order-sm="1">
          <VCardItem>
            <VCardTitle class="text-md-h5 text-primary">
              환영합니다 {{ userCarInfo.username }}님! 🎉
            </VCardTitle>
          </VCardItem>

          <VCardText>
            <span>
              차량 : {{ userCarInfo.modelName }}
              <br />
              현재 배터리 : {{ userCarInfo.carBattery }}%
            </span>
            <br /><br />
            <span>내 주변 충전소 검색하기</span>
            <br />
            <VBtn
              variant="tonal"
              class="mt-4"
              size="small"
              @click="showModal = true"
            >
              배터리 업데이트
            </VBtn>
          </VCardText>
        </VCol>

        <VCol cols="12" sm="4" order="1" order-sm="2" class="text-center">
          <img
            :src="userCarInfo.modelFilepath"
            :height="$vuetify.display.xs ? '150' : '175'"
            :class="$vuetify.display.xs ? 'mt-6 mb-n2' : 'position-absolute'"
            class="john-illustration flip-in-rtl"
          />
        </VCol>
      </VRow>
      
    </VCard>
    <div v-if="showModal" class="battery-modal">
      <div class="modal-content">
        <h2>배터리를 입력해주세요!</h2>
        <p class="description">현재 배터리 : {{ userCarInfo.carBattery }}%</p>
        <input
          type="text"
          v-model="carBattery"
          placeholder="배터리 입력"
        />
        <div class="buttons">
        <button class="button1" @click="updateBattery">변경</button>
        <button class="button2" @click="closeModal">닫기</button>
      </div>
      </div>
    </div>
  </div>

</template>


<style lang="scss" scoped>
.battery-modal {
  position: fixed; /* 화면에 고정 */
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5); /* 어두운 배경 */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000; /* 다른 요소 위에 표시 */
  overflow: hidden;
}

.modal-content {
  width: 400px;
  background-color: white;
  padding: 30px 24px 28px 24px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  position: relative;
  border: 1px solid #C9D6DE;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  z-index: 1100;
}

h2 {
  font-size: 28px;
  margin-top: 10px;
  margin-bottom: 48px;
  color: #52616a;
}
.buttons {
  width: 100%;
    display: flex;
    justify-content: space-between;
    margin-top: 20px;
}

input {
  width: 100%;
  height: 60px;
  padding: 10px;
  border: 1px solid #c9d6de;
  border-radius: 8px;
  font-size: 16px;
}

input:focus {
  border: 2px solid #52616a;
  border-radius: 4px;
}

.button1{
  width: 49%;
    padding: 15px;
    font-size: 16px;
    font-weight: bold;
    border: none;
    border-radius: 8px;
    cursor: pointer;
  background-color: #C9D6DE;
  color: #52616a;
}

.button2{
  width: 49%;
    padding: 15px;
    font-size: 16px;
    font-weight: bold;
    border: none;
    border-radius: 8px;
    cursor: pointer;
  background-color: #F0F5F9;
  color: #52616a;
}

button:hover {
    background-color: #52616A;
    color:#fff;
  }
.description {
    font-size: 16px;
    color: #666;
    line-height: 1.5;
    margin-bottom: 24px;
  }

.john-illustration {
  inset-block-end: -0.0625rem;
  inset-inline-end: 3rem;
}
</style>

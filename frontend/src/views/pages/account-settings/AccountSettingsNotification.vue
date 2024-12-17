<script setup>
import api from '@/axios';
import { onMounted, ref, computed  } from 'vue';

const route = useRoute()
const id = route.params.id;

const info = ref({});
const accountDataLocal = ref({
  modelId: '',
  carNumber: '',
  carBattery: '',
  chargerSpeedId: ''
});

const cars = ref({
        model: [
          { value: 1, text: 'EV6' },
          { value: 2, text: 'EV9' },
          { value: 3, text: 'IONIQ6' },
          { value: 4, text: 'IONIQ5' },
          { value: 5, text: 'Tesla Model S' },
          { value: 6, text: 'Tesla Model X' },
          { value: 7, text: 'Taycan' },
          { value: 8, text: 'Mercedes-Benz EQC' },
        ],
        chargers: [
          { value: 1, text: '7kw' },
          { value: 2, text: '50kw' },
          { value: 3, text: '100kw' },
          { value: 4, text: '200kw' },
          { value: 5, text: '300kw 이상' },
        ],
})




// 회원 차 정보 가져오기
const fetchMyCarInfo = async () => {
  try {
    const response = await api.get('/myinfo/car/' + id);
    info.value = response.data;
    accountDataLocal.value = { ...info.value };
  } catch (error) {
    console.error("Error fetching my info:", error);
  }
};

// Axios로 가져온 데이터를 표시하는 계산된 속성
const getChargerSpeedText = computed(() => {
  const selectedCharger = cars.value.chargers.find(
    (charger) => charger.value === accountDataLocal.value.chargerSpeedId
  );
  return selectedCharger ? selectedCharger.text : '선호 전력 타입 선택';
});

// const modelName = ref('')
// const carNumber = ref('')
// const carBattery = ref()
// const chagerType = ref()

onMounted(() => {
  fetchMyCarInfo();
});
</script>

<template>
  <VCard title="내 차 정보">
    <VCardText>
      <VDivider />
      <br>
    <VForm @submit.prevent="onSubmit">
    <VRow>
      <VCol cols="12">
        
        <VRow no-gutters>
          <!-- 👉 First Name -->
          <VCol
            cols="12"
            md="3"
          >
            <label for="car_model">차 종</label>
          </VCol>

          <VCol
            cols="12"
            md="9"
          >
            <VTextField
              id="car_model"
              v-model="accountDataLocal.modelId"
              type="text"
              :placeholder="accountDataLocal.modelId"
              persistent-placeholder
              readonly
            />
          </VCol>
        </VRow>
      </VCol>

      <VCol cols="12">
        <VRow no-gutters>
          <!-- 👉 Email -->
          <VCol
            cols="12"
            md="3"
          >
            <label for="car_number">차 번호</label>
          </VCol>

          <VCol
            cols="12"
            md="9"
          >
            <VTextField
              id="car_number"
              v-model="accountDataLocal.carNumber"
              type="text"
              :placeholder="accountDataLocal.carNumber"
              persistent-placeholder
              readonly>
            <template #append-inner>
                    <VBtn
                      class="v-btn--elevated bg-primary"
                      @click="onChangeCarNumberModal"
                      style="margin-right: -8px;"
                    >
                      차 번호 수정
                    </VBtn>
                  </template>
                </VTextField>
          </VCol>
        </VRow>
      </VCol>

      <VCol cols="12">
        <VRow no-gutters>
          <!-- 👉 Mobile -->
          <VCol
            cols="12"
            md="3"
          >
            <label for="car_baterry">현재 배터리</label>
          </VCol>

          <VCol
            cols="12"
            md="9"
          >
            <VTextField
              id="car_baterry"
              v-model="accountDataLocal.carBattery"
              type="text"
              :placeholder="accountDataLocal.carBattery"
              persistent-placeholder>
                  <template #append-inner>
                    <VBtn
                      class="v-btn--elevated bg-primary"
                      @click="onChangeBatteryModal"
                      style="margin-right: -8px;"
                    >
                      배터리 정보 수정
                    </VBtn>
                  </template>
                </VTextField>
          </VCol>
        </VRow>
      </VCol>

      <VCol cols="12">
        <VRow no-gutters>
          <!-- 👉 Password -->
          <VCol
            cols="12"
            md="3"
          >
            <label for="">선호 전력 타입</label>
          </VCol>

          <VCol
            cols="12"
            md="9"
          >
          <!-- <option v-for="car in cars" :value="car.value" :key="car.value">{{ car.text }}</option> -->
          <VSelect
              id="chager_type"
              v-model="accountDataLocal.chargerSpeedId"
              :items="cars.chargers"
              item-text="text"
              item-value="value"
              :placeholder="getChargerSpeedText"
              outlined
              persistent-placeholder
            />
            <!-- <VTextField
              id="chager_type"
              v-model="chagerType"
              type="text"
              placeholder="············"
              persistent-placeholder
            >
            <template #append-inner>
                    <VBtn
                      class="v-btn--elevated bg-primary"
                      @click="onChangeBatteryModal"
                      style="margin-right: -8px;"
                    >
                      선호 전력 타입 선택
                    </VBtn>
                  </template>
                  </VTextField> -->
          </VCol>
        </VRow>
      </VCol>


      <!-- 👉 submit and reset button -->
      <VCol
        offset-md="3"
        cols="12"
        md="9"
        class="d-flex gap-4"
      >
        <VBtn
          type="submit">
          정보 수정
        </VBtn>
        <VBtn
          color="secondary"
          variant="tonal"
          type="reset"
        >
          초기화
        </VBtn>
      </VCol>
    </VRow>
  </VForm>
</VCardText>
</VCard>
</template>

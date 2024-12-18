<script setup>
import api from '@/axios';
import { onMounted, ref, computed } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const id = route.params.id;

const info = ref({});
const accountDataLocal = ref({
  modelId: '',
  carNumber: '',
  carBattery: '',
  chargerSpeedId: null,
});

const cars = {
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
};

// 데이터 가져오기
const fetchMyCarInfo = async () => {
  try {
    const response = await api.get('/myinfo/car/' + id);
    info.value = response.data;
    accountDataLocal.value = { ...info.value };
  } catch (error) {
    console.error('Error fetching my info:', error);
  }
};

// modelId에 해당하는 차종 이름을 가져오는 computed 속성
const getCarModelName = computed(() => {
  const selectedModel = cars.model.find(
    (model) => model.value === accountDataLocal.value.modelId
  );
  return selectedModel ? selectedModel.text : '차종 선택';
});

// 차 번호 수정 상태 관리
const isCarNumberEditable = ref(false);

// 차 번호 수정 버튼 클릭 시 호출되는 함수
const onChangeCarNumberModal = () => {
  isCarNumberEditable.value = !isCarNumberEditable.value;
};

const getChargerSpeedText = computed(() => {
  const selectedCharger = cars.chargers.find(
    (charger) => charger.value === accountDataLocal.value.chargerSpeedId
  );
  return selectedCharger ? selectedCharger.text : '선호 전력 타입 선택';
});

const onSubmit = () => {
  console.log('수정된 정보:', accountDataLocal.value);
};

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
            <!-- 차종 선택 -->
            <VRow no-gutters>
              <VCol cols="12" md="3">
                <label for="car_model">차 종</label>
              </VCol>

              <VCol cols="12" md="9">
                <div class="v-input v-text-field v-text-field--is-focused">
                  <select v-model="accountDataLocal.modelId" id="car_model" class="v-input__control" required>
                    <option value="">선택안함</option>
                    <option v-for="model in cars.model" :value="model.value" :key="model.value">
                      {{ model.text }}
                    </option>
                  </select>
                </div>
              </VCol>
            </VRow>
          </VCol>

          <VCol cols="12">
            <!-- 선택된 차종 표시 -->
            <VRow no-gutters>
              <VCol cols="12" md="3">
                <label for="selected_car_model">선택된 차종</label>
              </VCol>

              <VCol cols="12" md="9">
                <VTextField 
                  id="selected_car_model"
                  :value="getCarModelText" 
                  persistent-placeholder
                  readonly
                />
              </VCol>
            </VRow>
          </VCol>

          <VCol cols="12">
            <VRow no-gutters>
              <!-- 👉 Email -->
              <VCol cols="12" md="3">
                <label for="car_number">차 번호</label>
              </VCol>

              <VCol cols="12" md="9">
                <VTextField id="car_number" v-model="accountDataLocal.carNumber" type="text"
                  :placeholder="accountDataLocal.carNumber" persistent-placeholder readonly>
                  <template #append-inner>
                    <VBtn
                      class="v-btn--elevated bg-primary"
                      @click="onChangeCarNumberModal"
                      style="margin-right: -8px;"
                    >
                      {{ isCarNumberEditable ? '차 번호 수정 완료' : '차 번호 수정' }}
                    </VBtn>
                  </template>
                </VTextField>
              </VCol>
            </VRow>
          </VCol>

          <VCol cols="12">
            <VRow no-gutters>
              <!-- 👉 Mobile -->
              <VCol cols="12" md="3">
                <label for="car_baterry">현재 배터리</label>
              </VCol>

              <VCol cols="12" md="9">
                <VTextField id="car_baterry" v-model="accountDataLocal.carBattery" type="text"
                  :placeholder="accountDataLocal.carBattery" persistent-placeholder>
                  <template #append-inner>
                    <VBtn class="v-btn--elevated bg-primary" @click="onChangeBatteryModal" style="margin-right: -8px;">
                      배터리 정보 수정
                    </VBtn>
                  </template>
                </VTextField>
              </VCol>
            </VRow>
          </VCol>

          <VRow>
          <VCol cols="12">
            <!-- 선호 전력 타입 -->
            <VRow no-gutters>
              <VCol cols="12" md="3">
                <label for="charger_type">선호 전력 타입</label>
              </VCol>

              <VCol cols="12" md="9">
                <div class="v-input v-text-field v-text-field--is-focused">
                  <select v-model="accountDataLocal.chargerSpeedId" id="charger_type" class="v-input__control" required>
                    <option value="">선택안함</option>
                    <option v-for="charger in cars.chargers" :value="charger.value" :key="charger.value">
                      {{ charger.text }}
                    </option>
                  </select>
                </div>
              </VCol>
            </VRow>
          </VCol>

          <VCol cols="12">
            <!-- 선택된 전력 타입 표시 -->
            <VRow no-gutters>
              <VCol cols="12" md="3">
                <label for="selected_charger_speed">선택된 전력 타입</label>
              </VCol>

              <VCol cols="12" md="9">
                <VTextField 
                  id="selected_charger_speed"
                  :value="getChargerSpeedText" 
                  persistent-placeholder
                  readonly
                />
              </VCol>
            </VRow>
          </VCol>
          </VRow>

                <!-- <VSelect 
                    id="charger_type" 
                    v-model="accountDataLocal.chargerSpeedId" 
                    :items="cars.chargers" 
                    item-text="text" 
                    item-value="value" 
                    :placeholder="getChargerSpeedText" 
                    outlined 
                    persistent-placeholder 
                  /> -->

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



          <!-- 👉 submit and reset button -->
          <VCol offset-md="3" cols="12" md="9" class="d-flex gap-4">
            <VBtn type="submit">
              정보 수정
            </VBtn>
            <VBtn color="secondary" variant="tonal" type="reset">
              초기화
            </VBtn>
          </VCol>
        </VRow>
      </VForm>
    </VCardText>
  </VCard>
</template>

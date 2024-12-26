<script setup>
import api from '@/axios';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const id = route.params.id;

const info = ref({});
const accountDataLocal = ref({
  modelId: '',
  carNumber: '',
  carBattery: '',
  chargerSpeedId: null,
  modelBattery: '',
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

const resetForm = () => {
  accountDataLocal.value = { ...info.value };
};

// 모달 관리
const showModalChangeCarNumber = ref(false)
const onChangeBatteryModal = ref(false)

// 모달 닫기 함수
const closeCarModal = () => {
  showModalChangeCarNumber.value = false
  accountDataLocal.value.carNumber = info.value.carNumber;
  accountDataLocal.value.modelId = info.value.modelId;
}
const closeBatteryModal = () => {
  onChangeBatteryModal.value = false;
  accountDataLocal.value.carBattery = info.value.carBattery;
}

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

const onSubmit = async () => {
  try {
    const response = await api.post('myinfo/car/' + id, {
      modelId: accountDataLocal.value.modelId,
      carNumber: accountDataLocal.value.carNumber,
      carBattery: accountDataLocal.value.carBattery,
      chargerSpeedId: accountDataLocal.value.chargerSpeedId,
    })
    if (response.status == 200) {
      alert('차량 정보가 성공적으로 변경되었습니다.')
      window.location.reload(true);
    }
  } catch (error) {
    if (error.response.status == 400) {
      alert("정보 수정 중 문제가 발생하였습니다.")
    } else {
      alert("정보 수정 중 문제가 발생하였습니다." + error)
    }
  }
  console.log('수정된 정보:', accountDataLocal.value);
};

const validateCarNumber = (carNumber) => {
  const trimmedCarNumber = carNumber.replace(/\s+/g, ''); // 공백 제거
  const carNumberRegex = /^(\d{2}|\d{3})[가-힣]\d{4}$/;
  return carNumberRegex.test(trimmedCarNumber);
};

const updateMyCar = () => {
  if (validateCarNumber(accountDataLocal.value.carNumber)) {
    alert('정보 수정 버튼을 클릭하여 변경을 확정하여 주세요!');
    showModalChangeCarNumber.value = false; // 모달 닫기
  } else {
    alert("유효하지 않은 차량번호입니다. 예: 123가4567 또는 12가4567");
    accountDataLocal.value.carNumber = '';
  }
};

const updateBattery = () => {
  const batteryValue = parseFloat(accountDataLocal.value.carBattery);
  // 배터리 용량 초과 또는 잘못된 입력값 체크
  if (isNaN(batteryValue) || batteryValue > accountDataLocal.value.modelBattery || batteryValue < 0) {
    alert('배터리 정보를 정확히 입력해주세요.');
    accountDataLocal.value.carBattery = '';
    return;
  } else {
    alert('정보 수정 버튼을 클릭하여 변경을 확정하여 주세요!');
    onChangeBatteryModal.value = false;
  }
}
onMounted(() => {
  fetchMyCarInfo();
});
</script>


<template>
  <div>
    <VCard title="내 차 정보">
      <VDivider />
      <VCardText>

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
                    <select v-model="accountDataLocal.modelId" id="car_model" class="model-category" disabled>
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
              <VRow no-gutters>
                <!-- 👉 Email -->
                <VCol cols="12" md="3">
                  <label for="car_number">차 번호</label>
                </VCol>

                <VCol cols="12" md="9">
                  <VTextField id="car_number" v-model="accountDataLocal.carNumber" type="text"
                    :placeholder="accountDataLocal.carNumber" persistent-placeholder readonly>
                    <template #append-inner>
                      <VBtn class="v-btn--elevated bg-primary" @click="showModalChangeCarNumber = true"
                        style="margin-right: -8px;"> 차번호 수정
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
                      <VBtn class="v-btn--elevated bg-primary" @click="onChangeBatteryModal = true"
                        style="margin-right: -8px;">
                        배터리 정보 수정
                      </VBtn>
                    </template>
                  </VTextField>
                </VCol>
              </VRow>
            </VCol>

            <VCol cols="12">
              <!-- 선호 전력 타입 -->
              <VRow no-gutters>
                <VCol cols="12" md="3">
                  <label for="charger_type">선호 전력 타입</label>
                </VCol>

                <VCol cols="12" md="9">
                  <div class="v-input v-text-field v-text-field--is-focused">
                    <select v-model="accountDataLocal.chargerSpeedId" id="charger_type" class="model-category" required>
                      <option value="">선택안함</option>
                      <option v-for="charger in cars.chargers" :value="charger.value" :key="charger.value">
                        {{ charger.text }}
                      </option>
                    </select>
                  </div>
                </VCol>
              </VRow>
            </VCol>
            <!-- 👉 submit and reset button -->
            <VCol offset-md="3" cols="12" md="9" class="d-flex gap-4">
              <VBtn type="submit">
                정보 수정
              </VBtn>
              <VBtn color="secondary" variant="tonal" type="reset" @click.prevent="resetForm">
                초기화
              </VBtn>
            </VCol>
          </VRow>
        </VForm>
      </VCardText>
    </VCard>

    <!-- modal showModalChangeCarNumber -->
    <div v-if="showModalChangeCarNumber" class="battery-modal">
      <div class="modal-content">
        <h2>변경하실 차량 정보를 입력해주세요!</h2>
        <VCol cols="12" md="12">
          <label for="car_modal_model">변경하실 차 종류 선택 : </label>
        </VCol>
        <div id="selected_car_modal_model" class="selected_container">
          <select v-model="accountDataLocal.modelId" id="car_modal_model" class="modal-model-category" required>
            <option value="">선택안함</option>
            <option v-for="model in cars.model" :value="model.value" :key="model.value">
              {{ model.text }}
            </option>
          </select>
        </div>
        <VCol cols="12" md="12">
          <label for="car-number">현재 차 번호 : {{ info.carNumber }}</label>
        </VCol>
        <input type="text" v-model="accountDataLocal.carNumber" id="car-number" placeholder="변경할 차 번호 입력" />
        <div class="buttons">
          <button class="button1" @click="updateMyCar">확인</button>
          <button class="button2" @click="closeCarModal">닫기</button>
        </div>
      </div>
    </div>

    <!-- modal onChangeBatteryModal -->
    <div v-if="onChangeBatteryModal" class="battery-modal">
      <div class="modal-content">
        <h2>배터리를 입력해주세요!</h2>
        <p class="description">현재 배터리 : {{ info.carBattery }} Kw</p>
        <input type="text" v-model="accountDataLocal.carBattery" placeholder="배터리 입력" />
        <div class="buttons">
          <button class="button1" @click="updateBattery">확인</button>
          <button class="button2" @click="closeBatteryModal">닫기</button>
        </div>
      </div>
    </div>
  </div>



</template>
<style lang="scss" scoped>
.model-category {
  width: 100%;
  height: 56px;
  padding: 15px 6px 15px 16px;
  border: 1px solid #c9d6de;
  border-radius: 8px;
  font-size: 16px;
  color: #84919D;
}

.selected_container {
  width: 100%;
}

.modal-model-category {
  width: 100%;
  height: 60px;
  padding: 10px;
  border: 1px solid #c9d6de;
  border-radius: 8px;
  font-size: 16px;
}

.battery-modal {
  position: fixed;
  /* 화면에 고정 */
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  /* 어두운 배경 */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  /* 다른 요소 위에 표시 */
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

.button1 {
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

.button2 {
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
  color: #fff;
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
<script setup>
const carModel = ref('')
const carNumber = ref('')
const carBattery = ref()
const chagerType = ref()
</script>

<template>
  <VCard title="내 차 정보">
    <VCardText>
      <VDivider />
      <br>
  <VForm @submit.prevent="() => {}">
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
              v-model="carModel"
              type="text"
              placeholder="John"
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
              v-model="carNumber"
              type="text"
              placeholder="johndoe@email.com"
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
              v-model="carBattery"
              type="text"
              placeholder="+1 123 456 7890"
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
          <VSelect
              id="chager_type"
              v-model="chagerType"
              :items="powerTypes"
              placeholder="선택하세요"
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
        <VBtn type="submit">
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

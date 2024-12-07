<script setup>
import avatar1 from '@images/avatars/avatar-1.png'

const accountData = {
  avatarImg: avatar1,
  firstName: 'john',
  id: 'milionvolt',
  email: 'milionvolt@example.com',
  phone: '010-1234-5678',
}

const refInputEl = ref()
const accountDataLocal = ref(structuredClone(accountData))
const isAccountDeactivated = ref(false)

const resetForm = () => {
  accountDataLocal.value = structuredClone(accountData)
}

// const changeAvatar = file => {
//   const fileReader = new FileReader()
//   const { files } = file.target
//   if (files && files.length) {
//     fileReader.readAsDataURL(files[0])
//     fileReader.onload = () => {
//       if (typeof fileReader.result === 'string')
//         accountDataLocal.value.avatarImg = fileReader.result
//     }
//   }
// }

// reset avatar image
// const resetAvatar = () => {
//   accountDataLocal.value.avatarImg = accountData.avatarImg
// }
</script>

<template>
  <VRow>
    <VCol cols="12">
      <VCard title="내 정보">
        <VDivider />

        <VCardText>
          <!-- 👉 Form -->
          <VForm class="mt-6">
            <VRow>
              <!-- 👉 First Name -->
              <VCol
                md="6"
                cols="12"
              >
                <VTextField
                  v-model="accountDataLocal.firstName"
                  placeholder="John"
                  label="성명"
                  readonly
                />
              </VCol>

              <!-- 👉 id -->
              <VCol
                cols="12"
                md="6"
              >
                <VTextField
                  v-model="accountDataLocal.id"
                  label="ID"
                  placeholder="milionvolt"
                  readonly
                />
              </VCol>

              <!-- 👉 Email -->
              <VCol
                cols="12"
                md="6"
              >
                <!-- <VTextField 
                  v-model="accountDataLocal.email"
                  label="E-mail"
                  placeholder="milionvolt@gmail.com"
                  type="email">
                  <div style="text-align: right; position: relative;">
                    <VBtn style="float: right;">이메일 변경</VBtn>
                  </div>
                  </VTextField> -->
                  <VTextField
                  v-model="accountDataLocal.email"
                  label="E-mail"
                  placeholder="milionvolt@gmail.com"
                  type="email"
                  outlined
                >
                  <!-- append-inner 로 버튼을 VTextField안에 삽입 -->
                  <template #append-inner>
                    <VBtn
                      class="v-btn--elevated bg-primary"
                      @click="onChangeEmailModal"
                      style="margin-right: -8px;"
                    >
                      이메일 변경
                    </VBtn>
                  </template>
                </VTextField>
              </VCol>

              <!-- 👉 Phone -->
              <VCol
                cols="12"
                md="6"
              >
                <VTextField
                  v-model="accountDataLocal.phone"
                  label="Phone Number"
                  placeholder="+1 (917) 543-9876"
                />
              </VCol>

              <!-- 👉 Form Actions -->
              <VCol
                cols="12"
                class="d-flex flex-wrap gap-4"
              >
                <VBtn>정보 변경</VBtn>

                <VBtn
                  color="secondary"
                  variant="tonal"
                  type="reset"
                  @click.prevent="resetForm"
                >
                  다시쓰기
                </VBtn>
              </VCol>
            </VRow>
          </VForm>
        </VCardText>
      </VCard>
    </VCol>

  </VRow>
</template>

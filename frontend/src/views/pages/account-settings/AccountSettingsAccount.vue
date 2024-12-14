<script setup>
import avatar1 from '@images/avatars/avatar-1.png'
import api from '@/axios';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router'

const route = useRoute()
const id = route.params.id;

console.log("route.params.id : " +id);

// 초기 값 설정
const info = ref({});
const accountDataLocal = ref({
  username: '',
  userId: '',
  email: '',
  phoneNumber: ''
});

// 회원 정보 가져오기
const fetchMyInfo = async () => {
  try {
    const response = await api.get('/myinfo/' + id);
    info.value = response.data;
    accountDataLocal.value = { ...info.value };
  } catch (error) {
    console.error("Error fetching my info:", error);
  }
};

// 회원 정보 리셋
const resetForm = () => {
  accountDataLocal.value = { ...info.value };
};

// 회원 정보 수정
const updateInfo = async () => {
  try {
    const response = await api.post('/myinfo/' + id, {
      username: accountDataLocal.value.username,
      email: accountDataLocal.value.email,
      phoneNumber: accountDataLocal.value.phoneNumber
    });

    if (response.status === 200) {
      alert('회원정보가 성공적으로 변경되었습니다.');
      console.log(accountDataLocal.value); // 변경된 데이터 확인
    }
  } catch (error) {
    console.error("Error updating info:", error);
    alert("정보 변경에 실패하였습니다.");
  }
};

onMounted(() => {
  fetchMyInfo();
});

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
                  v-model="accountDataLocal.username"
                  :placeholder="info.username"
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
                  v-model="accountDataLocal.userId"
                  label="ID"
                  :placeholder="info.userId"
                  readonly
                />
              </VCol>

              <!-- 👉 Email -->
              <VCol
                cols="12"
                md="6"
              >
                  <VTextField
                  v-model="accountDataLocal.email"
                  label="E-mail"
                  :placeholder="info.email"
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
                  v-model="accountDataLocal.phoneNumber"
                  label="Phone Number"
                  :placeholder="info.phoneNumber"
                />
              </VCol>

              <!-- 👉 Form Actions -->
              <VCol
                cols="12"
                class="d-flex flex-wrap gap-4"
              >
                <VBtn
                @click.prevent="updateInfo">정보 변경</VBtn>

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

<script setup>
import api from '@/axios';
import { ref } from 'vue';
import { useRoute } from 'vue-router'

const route = useRoute()
const id = route.params.id;

const isCurrentPasswordVisible = ref(false)
const isNewPasswordVisible = ref(false)
const isConfirmPasswordVisible = ref(false)
const currentPassword = ref('')
const newPassword = ref('')
const confirmPassword = ref('')

const passwordRequirements = [
  '이름, 생년월일, 전화번호, 아이디 등 쉽게 타인이 유추할 수 있는 정보로 비밀번호를 사용하지 마십시오.',
  '연속되는 단순 문자열 또는 단어로 구성된 비밀번호의 사용은 위험합니다.',
  '영문과 숫자, 특수문자를 조합하여 8 ~ 12자리 이내의 비밀번호를 사용하시기 바랍니다.',
  '비밀번호는 3개월에 한번씩 또는 그보다 자주 변경하는 것이 좋습니다.',
  '비밀번호 등 개인정보를 타인에게 절대 알려주지 마십시오.',
  '타 사이트와 동일한 아이디, 비밀번호를 사용하는 경우 해킹의 표적이 될 수 있습니다.',
  '가능한 다른 아이디, 비밀번호로 연속 피해를 방지하십시오.',
  '현재 사용중인 비밀번호와 상이하게 다른 비밀번호로 변경해 주시기 바랍니다.',
]


// 비밀번호 업데이트
const updatePassword = async () => {
  if (currentPassword.value == null || currentPassword.value.trim() == '') {
    alert("기존 비밀번호를 입력해주세요")
    return;
  }

  if (newPassword.value == null || newPassword.value.trim() == '') {
    alert("변경하실 비밀번호를 입력해주세요")
    return;
  }

  if (confirmPassword.value == null || confirmPassword.value.trim() == '') {
    alert("비밀번호 확인란에 비밀번호를 입력해주세요")
    return;
  }
  if (newPassword.value !== confirmPassword.value) {
    alert('새 비밀번호와 비밀번호 확인이 일치하지 않습니다.');
    return;
  }

  try {
    const response = await api.post('/myinfo/password/' + id, {
      password: currentPassword.value,
      newPassword: newPassword.value,
    });
    if (response.status == 200) {
      alert("비밀번호가 성공적으로 변경되었습니다.")
    } else {
      alert("비밀번호가 일치하지 않습니다.")
    }
  } catch (error) {
    // 에러 응답 처리
    if (error.response && error.response.status === 400) {
      alert("비밀번호가 일치하지 않습니다."); 
    } else {
      alert("비밀번호 변경 중 오류가 발생하였습니다. " + error.message);
    }
  }
}
</script>

<template>
  <VRow>
    <!-- SECTION: Change Password -->
    <VCol cols="12">
      <VCard title="비밀번호 변경">
        <VDivider />
        <VForm @submit.prevent="updatePassword">
          <VCardText>
            <!-- 👉 Current Password -->
            <VRow>
              <VCol cols="12" md="6">
                <!-- 👉 current password -->
                <VTextField v-model="currentPassword" :type="isCurrentPasswordVisible ? 'text' : 'password'"
                  :append-inner-icon="isCurrentPasswordVisible ? 'bx-hide' : 'bx-show'" label="기존 비밀번호"
                  placeholder="············" @click:append-inner="isCurrentPasswordVisible = !isCurrentPasswordVisible"
                  required />
              </VCol>
            </VRow>

            <!-- 👉 New Password -->
            <VRow>
              <VCol cols="12" md="6">
                <!-- 👉 new password -->
                <VTextField v-model="newPassword" :type="isNewPasswordVisible ? 'text' : 'password'"
                  :append-inner-icon="isNewPasswordVisible ? 'bx-hide' : 'bx-show'" label="변경할 비밀번호"
                  placeholder="············" @click:append-inner="isNewPasswordVisible = !isNewPasswordVisible"
                  required />
              </VCol>

              <VCol cols="12" md="6">
                <!-- 👉 confirm password -->
                <VTextField v-model="confirmPassword" :type="isConfirmPasswordVisible ? 'text' : 'password'"
                  :append-inner-icon="isConfirmPasswordVisible ? 'bx-hide' : 'bx-show'" label="비밀번호 확인"
                  placeholder="············" @click:append-inner="isConfirmPasswordVisible = !isConfirmPasswordVisible"
                  required />
              </VCol>
            </VRow>
          </VCardText>

          <!-- 👉 Password Requirements -->
          <VCardText>
            <p class="text-base font-weight-medium mt-2">
              비밀번호 변경 안내 사항:
            </p>

            <ul class="d-flex flex-column gap-y-3">
              <li v-for="item in passwordRequirements" :key="item" class="d-flex">
                <div>
                  <VIcon size="7" icon="bxs-circle" class="me-3" />
                </div>
                <span class="font-weight-medium">{{ item }}</span>
              </li>
            </ul>
          </VCardText>

          <!-- 👉 Action Buttons -->
          <VCardText class="d-flex flex-wrap gap-4">
            <VBtn type="submit">비밀번호 변경하기</VBtn>

            <VBtn type="reset" color="secondary" variant="tonal">
              다시 쓰기
            </VBtn>
          </VCardText>
        </VForm>
      </VCard>
    </VCol>
  </VRow>
</template>

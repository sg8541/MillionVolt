<script setup>
import { ref, reactive } from 'vue'

// 사용자 데이터
const accountData = {
  avatarImg: null, // 이미지 경로
  firstName: 'john',
  id: 'milionvolt',
  email: 'milionvolt@example.com',
  phone: '010-1234-5678',
}

// 로컬 데이터 복사
const accountDataLocal = ref(structuredClone(accountData))
const isModalVisible = ref(false) // 모달 표시 여부

// 모달 상태 데이터
const nickname = ref('')
const existingNicknames = reactive(['user1', 'test123', 'admin']) // 닉네임 중복 데이터
const statusMessage = ref('')
const statusClass = ref('')

// 닉네임 중복 확인
const checkNickname = () => {
  if (nickname.value.trim() === '') {
    statusMessage.value = '닉네임을 입력해 주세요.'
    statusClass.value = 'error'
  } else if (existingNicknames.includes(nickname.value)) {
    statusMessage.value = '사용할 수 없는 닉네임입니다.'
    statusClass.value = 'error'
  } else {
    statusMessage.value = '사용할 수 있는 닉네임입니다.'
    statusClass.value = 'success'
  }
}

// 닉네임 변경 확인
const confirmChange = () => {
  if (statusClass.value === 'success') {
    alert(`닉네임이 '${nickname.value}'(으)로 변경되었습니다!`)
    closeModal()
  } else {
    alert('닉네임 중복 확인이 필요합니다.')
  }
}

// 닉네임 변경 취소
const cancelChange = () => {
  nickname.value = ''
  statusMessage.value = ''
  statusClass.value = ''
}

// 모달 열기/닫기
const openModal = () => {
  isModalVisible.value = true
}
const closeModal = () => {
  isModalVisible.value = false
}

// 정보 초기화
const resetForm = () => {
  accountDataLocal.value = structuredClone(accountData)
}
</script>

<template>
  <VRow>
    <VCol cols="12">
      <VCard title="내 정보">
        <VDivider />

        <VCardText>
          <VForm class="mt-6">
            <VRow>
              <!-- 성명 -->
              <VCol md="6" cols="12">
                <VTextField
                  v-model="accountDataLocal.firstName"
                  placeholder="John"
                  label="성명"
                  readonly
                />
              </VCol>

              <!-- ID -->
              <VCol md="6" cols="12">
                <VTextField
                  v-model="accountDataLocal.id"
                  label="ID"
                  placeholder="milionvolt"
                  readonly
                />
              </VCol>

              <!-- 이메일 -->
              <VCol md="6" cols="12">
                <VTextField
                  v-model="accountDataLocal.email"
                  label="E-mail"
                  placeholder="milionvolt@gmail.com"
                  type="email"
                  outlined
                >
                  <template #append-inner>
                    <VBtn
                      class="blue-btn"
                      @click="openModal"
                      style="margin-right: -8px;"
                    >
                      이메일 변경
                    </VBtn>
                  </template>
                </VTextField>
              </VCol>

              <!-- 전화번호 -->
              <VCol md="6" cols="12">
                <VTextField
                  v-model="accountDataLocal.phone"
                  label="Phone Number"
                  placeholder="+1 (917) 543-9876"
                />
              </VCol>

              <!-- 버튼 -->
              <VCol cols="12" class="d-flex flex-wrap gap-4">
                <VBtn class="blue-btn">정보 변경</VBtn>
                <VBtn
                  class="gray-btn"
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

    <!-- 닉네임 중복 검사 모달 -->
    <div v-if="isModalVisible" class="modal">
      <!-- 닫기 버튼 -->
      <span class="close-btn" @click="closeModal">&times;</span>
      <h2>닉네임 중복 검사</h2>
      <label for="nickname">변경할 닉네임</label>
      <input
        type="text"
        id="nickname"
        v-model="nickname"
        placeholder="닉네임을 입력하세요"
      />
      <button class="blue-btn" @click="checkNickname"><b>중복 확인</b></button>
      <p v-if="statusMessage" :class="statusClass">{{ statusMessage }}</p>
      <div class="actions">
        <button class="blue-btn" @click="confirmChange">변경</button>
        <button class="gray-btn" @click="cancelChange">취소</button>
      </div>
    </div>
  </VRow>
</template>

<style scoped>
/* 공통 버튼 스타일 */
button {
  border: none;
  border-radius: 4px;
  padding: 8px 12px;
  cursor: pointer;
  font-size: 14px;
  text-align: center;
}

/* 파란색 버튼 */
.blue-btn {
  background-color: blue;
  color: white;
}
.blue-btn:hover {
  background-color: #003366;
}

/* 회색 버튼 */
/* 회색 버튼 */
.gray-btn {
  background-color: gray;
  color: white; /* 글자 컬러를 화이트로 설정 */
}
.gray-btn:hover {
  background-color: #4f4f4f;
}

/* 모달 스타일 */
.modal {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 255, 0.2);
  padding: 20px;
  width: 350px;
  text-align: center;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 1000;
}
h2 {
  font-size: 18px;
  margin-bottom: 15px;
  text-align: center;
}
input {
  width: 100%;
  padding: 8px;
  margin: 10px 0;
  box-sizing: border-box;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.success {
  color: green;
  margin: 10px 0;
}
.error {
  color: red;
  margin: 10px 0;
}
.actions {
  display: flex;
  justify-content: space-around;
  margin-top: 15px;
}
label {
  display: block;
  text-align: left;
  font-size: 14px;
  margin-bottom: 5px;
}
.close-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  cursor: pointer;
  font-size: 18px;
  font-weight: bold;
  color: #555;
}
.close-btn:hover {
  color: red;
}
</style>

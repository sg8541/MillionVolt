<script setup>
import api from '@/axios';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router'

const route = useRoute()
const id = route.params.id;

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

const accountData = ref({
  username: '',
  userId: '',
  email: '',
  phoneNumber: ''
});

// 회원 정보 리셋
const resetForm = () => {
  accountDataLocal.value = { ...info.value };
};

// 로컬 데이터 복사
const isModalVisible = ref(false) // 모달 표시 여부

// 모달 상태 데이터
const modalEmail = ref('')
const statusMessage = ref('')
const statusClass = ref('')

const checkEmail = async () => {
  // 이메일 형식 검사 (정규 표현식 사용)
  const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
  if (!emailRegex.test(modalEmail.value.trim())) {
    statusMessage.value = '이메일 형식이 올바르지 않습니다.';
    statusClass.value = 'error';
    return; // 이메일 형식이 잘못되었으면 종료
  }

  if (modalEmail.value.trim() === '') {
    statusMessage.value = '이메일을 입력해 주세요.';
    statusClass.value = 'error';
  } else {
    try {
      const response = await api.get(`/email-check?email=${modalEmail.value}`);
      if (response.status === 200) {
        // 이메일이 사용 가능한 경우
        statusClass.value = 'success';
        statusMessage.value = '사용할 수 있는 이메일입니다.';
      } else {
        // 이메일이 이미 존재하는 경우
        statusClass.value = 'error';
        statusMessage.value = '사용할 수 없는 이메일입니다.';
      }
    } catch (error) {
      // 오류 처리
      alert('이메일 중복 확인 중 오류가 발생하였습니다.' + error);
    }
  }
};
// 이메일 변경 확인
const confirmChange = () => {
  if (modalEmail.value.trim() === '') {
    statusMessage.value = '이메일을 입력해 주세요.'
    statusClass.value = 'error'
  }
  if (statusClass.value === 'success') {
    alert(`변경하실 이메일은 '${modalEmail.value}' 입니다!`)
    accountDataLocal.value.email = modalEmail.value;
    closeModal()
  } else {
    alert('이메일 중복 확인이 필요합니다.')
  }
}

// 닉네임 변경 취소
const cancelChange = () => {
  modalEmail.value = ''
  statusMessage.value = ''
  statusClass.value = ''
  closeModal()
}

// 모달 열기/닫기
const openModal = () => {
  isModalVisible.value = true
}
const closeModal = () => {
  isModalVisible.value = false
}

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
          <VTextField v-model="accountDataLocal.username" :placeholder="accountDataLocal.username" label="성명"
           readonly />
         </VCol>
  
         <!-- ID -->
         <VCol md="6" cols="12">
          <VTextField v-model="accountDataLocal.userId" label="ID" :placeholder="accountDataLocal.userId"
           readonly />
         </VCol>
  
         <!-- 👉 Email -->
         <VCol cols="12" md="6">
          <VTextField v-model="accountDataLocal.email" label="E-mail" :placeholder="accountDataLocal.email"
           type="email" outlined readonly>
           <template #append-inner>
            <VBtn class="blue-btn" @click="openModal" style="margin-right: -8px;">
                      이메일 중복확인
            </VBtn>
           </template>
          </VTextField>
         </VCol>
  
         <!-- 전화번호 -->
         <VCol md="6" cols="12">
          <VTextField v-model="accountDataLocal.phoneNumber" label="Phone Number"
           :placeholder="accountDataLocal.phoneNumber" />
         </VCol>
  
  
         <!-- 👉 Form Actions -->
         <VCol cols="12" class="d-flex flex-wrap gap-4">
          <VBtn @click.prevent="updateInfo">정보 변경</VBtn>
  
          <VBtn class="gray-btn" color="secondary" variant="tonal" type="reset" @click.prevent="resetForm">
           다시쓰기
          </VBtn>
         </VCol>
        </VRow>
       </VForm>
      </VCardText>
     </VCard>
    </VCol>
  
    <!-- 이메일 중복 검사 모달 -->
    <div v-if="isModalVisible" class="modal-overlay">
     <div class="modal">
      <!-- 닫기 버튼 -->
      <span class="close-btn" @click="closeModal">&times;</span>
      <h2>이메일 중복 검사</h2>
      <label for="email">변경할 이메일</label>
      <input type="email" id="modalEmail" v-model="modalEmail" placeholder="이메일을 입력하세요" />
      <button class="yellow-btn" @click="checkEmail"><b>중복 확인</b></button>
      <p v-if="statusMessage" :class="statusClass">{{ statusMessage }}</p>
      <div class="actions">
       <button class="yellow-btn" @click="confirmChange">변경</button>
       <button class="yellow-btn" @click="cancelChange">취소</button>
      </div>
     </div>
    </div>
   </VRow>
  </template>
  
  <style scoped>
  /* 모달 외부 오버레이 */
  .modal-overlay {
   position: fixed;
   top: 0;
   left: 0;
   right: 0;
   bottom: 0;
   background-color: rgba(0, 0, 0, 0.5);
   /* 배경 어둡게 */
   z-index: 999;
   /* 모달보다 낮은 z-index */
   display: flex;
   justify-content: center;
   align-items: center;
  }
  
  /* 모달 스타일 */
  .modal {
   background: #fff;
   border-radius: 8px;
   box-shadow: 0 0 10px rgba(0, 0, 255, 0.2);
   padding: 20px;
   width: 350px;
   text-align: center;
   position: relative;
   z-index: 1000;
   /* 모달의 z-index */
  }
  
  
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
   color: white;
   /* 글자 컬러를 화이트로 설정 */
  }
  
  .gray-btn:hover {
   background-color: #4f4f4f;
  }
  
  .yellow-btn {
   background-color: #FEE500;
   color:rgba(0, 0, 0, 1);
  }
  
  .yellow-btn:hover {
   background-color: rgba(0, 0, 0, 0.5);
  }
  /* 모달 스타일 */
  .modal {
   background: #fff;
   border-radius: 8px;
   box-shadow: 0 0 10px rgba(254, 229, 0, 0.8);
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
  
  
  
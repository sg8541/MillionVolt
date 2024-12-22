<template>
  <div class="login-page">
    <router-link to="/main" class="logo-link">
      <img src="images/logo.png" alt="백 만 볼트 로고" class="logo">
    </router-link>
    <div class="login-container">
      <h2>백만볼트 로그인</h2>
      <form @submit.prevent="handleSubmit">
        <div class="input-group">
          <input type="text" v-model="userId" 
          @input="handleUserIdInput"
          placeholder="아이디를 입력해주세요." required />
        </div>
        <div class="input-group">
          <input type="password" v-model="password" placeholder="비밀번호를 입력해주세요." required />
        </div>
        <button type="submit" class="login-button">로  그  인</button>
        <div class="signup-link">
          <router-link to="/find-id">아이디 찾기</router-link>
          <router-link to="/find-password">비밀번호 찾기</router-link>
          <router-link to="/agreement">회원가입</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { useAuthStore } from '../../../stores/auth';

export default {
  name: 'Login',
  data() {
    return {
      userId: '',
      password: ''
    };
  },
  setup() {
    const authStore = useAuthStore()
    return { authStore }
  },
  methods: {
    handleUserIdInput(inputId){
        this.userId = String(inputId.target.value);
    },
    async handleSubmit() {
      // 로그인 로직 처리
      console.log("아이디:", this.userId, "비밀번호:", this.password);
      await this.authStore.login(this.userId, this.password)
    },
  }
};
</script>

<style scoped>
/* 전체 페이지 스타일 */
.login-page {
  margin: 0;
  padding: 0;
  font-family: Arial, sans-serif;
  background-color: white;
  height: 100vh;
  display: flex;
  /* justify-content: center; */
  align-items: center;
  flex-direction: column;
}

form{
  width: 100%;
}

/* 로고 스타일 */
.logo {
    /* top: 5px;
    left: 5px; */
    width: 150px;
}

/* 로그인 컨테이너 스타일 */
.login-container {
  width: 500px;
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
}

/* 제목 스타일 */
h2 {
  font-size: 28px;
  margin-top: 10px;
  margin-bottom: 48px;
  color:#52616A;
}

/* 입력 그룹 스타일 */
.input-group {
  margin-bottom: 10px;
  display: flex;
  justify-content: center;
}

.input-group input {
  width: 100%;
  height: 60px;
  padding: 10px;
  border: 1px solid #c9d6de;
  border-radius: 4px;
  font-size: 16px;
}

.input-group input:focus {
  border: 2px solid #52616a;
  border-radius: 4px;
}

/* 로그인 버튼 스타일 */
.login-button {
  width: 100%;
  padding: 10px;
  height: 68px;
  background-color: #52616A;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 18px;
  cursor: pointer;
  transition: background-color 0.3s;
  margin-bottom: 22px;
  margin-top: 15px;
}

.login-button:hover {
  background-color: #1E2022;
}

/* 회원가입 링크 스타일 */
.signup-link {
  display: flex;
  font-size: 14px;
  justify-content: space-between;
}

.signup-link a {
  color: #a4a4a4;
  text-decoration: none;
  /* margin: 0 30px; */
}

.signup-link a:hover {
  /* text-decoration: underline; */
  color:#52616A;
}

.input-group input:focus {
  outline: none;
}
</style>
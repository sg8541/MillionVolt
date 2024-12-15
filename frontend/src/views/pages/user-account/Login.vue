<template>
    <div class="login-page">
      <a href="../templates/main.html">
        <img src="images/logo.png" alt="Logo" class="logo" />
      </a>
  
      <div class="login-container">
        <h2>백만볼트 로그인</h2>
        <form @submit.prevent="handleSubmit">
          <div class="input-group">
            <input 
              type="text" 
              v-model="userId" 
              placeholder="아이디를 입력해주세요." 
              required 
            />
          </div>
          <div class="input-group">
            <input 
              type="password" 
              v-model="password" 
              placeholder="비밀번호를 입력해주세요." 
              required 
            />
          </div>
          <button type="submit" class="login-button">로 그 인</button>
          <div class="signup-link">
            <router-link to="/find-id">아이디 찾기</router-link>
            <router-link to="/findPWD">비밀번호 찾기</router-link>
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
    justify-content: center;
    align-items: center;
    flex-direction: column;
  }
  
  /* 로고 스타일 */
  .logo {
    position: absolute;
    top: 5px;
    left: 5px;
    width: 150px;
  }
  
  /* 로그인 컨테이너 스타일 */
  .login-container {
    width: 500px;
    height: 500px;
    background-color: white;
    padding: 40px;
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    position: relative;
    border: 1px solid #c4c4c4;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }
  
  /* 제목 스타일 */
  h2 {
    font-size: 36px;
    margin-bottom: 50px;
  }
  
  /* 입력 그룹 스타일 */
  .input-group {
    margin-bottom: 10px;
    width: 100%;
    display: flex;
    justify-content: center;
  }
  
  .input-group input {
    width: 400px;
    height: 40px;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 16px;
  }
  
  /* 로그인 버튼 스타일 */
  .login-button {
    width: 400px;
    padding: 10px;
    height: 65px;
    background-color: #0b0b0b;
    color: white;
    border: none;
    border-radius: 4px;
    font-size: 26px;
    cursor: pointer;
    transition: background-color 0.3s;
    margin-bottom: 20px;
    margin-top: 10px;
  }
  
  .login-button:hover {
    background-color: #363636;
  }
  
  /* 회원가입 링크 스타일 */
  .signup-link {
    margin-top: 20px;
    font-size: 14px;
    text-align: center;
  }
  
  .signup-link a {
    color: #a4a4a4;
    text-decoration: none;
    margin: 0 30px;
  }
  
  .signup-link a:hover {
    text-decoration: underline;
  }
  
  .input-group input:focus {
    outline: none;
  }
  </style>
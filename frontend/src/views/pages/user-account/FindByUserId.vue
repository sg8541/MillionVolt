<template>
      <div class="logo">
      <router-link to="/main">
        <img src="images/logo.png" alt="백만볼트 로고">
      </router-link>
    </div>
  <div class="wrap">

    <div class="container">
      <div class="title">아이디 찾기 - 이메일 인증</div>
      <p class="description">본인확인 이메일과 입력한 이메일이 같아야,<br>인증번호를 받을 수 있습니다.</p>
      <form @submit.prevent="handleSubmit" class="form">
        <input v-model="name" type="text" class="input-field" placeholder="이름을 입력해주세요." required>
        <div class="email-row">
          <input v-model="email" type="email" class="input-field email-input" placeholder="이메일을 입력해주세요." required>
          <button type="button" class="verify-button" @click="sendVerificationCode">인증번호 받기</button>
        </div>
        <input v-model="verificationCode" type="text" class="input-field" placeholder="인증 번호 6자리 입력" required>
        <button type="submit" class="submit-button" :disabled="!isFormValid">아이디 찾기</button>
      </form>
      <div class="links">
        <router-link to="/findPWD">비밀번호 찾기</router-link>
        <router-link to="/joininform">회원가입</router-link>
      </div>
    </div>
  </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        name: '',
        email: '',
        verificationCode: '',
      };
    },
    computed: {
      isFormValid() {
        return this.name && this.email && this.verificationCode;
      },
    },
    methods: {
      sendVerificationCode() {
        // 이메일 인증번호 전송 로직
        alert(`인증번호가 ${this.email}로 전송되었습니다.`);
      },
      handleSubmit() {
        // 폼 제출 로직
        alert(`아이디 찾기 요청이 전송되었습니다.`);
      },
    },
  };
  </script>
  
  <style scoped>
  * {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
  }
  
  .wrap {
    font-family: 'Arial', sans-serif;
    /* background-color: white; */
    display: flex;
    justify-content: center;
    align-items: center;
    /* height: 100vh; */
    margin-bottom: 100px;
  }
  
  .container {
    width: 600px;
    height: 600px;
    background-color: #fff;
    padding: 20px;
    border: 1px solid #ddd;
    border-radius: 8px;
    text-align: center;
  }

  .logo {
    text-align: center;

  }
  
  .logo img {
    width: 150px;
  }
  
  .title {
    font-size: 28px;
    font-weight: bold;
    margin-top: 10px;
    margin-bottom: 30px;
  }
  
  .description {
    font-size: 16px;
    color: #666;
    line-height: 1.5;
    margin-bottom: 40px;
  }
  
  .form {
    display: flex;
    flex-direction: column;
    gap: 10px;
  }
  
  .input-field {
    width: 100%;
    height: 60px;
    padding: 12px;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 14px;
    margin-bottom: 10px;
    outline: none;
  }
  
  .email-row {
    display: flex;
    gap: 10px;
  }
  
  .email-input {
    flex: 1;
  }
  
  .verify-button {
    padding: 12px;
    height: 46px;
    border: 1px solid #595959;
    border-radius: 4px;
    font-size: 12px;
    background-color: #ededed;
    cursor: pointer;
    margin-top: 10px;
  }
  
  .verify-button:hover {
    background-color: #e0e0e0;
  }
  
  .submit-button {
    padding: 12px;
    background-color: #0b0b0b;
    border: none;
    border-radius: 4px;
    font-size: 14px;
    color: #fff;
    cursor: pointer;
    height: 65px;
  }
  
  .submit-button:hover {
    background-color: #464646;
  }
  
  .submit-button:disabled {
    cursor: not-allowed;
  }
  
  .links {
    margin-top: 30px;
    display: flex;
    justify-content: space-between;
    font-size: 12px;
    color: #666;
  }
  
  .links a {
    text-decoration: none;
    color: #666;
  }
  
  .links a:hover {
    text-decoration: underline;
  }
  </style>
  
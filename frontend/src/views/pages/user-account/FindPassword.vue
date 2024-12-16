<template>
    <div id="app">
      <div class="logo">
        <a href="../templates/main.html">
          <img src="images/logo.png" alt="백만불트 로고" />
        </a>
      </div>
  
      <div class="container">
        <h2>비밀번호 찾기 - 이메일 인증</h2>
        <p class="description">비밀번호를 찾고자하는 아이디를 입력해주세요.</p>
        <input
          type="text"
          v-model="username"
          placeholder="아이디를 입력해주세요."
          class="input-box"
        />
        <button class="next-btn" @click="handleNext">다 음</button>
  
        <div class="footer-links">
          <a href="#" class="find-id" @click="goToFindID">아이디 찾기</a>
          <a href="#" class="join" @click="goToJoin">회원가입</a>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        username: "",
      };
    },
    methods: {
      handleNext() {
        if (this.username.trim() === "") {
          alert("아이디를 입력해주세요.");
        } else {
          alert(`입력된 아이디: ${this.username}`);
          // 비밀번호 찾기 로직 추가
        }
      },
      goToFindID() {
        alert("아이디 찾기 페이지로 이동합니다.");
        window.location.href = "findID.html"; // 페이지 경로 수정
      },
      goToJoin() {
        alert("회원가입 페이지로 이동합니다.");
        window.location.href = "joininform.html"; // 페이지 경로 수정
      },
    },
  };
  </script>
  
  <style scoped>
  body {
    font-family: Arial, sans-serif;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    margin: 0;
    background-color: white;
  }
  
  .container {
    width: 600px;
    height: 500px;
    background-color: #fff;
    padding: 40px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    border-radius: 10px;
    text-align: center;
  }
  
  h2 {
    font-size: 28px;
    font-weight: bold;
    margin-top: 10px;
    margin-bottom: 30px;
  }
  
  .description {
    font-size: 18px;
    color: #666;
    line-height: 1.5;
    text-align: left;
    padding-left: 20px;
    width: 80%;
    margin-left: 30px;
    margin-bottom: 10px;
    margin-top: 130px;
  }
  
  .input-box {
    width: 80%;
    padding: 12px;
    font-size: 16px;
    border: 1px solid #ccc;
    border-radius: 5px;
    margin-bottom: 30px;
    height: 38px;
  }
  
  .next-btn {
    width: 84%;
    padding: 12px;
    background-color: #2c2c2c;
    font-size: 16px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    height: 60px;
    color: white;
    font-size: 24px;
    font-weight: bold;
  }
  
  .next-btn:hover {
    background-color: #4a4a4a;
  }
  
  .footer-links {
    margin-top: 40px;
  }
  
  .footer-links a {
    color: #a4a4a4;
    font-size: 14px;
    margin: 0 100px;
    text-decoration: none;
  }
  
  .footer-links a:hover {
    text-decoration: underline;
  }
  
  .logo img {
    position: absolute;
    top: 5px;
    left: 5px;
    width: 150px;
  }
  </style>
  
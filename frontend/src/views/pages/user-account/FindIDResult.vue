<template>
    <div class="wrap">
      <div class="header">
      <div class="logo">
        <router-link to="/main">
          <img src="images/logo.png" alt="백만볼트 로고">
        </router-link>
      </div>
      <div class="container">
        <div class="title">아이디 찾기 - 이메일 인증</div>
        <p class="description">고객님 정보와 일치하는 목록입니다.</p>
  
        <div class="info-box" v-if="userInfo">
          <div class="info-item">
            <span class="info-hash"># 아이디 :</span>
            <span class="info-text">{{ userInfo.userId }}</span>
          </div>
          <div class="info-item">
            <span class="info-hash"># 가입일 :</span>
            <span class="info-text">{{ userInfo.joinDate }}</span>
          </div>
        </div>
  
        <div class="buttons">
          <button class="login-button" @click="goToLogin">로그인</button>
          <button class="password-button" @click="findPassword">
            비밀번호 찾기
          </button>
        </div>
      </div>
    </div>
    </div>
  </template>
  
  <script>


  export default {
    data() {
      return {
        userInfo: {
          userId: this.$route.query.userId || '',
          joinDate: this.$route.query.joinDate || '',
        },
      };
    },
    methods: {
      goToLogin() {
        alert("로그인 페이지로 이동합니다.");
        // 로그인 페이지 이동 로직
        this.$router.replace({ path: '/login' });
      },
      findPassword() {
        alert("비밀번호 찾기 페이지로 이동합니다.");
        // 비밀번호 찾기 페이지 이동 로직
        this.$router.replace({ path: '/find-password' });
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
    font-family: "Arial", sans-serif;
    background-color: white;
    display: flex;
    justify-content: center;
    /* align-items: center; */
    height: 100vh;
    margin: 0;
  }
  
  .container {
    width: 500px;
    height: 400px;
    background-color: #fff;
    border: 1px solid #C9D6DE;
    border-radius: 8px;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 30px 24px 24px 24px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    text-align: center;
    margin-bottom: 10%;

  }
  
  .header {
  text-align: center;
}

.logo img {
  width: 150px;
}
  
  .title {
    font-size: 28px;
    font-weight: bold;
    margin-top: 10px;
    margin-bottom: 48px;
    color: #52616a;
  }
  
  .description {
    font-size: 16px;
    /* font-weight: bold; */
    color: #666;
    line-height: 1.5;
    text-align: left;
    padding-left: 10px;
    width: 90%;
    /* margin-top: 80px; */
    margin-bottom: 24px;
    text-align: center;
  }
  
  .info-box {
    width: 100%;
    height: 180px;
    border: 1px solid #c9d6de;
    border-radius: 8px;
    padding: 20px;
    margin-bottom: 5px;
    display: flex;
    justify-content: space-between;
  }
  
  .info-item {
    display: flex;
    align-items: center;
    gap: 5px;
    font-size: 14px;
  }
  
  .info-hash {
    color: #666;
    font-weight: bold;
  }
  
  .info-text {
    color: #1E2022;
    font-size: 16px;
    font-weight: bold;
  }
  
  .buttons {
    width: 100%;
    display: flex;
    justify-content: space-between;
    margin-top: 20px;
  }
  
  button {
    width: 49%;
    padding: 15px;
    font-size: 16px;
    border: none;
    border-radius: 8px;
    cursor: pointer;
  }
  
  .login-button {
    background-color: #52616a;
    color: white;
  }
  
  .password-button {
    background-color: #52616a;
    color: white;
  }
  
  button:hover {
    opacity: 0.9;
    background-color: #1E2022;
  }
  </style>
  
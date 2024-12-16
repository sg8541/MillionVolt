<template>
    <div id="app" class="container">
      <h1>회원 탈퇴</h1>
  
      <h3>
        회원탈퇴 시 유의사항 <span>[필수]</span>
      </h3>
  
      <div class="info-box">
        탈퇴한 아이디는 재사용 및 복구가 불가하며, 발급된 회원카드는 사용하실 수 없습니다.
        <br />
        신중하게 선택하시기 바랍니다.
        <p>문의: gjsdms1244@gmail.com</p>
      </div>
  
      <div class="checkbox-container">
        <label>
          <input type="checkbox" v-model="agree" />
          회원탈퇴 시 유의사항을 확인하였으며 동의합니다.
        </label>
      </div>
  
      <input
        type="password"
        v-model="password"
        placeholder="비밀번호 입력"
        required
      />
  
      <div class="buttons">
        <button class="mypage" @click="goToMypage">마이페이지</button>
        <button
          class="withdraw"
          :disabled="!agree || !password"
          @click="handleWithdraw"
        >
          회원탈퇴하기
        </button>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        agree: false,
        password: "",
      };
    },
    methods: {
      goToMypage() {
        alert("마이페이지로 이동합니다.");
        // 페이지 이동 로직 추가
      },
      handleWithdraw() {
        if (!this.password) {
          alert("비밀번호를 입력해주세요.");
          return;
        }
        alert("회원탈퇴가 완료되었습니다.");
        // 회원탈퇴 API 호출
      },
    },
  };
  </script>
  
  <style scoped>
  /* 전체 페이지 스타일 */
  body {
    font-family: Arial, sans-serif;
    margin: 0;
    padding: 0;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background-color: white;
  }
  
  .container {
    width: 800px;
    background-color: white;
    padding: 20px;
    text-align: center;
  }
  
  h1 {
    font-size: 36px;
    margin-bottom: 20px;
    padding: 10px;
  }
  
  h3 {
    font-size: 24px;
    text-align: left;
    margin-bottom: 10px;
  }
  
  span {
    color: #1c32e0;
  }
  
  .info-box {
    height: 130px;
    background-color: #f4f8ff;
    padding: 15px;
    border-radius: 8px;
    text-align: left;
    margin-bottom: 20px;
    font-size: 18px;
    line-height: 1.6;
  }
  
  .info-box p {
    color: #0056b3;
    text-decoration: none;
    font-weight: bold;
  }
  
  .checkbox-container {
    text-align: left;
    margin-bottom: 60px;
    font-size: 16px;
  }
  
  input[type="checkbox"] {
    transform: scale(1.4);
    margin-right: 10px;
  }
  
  input[type="password"] {
    width: 50%;
    padding: 10px;
    margin-bottom: 20px;
    border: 1px solid #ccc;
    border-radius: 5px;
    font-size: 14px;
    height: 35px;
    outline: none;
  }
  
  .buttons {
    margin-top: 15px;
    margin-left: 200px;
    width: 50%;
    text-align: center;
    display: flex;
    justify-content: space-between;
  }
  
  .buttons button {
    padding: 10px 20px;
    border-radius: 5px;
    width: 180px;
    height: 50px;
    font-size: 14px;
    cursor: pointer;
  }
  
  .buttons .mypage {
    background-color: #ffffff;
    color: black;
    border: 1px solid black;
  }
  
  .buttons .mypage:hover {
    background-color: #ededed;
  }
  
  .buttons .withdraw {
    background-color: #151515;
    border: none;
    color: white;
  }
  
  .buttons .withdraw:hover {
    background-color: #333;
  }
  
  .buttons .withdraw:disabled {
    background-color: #ccc;
    cursor: not-allowed;
  }
  </style>
  
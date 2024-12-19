<template>
  <div class="wrap">
    <div class="header">
      <div class="logo">
        <router-link to="/main">
          <img src="images/logo.png" alt="백만볼트 로고">
        </router-link>
      </div>

      <div class="container">
        <div class="content">
          <h2>비밀번호 찾기 - 이메일 인증</h2>
          <p class="description">비밀번호를 찾고자 하는 아이디를 <br>
            입력해 주세요.</p>
          <input type="text" v-model="userId" placeholder="아이디를 입력해주세요." class="input-box" />
          <button class="next-btn" @click="handleNext">다 음</button>

          <div class="footer-links">
            <a href="#" class="find-id" @click="goToFindID">아이디 찾기</a>
            <a href="#" class="join" @click="goToJoin">회원가입</a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import api from '@/axios'

export default {
  data() {
    return {
      userId: "",
    };
  },
  methods: {
    async handleNext() {
      if (this.userId.trim() === "") {
        alert("아이디를 입력해주세요.");
      } else {
        // 비밀번호 찾기 로직 추가
        try {
          const response = await api.get(`/findPwd/${this.userId}`);
          if (response.status === 200) {
            alert(`입력하신 아이디는 ${this.userId} 입니다.`);
            this.$router.push({
              path: '/find-password/email',
              query: {
                userId: this.userId,
              },
            });
          } else {
            alert("존재하지 않거나 잘못된 아이디입니다. 다시 확인해주세요.")
          }
        } catch (error) {
          console.error("비밀번호 찾기 에러:", error);
          alert("오류가 발생했습니다. 다시 시도해주세요.");
        }
      }
    },
    goToFindID() {
      alert("아이디 찾기 페이지로 이동합니다.");
      this.$router.push({ path: '/find-id' }); // 페이지 경로 수정
    },
    goToJoin() {
      alert("회원가입 페이지로 이동합니다.");
      this.$router.push({ path: '/agreement' }); // 페이지 경로 수정
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
  font-family: Arial, sans-serif;
  display: flex;
  justify-content: center;
  /* align-items: center; */
  height: 100vh;
  margin: 0;
  background-color: white;
}

.container {
  width: 500px;
  background-color: #fff;
  border: 1px solid #c9d6de;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  text-align: center;
  margin-bottom: 10%;
  align-content: center;

}

.container .content{
  padding: 30px 24px 28px 24px;
}

.content {
  margin: 0 auto;
}

h2 {
  font-size: 28px;
  margin-top: 10px;
  margin-bottom: 48px;
  color: #52616a;
}

.description {
  font-size: 16px;
  color: #666;
  line-height: 1.5;
  margin-bottom: 24px;
}

.input-box {
  width: 100%;
  padding: 12px;
  font-size: 16px;
  border: 1px solid #c9d6de;
  border-radius: 5px;
  margin-bottom: 24px;
  height: 60px;
}


.input-box input:focus {
  outline: none;
  border: 2px solid #52616A;
}

.next-btn {
  width: 100%;
  padding: 12px;
  background-color: #52616a;
  font-size: 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  height: 65px;
  color: white;
}

.next-btn:hover {
  background-color: #1E2022;
}

.footer-links {
  margin-top: 22px;
  display: flex;
  justify-content: space-between;
  font-size: 14px;
}

.footer-links a {
  color: #a4a4a4;
  font-size: 14px;
  text-decoration: none;
}

.footer-links a:hover {
  /* text-decoration: underline; */
  color:#1E2022;
}

.header {
  text-align: center;
}

.logo img {
  width: 150px;
}
</style>
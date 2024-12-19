<template>
  <div class="wrap">
    <!-- 로고 -->
    <div class="header">
      <div class="logo">
        <router-link to="/main">
          <img src="images/logo.png" alt="백만볼트 로고">
        </router-link>
      </div>

      <div class="login-container">
        <h2>새로운 비밀번호 입력</h2>
        <p class="description">새로운 비밀번호를 입력해주세요</p>
        <p class="description2"># 아이디 : <span class="description3">{{ userId }}</span></p>

        <form @submit.prevent="handleSubmit">
          <div class="input-group">
            <input type="password" v-model="password" placeholder="새 비밀번호" required />
          </div>

          <div class="input-group">
            <input type="password" v-model="passwordConfirm" placeholder="새 비밀번호 재확인" required />
          </div>

          <button type="submit" class="login-button">비밀번호 변경</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import api from '@/axios'
export default {
  data() {
    return {
      userId: this.$route.query.userId || '',
      password: "",
      passwordConfirm: "",
    };
  },
  created() {
    this.userId = this.$route.query.userId || '';  // 쿼리 파라미터가 존재하면 userId를 할당
  },
  methods: {
    async handleSubmit() {
      // 비밀번호 변경 API 호출
      if (this.password === this.passwordConfirm) {
        try {
          const response = await api.post(`/resetPwd`, {
            password: this.password,
            userId: this.userId,
            username: this.$route.query.username,
            email: this.$route.query.email,
          })
          if (response.status === 200) {
            alert("비밀번호가 성공적으로 변경되었습니다.");
            alert("로그인 화면으로 이동합니다.");
            this.$router.replace({ path: '/login' })
          }
        } catch (error) {
          alert("비밀번호 초기화 중 에러가 발생하였습니다. " + error);
        }
      } else {
        alert("비밀번호가 일치하지 않습니다.");
      }
    },
  },

  beforeRouteUpdate(to, from, next) {
    // 쿼리 파라미터에서 userId, username, email 확인
    if (to.query.userId) {
      this.userId = to.query.userId;
    }
    if (to.query.username) {
      this.username = to.query.username;
    }
    if (to.query.email) {
      this.email = to.query.email;
    }

    // 모든 값이 업데이트된 후 next 호출
    next();
  }
};
</script>

<style scoped>
/* 전체 페이지 스타일 */
.wrap {
  margin: 0;
  padding: 0;
  font-family: Arial, sans-serif;
  background-color: white;
  height: 100vh;
  display: flex;
  align-items: center;
  flex-direction: column;
}

/* 로고 스타일 */
.header {
  text-align: center;
}

.logo img {
  width: 150px;
}

form{
  width: 100%;
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
  color: #52616a;
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


.input-group input {
  height: 60px;
  padding: 12px;
  border: 1px solid #c9d6de;
  border-radius: 4px;
  font-size: 16px;
}

.input-group input:focus {
  outline: none;
  border: 2px solid #52616A;

  /* 기본 포커스 테두리 제거 */
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
  margin-top: 15px;
}

.login-button:hover {
  background-color: #363636;
}

.description {
    font-size: 16px;
    color: #666;
    line-height: 1.5;
    margin-bottom: 24px;
  }

.description2 {
  font-size: 16px;
  color: #666;
  line-height: 1.5;
  margin-bottom: 10px;
  text-align: left;
  padding-left: 10px;
  width: 95%;
}
.description2 .description3{
  color: #52616A;
  font-weight: 600;
}
</style>
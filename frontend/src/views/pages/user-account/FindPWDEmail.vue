<template>
  <div class="wrap">
    <div class="header">
      <div class="logo">
        <router-link to="/main">
          <img src="../../../../images/logo.png" alt="백만볼트 로고">
        </router-link>
      </div>

      <div class="container">
        <div class="title">비밀번호 찾기 - 이메일 인증</div>
        <p class="description">본인확인 이메일과 입력한 이메일이 같아야,<br>인증번호를 받을 수 있습니다.</p>
        <p class="description2"># 아이디 : <span class="description3">{{ userId }}</span></p>

        <form class="form" @submit.prevent="handleSubmit">
          <input type="text" class="input-field" placeholder="이름을 입력해주세요." v-model="username" required />

          <div class="email-row">
            <input type="email" class="input-field email-input" placeholder="이메일을 입력해주세요." v-model="email" required />
            <button type="button" class="verify-button" @click="sendVerificationCode">
              인증번호 받기
            </button>
          </div>

          <input type="text" class="input-field" placeholder="인증 번호 6자리 입력" v-model="verificationCode" required />

          <button type="submit" class="submit-button" :disabled="!canSubmit">
            비밀번호 찾기
          </button>
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
      userId: this.$route.query.userId || "",
      username: "",
      email: "",
      verificationCode: "",
      sentCode: '',  // 서버에서 받은 인증번호 저장
      isVerified: false,
    };
  },
  computed: {
    canSubmit() {
      return this.username && this.email && this.verificationCode.length === 6;
    },
  },
  methods: {
    async sendVerificationCode() {
      if (!this.email) {
        alert("이메일을 입력해주세요.");
        return;
      }
      // 여기에 API 호출 로직 추가
      try {
        const response = await api.get(`/email/${this.email}`);
        if (response.status === 200) {
          this.sentCode = response.data;  // 서버에서 전송된 인증번호 저장
          console.log("Sent Code:", this.sentCode);
          alert(`인증번호가 ${this.email}로 전송되었습니다.`);
        }
      } catch (error) {
        console.error("인증번호 전송 오류:", error);
        alert("오류가 발생했습니다. 다시 시도해주세요.");
      }
    },
    async handleSubmit() {
      if (this.verificationCode.toString() !== this.sentCode.toString()) {
        console.log("verificationCode : " + this.verificationCode);
        alert("잘못된 인증번호 입니다.");
        this.verificationCode = '';  // 입력 필드 초기화
        return;
      } else if (!this.canSubmit) {
        alert("모든 필드를 입력해주세요.");
        return;
      }
      // 비밀번호 찾기 API 호출 로직 추가
      try {
        const response = await api.get(`/findPwd/${this.username}/${this.email}`);
        if (response.status == 200) {
          alert("비밀번호 찾기 요청이 제출되었습니다.");
          this.$router.replace({
            path: '/new-password',
            query: {
              userId: this.userId,
            },
          });
        }
      } catch (error) {
        console.error("비밀번호 찾기 오류:", error);
        alert("오류가 발생했습니다. 다시 시도해주세요.");
      }
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
  height: 100vh;
  margin: 0;
  background-color: white;
}

.container {
  width: 500px;
  background-color: #fff;
  padding: 30px 24px 28px 24px;
  border: 1px solid #c9d6de;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  text-align: center;
  align-content: center;
  margin-bottom: 100px;
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
  margin-bottom: 30px;
  color:#52616a;
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
  width: 90%;
}
.description2 .description3{
  color: #52616A;
  font-weight: 600;
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
  border: 1px solid #c9d6de;
  border-radius: 4px;
  font-size: 16px;
}

input{
  outline: none;
}

.input-field:focus {
    border: 2px solid #52616A;
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
    height: 55px;
    border-radius: 4px;
    font-size: 14px;
    background-color: #C3C3C3;
    cursor: pointer;
    margin-top: 2px;
    color: #fff;
}
  
  .verify-button:hover {
    background-color: #52616A;
  }

.submit-button {
    padding: 12px;
    background-color: #52616A;
    border: none;
    border-radius: 4px;
    font-size: 16px;
    color: #fff;
    cursor: pointer;
    height: 65px;
    margin-top:15px;
  }

  .submit-button:hover {
    background-color: #1E2022;
  }

.submit-button:disabled {
  cursor: not-allowed;
  background-color: #b1b1b1;
}
</style>

<template>
  <div id="wrap">
    <div class="logo">
      <a href="../templates/main.html">
        <img src="images/logo.png" alt="백만불트 로고" />
      </a>
    </div>

    <div class="container">
      <div class="title">비밀번호 찾기 - 이메일 인증</div>
      <p class="description"># 아이디 : {{ userId }}</p>

      <form class="form" @submit.prevent="handleSubmit">
        <input
          type="text"
          class="input-field"
          placeholder="이름을 입력해주세요."
          v-model="name"
          required
        />

        <div class="email-row">
          <input
            type="email"
            class="input-field email-input"
            placeholder="이메일을 입력해주세요."
            v-model="email"
            required
          />
          <button
            type="button"
            class="verify-button"
            @click="sendVerificationCode"
          >
            인증번호 받기
          </button>
        </div>

        <input
          type="text"
          class="input-field"
          placeholder="인증 번호 6자리 입력"
          v-model="verificationCode"
          required
        />

        <button
          type="submit"
          class="submit-button"
          :disabled="!canSubmit"
        >
          비밀번호 찾기
        </button>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      userId: "재헌짱123",
      name: "",
      email: "",
      verificationCode: "",
      isVerified: false,
    };
  },
  computed: {
    canSubmit() {
      return this.name && this.email && this.verificationCode.length === 6;
    },
  },
  methods: {
    sendVerificationCode() {
      if (!this.email) {
        alert("이메일을 입력해주세요.");
        return;
      }
      alert(`인증번호가 ${this.email}로 전송되었습니다.`);
      // 여기에 API 호출 로직 추가
    },
    handleSubmit() {
      if (!this.canSubmit) {
        alert("모든 필드를 입력해주세요.");
        return;
      }
      alert("비밀번호 찾기 요청이 제출되었습니다.");
      // 비밀번호 찾기 API 호출 로직 추가
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
  align-items: center;
  height: 100vh;
}

.container {
  width: 600px;
  height: 600px;
  background-color: #fff;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.logo img {
  position: absolute;
  top: 5px;
  left: 5px;
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
  margin-top: 100px;
  margin-bottom: 10px;
  text-align: left;
  padding-left: 10px;
  width: 90%;
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
  background-color: #b1b1b1;
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

<template>
  <div class="wrap">
    <div class="header">
      <div class="logo">
        <router-link to="/main">
          <img src="/images/logo.png" alt="백만볼트 로고">
        </router-link>
      </div>
      <div class="container">
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
        <div class="script">
          <span>안전한 회원탈퇴를 위해 비밀번호를 다시 한 번 입력해주세요.</span>
          <input type="password" v-model="password" placeholder="비밀번호 입력" required />
        </div>


        <div class="buttons">
          <button class="mypage" @click="goToMypage">마이페이지</button>
          <button class="withdraw" :disabled="!agree || !password" @click="handleWithdraw">
            회원탈퇴하기
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import api from '@/axios'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth';

export default {
  setup() {
    const route = useRoute();
    const router = useRouter(); // useRouter 추가
    const id = route.params.id;

    const goToMypage = () => {
      alert('마이페이지로 이동합니다.');
      router.push(`/myinfo/dashboard/${id}`); // router.push로 변경
    };
    const authStore = useAuthStore();

    return {
      id,
      goToMypage,
      router,
      authStore
    };
  },
  data() {
    return {
      agree: false,
      password: "",
    };
  },
  methods: {
    async handleWithdraw() {
      if (!this.password) {
        alert("비밀번호를 입력해주세요.");
        return;
      }
      try {
        const response = await api.post(`exit/${this.id}`, {
          password: this.password,
        })
        if (response.status == 200) {
          alert('회원탈퇴가 완료되었습니다. 이용해주셔서 감사합니다.')
          this.handleLogout();
        }
      } catch (error) {
        if (error.response.status == 400) {
          alert('비밀번호를 다시 확인해주세요')
        } else if (error.response.status == 403) {
          alert('회원탈퇴 중 문제가 발생하였습니다. 403' + error)
        } else {
          alert('회원탈퇴 중 문제가 발생하였습니다.' + error)
        }
      }
    },
    async handleLogout() {
      try {
        await this.authStore.logout()
        console.log("로그아웃 실행");
        this.router.replace("/main");// 로그아웃 후 홈으로 이동
      } catch (error) {
        console.error('로그아웃 실패:', error)
      }
    }
  },
};
</script>

<style scoped>
/* 전체 페이지 스타일 */
.wrap {
  font-family: Arial, sans-serif;
  margin: 0;
  padding: 0;
  display: flex;
  justify-content: center;
  height: 100vh;
  background-color: white;
}

.container {
  width: 600px;
  background-color: #fff;
  padding: 30px 24px 28px 24px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border: 1px solid #C9D6DE;
  border-radius: 8px;
  text-align: center;
  margin-bottom: 10%;
}

.header {
  text-align: center;
}

.logo img {
  width: 150px;
}

h1 {
  font-size: 28px;
  margin-top: 10px;
  margin-bottom: 48px;
  padding: 10px;
}

h3 {
  font-size: 20px;
  text-align: left;
  margin-bottom: 10px;
}

span {
  color: #1c32e0;
}

.script span {
  color: #52616a;
}

.info-box {
  background-color: #f4f8ff;
  padding: 20px;
  border-radius: 8px;
  text-align: left;
  margin-bottom: 20px;
  font-size: 16px;
  line-height: 1.6;
}

.info-box p {
  color: #0056b3;
  text-decoration: none;
  font-weight: bold;
}

.checkbox-container {
  text-align: left;
  margin-bottom: 34px;
  font-size: 16px;
}

input[type="checkbox"] {
  transform: scale(1.4);
  margin-right: 10px;
}

input[type="password"] {
  width: 50%;
  padding: 10px;
  margin-top: 10px;
  margin-bottom: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 14px;
  height: 35px;
  outline: none;
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

.buttons button {
  background-color: #c9d6de;
  color: #52616a;
}

button:hover {
  opacity: 0.9;
  background-color: #1E2022;
}

.buttons .mypage {
  background-color: #52616a;
  color: #fff;
}

.buttons .mypage:hover {
  opacity: 0.9;
  background-color: #1E2022;
}

.buttons .withdraw {
  border: none;
  color: white;
}


.buttons .withdraw:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
</style>
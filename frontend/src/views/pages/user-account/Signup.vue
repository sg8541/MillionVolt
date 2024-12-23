<template>
<div class="wrap">
  <div class="header">
    <router-link to="/main" class="logo">
      <img src="images/logo.png" alt="백 만 볼트 로고" class="logo">
    </router-link>
  </div>

    <div class="container">

      <form @submit.prevent="submitForm">
        <div class="section">
          <div class="h2-div">
            <h2>기본정보 입력</h2>
          </div>
          <div class="form-row">
            <label for="name">이름<span class="star">*</span></label>
            <input v-model="form.username" type="text" id="name" placeholder="이름을 입력해주세요" required />
          </div>
          <div class="form-row">
            <label for="id">아이디<span class="star">*</span></label>
            <input v-model="form.user_id" type="text" id="user_id" placeholder="아이디를 입력해주세요" required />
            <button type="button" class="verify-btn" @click="checkIdDuplicate('user_id')">중복확인</button>
          </div>
          <div class="form-row">
            <label for="password">비밀번호<span class="star">*</span></label>
            <input v-model="form.password" type="password" id="password" placeholder="비밀번호를 입력해주세요" required />
          </div>
          <small>비밀번호는 8자리 이상, 영문 숫자가 포함되어야 합니다.</small>
          <div class="form-row">
            <label for="confirm-password">비밀번호 재확인<span class="star">*</span></label>
            <input v-model="form.confirm_password" type="password" id="confirm-password" placeholder="비밀번호를 다시 입력해주세요"
              required />
          </div>
          <span class="pwd-check" v-if="passwordMismatch">비밀번호가 일치하지 않습니다.</span>
        </div>

        <div class="section">
          <div class="h2-div">
            <h2>상세 정보 입력</h2>
          </div>
          <div class="form-row">
            <label for="email">이메일<span class="star">*</span></label>
            <input v-model="form.email" type="email" id="email" placeholder="이메일을 입력해주세요" required />
            <button type="button" class="verify-btn" @click="checkEmailDuplicate('email')">중복확인</button>
          </div>
          <div class="form-row">
            <label for="phone">전화번호<span class="star">*</span></label>
            <input v-model="form.phone_number" type="tel" id="phone" placeholder="전화번호를 입력해주세요"
              @input="formatPhoneNumber" required />
          </div>
          <span v-if="showPhoneError" class="pwd-check">유효하지 않은 전화번호입니다.</span>
          <div class="form-row">
            <label for="car">차종<span class="star">*</span></label>
            <select v-model="form.modal_id" id="car" required>
              <option v-for="car in cars" :value="car.value" :key="car.value">{{ car.text }}</option>
            </select>
          </div>
          <div class="form-row">
            <label>선호 충전기 타입</label>
            <select v-model="form.charger_speed_id" id="charging" required>
              <option value="">선택안함</option>
              <option v-for="charger in chargers" :value="charger.value" :key="charger.value">{{ charger.text }}
              </option>
            </select>
          </div>
          <div class="form-row">
            <label for="car-number">차량번호<span class="star">*</span></label>
            <input v-model="form.car_number" type="text" id="car-number" placeholder="차량번호를 입력해주세요" required />
          </div>
        </div>
        <div class="submit-container">
          <button type="submit" class="submit-btn">가입 하기</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import api from '../../../axios' // Axios 인스턴스 import


export default {
  data() {
    return {
      idCheck: false,
      emailCheck: false,
      form: {
        username: '',
        user_id: '',
        password: '',
        confirm_password: '',
        email: '',
        phone_number: '',
        modal_id: '',
        charge_speed_id: 4,
        car_number: '',
      },
      cars: [
        { value: 1, text: 'EV6' },
        { value: 2, text: 'EV9' },
        { value: 3, text: 'IONIQ6' },
        { value: 4, text: 'IONIQ5' },
        { value: 5, text: 'Tesla Model S' },
        { value: 6, text: 'Tesla Model X' },
        { value: 7, text: 'Taycan' },
        { value: 8, text: 'Mercedes-Benz EQC' },
      ],
      chargers: [
        { value: 1, text: '7kw' },
        { value: 2, text: '50kw' },
        { value: 3, text: '100kw' },
        { value: 4, text: '200kw' },
        { value: 5, text: '300kw 이상' },
      ],
    };
  },

  computed: {
    // 비밀번호 확인 메세지
    passwordMismatch() {
      return this.form.password && this.form.confirm_password && this.form.password !== this.form.confirm_password;
    },
    // 전화번호 확인 메세지
    showPhoneError() {
      return this.form.phone_number && !this.isValidPhoneNumber();
    },
  },
  methods: {
    // 비밀번호 패턴 검증(숫자+영어 8자리 이상, 특수문자 허용)
    passwordValidation(password) {
      const passwordRegex = /^[a-zA-Z\d\W_]{8,}$/;
      return passwordRegex.test(password);
    },
    // 전화번호 패턴 검증 + 자동 하이픈 생성
    formatPhoneNumber(event) {
      let value = event.target.value.replace(/[^0-9]/g, ''); // 숫자만 남김

      if (value.length <= 7) {
        // 123-4567
        this.form.phone_number = value.replace(/^(\d{3})(\d{4})$/, `$1-$2`);
      } else if (value.length <= 9) {
        // 02-123-4567
        this.form.phone_number = value.replace(/^(\d{2})(\d{3})(\d{4})$/, `$1-$2-$3`);
      } else if (value.length <= 10) {
        // 055-132-4567 (10자리)
        this.form.phone_number = value.replace(/^(\d{3})(\d{3})(\d{4})$/, `$1-$2-$3`);
      } else if (value.length === 11) {
        // 010-1234-5678 (11자리)
        this.form.phone_number = value.replace(/^(\d{3})(\d{4})(\d{4})$/, `$1-$2-$3`);
      }
    },
    // 전화번호 유효성 검사 정규식
    isValidPhoneNumber() {
      const phoneRegex = /^(01[016789]|02|0[3-9][0-9])-\d{3,4}-\d{4}$/;
      return phoneRegex.test(this.form.phone_number);
    },

    // 자동차 번호 검증(12가4567 또는 123가4567)
    validateCarNumber(carNumber) {
      const trimmedCarNumber = carNumber.replace(/\s+/g, ''); // 공백 제거
      const carNumberRegex = /^(\d{2}|\d{3})[가-힣]\d{4}$/;
      return carNumberRegex.test(trimmedCarNumber);
    },
    // 가입 form 제출
    async submitForm() {
      if (this.passwordMismatch) {
        alert("비밀번호가 일치하지 않습니다.");
        return;
      }
      if (!this.passwordValidation(this.form.password)) {
        alert("비밀번호는 8자리 이상, 영문과 숫자를 포함해야 합니다.");
        return;
      }
      if (!this.idCheck) {
        alert("아이디 중복 검사는 필수입니다.");
        return;
      }
      if (!this.emailCheck) {
        alert("이메일 중복 확인은 필수입니다.");
        return;
      }
      if (this.showPhoneError) {
        alert("유효하지 않은 전화번호입니다. 예: 02-3456-7890 또는 010-1234-5678");
        return;
      }
      if (!this.validateCarNumber(this.form.car_number)) {
        alert("유효하지 않은 차량번호입니다. 예: 123가4567 또는 12가4567");
        return;
      }
      try {
        const response = await api.post('/signup', {
          username: this.form.username,
          userId: this.form.user_id,
          email: this.form.email,
          phoneNumber: this.form.phone_number,
          password: this.form.password,
          modelId: this.form.modal_id,
          chargerSpeedId: this.form.charger_speed_id,
          carNumber: this.form.car_number
        })
        if (response.status == 200) {
          alert('가입 신청이 완료되었습니다!');
          console.log(this.form);
          window.location.href = '/login';
        }
      } catch (error) {
        if(error.response.status == 403){
          alert("회원가입 중 문제가 발생하였습니다. 잠시 후 다시 수행해 주시길 바랍니다.")
        }else {
          alert("문제가 발생하였습니다." + error)
        }
      }
    },
    // 아이디 중복 검사
    async checkIdDuplicate(field) {
      const userId = this.form.user_id;  // user_id 값 가져오기
      console.log(userId);
      try {
        const response = await api.get(`/id-check?user_id=${userId}`);
        if (response.status === 200) {
          alert("사용할 수 있는 아이디입니다.")
          this.idCheck = true;
        }
      } catch (error) {
        if (error.response.status == 400) {
          alert("사용할 수 없는 아이디입니다.")
          this.idCheck = false;
        } else {
          alert("중복 검사 중 오류가 발생하였습니다." + error)
        }
      }
    },
    // 이메일 중복 체크
    async checkEmailDuplicate(field) {
      const email = this.form.email;  // user_id 값 가져오기
      console.log(email);
      try {
        const response = await api.get(`/email-check?email=${email}`);
        if (response.status === 200) {
          alert("사용할 수 있는 이메일입니다.")
          this.emailCheck = true;
        }
      } catch (error) {
        if (error.response.status == 400) {
          alert("사용할 수 없는 이메일입니다.")
          this.emailCheck = false;
        } else {
          alert("중복 검사 중 오류가 발생하였습니다." + error)
        }
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

.v-application__wrap{
  background-color: white;
}

.wrap {
  margin: 0;
  padding: 0;
  font-family: Arial, sans-serif;
  background-color: white;
  display: flex;
  align-items: center;
  flex-direction: column;
}

.container {
  width: 600px;
  border: 1px solid #C9D6DE;
  /* height: 850px; */
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  padding: 30px;
  margin-bottom: 10%;
}

.header {
  text-align: center;
}

.logo img {
  width: 150px;
}

/* .wrap .header .logo {
  text-align: center;
  position: absolute;
} */

h2 {
  font-size: 18px;
  margin-bottom: 10px;
}

.section {
  margin-bottom: 30px;
}

.form-row {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.form-row label {
  width: 120px;
  font-size: 14px;
  margin-right: 10px;
  text-align: left;
}

select,
.form-row input {
  flex: 1;
  padding: 12px;
  border: 1px solid #C9D6DE;
  border-radius: 4px;
  font-size: 14px;
}

.verify-btn {
  background-color: #C3C3C3;
  color: #fff;
  padding: 8px 12px;
  font-size: 14px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-left: 10px;

}

.verify-btn:hover {
  background-color: #52616A;
  color: white;
}


.submit-container {
  text-align: center;
}

.submit-btn {
  background-color: #52616A;
  color: white;
  padding: 12px 20px;
  font-size: 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  width: 100%;
}

.submit-btn:hover {
  background-color: #1E2022;
}

small,
.pwd-check {
  display: block;
  font-size: 12px;
  color: gray;
  margin-top: 3px;
  margin-bottom: 10px;
  margin-left: 135px;
}

.pwd-check {
  color: red;
}

.star {
  color: blue;
}

.h2-div {
  border-bottom: 3px solid #3c3c3c;
  margin-bottom: 20px;
}


select:focus {
  border-color: #007BFF;
  /* 포커스 시 테두리 색상 변경 */
  outline: none;
  /* 기본 포커스 테두리 제거 */
  box-shadow: 0 0 4px rgba(0, 123, 255, 0.5);
  /* 포커스 시 그림자 효과 */
}

select option {
  padding: 10px;
  /* 옵션 내부 여백 */
}

input {
  outline: none;
}

input:focus {
  border: 2px solid #52616A;
  /* 기본 포커스 테두리 제거 */
}
</style>
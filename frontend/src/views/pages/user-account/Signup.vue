<template>
        <div class="header">
      <a href="../templates/main.html"><img src="images/logo.png" alt="백 만 볼트 로고" class="logo"></a>
    </div>
    <div class="wrap">

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
            <input v-model="form.user_id" type="text" id="id" placeholder="아이디를 입력해주세요" required />
            <button type="button" class="verify-btn" @click="checkDuplicate('user_id')">중복확인</button>
          </div>
          <div class="form-row">
            <label for="password">비밀번호<span class="star">*</span></label>
            <input v-model="form.password" type="password" id="password" placeholder="비밀번호를 입력해주세요" required />
          </div>
          <small>비밀번호는 8자리 이상, 영문 숫자가 포함되어야 합니다.</small>
          <div class="form-row">
            <label for="confirm-password">비밀번호 재확인<span class="star">*</span></label>
            <input v-model="form.confirm_password" type="password" id="confirm-password" placeholder="비밀번호를 다시 입력해주세요" required />
          </div>
          <span class="pwd-check" v-if="passwordMismatch">비밀번호 불일치 여부</span>
        </div>
  
        <div class="section">
          <div class="h2-div">
            <h2>상세 정보 입력</h2>
          </div>
          <div class="form-row">
            <label for="email">이메일<span class="star">*</span></label>
            <input v-model="form.email" type="email" id="email" placeholder="이메일을 입력해주세요" required />
            <button type="button" class="verify-btn" @click="checkDuplicate('email')">중복확인</button>
          </div>
          <div class="form-row">
            <label for="phone">전화번호<span class="star">*</span></label>
            <input v-model="form.phone_number" type="tel" id="phone" placeholder="전화번호를 입력해주세요" required />
          </div>
          <div class="form-row">
            <label for="car">차종<span class="star">*</span></label>
            <select v-model="form.modal_id" id="car" required>
              <option v-for="car in cars" :value="car.value" :key="car.value">{{ car.text }}</option>
            </select>
          </div>
          <div class="form-row">
            <label>선호 충전기 타입</label>
            <select v-model="form.charger_type" id="charging" required>
              <option value="">선택안함</option>
              <option v-for="charger in chargers" :value="charger.value" :key="charger.value">{{ charger.text }}</option>
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
  export default {
    data() {
      return {
        form: {
          username: '',
          user_id: '',
          password: '',
          confirm_password: '',
          email: '',
          phone_number: '',
          modal_id: '',
          charger_type: '',
          car_number: '',
        },
        cars: [
          { value: '1', text: 'EV6' },
          { value: '2', text: 'EV9' },
          { value: '3', text: 'IONIQ6' },
          { value: '4', text: 'IONIQ5' },
          { value: '5', text: 'Tesla Model S' },
          { value: '6', text: 'Tesla Model X' },
          { value: '7', text: 'Taycan' },
          { value: '8', text: 'Mercedes-Benz EQC' },
        ],
        chargers: [
          { value: '7kw', text: '7kw' },
          { value: '50kw', text: '50kw' },
          { value: '100kw', text: '100kw' },
          { value: '200kw', text: '200kw' },
          { value: '300kw+', text: '300kw 이상' },
        ],
      };
    },
    computed: {
      passwordMismatch() {
        return this.form.password && this.form.confirm_password && this.form.password !== this.form.confirm_password;
      },
    },
    methods: {
      checkDuplicate(field) {
        alert(`${field} 중복 확인 요청됨.`);
      },
      submitForm() {
        alert('가입 신청이 완료되었습니다!');
        console.log(this.form);
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
    background-color: white;
    display: flex;
    justify-content: center;
    /* align-items: center; */
    /* height: 100vh; */
    margin:0 auto 100px auto;
}


.container {
    width: 600px;
    height : 850px;
    background-color: white;
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    padding: 30px;
}

.header {
    text-align: center;
    /* margin-bottom: 30px; */
}

.wrap .header .logo{
  text-align: center;
  position: absolute;
}


.logo {
    /* top: 5px;
    left: 5px; */
    width: 150px;
}

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

select, .form-row input {
    flex: 1;
    padding: 12px;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 14px;
}

.verify-btn {
    background-color: #a5a5a5;
    color: white;
    padding: 8px 12px;
    font-size: 14px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    margin-left: 10px;
}

.verify-btn:hover {
    background-color: #7e7e7e;
}


.submit-container {
    text-align: center;
}

.submit-btn {
    background-color: #0b0b0b;
    color: white;
    padding: 12px 20px;
    font-size: 16px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    width: 100%;
}

.submit-btn:hover {
    background-color: #3c3c3c;
}

small, .pwd-check {
display: block;
font-size: 12px;
color: gray;
margin-top: 3px;
margin-bottom : 10px;
margin-left : 135px;
}

.pwd-check {
    color: red;
}
.star{
   color : blue;
}
.h2-div{
  border-bottom: 3px solid #3c3c3c;
  margin-bottom : 20px;
}


select:focus {
    border-color: #007BFF; /* 포커스 시 테두리 색상 변경 */
    outline: none; /* 기본 포커스 테두리 제거 */
    box-shadow: 0 0 4px rgba(0, 123, 255, 0.5); /* 포커스 시 그림자 효과 */
}

select option {
    padding: 10px; /* 옵션 내부 여백 */
}

input{
   outline: none;
}
</style>

<template>
    <div class="reservation-go-info-title">예약 진행 및 예약 상세 정보</div>

    <div class="reservation-username-title">예약자</div>
    <div class="reservation-username">
            
    </div>

    <div class="reservation-address-title">주소</div>
    <div class="reservation-address">
            
    </div>

    <div class="reservation-chargerType-title">충전기 타입</div>
    <div class="reservation-chargerType">

    </div>

    <div class="reservation-deposit-title">보증금</div>
    <div class="reservation-depotsit">
        5.000원
    </div>

    <div class="reserve-date-container-title">예약 날짜 및 시간</div>
    <div class="reserve-date-container">
        <div class="date-container">
            <label for="reservation-start-date">시작 날짜</label>
            <input type="date" id="reservation-start-date" v-model="reservation.startDate" @change="printReservationList" />

            <span>-</span> <!-- 구분선 -->

            <label for="reservation-end-date">종료 날짜</label>
            <input type="date" id="reservation-end-date" v-model="reservation.endDate" @change="printReservationList" />
        </div>
        
        <div class="time-container">
            <label for="reservation-start-time">시작 시간</label>
            <input type="time" id="reservation-start-time" v-model="reservation.startTime" :step="300" @blur="validateTime('startTime')" />

            <span>-</span> <!-- 구분선 -->

            <label for="reservation-end-time">종료 시간</label>
            <input type="time" id="reservation-end-time" v-model="reservation.endTime" :step="300" @blur="validateTime('endTime')" />
        </div>
        <h5 class="minuteAlarm">시간은 5분 단위로 예약이 가능합니다.</h5>
    </div>
  
    <!-- 전달받은 충전기 정보 출력 -->
    <div class="reservation-info-title">충전기 정보</div>
    <div class="reservation-info">
      <p><strong>충전소 이름:</strong> {{ stationName }}</p>
      <p><strong>충전소 주소:</strong> {{ stationAddress }}</p>
      <p><strong>충전기 ID:</strong> {{ chargerId }}</p>
      <p><strong>충전기 타입:</strong> {{ chargerType }}</p>
      <p><strong>충전 속도:</strong> {{ chargerSpeed }}</p>
    </div>
  
    <!-- 예약 목록 -->
    <div class="reservation-list-title">예약 목록</div>
    <div class="reservation-list">
            <div v-for="(reservation, index) in reservationList" :key="index">
                <p>예약 번호: {{ reservation.reservationId }}</p>
                <p>이용 시간: {{ formatDate(reservation.startTime) }} ~ {{ formatDate(reservation.endTime) }}</p>
                <br>
            </div>
    </div>
    <div class="reservation-button-container">
        <button class="reservation-button" @click="reserve">예약</button>
    </div>
</template>

<script setup>
import { ref, computed } from "vue";
import axios from "axios";

function formatDate(timestamp) {
  const date = new Date(timestamp);
  const year = date.getFullYear();
  const month = date.getMonth() + 1; // 월은 0부터 시작하므로 +1
  const day = date.getDate();
  const hours = date.getHours();
  const minutes = date.getMinutes();

  return `${year}년 ${month}월 ${day}일 ${hours}시 ${minutes}분`;
}

// 예약 정보
const reservation = ref({
    startDate: "",
    endDate: "",
    startTime: "",
    endTime: "",
    impUid: "", // 결제 완료 후 설정됨
    status: "",
});

const reservationList = ref([]);

// 시작 날짜와 시간, 종료 날짜와 시간을 계산하여 날짜 객체로 반환
const reservationStartDate = computed(() => {
    if (!reservation.value.startDate || !reservation.value.startTime) return null;
    return new Date(`${reservation.value.startDate}T${reservation.value.startTime}`);
  });
  
  const reservationEndDate = computed(() => {
    if (!reservation.value.endDate || !reservation.value.endTime) return null;
    return new Date(`${reservation.value.endDate}T${reservation.value.endTime}`);
  });
  
  // 시간 유효성 검사
  const validateTime = (field) => {
    const time = reservation.value[field];
    const [hours, minutes] = time.split(":").map(Number);
    if (minutes % 5 !== 0) {
      alert(`${field === "startTime" ? "시작" : "종료"} 시간은 5분 단위로 입력해야 합니다.`);
      reservation.value[field] = "";
    }
  };
  
  // 날짜 및 시간 유효성 검사
  const isValidDateAndTime = () => {
    if (!reservationStartDate.value || !reservationEndDate.value) {
      alert("모든 필드를 입력해주세요.");
      return false;
    }
    const now = new Date();
    if (reservationStartDate.value <= now) {
      alert("예약은 현재 시간 이후로만 가능합니다.");
      return false;
    }
    if (reservationStartDate.value >= reservationEndDate.value) {
      alert("종료 시간은 시작 시간보다 늦어야 합니다.");
      return false;
    }
    return true;
  };
  
  // 예약 API 호출
  const reserve = async () => {
    if (!isValidDateAndTime()) return;
  
    const { IMP } = window;
    IMP.init("imp50578251");
    IMP.request_pay(

        {
            pg: "html5_inicis",
            pay_method: "카카오페이",
            merchant_uid: `order_${Date.now()}`,
            name: "보증금 결제",
            amount: 100,
        },
        async (rsp) => {
            if (rsp.success) {
                reservation.value.impUid = rsp.imp_uid;

                try {
                    const response = await axios.post(`http://localhost:8081/api/v1/reservation/${reservation.value.impUid}`, {
                        startTime: reservationStartDate.value.toISOString(),
                        endTime: reservationEndDate.value.toISOString(),
                        userId: 1,
                        stationId: 2,
                        chargerId: 2,
                        status: "confirmed",
                    });
                    alert(response.data.message);
                    window.location.href = "/";
                } catch (error) {
                    alert(error.response?.data?.message || "예약 처리 중 오류가 발생했습니다.");
                }
            } else {
                alert(`결제 실패: ${rsp.error_msg}`);
            }
        }
      }
    );
  };
  
  // 예약 목록 조회
  const printReservationList = async () => {
    if (!reservation.value.startDate || !reservation.value.endDate) return;
    try {
      const formattedStartDate = reservation.value.startDate + "T00:00:00";
      const formattedEndDate = reservation.value.endDate + "T23:59:59";
  
      const response = await axios.get(
        `http://localhost:8081/reservationList/${formattedStartDate}/${formattedEndDate}`
      );
  
      reservationList.value = response.data;
    } catch (error) {
      alert("해당 날짜의 예약 조회에 실패했습니다.");
    }
  };
  </script>
<style>
/* .reserve-date-container {
    display: flex;
    flex-direction: column;
    gap: 20px;
    width: 100%;
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
    box-sizing: border-box;
    text-align: center;
} */

.reservation-go-info-title {
  font-size: 30px; /* 원하는 폰트 크기 */
  font-weight: bold; /* 폰트 두께 */
  color: #333; /* 텍스트 색상 */
  margin: 20px auto 5px;
  white-space: nowrap;
}

/* 날짜 입력 필드 */
.date-container {
    display: flex;
    align-items: center;
    gap: 10px;
    justify-content: center; /* 가운데 정렬 */
    background-color: #f9f9f9; /* 밝은 회색 배경 */
    border: 1px solid #ddd; /* 얇은 테두리 */
    border-radius: 10px; /* 둥근 모서리 */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 부드러운 그림자 효과 */
    padding: 15px; /* 내부 여백 */
    margin: 20px auto; /* 위아래 여백 */
    width: 600px; /* 너비 80% */
    box-sizing: border-box;
}

.time-container {
    display: flex;
    align-items: center;
    gap: 10px;
    justify-content: center; /* 가운데 정렬 */
    background-color: #f9f9f9; /* 밝은 회색 배경 */
    border: 1px solid #ddd; /* 얇은 테두리 */
    border-radius: 10px; /* 둥근 모서리 */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 부드러운 그림자 효과 */
    padding: 15px; /* 내부 여백 */
    margin: 20px auto; /* 위아래 여백 */
    width: 600px;
    box-sizing: border-box;
}

/* 구분선 */
.date-container span, .time-container span {
    margin: 0 10px;
    font-size: 18px;
}

/* 레이블 스타일 */
.date-container label, .time-container label {
    margin-right: 10px;
    font-weight: bold;
}

/* 날짜 및 시간 입력 필드 스타일 */
.date-container input, .time-container input {
    width: 150px;
    padding: 5px;
    font-size: 16px;
    border-radius: 5px;
    border: 1px solid #ccc;
    box-sizing: border-box;
}

/* 포커스 시 스타일 */
.date-container input:focus, .time-container input:focus {
    border-color: #007BFF;
    outline: none;
}

.reservation-address, .reservation-username {
  background-color: #ffffff; /* 컨테이너 배경색 */
  border-radius: 10px; /* 모서리를 둥글게 */
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); /* 부드러운 그림자 효과 */
  padding: 20px; /* 내부 여백 */
  margin: 15px auto; /* 세로 간격 + 중앙 정렬 */
  border: 1px solid #ccc; /* 얇은 테두리 */
  width: 600px; /* 넓이를 화면의 80%로 설정 */
}

.reservation-chargerType, .reservation-depotsit {
  background-color: #ffffff; /* 컨테이너 배경색 */
  border-radius: 10px; /* 모서리를 둥글게 */
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); /* 부드러운 그림자 효과 */
  padding: 20px; /* 내부 여백 */
  margin: 15px auto; /* 세로 간격 + 중앙 정렬 */
  border: 1px solid #ccc; /* 얇은 테두리 */
  width: 600px; /* 넓이를 화면의 80%로 설정 */
}

.reservation-list {
  background-color: #ffffff; /* 컨테이너 배경색 */
  border-radius: 10px; /* 모서리를 둥글게 */
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); /* 부드러운 그림자 효과 */
  padding: 20px; /* 내부 여백 */
  margin: 15px auto; /* 세로 간격 + 중앙 정렬 */
  border: 1px solid #ccc; /* 얇은 테두리 */
  width: 600px; /* 넓이를 화면의 80%로 설정 */
}

.reservation-info-title, .reservation-list-title, .reservation-date-time-title,
.reserve-date-container-title, .reservation-address-title, .reservation-chargerType-title,
.reservation-deposit-title, .reservation-username-title{
  font-size: 24px; /* 원하는 폰트 크기 */
  font-weight: bold; /* 폰트 두께 */
  color: #333; /* 텍스트 색상 */
  padding-left: 190px;
  margin-bottom: 5px; /* 아래쪽 마진 추가 */
  margin-top: 20px;
  margin-left: calc(15vw + 0px);
  white-space: nowrap;
}

.reservation-button {
  background-color: black; /* 배경색 검은색 */
  color: white; /* 텍스트 색상 흰색 */
  border: none; /* 기본 테두리 제거 */
  padding: 10px 20px; /* 여백 설정 (상하, 좌우) */
  font-size: 16px; /* 폰트 크기 */
  font-weight: bold; /* 텍스트 굵게 */
  border-radius: 10px; /* 모서리 둥글게 */
  cursor: pointer; /* 마우스를 올렸을 때 포인터 모양 */
  transition: background-color 0.3s ease; /* 배경색 변화 애니메이션 */
  width: 600px;
  margin: 0 auto; /* 가로축 가운데 정렬 */
  display: block; /* block 속성으로 설정 */
  text-align: center;
}

/* 버튼에 마우스를 올렸을 때 색상 변경 */
.reservation-button:hover, .reservation-list-button:hover {
  background-color: #333; /* 어두운 회색 */
}

.minuteAlarm {
    color: rgb(238, 110, 110);
    margin-left: calc(28vw + 0px);
    margin-bottom: 30px;
    white-space: nowrap;   
}
</style>


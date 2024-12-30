<template>
    <div class="reservation-wrap">
        <div class="reservation-header">
            <div class="reservation-logo">
                <router-link to="/main">
                    <img src="images/logo.png" alt="백만볼트 로고">
                </router-link>
            </div>
            <div class="reservation-container">
                <div class="reservation-go-info-title">예약 진행 및 예약 상세 정보</div>
                <div class="reservation-username-title">예약자</div>
                <div class="reservation-content">
                    <string>{{ username }}</string>
                </div>
                <div class="reservation-address-title">상호명</div>
                <div class="reservation-content">
                    <string>{{ stationName }}</string>
                </div>


                <div class="reservation-address-title">주소</div>
                <div class="reservation-content">
                    <string>{{ stationAddress }}</string>
                </div>

                <div class="reservation-chargerType-title">충전기 타입</div>
                <div class="reservation-content">
                    <string>{{ chargerType }}</string>
                    <br>
                    <string>{{ chargerSpeed }}</string>
                </div>

                <div class="reservation-deposit-title">보증금</div>
                <div class="reservation-content">
                    <string>{{ depositAmount }}원</string>
                </div>

                <div class="reserve-date-container-title">예약 날짜 및 시간</div>
                <div class="reserve-date-container">
                    <div class="date-container">

                        <label for="reservation-start-date">시작 날짜</label>
                        <DatePicker id="reservation-start-date" 
                            v-model="reservation.startDate"
                            @closed="printReservationList" 
                            locale="ko" 
                            :start-time="startTime" 
                            :format="formatDate"
                            :min-date="new Date()" 
                            :minutes-grid-increment="5" 
                            minutes-increment="5" 
                            teleport-center
                            time-picker-inline />

                    </div>

                    <div class="time-container">
                        <label for="reservation-end-date">종료 날짜</label>
                        <DatePicker id="reservation-end-date" v-model="reservation.endDate"
                            @closed="printReservationList" locale="ko" :format="formatDate" :start-time="startMinTime"
                            :min-date="minDate" :max-date="maxDate" :minutes-grid-increment="5" :minutes-increment="5"
                            teleport-center time-picker-inline :disabled="!reservation.startDate" />

                    </div>
                    <h5><span class="minuteAlarm">시작 날짜를 선택한 후 종료 날짜를 선택</span>할 수 있습니다.</h5>
                    <h5>예약은 <span class="minuteAlarm">최대 2일까지 가능</span>합니다.</h5>
                    <h5>예약은 <span class="minuteAlarm">5분 단위로 가능</span>합니다.</h5>
                    <h5><span class="minuteAlarm">다른 예약이 있을 경우, 15분 후부터</span> 예약이 가능합니다.</h5>
                    </div>
                <!-- 예약 목록 -->
                <div class="reservation-list-title">예약 목록</div>
                <div class="reservation-list">
                    <div v-if="reservationList.length === 0">
                        <p>해당 날짜에 예약된 항목이 없습니다.</p>
                    </div>
                    <div v-else v-for="(reservation, index) in reservationList" :key="index">
                        <p>예약 번호: {{ reservation.reservationId }}</p>
                        <p>이용 시간: {{ formatDate(reservation.startTime) }} ~ {{ formatDate(reservation.endTime) }}</p>
                    </div>
                </div>
                <div class="reservation-button-container">
                    <button class="reservation-button" @click="reserve">예약</button>
                </div>
            </div>
        </div>
    </div>

</template>

<script setup>
import { onMounted } from "vue";
import { ref, computed } from "vue";
import axios from "axios";
import { useRoute } from "vue-router";
const route = useRoute();

import { addDays } from 'date-fns';

// 예약 최소 시간과 최대 시간 설정
const minDate = computed(() => reservation.value.startDate || new Date());
const maxDate = computed(() => addDays(minDate.value, 2));
const date = ref(new Date());
const startTime = ref({ hours: date.value.getHours(), minutes: Math.ceil(date.value.getMinutes() / 5) * 5 });
const startMinTime = computed(() => ({
    hours: minDate.value.getHours(),
    minutes: minDate.value.getMinutes()
}));

const chargerId = ref(null);
const chargerType = ref(null);
const chargerSpeed = ref(null);
const stationName = ref(null);
const stationAddress = ref(null);
const stationId = ref(null);
const username = ref('');
const depositAmount = ref(100);



const token = localStorage.getItem('user');
const parsedToken = JSON.parse(token);
const userId = parsedToken.id;

// 라우터를 통해서 예약정보 값 받기기
onMounted(() => {
    chargerId.value = route.query.chargerId; //충전기 아이디
    chargerType.value = route.query.chargerType; //충전기 타입
    chargerSpeed.value = route.query.chargerSpeed; //충전 속도
    stationId.value = route.query.stationId; //충전소 아이디 
    stationName.value = route.query.stationName; //충전소 상호명
    stationAddress.value = route.query.stationAddress; //충전소 주소
    printReserverName(); //예약자 이름
})

//날짜 시간 형식 변경
const formatDate = (date) => {
    const parsedDate = new Date(date);  // ISO 8601 형식 문자열 또는 Date 객체를 Date 객체로 변환
    const day = parsedDate.getDate();
    const month = parsedDate.getMonth() + 1; // 0부터 시작하므로 1을 더해야 함
    const year = parsedDate.getFullYear();
    const hours = parsedDate.getHours();
    const minutes = parsedDate.getMinutes();

    return `${year}년 ${month}월 ${day}일 ${hours}시 ${minutes}분`;
};


// 예약 정보
const reservation = ref({
    startDate: "",
    endDate: "",
    startTime: "",
    endTime: "",
    impUid: "",
    status: "",
});

// 해당 날짜의 예약 시간 목록
const reservationList = ref([]);

// 시작 날짜와 시간, 종료 날짜와 시간을 계산하여 날짜 객체로 반환
const reservationStartDate = computed(() => {
    if (!reservation.value.startDate) return null;
    return new Date(`${reservation.value.startDate}`);
});

const reservationEndDate = computed(() => {
    if (!reservation.value.endDate) return null;
    return new Date(`${reservation.value.endDate}`);
});


// 예약 리스트와 입력된 예약 시간이 겹치는지 확인하는 함수
const isOverlap = () => {
    // 예약 시작 및 종료 시간
    const startDate = reservationStartDate.value;
    const endDate = reservationEndDate.value;

    // 예약 리스트 내의 각 예약과 비교
    for (const reservationItem of reservationList.value) {
        const itemStartTime = new Date(reservationItem.startTime);  // 예약 시작 시간
        const itemEndTime = new Date(reservationItem.endTime);  // 예약 종료 시간

        // 예약 시간이 겹치는지 확인
        if (
            (startDate < itemEndTime && endDate > itemStartTime) // 시작일과 종료일이 겹치는 경우
        ) {
            return true;  // 겹치면 true 반환
        }
    }
    return false;  // 겹치지 않으면 false 반환
};

// 다른 예약이 있는 경우, 예약 가능한지 확인하는 함수
const isAvailableTime = () => {
    const startDate = reservationStartDate.value;  // 사용자가 입력한 예약 시작 시간

    // 예약 리스트 내의 각 예약과 비교
    for (const reservationItem of reservationList.value) {
        const itemEndTime = new Date(reservationItem.endTime);  // 기존 예약의 종료 시간

        // 종료 시간 + 15분 후부터 예약 가능
        const availableTime = new Date(itemEndTime.getTime() + 15 * 60 * 1000);  // 15분 후

        // 예약 시작 시간이 15분 이후여야 예약이 가능
        if (startDate < availableTime) {
            return false;  // 예약 불가능
        }
    }
    return true;  // 예약 가능
};

// 예약 API 호출
const reserve = async () => {
    // 예약 시간 겹침 체크
    if (isOverlap()) {
        alert("다른 날짜를 예약해주세요.");
        return;  // 겹치면 예약 진행하지 않음
    }

    // 예약 가능한 시간 확인
    if (!isAvailableTime()) {
        alert("다른 예약이 있을 경우, 해당 예약이 끝난 시간 15분 후부터 예약이 가능합니다.");
        return;
    }

    const { IMP } = window;
    IMP.init("imp50578251");
    IMP.request_pay(
        {
            pg: "html5_inicis",
            pay_method: "카카오페이",
            merchant_uid: `order_${Date.now()}`,
            name: "보증금 결제",
            amount: depositAmount.value,
        },
        async (rsp) => {
            if (rsp.success) {
                reservation.value.impUid = rsp.imp_uid;

                try {
                    const response = await axios.post(`http://localhost:8081/api/v1/reservation/${reservation.value.impUid}`, {
                        startTime: reservationStartDate.value.toISOString(),
                        endTime: reservationEndDate.value.toISOString(),
                        userId: userId,
                        stationId: stationId.value,
                        chargerId: chargerId.value,
                        status: 'confirmed',
                    });
                    alert(response.data.message);
                    window.location.href = "/";
                } catch (error) {
                    // alert(error.response?.data?.message || "예약 처리 중 오류가 발생했습니다.");
                    if (error.response?.status === 400) {
                        // 예약 충돌 관련 오류 처리
                        alert(error.response.data.message);
                    } else if (error.response?.status === 500) {
                        // 서버 내부 오류 처리
                        alert(error.response.data.message);

                    }
                }
            } else {
                alert(`결제 실패: ${rsp.error_msg}`);
            }
        }
    )
};


// 예약 목록 조회
const printReservationList = async () => {
    if (!reservation.value.startDate || !reservation.value.endDate) return;
    try {
        const formattedStartDate = reservation.value.startDate.toISOString().split(".")[0];;
        const formattedEndDate = reservation.value.endDate.toISOString().split(".")[0];;

        const response = await axios.get(
            `http://localhost:8081/reservationList/${formattedStartDate}/${formattedEndDate}/${stationId.value}/${chargerId.value}`
        );
        reservationList.value = response.data;

    } catch (error) {
        alert("해당 날짜의 예약 조회에 실패했습니다.");
    }
};

//예약자 이름 조회
const printReserverName = async () => {
    const usernameResponse = await axios.get(
        `http://localhost:8081/api/v1/reservation/${userId}`
    );
    username.value = usernameResponse.data.username;
    console.log(username.value);
};
</script>
<style>
h5{
    font-size: 14px;
}

.reservation-wrap {
    font-family: Arial, sans-serif;
    display: flex;
    justify-content: center;
    margin: 0;
    background-color: white;
}

.reservation-container {
    width: 650px;
    background-color: #fff;
    border: 1px solid #969696;
    padding: 30px 24px 28px 24px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    border-radius: 10px;
    text-align: center;
    align-content: center;
}

.reservation-header {
    text-align: center;
    margin-bottom: 10%;
}

.reservation-logo img {
    width: 150px;
}

.reservation-go-info-title {
    font-size: 28px;
    font-weight: bold;
    color: #52616A;
    margin: 10px 0px 48px 0px;
    white-space: nowrap;
}

/* 날짜 입력 필드 */
.date-container {
    display: flex;
    align-items: center;
    gap: 10px;
    justify-content: center;
    background-color: #fff;
    border: 1px solid #C9D6DE;
    border-radius: 10px;
    padding: 15px;
    margin: 20px auto;
    box-sizing: border-box;
}

.time-container {
    display: flex;
    align-items: center;
    gap: 10px;
    justify-content: center;
    background-color: #fff;
    border: 1px solid #C9D6DE;
    border-radius: 10px;
    padding: 15px;
    margin: 20px auto;
    box-sizing: border-box;
}

/* 구분선 */
.date-container span,
.time-container span {
    margin: 0 10px;
    font-size: 18px;
}

/* 레이블 스타일 */
.date-container label,
.time-container label {
    margin-right: 10px;
    width: 20%;
    font-weight: bold;
    color: #52616A;
}

/* 날짜 및 시간 입력 필드 스타일 */
.date-container input,
.time-container input {
    width: 80%;
    padding: 10px;
    font-size: 14px;
    border-radius: 5px;
    box-sizing: border-box;
    height: 40px;
    background-color: #fff;
    color: #52616A;
}

/* 포커스 시 스타일 */
.date-container input:focus,
.time-container input:focus {
    border-color: #c9d6de;
    outline: none;
}

.reservation-content {
    border: 1px solid #C9D6DE;
    padding: 15px;
    border-radius: 5px;
    display: flex;
    height: 60px;

}

.reservation-address,
.reservation-username {
    background-color: #ffffff;
    /* 컨테이너 배경색 */
    border-radius: 10px;
    /* 모서리를 둥글게 */
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
    /* 부드러운 그림자 효과 */
    padding: 20px;
    /* 내부 여백 */
    /* 세로 간격 + 중앙 정렬 */
    border: 1px solid #ccc;
    /* 얇은 테두리 */
    /* width: 600px; 넓이를 화면의 80%로 설정 */
}

.reservation-chargerType,
.reservation-depotsit {
    background-color: #ffffff;
    /* 컨테이너 배경색 */
    border-radius: 10px;
    /* 모서리를 둥글게 */
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
    /* 부드러운 그림자 효과 */
    padding: 20px;
    /* 내부 여백 */
    margin: 15px auto;
    /* 세로 간격 + 중앙 정렬 */
    border: 1px solid #ccc;
    /* 얇은 테두리 */
    /* width: 600px;  */
}

.reservation-list {
    background-color: #ffffff;
    border-radius: 10px;
    padding: 20px;
    margin: 15px 0px 22px 0px;
    border: 1px solid #C9D6DE;
    overflow-y: auto;
    color: #52616A;

}

p {
    margin: 0px;
    padding: 0px;
}

string {
    margin-top: 2px;
    color: #52616A;
}

.reservation-info-title,
.reservation-list-title,
.reservation-date-time-title,
.reserve-date-container-title,
.reservation-address-title,
.reservation-chargerType-title,
.reservation-deposit-title,
.reservation-username-title {
    font-size: 17px;
    font-weight: bold;
    color: #1E2022;
    /* padding-left: 190px; */
    margin-bottom: 5px;
    margin-top: 20px;
    /* margin-left: calc(15vw + 0px); */
    white-space: nowrap;
    /* float: left; */
    display: flex;
}

.reservation-list-title{
    margin-top:30px;
}

.reservation-button {
    background-color: #52616A;
    width: 100%;
    height: 68px;
    color: white;
    border: none;
    padding: 10px 20px;
    /* 여백 설정 (상하, 좌우) */
    font-size: 17px;
    /* 폰트 크기 */
    font-weight: bold;
    /* 텍스트 굵게 */
    border-radius: 10px;
    /* 모서리 둥글게 */
    cursor: pointer;
    /* 마우스를 올렸을 때 포인터 모양 */
    transition: background-color 0.3s ease;
    /* 배경색 변화 애니메이션 */
    /* width: 600px; */
    margin: 0 auto;
    /* 가로축 가운데 정렬 */
    display: block;
    /* block 속성으로 설정 */
    text-align: center;
}


.reservation-button:hover,
.reservation-list-button:hover {
    background-color: #1E2022;
}

.minuteAlarm {
    color: rgb(238, 110, 110);
    /* margin-left: calc(28vw + 0px); */
    margin-bottom: 5px;
    white-space: nowrap;
    font-size: 14px;
    text-align: center;
}
</style>

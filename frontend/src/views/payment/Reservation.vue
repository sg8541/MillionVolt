<!-- <template>
    <div class="reservation-payment">
    <div class="reservation-form">
        <label for="reservation-date">이용 날짜:</label>
        <input type="date" id="reservation-date" v-model="reservation.date" />

        <label for="start-time">시작 시간:</label>
        <input type="time" id="start-time" v-model="reservation.startTime" step="300" />

        <label for="end-time">종료 시간:</label>
        <input type="time" id="end-time" v-model="reservation.endTime" step="300" />

        <button @click="reserve">결제 및 예약</button>
    </div>
    </div>
</template>

<script setup>
import { ref } from "vue";
import axios from "axios";

  // 예약 정보
const reservation = ref({
    date: "",
    startTime: "",
    endTime: "",
    impUid: "", // 결제 완료 후 설정됨
});

  // 날짜와 시간 비교
const isValidDateAndTime = () => {
    if (!reservation.value.date || !reservation.value.startTime || !reservation.value.endTime) {
    alert("모든 필드를 입력해주세요.");
    return false;
    }

    const now = new Date();
    const reservationDate = new Date(`${reservation.value.date}T${reservation.value.startTime}`);
    const reservationEndDate = new Date(`${reservation.value.date}T${reservation.value.endTime}`);
    
    if (reservationDate <= now) {
    alert("예약은 현재 시간 이후로만 가능합니다.");
    return false;
    }
    
    if (reservationDate >= reservationEndDate) {
    alert("종료 시간은 시작 시간보다 늦어야 합니다.");
    return false;
    }

    return true;
};


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
            const response = await axios.post(`/api/v1/reservation/${reservation.value.impUid}`, {
            date: reservation.value.date,
            startTime: reservation.value.startTime,
            endTime: reservation.value.endTime,
            userId: 2,
            reservationId: 8,
            stationId: 2,
            chargerId: 2
            });
            alert(response.data.message);
        } catch (error) {
            alert(error.response?.data?.message || "예약 처리 중 오류가 발생했습니다.");
        }
        } else {
        alert(`결제 실패: ${rsp.error_msg}`);
        }
    }
    );
};
</script>

<style>
  /* 스타일 */
.reservation-payment {
    max-width: 400px;
    margin: 0 auto;
}

button {
    margin-top: 10px;
    padding: 10px;
    background-color: #007bff;
    color: white;
    border: none;
    cursor: pointer;
    border-radius: 5px;
}

button:hover {
    background-color: #0056b3;
}
</style> -->


<!-- <template>
    <div class="reservation-payment">
    <div class="reservation-form">
        <label for="reservation-date">이용 날짜:</label>
        <input type="date" id="reservation-date" v-model="reservation.date" />

        <label for="start-time">시작 시간:</label>
        <input type="time" id="start-time" v-model="reservation.startTime" step="300" />

        <label for="end-time">종료 시간:</label>
        <input type="time" id="end-time" v-model="reservation.endTime" step="300" />

        <button @click="reserve">결제 및 예약</button>
    </div>
    </div>
</template>

<script setup>
import { ref, computed } from "vue";
import axios from "axios";

  // 예약 정보
const reservation = ref({
    date: "",
    startTime: "",
    endTime: "",
    impUid: "", // 결제 완료 후 설정됨
});

  // 시작 시간과 종료 시간을 컴퓨티드로 처리
const reservationDate = computed(() => {
    if (!reservation.value.date || !reservation.value.startTime) return null;
    return new Date(`${reservation.value.date}T${reservation.value.startTime}`);
});

const reservationEndDate = computed(() => {
    if (!reservation.value.date || !reservation.value.endTime) return null;
    return new Date(`${reservation.value.date}T${reservation.value.endTime}`);
});

  // 유효성 검사
const isValidDateAndTime = () => {
    if (!reservationDate.value || !reservationEndDate.value) {
    alert("모든 필드를 입력해주세요.");
    return false;
    }

    const now = new Date();

    if (reservationDate.value <= now) {
    alert("예약은 현재 시간 이후로만 가능합니다.");
    return false;
    }

    if (reservationDate.value >= reservationEndDate.value) {
    alert("종료 시간은 시작 시간보다 늦어야 합니다.");
    return false;
    }

    return true;
};

  // 예약 및 결제 처리
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
            startTime: reservationDate.value.toISOString(),
            endTime: reservationEndDate.value.toISOString(),
            userId: 2,
            reservationId: 9,
            stationId: 2,
            chargerId: 2,
            });
            alert(response.data.message);
        } catch (error) {
            alert(error.response?.data?.message || "예약 처리 중 오류가 발생했습니다.");
        }
        } else {
        alert(`결제 실패: ${rsp.error_msg}`);
        }
    }
    );
};
</script>

<style>
  /* 스타일 */
.reservation-payment {
    max-width: 400px;
    margin: 0 auto;
}

button {
    margin-top: 10px;
    padding: 10px;
    background-color: #007bff;
    color: white;
    border: none;
    cursor: pointer;
    border-radius: 5px;
}

button:hover {
    background-color: #0056b3;
}
</style> -->

<template>
    <div class="reservation-payment">
    <div class="reservation-form">
        <label for="reservation-start-date">시작 날짜:</label>
        <input type="date" id="reservation-start-date" v-model="reservation.startDate" />

        <label for="reservation-end-date">종료 날짜:</label>
        <input type="date" id="reservation-end-date" v-model="reservation.endDate" />

        <button @click="printReservationList">예약 조회</button>

        <label for="start-time">시작 시간:</label>
        <input type="time" id="start-time" v-model="reservation.startTime" step="300" />

        <label for="end-time">종료 시간:</label>
        <input type="time" id="end-time" v-model="reservation.endTime" step="300" />

        <button @click="reserve">결제 및 예약</button>
    </div>
    </div>
    <div class="reservation-list">
        <h3>예약 리스트</h3>
            <ul>
                <li v-for="(reservation, index) in reservationList" :key="index">
                    <p>예약 ID: {{ reservation.reservationId }}</p>
                    <p>이용 시간: {{ reservation.startTime }} ~ {{ reservation.endTime }}</p>
                    <p>상태: {{ reservation.status }}</p>
                </li>
            </ul>
    </div>
</template>

<script setup>
import { ref, computed } from "vue";
import axios from "axios";

  // 예약 정보
const reservation = ref({
    date: "",
    startTime: "",
    endTime: "",
    impUid: "", // 결제 완료 후 설정됨
});

const reservationList = ref([]);

  // 시작 시간과 종료 시간을 컴퓨티드로 처리
const reservationStartDate = computed(() => {
    if (!reservation.value.startDate || !reservation.value.startTime) return null;
    return new Date(`${reservation.value.startDate}T${reservation.value.startTime}`);
});

const reservationEndDate = computed(() => {
    if (!reservation.value.endDate || !reservation.value.endTime) return null;
    return new Date(`${reservation.value.endDate}T${reservation.value.endTime}`);
});

  // 유효성 검사
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

  // 예약 및 결제 처리
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
            reservationId: 9,
            stationId: 2,
            chargerId: 2,
            });
            alert(response.data.message);
        } catch (error) {
            alert(error.response?.data?.message || "예약 처리 중 오류가 발생했습니다.");
        }
        } else {
        alert(`결제 실패: ${rsp.error_msg}`);
        }
    }
    );
};

// const printReservationList = async () => {
// try {
//     const response = await axios.get(
//     `http://localhost:8081/reservationList/${reservationStartDate.value.toISOString()}/${reservationEndDate.value.toISOString()}`
//     );
//     reservationList.value = response.data;
//     console.log("예약 리스트:", reservationList.value);
// } catch (error) {
//     alert("해당 날짜의 예약 조회에 실패했습니다.");
//     console.error(error.response?.data || error.message);
// }
// };
const printReservationList = async () => {
    try {
        const formattedStartDate = reservationStartDate.value.toISOString().split('.')[0]; 
        const formattedEndDate = reservationEndDate.value.toISOString().split('.')[0];

        const response = await axios.get(
            `http://localhost:8081/reservationList/${formattedStartDate}/${formattedEndDate}`
        );

        reservationList.value = response.data;
        console.log("예약 리스트:", reservationList.value);
    } catch (error) {
        alert("해당 날짜의 예약 조회에 실패했습니다.");
        console.error(error.response?.data || error.message);
    }
};
</script>

<style>
  /* 스타일 */
.reservation-payment {
    max-width: 400px;
    margin: 0 auto;
}

button {
    margin-top: 10px;
    padding: 10px;
    background-color: #007bff;
    color: white;
    border: none;
    cursor: pointer;
    border-radius: 5px;
}

button:hover {
    background-color: #0056b3;
}
</style>

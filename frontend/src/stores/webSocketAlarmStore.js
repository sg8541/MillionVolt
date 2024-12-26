import { ref } from 'vue';
import { defineStore } from 'pinia';

export const useAlarmWebSocketStore = defineStore('alarmWebsocket', () => {
    const socketAlarm = ref(null);

    const alarm = ref({
        message: '',
        reservationId: '',
        startTime: '',
        stationId:'',
        closeReservationTime:'',
        penaltyAmount:''
    });

    const isConnected = ref(false);

    const connectAlarmWebSocket = () => {

        if(isConnected.value) {
            console.log("이미연결된 웹소켓.");
            return;
        }
    
        socketAlarm.value = new WebSocket('ws://localhost:8081/alarm');
        console.log('웹소켓 연결.');

        // 웹소켓 연결 성공 시
        socketAlarm.value.onopen = () => {
            const token = localStorage.getItem('user');
            const parsedToken = JSON.parse(token);
            const userId = parsedToken.userId;
            socketAlarm.value.send(userId);
            isConnected.value = true;
        };

        // 메시지 수신 처리
        socketAlarm.value.onmessage = (event) => {
        const data = JSON.parse(event.data);
        alarm.value.message = data.message;
        alarm.value.reservationId = data.reservationId;
        alarm.value.startTime = data.startTime;
        alarm.value.stationId= data.stationId;
        alarm.value.closeReservationTime = data.closeReservationTime;
        alarm.value.penaltyAmount = data.penaltyAmount;
        };

        // 에러 처리
        socketAlarm.value.onerror = (error) => {
        console.error('WebSocket 에러', error);
        };
    };

    const clearAlarmMessage = () => {
        alarm.value.message = '';
    };

    return {
        connectAlarmWebSocket,
        alarm,
        clearAlarmMessage,
    };
});
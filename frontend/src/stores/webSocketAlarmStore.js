import { ref } from 'vue';
import { defineStore } from 'pinia';

export const useAlarmWebSocketStore = defineStore('alarmWebsocket', () => {
    const socketAlarm = ref(null);

    const alarm = ref({
        message: '',
        reservationId: '',
        startTime: '',
    });

    const connectAlarmWebSocket = () => {
        socketAlarm.value = new WebSocket('ws://localhost:8081/alarm');
        console.log('웹소켓 연결.');

        console.log(alarm.value.message);

        // 웹소켓 연결 성공 시
        socketAlarm.value.onopen = () => {
        const userId = 'wogjsdl1244'; // 실제 사용자는 토큰을 디코딩해서 전달
        socketAlarm.value.send(userId);
        };

        // 메시지 수신 처리
        socketAlarm.value.onmessage = (event) => {
        const data = JSON.parse(event.data);
        alarm.value.message = data.message;
        alarm.value.reservationId = data.reservationId;
        alarm.value.startTime = data.startTime;
        };

        // 에러 처리
        socketAlarm.value.onerror = (error) => {
        console.error('WebSocket 에러', error);
        };
    };

    return {
        connectAlarmWebSocket,
        alarm,
    };
});
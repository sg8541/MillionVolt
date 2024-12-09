import { ref } from 'vue';
import { defineStore } from 'pinia';

export const useAlarmWebSocketStore = defineStore('alarmWebsocket',()=> {
    const socketAlarm = ref(null);
    const alram = ref({
        messgae: '',
    });

    const connectAlarmmWebSocket = () => {
        socketAlarm.value = new WebSocket('ws://localhost:8081/alram');

        socketAlarm.value.onopen = () => {
            let userId= 'wogjsdl1244' // 토큰 가져와서 디코딩 해서 보여줄 예정. 
            socketAlarm.value.send(userId);   
        }
        socketAlarm.value.onmessage() = (event) => {
            const data = JSON.parse(event.data);
            alram.value.messgae =  data.value;
        }

        socketInstance.value.onerror = (error) => {
            console.error('WebSocket 에러', error);
        };

        return{
            connectAlarmmWebSocket,
        }

    }
})
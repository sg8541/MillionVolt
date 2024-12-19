import { ref } from 'vue';
import { defineStore } from 'pinia';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

export const chargerStateStoreStomp = defineStore('chargerStomp', () => {
    const chargerState = ref([]);
let StompClient = null;

const connectChargerStomp = (stationId) => {
    const socket = new SockJS('http://localhost:8081/stomp-chargerstate');
    StompClient = Stomp.over(socket);

    StompClient.connect({}, () => {
        StompClient.subscribe('/topic/chargerstate', (message) => {
            const parsedData = JSON.parse(message.body);
            chargerState.value = parsedData; // JSON parsing 후 값 설정
        
        });
        StompClient.send('/app/charger', {}, JSON.stringify({ stationId }));
    });
};

const disconnectChargerStomp = () => {
    if (StompClient && StompClient.connected) {
        StompClient.disconnect(() => {
            console.log('STOMP connection closed');
        });
        StompClient = null;
    } else {
        console.warn('No active STOMP connection to disconnect');
    }
};

return {
        connectChargerStomp,
        chargerState,
        StompClient,
        disconnectChargerStomp
    };
});
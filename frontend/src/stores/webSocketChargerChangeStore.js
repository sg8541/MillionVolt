import { reactive, ref } from 'vue';
import { defineStore } from 'pinia';

export const chargerStateChangeWebSocketStore = defineStore('chargeWebsocket',() => {
    const socketCharger = ref(null);
    const chargeState = ref([]);
    console.log("1 : "+chargeState.value);

    const connectCharger = (stationId) => {
        
        socketCharger.value = new WebSocket('ws://localhost:8081/chargerstatus');
        console.log('충전 웹소켓 연결');

        
        socketCharger.value.onopen = () => {
            socketCharger.value.send(stationId);
        }
        
        socketCharger.value.onmessage = (event) => {
            const data = JSON.parse(event.data);
            if(Array.isArray(data)){
                chargeState.value = [...data];
            }
        }

        socketCharger.value.onclose = () => {
            socketCharger.value.close();
        }
        socketCharger.value.onerror = (error) => {
            console.log('충전소 웹소켓 에러 발생', error);
        }
    }

    const disconnectWebSocket = () => {
        if (socketCharger.value) {
            socketCharger.value.close();
            }
        };
        
    return {
        connectCharger,
        chargeState,
        disconnectWebSocket,
    }

})
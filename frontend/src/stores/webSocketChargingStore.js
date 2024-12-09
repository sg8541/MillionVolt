import { ref } from 'vue';
import { defineStore } from 'pinia';

export const useWebSocketStore = defineStore('websocket', () => {
    const socketInstance = ref(null);
    const chargingData = ref({
            batteryPercent: '',
            amount: '',
            chargingKwh: '',
            chargerType: '',
            name: '',
            address: '',
            carNumber:'',
            pricePerKWh:'',
            expectAmount: '',
            estimatedTimeSeconds: '',
            userId:'',
            stationId:'',

        });
    
        const isConnected = ref(false);

        // WebSocket 연결 함수
        const connectWebSocket = () => {
            if(isConnected.value) {
                console.log("이미연결된 웹소켓.");
                return;
            }
        const userId = 'wogjsdl1244'; // 사용자 ID 동적으로 바꿀 수 있습니다.
        // const reservationId ='1'; 
        socketInstance.value = new WebSocket(`ws://localhost:8081/charging?userId=${userId}`);
    
        socketInstance.value.onopen = () => {
            isConnected.value = true;
            socketInstance.value.send('start');
        
        };
    
        socketInstance.value.onmessage = (event) => {
            const data = JSON.parse(event.data);
            chargingData.value.batteryPercent = data.batteryPercent;
            chargingData.value.amount = data.amount;
            chargingData.value.chargingKwh = data.chargingKwh;
            chargingData.value.chargerType = data.chargerType;
            chargingData.value.name = data.name;
            chargingData.value.address = data.address;
            chargingData.value.carNumber = data.carNumber;
            chargingData.value.pricePerKWh = data.pricePerKWh;
            chargingData.value.expectAmount = data.expectAmount;
            chargingData.value.estimatedTimeSeconds = data.estimatedTimeSeconds;
            chargingData.value.userId = data.userId;
            chargingData.value.reservationId = data.reservationId;
            chargingData.value.stationId = data.stationId;
            
        };
    
        socketInstance.value.onclose = () => {
            console.log('웹소켓 연결 종료');
            socketInstance.value.send('stop');
        };
    
        socketInstance.value.onerror = (error) => {
            console.error('WebSocket 에러', error);
            };
        };
    
        // 컴포넌트 언마운트 시 WebSocket 닫기
        const disconnectWebSocket = () => {
        if (socketInstance.value) {
            sessionStorage.removeItem('websocket');
            socketInstance.value.close();
            isConnected.value = false;
            console.log('웹소켓 연결 종료');
            socketInstance.value.send('stop');
            }
        };
    
        return {
            connectWebSocket,
            disconnectWebSocket,
            chargingData,
            isConnected,
        };
    
    });
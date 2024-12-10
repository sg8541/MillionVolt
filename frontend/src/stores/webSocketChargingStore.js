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
    const finishAlarm = ref({
        message:'',
    })

    const elapsedTime = ref(0); // 경과 시간 (초 단위)
    let timer = null; // 타이머 ID


// 타이머 시작
    const startTimer = () => {
            timer = setInterval(() => {
            elapsedTime.value += 1; // 1초마다 증가
        }, 1000); // 1000ms = 1초
    };

//타이머 종료
    const stopTimer = () => {
        if (timer) {
            clearInterval(timer);
            timer = null;
        }
    };

    const startTime = ref(null); // 충전 시작 시간
    const endTime = ref(null); // 충전 종료 시간. 


    const isConnected = ref(false);

        // WebSocket 연결 함수
        const connectWebSocket = () => {
            if(isConnected.value) {
                console.log("이미연결된 웹소켓.");
                return;
            }
        
        const userId = 'wogjsdl1244'; // 사용자 ID 동적으로 바꿀 수 있습니다.
        const reservationId ='1'; 
        socketInstance.value = new WebSocket(`ws://localhost:8081/charging?userId=${userId}&reservationId=${reservationId}`);
    
        socketInstance.value.onopen = () => {
            isConnected.value = true;
            socketInstance.value.send('start');
          
            startTimer();
            startTime.value = new Date();
           // socketInstance.value.send(reservationId);
        
        };
    
        socketInstance.value.onmessage = (event) => {
            const data = JSON.parse(event.data);
            if (data.message === "충전이 완료되었습니다.") {
                finishAlarm.value.message= data.message; // 또는 원하는 UI 로직을 처리
                chargingData.value.batteryPercent = 100;
                console.log("충전 완료 상태 감지됨");
                console.log(finishAlarm.value.message);
            } else {
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
            }
        };
        
    
        socketInstance.value.onclose = () => {
            console.log('웹소켓 연결 종료1');
            socketInstance.value.close();
            stopTimer();
            endTime.value = new Date();
            isConnected.value = false;
        };
    
        socketInstance.value.onerror = (error) => {
            console.error('WebSocket 에러', error);
            };
        };
    
        // 컴포넌트 언마운트 시 WebSocket 닫기
        const disconnectWebSocket = () => {
        if (socketInstance.value) {
            socketInstance.value.close();
            isConnected.value = false;
            console.log('웹소켓 연결 종료2');
            socketInstance.value.send('stop');
            stopTimer();
            endTime.value = new Date();
            }
        };
    
        return {
            connectWebSocket,
            disconnectWebSocket,
            chargingData,
            isConnected,
            finishAlarm,
            elapsedTime,
            startTime,
            endTime,
        };
    
    });
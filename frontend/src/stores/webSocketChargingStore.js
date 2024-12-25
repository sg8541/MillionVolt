import { ref } from 'vue';
import { defineStore } from 'pinia';
import { useRoute } from 'vue-router';

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
            chargerId:'',
            
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
         
        
        const token = localStorage.getItem('user');
        const parsedToken = JSON.parse(token);
        const userId = parsedToken.userId;
        const route = useRoute();
        const reservationId = route.query.reservationId; 
        const stationId = route.query.stationId;

        if(!reservationId|| !stationId){
            return;
        }
        socketInstance.value = new WebSocket(`ws://localhost:8081/charging?userId=${userId}&reservationId=${reservationId}&stationId=${stationId}`);
    
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
                chargingData.value.batteryPercent = data.batteryPercent; // 배터리 퍼센트
                chargingData.value.amount = data.amount; // 현재 충전 요금
                chargingData.value.chargingKwh = data.chargingKwh; // 충전량 
                chargingData.value.chargerType = data.chargerType; // 충전 타입 몇 kW
                chargingData.value.name = data.name; // 충전소 명
                chargingData.value.address = data.address; // 주소
                chargingData.value.carNumber = data.carNumber; // 유저 차번호
                chargingData.value.pricePerKWh = data.pricePerKWh; // kWh당 가격
                chargingData.value.expectAmount = data.expectAmount;  // 예상금액
                chargingData.value.estimatedTimeSeconds = data.estimatedTimeSeconds; //예상 시간
                chargingData.value.userId = data.userId; // 유저 아이디(PK)
                chargingData.value.reservationId = data.reservationId; // 예약번호
                chargingData.value.stationId = data.stationId; // 충전소 pK
                chargingData.value.chargerId = data.chargerId;// 충전기 pk
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

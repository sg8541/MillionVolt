
<script setup>
import { ref, onMounted } from 'vue';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

const chargerState = ref('');

let stompClient = null;

const connectWebSocket = () => {
  const socket = new SockJS('http://localhost:8081/stomp-chargerstate');
  stompClient = Stomp.over(socket);

  stompClient.connect({}, () => {
    console.log('Connected to WebSocket');

    stompClient.subscribe('/topic/chargerstate', (message) => {
    console.log('Message received: ', message.body);
    try {
        const parsedData = JSON.parse(message.body);
        console.log('Parsed Data: ', parsedData);
        chargerState.value = JSON.stringify(parsedData, null, 2);
    } catch (error) {
        console.error('Error parsing message: ', error);
    }
});
    console.log(chargerState.value);

    // 클라이언트 요청 (1초마다 서버 상태 조회 X, 브로드캐스트는 서버 스케줄링 기능으로 동작)
    stompClient.send('/app/charger', {}, JSON.stringify({ stationId: 1 }));
  });

}

onMounted(() => {
  connectWebSocket();
});


</script>
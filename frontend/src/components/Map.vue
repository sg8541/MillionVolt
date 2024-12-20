<template>
  <div class="relative">
    <div id="map" class="w-full h-screen"></div>
    <div v-if="error" class="absolute top-4 left-1/2 transform -translate-x-1/2 bg-red-500 text-white px-4 py-2 rounded-md">
      {{ error }}
    </div>
    <div v-if="loading" class="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2">
      <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
    </div>
    <Modal
      v-if="isModalVisible"
      :stationId="selectedStationId"
      :isVisible="isModalVisible"
      @close="closeModal"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import Modal from "@/components/Modal.vue";

const router = useRouter(); // Router 객체 생성
const map = ref(null);
const markers = ref([]); // 충전소 마커 리스트
const geocoder = ref(null);
const error = ref(null);
const loading = ref(true);

const selectedStationId = ref(null); // 선택된 충전소 ID
const isModalVisible = ref(false); // 모달 표시 여부

// Kakao API Key 설정
const kakaoApiKey = import.meta.env.VITE_KAKAO_API_KEY;

if (!kakaoApiKey) {
  console.error("Kakao API Key가 .env 파일에 설정되지 않았습니다.");
}

// 모달 열기
const openModal = (stationId) => {
  selectedStationId.value = stationId;
  isModalVisible.value = true;
};

// 모달 닫기
const closeModal = () => {
  selectedStationId.value = null;
  isModalVisible.value = false;
};

// Kakao Maps 스크립트 로드
const loadKakaoMapsScript = async () => {
  return new Promise((resolve, reject) => {
    if (window.kakao && window.kakao.maps) {
      resolve();
      return;
    }
    const script = document.createElement("script");
    script.src = `https://dapi.kakao.com/v2/maps/sdk.js?appkey=${kakaoApiKey}&libraries=services&autoload=false`;
    script.onload = resolve;
    script.onerror = () => reject(new Error("Kakao Maps 스크립트 로딩 실패"));
    document.head.appendChild(script);
  });
};

// 충전소 마커 로드
const loadMarkers = async () => {
  try {
    const response = await fetch("http://localhost:8081/api/v1/charging-stations/markers");
    if (!response.ok) {
      throw new Error(`HTTP 에러: ${response.status}`);
    }

    const stations = await response.json();
    console.log("전체 충전소 데이터:", stations);

    if (!stations || stations.length === 0) {
      console.warn("충전소 데이터가 없습니다.");
      error.value = "충전소 데이터가 없습니다.";
      return;
    }

    // 기존 마커 제거
    markers.value.forEach((marker) => marker.setMap(null));
    markers.value = [];

    // 마커 생성
    stations.forEach((station) => {
      geocoder.value.addressSearch(station.address, (result, status) => {
        if (status === kakao.maps.services.Status.OK) {
          const stationCoords = new kakao.maps.LatLng(result[0].y, result[0].x);
          const marker = new kakao.maps.Marker({
            position: stationCoords,
            map: map.value,
            title: `${station.name} (${station.availableCharger || 0}/${station.totalCharger || 0})`,
          });

          // 마커 클릭 이벤트
          kakao.maps.event.addListener(marker, "click", () => {
            console.log(`마커 클릭됨: ${station.name}`);
            openModal(station.stationId);
            //router.push({ name: "Reservation", params: { station_id: station.stationId } }); // 예약 페이지로 이동
          });

          markers.value.push(marker);
        } else {
          console.error(`주소 변환 실패: ${station.address}`);
        }
      });
    });
  } catch (error) {
    console.error("충전소 데이터를 불러오는 중 문제가 발생했습니다:", error);
    error.value = "충전소 데이터를 불러오는 중 문제가 발생했습니다.";
  } finally {
    loading.value = false;
  }
};

// 지도 초기화
const initializeMap = () => {
  const container = document.getElementById("map");
  if (!container) {
    console.error("#map 요소를 찾을 수 없습니다.");
    return;
  }

  // 지도 설정
  map.value = new kakao.maps.Map(container, {
    center: new kakao.maps.LatLng(37.5665, 126.9780), // 기본 서울 중심 좌표
    level: 5,
  });

  geocoder.value = new kakao.maps.services.Geocoder();
  console.log("지도 객체 초기화 성공:", map.value);

  // 사용자 위치 설정
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        const lat = position.coords.latitude;
        const lng = position.coords.longitude;
        const coords = new kakao.maps.LatLng(lat, lng);
        map.value.setCenter(coords);

        // 사용자 위치 마커
        new kakao.maps.Marker({
          position: coords,
          map: map.value,
          title: "현재 위치",
          image: new kakao.maps.MarkerImage(
            "/images/user-location-icon.png", // 사용자 위치 아이콘 경로
            new kakao.maps.Size(32, 32)
          ),
        });

        console.log("사용자 위치:", coords);
        loadMarkers(); // 충전소 마커 로드
      },
      (err) => {
        console.warn("사용자 위치를 가져올 수 없습니다:", err);
        error.value = "사용자 위치를 가져올 수 없어 기본 위치를 사용합니다.";
        loadMarkers(); // 기본 위치에서 충전소 마커 로드
      }
    );
  } else {
    console.warn("Geolocation API를 사용할 수 없습니다.");
    error.value = "위치 정보를 가져올 수 없어 기본 위치를 사용합니다.";
    loadMarkers(); // 기본 위치에서 충전소 마커 로드
  }
};

// 컴포넌트 마운트 시 지도 초기화
onMounted(async () => {
  try {
    await loadKakaoMapsScript();
    kakao.maps.load(() => {
      console.log("카카오 지도 로드 성공");
      initializeMap();
    });
  } catch (error) {
    console.error("카카오 지도 로드 실패:", error);
    error.value = "카카오 지도를 로드하는 중 문제가 발생했습니다.";
  }
});
</script>

<style scoped>

#map {
  width: 100%;
  height: 100%;
  background-color: #f0f0f0;
}

.relative {
  position: relative;
  height: 100%; /* 부모 요소 높이 상속 */
  overflow: hidden; /* 내부 스크롤 방지 */
}

</style>

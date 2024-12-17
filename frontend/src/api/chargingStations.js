import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_BACKEND_URL, // .env에서 백엔드 URL 가져오기
});

export const chargingStationApi = {
  // 프론트엔드에서 Kakao API Key 가져오기
  async getKakaoApiKey() {
    const apiKey = import.meta.env.VITE_KAKAO_API_KEY;
    if (!apiKey) {
      throw new Error('Kakao API Key가 .env 파일에 설정되지 않았습니다.');
    }
    return apiKey;
  },

  // 주변 충전소 목록 가져오기
  async getNearbyStations(address = '서울', page = 1, size = 5) {
    try {
      const response = await api.get('/sidebar', {
        params: { address, page, size },
      });
      return response.data;
    } catch (error) {
      console.error('Error fetching nearby stations:', error);
      throw error;
    }
  },

  // 위치 기반 충전소 검색
  async getNearbyStationsByLocation(latitude, longitude, radius = 5) {
    try {
      const response = await api.get('/nearby', {
        params: { latitude, longitude, radius },
      });
      return response.data;
    } catch (error) {
      console.error('Error fetching stations by location:', error);
      throw error;
    }
  },

  // 충전소 상세 정보 가져오기
  async getStationDetails(stationId) {
    try {
      const response = await api.get(`/station/${stationId}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching details for station ID ${stationId}:`, error);
      throw error;
    }
  },

  // 충전소의 충전기 정보 가져오기
  async getStationChargers(stationId) {
    try {
      const response = await api.get(`/station/${stationId}/chargers`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching chargers for station ID ${stationId}:`, error);
      throw error;
    }
  },

  // 충전소 검색
  async searchStations(query, page = 1, size = 5) {
    try {
      const response = await api.get('/search-paginated', {
        params: { query, page, size },
      });
      return response.data;
    } catch (error) {
      console.error('Error searching stations:', error);
      throw error;
    }
  },

  // 충전 속도로 필터링
  async filterStations(chargerSpeedId, address = '서울') {
    try {
      const response = await api.get('/filter', {
        params: { chargerSpeedId, address },
      });
      return response.data;
    } catch (error) {
      console.error('Error filtering stations:', error);
      throw error;
    }
  },

  // 사이드바용 충전소 목록
  async getSidebarStations(address = '서울') {
    try {
      const response = await api.get('/sidebar', {
        params: { address },
      });
      return response.data;
    } catch (error) {
      console.error('Error fetching sidebar stations:', error);
      throw error;
    }
  },

  // 지도 마커용 충전소 데이터
  async getMapMarkers(address = '서울', chargerSpeedId = null) {
    try {
      const response = await api.get('/markers', {
        params: { address, chargerSpeedId },
      });
      return response.data;
    } catch (error) {
      console.error('Error fetching map markers:', error);
      throw error;
    }
  },
};

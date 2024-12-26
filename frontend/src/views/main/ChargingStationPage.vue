<template>
  <div class="container">
    <Header class="header" @toggleSidebar="toggleSidebar" />
    <div class="content">
      <div class="map-container">
        <Map
          @stationSelected="selectStation"
          @updateStations="updateStations"
        />
        <Modal
          v-if="selectedStation"
          :station="selectedStation"
          @close="selectedStation = null"
        />
        <div
          v-if="selectedStation && selectedStation.chargers"
          class="chargers-container"
        >
          <Charger
            v-for="(charger, index) in selectedStation.chargers"
            :key="charger.charger_id"
            :charger="charger"
            :index="index"
            @reserve="handleReserve"
          />
        </div>
      </div>
      <Sidebar
        class="sidebar"
        :stations="stations"
        :isSidebarVisible="isSidebarVisible"
        @toggleSidebar="toggleSidebar"
        @stationSelected="selectStation"
      />
    </div>
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import Map from "@/components/Map.vue";
import Sidebar from "@/components/Sidebar.vue";
import Modal from "@/components/Modal.vue";
import Charger from "@/components/Charger.vue";

export default {
  components: { Header, Map, Sidebar, Modal, Charger },
  data() {
    return {
      stations: [], // 충전소 데이터
      selectedStation: null, // 선택된 충전소
      isSidebarVisible: false, // 사이드바 가시성 상태
    };
  },
  methods: {
    toggleSidebar() {
      this.isSidebarVisible = !this.isSidebarVisible; // 상태 값 반전
      console.log("isSidebarVisible 상태:", this.isSidebarVisible); // 상태 값 디버깅
    },
    selectStation(station) {
      this.selectedStation = station; // 선택된 충전소 업데이트
    },
    updateStations(stations) {
      this.stations = stations; // Map.vue에서 전달받은 충전소 데이터 업데이트
    },
    async filterStationsBySpeed(speeds) {
      try {
        const response = await fetch(
          `/api/v1/charging-stations/markers?speeds=${speeds.join(",")}`
        );
        if (!response.ok) {
          throw new Error("Failed to filter stations");
        }
        const data = await response.json();
        this.stations = data; // 필터링된 충전소 데이터 업데이트
      } catch (error) {
        console.error("Error filtering stations:", error);
      }
    },
    handleReserve(charger) {
      alert(`Reserving charger ID: ${charger.charger_id}`);
    },
  },
};
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.header {
  flex: 0 0 auto;
  background-color: #f8f9fa;
  border-bottom: 1px solid #ddd;
  padding: 10px;
}

.content {
  display: flex;
  flex: 1;
  height: calc(100vh - 50px);
}

.map-container {
  flex: 7;
  padding: 10px;
  position: relative;
}

.sidebar {
  flex: 3;
  padding: 10px;
  background-color: #f4f4f4;
  overflow-y: auto;
  border-left: 1px solid #ddd;
  display: none; /* 기본적으로 숨김 */
}

.sidebar.visible {
  display: block; /* 가시성 상태에 따라 표시 */
}

.chargers-container {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 20px;
}

@media (prefers-contrast: more) {
  .chargers-container {
    outline: 1px solid #000;
  }
}
</style>

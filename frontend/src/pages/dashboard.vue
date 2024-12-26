<script setup>
import AnalyticsCongratulations from '@/views/dashboard/AnalyticsCongratulations.vue'
import AnalyticsTotalRevenue from '@/views/dashboard/AnalyticsTotalRevenue.vue'
import AnalyticsPaymentTable from '@/views/dashboard/AnalyticsPaymentTable.vue'
import AnalyticsReservationTable from '@/views/dashboard/AnalyticsReservationTable.vue'
import { useAuthStore } from '@/stores/auth';
import { defineProps } from 'vue'
import { useRouter } from 'vue-router';
import api from '@/axios'

const authStore = useAuthStore();
const router = useRouter();
const props = defineProps({
  id: {
    type: Number,
    required: true
  }
})

const info = ref(null)

const fetchMyInfo = async (id) => {

  try {
    const response = await api.get(`/myinfo/dashboard/${id}`)
    console.log(response);
    info.value = response.data;
    console.log("dashboard id : " + id);
  } catch (error) {
    if (error.response.status === 403) {
      const tokenRefreshed = await handleRefreshAccessToken();
      if(!tokenRefreshed){
        router.replace("/forbidden")
        alert('잘못된 접근입니다. 다시 로그인 해주세요')
      }
    } else
      console.error("Error fetching my info:", error);
  }
}
onMounted(() => {
  fetchMyInfo(props.id);
})

// const handleLogout = async () => {
//   try {
//     await authStore.logout()
//     showUserMenu.value = false
//     console.log("로그아웃 실행");
//     isLoggedIn.value = false;
//     router.push('/main') // 로그아웃 후 홈으로 이동
//   } catch (error) {
//     console.error('로그아웃 실패:', error)
//   }
// }

const handleRefreshAccessToken = async () => {
  try{
    await authStore.refreshAccessToken();
    return true;
  }catch(error){
    console.error('토큰 재발급 실패:', error)
    return false;
  }
}


</script>

<template>
  <VRow>
    <!-- 👉 Congratulations -->
    <VCol cols="12" md="12">
      <AnalyticsCongratulations v-if="info && info.userCarInfo" :user-car-info="info.userCarInfo" />
    </VCol>

    <!-- 👉 ReservationTable -->
    <VCol cols="12" md="12" order="3">
      <!-- info.reservationList가 있을 때만 AnalyticsReservationTable 렌더링 -->
      <AnalyticsReservationTable v-if="info && info.reservationList" :reservations="info.reservationList" />
    </VCol>

    <!-- 👉 PaymentTable -->
    <VCol cols="12" md="12" order="3">
      <!-- info.paymentList가 있을 때만 AnalyticsPaymentTable 렌더링 -->
      <AnalyticsPaymentTable v-if="info && info.paymentList" :payments="info.paymentList" />
    </VCol>
  </VRow>
</template>

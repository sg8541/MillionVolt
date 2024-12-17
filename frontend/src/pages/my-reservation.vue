<script setup>
import ReservationTable from '@/views/pages/tables/ReservationTable.vue'
import { onMounted, ref} from 'vue';
import api from '@/axios'

import { useRoute } from 'vue-router'


const route = useRoute()
const id = route.params.id;

const info = ref(null)

const fetchResulvationInfo = async (id) => {
 
 try{
   const response = await api.get('/myinfo/reservation/'+id)
   console.log(response);
   info.value = response.data;
 }catch(error){
   console.error("Error fetching Resulvation info:", error);
 }
}
onMounted(()=>{
  fetchResulvationInfo(id);
})


</script>

<template>
  <VRow>
    <VCol cols="12">
      <VCard title="나의 예약 내역">
        <ReservationTable v-if="Array.isArray(info) && info.length" :reservations="info"/>
      </VCard>
    </VCol>
  </VRow>
</template>

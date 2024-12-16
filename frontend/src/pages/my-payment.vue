<script setup>
import PaymentTable from '@/views/pages/tables/PaymentTable.vue'
import { onMounted, ref} from 'vue';
import api from '@/axios'
import { useRoute } from 'vue-router'


const route = useRoute()
const id = route.params.id;

const info = ref(null)

const fetcPaymentInfo = async (id) => {
 
 try{
   const response = await api.get('/myinfo/payment/'+id)
   console.log(response);
   console.log("payment id : " + id);
   info.value = response.data;
 }catch(error){
   console.error("Error fetching Payment info:", error);
 }
}
onMounted(()=>{
  fetcPaymentInfo(id);
})

</script>

<template>
  <VRow>
    <VCol cols="12">
      <VCard title="나의 결제 내역">
        <PaymentTable   v-if="Array.isArray(info) && info.length" :payments="info"/>
      </VCard>
    </VCol>
  </VRow>
</template>

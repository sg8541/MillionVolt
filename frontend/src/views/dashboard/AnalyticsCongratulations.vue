<script setup>
import { useTheme } from 'vuetify'
import illustrationJohnDark from '@images/cards/illustration-john-dark.png'
import illustrationJohnLight from '@images/cards/illustration-john-light.png'
import VerticalNavLink from '@layouts/components/VerticalNavLink.vue'

const { global } = useTheme()
const illustrationJohn = computed(() => global.name.value === 'dark' ? illustrationJohnDark : illustrationJohnLight)

import { defineProps } from 'vue'
import { useRoute } from 'vue-router'


const route = useRoute()

defineProps({
  userCarInfo: {
    type: Object,
    required: true
  },
})



</script>

<template>
  <VCard class="text-center text-sm-start">
    <VRow no-gutters>
      <VCol
        cols="12"
        sm="8"
        order="2"
        order-sm="1"
      >
        <VCardItem>
          <VCardTitle class="text-md-h5 text-primary">
            환영합니다 {{userCarInfo.username}}님! 🎉
          </VCardTitle>
        </VCardItem>

        <VCardText>
          <span>
            차량 : {{ userCarInfo.modelName }}
            <br>
            현재 배터리 : {{ userCarInfo.carBattery }}
          </span>
          <br>
          <br>
          <span>
            내 주변 충전소 검색하기
          </span>
          <br>
          <VBtn
            variant="tonal"
            class="mt-4"
            size="small"
          >
          <VerticalNavLink
            :item="{
              title: '배터리 업데이트',
              to: '/myinfo/account-settings/'+route.params.id,
            }"
          >배터리 업데이트
            </VerticalNavLink>
          </VBtn>
        </VCardText>
      </VCol>

      <VCol
        cols="12"
        sm="4"
        order="1"
        order-sm="2"
        class="text-center"
      >
        <img
          :src="userCarInfo.modelFilepath"
          :height="$vuetify.display.xs ? '150' : '175'"
          :class="$vuetify.display.xs ? 'mt-6 mb-n2' : 'position-absolute'"
          class="john-illustration flip-in-rtl"
        >
        ### 차량 이미지로 바꾸기
      </VCol>
    </VRow>
  </VCard>
</template>

<style lang="scss" scoped>
.john-illustration {
  inset-block-end: -0.0625rem;
  inset-inline-end: 3rem;
}
</style>

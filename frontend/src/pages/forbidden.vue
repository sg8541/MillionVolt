<script setup>
import ErrorHeader from '@/components/ErrorHeader.vue'
import misc403 from '@images/pages/403.png'
</script>

<template>
  <div class="misc-wrapper">
    <ErrorHeader
      error-code="403"
      error-title="접근 제한 ⚠️"
      error-description="잘못된 접근입니다. 입력하신 페이지의 주소가 정확한지 다시 한번 확인해주시기 바랍니다."
    />

    <!-- 👉 Image -->
    <div class="misc-avatar w-100 text-center">
      <VImg
        :src="misc403"
        alt="Coming Soon"
        :max-width="800"
        class="mx-auto"
      />
      <VBtn
        to="/"
        class="mt-10"
      >
        Back to Home
      </VBtn>
    </div>
  </div>
</template>

<style lang="scss">
@use "@core/scss/template/pages/misc.scss";
</style>

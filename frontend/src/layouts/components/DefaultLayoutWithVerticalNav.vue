<script setup>
import { useTheme } from 'vuetify'
import VerticalNavSectionTitle from '@/@layouts/components/VerticalNavSectionTitle.vue'
import VerticalNavLayout from '@layouts/components/VerticalNavLayout.vue'
import VerticalNavLink from '@layouts/components/VerticalNavLink.vue'

// Components
import NavbarThemeSwitcher from '@/layouts/components/NavbarThemeSwitcher.vue'
import UserProfile from '@/layouts/components/UserProfile.vue'

const vuetifyTheme = useTheme()

import { defineProps } from 'vue'

import { useRoute } from 'vue-router'


const route = useRoute()
console.log(``)

const props = defineProps({
  id: {
    type: Number,
    required: true
  }
})

onMounted(()=>{
  console.log(props.id)
})
</script>

<template>
  <VerticalNavLayout>
    <!-- 👉 navbar -->
    <template #navbar="{ toggleVerticalOverlayNavActive }">
      <div class="d-flex h-100 align-center">
        <!-- 👉 Vertical nav toggle in overlay mode -->
        <IconBtn
          class="ms-n3 d-lg-none"
          @click="toggleVerticalOverlayNavActive(true)"
        >
          <VIcon icon="bx-menu" />
        </IconBtn>

        <VSpacer />

        <NavbarThemeSwitcher class="me-2" />

        <!-- <UserProfile /> -->
      </div>
    </template>

    <template #vertical-nav-content>
      <VerticalNavLink
        :item="{
          title: 'Dashboard',
          icon: 'bx-home',
          to: '/myinfo/dashboard/'+route.params.id,
        }"
      />
      <VerticalNavLink
        :item="{
          title: '내 정보',
          icon: 'mdi-account-cog-outline',
          to: '/myinfo/account-settings/'+route.params.id,
        }"
      />
      <VerticalNavLink
        :item="{
          title: '나의 예약 정보',
          icon: 'bx-time',
          to: '/myinfo/my-reservation/'+route.params.id,
        }"
      />
      <VerticalNavLink
        :item="{
          title: '나의 결제 정보',
          icon: 'bx-wallet-alt',
          to: '/myinfo/my-payment/'+route.params.id,
        }"
      />

      <!-- 👉 User Interface -->
      <VerticalNavSectionTitle
        :item="{
          heading: 'User Interface',
        }"
      />
      <VerticalNavLink
        :item="{
          title: '회원탈퇴',
          icon: 'bx-info-circle',
          to: '/exit/'+route.params.id,
        }"
      />

    </template>

    <!-- 👉 illustration -->
    <!-- <template #after-vertical-nav-items>
    
      <a
        href="https://themeselection.com/item/sneat-vuetify-vuejs-admin-template"
        target="_blank"
        rel="noopener noreferrer"
        style="margin-left: 7px;"
      >
      </a>
    </template> -->

    <!-- 👉 Pages -->
    <slot />

  </VerticalNavLayout>
</template>

<style lang="scss" scoped>
.meta-key {
  border: thin solid rgba(var(--v-border-color), var(--v-border-opacity));
  border-radius: 6px;
  block-size: 1.5625rem;
  line-height: 1.3125rem;
  padding-block: 0.125rem;
  padding-inline: 0.25rem;
}
</style>

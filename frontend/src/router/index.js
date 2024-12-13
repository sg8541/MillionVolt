import { createRouter, createWebHistory } from 'vue-router'
import ChargingStatus from '../views/chargingwebsocket/ChargingStatus.vue'
import PayPrice from '@/views/payment/PayPrice.vue'
import TestAlarm from '@/views/alarm/TestAlarm.vue'

import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/myinfo/dashboard/:id' },
    {
      path: '/myinfo/',
      component: () => import('../layouts/default.vue'),
      props: true,
      meta: { requiresAuth: true, requiresMember: true },
      children: [
        {
          path: 'dashboard/:id',
          component: () => import('../pages/dashboard.vue'),
          props: true,
          meta: { requiresAuth: true, requiresMember: true },
        },
        {
          path: 'account-settings/:id',
          component: () => import('../pages/account-settings.vue'),
          props: true,
          meta: { requiresAuth: true, requiresMember: true },
        },
        { 
          path: 'my-reservation',
          component: () => import('../pages/my-reservation.vue'),
          props: true,
          meta: { requiresAuth: true, requiresMember: true },
        },
        {
          path: 'my-payment',
          component: () => import('../pages/my-payment.vue'),
          props: true,
          meta: { requiresAuth: true, requiresMember: true },
        },
      ],
    },
    {
      path: '/login',
      name: 'Login',
      component : () => import('../views/pages/user-account/Login.vue'),
    },
    {
      path: '/agreement',
      neme: 'Agreement',
      component : () => import('../views/pages/user-account/Agreement.vue'),
    },
    {
      path: '/signup',
      neme: 'Signup',
      component : () => import('../views/pages/user-account/Signup.vue'),
    },
    {
      path: '/find-id',
      neme: 'FindByUserId',
      component : () => import('../views/pages/user-account/FindByUserId.vue'),
    },
    {
      path:'/chargingStatus',
      name: 'ChargingStatus',
      component : ChargingStatus
    },
    { 
      path:'/payPrice',
      name : PayPrice,
      component : PayPrice
    },
    {
      path:'/testAlarm',
      name:TestAlarm,
      component:TestAlarm
    },
    {
      path: '/',
      component: () => import('../layouts/blank.vue'),
      children: [
        //에러페이지
        {
          path: '/:pathMatch(.*)*',
          component: () => import('../pages/[...all].vue'),
        },
      ],
    },
    {
      path:'/Reservation',
      name: 'Reservation',
      component: () => import('../views/payment/Reservation.vue'),
    }
  ],
})

  // 라우터 가드 설정
  router.beforeEach((to, from, next) => {
    const authStore = useAuthStore()
    if (to.matched.some(record => record.meta.requiresAuth)) {
      if (!authStore.accessToken) {
        next({ name: 'Login' })
      } else if (to.matched.some(record => record.meta.requiresMember) && authStore.user.role !== 'MEMBER') {
        // 유저 확인
        next({ path: '/dashboard:id' })
      } else {
        next()
      }
    } else {
      next()
    }
  })


export default router

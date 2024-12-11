import { createRouter, createWebHistory } from 'vue-router'
import ChargingStatus from '../views/chargingwebsocket/ChargingStatus.vue'
import PayPrice from '@/views/payment/PayPrice.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/dashboard' },
    {
      path: '/',
      component: () => import('../layouts/default.vue'),
      children: [
        {
          path: 'dashboard',
          component: () => import('../pages/dashboard.vue'),
        },
        {
          path: 'account-settings',
          component: () => import('../pages/account-settings.vue'),
        },
        {
          path: 'my-reservation',
          component: () => import('../pages/my-reservation.vue'),
        },
        {
          path: 'my-payment',
          component: () => import('../pages/my-payment.vue'),
        },
      ],
    },
    {
      path:'/chargingStatus',
      component : ChargingStatus
    },
    { 
      path:'/payPrice',
      name : PayPrice,
      component : PayPrice
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
  ],
})

export default router

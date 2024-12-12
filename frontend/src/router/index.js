import { createRouter, createWebHistory } from 'vue-router'
import ChargingStatus from '../views/chargingwebsocket/ChargingStatus.vue'
import PayPrice from '@/views/payment/PayPrice.vue'
import TestAlarm from '@/views/alarm/TestAlarm.vue'
import ReservationAlarm from '@/views/alarm/ReservationAlarm.vue'

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
      name: 'TestAlarm',
      component:TestAlarm
    },
    {
      path:'/reservationAlarm',
      name:'ReservationAlarm',
      component:ReservationAlarm
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

export default router

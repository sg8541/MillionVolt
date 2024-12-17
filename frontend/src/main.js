/* eslint-disable import/order */
import '@/@iconify/icons-bundle'
import App from '@/App.vue'
import vuetify from '@/plugins/vuetify'
import { loadFonts } from '@/plugins/webfontloader'
import router from '@/router'
import '@core/scss/template/index.scss'
import '@layouts/styles/index.scss'
import '@styles/styles.scss'
import { createPinia } from 'pinia'
import { createApp } from 'vue'

// 중앙집중식 Axios 인스턴스 임포트
import api from './axios'


loadFonts()


// Create vue app
const app = createApp(App);
//const pinia = createPinia(); 

// 전역 속성으로 Axios 설정 (선택 사항)
app.config.globalProperties.$axios = api


// Use plugins
app.use(vuetify)
app.use(createPinia())
app.use(router)

// Mount vue app
app.mount('#app')
//app.use(pinia); 

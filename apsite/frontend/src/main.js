import { createApp, defineAsyncComponent } from 'vue'
import { createI18n } from 'vue-i18n'
import { store } from './store/index.js'
import App from './App.vue'
import router from './router'
import dynamicRouter from './router/dynamicRouter'
import messages from './locales/index.js'
import LoadScript from 'vue-plugin-load-script'
import * as VeeValidate from 'vee-validate'
import './assets/scss/styles.scss'
import './assets/css/grp.css'

import { SetupCalendar, Calendar, DatePicker } from 'v-calendar'
import 'v-calendar/dist/style.css'

//[S]tab sesstionStorage 공유
if (!sessionStorage.length) {
  localStorage.setItem('getSessionStorage', Date.now());
  localStorage.setItem('sessionStorage', JSON.stringify(sessionStorage));
}

window.addEventListener('storage', function(event) {
  if (event.key == 'getSessionStorage') {
    localStorage.setItem('sessionStorage', JSON.stringify(sessionStorage));
    localStorage.removeItem('sessionStorage');
  } else {
    const data = JSON.parse(event.newValue);
    for (let key in data) {
      sessionStorage.setItem(key, data[key]);
    }
    const sessionToken = sessionStorage.getItem('accessToken') || ''
    if(sessionToken){
      store.dispatch('signinByToken', { sessionToken: sessionToken, localToken : '' }).then(() => {
        //router.push({ path: '/main' })
      })      
    }
  }
})
//[E]tab sesstionStorage 공유  

const init = () => {
  const sessionToken = sessionStorage.getItem('accessToken') || ''
  const localToken = localStorage.getItem('accessToken') || ''
  if (sessionToken) {
    return store.dispatch('signinByToken', { sessionToken: sessionToken, localToken : '' })
  } else if (localToken) {
    return store.dispatch('signinByToken', { localToken: localToken })
  }
  return Promise.resolve()
}

//window.addEventListener('storage') 이벤트 발생 시간과 init 쪽에 gap 이 존재하여
//setTimeout 줌, tab 새로 추가해서 호출할 경우 잠깐의 화면 번쩍임이 존재할 수 있다.
setTimeout(() => {
  init().then(() => {
    const i18n = createI18n({
      legacy: false,
      locale: store.getters['getLangCd'](),
      fallbackLocale: 'ko',
      warnHtmlMessage: false,
      messages
    })
    //라우터 저장
    dynamicRouter.init()
    const app = createApp(App)
    app.use(store)
    app.use(router)
    app.use(i18n)
    app.use(LoadScript)
    app.use(VeeValidate)
    app.use(SetupCalendar, {})
  
    app.component('Calendar', Calendar)
    app.component('DatePicker', DatePicker)
    app.component('ApDatePickerRange', defineAsyncComponent(() => import('@/components/comm/ApDatePickerRange.vue')))
    app.component('ApAlert', defineAsyncComponent(() => import('@/components/comm/ApAlert.vue')))
    app.mount('#app')
  })
}, 300);
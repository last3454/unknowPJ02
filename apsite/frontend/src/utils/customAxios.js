
// import axios from 'axios'
import { axios } from '@bundled-es-modules/axios'
import { store } from '@/store/index.js'
import { createI18n } from 'vue-i18n'
import messages from '@/locales/index.js'

// 타임아웃 제한 시간을 30초로 설정
// 각 호출시 제한시간을 변경 할 수 있어, 통신이 끝나면(성공/실패) 초기값으로 항상 셋팅함
const timeout = 30 * 1000

// axios.defaults.baseURL = process.env.VUE_APP_API_URL || ''
// axios.defaults.timeout = timeout // 타임아웃 제한시간 초기화

const instance = axios.create({
  baseURL: import.meta.env.VITE_BASE_URL,
  timeout: timeout,
  headers: {
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': '*'
  }
})

instance.interceptors.request.use(config => {
  // config.headers['Access-Control-Allow-Origin'] = '*'
  const accessToken = store.getters['getAccessToken']()
  const langCd = store.getters['getLangCd']()

  if (accessToken !== undefined && accessToken !== '') {
    config.headers['authorization'] = accessToken
  }

  if (langCd !== undefined && langCd !== '') {
    config.headers['x-lang-cd'] = langCd
  }

  if(config.isLoading) store.dispatch('setLoading', true)
  return config
}, error => {
  instance.defaults.timeout = timeout // 타임아웃 제한시간 초기화
  if(config.isLoading) store.dispatch('setLoading', false)
})

instance.interceptors.response.use(response => {
  instance.defaults.timeout = timeout
  if(response.config.isLoading) store.dispatch('setLoading', false)
  // const contentType = response.headers['content-type'].toLowerCase()

  // if (contentType.indexOf('json') === -1) {
  //   // 엑셀다운로드
  //   return response.data
  // }
  const hisUrl = window.location.href

  if (hisUrl.indexOf('/error') === -1) {
    sessionStorage.setItem('historyBack', hisUrl)
  }

  return response
}, error => {
  instance.defaults.timeout = timeout // 타임아웃 제한시간 초기화

  // console.log('[s] axios : ERROR')
  // console.log(error)
  // console.log(error.response)
  // console.log('[e] axios : ERROR')
  store.dispatch('setLoading', false)
  const i18n = createI18n({
    legacy: false,
    locale: store.getters['getLangCd'](),
    fallbackLocale: 'ko',
    warnHtmlMessage: false,
    messages
  })

  const errorCode = error.response.status

  if (error.response === undefined || errorCode === undefined) {
    return Promise.reject(error)
  }

  if (errorCode === 401) {
    window.location.href = '/login'
  } else if (errorCode === 400 || errorCode === 403 ||errorCode === 500) {
    window.location.href = '/error/500'
  } else {
    return Promise.reject(error)
  }
})

export default instance

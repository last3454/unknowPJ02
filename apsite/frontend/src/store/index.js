import axios from '@/utils/customAxios'
import { createStore } from 'vuex'

export const store = createStore({
  state: () => ({
    isLoading: false,
    accessToken: '',
    myInfo: {},
    langCd: '',
    alertInfo: {},
    alertAsync: {},
    gnbList: [],
    routerList: [],
    popupInfo: {},
    popupAsync: {}
  }),
  getters: {
    getLoading: (state) => () =>{
      return state.isLoading
    },
    getPopupInfo: (state) => () => {
      return state.popupInfo
    },
    getAlertInfo: (state) => () => {
      return state.alertInfo
    },
    getAccessToken: (state) => () => {
      return state.accessToken
    },
    getMyInfo: (state) => () => {
      return state.myInfo
    },
    getLangCd: (state) => () => {
      if (state.langCd === '') {
        const langCd = localStorage.getItem('langCd')
        if (!langCd) {
          state.langCd = 'ko'
          localStorage.setItem('langCd', 'ko')
        } else {
          state.langCd = langCd
        }
      }
      return state.langCd
    },
    getGnb: (state) => () => {
      return state.gnbList
    },
    getRouter: (state) => () => {
      return state.routerList 
    }
  },
  // 뮤테이션(mutations)은 스토어의 상태를 변경할 수 있는 유일한 메서드 이다.
  mutations: {
    SET_ACCESS_TOKEN (state, accessToken) {
      if (accessToken) {
        state.accessToken = accessToken
        sessionStorage.setItem('accessToken', accessToken)
      }
    },
    REMOVE_ACCESS_TOKEN (state) {
      state.accessToken = ''
      state.myInfo = {}
      state.routerList = []
      sessionStorage.removeItem('accessToken')
      sessionStorage.removeItem('routerList')
      localStorage.removeItem('accessToken')
    },
    SET_MY_INFO (state, payload) {
      if (payload) {
        state.myInfo = payload
      }
    },
    SET_LANG_CD (state, langCd) {
      if (langCd) {
        state.langCd = langCd
        localStorage.setItem('langCd', langCd)
      }
    },
    OPEN_POPUP (state, payload) {
      const scrollTop = window.scrollY || window.document.documentElement.scrollTop
      state.popupInfo = {
        isOpen: true,
        scrollTop: scrollTop,
        ...payload
      }

      sessionStorage.setItem('scroll', scrollTop)
      window.document.querySelector('html').style.overflow = 'hidden'
    },
    CLOSE_POPUP (state, payload) {
      state.popupAsync.resolve(payload)

      window.document.querySelector('html').style.overflow = 'auto'
      setTimeout(() => {
        window.document.documentElement.scrollTo(0, sessionStorage.getItem('scroll'))
        sessionStorage.removeItem('scroll')
      }, 100)

      state.popupInfo = {
        isOpen: false
      }
      state.popupAsync.resolve = undefined
      state.popupAsync.reject = undefined
    },
    SET_POPUP (state, { resolve, reject }) {
      state.popupAsync.resolve = resolve
      state.popupAsync.reject = reject
    },
    OPEN_ALERT (state, payload) {
      const scrollTop = window.scrollY || window.document.documentElement.scrollTop
      state.alertInfo = {
        isOpen: true,
        scrollTop: scrollTop,
        ...payload
      }

      window.document.querySelector('html').style.overflow = 'hidden'
    },
    CLOSE_ALERT (state, type) {
      state.alertAsync.resolve(type === 'OK')

      window.document.querySelector('html').style.overflow = 'auto'
      window.scrollTo(0, state.alertInfo.scrollTop)

      state.alertInfo = {
        isOpen: false
      }
      state.alertAsync.resolve = undefined
      state.alertAsync.reject = undefined
    },
    SET_ALERT (state, { resolve, reject }) {
      state.alertAsync.resolve = resolve
      state.alertAsync.reject = reject
    },
    REMOVE_ALERT (state, type) {
      if (type === 'OK') {
        state.alertAsync.resolve(true)
      } else {
        state.alertAsync.resolve(false)
      }
      state.alertAsync.resolve = undefined
      state.alertAsync.reject = undefined
    },
    SET_GNB(state, payload) {
      state.gnbList = payload
    },
    SET_ROUTER(state, payload) {
      state.routerList = payload;
      sessionStorage.setItem('routerList', JSON.stringify(payload))
    },
    SET_LOADING(state, payload){
      state.isLoading = payload;
    }
  },
  // 액션(actions)은 비동기 처리를 포함할 수 있는 메서드이다.
  // 액션을 사용하는 일반적인 경우는 데이터 가공 또는 비동기 처리를 실시한 후, 그 결과를 뮤테이션에 전달하고 뮤테이션을 커밋하는 것이다.
  actions: {
    setLoading(context, isLoading){
      context.commit('SET_LOADING', isLoading);
    },
    setSession (context, { token, myInfo }) {
      context.commit('SET_ACCESS_TOKEN', token)
      context.commit('SET_MY_INFO', myInfo)
    },
    changeLangCd (context, langCd) {
      context.commit('SET_LANG_CD', langCd)
    },
    signin (context, payload) {
      return axios({
        method: 'post',
        url: '/api/auth/signin',
        isLoading: true,
        data: payload
      })
        .then(async response => {
          const resData = response.data

          if (resData.code === 'C0000' || resData.code === 'USER_PW_EXPIRED') {
            const myInfo = resData.data 
            const token = myInfo.token
            delete myInfo.token

            context.commit('SET_ACCESS_TOKEN', token)
            context.commit('SET_MY_INFO', myInfo)

            if (payload.rememberMe === 'Y') {
              const remToken = resData.data.rememberMeToken
              localStorage.setItem('accessToken', remToken)
            } else {
              localStorage.removeItem('accessToken')
            }
          }

          return response
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    signinByToken (context, payload) {
      return axios({
        method: 'get',
        url: '/api/auth/token-check',
        headers: {
          'x-session-token': payload.sessionToken,
          'x-local-token': payload.localToken ? payload.localToken : ''
        }
      }).then(res => {
          const resData = res.data
          // T0001 : sessionToken 유효, T0002 : localToken 유효(자동로그인)
          if (resData.code === 'T0001' || resData.code === 'T0002') {
            const myInfo = resData.data
            const token = myInfo.token
            delete myInfo.token
            context.commit('SET_ACCESS_TOKEN', token)
            context.commit('SET_MY_INFO', myInfo)
          } else {
            context.commit('REMOVE_ACCESS_TOKEN')
          }
        })
    },
    signout (context) {
      const sessionToken = sessionStorage.getItem('accessToken')
      const localToken = localStorage.getItem('accessToken')
      return axios({
        method: 'get',
        url: '/api/auth/signout',
        isLoading: true,
        headers: {
          'x-session-token': sessionToken,
          'x-local-token': localToken
        }
      })
        .then(async response => {
          const resData = response.data

          if (resData.code === 'C0000') {
            context.commit('REMOVE_ACCESS_TOKEN')
          }

          return response
        })
        .catch(function (error) {
          console.error(error)
        })
    },
    openAsyncPopup (context, payload) {
      context.commit('OPEN_POPUP', payload)
      return new Promise((resolve, reject) => {
        context.commit('SET_POPUP', { resolve, reject })
      })
    },
    closeAsyncPopup (context, payload) {
      context.commit('CLOSE_POPUP', payload)
    },
    openAsyncAlert (context, payload) {
      const opts = {
        ...payload,
        type: 'ALERT'
      }
      context.commit('OPEN_ALERT', opts)
      return new Promise((resolve, reject) => {
        context.commit('SET_ALERT', { resolve, reject })
      })
    },
    closeAsyncAlert (context) {
      context.commit('CLOSE_ALERT', 'OK')
    },
    openAsyncConfirm (context, payload) {
      const opts = {
        ...payload,
        type: 'CONFIRM'
      }
      context.commit('OPEN_ALERT', opts)
      return new Promise((resolve, reject) => {
        context.commit('SET_ALERT', { resolve, reject })
      })
    },
    closeAsyncConfirm (context, type) {
      context.commit('CLOSE_ALERT', type)
    },
    findGnb (context) {
      return axios({
        method: 'get',
        url: '/api/menu',
        paramsSerializer: paramsObj => {
          const params = new URLSearchParams()
          for (const key in paramsObj) {
            if (Array.isArray(paramsObj[key]) && paramsObj[key].length === 0) {
              continue
            }
            params.append(key, paramsObj[key])
          }
          return params.toString()
        }
      }).then(async response => {
        const resData = response.data
        if (resData.code === 'C0000') {
          const gnbData = resData.data 
          context.commit('SET_GNB', gnbData)
        }
        return response
      })
      .catch(function (error) {
        console.log(error)
      })
    },
    findRouter (context) {
      return axios({
        method: 'get',
        url: '/api/menu/page',
        paramsSerializer: paramsObj => {
          const params = new URLSearchParams()
          for (const key in paramsObj) {
            if (Array.isArray(paramsObj[key]) && paramsObj[key].length === 0) {
              continue
            }
            params.append(key, paramsObj[key])
          }
          return params.toString()
        }
      }).then(async response => {
        const resData = response.data
        if (resData.code === 'C0000') {
          const gnbData = resData.data 
          context.commit('SET_ROUTER', gnbData)
        }
        return response
      })
      .catch(function (error) {
        console.log(error)
      })
    }
  }
})
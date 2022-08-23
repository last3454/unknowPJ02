import axios from '@/utils/customAxios'
import { reactive, toRefs } from 'vue'
import { inject } from 'vue'
import { useActions } from 'vuex-composition-helpers'

export const useLogin = () => {
  const state = reactive({
    loginPw: '',
    loginPwTemp: '',
    niceType: '',
    popupContent: '',
    popParams: {}
  })

  const t = inject('t')
  const { openAsyncPopup, openAsyncAlert } = useActions(['openAsyncPopup','openAsyncAlert'])

  const fnOpenPopup = (compNm) => {
    state.popupContent = compNm
    
    openAsyncPopup({ minWidth: 800 })
      .then(res => {
        if (res.emitValue) {
          const data = res.emitValue
        }
      })
      .catch(err => {
        console.log(err)
      })
      .finally(() => {
        state.popupContent = null
      })
  }

  const fetchSearchhId = (payload) => {
    return axios({
      url: '/api/join/id-search',
      method: 'get',
      params: payload
    })
    .then(async res => {
      const resData = res.data
      if (resData.code === 'C9999') {
        return false
      } else {
        return true
      }
    })
  }

  const fetchDiCheck = (di) =>{
    return axios({
      url: '/api/join/di-check',
      method: 'get',
      params: {
        userDi : di
      }
    }).then(async (res)=>{
      const resData = res.data
      if(resData.code === 'C9999'){
        return null
      } else{
        return resData.data
      }
    })
  }

  const fetchPassChange = (payload) => {
    return axios({
      url: '/api/join/pass-change',
      method: 'put',
      data: payload
    })
    .then(async res => {
      const resData = res.data
      if (resData.data !== undefined) {
        if (resData.code === 'C9999') {
          return false
        } else {
          return true
        }
      } else {
        await openAsyncAlert({ message: t(resData.message) })
      }
    })
  }

  const fetchPassChangeNextTime = () => {
    return axios({
      url: '/api/join/pass-change-next-time',
      method: 'put'
    })
    .then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        return true
      } else {
        await openAsyncAlert({ message: t(resData.message) })
        return false
      }
    })
  }

  const saveDiHistory = (di) => {
    return axios({
      url: '/api/join/di-history',
      method: 'post',
      data: {
        userDi: di
      }
    })
  }

  return {
    ...toRefs(state),
    fnOpenPopup,
    fetchDiCheck,
    fetchSearchhId,
    fetchPassChange,
    fetchPassChangeNextTime,
    saveDiHistory
  }
}


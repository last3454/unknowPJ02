import axios from '@/utils/customAxios'
import { reactive, toRefs } from 'vue'
import { inject } from 'vue'
import { useActions } from 'vuex-composition-helpers'

export const useJoin = () => {
  const state = reactive({
    params: {
      stepSeq: 1,
      userNm: '',
      userDi: '',
      mobileNo: '',
      masterFlag: 'N'
    },
    step1: {
      stepSeq: 2,
      userNm: '',
      userDi: '',
      mobileNo: '',
      registerAdrr1: '',
      registerAdrr2: ''
    },
    step2: {
      stepSeq: 3,
      userDi: '',
      userNm: '',
      mobileNo: '',
      loginId: '',
      loginPw: '',
      loginPwCopy: '',
      loginPwTemp: '',
      email: '',
      compNm: '',
      compCd: '',
      compDeptNm: '',
      isMailCheck: true,
      isCheck: true,
      isAgree: 'N',
      masterFlag: 'N'
    },
    compParams : {
      masterFlag: 'Y',
      compSeq : '',
      compNm : '',
      compEnm : '',
      compNum : '',
      compPhone : '',
      compSite : '',
      compZip : '',
      compAddr : '',
      compEnaddr: '',
      compContent: '',
      compApUserId: '',
      compApUserNm: '',
      compRepresentNm: '',
      regUserCd: '',
      files: []
    },
    popupContent: ''
  })

  const t = inject('t')
  const { openAsyncAlert, openAsyncPopup } = useActions(['openAsyncAlert', 'openAsyncPopup'])

  const fetchIdCheck = (payload) => {
    return axios({
      url: '/api/join/id-check',
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

  const fetchEmailCheck = (payload) => {
    return axios({
      url: '/api/join/email-check',
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

  const fetchJoin = (payload) => {
    return axios({
      method: 'post',
      url: '/api/join/signup',
      data: payload,
      isLoading:true
    }).then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        await openAsyncAlert({ message: t('join.msg.desc28') })
        return resData
      } else {
        await openAsyncAlert({ message: t('join.msg.desc29') })
        return false
      }
    })
  }

  const fnOpenPopup = (compNm) => {
    state.popupContent = compNm

    openAsyncPopup({ minWidth: 800 })
      .then(res => {
        if (compNm === 'CompanySearchPop') {
          if (res.emitValue) {
            const data = res.emitValue
            state.compParams = data
            state.step2.compNm = data.compNm ? data.compNm : ''
            state.step2.compCd = data.compCd ? data.compCd : ''
            state.step2.masterFlag = data.masterFlag
          }
        } else {
          if (res.emitValue) {
            const data = res.emitValue
            state.step2.isAgree = data
          }
        }
      })
      .catch(err => {
        console.log(err)
      })
      .finally(() => {
        state.popupContent = null
      })
  }

  return {
    ...toRefs(state),
    fetchJoin,
    fetchIdCheck,
    fetchEmailCheck,
    fnOpenPopup
  }
}
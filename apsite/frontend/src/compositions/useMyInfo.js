import axios from '@/utils/customAxios'
import router from '../router'
import { useStore } from 'vuex'
import { inject, reactive, toRefs } from 'vue'
import { useActions } from 'vuex-composition-helpers'

export const useMyInfo = () => {
  const state = reactive({
    // 개인정보
    personalInfo: {
      // 유저 회사 코드
      compCd: '',
      // 유저 이메일
      email: '',
      // 유저 로그인 아이디
      loginId: '',
      // 유저 소속
      userCompDeptNm: '',
      // 유저 master 여부
      masterFlag: '',
      // 유저 핸드폰 번호
      mobileNo: '',
      // 유저 코드
      userCd: '',
      // 유저 이름
      userNm: ''
    },
    // 업체정보
    companyInfo: {},
    // 메인 업체 계정 정보
    mainCompUserList: [],
    // 팝업 관련 Data
    popupContent: null,
    popParams: {},
    popFunc: null,
  })

  // 전역변수 설정
  const t = inject('t')
  const store = useStore()
  const commonUtils = inject('commonUtils')
  const { openAsyncConfirm, openAsyncAlert, openAsyncPopup, closeAsyncPopup } = useActions(['openAsyncConfirm', 'openAsyncAlert', 'openAsyncPopup', 'closeAsyncPopup'])

//////[S] Axios //////////////////////////////////////////////////////////////////////

  // 가입정보 상세
  const fetchProfileInfo = () => {
    return axios({
        url: '/api/myInfo/profile',
        method: 'get'
      })
      .then(res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          const result = resData.data

          if (result && result.myInfo) {
            state.personalInfo = result.myInfo
          }

          if (result && result.compInfo) {
            state.companyInfo = result.compInfo
          }
          
        } else {
          alert(resData.message)
        }
        return res
      })
  }

  // 가입정보 수정
  const updateMyInfoP = (payload) => {
    return axios({
      url: '/api/myInfo/profile',
      method: 'put',
      data: payload
    })
  }

  // 소속회사 정보 수정
  const updateMyInfoC = (payload) => {
    return axios({
      url: '/api/myInfo/comp',
      method: 'put',
      data: payload
    })
  }

  // 소속회사 계정정보 리스트
  const fetchCompanyList = () => {
    return axios({
        url: '/api/myInfo',
        method: 'get'
      })
      .then(res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          resData.data.list.forEach(item => {
            item.mobileNo = commonUtils.phoneFormat(item.mobileNo)
            item.regDtm = commonUtils.convertDt(item.regDtm)
          })
          state.mainCompUserList = resData.data.list
        } else {
          alert(resData.message)
        }
        return res
      })
  }

  // 회원 탈퇴
  const fetchPersonalRetire = (userNm) => {
    return axios({
      method: 'put',
      url: '/api/myInfo/personal-retire'
    }).then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        return true
      } else {
        openAsyncAlert({ message: t(resData.message, { userNm }) })
      }
    })
  }

//////[S] Function //////////////////////////////////////////////////////////////////////

  // 팝업 관련
  const fnChkPwdPop = () => {
    // 비밀번호 체크 팝업
    fnOpenPopup('ChkPwdPop')
  }

  const fnChgPwdPop = () => {
    // 비밀번호 변경 팝업
    fnOpenPopup('UserPassChangePop')
  }

  const fnOpenPopup = (compNm) => {
    state.popupContent =  compNm

    openAsyncPopup({ minWidth: compNm === 'ChkPwdPop' ? 400 : 800 })
      .then(res => {
      })
      .catch(err => {
      })
      .finally(() => {
        state.popupContent = null
      })
  }

  // 가입 정보 수정처리
  const fnModifyInfo = (flag, pwd) => {
    if (flag === 'P') {
      const info = state.personalInfo
      const params = {
        email: info.email,
        mobileNo: info.mobileNo,
        userCompDeptNm: info.userCompDeptNm,
        pwd: pwd
      }

      updateMyInfoP(params)
            .then(async (res) => {
              const resData = res.data
              if (resData.code === 'C0000') {
                await openAsyncAlert({ message: t('common.msg.modify_ok') }) // 수정 완료되었습니다.
                closeAsyncPopup({ message: '닫기' })
                router.push({
                  path: '/myInfop/view'
                })
              } else {
                openAsyncAlert({ message: t(resData.message) })
              }
      })
    } else {
      const info = state.companyInfo
      const params = {
        compAddr: info.compAddr,
        compApUserId: info.compApUserId,
        compContent: info.compContent,
        compEnaddr: info.compEnaddr,
        compEnm: info.compEnm,
        compRepresentNm: info.compRepresentNm,
        compNm: info.compNm,
        compNum: info.compNum,
        compPhone: info.compPhone,
        compSapCd: info.compSapCd,
        compSite: info.compSite,
        compZip: info.compZip,
        files: info.files,
        pwd: pwd
      }
      updateMyInfoC(params)
            .then(async (res) => {
              const resData = res.data
              if (resData.code === 'C0000') {
                await openAsyncAlert({ message: t('common.msg.modify_ok') }) // 수정 완료되었습니다.
                closeAsyncPopup({ message: '닫기' })
                router.push({
                  path: '/myInfoc/view'
                })
              } else {
                openAsyncAlert({ message: t(resData.message) })
              }
      })
    }
  }

  const fnPersonalRetire = async (userNm) => {
    const message = t('company.msg.desc28',{ userNm })

    if (!await openAsyncConfirm({ message })) {
      return
    }

    fetchPersonalRetire(userNm).then(async (flag) => {
      if(flag){
        await openAsyncAlert({ message: t('company.msg.desc29') })
        fnLogout()
      }
    })
  }

  // 취소 시 상세화면으로 이동
  const onCancel = async (flag) => {
    const path = flag === 'P' ? '/myInfop/view' : '/myInfoc/view'
    // 입력하신 내용을 취소하시겠습니까?
    if(await openAsyncConfirm({message: t('myinfo.msg.profile.desc6') })) {
      router.push({
        path: path
      })
    }
  }

  // 로그아웃
  const fnLogout = () => {
    store.dispatch('signout')
      .then(res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          router.push({ path: '/login' })
        } else {
          alert(res.data.message)
        }
      })
  }

  const goModify = () => {
    router.push({ path: '/myInfop/modify'})
  }

  const goCompModify = () => {
    router.push({ path: '/myInfoc/modify'})
  }

  return {
    t,
    ...toRefs(state),
    fetchProfileInfo,
    fnChkPwdPop,
    fnChgPwdPop,
    goModify,
    goCompModify,
    fnModifyInfo,
    fnPersonalRetire,
    fetchCompanyList,
    onCancel
  }
}
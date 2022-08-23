import axios from '@/utils/customAxios'
import { inject, reactive, toRefs } from 'vue'
import router from '../router'
import { useActions } from 'vuex-composition-helpers'

export const useCompany = () => {
    const state = reactive({
      list: [],
      managelist: [],
      page: {},
      params : {
        compSeq : '',
        compNm : '',
        compEnm : '',
        compNum : '',
        compPhone : '',
        compSite : '',
        compZip : '',
        compAddr : '',
        compEnaddr: '',
        compSapCd: '',
        compContent: '',
        compApUserId: '',
        compApUserNm: '',
        compRepresentNm: '',
        regUserCd: '',
        files: []
      },
      companyInfo: {}
    })

    const fnParamsInit = () => {
      state.params = {
        compSeq : '',
        compNm : '',
        compEnm : '',
        compNum : '',
        compPhone : '',
        compSite : '',
        compZip : '',
        compAddr : '',
        compEnaddr: '',
        compSapCd: '',
        compContent: '',
        compApUserId: '',
        compApUserNm: '',
        compRepresentNm: '',
        regUserCd: '',
        files: []
      }
    }

    //아래서 사용할 전역변수 사용
    const t = inject('t')
    const { openAsyncAlert } = useActions(['openAsyncAlert'])
    const fnLinkReg = (seq) => {
      if(seq){
        return router.push({ name: 'company-modify', query: { seq : (seq || '') } }).catch(() => {})
      }else{
        return router.push({ name: 'company-register', query: { seq : (seq || '') } }).catch(() => {})
      }
    }

    const fnLinkView = (seq) => {
      return router.push({ name: 'company-view', query: { seq } }).catch(() => {})
    }

    const fnLinkList = () => {
      return router.push({ name: 'company-list', query: null }).catch(() => {})
    }

    const fetchCompCheck = async (payload) => {
      if(!payload.keyword){
        await openAsyncAlert({ message: t('company.msg.desc11') }) // 회사명 또는 사업자 등록번호를 입력해주세요.
        return
      }

      return axios({
        url: '/api/comp/compCheck',
        method: 'get',
        params: payload,
        isLoading:true
      })
      .then(async res => {
        const resData = res.data
        if (resData.code === 'C9999') {
          if(!payload.noAlertFlag){
            await openAsyncAlert({ message: t('company.msg.desc12') }) // 이미 등록되어 있는 업체 입니다
          }
          return false
        } else {
          if(!payload.noAlertFlag){
            await openAsyncAlert({ message: t('company.msg.desc13') }) // 등록 가능한 업체 입니다
          }
          return true
        }
      })
    }

    const fetchCompany = (compSeq) => {
      return axios({
        url: `/api/comp/${compSeq}`,
        method: 'get'
      })
      .then(res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          state.params = resData.data
        } else {
          alert(resData.message)
        }
      })
    }

    const fetchCompanys = (payload) => {
      return axios({
        url: '/api/comp',
        method: 'get',
        params: payload
      })
      .then(res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          state.page = resData.data.page
          state.list = resData.data.list
          if (resData.data.list) {
            return true
          } else {
            return false
          }
        } else {
          alert(resData.message)
        }
      })
    }

    const fetchSaveCompany = (payload) => {
      return axios({
        method: 'post',
        url: '/api/comp',
        data: payload,
        isLoading: true
      }).then(async res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          if(!payload.masterFlag){
            await openAsyncAlert({ message: t('common.msg.save_ok') }) // 저장 되었습니다.
          }
          return resData
        } else {
          alert(resData.message)
        }
      }).catch(function (error) {
        console.log(error)
      })
    }

    const fetchUpdateCompany = (payload) => {
      return axios({
        method: 'put',
        url: `/api/comp/${payload.compSeq}`,
        data: payload
      }).then(async res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          await openAsyncAlert({ message: t('common.msg.modify_ok') }) // 수정 되었습니다.
          return payload.compSeq
        } else {
          alert(resData.message)
        }
      }).catch(function (error) {
        console.log(error)
      })
    }

    const fetchDelete = (payload) =>{
      return axios({
        method: 'put',
        url: `/api/comp/delete`,
        data: payload
      }).then(async res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          return true
        } else {
          return true
        }
      }).catch(function (error) {
        console.log(error)
      })
    }

    const fetchApproval = (payload) =>{
      return axios({
        method: 'put',
        url: `/api/comp/approval`,
        data: payload
      }).then(async res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          await openAsyncAlert({ message: t('common.msg.process_ok') }) // 처리 되었습니다.
          return payload.compSeq
        } else {
          alert(resData.message)
        }
      }).catch(function (error) {
        console.log(error)
      })
    }

    const fetchMaster = (payload) => {
      return axios({
        method: 'put',
        url: `/api/comp/master`,
        data: payload
      }).then(async res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          return payload.compSeq
        } else {
          alert(resData.message)
        }
      }).catch(function (error) {
        console.log(error)
      })
    }

    const fetchRetire = (userCd, userNm) => {
      return axios({
        method: 'put',
        url: '/api/comp/retire',
        data : {
          compUserCd: userCd
        }
      }).then(async res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          return true
        } else {
          openAsyncAlert({ message: t(resData.message, { userNm }) })
        }
      })
    }

    const fnZipPop = () => {
      // eslint-disable-next-line no-undef
      new daum.Postcode({
          oncomplete: function(data) {
            let addr = ''
            let enAddr = ''
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
              addr = data.roadAddress
              enAddr = data.addressEnglish
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
              addr = data.jibunAddress
              enAddr = data.jibunAddressEnglish
            }
            state.params.compZip = data.zonecode
            state.params.compAddr = addr
            state.params.compEnaddr = enAddr
          }
      }).open({
        left: (window.screen.width / 2) - (500 / 2),
        top: (window.screen.height / 2) - (600 / 2)
      })
    }

    const fetchResearch = (userNm) => {
      return axios({
        url: '/api/join/rsearch',
        method: 'get',
        params: {
          userNm : userNm
        }
      })
      .then(async res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          return resData
          //rSearchParam.list = resData.data
        }
      })
    }

    //[S] 업체 - 마스터를 위해 필요한 기능
    const fnManagerLinkReg = (seq) => {
      if(seq){
        return router.push({ name: 'companym-modify', query: { seq : (seq || '') } }).catch(() => {})
      }else{
        return router.push({ name: 'companym-register', query: { seq : (seq || '') } }).catch(() => {})
      }
    }

    const fnManagerLinkList = (seq) => {
      return router.push({ name: 'companym-list', query: { seq } }).catch(() => {})
    }
    const fnManagerLinkView = (seq) => {
      return router.push({ name: 'companym-view', query: { seq } }).catch(() => {})
    }

    const fetchManagerCompanys = (payload) => {
      return axios({
        url: '/api/comp/manager',
        method: 'get',
        params: payload
      })
      .then(res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          state.page = resData.data.page
          state.list = resData.data.managerList
          if (resData.data.list) {
            return true
          } else {
            return false
          }
        } else {
          alert(resData.message)
        }
      })
    }
    //[E] 업체 - 마스터를 위해 필요한 기능

    // 소속회사정보 상세
    const fetchCompanyInfo = () => {
      return axios({
          url: '/api/comp/my-company',
          method: 'get'
        })
        .then(res => {
          const resData = res.data
          if (resData.code === 'C0000') {
            state.companyInfo = resData.data
          } else {
            alert(resData.message)
          }
          return res
        })
    }

    return {
      ...toRefs(state),
      fnParamsInit,
      fnZipPop,
      fnLinkList,
      fnLinkView,
      fnLinkReg,
      fnManagerLinkList,
      fnManagerLinkView,
      fnManagerLinkReg,
      fetchDelete,
      fetchRetire,
      fetchMaster,
      fetchApproval,
      fetchSaveCompany,
      fetchUpdateCompany,
      fetchCompany,
      fetchCompanys,
      fetchCompCheck,
      fetchResearch,
      fetchManagerCompanys,
      fetchCompanyInfo
    }
  }
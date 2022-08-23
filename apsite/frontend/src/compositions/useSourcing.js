import axios from '@/utils/customAxios'
import router from '../router'
import { useStore } from 'vuex'
import { reactive, toRefs, inject } from 'vue'
import { useActions } from 'vuex-composition-helpers'
import sanitizeHtml from '@/utils/sanitizeHtml'

// 밸리데이션 관련
import validation from '@/utils/validation'
import { useForm, useField } from 'vee-validate'
import * as yup from 'yup'

export const useSourcing = () => {
  const state = reactive({
    sourcingList: [],
    page: {},
    isAdmin: 'N', // 관리자 여부
    isApUser: 'N', // AP연구원 여부
    params: {
      // 구분
      seq: 0,
      // 제목
      title: '',
      // 내용
      content: '',
      // 공개여부
      openAllYn: 'N',
      // 공개 대상 원료사 정보
      openRawCompInfo : [],
      // 조회수
      viewCnt: 0,
      // 작성자
      regUserNm: '',
      regUserCd: '',
      // 작성일
      regDtm: '',
      // 게시여부
      postStatus: 'N',
      // 파일
      files: [],
      // 원료명
      openRawCompNm: '',
      openRawCompEnm: ''
    },
    // 팝업 관련 Data
    popupContent: null,
    popParams: {},
    popSelectFunc: null
  })

  // 전역변수 설정
  const t = inject('t')
  const store = useStore()
  const commonUtils = inject('commonUtils')
  const { openAsyncConfirm, openAsyncAlert, openAsyncPopup, closeAsyncPopup } = useActions(['openAsyncConfirm', 'openAsyncAlert', 'openAsyncPopup', 'closeAsyncPopup'])

  let isSaving = false

  // 로그인 정보 셋팅
  state.isApUser = commonUtils.checkAuth('SGG000003|SGG000007|SGG000011|SGG000012|SGG000013|SGG000014') === true ? 'Y' : 'N'

//////[S] Router /////////////////////////////////////////////////////////////////////

// 등록
const goRegister = () => {
  router.push({ path: '/sourcing/register' })
}

// 페이지 관련
const goPageNum = (pg) => {
  console.log(pg)
}

// 목록
const goList = () => {
  router.push({ path: '/sourcing/list' })
}

// 수정
const fnModify = (seq) => {
  router.push({ path: '/sourcing/modify', query: { seq: seq } })
}

// 취소 버튼
const fnCancel = (modifyYn, seq) => {
  if (modifyYn === 'Y') {
    router.push({
      path: '/sourcing/view',
      query: { seq: seq },
    })
  } else {
    router.push({ path: '/sourcing/list' })
  }
}

//////[S] Validation /////////////////////////////////////////////////////////////////
  validation.init()

  const schema = yup.object().shape({
    title: yup.string().required()
  })

  const { handleSubmit, errors } = useForm({
    validationSchema: schema
  })

  const useFieldSet = ['title']
  useFieldSet.forEach(str => {
    useField(str)
  })

  function onInvalidSubmit({ values, errors, results }) {
    //validation 체크 걸렸을때 들어오는 function
    console.log(values); // current form values
  }

  const fnAction = handleSubmit(async values => {
    //validation 체크가 성공 후 들어오는 부분
    const msg = (state.params.seq ? t('board.msg.sourcing.desc7') : t('board.msg.sourcing.desc8'))
    if (!await openAsyncConfirm({ message : msg })) {
      return
    } else {
      fnSave()
    }

  }, onInvalidSubmit)

//////[S] Axios //////////////////////////////////////////////////////////////////////

  // 원료소싱 목록
  const fetchSourcing = (payload) => {
    return axios({
        url: '/api/sourcing',
        method: 'get',
        params: payload
      })
      .then(res => {
        const resData = res.data
        const sourcingList = resData.data.list
        if (resData.code === 'C0000') {
          state.sourcingList = sourcingList
          state.page = resData.data.page
        } else {
          alert(resData.message)
        }
      })
  }

  // 원료소싱 상세
  const fetchSourcingView = (seq) => {
    return axios({
        url: `/api/sourcing/${seq}`,
        method: 'get'
      })
      .then(res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          state.params = resData.data.sourcingInfo
          state.params.openRawCompInfo = resData.data.openRawCompInfo
          state.params.content = sanitizeHtml(state.params.content)
          let seq = 0
          state.params.openRawCompInfo.forEach(item => {
          if (seq > 0) {
            state.params.openRawCompNm += ', ' + item.compNm
            state.params.openRawCompEnm += ', ' + item.compEnm
          } else {
            state.params.openRawCompNm = item.compNm
            state.params.openRawCompEnm = item.compEnm
          }

          seq++
        })

        } else {
          alert(resData.message)
        }
        return res
      })
  }

  // 공개대상 원료사 조회
  const fnSearchComp = (payload) => {
    return axios({
        url: '/api/sourcing/searchComp',
        method: 'get',
        params: payload
      })
      .then(res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          return resData
        }
      })
  }

  // 원료소싱 등록
  const insertSourcing = (payload) => {
    return axios({
      url: '/api/sourcing',
      method: 'post',
      data: payload
    })
  }

  // 원료소싱 수정
  const updateSourcing = (payload) => {
    return axios({
      url: '/api/sourcing/' + payload.seq,
      method: 'put',
      data: payload
    })
  }

  // 원료소싱 삭제
  const deleteSourcing = (seq) => {
    return axios({
      url: '/api/sourcing/' + seq,
      method: 'delete'
    })
  }

  // 조회수 증가
  const updateViewCnt = (seq) => {
    return axios({
      url: '/api/sourcing/viewCnt/' + seq,
      method: 'put'
    })
  }

  // 게시 처리
  const updatePost = (seq) => {
    return axios({
      url: '/api/sourcing/post/' + seq,
      method: 'put'
    })
  }

  // 게시 취소 처리
  const cancelPost = (seq) => {
    return axios({
      url: '/api/sourcing/cancelPost/' + seq,
      method: 'put'
    })
  }

//////[S] Function //////////////////////////////////////////////////////////////////////

  // 원료소싱 등록/수정
  const fnTempSave = async (uploadParams) => {
    // 첨부파일 처리
    state.params.files = uploadParams.items

    if (isSaving) {
      return
    }

    if (state.params.openAllYn === 'N' && state.params.openRawCompInfo.length < 1) {
      openAsyncAlert({ message: '공개여부를 설정해주십시오.' }) // 공개여부를 설정해주십시오
      return
    }

    fnAction()
  }

  const fnSave = () => {
    isSaving = true

    if (state.params.seq) {
      // 수정 로직
      // L 1. 수정처리 이후 게시설정 가능.
      //  L 1-1 Y - 게시처리, N - 리스트
      updateSourcing(state.params)
        .then(async (res) => {
          const resData = res.data
          if (resData.code === 'C0000') {
            if (state.isApUser === 'Y' && state.params.postStatus === 'N') {
              fnPost(state.params.seq, state.params.postStatus)
            } else {
              await openAsyncAlert({ message: t('common.msg.modify_ok') }) // 수정 완료되었습니다.
              router.push({
                path: '/sourcing/view',
                query: { seq: state.params.seq }
              })
            }
          } else {
            alert(resData.message)
          }
        })
        .finally( () => {
          isSaving = false
        })
    } else {
      insertSourcing(state.params)
        .then(async (res) => {
          const resData = res.data
          if (resData.code === 'C0000') {
            await openAsyncAlert({ message: t('common.msg.save_ok') }) // 등록 완료되었습니다.
            router.push({
              path: '/sourcing/view',
              query: { seq: resData.data }
            })
          } else {
            alert(resData.message)
          }
        })
        .finally( () => {
          isSaving = false
        })
    }
  }

  // 삭제 버튼
  const fnDelete = async (seq) => {
    if (!await openAsyncConfirm({ message: t('common.msg.delete_confirm_msg') })) { // 정말로 삭제 하시겠습니까?
      return
    }

    deleteSourcing(seq).then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        await openAsyncAlert({ message: t('common.msg.delete_ok') }) // 삭제 되었습니다.
        router.push({ path: '/sourcing/list' })
      } else {
        openAsyncAlert({ message: resData.message })
      }
    })
  }

  // 게시하기
  const fnPost = async (seq, status) => {
    const msg = status === 'Y' ? t('board.msg.sourcing.desc10') : t('board.msg.sourcing.desc11')
    if (!await openAsyncConfirm({ message: msg })) {
      return
    }

    // 게시 상태값 변경
    if (status === 'N') {
      updatePost(seq).then(async res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          await openAsyncAlert({ message: t('board.msg.sourcing.desc12') }) // 게시처리 되었습니다.
            router.push({
              path: '/sourcing/list'
            })
        } else {
          alert(resData.message)
        }
      })
    } else {
      cancelPost(seq).then(async res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          await openAsyncAlert({ message: t('board.msg.sourcing.desc13') }) // 게시취소처리 되었습니다.
              router.push({
                path: '/sourcing/list'
              })
        } else {
          alert(resData.message)
        }
      })
    }
  }

  // 메일 발송
  const fnMailPop = () => {
    fnOpenPopup('RawSourcingInquiryPop')
  }

  // 원료사 팝업
  const fnSearchCompPop = () => {
    const checkedList = []

    state.params.openRawCompInfo.forEach(item => {
      checkedList.push(item)
    })

    state.popParams = {
      isMulti: 'Y',
      checkedList: checkedList
    }

    state.popSelectFunc = getCompInfo

    fnOpenPopup('CompSearchPop')
  }

  const getCompInfo = (obj) => {
    const list = obj.returnObj
    const idx = obj.idx
    let seq  = 0

    state.params.openRawCompNm = ''
    state.params.openRawCompEnm = ''
    state.params.openRawCompInfo = []

    if (list.length > 0) {
      list.forEach(vo => {
        if (seq > 0) {
          state.params.openRawCompNm += ',' + vo.compNm
          state.params.openRawCompEnm += ',' + vo.compEnm
        } else {
          state.params.openRawCompNm = vo.compNm
          state.params.openRawCompEnm = vo.compEnm
        }
        state.params.openRawCompInfo.push(vo)

        seq++
      })
    }
  }

  // 팝업 관련
  const fnOpenPopup = (compNm) => {
    state.popupContent = compNm

    openAsyncPopup({ minWidth: 500 })
      .then(res => {
        console.log(res)
      })
      .catch(err => {
        console.log(err)
      })
      .finally(() => {
        state.popupContent = null
      })
  }

  // 전체 버튼 이벤트
  const fnCngOpenAllYn = () => {
    const tempVal = state.params.openAllYn
    if (tempVal === 'Y') {
      state.params.openRawCompNm = '전체'
      state.params.openRawCompEnm = 'All'
      state.params.openRawCompInfo = []
    }
  }

  return {
    t,
    errors,
    commonUtils,
    ...toRefs(state),
    goRegister,
    goPageNum,
    goList,
    fnModify,
    fnDelete,
    fnTempSave,
    fnSave,
    fnCancel,
    fnCngOpenAllYn,
    updateViewCnt,
    fetchSourcing,
    fetchSourcingView,
    fnSearchComp,
    fnSearchCompPop,
    fnPost,
    fnMailPop
  }
}
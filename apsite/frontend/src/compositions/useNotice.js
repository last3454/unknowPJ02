import axios from '@/utils/customAxios'
import commonUtils from '@/utils/commonUtils'
import sanitizeHtml from '@/utils/sanitizeHtml'
import { reactive, toRefs, inject } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useActions } from 'vuex-composition-helpers'

// 밸리데이션 관련
import validation from '@/utils/validation'
import { useForm, useField } from 'vee-validate'
import * as yup from 'yup'

export const useNotice = () => {
  const state = reactive({
    mainList: [],
    topList: [],
    list: [],
    page: {},
    params: {
      // 구분
      seq: 0,
      // 제목
      title: '',
      // 공지여부
      noticeYn: 'N',
      // 공지 시작일
      noticeStartDt: '',
      // 공지 종료일
      noticeEndDt: '',
      // 내용
      content: '',
      // 파일
      files: [],
      // youtube url
      ytbUrl: ''
    },
    // 팝업 관련
    popupContent: null,
    popParams: {}
  })

  // 전역변수 설정
  const t = inject('t')
  const router = useRouter()
  const { openAsyncConfirm, openAsyncAlert, openAsyncPopup } = useActions(['openAsyncConfirm', 'openAsyncAlert', 'openAsyncPopup'])

  // 관리자 여부 설정
  // const isAdmin = commonUtils.checkAuth('SGG000001')
  const isAdmin = commonUtils.checkAuth('SGG000003|SGG000011|SGG000012|SGG000013|SGG000014')

  // 현재 날짜 가져오기
  const today = new Date()

  let isSaving = false

//////[S] Router /////////////////////////////////////////////////////////////////////

  // 상세
  const fnCancel = (modifyYn, seq) => {
    if (modifyYn === 'Y') {
      router.push({ path: '/notice/view', query: { seq: seq } })
    } else {
      router.push({ path: '/notice/list' })
    }
  }

  // 목록
  const goList = () => {
    router.push({ path: '/notice' })
  }

  // 수정
  const goModify = (seq) => {
    router.push({ path: '/notice/modify', query: { seq: seq } })
  }

  // 등록
  const goRegister = () => {
    router.push({ path: '/notice/register' })
  }

  // 페이지 관련
  const goPageNum = (pg) => {
    console.log(pg)
  }

//////[S] Validation /////////////////////////////////////////////////////////////////

  validation.init()

  const schema = yup.object().shape({
    title: yup.string().required(),
    noticeYn: yup.string(),
    ytbUrl: yup.string(),
    noticeStartDt: yup.string()
      .when(
      /**
      * 1. 공지등록시에만 validation 적용
      * 2. 현재 날짜 이후에만 validation 적용
      **/
      ['noticeYn', 'modifyYn'],
      {
        is: (noticeYn, modifyYn) => noticeYn === 'Y' && modifyYn === 'N',
        then: yup.string().test(
          'noticeStartDt',
          t('board.msg.notice.desc5'), //지정한 날짜가 현재 날짜보다 이전입니다.
          (value) => {
            if (commonUtils.isNotEmpty(value) && commonUtils.isNotEmpty(state.params.noticeEndDt)) {
              return new Promise((resolve) => {
                setTimeout(() => resolve((
                  value >= commonUtils.getToday())
                  && (state.params.noticeEndDt >= commonUtils.getToday())
                ))
              })
            } else {
              return true
            }
          }
        ).required()
      }
    )
  })

  const { handleSubmit, errors } = useForm({
    validationSchema: schema
  })

  const useFieldSet = ['title', 'ytbUrl', 'noticeYn', 'noticeStartDt', 'modifyYn']
  useFieldSet.forEach(str => {
    useField(str)
  })

  function onInvalidSubmit({ values, errors, results }) {
    //validation 체크 걸렸을때 들어오는 function
    console.log(values)
  }

  const fnAction = handleSubmit(async values => {
    //validation 체크가 성공 후 들어오는 부분
    const msg = (state.params.seq ? t('board.msg.notice.desc6') : t('board.msg.notice.desc7'))
    if (!await openAsyncConfirm({ message : msg })) {
      return
    } else {
      fnSave()
    }

  }, onInvalidSubmit)

//////[S] Axios /////////////////////////////////////////////////////////////////////

  // 메인화면 공지
  const fetchMainBoards = () => {
    return axios({
      url: '/api/notices/main',
      method: 'get',
      params: { }
    })
    .then(res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        state.mainList = resData.data
        state.mainList.forEach(item => {
          const getId = item.ytbUrl
          item.ytbUrl = getId ? `https://www.youtube.com/embed/${getId}` : ''
        })
      } else {
        alert(resData.message)
      }
    })
  }

  // 상세
  const fetchBoard = (seq) => {
    return axios({
        url: `/api/notices/${seq}`,
        method: 'get'
      })
      .then(res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          state.params = resData.data
          state.params.content = sanitizeHtml(state.params.content)
          const getId = state.params.ytbUrl
          state.params.ytbUrl = getId ? `https://www.youtube.com/embed/${getId}` : ''
        } else {
          alert(resData.message)
        }
        return res
      })
  }

  // 목록
  const fetchBoards = (payload) => {
    return axios({
        url: '/api/notices',
        method: 'get',
        params: payload
      })
      .then(res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          state.topList = resData.data.topList
          state.list = resData.data.list
          state.page = resData.data.page
        } else {
          alert(resData.message)
        }
      })
  }

  // 등록
  const insertNotice = (payload) => {
    return axios({
        url: '/api/notices',
        method: 'post',
        data: payload
      })
  }

  // 수정
  const updateNotice = (payload) => {
    return axios({
        url: '/api/notices/' + payload.seq,
        method: 'put',
        data: payload
      })
  }

  // 조회수 변경
  const updateNoticeViewCnt = (seq) => {
    return axios({
        url: '/api/notices/' + seq + '/view-cnt',
        method: 'put'
      })
  }

  // 삭제
  const deleteNotice = (seq) => {
    return axios({
        url: '/api/notices/' + seq,
        method: 'delete'
      })
  }

  //////[S] Function //////////////////////////////////////////////////////////////////////

  // 팝업 관련
  const fnYtbPop = (url, title) => {
    state.popParams.popUrl = url
    state.popParams.title = title
    fnOpenPopup('NoticeYtbPop')
  }

  const fnOpenPopup = (compNm) => {
    state.popupContent = compNm

    openAsyncPopup({ minWidth: 800 })
      .then(res => {
        console.log(res)
      })
      .catch(err => {
        console.log(err)
      })
      .finally(() => {
        state.popupContent = null
        state.url = ''
      })
  }

  // 공지사항 등록/수정
  const fnTempSave = async (uploadParams) => {
    // 첨부파일 처리
    state.params.files = uploadParams.items

    if (isSaving) {
      return
    }

    fnAction()
  }

  const fnSave = () => {
    if (state.params.seq) {
      updateNotice(state.params)
        .then(async res => {
          const resData = res.data
          if (resData.code === 'C0000') {
            await openAsyncAlert({ message: t('board.msg.notice.desc8') }) // 수정이 완료되었습니다
            router.push({ path: '/notice/view', query: { seq: state.params.seq } })
          } else {
            alert(resData.message)
          }
        })
        .finally( () => {
          isSaving = false
        })
    } else {
      insertNotice(state.params)
        .then(async res => {
          const resData = res.data
          if (resData.code === 'C0000') {
            await openAsyncAlert({ message: t('board.msg.notice.desc9') }) // 등록이 완료되었습니다
            router.push({ path: '/notice' })
          } else {
            alert(resData.message)
          }
        })
        .finally( () => {
          isSaving = false
        })
    }
  }

  // 삭제 버튼 이벤트
  const onDelete = async (seq) => {
    if (!await openAsyncConfirm({ message: t('common.msg.delete_confirm_msg') })) { // 정말로 삭제 하시겠습니까?
      return
    }

    deleteNotice(seq).then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        await openAsyncAlert({ message: t('common.msg.delete_ok') }) // 삭제 되었습니다.
        router.push({ path: '/notice/list' })
      } else {
        openAsyncAlert({ message: resData.message })
      }
    })
  }

  return {
    t,
    errors,
    isAdmin,
    commonUtils,
    ...toRefs(state),
    fnTempSave,
    goList,
    goModify,
    fnCancel,
    fnYtbPop,
    onDelete,
    fetchBoard,
    fetchBoards,
    goRegister,
    goPageNum,
    updateNoticeViewCnt,
    fetchMainBoards
  }
}
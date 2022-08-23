import axios from '@/utils/customAxios'
import { reactive, toRefs, inject } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { useActions } from 'vuex-composition-helpers'
import sanitizeHtml from '@/utils/sanitizeHtml'

export const useErrorInq = () => {
  const state = reactive({
    list: [],
    page: {},
    params: {
      seq: 0,
      menuCtg1: '',
      menuCtg2: '',
      title: '',
      content: '',
      files: []
    },
    // 메뉴 Depth 리스트
    menu1List: [],
    menu2List: [],
    // Commuication 영역 리스트
    commuList: []
  })

  // 전역변수 설정
  const t = inject('t')
  const route = useRoute()
  const router = useRouter()
  const store = useStore()
  const commonUtils = inject('commonUtils')
  const myInfo = store.getters.getMyInfo()
  const { openAsyncConfirm, openAsyncAlert, openAsyncPopup, closeAsyncPopup } = useActions(['openAsyncConfirm', 'openAsyncAlert', 'openAsyncPopup', 'closeAsyncPopup'])
  let list = []

  // 메뉴 리스트 insert
  store.dispatch('findRouter', myInfo.groups).then(async resData => {
    if (resData.data.code === 'C0000') {
      list = resData.data.data
      state.menu1List = list.filter(item => item.level === 2)
      fnSetMenu2(state.params.menuCtg2)
    }
  })

//////[S] Axios /////////////////////////////////////////////////////////////////////

  // 목록
  const fetchBoards = async (payload) => {
    return await axios({
        url: '/api/errorinq',
        method: 'get',
        params: payload
      })
      .then(res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          state.list = resData.data.list
          state.page = resData.data.page
        } else {
          alert(resData.message)
        }
      })
  }

  // 상세보기
  const fetchBoard = (seq) => {
    return axios({
        url: `/api/errorinq/${seq}`,
        method: 'get'
      })
      .then(res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          state.params = resData.data
          state.params.content = sanitizeHtml(state.params.content)        
          fetchComment(seq)
        } else {
          alert(resData.message)
        }
        return res
      })
  }

  // 등록
  const insertErrorInq = (payload) => {
    return axios({
        url: '/api/errorinq',
        method: 'post',
        data: payload
      })
  }

  // 조회수 변경
  const updateErrorInqViewCnt = (seq) => {
    return axios({
        url: '/api/errorinq/' + seq + '/view-cnt',
        method: 'put'
      })
  }

  // 삭제
  const deleteErrorInq = (seq) => {
    return axios({
        url: '/api/errorinq/' + seq,
        method: 'delete'
      })
  }

  // 수정
  const updateErrorInq = (payload) => {
    return axios({
        url: '/api/errorinq/' + payload.seq,
        method: 'put',
        data: payload
      })
  }

  // 답변상태 바꾸기
  const fnAnswerStatus = (seq) => {
    return axios({
      url: '/api/errorinq/answer/' + seq,
      method: 'put'
    })
  }

  // 댓글 목록 가져오기
  const fetchComment = async (seq) => {
    return await axios({
      url: '/api/errorinq/' + seq + '/commu',
      method: 'get'
    })
    .then(res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        state.commuList = resData.data
      } else {
        alert(resData.message)
      }
    })
  }

  // 댓글 등록
  const fnInsertComment = (payload) => {
    return axios({
      url: '/api/errorinq/' + payload.seq + '/commu',
      method: 'post',
      isLoading: true,
      data: payload
    })
  }

  // 댓글 삭제
  const fnDeleteComment = (payload) => {
    return axios({
      url: '/api/errorinq/' + payload.comSeq + '/commu',
      method: 'delete',
      data: payload,
      isLoading: true
    })
  }

  // [S] Function
  const fnAnswer = async (seq) => {
    if (!await openAsyncConfirm({ message: t('board.msg.errorinq.answer_msg1') })) { // 답변처리 하시겠습니까?
      return
    }

    fnAnswerStatus(seq).then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        await openAsyncAlert({ message: t('board.msg.errorinq.answer_msg2') }) // 답변처리 되었습니다.
        router.push({ path: '/errorinq/list' })
      } else {
        openAsyncAlert({ message: resData.message })
      }
    })
  }

  const fnSetMenu2 = (menuCtg2) => {
    if (commonUtils.isEmpty(menuCtg2)) {
      // 등록
      if (state.params.menuCtg1 !== '') {
        state.menu2List = list.filter(item => item.umenuid === state.params.menuCtg1)
      } else {
        state.menu2List = []
      }

      state.params.menuCtg2 = ''
    } else {
      // 수정
      state.menu2List = list.filter(item => item.umenuid === state.params.menuCtg1)
      state.params.menuCtg2 = menuCtg2
    }
  }

  return {
    ...toRefs(state),
    fetchBoards,
    fetchBoard,
    fnAnswer,
    fnSetMenu2,
    fetchComment,
    fnDeleteComment,
    fnInsertComment,
    insertErrorInq,
    updateErrorInqViewCnt,
    deleteErrorInq,
    updateErrorInq
  }
}
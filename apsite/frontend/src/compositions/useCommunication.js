
import { reactive, toRefs, inject } from 'vue'
import { useStore } from 'vuex'
import { useCode } from '@/compositions/useCode'
import { useRoute, useRouter } from 'vue-router'
import { useActions } from 'vuex-composition-helpers'
import { useIngredient } from '@/compositions/useIngredient'
import axios from '@/utils/customAxios'
import sanitizeHtml from '@/utils/sanitizeHtml'

export const useCommunication = () => {
    const state = reactive({
      commentList: [],
      mainCmmtList: [],
      params: {
        // 순번
        seq: 0,
        // 원료 레코드 번호
        recordCd: '',
        // 문의 영역 코드
        stepCd: '',
        // 단계 코드
        stepSeq: '',
        // 내부 댓글 여부
        apYn: 'N',
        // 비밀 댓글 여부
        scretYn: 'N',
        // 댓글 내용
        cmmtCont: '',
        // 댓글 단계코드
        cmmtSeq: '',
        // 답글 여부
        cmmtFlag: 'N',
        // 메인메뉴 플래그
        menuFlag: 'R'
      },
      // 화면 여부
      isShow: false,
      // AP 유저 플래그
      isApUser: false,
      // 댓글 플래그
      chkFlag: false
    })

    // 전역변수 설정
    const t = inject('t')
    const route = useRoute()
    const router = useRouter()
    const commonUtils = inject('commonUtils')
    const store = useStore()
    const myInfo = store.getters.getMyInfo()
    const { fetchCodeGroupMaps, codeGroupMaps } = useCode()
    const { openAsyncConfirm, openAsyncAlert } = useActions(['openAsyncConfirm', 'openAsyncAlert'])
    const { fnDetail } = useIngredient()

    // 로그인 정보 셋팅
    state.isApUser = myInfo.compCd !== 'AP' ? false : true

//////[S] Router //////////////////////////////////////////////////////////////////////

    const fnIngDetail = (recordCd) => {
      return router.push({ name: 'ingredient-list' }).catch(() => {})
    }

//////[S] Axios //////////////////////////////////////////////////////////////////////

    // 원료등록 Comment 목록
    const fetchComments = (payload) => {
      return axios({
        url: '/api/communi',
        method: 'get',
        params: payload
      })
      .then(res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          fnSetFlag(resData.data, 'C')
        } else {
          alert(resData.message)
        }
      })
    }

    // [메인] 원료등록 Comment 목록
    const fetchMainComments = (payload) => {
      return axios({
        url: '/api/communi/main',
        method: 'get',
        params: payload
      })
      .then(res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          fnSetFlag(resData.data, 'M')
        } else {
          alert(resData.message)
        }
      })
    }

    // Comment 등록
    const fnSaveComment = async (payload) => {
      return axios({
        method: 'post',
        url: '/api/communi',
        data: payload
      }).then(res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          // 초기화
          state.params.apYn = 'N'
          state.params.scretYn = 'N'
          state.params.cmmtCont = ''
          state.params.stepCd = payload.stepCd

          const tempParams =
          {
            stepSeq: payload.stepSeq,
            recordCd: payload.recordCd,
          }

          fetchComments(tempParams)

        } else {
          alert(resData.message)
        }
      }).catch(function (error) {
        console.log(error)
      })
    }

//////[S] Function ///////////////////////////////////////////////////////////////////

    // 토글이벤트
    const toggleEvent = () => {
      state.isShow = state.isShow === true ? false : true
    }

    // 메인 메뉴 이동 이벤트
    const fnChgFlag = (flag) => {
      state.params.menuFlag = flag
    }

    // 답글등록 버튼 이벤트
    const fnInsertReply = (vo) => {
      if (state.params.seq === vo.seq && state.params.cmmtFlag === 'Y') {
        state.chkFlag = false
        state.params.scretYn = 'N'
        state.params.apYn = 'N'
        state.params.cmmtFlag = 'N'
        state.params.seq = 0
      } else {
        if (vo.scretYn === 'Y') {
          state.chkFlag = true
          state.params.scretYn = 'Y'
        } else if (vo.apYn === 'Y') {
          state.chkFlag = true
          state.params.apYn = 'Y'
        }
        state.params.cmmtFlag = 'Y'
        state.params.seq = vo.seq
        state.params.stepCd = vo.stepCd
      }
    }

    // [메인] 커뮤니케이션 이동 이벤트
    const fnMoveCommu = async (item) => {
      if (commonUtils.isNotEmpty(item)) {
        await fnDetail(item.recordCd, true)
      } else {
        await openAsyncAlert({ message: t('main.msg.communication.no_move') }) // 해당 댓글로 이동할 수 없습니다.
        return
      }
    }

    // 화면 플래그 이벤트
    const fnSetFlag = async (list, flag) => {
      let tempList = list

      // 화면 flag 설정 (답글, 비밀글, AP글)
      await tempList.forEach(comment => {
        let displayFlag = ''

        myInfo.userCd === comment.regUserCd ? displayFlag += 'send' : displayFlag += 'receive'
        comment.scretYn === 'Y' ? displayFlag += ' secret' : displayFlag
        comment.cmmtLevel > 0 ? displayFlag += ' reply' : displayFlag
        comment.apYn === 'Y' ? displayFlag += ' ap' : displayFlag
        comment.displayFlag = displayFlag
        comment.cmmtCont = sanitizeHtml(comment.cmmtCont)
      })

      // 비밀글 제어
      await tempList.forEach((comment) => {
        if (comment.scretYn === 'Y') {
          if (myInfo.userCd !== comment.regUserCd) {
            const tempCmmtSeq = comment.cmmtSeq
            tempList = tempList.filter(vo => vo.cmmtSeq !== tempCmmtSeq)
          }
        }
      })

      if (flag == 'C') {
        state.commentList = tempList
      } else {
        state.mainCmmtList = tempList.splice(-30).reverse()
      }
    }

    // [상세] 카테고리 메뉴 변경 이벤트
    const fnChgCategory = async (recordCd) => {
      await fetchCodeGroupMaps(['RAW_STEP_MENU'])
      let tempVal = 0
      codeGroupMaps.value['RAW_STEP_MENU'].forEach((c, i) => {
        if (c.code == state.params.stepCd) {
          tempVal = c.buffer1
        }
      })

      const fetchParams = {
        stepSeq: tempVal,
        recordCd: recordCd,
        stepCd : state.params.stepCd
      }

      await fetchComments(fetchParams)
    }

    return {
      ...toRefs(state),
      toggleEvent,
      fnIngDetail,
      fnChgFlag,
      fnMoveCommu,
      fnInsertReply,
      fnChgCategory,
      fetchMainComments,
      fetchComments,
      fnSaveComment
    }
  }
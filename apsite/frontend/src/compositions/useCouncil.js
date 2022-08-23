import { reactive, toRefs, inject } from 'vue'
import validation from '@/utils/validation'
import axios from '@/utils/customAxios'
import router from '../router'
import { useRoute } from 'vue-router'
import { useActions } from 'vuex-composition-helpers'
import { useStore } from 'vuex'
import { useForm, useField } from 'vee-validate'
import * as yup from 'yup'

export const useCouncil = () => {
  // const myInfo = JSON.parse(sessionStorage.getItem('myInfo'))
  const store = useStore()
  const myInfo = store.getters.getMyInfo()
  const route = useRoute()
  const t = inject('t')
  const commonUtils = inject('commonUtils')
  const { openAsyncConfirm, openAsyncAlert, openAsyncPopup } = useActions(['openAsyncConfirm', 'openAsyncAlert', 'openAsyncPopup'])

  let isProgress = false

  const viewBtnAuth = commonUtils.checkAuth('SGG000003|SGG000007|SGG000011|SGG000012|SGG000013|SGG000014')

  // [s] validation init
  validation.init()

  const schema = yup.object({
    title: yup.string().required(),
    cfrcDtm: yup.string()
                .test(
                  'date-check1',
                  t('board.msg.council.vali_before_today'), // 지정한 날짜가 현재 날짜보다 이전입니다.
                  (value) => {
                      if (value) {
                        return new Promise((resolve) => {
                          setTimeout(() => resolve(commonUtils.convertStrToDateTime(value) > new Date()))
                        })
                      } else {
                        return true
                      }
                  }
                )
                .required(),
    cfrcPlcCls: yup.string(),
    cfrcPlcRmkOn: yup.string()
                    .test(
                      'cfrc-plc-rmk-on-check',
                      t('common.msg.essential'), // 필수 입력항목 입니다.
                      (value) => {
                        return new Promise((resolve) => {
                          setTimeout(() => resolve((state.params.cfrcPlcCls === 'ON' && commonUtils.isNotEmpty(value)) || (state.params.cfrcPlcCls !== 'ON' && commonUtils.isEmpty(value))))
                        })
                      }
                    )
                    .url(t('board.msg.council.vali_url')), // 웹 주소를 정확하게 적어주세요
    cfrcPlcRmkOff: yup.string()
                      .test(
                        'cfrc-plc-rmk-off-check',
                        t('common.msg.essential'), // 필수 입력항목 입니다.
                        (value) => {
                          return new Promise((resolve) => {
                            setTimeout(() => resolve((state.params.cfrcPlcCls === 'OFF' && commonUtils.isNotEmpty(value)) || (state.params.cfrcPlcCls !== 'OFF' && commonUtils.isEmpty(value))))
                          })
                        }
                      ),
    rawInfoYn: yup.string().required()
  })

  const { handleSubmit, errors } = useForm({
    validationSchema: schema
  })

  const useFieldSet = ['title', 'cfrcPlcRmkOn', 'cfrcPlcRmkOff']
  useFieldSet.forEach(str => {
    useField(str)
  })

  const onInvalidSubmit = ({ values, errors, results }) => {
    //validation 체크 걸렸을때 들어오는 function
    // console.log(values); // current form values
    // console.log(errors); // a map of field names and their first error message
    // console.log(results); // a detailed map of field names and their validation results
  }
  // [e] validation init

  /////////////////////////////////////////////////////////////////////////////////////////
  // Data
  /////////////////////////////////////////////////////////////////////////////////////////

  const state = reactive({
    searchParams: {
      type:'',
      keyword: '',
      nowPageNo: 1
    },
    page: {},
    list: [],
    params: {
      seq: '',
      title: '',
      cfrcType: 'EX',
      cfrcDtm: '',
      cfrcPlcCls: 'ON',
      cfrcPlcRmk: '',
      cfrcPlcRmkOn: '',
      cfrcPlcRmkOff: '',
      rawInfo: [],
      files: [],
      cfrcTypeDesc: '',
      cfrcPlcClsDesc: '',
      cfrcDtmDesc: '',
      regUserCd: '',
      regDtm: '',
      viewCnt: 0
    },
    popupContent: null,
    rawUploadParamsList: [],
    updateDeleteAuth: false,
    cfrcResultRegAuth: false,
    cfrcFinishAreaDispFlag: false,
    rawInfoYn: ''
  })
  
  /////////////////////////////////////////////////////////////////////////////////////////
  // Router
  /////////////////////////////////////////////////////////////////////////////////////////

  const fnLinkReg = (seq) => {
    if (seq) {
      return router.push({ name: 'council-modify', query: { seq : seq } }).catch(() => {})
    } else {
      return router.push({ name: 'council-register' }).catch(() => {})
    }
  }

  const fnLinkView = (seq) => {
    return router.push({ name: 'council-view', query: { seq: seq } }).catch(() => {})
  }

  const fnLinkList = () => {
    return router.push({ name: 'council-list' }).catch(() => {})
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  // Axios
  /////////////////////////////////////////////////////////////////////////////////////////

  // 상세
  const fetchCouncil = (seq) => {
    return axios({
        url: `/api/council/${seq}`,
        method: 'get',
        isLoading: true
      })
      .then(async res => {
        const resData = res.data

        if (resData) {
          if (resData.code === 'C0000') { 
            // 신원료협의체 [기본정보] 세팅
            state.params = { ...state.params, ...resData.data?.info ?? {} }

            if (route.name === 'council-view' && state.params.cfrcStatus === 'CCS0010' && myInfo.userCd !== state.params.updUserCd) {
              state.params.cfrcType = null
              state.params.cfrcPlcCls = null
            }

            state.updateDeleteAuth = getAuth('UD')
            state.cfrcResultRegAuth = getAuth('RST')

            // 신원료협의체 [내용] 세팅
            // state.params.content = resData.data?.content?.content ?? ''
            
            // 신원료협의체 [회의분류] 세팅(EX - 검토예정 / IN - 도입예정)
            state.params.cfrcTypeDesc = 'EX|IN'.indexOf(state.params?.cfrcType) > -1 ? (state.params.cfrcType === 'EX' ? t('board.label.council.be_exam') : t('board.label.council.be_intro')) : ''
  
            // 신원료협의체 [회의장소] 세팅(ON - 온라인 / OFF - 오프라인)
            state.params.cfrcPlcClsDesc = 'ON|OFF'.indexOf(state.params?.cfrcPlcCls) > -1 ? (state.params.cfrcPlcCls === 'ON' ? t('common.label.online') : t('common.label.offline')) : ''

            // 신원료협의체 [대상 원료] 세팅
            state.params.rawInfo = resData.data?.rawInfo ?? []

            const resRawInfo = state.params.rawInfo

            if (resRawInfo.length > 0) {
              state.rawInfoYn = 'Y'

              for (let i = 0; i < resRawInfo.length; i++) {
                const o1 = resRawInfo[i];

                o1.readOnly = o1.regUserCd !== myInfo.userCd ? true : false

                if (route.name === 'council-view') {
                  if (state.params.cfrcStatus === 'CCS0030' && !viewBtnAuth) {
                    switch (o1.introCd) {
                      case 'RIS0010':
                        o1.introCdDesc = t('board.label.council.intro_cancel') // 도입취소
                        break
                      case 'RIS0020':
                        o1.introCdDesc = t('board.label.council.intro_defer') // 도입보류
                        break
                      case 'RIS0030':
                        o1.introCdDesc = t('board.label.council.intro_approval') // 도입승인
                        break
                      default:
                        break
                    }
                  } else {
                    state.rawUploadParamsList.push({
                      targetKey: `${state.params.seq}_${i+1}`,
                      uploadCd: 'COUNCIL02',
                      items: []
                    })
                  }
                }
              }
            }
  
            /**
             * 신원료협의체 [회의일시] 세팅
             * EX)
             * 2021-03-21 11:15 => 2021-03-21 오전 11시 15분
             * 2021-04-15 12:05 => 2021-04-15 오후 12시 5분
             * 2022-05-09 16:30 => 2022-05-09 오후 4시 30분
             */
            if (state.params.cfrcDtm) {
              /**
               * ex)
               * dateTimeArr
               * |_ [0]: 2022-05-02
               * |_ [1]: 16:30
               */
              const dateTimeArr = state.params.cfrcDtm.split(' ')
              if ((dateTimeArr?.length ?? 0) > 1) {
                /**
                 * ex)
                 * timeArr
                 * |_ [0]: 16
                 * |_ [1]: 30
                 */
                let timeArr = dateTimeArr[1].split(':')
                if ((timeArr?.length ?? 0) > 1) {
                  if (Number(timeArr[0] >= 12)) {
                    // 오후
                    state.params.cfrcDtmDesc = `${dateTimeArr[0]} 오후 ${(Number(timeArr[0]) > 12 ? Number(timeArr[0]) - 12 : Number(timeArr[0])) + '시'} ${Number(timeArr[1]) > 0 ? timeArr[1] + '분' : ''}`
                  } else {
                    // 오전
                    state.params.cfrcDtmDesc = `${dateTimeArr[0]} 오전 ${Number(timeArr[0]) + '시'} ${Number(timeArr[1]) > 0 ? timeArr[1] + '분' : ''}`
                  }
                }
              }
            }

            /**
             * 회의장소 구분(cfrcPlcCls) 분기
             * |_ 'ON' => cfrcPlcRmkOn = cfrcPlcRmk
             * |_ 'OFF' => cfrcPlcRmkOff = cfrcPlcRmk
             */
            if (state.params.cfrcPlcCls && state.params.cfrcPlcRmk) {
              if (state.params.cfrcPlcCls === 'ON') {
                state.params.cfrcPlcRmkOn = state.params.cfrcPlcRmk
              } else {
                state.params.cfrcPlcRmkOff = state.params.cfrcPlcRmk
              }
            }

            // 관리자가 아닌 경우, 조회수 증가
            // if (!commonUtils.isAdmin) {
            //   updateCouncilViewCnt(seq)
            // }

            updateCouncilViewCnt(seq)
          } else {
            alert(resData.message)
          }
        }
      })
      .catch(err => {
        console.log(err)
      })
  }

  // 리스트
  const fetchCouncils = (payload) => {
    return axios({
        url: '/api/council',
        method: 'get',
        isLoading: true,
        params: payload
      })
      .then(res => {
        const resData = res.data

        if (resData) {
          if (resData.code === 'C0000') {
            // 목록 세팅
            state.list = resData.data.list

            // 페이지 정보 세팅
            state.page = resData.data.page
  
            // 목록이 존재하는 경우
            if (state.list) {
              state.list.forEach((item) => {
                // 목록 [장소] 세팅(ON - 온라인 / OFF - 오프라인)
                item.cfrcPlcClsDesc = 'ON|OFF'.indexOf(item?.cfrcPlcCls) > -1 ? (item.cfrcPlcCls === 'ON' ? t('common.label.online') : t('common.label.offline')) : ''
                
                /**
                 * 목록 [회의일시] 세팅
                 * EX)
                 * 2021-03-21 11:15 => 2021-03-21 11:15 AM
                 * 2021-04-15 12:05 => 2021-04-15 12:05 PM
                 * 2022-05-09 16:30 => 2022-05-09 4:30 PM
                 */
                let cfrcDtmDesc = ''
  
                if (item.cfrcDtm) {
                  /**
                   * ex)
                   * dateTimeArr
                   * |_ [0]: 2022-05-02
                   * |_ [1]: 16:30
                   */
                  const dateTimeArr = item.cfrcDtm.split(' ')
                  if ((dateTimeArr?.length ?? 0) > 1) {
                    /**
                     * ex)
                     * timeArr
                     * |_ [0]: 16
                     * |_ [1]: 30
                     */
                    let timeArr = dateTimeArr[1].split(':')
                    if ((timeArr?.length ?? 0) > 1) {
                      if (Number(timeArr[0] >= 12)) {
                        // PM
                        cfrcDtmDesc = `${dateTimeArr[0]} ${Number(timeArr[0]) > 12 ? Number(timeArr[0]) - 12 : Number(timeArr[0])}:${timeArr[1]} PM`
                      } else {
                        // AM
                        cfrcDtmDesc = `${dateTimeArr[0]} ${Number(timeArr[0])}:${timeArr[1]} AM`
                      }
                    }
                  }
                }
  
                item.cfrcDtmDesc = cfrcDtmDesc

                // 신원료협의체 리스트 진행상태 표기
                if (item.cfrcStatus === 'CCS0010') {
                  item.cfrcStatusDesc = t('board.label.council.cfrc_status_temp_save') // 임시저장
                } else if (item.cfrcStatus === 'CCS0030') {
                  item.cfrcStatusDesc = t('board.label.council.cfrc_status_finish') // 회의완료
                } else if (commonUtils.convertStrToDateTime(item.cfrcDtm) > new Date()) {
                  item.cfrcStatusDesc = t('board.label.council.cfrc_status_be') // 예정
                } else if (commonUtils.convertStrToDateTime(item.cfrcDtm) < new Date()) {
                  item.cfrcStatusDesc = t('board.label.council.cfrc_status_ing') // 진행중
                }
              })
            }
          } else {
            alert(resData.message)
          }
        }
      })
      .catch(err => {
        console.log(err)
      })
  }

  // 등록
  const insertCouncil = (payload) => {
    return axios({
        url: '/api/council',
        method: 'post',
        isLoading: true,
        data: payload
      })
      .then(async res => {
        const resData = res.data

        if (resData) {
          if (resData.code === 'C0000') {
            // 임시저장 완료되었습니다.
            // 등록 완료되었습니다.
            const message = payload.cfrcStatus === 'CCS0010' ? t('common.msg.temp_save_ok') : t('common.msg.save_ok')

            await openAsyncAlert({ message: message }) 
            if (resData.data) {
              fnLinkView(resData.data)
            }
          } else {
            alert(resData.message)
          }
        }
      })
      .catch(err => {
        console.log(err)
      })
  }

  // 수정
  const updateCouncil = (payload) => {
    return axios({
        url: `/api/council/${payload.seq}`,
        method: 'put',
        isLoading: true,
        data: payload
      })
      .then(async res => {
        const resData = res.data

        if (resData) {
          if (resData.code === 'C0000') {
            // 임시저장 완료되었습니다.
            // 수정 완료되었습니다.
            const message = payload.cfrcStatus === 'CCS0010' ? t('common.msg.temp_save_ok') : t('common.msg.modify_ok')

            await openAsyncAlert({ message: message }) 
            if (resData.data) {
              fnLinkView(resData.data)
            }
          } else {
            alert(resData.message)
          }
        }
      })
      .catch(err => {
        console.log(err)
      })
  }

  const deleteCouncil = (seq) => {
    return axios({
        url: `/api/council/${seq}`,
        method: 'delete',
        isLoading: true
      })
      .then(async res => {
        const resData = res.data

        if (resData) {
          if (resData.code === 'C0000') {
            // 삭제 되었습니다.
            await openAsyncAlert({ message: t('common.msg.delete_ok') })
            fnLinkList()
          }
        }
      })
      .catch(err => {
        console.log(err)
      })
  }

  // 조회수 증가
  const updateCouncilViewCnt = (seq) => {
    return axios({
        url: `/api/council/${seq}/view-cnt`,
        method: 'put'
      })
      .then(res => {
        const resData = res.data

        if (resData) {
          if (resData.code === 'C0000') {
            state.params.viewCnt = state.params.viewCnt + 1
          }
        }
      })
      .catch(err => {
        console.log(err)
      })
  }

  // 회의결과 등록
  const insertCouncilCfrcResult = (payload) => {
    return axios({
        url: `/api/council/${payload.seq}/cfrc-result`,
        method: 'post',
        isLoading: true,
        data: payload
      })
      .then(async res => {
        const resData = res.data

        if (resData) {
          if (resData.code === 'C0000') {
            let message = ''

            if (payload.insertUpdateFlag === 'INSERT') {
              message = t('common.msg.save_ok') // 등록 완료되었습니다.
            } else if (payload.insertUpdateFlag === 'UPDATE') {
              message = t('common.msg.modify_ok') // 수정 완료되었습니다.
            }

            await openAsyncAlert({ message: message })
            fnLinkList()
          }
        }
      })
      .catch(err => {
        console.log(err)
      })
  }

  const insertCouncilTargetRaw = (payload) => {
    return axios({
        url: `/api/council/${payload.seq}/target-raw`,
        method: 'post',
        isLoading: true,
        data: payload
      })
      .then(async res => {
        const resData = res.data

        if (resData) {
          if (resData.code === 'C0000') {
            // 대상 원료가 추가되었습니다.
            await openAsyncAlert({ message: t('board.msg.council.target_append_save') })
            fetchCouncil(state.params.seq)
          }
        }
      })
      .catch(err => {
        console.log(err)
      })
  }

  const deleteCouncilTargetRaw = (payload) => {
    return axios({
        url: `/api/council/${state.params.seq}/target-raw`,
        method: 'delete',
        isLoading: true,
        data: payload
      })
      .then(async res => {
        const resData = res.data

        if (resData) {
          if (resData.code === 'C0000') {
            // 대상 원료가 삭제되었습니다.
            await openAsyncAlert({ message: t('board.msg.council.target_append_delete') })
            fetchCouncil(state.params.seq)
          }
        }
      })
      .catch(err => {
        console.log(err)
      })
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  // 사용자 함수
  /////////////////////////////////////////////////////////////////////////////////////////

  // 신원료협의체 장소 Change Event
  const fnChangeCfrcPlcCls = () => {
    if (state.params.cfrcPlcCls === 'ON') {
      state.params.cfrcPlcRmkOff = ''
    } else {
      state.params.cfrcPlcRmkOn = ''
    }
  }

  // 신원료협의체 장소 비고 세팅
  const fnSettingCfrcPlcRmk = () => {
    state.params.cfrcPlcRmk = state.params.cfrcPlcCls === 'ON' ? state.params.cfrcPlcRmkOn : state.params.cfrcPlcRmkOff
  }

  const fnActionTemp = async () => {
    // 입력하신 내용으로 임시저장하시겠습니까?
    if (!await openAsyncConfirm({ message: t('board.msg.seminar.temp_save') })) {
      return
    }

    fnSettingCfrcPlcRmk()

    isProgress = true

    state.params.cfrcStatus = 'CCS0010'

    if(state.params.seq){        
      updateCouncil(state.params).finally(() => { isProgress = false })
    }else{
      insertCouncil(state.params).finally(() => { isProgress = false })
    }  
  }

  const fnAction = handleSubmit(async values => {
    //validation 체크가 성공 후 들어오는 부분

    // 입력하신 내용으로 수정하시겠습니까?
    // 입력하신 내용으로 등록하시겠습니까?
    const message = state.params.seq ? t('board.msg.council.update') : t('board.msg.council.save')
    
    if (!await openAsyncConfirm({ message })) {
      return
    }

    // state.params.files = state.uploadParams.items
    fnSettingCfrcPlcRmk()

    isProgress = true

    state.params.cfrcStatus = 'CCS0020'

    if(state.params.seq){        
      updateCouncil(state.params).finally(() => { isProgress = false })
    }else{
      insertCouncil(state.params).finally(() => { isProgress = false })
    }  
  }, onInvalidSubmit)

  const fnSave = (flag) => {
    if (isProgress) {
      return
    }
    if (flag === 'TEMP') {
      fnActionTemp()
    } else if (flag === 'CONFIRM') {
      fnAction()
    }
  }

  const fnCancel = async () => {
    // 입력하신 내용을 취소 하시겠습니까?
    if (!await openAsyncConfirm({ message: t('board.msg.council.cancel') })) {
      return
    }      
    fnLinkList()
  }

  const fnOpenPopup = (compNm) => {
    state.popupContent = compNm
    
    openAsyncPopup({ minWidth: 1200, noLimitH: true })
      .then(res => {
        if (res.emitValue) {
          const emitValue = res.emitValue
          state.params.rawInfo = emitValue

          if (res.flag === 'V') {
            insertCouncilTargetRaw(state.params)
          }

          state.rawInfoYn = state.params.rawInfo.length > 0 ? 'Y' : ''
        }
      })
      .catch(err => {
        console.log(err)
      })
      .finally(() => {
        state.popupContent = null
      })
  }

  const fnDelTargetRaw = (idx) => {
    state.params.rawInfo.splice(idx, 1)
    state.rawInfoYn = state.params.rawInfo.length > 0 ? 'Y' : ''
  }

  const fnToggleCfrcFinishBtn = async (flag) => {
    if (state.cfrcFinishAreaDispFlag === flag) return
    if (!flag) {
      // 회의결과 등록을 취소하시겠습니까?
      if (!await openAsyncConfirm({ message: t('board.msg.council.cfrc_result_reg_cancel') })) {
        return
      }
      // state.params.rawInfo.forEach(raw => {
      //   raw.introCd = 'RIS0010'
      //   raw.cfrcOpn = ''
      // })
    }
    state.cfrcFinishAreaDispFlag = flag
  }

  const fnSaveCfrcResult = async (flag) => {
    if (!flag) return
    
    let message = null

    if (flag === 'INSERT') {
      message = t('board.msg.council.cfrc_result_reg') // 회의결과를 등록하시겠습니까?
    } else if (flag === 'UPDATE') {
      message = t('board.msg.council.cfrc_result_update') // 회의결과를 수정하시겠습니까?
    } else {
      return
    }

    if (!await openAsyncConfirm({ message: message })) {
      return
    }

    const rawInfo = state.params.rawInfo
    const rawUploadParamsList = state.rawUploadParamsList

    if ((rawInfo && rawUploadParamsList) && (rawInfo.length === rawUploadParamsList.length)) {
      for (let i = 0; i < rawInfo.length; i++) {
        // rawInfo[i].targetKey = rawUploadParamsList[i].targetKey
        rawInfo[i].files = rawUploadParamsList[i].items
      }
    }

    insertCouncilCfrcResult({ ...state.params, ...{ insertUpdateFlag: flag }})
  }

  const fnSearch = (pg) => {
    if(!pg){
      pg = 1
    }      
    state.searchParams.nowPageNo = pg
    fetchCouncils(state.searchParams)
  }

  const fnDelete = async () => {
    // 정말로 삭제 하시겠습니까?
    if (!await openAsyncConfirm({ message: t('common.msg.delete_confirm_msg') })) {
      return
    }
    deleteCouncil(state.params.seq)
  }

  /**
   * 권한 설정
   * @param flag 
   * @returns 
   * flag
   * |_ UD : Update/Delete (수정/삭제 권한)
   * |_ RST : result (회의결과 등록 권한)
   */
  /**
    * 수정-삭제 권한
    * 1. 현재 일자 < 회의 일시 (임시저장 상태 아닌 경우)
    * 2. 관리자 or 신원료 협의체 담당자
    */
  /**
   * 회의 결과 등록 권한
   * 1. 회의 일시 < 현재 일자 (임시저장 상태 아닌 경우)
   * 2. 회의 진행상태 !== '회의완료'
   * 3. 관리자 or 신원료 협의체 담당자
   */
  const getAuth = (flag) => {
    if (!flag) return false
    if ('UD|RST'.indexOf(flag) === -1) return false

    let auth = viewBtnAuth
    
    if (flag === 'UD') {
      if (state.params.cfrcStatus !== 'CCS0010') {
        auth = (new Date() < commonUtils.convertStrToDateTime(state.params.cfrcDtm)) && auth
      }
    } else {
      if (state.params.cfrcStatus !== 'CCS0010') {
        auth = (commonUtils.convertStrToDateTime(state.params.cfrcDtm) < new Date()) && state.params.cfrcStatus !== 'CCS0030' && auth
      } else {
        auth = false
      }
    }

    return auth
  }

  const fnDeleteTargetRaw = async (raw) => {
    // 대상 원료를 삭제하시겠습니까?
    if (!await openAsyncConfirm({ message: t('board.msg.council.req_target_append_delete') })) {
      return
    }
    deleteCouncilTargetRaw(raw)
  }

  /////////////////////////////////////////////////////////////////////////////////////////

  return {
    myInfo,
    t,
    ...toRefs(state),
    errors,
    fnLinkList,
    fnLinkView,
    fnLinkReg,
    fetchCouncil,
    insertCouncil,
    updateCouncil,
    updateCouncilViewCnt,
    fnChangeCfrcPlcCls,
    fnSettingCfrcPlcRmk,
    fnSave,
    fnCancel,
    fnOpenPopup,
    fnDelTargetRaw,
    fnToggleCfrcFinishBtn,
    fnSaveCfrcResult,
    fnSearch,
    fnDelete,
    fnDeleteTargetRaw
  }
}
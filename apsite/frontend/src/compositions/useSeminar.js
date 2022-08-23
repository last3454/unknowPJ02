import { reactive, toRefs, inject } from 'vue'
import validation from '@/utils/validation'
import axios from '@/utils/customAxios'
import router from '../router'
import { useActions } from 'vuex-composition-helpers'
import { useStore } from 'vuex'
import { useForm, useField } from 'vee-validate'
import sanitizeHtml from '@/utils/sanitizeHtml'
import * as yup from 'yup'

export const useSeminar = () => {
  const today = new Date()
  // const myInfo = JSON.parse(sessionStorage.getItem('myInfo'))
  const store = useStore()
  const myInfo = store.getters.getMyInfo()
  const t = inject('t')
  const commonUtils = inject('commonUtils')
  const { openAsyncConfirm, openAsyncAlert, openAsyncPopup } = useActions(['openAsyncConfirm', 'openAsyncAlert', 'openAsyncPopup'])
  const storeLangCd = store.getters.getLangCd()

  let isProgress = false

  // [s] validation init
  validation.init()

  const schema = yup.object({
    title: yup.string().required(),
    smnDtm: yup.string()
              .test(
                'date-check1',
                t('board.msg.seminar.vali_before_today'), // 지정한 날짜가 현재 날짜보다 이전입니다.
                (value) => {
                  if (value) {
                    return new Promise((resolve) => {
                      setTimeout(() => resolve(commonUtils.convertStrToDateTime(value) > today))
                    })
                  } else {
                    return true
                  }
                }
              )
              .test(
                'date-check2',
                t('board.msg.seminar.vali_before_reg_end_day'), // 지정한 날짜가 신청 종료 날짜보다 이전입니다.
                (value) => {
                  if (commonUtils.isNotEmpty(value) && commonUtils.isNotEmpty(state.params.smnRegEndDt)) {
                    return new Promise((resolve) => {
                      setTimeout(() => resolve(commonUtils.convertStrToDateTime(value) > commonUtils.convertStrToDate(state.params.smnRegEndDt)))
                    })
                  } else {
                    return true
                  }
                }
              )
              .test(
                'date-check3',
                t('board.msg.seminar.vali_term_smndtm_reg_end_day'), // 세미나 일시는 신청 종료 일자와 일주일 이상 차이가 있어야 합니다.
                (value) => {
                  if (commonUtils.isNotEmpty(value) && commonUtils.isNotEmpty(state.params.smnRegEndDt)) {
                    const smnDtm = commonUtils.convertStrToDateTime(value)
                    const smnRegEndDt = commonUtils.convertStrToDate(state.params.smnRegEndDt)
                    smnRegEndDt.setDate(smnRegEndDt.getDate() + 7)
                    if (smnDtm > smnRegEndDt) {
                      return new Promise((resolve) => {
                        setTimeout(() => resolve(smnDtm > smnRegEndDt))
                      })
                    }
                  } else {
                    return true
                  }
                }
              )
              .test(
                'date-check4',
                t('board.msg.seminar.vali_before_smnenddtm'), // 세미나 종료시간이 시작시간과 같거나 빠릅니다.
                (value) => {
                  if (commonUtils.isNotEmpty(value) && commonUtils.isNotEmpty(state.params.smnEndDtm)) {
                    const smnDtm = commonUtils.convertStrToDateTime(value)
                    const smnEndDtm = commonUtils.convertStrToDateTime(state.params.smnEndDtm)
                    if (smnEndDtm > smnDtm) {
                      return new Promise((resolve) => {
                        setTimeout(() => resolve(smnEndDtm > smnDtm))
                      })
                    }
                  } else {
                    return true
                  }
                }
              )
              .required(),
    smnRegStartDt: yup.string()
                      .test(
                        'date-check',
                        t('board.msg.seminar.vali_before_today'), // 지정한 날짜가 현재 날짜보다 이전입니다.
                        (value) => {
                          if (value) {
                            return new Promise((resolve) => {
                              const tmpToday = new Date()
                              tmpToday.setHours(0,0,0,0)
                              setTimeout(() => resolve((commonUtils.convertStrToDate(value) >= tmpToday) && (commonUtils.convertStrToDate(state.params.smnRegEndDt) > tmpToday)))
                            })
                          } else {
                            return true
                          }
                        }
                      )
                      .required(),
    smnPlcCls: yup.string(),
    smnPlcRmkOn: yup.string()
                    .test(
                      'smn-plc-rmk-on-check',
                      t('common.msg.essential'), // 필수 입력항목 입니다.
                      (value) => {
                        return new Promise((resolve) => {
                          setTimeout(() => resolve((state.params.smnPlcCls === 'ON' && commonUtils.isNotEmpty(value)) || (state.params.smnPlcCls !== 'ON' && commonUtils.isEmpty(value))))
                        })
                      }
                    )
                    .url(t('board.msg.seminar.vali_url')), // 웹 주소를 정확하게 적어주세요
    smnPlcRmkOff: yup.string()
                    .test(
                      'smn-plc-rmk-off-check',
                      t('common.msg.essential'), // 필수 입력항목 입니다.
                      (value) => {
                        return new Promise((resolve) => {
                          setTimeout(() => resolve((state.params.smnPlcCls === 'OFF' && commonUtils.isNotEmpty(value)) || (state.params.smnPlcCls !== 'OFF' && commonUtils.isEmpty(value))))
                        })
                      }
                    ),
    applyMinCnt: yup.string()
                    .test(
                      'apply-cnt-check1',
                      t('common.msg.essential'), // 필수 입력항목 입니다.
                      (value) => {
                        if (commonUtils.isNotEmpty(value) && commonUtils.isNotEmpty(state.params.applyMaxCnt)) {
                          return new Promise((resolve) => {
                            setTimeout(() => resolve(commonUtils.isNotEmpty(value) && commonUtils.isNotEmpty(state.params.applyMaxCnt)))
                          })
                        } else {
                          return true
                        }
                      }
                    )
                    .test(
                      'apply-cnt-check2',
                      t('board.msg.seminar.vali_not_zero'), // 입력값이 0이 될 수 없습니다.
                      (value) => {
                        if (commonUtils.isNotEmpty(value)) {
                          if (Number(value) === 0) {
                            return new Promise((resolve) => {
                              setTimeout(() => resolve(Number(value) > 0))
                            })
                          } else {
                            // return true
                            if (commonUtils.isNotEmpty(state.params.applyMaxCnt)) {
                              if (Number(state.params.applyMaxCnt) === 0) {
                                return new Promise((resolve) => {
                                  setTimeout(() => resolve(Number(state.params.applyMaxCnt) > 0))
                                })
                              } else {
                                return true
                              }
                            } else {
                              return true
                            }
                          }
                        }
                      }
                    )
                    .test(
                      'apply-cnt-check3',
                      t('board.msg.seminar.vali_gt_max'), // 최소 인원수가 최대 인원수 보다 큽니다.
                      (value) => {
                        if (commonUtils.isNotEmpty(value) && commonUtils.isNotEmpty(state.params.applyMaxCnt)) {
                          if (Number(value) !== 0 && Number(state.params.applyMaxCnt) !== 0) {
                            return new Promise((resolve) => {
                              setTimeout(() => resolve(Number(value) <= Number(state.params.applyMaxCnt)))
                            })
                          }
                        } else {
                          return true
                        }
                      }
                    )
                    .required(),
    applyMaxCnt: yup.string(),
    content: yup.string().required(),
    ytbUrl: yup.string()
  })

  const { handleSubmit, errors } = useForm({
    validationSchema: schema
  })

  const useFieldSet = ['title', 'smnEndDtm', 'smnPlcRmkOn', 'smnPlcRmkOff', 'applyMinCnt', 'applyMaxCnt', 'content', 'ytbUrl']
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
    attendList: [],
    params: {
      seq: '',
      title: '',
      smnDtm: '',
      smnEndDtm: '',
      smnRegStartDt: '',
      smnRegEndDt: '',
      smnPlcCls: 'ON',
      smnPlcRmk: '',
      smnPlcRmkOn: '',
      smnPlcRmkOff: '',
      applyCnt: 0,
      applyMinCnt: '',
      applyMaxCnt: '',
      rawInfo: [],
      content: '',
      files: [],
      ytbUrl: '',
      smnPlcClsDesc: '',
      smnDtmDesc: '',
      likeCnt: 0,
      rcmdCnt: 0,
      regUserCd: '',
      regDtm: '',
      likeInfo: {},
      recommendInfo: {},
      attendInfo: {},
      viewCnt: 0,
      commuList: [],
      commuContent: '',
      smnCanclReason: ''
    },
    popupContent: null,
    uploadParams: {
      targetKey: '',
      uploadCd: 'SEMINAR01',
      items: []
    },
    smnFinishFlag: false,
    smnCancelAreaDispFlag: false,
    smnCancelConfirmAuth: false,
    smnUpdateDeleteAuth: false,
    todayBetweenReqDate: false
  })

  /////////////////////////////////////////////////////////////////////////////////////////
  // Router
  /////////////////////////////////////////////////////////////////////////////////////////

  // 세미나 재신청
  const fnLinkReReq = () => {
    return router.push({ name: 'seminar-register', query: { params: JSON.stringify(state.params) } }).catch(() => {})
  }

  // 세미나 등록
  const fnLinkReg = (seq) => {
    if (seq) {
      return router.push({ name: 'seminar-modify', query: { seq : seq } }).catch(() => {})
    } else {
      return router.push({ name: 'seminar-register' }).catch(() => {})
    }
  }

  // 세미나 상세
  const fnLinkView = (seq) => {
    return router.push({ name: 'seminar-view', query: { seq: seq } }).catch(() => {})
  }

  // 세미나 목록
  const fnLinkList = () => {
    return router.push({ name: 'seminar-list' }).catch(() => {})
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  // Axios
  /////////////////////////////////////////////////////////////////////////////////////////

  // 메인 노출 세미나(가장 최근 등록된 것)
  const fetchMainSeminar = () => {
    return axios({
        url: `/api/seminar/main`,
        method: 'get'
      })
      .then(res => {
        const resData = res.data

        if (resData) {
          if (resData.code === 'C0000') {
            // 세미나 기본정보 세팅
            state.params = { ...state.params, ...resData.data?.info ?? {} }

            state.params.smnPlcClsDesc = 'ON|OFF'.indexOf(state.params?.smnPlcCls ?? '') > -1 ? (state.params.smnPlcCls === 'ON' ? t('common.label.online') : t('common.label.offline')) : ''

            if (state.params.smnDtm) {
              /**
               * ex)
               * dateTimeArr
               * |_ [0]: 2022-05-02
               * |_ [1]: 16:30
               */
              const dateTimeArr = state.params.smnDtm.split(' ')
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
                    if (storeLangCd === 'ko') {
                      // 오후
                      state.params.smnDtmDesc = `${dateTimeArr[0]} 오후 ${(Number(timeArr[0]) > 12 ? Number(timeArr[0]) - 12 : Number(timeArr[0])) + '시'} ${Number(timeArr[1]) > 0 ? timeArr[1] + '분' : ''}`
                    } else {
                      // PM
                      state.params.smnDtmDesc = `${dateTimeArr[0]} ${Number(timeArr[0]) > 12 ? Number(timeArr[0]) - 12 : Number(timeArr[0])}:${timeArr[1]} PM`
                    }
                  } else {
                    if (storeLangCd === 'ko') {
                      // 오전
                      state.params.smnDtmDesc = `${dateTimeArr[0]} 오전 ${Number(timeArr[0]) + '시'} ${Number(timeArr[1]) > 0 ? timeArr[1] + '분' : ''}`
                    } else {
                      // AM
                      state.params.smnDtmDesc = `${dateTimeArr[0]} ${Number(timeArr[0])}:${timeArr[1]} AM`
                    }
                  }
                }
              }

              if (state.params.smnEndDtm) {
                state.smnFinishFlag = commonUtils.convertStrToDateTime(state.params.smnEndDtm) < today

                const dateTimeArr2 = state.params.smnEndDtm.split(' ')

                if ((dateTimeArr2?.length ?? 0) > 1) {
                  let timeArr2 = dateTimeArr2[1].split(':')

                  if ((timeArr2?.length ?? 0) > 1) {
                    if (Number(timeArr2[0] >= 12)) {
                      if (storeLangCd === 'ko') {
                        // 오후
                        state.params.smnDtmDesc = `${state.params.smnDtmDesc} ~ 오후 ${(Number(timeArr2[0]) > 12 ? Number(timeArr2[0]) - 12 : Number(timeArr2[0])) + '시'} ${Number(timeArr2[1]) > 0 ? timeArr2[1] + '분' : ''}`
                      } else {
                        // PM
                        state.params.smnDtmDesc = `${state.params.smnDtmDesc} ~ ${Number(timeArr2[0]) > 12 ? Number(timeArr2[0]) - 12 : Number(timeArr2[0])}:${timeArr2[1]} PM`
                      }
                    } else {
                      if (storeLangCd === 'ko') {
                        // 오전
                        state.params.smnDtmDesc = `${state.params.smnDtmDesc} ~ 오전 ${Number(timeArr2[0]) + '시'} ${Number(timeArr2[1]) > 0 ? timeArr2[1] + '분' : ''}`
                      } else {
                        // AM
                        state.params.smnDtmDesc = `${state.params.smnDtmDesc} ~ ${Number(timeArr2[0])}:${timeArr2[1]} AM`
                      }
                    }
                  }
                }
              }
            }
          }
        }
      })
      .catch(err => {
        console.log(err)
      })
  }

  // 상세
  const fetchSeminar = (seq) => {
    return axios({
        url: `/api/seminar/${seq}`,
        method: 'get',
        isLoading: true
      })
      .then(res => {
        const resData = res.data

        if (resData) {
          if (resData.code === 'C0000') {
            // 세미나 기본정보 세팅
            state.params = { ...state.params, ...resData.data?.info ?? {} }
            state.params.content = sanitizeHtml(state.params.content)
            /**
             * 세미나 취소/확정 권한 설정
             * 1. 세미나 상태가 '개최확정', '개최취소' 상태가 아닌 경우
             * 2. 현재 사용자가 등록자와 같은 경우
             * 3. 세미나 신청 마감일자 < 현재 날짜 < 세미나 일시
             */
            state.smnCancelConfirmAuth =
              'SMN0030|SMN0040'.indexOf(state.params.smnStatus) === -1 // 1.
              && (myInfo.userCd === state.params.regUserCd) // 2.
              && ((date) => { return (commonUtils.convertStrToDate(state.params.smnRegEndDt) < date) && (date < commonUtils.convertStrToDateTime(state.params.smnDtm)) })(today) // 3.

            /**
             * 세미나 기본정보 수정/삭제 권한
             * 1. 현재 사용자가 등록자와 같은 경우
             * 2. 현재 날짜 < 세미나 신청 시작일자
             */
            state.smnUpdateDeleteAuth =
              (myInfo.userCd === state.params.regUserCd) // 1.
              && ((date) => { return date < commonUtils.convertStrToDate(state.params.smnRegStartDt) })(today) // 2.

            // 세미나 내용 세팅
            state.params.content = resData.data?.content?.content ?? ''

            // 세미나 좋아요/추천 세팅
            const likeRecm = resData.data?.likeRecm ?? []

            if (likeRecm.length > 0) {
              likeRecm.forEach((item) => {
                if (item.categoryCd === 'L') {
                  state.params.likeInfo = item
                } else if (item.categoryCd === 'R') {
                  state.params.recommendInfo = item
                }
              })
            }

            // 세미나 참석 정보 세팅
            state.params.attendInfo = resData.data?.attend ?? {}

            state.todayBetweenReqDate = fnCompareTodayBetweenReqDate(today)

            // 세미나 원료정보 세팅
            state.params.rawInfo = resData.data?.rawInfo ?? []

            // 세미나 댓글 리스트 세팅
            state.params.commuList = resData.data?.commu ?? []

            state.params.smnPlcClsDesc = 'ON|OFF'.indexOf(state.params?.smnPlcCls ?? '') > -1 ? (state.params.smnPlcCls === 'ON' ? t('common.label.online') : t('common.label.offline')) : ''

            if (state.params.smnDtm) {
              // 세미나 종료 여부(임시로 세미나 일시와 오늘 날짜 비교하여 판단)
              state.smnFinishFlag = commonUtils.convertStrToDateTime(state.params.smnDtm) < today

              /**
               * ex)
               * dateTimeArr
               * |_ [0]: 2022-05-02
               * |_ [1]: 16:30
               */
              const dateTimeArr = state.params.smnDtm.split(' ')

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
                    if (storeLangCd === 'ko') {
                      // 오후
                      state.params.smnDtmDesc = `${dateTimeArr[0]} 오후 ${(Number(timeArr[0]) > 12 ? Number(timeArr[0]) - 12 : Number(timeArr[0])) + '시'} ${Number(timeArr[1]) > 0 ? timeArr[1] + '분' : ''}`
                    } else {
                      // PM
                      state.params.smnDtmDesc = `${dateTimeArr[0]} ${Number(timeArr[0]) > 12 ? Number(timeArr[0]) - 12 : Number(timeArr[0])}:${timeArr[1]} PM`
                    }
                  } else {
                    if (storeLangCd === 'ko') {
                      // 오전
                      state.params.smnDtmDesc = `${dateTimeArr[0]} 오전 ${Number(timeArr[0]) + '시'} ${Number(timeArr[1]) > 0 ? timeArr[1] + '분' : ''}`
                    } else {
                      // AM
                      state.params.smnDtmDesc = `${dateTimeArr[0]} ${Number(timeArr[0])}:${timeArr[1]} AM`
                    }
                  }
                }
              }

              if (state.params.smnEndDtm) {
                state.smnFinishFlag = commonUtils.convertStrToDateTime(state.params.smnEndDtm) < today

                const dateTimeArr2 = state.params.smnEndDtm.split(' ')

                if ((dateTimeArr2?.length ?? 0) > 1) {
                  let timeArr2 = dateTimeArr2[1].split(':')

                  if ((timeArr2?.length ?? 0) > 1) {
                    if (Number(timeArr2[0] >= 12)) {
                      if (storeLangCd === 'ko') {
                        // 오후
                        state.params.smnDtmDesc = `${state.params.smnDtmDesc} ~ 오후 ${(Number(timeArr2[0]) > 12 ? Number(timeArr2[0]) - 12 : Number(timeArr2[0])) + '시'} ${Number(timeArr2[1]) > 0 ? timeArr2[1] + '분' : ''}`
                      } else {
                        // PM
                        state.params.smnDtmDesc = `${state.params.smnDtmDesc} ~ ${Number(timeArr2[0]) > 12 ? Number(timeArr2[0]) - 12 : Number(timeArr2[0])}:${timeArr2[1]} PM`
                      }
                    } else {
                      if (storeLangCd === 'ko') {
                        // 오전
                        state.params.smnDtmDesc = `${state.params.smnDtmDesc} ~ 오전 ${Number(timeArr2[0]) + '시'} ${Number(timeArr2[1]) > 0 ? timeArr2[1] + '분' : ''}`
                      } else {
                        // AM
                        state.params.smnDtmDesc = `${state.params.smnDtmDesc} ~ ${Number(timeArr2[0])}:${timeArr2[1]} AM`
                      }
                    }
                  }
                }
              }
            }

            if (state.params.smnPlcCls && state.params.smnPlcRmk) {
              if (state.params.smnPlcCls === 'ON') {
                state.params.smnPlcRmkOn = state.params.smnPlcRmk
              } else {
                state.params.smnPlcRmkOff = state.params.smnPlcRmk
              }
            }

            if (state.params.ytbUrl) {
              const getId = state.params.ytbUrl

              state.params.ytbUrl = getId ? `https://www.youtube.com/embed/${getId}` : ''
            }

            // if (!commonUtils.isAdmin) {
            //   updateSeminarViewCnt(seq)
            // }
            updateSeminarViewCnt(seq)
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
  const fetchSeminars = (payload) => {
    return axios({
        url: '/api/seminar',
        method: 'get',
        isLoading: true,
        params: payload
      })
      .then(res => {
        const resData = res.data

        if (resData) {
          if (resData.code === 'C0000') {
            state.list = resData.data.list
            state.page = resData.data.page

            if (state.list) {
              state.list.forEach((item) => {
                item.smnPlcClsDesc = 'ON|OFF'.indexOf(item?.smnPlcCls ?? '') > -1 ? (item.smnPlcCls === 'ON' ? t('common.label.online') : t('common.label.offline')) : ''
                let smnDtmDesc = ''

                if (item.smnDtm) {
                  /**
                   * ex)
                   * dateTimeArr
                   * |_ [0]: 2022-05-02
                   * |_ [1]: 16:30
                   */
                  const dateTimeArr = item.smnDtm.split(' ')
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
                        smnDtmDesc = `${dateTimeArr[0]} ${Number(timeArr[0]) > 12 ? Number(timeArr[0]) - 12 : Number(timeArr[0])}:${timeArr[1]} PM`
                      } else {
                        // AM
                        smnDtmDesc = `${dateTimeArr[0]} ${Number(timeArr[0])}:${timeArr[1]} AM`
                      }
                    }
                  }
                }

                item.smnDtmDesc = smnDtmDesc
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
  const insertSeminar = (payload) => {
    return axios({
        url: '/api/seminar',
        method: 'post',
        isLoading: true,
        data: payload
      })
      .then(async res => {
        const resData = res.data

        if (resData) {
          if (resData.code === 'C0000') {
            await openAsyncAlert({ message: t('common.msg.save_ok') }) // 등록 완료되었습니다.
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
  const updateSeminar = (payload) => {
    return axios({
        url: '/api/seminar/' + payload.seq,
        method: 'put',
        isLoading: true,
        data: payload
      })
      .then(async res => {
        const resData = res.data

        if (resData) {
          if (resData.code === 'C0000') {
            await openAsyncAlert({ message: t('common.msg.modify_ok') }) // 수정 완료되었습니다.
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

  // 삭제
  const deleteSeminar = (seq) => {
    return axios({
        url: `/api/seminar/${seq}`,
        method: 'delete',
        isLoading: true
      })
      .then(async res => {
        const resData = res.data

        if (resData) {
          if (resData.code === 'C0000') {
            await openAsyncAlert({ message: t('common.msg.delete_ok') }) // 삭제 되었습니다.
            fnLinkList()
          } else {
            alert(resData.message)
          }
        }
      })
      .catch(err => {
        console.log(err)
      })
  }

  // 세미나 취소
  const updateSeminarCancel = (payload) => {
    return axios({
        url: `/api/seminar/${payload.seq}/seminar-cancel`,
        method: 'put',
        isLoading: true,
        data: payload
      })
      .then(async res => {
        const resData = res.data

        if (resData) {
          if (resData.code === 'C0000') {
            await openAsyncAlert({ message: t('board.msg.seminar.cancel_ok_smn') }) // 세미나가 취소되었습니다.
            fnLinkList()
          } else {
            alert(resData.message)
          }
        }
      })
      .catch(err => {
        console.log(err)
      })
  }

  // 세미나 확정
  const updateSeminarConfirm = (seq) => {
    return axios({
        url: `/api/seminar/${seq}/seminar-confirm`,
        method: 'put',
        isLoading: true
      })
      .then(async res => {
        const resData = res.data

        if (resData) {
          if (resData.code === 'C0000') {
            await openAsyncAlert({ message: t('board.msg.seminar.confirm_ok_smn') }) // 세미나가 확정되었습니다.
            fnLinkList()
          } else {
            alert(resData.message)
          }
        }
      })
      .catch(err => {
        console.log(err)
      })
  }

  // 좋아요/추천 등록
  const insertSeminarLikeRecm = (seq, categoryCd) => {
    if (isProgress) return

    isProgress = true

    return axios({
        url: `/api/seminar/${seq}/like-recm`,
        method: 'post',
        isLoading: true,
        data: { seq: seq, categoryCd: categoryCd }
      })
      .then(res => {
        const resData = res.data

        if (resData) {
          if (resData.code === 'C0000') {
            if (resData.data.categoryCd === 'L') {
              state.params.likeInfo = resData?.data ?? {}
              state.params.likeCnt = state.params.likeCnt + 1
            } else if (resData.data.categoryCd === 'R') {
              state.params.recommendInfo = resData?.data ?? {}
              state.params.rcmdCnt = state.params.rcmdCnt + 1
            }
          }
        }
      })
      .catch(err => {
        console.log(err)
      })
      .finally(() => {
        isProgress = false
      })
  }

  // 좋아요/추천 해제
  const deleteSeminarLikeRecm = (payload, categoryCd) => {
    if (isProgress) return

    isProgress = true

    return axios({
        url: `/api/seminar/${payload.seq}/like-recm`,
        method: 'delete',
        isLoading: true,
        data: { ...payload, ...{ categoryCd: categoryCd } }
      })
      .then(res => {
        const resData = res.data

        if (resData) {
          if (resData.code === 'C0000') {
            if (resData.data === 'L') {
              state.params.likeInfo = {}

              if (state.params.likeCnt > 0) {
                state.params.likeCnt = state.params.likeCnt - 1
              }
            } else if (resData.data === 'R') {
              state.params.recommendInfo = {}

              if (state.params.rcmdCnt > 0) {
                state.params.rcmdCnt = state.params.rcmdCnt - 1
              }
            }
          } else if (resData.code === 'C9999') {
            alert(resData.message)
          }
        }
      })
      .catch(err => {
        console.log(err)
      })
      .finally(() => {
        isProgress = false
      })
  }

  // 참석 명단
  const fetchSeminarAttendList = (seq) => {
    return axios({
        url: `/api/seminar/${seq}/attend`,
        method: 'get',
        isLoading: true
      })
      .then(res => {
        const resData = res.data

        if (resData) {
          if (resData.code === 'C0000') {
            state.attendList = resData?.data ?? []
          }
        }
      })
  }

  // 참석 신청
  const insertSeminarAttend = (seq) => {
    return axios({
        url: `/api/seminar/${seq}/attend`,
        method: 'post',
        isLoading: true
      })
      .then(async res => {
        const resData = res.data

        if (resData) {
          if (resData.code === 'C0000') {
            // 참석 신청 완료되었습니다.
            await openAsyncAlert({ message: t('board.msg.seminar.save_attend') })
            state.params.attendInfo = resData?.data ?? {}
            state.params.applyCnt = state.params.applyCnt + 1
          } else if (resData.code === 'C9999') {
            // 참석 인원수가 최대에 도달하여 신청 실패하였습니다.
            await openAsyncAlert({ message: t('board.msg.seminar.attend_max') })
          }
        }
      })
      .catch(err => {
        console.log(err)
      })
  }

  // 참석 취소
  const deleteSeminarAttend = (payload) => {
    return axios({
        url: `/api/seminar/${payload.seq}/attend`,
        method: 'delete',
        isLoading: true,
        data: payload
      })
      .then(async res => {
        const resData = res.data

        if (resData) {
          if (resData.code === 'C0000') {
            // 취소 완료되었습니다.
            await openAsyncAlert({ message: t('board.msg.seminar.cancel_attend') })
            state.params.attendInfo = {}
            state.params.applyCnt = state.params.applyCnt - 1
          } else if (resData.code === 'C9999') {
            alert(resData.message)
          }
        }
      })
  }

  // 조회수 증가
  const updateSeminarViewCnt = (seq) => {
    return axios({
        url: `/api/seminar/${seq}/view-cnt`,
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

  // 댓글 등록
  const insertSeminarCommu = ({ seq, commuContent }, commentArea) => {
    return axios({
        url: `/api/seminar/${seq}/commu`,
        method: 'post',
        isLoading: true,
        data: { seq: seq, commuInfo : { content: commuContent } }
      })
      .then(async res => {
        const resData = res.data

        if (resData) {
          if (resData.code === 'C0000') {
            fetchSeminarCommu(seq)
            await (() => { state.params.commuContent = '' })()
            commentArea.init()
          }
        }
      })
      .catch(err => {
        console.log(err)
      })
  }

  // 댓글 삭제
  const deleteSeminarCommu = (seq, idx) => {
    return axios({
        url: `/api/seminar/${seq}/commu`,
        method: 'delete',
        isLoading: true,
        data: { seq: seq, commuInfo : state.params.commuList[idx] }
      })
      .then(res => {
        const resData = res.data

        if (resData) {
          if (resData.code === 'C0000') {
            fetchSeminarCommu(seq)
          } else if (resData.code === 'C9999') {
            alert(resData.message)
          }
        }
      })
      .catch(err => {
        console.log(err)
      })
  }

  // 댓글 리스트 조회
  const fetchSeminarCommu = (seq) => {
    return axios({
        url: `/api/seminar/${seq}/commu`,
        method: 'get'
      })
      .then(res => {
        const resData = res.data

        if (resData) {
          state.params.commuList = resData?.data ?? []
        }
      })
      .catch(err => {
        console.log(err)
      })
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  // 사용자 함수
  /////////////////////////////////////////////////////////////////////////////////////////

  // 세미나 장소 Change Event
  const fnChangeSmnPlcCls = () => {
    if (state.params.smnPlcCls === 'ON') {
      state.params.smnPlcRmkOff = ''
    } else {
      state.params.smnPlcRmkOn = ''
    }
  }

  // 세미나 장소 비고 세팅
  const fnSettingSmnPlcRmk = () => {
    state.params.smnPlcRmk = state.params.smnPlcCls === 'ON' ? state.params.smnPlcRmkOn : state.params.smnPlcRmkOff
  }

  // 좋아요 등록/해제
  const fnToggleLike = () => {
    if (state.params.seq) {
      if (Object.keys(state.params.likeInfo).length === 0) {
        insertSeminarLikeRecm(state.params.seq, 'L')
      } else {
        deleteSeminarLikeRecm(state.params, 'L')
      }
    }
  }

  // 추천 등록/해제
  const fnToggleRecommend = () => {
    if (state.params.seq) {
      if (Object.keys(state.params.recommendInfo).length === 0) {
        insertSeminarLikeRecm(state.params.seq, 'R')
      } else {
        deleteSeminarLikeRecm(state.params, 'R')
      }
    }
  }

  // 참석 신청
  const fnReqAttendSeminar = async () => {
    // 세미나에 참석 신청하시겠습니까?
    if (!await openAsyncConfirm({ message: t('board.msg.seminar.req_attend') })) {
      return
    }
    if (state.params.seq) {
      insertSeminarAttend(state.params.seq)
    }
  }

  // 참석 취소
  const fnCancelAttendSeminar = async () => {
    // 세미나 참석 신청을 취소하시겠습니까?
    if (!await openAsyncConfirm({ message: t('board.msg.seminar.req_cancel_attend') })) {
      return
    }
    deleteSeminarAttend(state.params)
  }

  // 취소사유 입력 영역 토글
  const fnToggleCancelArea = (flag) => {
    state.smnCancelAreaDispFlag = flag
  }

  // 세미나 취소
  const fnCancelSeminar = async () => {
    // 세미나를 취소하시겠습니까?
    if (!await openAsyncConfirm({ message: t('board.msg.seminar.cancel_smn') })) {
      return
    }
    updateSeminarCancel(state.params)
  }

  // 세미나 확정
  const fnConfirmSeminar = async () => {
    // 세미나를 개최하시겠습니까?
    if (!await openAsyncConfirm({ message: t('board.msg.seminar.confirm_smn') })) {
      return
    }
    updateSeminarConfirm(state.params.seq)
  }

  // 댓글 등록
  const fnRegComment = async (commentArea) => {
    const commentValue = commentArea.value

    if (commonUtils.isEmpty(commentValue)) {
      // 내용을 입력하세요.
      await openAsyncAlert({ message: t('common.msg.content_required') })
      return
    }

    if (!commonUtils.checkByte(commentValue, 900)) {
      // 내용이 900 Byte를 초과할 수 없습니다.
      await openAsyncAlert({ message: t('common.msg.byte_msg2', { byteSize: 900 }) })
      return
    }

    // 댓글을 등록하시겠습니까?
    if (!await openAsyncConfirm({ message: t('board.msg.seminar.save_comment') })) {
      return
    }
    insertSeminarCommu(state.params, commentArea)
  }

  // 댓글 삭제
  const fnDelComment = async (idx) => {
    // 댓글을 삭제하시겠습니까?
    if (!await openAsyncConfirm({ message: t('board.msg.seminar.delete_comment') })) {
      return
    }
    deleteSeminarCommu(state.params.seq, idx)
  }

  // 댓글 입력란 이벤트(textarea 영역 크기 조절)
  const fnTextareaResize = (e) => {
    e.target.style.height = `${68}px`
    e.target.style.height = `${e.target.scrollHeight}px`
  }

  const fnAction = handleSubmit(async values => {
    //validation 체크가 성공 후 들어오는 부분
    // 입력하신 내용으로 수정하시겠습니까?
    // 입력하신 내용으로 등록하시겠습니까?
    const message = state.params.seq ? t('board.msg.seminar.update') : t('board.msg.seminar.save')

    if (!await openAsyncConfirm({ message })) {
      return
    }

    state.params.files = state.uploadParams.items
    fnSettingSmnPlcRmk()

    isProgress = true

    if(state.params.seq){
      updateSeminar(state.params).finally(() => { isProgress = false })
    }else{
      insertSeminar(state.params).finally(() => { isProgress = false })
    }
  }, onInvalidSubmit)

  // 세미나 등록/수정
  const fnSave = () => {
    if (isProgress) {
      return
    }
    fnAction()
  }

  const fnCancel = async () => {
    // 입력하신 내용을 취소 하시겠습니까?
    if (!await openAsyncConfirm({ message: t('board.msg.seminar.cancel') })) {
      return
    }
    fnLinkList()
  }

  const fnDelete = async () => {
    // 정말로 삭제 하시겠습니까?
    if (!await openAsyncConfirm({ message: t('common.msg.delete_confirm_msg') })) {
      return
    }
    deleteSeminar(state.params.seq)
  }

  // 팝업 열기(원료 리스트)
  const fnOpenPopup = (compNm, width) => {
    state.popupContent = compNm

    let payload = {
      minWidth: width ?? 600
    }

    if (compNm === 'RawListPop') {
      payload.noLimitH = true
    }

    openAsyncPopup(payload)
      .then(res => {
        if (res.emitValue) {
          state.params.rawInfo = res.emitValue
        }
      })
      .catch(err => {
        console.log(err)
      })
      .finally(() => {
        state.popupContent = null
      })
  }

  // 세미나 등록/수정 - 원료 정보 삭제
  const fnDelTargetRaw = (idx) => {
    state.params.rawInfo.splice(idx, 1)
  }

  // 세미나 목록 - 검색
  const fnSearch = (pg) => {
    if(!pg){
      pg = 1
    }
    state.searchParams.nowPageNo = pg
    fetchSeminars(state.searchParams)
  }

  const fnSettingReReqSmnInfo = (params) => {
    state.params.content = params.content
    state.params.files = params.files
    state.params.rawInfo = params.rawInfo
    state.params.smnPlcCls = params.smnPlcCls
    state.params.title = params.title
    state.params.ytbUrl = params.ytbUrl

    if (state.params.smnPlcCls === 'ON') {
      state.params.smnPlcRmkOn = params.smnPlcRmk
    } else {
      state.params.smnPlcRmkOff = params.smnPlcRmk
    }
  }

  const fnCompareTodayBetweenReqDate = (date) => {
    return (commonUtils.convertStrToDate(state.params.smnRegStartDt) < date) && (date < commonUtils.convertStrToDate(state.params.smnRegEndDt))
  }

  const selectSmDtmFunc = (val) => {
    if (val) {
      state.params.smnEndDtm = val
    }
  }

  /////////////////////////////////////////////////////////////////////////////////////////

  return {
    t,
    myInfo,
    ...toRefs(state),
    errors,
    fnLinkList,
    fnLinkView,
    fnLinkReg,
    fnLinkReReq,
    fetchMainSeminar,
    fetchSeminar,
    insertSeminar,
    updateSeminar,
    updateSeminarViewCnt,
    fetchSeminarAttendList,
    fnChangeSmnPlcCls,
    fnSettingSmnPlcRmk,
    fnToggleLike,
    fnToggleRecommend,
    fnReqAttendSeminar,
    fnCancelAttendSeminar,
    fnToggleCancelArea,
    fnCancelSeminar,
    fnConfirmSeminar,
    fnRegComment,
    fnDelComment,
    fnTextareaResize,
    fnSave,
    fnCancel,
    fnDelete,
    fnOpenPopup,
    fnDelTargetRaw,
    fnSearch,
    fnSettingReReqSmnInfo,
    selectSmDtmFunc
  }
}
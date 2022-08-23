import { reactive, computed, toRefs, provide, inject } from 'vue'
import axios from '@/utils/customAxios'
import router from '../router'
import { useActions } from 'vuex-composition-helpers'
import { useStore } from 'vuex'

export const useIngredient = () => {
  const state = reactive({
    searchParams: {
      type:'',
      keyword: '',
      flag: '',
      nowPageNo: 1
    },
    list: [],
    page: {},
    hisList: [],
    hisPage: {},
    foodStdNmList: [],
    params: {
      rawCd: '',
      statusCd: '',
      files: [],
      sectionInfo: {}
    },
    popupContent: null,
    popParams: {},
    popSelectFunc: null
  })

  const t = inject('t')
  const commonUtils = inject('commonUtils')
  const checkRmqcAuth = commonUtils.checkAuth('SGG000011|SGG000012|SGG000013|SGG000014')
  const checkLaborAuth = commonUtils.checkAuth('SGG000003')
  const store = useStore()
  const compCd = store.getters.getMyInfo().compCd
  const { openAsyncConfirm, openAsyncAlert, openAsyncPopup } = useActions(['openAsyncConfirm', 'openAsyncAlert', 'openAsyncPopup'])

  const fetchIngredientList = (payload) => {
    return axios({
      url: '/api/ingrd',
      method: 'get',
      params: payload
    })
    .then(res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        state.page = resData.data.page
        state.list = resData.data.list
      } else {
        openAsyncAlert({ message: t(resData.message) })
      }
    })
  }

  const fetchMainIngredientList = (payload) => {
    return axios({
      url: '/api/ingrd/main',
      method: 'get',
      params: payload
    })
    .then(res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        state.list = resData.data.list
      }
    })
  }

  const fetchMainCommuIngredientList = (payload) => {
    return axios({
      url: '/api/ingrd/main-ingrd',
      method: 'get',
      params: payload
    })
    .then(res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        state.list = resData.data.list
      }
    })
  }

  const fetchIngredient = (recordCd, pageFlag) => {
    if (!recordCd) {
      return
    }
    return axios({
      url: '/api/ingrd/' + recordCd,
      method: 'get',
      params: {
        pageFlag: pageFlag
      }
    })
    .then(res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        state.params = resData.data
      } else {
        openAsyncAlert({ message: t(resData.message) })
      }
    })
  }

  const fetchMinIngredient = (rawCd) => {
    if (!rawCd) {
      return
    }
    return axios({
      url: '/api/ingrd/min/' + rawCd,
      method: 'get'
    })
    .then(res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        return resData.data
      }
    })
  }

  const fetchIngredientReturnData = async (recordCd) => {
    return axios({
      url: '/api/ingrd/' + recordCd,
      method: 'get'
    })
    .then(res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        return resData.data
      } else {
        openAsyncAlert({ message: resData.message })
      }
    })
  }

  const setUploadInfo = (arrUploadCd, targetKey) => {
    const uploadFileList = []

    for (const uploadCd of arrUploadCd) {
      const uploadParams = reactive({
        targetKey: targetKey,
        uploadCd: uploadCd,
        items: []
      })

      uploadFileList.push(uploadParams)
    }

    uploadFileList.forEach(vo => {
      provide('upload-' + vo.uploadCd, vo)
    })

    return uploadFileList
  }

  const addFoodRaw = (concdList) => {
    const idx = concdList.length > 0 ? concdList.length : 0
    const obj = {
      idx: idx,
      topIdx: idx,
      materialNm: '',
      parentIdx: 0,
      amount: '',
      sumAmount: '',
      step: 1,
      flagFunctional: '',
      functionalDesc: '',
      resourceCd: '',
      resourceNm: '',
      flagRadiation: '',
      allergyCd: 'BFA001',
      foodTypeCd: '',
      foodTypeNm: '',
      countryList: [],
      countryNmList: []
    }

    concdList.push(obj)
  }

  const delFoodRaw = (concdList, idx) => {
    concdList.splice(idx, 1)

    if (concdList.length == 0) {
      addFoodRaw(concdList)
    }
  }

  const addConcdNormal = (concdList) => {
    const obj = {
      concd: '',
      connm: '',
      casno: '',
      minAmount: '',
      cenAmount: '',
      maxAmount: '',
      sumAmount: '',
      noiAmount: '',
      noiSumAmount: '',
      origin: '',
      kind: '',
      extrType: '',
      extrProcess: '',
      extrPart: '',
      extrSolvYn: '',
      colorYn: '',
      colorLake: '',
      nanoYn: '',
      euAdqcYn: '',
      citesYn: '',
      annexYn: '',
      hazadCn: '',
      hazadEn: '',
      contLimit: '',
      directRegYn: '',
      uploadFileInfo: {
        'INGRD_CONT01': {
          targetKey: '',
          uploadCd: 'INGRD_CONT01',
          items: []
        },
        'INGRD_CONT02': {
          targetKey: '',
          uploadCd: 'INGRD_CONT02',
          items: []
        },
        'INGRD_CONT03': {
          targetKey: '',
          uploadCd: 'INGRD_CONT03',
          items: []
        },
        'INGRD_CONT04': {
          targetKey: '',
          uploadCd: 'INGRD_CONT04',
          items: []
        }
      }
    }

    concdList.push(obj)
  }

  const delConcdNormal = (concdList, idx) => {
    concdList.splice(idx, 1)

    if (concdList.length == 0) {
      addConcdNormal(concdList)
    }
  }

  const addAnnex = (annexList) => {
    const obj = {
      hazardCd: '',
      hazardEn: '',
      hazardCn: '',
      hazardEnEtc: '',
      hazardCnEtc: '',
      contLimit: '',
      uploadFileInfo: {
        'INGRD30': {
          targetKey: '',
          uploadCd: 'INGRD30',
          items: []
        }
      }
    }

    annexList.push(obj)
  }

  const delAnnex = (annexList, idx) => {
    annexList.splice(idx, 1)

    if (annexList.length === 0) {
      addAnnex(annexList)
    }
  }

  const addSafetyEtc = (safetyEtcList) => {
    const obj = {
      itemTitle: '',
      uploadFileInfo: {
        'INGRD32': {
          targetKey: '',
          uploadCd: 'INGRD32',
          items: []
        }
      }
    }

    safetyEtcList.push(obj)
  }

  const delSafetyEtc = (safetyEtcList, idx) => {
    safetyEtcList.splice(idx, 1)

    if (safetyEtcList.length === 0) {
      addSafetyEtc()
    }
  }

  const fnStep01Insert = (payload) => {
    return axios({
      method: 'post',
      url: '/api/ingrd',
      data: payload,
      isLoading: true
    })
    .then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        await openAsyncAlert({ message: t('common.msg.save_ok2') })
        return resData.data
      } else {
        openAsyncAlert({ message: t(resData.message) })
      }
    })
  }

  const fnStep01Update = (payload) => {
    return axios({
      method: 'put',
      url: `/api/ingrd/step01/${payload.recordCd}`,
      data: payload,
      isLoading: true
    })
    .then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        await openAsyncAlert({ message: t('common.msg.save_ok2') })
        return resData.code
      } else {
        openAsyncAlert({ message: t(resData.message) })
      }
    })
  }

  const fnStep02Update = (payload) => {
    return axios({
      method: 'put',
      url: `/api/ingrd/step02/${payload.recordCd}`,
      data: payload,
      isLoading: true
    })
    .then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        await openAsyncAlert({ message: t('common.msg.save_ok2') })
        return resData.code
      } else {
        openAsyncAlert({ message: t(resData.message) })
      }
    })
  }

  const fnStep03Update = (payload) => {
    return axios({
      method: 'put',
      url: `/api/ingrd/step03/${payload.recordCd}`,
      data: payload
    })
    .then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        if(payload.signType !== 'U'){
          await openAsyncAlert({ message: t('common.msg.save_ok2') })
        }
        return resData.code
      } else {
        openAsyncAlert({ message: t(resData.message) })
      }
    })
  }

  const fnStep03UpdateLabor = (payload) => {
    return axios({
      method: 'put',
      url: `/api/ingrd/step03/${payload.recordCd}`,
      data: payload,
      isLoading: true
    })
    .then(async res => {
      const resData = res.data
      if (resData.code !== 'C0000') {
        openAsyncAlert({ message: t(resData.message) })
      }
    })
  }

  const fnStep04Update = (payload) => {
    return axios({
      method: 'put',
      url: `/api/ingrd/step04/${payload.recordCd}`,
      data: payload,
      isLoading: true
    })
    .then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        await openAsyncAlert({ message: t('common.msg.save_ok2') })
        return resData.code
      } else {
        openAsyncAlert({ message: t(resData.message) })
      }
    })
  }

  const fnStep05Update = (payload) => {
    return axios({
      method: 'put',
      url: `/api/ingrd/step05/${payload.recordCd}`,
      data: payload,
      isLoading: true
    })
    .then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        await openAsyncAlert({ message: t('common.msg.save_ok2') })
        return resData.code
      } else {
        openAsyncAlert({ message: t(resData.message) })
      }
    })
  }

  const fnStep06Update = (payload) => {
    return axios({
      method: 'put',
      url: `/api/ingrd/step06/${payload.recordCd}`,
      data: payload,
      isLoading: true
    })
    .then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        await openAsyncAlert({ message: t('common.msg.save_ok2') })
        return resData.code
      } else {
        openAsyncAlert({ message: t(resData.message) })
      }
    })
  }

  const fnStep07Update = (payload) => {
    return axios({
      method: 'put',
      url: `/api/ingrd/step07/${payload.recordCd}`,
      data: payload,
      isLoading: true
    })
    .then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        await openAsyncAlert({ message: t('common.msg.save_ok2') })
        return resData.code
      } else {
        openAsyncAlert({ message: t(resData.message) })
      }
    })
  }

  const fnStep08Update = (payload) => {
    return axios({
      method: 'put',
      url: `/api/ingrd/step08/${payload.recordCd}`,
      data: payload,
      isLoading: true
    })
    .then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        await openAsyncAlert({ message: t('common.msg.save_ok2') })
        return resData.code
      } else {
        openAsyncAlert({ message: t(resData.message) })
      }
    })
  }

  const fnStep09Update = (payload) => {
    return axios({
      method: 'put',
      url: `/api/ingrd/step09/${payload.recordCd}`,
      data: payload,
      isLoading: true
    })
    .then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        await openAsyncAlert({ message: t('common.msg.save_ok2') })
        return resData.code
      } else {
        openAsyncAlert({ message: t(resData.message) })
      }
    })
  }

  const fnStep10Update = (payload) => {
    return axios({
      method: 'put',
      url: `/api/ingrd/step10/${payload.recordCd}`,
      data: payload,
      isLoading: true
    })
    .then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        await openAsyncAlert({ message: t('common.msg.save_ok2') })
        return resData.data
      } else {
        openAsyncAlert({ message: t(resData.message) })
      }
    })
  }

  const fnUpdateDiscontinued = (payload) => {
    return axios({
      method: 'put',
      url: `/api/ingrd/discontinued/${payload.recordCd}`,
      data: payload,
      isLoading: true
    })
    .then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        await openAsyncAlert({ message: t('ingredient.msg.discontinued_ok') })
        return resData.code
      } else {
        openAsyncAlert({ message: t(resData.message) })
      }
    })
  }

  const fnDeleteIngredient = (recordCd) => {
    return axios({
      method: 'put',
      url: `/api/ingrd/delete/${recordCd}`,
      isLoading: true
    })
    .then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        await openAsyncAlert({ message: t('common.msg.delete_ok') })
        return resData.data
      } else {
        openAsyncAlert({ message: t(resData.message) })
      }
    })
  }

  const checkAllValidation = (obj, rawTypeCd) => {
    let isOk = true
    const lastStep = rawTypeCd !== 'RT03' ? 7 : 6

    for (let i = 1; i <= lastStep; i++) {
      if (!obj['sctn' + i + 'Yn'] || obj['sctn' + i + 'Yn'] !== 'Y') {
        isOk = false
      }
    }

    return isOk
  }

  const fetchFoodStdNm = (keyword) => {
    return axios({
      method: 'get',
      url: '/api/ingrd/food-std-nm-list',
      params: {
        keyword: keyword
      }
    })
    .then(res => {
      const resData = res.data
      state.foodStdNmList = resData.data
    })
  }

  const fetchFoodStdNmList = (payload) => {
    return axios({
      method: 'get',
      url: '/api/ingrd/food-std-nm-list',
      params: payload
    })
    .then(res => {
      const resData = res.data
      state.page = resData.data.page
      state.list = resData.data.list
    })
  }

  const fetchFoodTypeList = (payload) => {
    return axios({
      method: 'get',
      url: '/api/ingrd/food-type-list',
      params: payload
    })
    .then(res => {
      const resData = res.data
      state.page = resData.data.page
      state.list = resData.data.list
    })
  }

  const fetchStep01FoodTypeList = (recordCd) => {
    return axios({
      method: 'get',
      url: '/api/ingrd/step01/food-type/' + recordCd
    })
    .then(res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        return resData.data.foodTypeList
      } else {
        openAsyncAlert({ message: t(resData.message) })
      }
    })
  }

  const fetchFragTypeInfo = (recordCd) => {
    return axios({
      url: '/api/ingrd/step01/frag-type/' + recordCd,
      method: 'get'
    })
    .then(res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        return resData.data.fragTypeList
      } else {
        openAsyncAlert({ message: t(resData.message) })
      }
    })
  }

  const fetchConInfo = (recordCd) => {
    return axios({
      url: '/api/ingrd/step04/con-info/' + recordCd,
      method: 'get'
    })
    .then(res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        return resData.data.concdList
      } else {
        openAsyncAlert({ message: t(resData.message) })
      }
    })
  }

  const fetchFoodRawInfo = (recordCd) => {
    return axios({
      url: '/api/ingrd/step04/food-raw-info/' + recordCd,
      method: 'get'
    })
    .then(res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        return resData.data.foodRawList
      } else {
        openAsyncAlert({ message: t(resData.message) })
      }
    })
  }

  const fetchStep07SubInfo = (recordCd) => {
    return axios({
      method: 'get',
      url: `/api/ingrd/step07/sub-info/${recordCd}`
    })
    .then(res => {
      const code = res.data.code

      if (code === 'C0000') {
        const resData = res.data.data
        return resData
      }
    })
  }

  const fetchStep09SubInfo = (recordCd) => {
    return axios({
      method: 'get',
      url: `/api/ingrd/step09/sub-info/${recordCd}`
    })
    .then(res => {
      const code = res.data.code

      if (code === 'C0000') {
        const resData = res.data.data
        return resData
      }
    })
  }

  const fetchStep10SubInfo = (payload) => {
    return axios({
      method: 'get',
      url: `/api/ingrd/step10/sub-info/${payload.recordCd}`,
      params: {
        rawTypeCd: payload.rawTypeCd
      }
    })
    .then(res => {
      const code = res.data.code

      if (code === 'C0000') {
        const resData = res.data.data
        return resData
      }
    })
  }

  const fetchRawHistoryList = (payload) => {
    return axios({
      method: 'get',
      url: `/api/ingrd/history-list`,
      params: payload
    })
    .then(res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        state.hisPage = resData.data.page
        state.hisList = resData.data.list
      } else {
        openAsyncAlert({ message: t(resData.message) })
      }
    })
  }

  const fetchRawAdditionInfo = (payload) => {
    return axios({
      method: 'get',
      url: '/api/ingrd/add-info',
      params: payload
    })
    .then(res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        return resData.data
      } else {
        openAsyncAlert({ message: t(resData.message) })
      }
    })
  }

  const fetchTempContCount = (recordCd) => {
    return axios({
      method: 'get',
      url: `/api/ingrd/temp-con-count/${recordCd}`
    })
    .then(res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        const tempCount = resData.data
        return tempCount
      } else {
        openAsyncAlert({ message: t(resData.message) })
      }
    })
  }

  const fnReviewResultSave = (payload) => {
    return axios({
      method: 'post',
      url: '/api/ingrd/review',
      data: payload,
      isLoading: true
    })
    .then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        await openAsyncAlert({ message: t('common.msg.process_ok2') })
        return resData.data
      } else {
        openAsyncAlert({ message: t(resData.message) })
      }
    })
  }

  const fnUpdateSapCd = (payload) => {
    return axios({
      method: 'put',
      url: `/api/ingrd/sapcd/${payload.recordCd}`,
      data: payload,
      isLoading: true
    })
    .then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        await openAsyncAlert({ message: t('common.msg.save_ok2') })
        return resData.data
      } else {
        openAsyncAlert({ message: t(resData.message) })
      }
    })
  }

  const fetchFileFixedInfo = (recordCd) => {
    return axios({
      method: 'get',
      url: `/api/ingrd/fix-info/${recordCd}`,
    })
    .then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        return resData.data
      } else {
        openAsyncAlert({ message: t(resData.message) })
      }
    })
  }

  const fetchStockInfo = (payload) => {
    return axios({
      method: 'get',
      url: '/api/ingrd/stock-info',
      params: payload
    })
    .then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        return resData.data
      } else {
        openAsyncAlert({ message: t(resData.message) })
      }
    })
  }

  const fnUpdateMaker = (payload) => {
    return axios({
      method: 'put',
      url: `/api/ingrd/maker/${payload.recordCd}`,
      data: payload,
      isLoading: true
    })
    .then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        await openAsyncAlert({ message: t('common.msg.save_ok2') })
        return resData.data
      } else {
        openAsyncAlert({ message: t(resData.message) })
      }
    })
  }

  const fetchCommentCount = (recordCd) => {
    return axios({
      method: 'get',
      url: `/api/ingrd/cmmt-count/${recordCd}`
    })
    .then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        state.params.commentCountInfo = resData.data
      }
    })
  }

  const fnDetail = (recordCd, showFlag) => {
    return router.push({ name: 'ingredient-view', query: {recordCd: recordCd, showFlag: showFlag }}).catch(() => {})
  }

  const fnDetailWithHistory = (query) => {
    return router.push({ name: 'ingredient-view', query: query }).catch(() => {})
  }

  const fnModify = (recordCd) => {
    return router.push({ name: 'ingredient-register', query: {recordCd: recordCd }}).catch(() => {})
  }

  const fnModifyReq = (recordCd) => {
    return router.push({ name: 'rawmodreq-register', query: {recordCd: recordCd }}).catch(() => {})
  }

  const fnRawModify = (recordCd) => {
    return router.push({ name: 'rawmodreq-modify', query: {recordCd: recordCd }}).catch(() => {})
  }

  const fnSafetyReg = (recordCd) => {
    return router.push({ name: 'ingredient-register', query: {recordCd: recordCd, safetyFlag: 'Y' }}).catch(() => {})
  }

  const fnRmqcReg = (recordCd) => {
    return router.push({ name: 'ingredient-register', query: {recordCd: recordCd, rmqcFlag: 'Y' }}).catch(() => {})
  }

  const fnLinkReg = () => {
    return router.push({ name: 'ingredient-register' }).catch(() => {})
  }

  const fnList = () => {
    return router.push({ name: 'ingredient-list'}).catch(() => {})
  }

  const fnHistoryBack = () => {
    router.go(-1)
  }

  const fnOpenPopup = (compNm, minWidth) => {
    state.popupContent = compNm

    openAsyncPopup({ minWidth: minWidth || 1000 })
      .then(res => {
      })
      .catch(err => {
        console.log(err)
      })
      .finally(() => {
        state.popupContent = null
      })
  }

  const setSaveFileInfo = (uploadFileList) => {
    const fileList = []
    uploadFileList.forEach(vo => {
      vo.items.forEach(item => {
        fileList.push(item)
      })
    })

    return fileList
  }

  const fnShowTempBtn = (obj, key) => {
    let isVisible = false

    if (!obj || obj[key] !== 'Y') {
      isVisible = true
    }

    return isVisible
  }

  const fnAddrSearch = (obj) => {
    new daum.Postcode({
        oncomplete: function(data) {
          let addr = ''
          let enAddr = ''
          if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
            addr = data.roadAddress;
            enAddr = data.addressEnglish;
          } else { // 사용자가 지번 주소를 선택했을 경우(J)
            addr = data.jibunAddress;
            enAddr = data.jibunAddressEnglish;
          }

          obj.manfAddrKo = addr
          obj.manfAddrEn = enAddr
        }
    }).open({
      left: (window.screen.width / 2) - (500 / 2),
      top: (window.screen.height / 2) - (600 / 2)
    });
  }

  const callBackFileUpload = (returnObj, fileValidationList) => {
    const uploadCd = returnObj.uploadCd
      const uploadList = returnObj.items

      if (!uploadList || uploadList.length === 0) {
        fileValidationList[uploadCd] = 'N'
      } else {
        fileValidationList[uploadCd] = 'Y'
      }
  }

  const fnValidationFile = (arrEssentialUploadKey, fileValidationList) => {
    let isOk = true

    arrEssentialUploadKey.forEach(key => {
      if (fileValidationList[key] !== 'Y') {
        isOk = false
      }
    })

    return isOk
  }

  const setHistoryBtn = (key, info, beforeInfo) => {
    let isVisible = false

    if (beforeInfo && beforeInfo.recordCd && (info[key] !== beforeInfo[key])) {
      isVisible = true
    }

    return isVisible
  }

  const showHistory = (infoData, beforeData) => {
    beforeData = beforeData || t('ingredient.label.blank')
    infoData = infoData || t('ingredient.label.blank')

    const beforeHtml = '<span class=\'data_txt txt_before\'>\'' + beforeData + '\'</span> →'
    const infoHtml = '<span class=\'data_txt\'>\'' + infoData + '\'</span>'

    openAsyncAlert({message: beforeHtml + '<br>' + infoHtml})
  }

  const fnOpenFileHistoryPopup = (params) => {
    state.popParams = {
      targetKey: params.targetKey,
      uploadCd: params.uploadCd,
      isReviewBtn: params.isReviewBtn,
      rawVer: params.rawVer,
      preRawVer: params.preRawVer,
      preRecordCd: params.preRecordCd
    }

    fnOpenPopup('UploadHisListPop', 560)
  }

  const fnOpenFileReviewPopup = (targetKey, uploadCd) => {
    state.popParams = {
      targetKey: targetKey,
      uploadCd: uploadCd
    }

    fnOpenPopup('UploadFileReviewPop', 560)
  }

  const setUploadFileCount = (statusCd) => {
    return statusCd === 'VS040' ? 0 : 1
  }

  const setUploadHistoryBtn = (fileList, preVer) => {
    let isVisible = false
    if (fileList && fileList.length > 0 && commonUtils.isNotEmpty(fileList[0].preVer) && fileList[0].preVer === preVer) {
      isVisible = true
    }

    return isVisible
  }

  const setUploadReviewBtn = (fileList, verStatusCd) => {
    let isVisible = false
    if (fileList && fileList.length > 1 && checkRmqcAuth && verStatusCd === 'VS040') {
      isVisible = true
    }

    return isVisible
  }

  const showNextStepBtn = (obj, key, verStatusCd) => {
    let isVisible = false

    if (fnShowTempBtn(obj, key) || (verStatusCd === 'VS040' && (checkLaborAuth || checkRmqcAuth || compCd !== 'AP'))) {
      isVisible = true
    }

    return isVisible
  }

  const fetchStep03Approval = async (recordCd) =>{
    if (!await openAsyncConfirm({ message: t('ingredient.msg.sign_ok_approval')})) {
      return
    }

    return axios({
      method: 'get',
      url: `/api/ingrd/step03/approval/${recordCd}`
    })
    .then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        openAsyncAlert({ message: t('common.msg.request_complete') })
      }
    })
  }

  return {
    ...toRefs(state),
    fetchIngredientList,
    fetchMainIngredientList,
    fetchIngredient,
    fetchIngredientReturnData,
    fnStep01Insert,
    fnStep01Update,
    fnStep02Update,
    fnStep03Update,
    fnStep03UpdateLabor,
    fnStep04Update,
    fnStep05Update,
    fnStep06Update,
    fnStep07Update,
    fnStep08Update,
    fnStep09Update,
    fnStep10Update,
    fnUpdateDiscontinued,
    fnUpdateSapCd,
    checkAllValidation,
    fetchFoodStdNm,
    fetchFoodStdNmList,
    fetchFoodTypeList,
    fetchFoodRawInfo,
    fnList,
    fnDetail,
    fnDetailWithHistory,
    fnModify,
    fnRawModify,
    fnModifyReq,
    fnSafetyReg,
    fnRmqcReg,
    fnLinkReg,
    fnHistoryBack,
    fnAddrSearch,
    setUploadInfo,
    setUploadFileCount,
    setSaveFileInfo,
    addConcdNormal,
    delConcdNormal,
    addFoodRaw,
    delFoodRaw,
    addAnnex,
    delAnnex,
    addSafetyEtc,
    delSafetyEtc,
    fetchStep07SubInfo,
    fetchStep09SubInfo,
    fetchStep10SubInfo,
    fetchConInfo,
    fetchFragTypeInfo,
    fetchStep01FoodTypeList,
    fnShowTempBtn,
    fnOpenPopup,
    callBackFileUpload,
    fnValidationFile,
    setHistoryBtn,
    showHistory,
    showNextStepBtn,
    fnOpenFileHistoryPopup,
    fnOpenFileReviewPopup,
    fetchMinIngredient,
    fetchRawHistoryList,
    fetchRawAdditionInfo,
    fetchTempContCount,
    fnReviewResultSave,
    setUploadHistoryBtn,
    setUploadReviewBtn,
    fetchFileFixedInfo,
    fetchStockInfo,
    fnUpdateMaker,
    fnDeleteIngredient,
    fetchCommentCount,
    fetchStep03Approval,
    fetchMainCommuIngredientList,
    checkLaborAuth,
    checkRmqcAuth,
    compCd
  }
}
import { reactive, computed, toRefs, provide } from 'vue'
import axios from '@/utils/customAxios'
import router from '../router'
import { useActions } from 'vuex-composition-helpers'
import { inject } from 'vue'
import sanitizeHtml from '@/utils/sanitizeHtml'

export const useIngredientModify = () => {
  const state = reactive({
    list: [],
    page: {},
    detail: {},
    popupContent: '',
    popParams: {},
    popSelectFunc: null
  })

  const t = inject('t')
  const { openAsyncConfirm, openAsyncAlert, openAsyncPopup } = useActions(['openAsyncConfirm', 'openAsyncAlert', 'openAsyncPopup'])

  const fetchModifyReqList = (payload) => {
    return axios({
      url: '/api/ingrd/mod-req-list',
      method: 'get',
      params: payload
    })
    .then(res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        state.page = resData.data.page
        state.list = resData.data.list
      } else {
        alert(resData.message)
      }
    })
  }

  const fetchModifyReqInfo = async (modSeq) => {
    return axios({
      url:  `/api/ingrd/mod-req/${modSeq}`,
      method: 'get'
    })
    .then(res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        const data = resData.data

        if (data) {
          data.modContent = sanitizeHtml(data.modContent)
          data.impactContent = sanitizeHtml(data.impactContent)
          state.detail = data
        }
      } else {
        alert(resData.message)
      }
    })
  }

  const fnModReqInsert = (payload) => {
    return axios({
      method: 'post',
      url: '/api/ingrd/mod-req',
      data: payload,
      isLoading: true
    })
    .then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        await openAsyncAlert({ message: t('common.msg.save_ok2') })
        return resData.data
      } else {
        openAsyncAlert({ message: resData.message })
      }
    })
  }

  const fnRawModInfoUpdate = (payload) => {
    return axios({
      method: 'put',
      url: `/api/ingrd/mod/${payload.recordCd}`,
      data: payload,
      isLoading: true
    })
    .then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        await openAsyncAlert({ message: t('common.msg.save_ok2') })
        return resData.data
      } else {
        openAsyncAlert({ message: resData.message })
      }
    })
  }

  const deleteModreqInfo = (payload) => {
    return axios({
      method: 'put',
      url: `/api/ingrd/mod/req-delete/${payload.modSeq}`,
      data: payload,
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

  const fnRawModify = (modSeq) => {
    return router.push({ name: 'rawmodreq-modify', query: {modSeq: modSeq }}).catch(() => {})
  }

  const fnRawModifyView = (modSeq) => {
    return router.push({ name: 'rawmodreq-view', query: {modSeq: modSeq }}).catch(() => {})
  }

  const fnModList = () => {
    return router.push({ name: 'rawmodreq-list'}).catch(() => {})
  }

  return {
    ...toRefs(state),
    fetchModifyReqList,
    fetchModifyReqInfo,
    fnModReqInsert,
    fnRawModInfoUpdate,
    fnRawModify,
    fnRawModifyView,
    fnModList,
    deleteModreqInfo
  }
}
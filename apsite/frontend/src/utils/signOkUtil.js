import axios from '@/utils/customAxios'

export default {
  fetchAccessToken () {
    return axios({
      url: '/api/auth/sign-token',
      method: 'get',
      params: {}
    })
    .then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        return resData.data.access_token
      } else {
        return ''
      }
    })
  },
  fetchDocumentCreate (params) {
    return axios({
      url: '/api/auth/sign-doc-create',
      method: 'post',
      isLoading: true,
      data: params
    })
    .then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        return resData.data.document
      } else {
        return ''
      }
    })
  },
  fetchDocumentStatusInfo (params) {
    return axios({
      url: '/api/auth/sign-doc-status',
      method: 'get',
      params: {
        documentId : params.documentId,
        accessToken : params.accessToken
      }
    })
    .then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        return resData.data.document
      } else {
        return ''
      }
    })
  },
  fetchDocumentDownload (params) {
    return axios({
      url: '/api/auth/sign-doc-download',
      method: 'get',
      params: {
        targetKey : params.targetKey,
        documentId : params.documentId,
        accessToken : params.accessToken,
        compCd: params.compCd
      }
    })
    .then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        return true
      } else {
        return false
      }      
    })    
  },
  fetcApprovalUrl (params) {
    return axios({
      url: '/api/auth/sign-approval-url',
      method: 'get',
      params: {
        accessToken : params.accessToken,
        documentId : params.documentId
      }
    })
    .then(async res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        return resData.data.document.approve
      }else{
        return ''
      }
    })
  }
}
import { reactive, toRefs, inject } from 'vue'
import axios from '@/utils/customAxios'
import { useActions } from 'vuex-composition-helpers'

export const useUploadFile = () => {
  const t = inject('t')
  const commonUtils = inject('commonUtils')
  const { openAsyncAlert } = useActions(['openAsyncAlert'])

  const state = reactive({
    filesMap: {},
    hisList: []
  })

  // const filesMap = computed(() => state.filesMap)

  const downloadFile = (seq, filename) => {
    return axios({
      url: '/api/files/' + seq,
      method: 'get',
      isLoding: true,
      responseType: 'blob'
    })
    .then(res => {
      let name = filename
      if (!name) {
        name = res.headers['filename']

        const contentDisposition = res.headers['content-disposition'] || ''
        if (contentDisposition) {
          name = decodeURIComponent(escape(name))
        } else {
          name = filename
        }
      }
      const blob = new Blob([res.data])
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.setAttribute('download', name)
      link.style.cssText = 'display:none'
      document.body.appendChild(link)
      link.click()
      link.remove()
    })
    // .error(err => {
    //   alert('권한이 없거나 파일이 존재 하지 않습니다.')
    // })
  }

  const fetchUploadFiles = (payload) => {
    // payload = { targetKey: '', uploadCd: '' }
    return axios({
        url: '/api/files',
        method: 'get',
        params: payload
      })
      .then(res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          state.filesMap[payload.uploadCd] = resData.data
        }
      })
  }

  const fetchUploadMultiFiles = (payload) => {
    return axios({
      url: '/api/files',
      method: 'get',
      params: payload
    })
    .then(res => {
      const resData = res.data
        if (resData.code === 'C0000') {
          const obj = {
            [`${payload.targetKey}` + '']: resData.data
          }
          state.filesMap[payload.uploadCd] = { ... state.filesMap[payload.uploadCd], ...obj}
        }
    })
  }

  const setModifyUploadFiles = (payload) => {
    // payload = { targetKey: '', uploadCd: '' }
    return axios({
        url: '/api/files',
        method: 'get',
        params: payload
      })
      .then(res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          return resData.data
        } else {
          return []
        }
      })
  }

  const addUploadFiles = async (evt, uploadCd, items, fileType) => {
    const files = evt.target.files
    if (files === undefined || files.length === 0) {
      return
    }

    if (fileType && fileType === 'image') {
      const fileList = [...files]

      for (let i = 0; i < fileList.length; i++) {
        const element = fileList[i];

        if (!(element.type.indexOf('image/') > -1)) {
          // 이미지를 첨부해 주세요.
          await openAsyncAlert({ message: t('common.msg.only_image') })
          return
        }
      }
    }

    const formData = new FormData()
    const len = files.length
    for (let i = 0; i < len; i++) {
      formData.append("files", files[i])
    }

    formData.append('uploadCd', uploadCd)
    formData.append('fileType', fileType)

    return axios({
        url: '/upload/files',
        method: 'post',
        headers: {
          'Content-Type': 'multipart/form-data'
        },
        data: formData,
        onUploadProgress: (progressEvent) => {
          const percentCompleted = Math.round((progressEvent.loaded * 100) / progressEvent.total)
        },
        isLoading: true
      })
      .then(res => {
        const resData = res.data

        if (resData.code === 'C0000') {
          resData.data.forEach(dto => {
            const item = {
              ...dto,
              newYn: 'Y',
              uploadCd: uploadCd
            }
            items.push(item)
          })

          evt.target.value = ''
          return resData
        } else {
          openAsyncAlert({ message: t(resData.message)})
          evt.target.value = ''
        }
      })
  }

  const delUploadFile = (obj) => {
    return axios({
      url: '/upload/files/delete',
      method: 'post',
      data: obj
    })
  }

  const fetchUploadFileHistory = (payload) => {
    return axios({
        url: '/api/files/history',
        method: 'get',
        params: {
          targetKey: payload.targetKey,
          uploadCd: payload.uploadCd,
          preRecordCd: payload.preRecordCd || 0,
          reviewFlag: payload.reviewFlag || 'N'
        }
      })
      .then(async res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          state.hisList = resData.data
        }
      })
  }

  const updateFixedFlag = (payload) => {
    return axios({
      url: '/api/files/fix-file',
      method: 'post',
      data: payload
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

  return {
    ...toRefs(state),
    fetchUploadFiles,
    fetchUploadMultiFiles,
    setModifyUploadFiles,
    addUploadFiles,
    delUploadFile,
    downloadFile,
    fetchUploadFileHistory,
    updateFixedFlag
  }
}
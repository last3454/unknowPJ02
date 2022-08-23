import { reactive, computed, toRefs } from 'vue'
import axios from '@/utils/customAxios'

export const useComm = () => {
  const state = reactive({
    list: [],
    laborList: [],
    page: {}
  })

  const fetchConList = (payload) => {
    return axios({
      method: 'get',
      url: '/api/common/con-list',
      params: payload
    })
    .then(res => {
      const resData = res.data
      state.page = resData.data.page
      state.list = resData.data.list
    })
  }

  const fetchLaborList = (keyword) => {
    return axios({
      method: 'get',
      url: '/api/common/labor-list',
      params: {
        keyword: keyword
      }
    })
    .then(res => {
      const resData = res.data
      state.laborList = resData.data
    })
  }

  return {
    ...toRefs(state),
    fetchConList,
    fetchLaborList
  }
}
import { reactive, computed } from 'vue'
import axios from '@/utils/customAxios'

export const useCode = () => {
  const state = reactive({
    codeGroupMaps: {}

  })

  const codeGroupMaps = computed(() => state.codeGroupMaps)

  const fetchCodeGroupMaps = async (arrGroupCd, buffer1) => {

    const params = {
      arrGroupCd: arrGroupCd
    }

    if (buffer1 !== undefined) {
      params.buffer1 = buffer1
    }

    return await axios({
      url: '/api/code/group-maps',
      method: 'get',
      params: params,
      paramsSerializer: paramsObj => {
        const params = new URLSearchParams()
        for (const key in paramsObj) {
          if (Array.isArray(paramsObj[key]) && paramsObj[key].length === 0) {
            continue
          }
          params.append(key, paramsObj[key])
        }
        return params.toString()
      }
    })
      .then(res => {
        const resData = res.data
        if (resData.code == 'C0000') {
          state.codeGroupMaps = { ...state.codeGroupMaps, ...resData.data}
        }
      })
  }

  const fetchTiumCodeGroupMaps = (arrGroupCd) => {
    const params = {
      arrGroupCd: arrGroupCd
    }

    return axios({
      url: '/api/code/tium-group-maps',
      method: 'get',
      params: params,
      paramsSerializer: paramsObj => {
        const params = new URLSearchParams()
        for (const key in paramsObj) {
          if (Array.isArray(paramsObj[key]) && paramsObj[key].length === 0) {
            continue
          }
          params.append(key, paramsObj[key])
        }
        return params.toString()
      }
    })
      .then(res => {
        const resData = res.data
        if (resData.code == 'C0000') {
          state.codeGroupMaps = { ...state.codeGroupMaps, ...resData.data}
        }
      })
  }

  const getCodeNm = (groupCd, code) => {
    const vo = (state.codeGroupMaps[groupCd] || []).find(dto => dto.code === code)
    if (vo === undefined) {
      return code
    }

    return vo.codeNm
  }

  return {
    codeGroupMaps,
    fetchCodeGroupMaps,
    fetchTiumCodeGroupMaps,
    getCodeNm
  }
}
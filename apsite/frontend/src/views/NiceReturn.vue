<template>
    <div></div>
</template>

<script>
import axios from '@/utils/customAxios'

import { useRoute } from 'vue-router'

export default {
  name: 'NiceReturn',
  setup() {

    const tokenKey = localStorage.getItem('tokenKey')

    if(!tokenKey){
      self.close()
    }

    const route = useRoute()
    const fnNiceDecode = async () =>{
      await axios({
        url: '/api/auth/nice-token-decrypt',
        method: 'get',
        params: {
           tokenKey : localStorage.getItem('tokenKey')
          ,tokenIv : localStorage.getItem('tokenIv')
          ,encData : route.query.enc_data
        }
      }).then(async (res)=>{
        const resData = res.data
        localStorage.removeItem("tokenKey")
        localStorage.removeItem("tokenIv")
        //parent 펑션 호출
        window.opener.outsideCall(resData.data)
        self.close()            
      })
    }
    fnNiceDecode()
    return {
      
    }
  },
}
</script>
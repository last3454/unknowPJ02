<template>
  <div>
    <form name="nice_form" id="nice_form">
      <input type="hidden" id="m" name="m" value="service" />
      <input type="hidden" id="token_version_id" name="token_version_id" v-model="niceParam.tokenVersionId" />
      <input type="hidden" id="enc_data" name="enc_data" v-model="niceParam.encData" />
      <input type="hidden" id="integrity_value" name="integrity_value" v-model="niceParam.integrityValue" />
    </form>
  </div>
</template>

<script>
import axios from '@/utils/customAxios'
import { useLogin } from '@/compositions/useLogin'
import { useActions } from 'vuex-composition-helpers'
import { reactive, inject, onMounted } from 'vue'

export default {
  name: 'NiceCall',
  props: {
    type: {
      type: String,
      default: 'di'
    }
  },
  setup(props, { emit }) {

    const t = inject('t')
    const { openAsyncAlert } = useActions(['openAsyncAlert'])

    const {
      fetchDiCheck
    } = useLogin()

    const niceParam = reactive({
      tokenVersionId: "",
      encData: "",
      integrityValue: ""
    })

    const onCalledOutside = async (rtnData) =>{
      if(props.type === 'no_di'){
        emit('returnCall', rtnData)
        window.outsideCall = null
      }else{
        fetchDiCheck(rtnData.di).then(async (resData) => {
          if(resData){
            await openAsyncAlert({ message: t('company.msg.desc27') }) // 해당 개인정보로 아이디가 이미 존재합니다.
            return
          }
          emit('returnCall', rtnData)
          window.outsideCall = null
        })
      }
    }

    onMounted(() => {
      window.outsideCall = onCalledOutside
    })

    const niceTokenCrypto = async () => {
      await axios({
        url: '/api/auth/nice-token-crypto',
        method: 'get'
      }).then((res)=>{
        const resData = res.data.data
        localStorage.setItem('tokenKey', resData.tokenKey)
        localStorage.setItem('tokenIv', resData.tokenIv)
        niceParam.integrityValue = resData.integrityValue
        niceParam.tokenVersionId = resData.tokenVersionId
        niceParam.encData = resData.encData
        setTimeout(() => {
          window.open('', 'nicePopup', 'width=500, height=550, top=100, left=100, fullscreen=no, menubar=no, status=no, toolbar=no, titlebar=yes, location=no, scrollbar=no');
          document.nice_form.action = "https://nice.checkplus.co.kr/CheckPlusSafeModel/checkplus.cb";
          document.nice_form.target = "nicePopup";
          document.nice_form.submit();
        }, 500);
      })
    }

    return {
      niceParam,
      niceTokenCrypto
    }
  },
}
</script>
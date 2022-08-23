<template>
  <div>
    <NiceApiCall ref="niceCall" @return-call="returnCall"></NiceApiCall>
    <div class="signup-txt">
      <p>{{t('join.msg.desc0')}}</p>
    </div>
    <fieldset class="login-input">
      <legend>{{t('join.label.auth_check')}}</legend>
      <div class="login-input-wrap">
        <div class="cont-input-wrap" :class="(errors.userNm ? 'error' : '')">
          <Field as="input" name="userNm" v-model="step1.userNm" :placeholder="t('join.label.user_nm')"></Field>
          <p class="error-msg">{{ errors.userNm }}</p>
        </div>
        <div class="cont-input-wrap" :class="(errors.registerAddr1 || errors.registerAddr2 ? 'error' : '')">
          <div class="signup-birth">
            <Field as="input" name="registerAddr1" v-model="step1.registerAddr1" :placeholder="t('join.label.birthday')" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');" maxlength="6"></Field>        
            <span>-</span>
            <div class="signup-birth-last">
              <Field as="input" name="registerAddr2" v-model="step1.registerAddr2" placeholder="●" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');" maxlength="1"></Field>
              <p>●●●●●●</p>
            </div>
          </div>
          <p class="error-msg">{{ errors.registerAddr1 }}</p>
        </div>
        <div class="cont-input-wrap" :class="(errors.mobileNo ? 'error' : '')">
          <Field as="input" name="mobileNo" v-model="step1.mobileNo" :placeholder="t('join.label.phone_number')" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');" maxlength="11"></Field>
          <p class="error-msg">{{ errors.mobileNo }}</p>
        </div>
      </div>
      <a href="javascript:;" @click.prevent="fnAction()" class="login-btn">{{t('join.label.auth_check')}}</a> <!-- AP 제공 본인인증 팝업창 선오픈 -->
    </fieldset>
  </div>  
</template>

<script>
import validation from '@/utils/validation'
import { useJoin } from '@/compositions/useJoin'

import { defineAsyncComponent, inject, ref } from 'vue'
import { useForm, useField, Field } from 'vee-validate'
import * as yup from 'yup'

export default {
  name : 'JoinStep01', 
  components: {
    Field,
    NiceApiCall: defineAsyncComponent(() => import('@/components/comm/NiceCall.vue')),
  },
  setup (props, { emit }) {

    const t = inject('t')
    const niceCall = ref(null)

    const {
      step1
    } = useJoin()

    validation.init()

    const schema = yup.object().shape({
      userNm: yup.string().required(),
      registerAddr1: yup.string().required(),
      registerAddr2: yup.string().required(),
      mobileNo: yup.string().required()
    })
    //blur은 기본
    const { handleSubmit, errors } = useForm({
      validationSchema: schema
    })
    const useFieldSet = ['userNm','registerAddr1', 'registerAddr2','mobileNo']
    useFieldSet.forEach(str => {
      useField(str)
    })

    const fnAction = handleSubmit(async values => {
      niceCall.value.niceTokenCrypto()
    },(values)=>{
      console.log(values)
    });

    const returnCall = (rtnData) => {
      step1.value.userDi = rtnData.di
      emit('stepSetting', step1.value)
    }

    return {
      t,
      errors,
      step1,
      niceCall,
      fnAction,
      returnCall
    }
  }
}
</script>
<template>
  <div>
    <div class="signup-txt">
      <p>{{ t('join.msg.desc22') }}</p>
    </div>
    <fieldset class="login-input signup-input">
      <legend>{{ t('join.label.input')}}</legend>
      <div class="login-input-wrap">
        <div class="cont-input-wrap">
          <input type="text" v-model="step2.userNm" readonly>
        </div>
        <div class="cont-input-wrap">
          <input type="text" v-model="step2.mobileNo" readonly>
        </div>
        <div class="cont-input-wrap" :class="(errors.loginId || (step2.loginId && !step2.isCheck) ? 'error' : '')">
          <Field as="input" name="loginId" v-model="step2.loginId" :placeholder="t('join.label.id_account')" @blur="fnIdCheck()" />
          <p class="error-msg">{{ errors.loginId }}</p>
          <p class="error-msg" v-show="!errors.loginId && step2.loginId && !step2.isCheck">{{ t('join.msg.desc20')}}</p>
        </div>
        <div class="cont-input-wrap" :class="(errors.loginPwCopy ? 'error' : '')">
          <Field as="input" type="password" name="loginPwCopy" v-model="step2.loginPwCopy" :placeholder="t('join.label.pass')" @input="fnPwCheck(event)" />
          <p class="error-msg">{{ errors.loginPwCopy }}</p>
        </div>
        <div class="cont-input-wrap" :class="(errors.loginPwTemp ? 'error' : '')">
          <Field as="input" type="password" name="loginPwTemp" v-model="step2.loginPwTemp" :placeholder="t('join.label.pass_confirm')" @input="fnPwCheck(event)"/>
          <p class="error-msg">{{ errors.loginPwTemp }}</p>
        </div>
        <div class="cont-input-wrap" :class="(errors.email || (step2.email && !step2.isMailCheck) ? 'error' : '')">
          <Field as="input" name="email" v-model="step2.email" :placeholder="t('join.label.email_adrr')"  @blur="fnMailCheck()"/>
          <p class="error-msg">{{ errors.email }}</p>
          <p class="error-msg" v-show="!errors.email && step2.email && !step2.isMailCheck">{{ t('join.msg.desc21')}}</p>
        </div>
        <div class="cont-input-wrap" :class="(errors.compNm ? 'error' : '')">
          <div class="cont-input-wrap__flex">
            <Field as="input" name="compNm" v-model="step2.compNm" :placeholder="t('join.label.join_company')" readonly />
            <a href="javascript:;" @click.prevent="fnOpenPopup('CompanySearchPop')" class="i-btn typeA large">{{ t('common.label.search2') }}</a>
          </div>
          <p class="error-msg">{{ errors.compNm }}</p>
        </div>
        <div class="cont-input-wrap" :class="(errors.userCompDeptNm ? 'error' : '')">
          <Field as="input" name="userCompDeptNm" v-model="step2.userCompDeptNm" placeholder="소속" :readonly="(!step2.compNm ?? false)" />
          <p class="error-msg">{{ errors.userCompDeptNm }}</p>
        </div>
        <div class="cont-input-wrap">
          <div class="input-check">
            <div class="input-check-fr">
              <input type="checkbox" name="isAgree" v-model="step2.isAgree" class="chk typeB" id="signupAgree" true-value="Y" false-value="N" />
              <label for="signupAgree"></label>
              <template v-if="langCd === 'ko'">
                <a href="javascript:;" @click.prevent="fnOpenPopup('PrivacyPolicyAgreePop')" class="signup-link">{{ t('join.label.user_privacy') }}</a>
                {{ t('join.msg.desc23') }}
              </template>
              <template v-else>
                {{ t('join.msg.desc23') }}
                <a href="javascript:;" @click.prevent="fnOpenPopup('PrivacyPolicyAgreePop')" class="signup-link">{{ t('join.label.user_privacy') }}</a>
              </template>
            </div>
          </div>
          <p class="error-msg">{{ errors.isAgree }}</p>
        </div>
      </div>
      <a href="javascript:;" @click.prevent="fnAction()" class="login-btn signup-btn">{{ t('join.label.join_request') }}</a>
    </fieldset>
    <teleport to="#common-modal">
      <ap-popup>
        <component
          :is="popupContent"
          :isAgree="step2.isAgree"
        />
      </ap-popup>
    </teleport>
  </div>
</template>

<script>

import validation from '@/utils/validation'
import { useJoin } from '@/compositions/useJoin'
import { useCompany } from '@/compositions/useCompany'

import { inject, toRefs, defineAsyncComponent } from 'vue'
import { useForm, useField, Field } from 'vee-validate'
import { useActions } from 'vuex-composition-helpers'
import { useStore } from 'vuex'
import { sha512 } from 'js-sha512'
import * as yup from 'yup';

export default {
  name:'JoinStep02',
  components: {
    Field,
    ApPopup: defineAsyncComponent(() => import('@/components/comm/ApPopup.vue')),
    CompanySearchPop : defineAsyncComponent(() => import('@/components/popup/CompanySearchPop.vue')),
    PrivacyPolicyAgreePop : defineAsyncComponent(() => import('@/components/popup/PrivacyPolicyAgreePop.vue')),
  },
  props: {
    parentInfo: {
      type: Object,
      default: null
    }
  },
  setup(props, { emit }) {

    const t = inject('t')
    const store = useStore()
    const { openAsyncAlert } = useActions(['openAsyncAlert'])
    const langCd = store.getters.getLangCd()

    const {
      step2,
      compParams,
      popupContent,
      fetchJoin,
      fetchIdCheck,
      fetchEmailCheck,
      fnOpenPopup
    } = useJoin()

    const {
      fetchSaveCompany
    } = useCompany()

    const { userNm, userDi, mobileNo } = toRefs(props.parentInfo)

    step2.value.userNm = userNm.value
    step2.value.userDi = userDi.value
    step2.value.mobileNo = mobileNo.value

    validation.init()

    const pwRegExp = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"])[A-Za-z\d\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]{8,}$/
    const schema = yup.object().shape({
      loginId: yup.string().required(t('join.msg.desc1')).test('',t('join.msg.desc2'), (values)=>{
        const checkId = ['admin', 'administrator', 'manager', 'super', 'master', 'system']
        if(checkId.includes(values)){
          return false
        }

        if(values && (values.toUpperCase().startsWith('AC') || values.toUpperCase().startsWith('AP'))){
          const str = values.substring(2)
          if(str.length <= 10){
            const check = /^[0-9]+$/;
            if(check.test(str)) {
              return false
            }
          }
          return true
        }else{
          return true
        }
      }),
      loginPwCopy: yup.string().matches(pwRegExp, t('join.msg.desc3')).required(),
      loginPwTemp: yup.string().required(t('join.msg.desc4')).oneOf([yup.ref('loginPwCopy'), null], t('join.msg.desc5')),
      email: yup.string().required().email(t('join.msg.desc6')),
      compNm: yup.string().required(t('join.msg.desc7')),
      userCompDeptNm: yup.string().required(t('join.msg.desc19')),
    })

    const { handleSubmit, errors } = useForm({
      validationSchema: schema
    })
    const useFieldSet = ['loginId','loginPwCopy', 'loginPwTemp', 'email', 'userCompDeptNm']
    useFieldSet.forEach(str => {
      useField(str)
    })

    const fnAction = handleSubmit(async values => {

      if(!step2.value.isCheck){
        return
      }

      if(!step2.value.isMailCheck){
        return
      }

      if(step2.value.isAgree !== 'Y'){
        await openAsyncAlert({ message: t('join.msg.desc8') })
        return
      }

      step2.value.loginPw = sha512(step2.value.loginPwCopy)
      fetchJoin(step2.value).then(async (res)=>{
        if (res) {
          if(step2.value.masterFlag === 'Y'){
            compParams.value.regUserCd = res.data
            await fetchSaveCompany(compParams.value).then((rvo)=>{
              emit('stepSetting', step2.value)
            })
          }else{
            emit('stepSetting', step2.value)
          }
        }
      })
    },(values)=>{
      console.log(values)
    })

    const fnIdCheck = () => {
      if(step2.value.loginId){
        fetchIdCheck(step2.value).then((flag)=>{
          step2.value.isCheck = flag
        })
      }
    }

    const fnMailCheck = () => {
      if(step2.value.email){
        fetchEmailCheck(step2.value).then((flag)=>{
          step2.value.isMailCheck = flag
        })
      }
    }

    const fnPwCheck = (e) => {
      console.log(e)

    }

    return {
      t,
      langCd,
      errors,
      step2,
      popupContent,
      fnIdCheck,
      fnMailCheck,
      fnPwCheck,
      fnOpenPopup,
      fnAction
    }
  }
}
</script>
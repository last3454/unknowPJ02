<template>
  <div class="modal-wrap">
    <div class="modal-content">
      <div class="modal-tit-wrap">
        <p class="modal-tit">{{ t('join.label.user_pass_expired') }}</p><!-- 비밀번호 만료안내 -->
      </div>
      <div class="modal-cont-wrap modal-cont-wrap2">
        <fieldset class="cont-input">
          <legend class="hidden">{{ t('join.label.user_pass_expired') }}</legend><!-- 비밀번호 만료안내 -->
          <ul class="cont-input-list">
            <li class="cont-input-list-item">
              <dt class="cont-input-tit" v-html="t('common.msg.login_0009')"></dt><!-- 기존 비밀번호를 제외한 신규 비밀번호로 입력 부탁드립니다. -->
              <dl>
                <dd class="cont-input-wrap" :class="(errors.nowLoginPw ? 'error' : '')">
                  <Field type="password" name="nowLoginPw" v-model="changeParams.nowLoginPw" :placeholder="t('join.label.now_pass')"/><!-- 현재 비밀번호 -->
                  <p class="error-msg">{{ errors.nowLoginPw }}</p>
                </dd>
                <dd class="cont-input-wrap" :class="(errors.loginPw ? 'error' : '')">
                  <Field type="password" name="loginPw" v-model="changeParams.loginPw" :placeholder="t('join.label.new_pass')"/><!-- 신규 비밀번호 -->
                  <p class="error-msg">{{ errors.loginPw }}</p>
                </dd>
                <dd class="cont-input-wrap" :class="(errors.loginPwTemp ? 'error' : '')">
                  <Field type="password" name="loginPwTemp" v-model="changeParams.loginPwTemp" :placeholder="t('join.label.new_pass_confirm')"/><!-- 신규 비밀번호 확인 -->
                  <p class="error-msg">{{ errors.loginPwTemp }}</p>
                </dd>
              </dl>
            </li>
          </ul>
        </fieldset>
        <div class="btn-wrap">
          <a href="#" @click.prevent="fnAction()" class="btn typeB long">
            <span>{{ t('join.label.change') }}</span><!-- 변경 -->
          </a>
          <a href="#" @click.prevent="fnActionNextTime()" class="btn typeA long">
            <span>{{ t('join.label.next_time_change') }}</span><!-- 30일 후에 변경 -->
          </a>
        </div>
      </div>
      <div class="modal-close-btn">
        <a href="javascript:;" @click.prevent="fnClose()" :title="t('common.label.pop_close')">
          {{ t('common.label.pop_close') }}<!-- 팝업창 닫기 -->
        </a>
      </div>
    </div>
  </div>
</template>

<script>

import validation from '@/utils/validation'
import { useLogin } from '@/compositions/useLogin'

import { inject, reactive } from 'vue'
import { useForm, useField, Field } from 'vee-validate'
import { useActions } from 'vuex-composition-helpers'
import * as yup from 'yup';
import { sha512 } from 'js-sha512'

export default {
  name: 'UserPassExpiredPop',
  props: {
    popParams: {
      type: Object,
      default: () => {
        return {
          userCd: ''
        }
      }
    }
  },
  components:{
    Field
  },
  setup(props) {
    const { openAsyncAlert, closeAsyncPopup } = useActions(['openAsyncAlert', 'closeAsyncPopup'])
    const t = inject('t')
    const {
      fetchPassChange,
      fetchPassChangeNextTime
    } = useLogin()

    const changeParams = reactive({
      nowLoginPw: '',
      loginPw: '',
      loginPwTemp: ''
    })

    validation.init()

    const pwRegExp = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"])[A-Za-z\d\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]{8,}$/
    const schema = yup.object().shape({
      nowLoginPw: yup.string().required(),
      loginPw: yup.string().matches(pwRegExp, t('join.msg.desc3')).required(), // 영문, 숫자, 특수문자 조합으로 8자리 이상 구성하세요.
      loginPwTemp: yup.string().required(t('join.msg.desc4')).oneOf([yup.ref('loginPw'), null], t('join.msg.desc5')) // 비밀번호 확인을 해주세요. / 비밀번호가 일치하지 않습니다.
    })

    const { handleSubmit, errors } = useForm({
      validationSchema: schema
    })

    const useFieldSet = ['nowLoginPw', 'loginPw', 'loginPwTemp']
    useFieldSet.forEach(str => {
      useField(str)
    })

    const fnAction = handleSubmit(async values => {
      const params = {
        type: 'PASS_EXPIRED',
        nowLoginPw: sha512(changeParams.nowLoginPw),
        loginPw: sha512(changeParams.loginPw)
      }
      fetchPassChange(params).then(async (flag) => {
        if(flag){
          await openAsyncAlert({ message: t('join.msg.desc15') }) // 비밀번호가 정상적으로 변경되었습니다.
          window.location.href = '/main'
        }
      })
    }, (values)=>{
      console.log(values)
    })

    const fnActionNextTime = () => {
      fetchPassChangeNextTime().then(async (flag) => {
        if(flag){
          window.location.href = '/main'
        }
      })
    }

    const fnClose = () => {
      closeAsyncPopup({ message: '닫기' })
    }

    return {
      t,
      errors,
      changeParams,
      fnAction,
      fnActionNextTime,
      fnClose
    }
  },
}
</script>
<template>
  <div class="inner">
    <nice-api-call ref="refNiceCall" @return-call="returnCall" :type="loginNiceType"></nice-api-call>
    <div class="box">
      <div class="box-cont">
        <div class="cont-top">
          <h2 class="cont-top-title">{{ t('myinfo.label.profile.title2') }}</h2><!-- 가입정보 수정 -->
        </div>
        <div class="cont-mid">
          <div class="cont-form noBottom">
            <fieldset class="cont-input cont-narrow">
              <legend class="hidden">{{ t('myinfo.label.profile.title2') }}</legend><!-- 가입정보 수정 -->
              <ul class="cont-input-list">
                <li class="cont-input-list-item">
                  <dl>
                    <dt class="cont-input-tit">{{ t('myinfo.label.profile.userNm') }}</dt><!-- 성명 -->
                    <dd class="cont-input-wrap">
                      <input type="text" v-model="personalInfo.userNm" disabled>
                    </dd>
                  </dl>
                </li>
                <li class="cont-input-list-item">
                  <dl>
                    <dt class="cont-input-tit">{{ t('myinfo.label.profile.mobileNo2') }}</dt><!-- 휴대폰 번호 -->
                    <dd :class="(errors.mobileNo) ? 'cont-input-wrap error' : 'cont-input-wrap'">
                      <Field
                        as="input"
                        v-model="personalInfo.mobileNo"
                        name="mobileNo"
                        type="input"
                        maxlength="11"
                        :placeholder="t('myinfo.msg.profile.desc2')"
                        ref="mobileNo"
                      /><!-- 휴대폰 번호를 입력하세요. -->
                      <p class="error-msg">{{ errors.mobileNo }}</p>
                    </dd>
                  </dl>
                </li>
                <li class="cont-input-list-item">
                  <dl>
                    <dt class="cont-input-tit">{{ t('myinfo.label.profile.loginId') }}</dt><!-- 계정 아이디 -->
                    <dd class="cont-input-wrap">
                      <input type="text" v-model="personalInfo.loginId" disabled>
                    </dd>
                  </dl>
                </li>
                <li class="cont-input-list-item">
                  <dl>
                    <dt class="cont-input-tit">{{ t('myinfo.label.profile.userCompDeptNm') }}</dt><!-- 소속 -->
                    <dd :class="(errors.userCompDeptNm) ? 'cont-input-wrap error' : 'cont-input-wrap'">
                      <Field
                        as="input"
                        v-model="personalInfo.userCompDeptNm"
                        name="userCompDeptNm"
                        type="input"
                        :placeholder="t('myinfo.msg.profile.desc10')"
                        ref="userCompDeptNm"
                      /><!-- 소속을 입력하세요. -->
                      <p class="error-msg">{{ errors.userCompDeptNm }}</p>
                    </dd>
                  </dl>
                </li>
                <li class="cont-input-list-item">
                  <dl>
                    <dt class="cont-input-tit">{{ t('myinfo.label.profile.email') }}</dt><!-- 이메일 주소 -->
                    <dd :class="(errors.email) ? 'cont-input-wrap error' : 'cont-input-wrap'">
                      <Field
                        as="input"
                        v-model="personalInfo.email"
                        name="email"
                        type="input"
                        :placeholder="t('myinfo.msg.profile.desc3')"
                        ref="email"
                      /><!-- 이메일 주소를 입력하세요. -->
                      <p class="error-msg">{{ errors.email }}</p>
                    </dd>
                  </dl>
                </li>
                <li class="cont-input-list-item">
                  <dl>
                    <a href="#" class="i-btn medium typeB sky-gray f-right" @click.prevent="fnNiceApiCall()">
                      <span>{{ t('myinfo.label.profile.chgPwd') }}</span><!-- 비밀번호 변경 -->
                    </a>
                  </dl>
                </li>
              </ul>
            </fieldset>
          </div>
        </div>
      </div>
      <div class="btn-wrap center">
        <a href="#" @click.prevent="onCancel('P')" class="btn typeB gray">
          <span>{{ t('myinfo.label.profile.btnCancel') }}</span><!-- 취소 -->
        </a>
        <a href="#" @click.prevent="fnModify()" class="btn typeB">
          <span class="mod">{{ t('myinfo.label.profile.btnSave') }}</span><!-- 저장 -->
        </a>
      </div>
    </div>
    <teleport to="#common-modal">
      <ap-popup>
        <component
          :is="popupContent"
          :pop-params="popParams"
          @callBack="popFunc"
        />
      </ap-popup>
    </teleport>
  </div>
</template>

<script>
import { defineAsyncComponent, ref, inject } from 'vue'
import { useRouter } from 'vue-router'
import { useActions } from 'vuex-composition-helpers'
import { useMyInfo } from '@/compositions/useMyInfo'
import { useLogin } from '@/compositions/useLogin'

// validation 관련
import { Field, useField, useForm } from 'vee-validate'
import { useStore } from 'vuex'
import validation from '@/utils/validation'
import * as yup from 'yup';

export default {
  name: 'MyPersonalModify',
  props: {
    userCd: String,
    compCd: String
  },
  components: {
    NiceApiCall: defineAsyncComponent(() => import('@/components/comm/NiceCall.vue')),
    ApPopup: defineAsyncComponent(() => import('@/components/comm/ApPopup.vue')),
    UserPassChangePop : defineAsyncComponent(() => import('@/components/popup/UserPassChangePop.vue')),
    ChkPwdPop: defineAsyncComponent(() => import('@/components/popup/ChkPwdPop.vue')),
    Field
  },
  setup(props) {
    const t = inject('t')
    const router = useRouter()
    const refNiceCall = ref(null)
    const commonUtils = inject('commonUtils')
    const { openAsyncAlert, openAsyncConfirm } = useActions(['openAsyncAlert', 'openAsyncConfirm'])

    const store = useStore()
    const myInfo = store.getters.getMyInfo()

    let loginNiceType = ref('')

    const {
      niceType,
      saveDiHistory
    } = useLogin()

    const {
      personalInfo,
      fnModifyInfo,
      onCancel,
      fnChgPwdPop,
      fnChkPwdPop,
      popupContent,
      popParams,
      popFunc,
      fetchProfileInfo
    } = useMyInfo()

     // 로그인 여부 체크
    if (commonUtils.isNotEmpty(myInfo.userCd)) {
      fetchProfileInfo()
    } else {
      router.push({ path: '/login' })
    }

    //////[S] Validation /////////////////////////////////////////////////////////////////

    validation.init()

    const schema = yup.object().shape({
      mobileNo: yup.string().required(),
      userCompDeptNm: yup.string().required(),
      email: yup.string().required().email(t('myinfo.msg.profile.desc4')) // 올바른 형식의 메일 주소를 입력해주세요.
    })

    const { handleSubmit, errors } = useForm({
      validationSchema: schema
    })

    const useFieldSet = ['mobileNo', 'userCompDeptNm', 'email']
    useFieldSet.forEach(str => {
      useField(str)
    })

    function onInvalidSubmit({ values, errors, results }) {
      //validation 체크 걸렸을때 들어오는 function
      console.log(values); // current form values
    }

    const fnAction = handleSubmit(async values => {
      // 입력하신 내용을 저장하시겠습니까?
      if (!await openAsyncConfirm({ message : t('myinfo.msg.profile.desc5') })) {
        return
      } else {
        //fnModifyInfo('P')
        fnPassChkPopOpen()
      }

    }, onInvalidSubmit)

    //////[S] Function /////////////////////////////////////////////////////////////////

    // 저장 버튼 이벤트
    const fnModify = () => {
      fnAction()
    }

    // 본인 인증 이벤트
    const fnNiceApiCall = async () => {
      loginNiceType.value = 'no_di'
      niceType.value = 'P'
      await refNiceCall.value.niceTokenCrypto()
    }

    const fnPassChkPopOpen = () => {
      popFunc.value = callBackPass
      fnChkPwdPop()
    }

    const callBackPass = (obj) => {
      if (obj.saveFlag === 'Y') {
        fnModifyInfo('P', obj.pwd)
      }
    }

    const returnCall = async (rtnData) => {
      // 비밀번호 찾는 화면으로 넘긴다.
      if(niceType.value === 'P'){
        await saveDiHistory(rtnData.di)
        if (rtnData && rtnData.di) {
          popParams.value = {
            userDi: rtnData.di,
            restClearYn : 'N'
          }
          fnChgPwdPop()
        }
      } else {
        router.push({path:'/join', query : {userNm: rtnData.name}})
        sessionStorage.setItem('mobileNo',rtnData.mobileno)
        sessionStorage.setItem('userDi',rtnData.di)
      }
    }

    return {
      t,
      fnChgPwdPop,
      errors,
      loginNiceType,
      commonUtils,
      onCancel,
      fnModify,
      personalInfo,
      popupContent,
      popParams,
      popFunc,
      refNiceCall,
      fnNiceApiCall,
      returnCall
    }
  }
}
</script>
<template>
  <div class="page-login">
    <nice-api-call ref="refNiceCall" @return-call="returnCall" :type="loginParams.niceType"></nice-api-call>
    <div id="wrap">
      <main id="login">
        <h1 class="login-top">
          <img src="@/assets/img/logo-amore.svg">
          <em>{{ t('join.label.title_login') }}</em><!-- 원료정보시스템 로그인 -->
        </h1>
        <div class="login-txt">
          <p v-html="t('join.msg.desc12')" /><!-- 아모레퍼시픽 고객연구센터에 로그인 하시려면<br>아모레퍼시픽 통합 아이디와 비밀번호를 사용하셔야 합니다. -->
          <p>{{ t('join.msg.desc13') }}</p><!-- AP 임직원은 계정 권한을 획득하신 후 접속하시기 바랍니다. -->
        </div>
        <fieldset class="login-input">
          <legend class="hidden">{{ t('join.label.title_login') }}</legend><!-- 원료정보시스템 로그인 -->
          <div class="login-input-wrap">
            <div class="cont-input-wrap">
              <input type="text" v-model="loginParams.loginId" placeholder="ID" autocomplete="false"/>
            </div>
            <div class="cont-input-wrap">
              <input type="password" class="password" v-model="loginParams.loginPw" placeholder="Password"  autocomplete="false" @keyup.enter="fnLogin()"/>
            </div>
          </div>
          <a href="#" class="login-btn" @click.prevent="fnLogin()">{{ t('join.label.login') }}</a><!-- 로그인 -->
        </fieldset>
        <ul class="login-links">
          <li>
            <a href="javascript:;" @click.prevent="fnOpenPopup('UserIdSearchPop')">{{ t('join.label.id_search') }}</a><!-- 아이디 찾기 -->
          </li>
          <li>
            <a href="javascript:;" @click.prevent="fnNiceCall('P')">{{ t('join.label.pass_search') }}</a><!-- 비밀번호 찾기 -->
          </li>
          <li>
            <a href="javascript:;" @click.prevent="fnNiceCall('J')">{{ t('join.label.join') }}</a><!-- 회원 가입 -->
          </li>
        </ul>
      </main>
    </div>
    <teleport to="#common-modal">
      <ap-popup>
        <component
          :is="popupContent"
          :pop-params="popParams"
        />
      </ap-popup>
    </teleport>
  </div>
</template>
<script>

import dynamicRouter from '../router/dynamicRouter'
import { useLogin } from '@/compositions/useLogin'
import { useActions } from 'vuex-composition-helpers'
import { reactive, defineAsyncComponent, inject, ref } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { sha512 } from 'js-sha512'

export default {
  name: 'LoginPage',
  components: {
    ApPopup: defineAsyncComponent(() => import('@/components/comm/ApPopup.vue')),
    UserIdSearchPop : defineAsyncComponent(() => import('@/components/popup/UserIdSearchPop.vue')),
    UserPassChangePop : defineAsyncComponent(() => import('@/components/popup/UserPassChangePop.vue')),
    UserPassExpiredPop : defineAsyncComponent(() => import('@/components/popup/UserPassExpiredPop.vue')),
    NiceApiCall: defineAsyncComponent(() => import('@/components/comm/NiceCall.vue')),
  },
  setup () {
    const t = inject('t')
    const refNiceCall = ref(null)

    const { openAsyncConfirm, openAsyncAlert } = useActions(['openAsyncConfirm', 'openAsyncAlert'])
    const router = useRouter()
    const store = useStore()
    const loginParams = reactive({
      loginId: '',
      loginPw: '',
      rememberMe: 'N',
      niceType : ''
    })

    const {
      niceType,
      popParams,
      popupContent,
      fnOpenPopup,
      saveDiHistory
    } = useLogin()

    const fnNiceCall = (type) => {
      if(type === 'P'|| type === 'R'){
        loginParams.niceType = 'no_di'
      }else{
        loginParams.niceType = 'yes_di'
      }
      niceType.value = type
      refNiceCall.value.niceTokenCrypto()
    }

    const returnCall = async (rtnData) => {
      //비밀번호 찾는 화면으로 넘긴다.
      if(niceType.value === 'P'|| niceType.value === 'R'){
        await saveDiHistory(rtnData.di)
        if (rtnData && rtnData.di) {
          popParams.value = {
            userDi: rtnData.di,
            restClearYn : 'N'
          }
          fnOpenPopup('UserPassChangePop')
        }
      } else {
        router.push({path:'/join', query : {userNm: rtnData.name}})
        sessionStorage.setItem('mobileNo',rtnData.mobileno)
        sessionStorage.setItem('userDi',rtnData.di)
      }
    }

    const fnLoginSuccess = (groups) => {
      //[S]메뉴 및 라우터 저장
      dynamicRouter.init()
      store.dispatch('findGnb', groups).then(menuRes => {
        if (menuRes !=null && menuRes.data.code === 'C0000') {
          router.push({ path: '/main' })
        }else{
          alert('메뉴 호출이 정상적으로 작동하지 않았습니다.')
        }
      })
      //[E]메뉴 및 라우터 저장
    }

    const fnLogin = (dupleYn = 'N') => {
      if (loginParams.loginId === '' || loginParams.loginPw === '') {
        openAsyncAlert({ message: t('common.msg.login_0001') })
        return
      }

      const params = {
        loginId: loginParams.loginId.trim(),
        loginPw: sha512(loginParams.loginPw),
        rememberMe: loginParams.rememberMe,
        loginDupleYn : dupleYn
      }

      store.dispatch('signin', params).then(async res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          fnLoginSuccess(resData.data.groups)
        } else if (resData.code === 'USER_PW_EXPIRED') {
          store.dispatch('findGnb', resData.data.groups).then(menuRes => {
            if (menuRes.data.code === 'C0000') {
              fnOpenPopup('UserPassExpiredPop')
            }
          })
        } else if (resData.code === 'USER_PW_SEVEN_EXPIRED') {
          await openAsyncAlert({ message: t(resData.message) })
          fnLoginSuccess(resData.data.groups)
        } else if (resData.code === 'USER_REST') {
          if (await openAsyncConfirm({ message: t( resData.message )})) {
            fnNiceCall('R')
            return
          }
        } else if (resData.code === 'USER_DUPLE') {
          if (await openAsyncConfirm({ message: t('common.msg.login_0013')})) {
            fnLogin('Y')
          }
        } else if (resData.code === 'USER_PW_FAIL') {
          await openAsyncAlert({ message: t('common.msg.login_0001') })
        } else {
          if (resData.data !== undefined) {
            await openAsyncAlert({ message: t(resData.message, resData.data) })
          } else {
            await openAsyncAlert({ message: t(resData.message) })
          }
        }
      })
      document.querySelector(".password").blur()
    }

    return {
      t,
      popParams,
      loginParams,
      popupContent,
      fnOpenPopup,
      fnLogin,
      fnNiceCall,
      returnCall,
      refNiceCall
    }
  }
}
</script>


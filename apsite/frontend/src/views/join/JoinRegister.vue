<template>
  <div class="page-login">
    <div id="wrap">
      <main id="login">
        <h1 class="login-top">
          <img src="@/assets/img/logo-amore.svg">
          <em>{{ t('join.label.title') }}</em>
        </h1>
        <join-step01 v-if="params.stepSeq === 1" @step-setting="stepSetting"></join-step01>
        <join-step02 v-else-if="params.stepSeq === 2" :parent-info="params" @step-setting="stepSetting"></join-step02>
        <join-step03 v-else :parent-info="params" @step-setting="stepSetting"></join-step03>
      </main>
    </div>
    <teleport to="#common-modal">
      <ap-alert ref="modal"></ap-alert>
    </teleport>    
  </div>
</template>

<script>

import { defineAsyncComponent, inject } from 'vue'
import { useJoin } from '@/compositions/useJoin'
import { useRoute } from 'vue-router'

export default {
  name: 'JoinRegister',
  components: {
    JoinStep01 : defineAsyncComponent(() => import('@/components/join/JoinStep01.vue')),
    JoinStep02 : defineAsyncComponent(() => import('@/components/join/JoinStep02.vue')),
    JoinStep03 : defineAsyncComponent(() => import('@/components/join/JoinStep03.vue'))
  },
  setup () {
    const t = inject('t')
    const route = useRoute()

    const {
      params
    } = useJoin()

    //개발 도중에 본인인증 팝업이 들어감으로써 1단계가 필요 없게 됨
    if(route.query.userNm){
      params.value.stepSeq = 2
      params.value.userNm = route.query.userNm
      params.value.userDi = sessionStorage.getItem('userDi')
      params.value.mobileNo = sessionStorage.getItem('mobileNo')
      sessionStorage.removeItem('userDi')
      sessionStorage.removeItem('mobileNo')
    }

    const stepSetting = (obj) => {
      params.value.stepSeq = obj.stepSeq
      params.value.mobileNo = obj.mobileNo
      params.value.userNm = obj.userNm
      params.value.userDi = obj.userDi
      params.value.masterFlag = obj.masterFlag
    }

    return {
      t,
      stepSetting,
      params
    }
  }
}
</script>

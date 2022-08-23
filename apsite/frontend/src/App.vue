<template>
  <component :is="resolveLayout" v-cloak>
    <router-view></router-view>
  </component>
  <spinner-layout :loading="store.getters.getLoading()"></spinner-layout>
</template>

<script>
import { computed, defineAsyncComponent, provide, readonly, watch, reactive } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { useStore } from 'vuex'
import commonUtils from '@/utils/commonUtils'
import { useActions } from 'vuex-composition-helpers'

export default {
  components: {
    LayoutBlank: defineAsyncComponent(() => import('@/views/LayoutBlank.vue')),
    LayoutError: defineAsyncComponent(() => import('@/views/LayoutError.vue')),
    LayoutContentType01: defineAsyncComponent(() => import('@/views/LayoutType01.vue')),
    SpinnerLayout: defineAsyncComponent(() => import('@/components/comm/Spinner.vue')),
  },
  setup () {

    //우클릭 방지
    document.oncontextmenu = function (e) {
      return false;
    }

    const { t } = useI18n()
    const route = useRoute()
    const router = useRouter()
    const store = useStore()
    const myInfo = store.getters.getMyInfo()
    const { openAsyncAlert } = useActions(['openAsyncAlert'])

    provide('t', readonly(t))
    provide('commonUtils', readonly(commonUtils))

    const resolveLayout = computed(() => {
      if (route.name === null || route.name === undefined) {
        return null
      }

      if (route.meta.layout === 'blank') {
        return 'layout-blank'
      } else if (route.meta.layout === 'error') {
        return 'layout-error'
      }

      return 'layout-content-type01'
    })

    const routeInfo = computed(() => {
      if (route.name === null || route.name === undefined) {
        return null
      }
      return {
        name: route.name,
        meta: route.meta || {}
      }
    })

    // 1시간 미사용시 로그아웃
    const logoutTimer = reactive({
      count: 0,
      showWarning: undefined,
      doLogout: undefined
    })

    const setLogoutTimers = (eventName) => {
      if (logoutTimer.showWarning !== undefined) {
        clearTimeout(logoutTimer.showWarning)
        clearTimeout(logoutTimer.doLogout)
        logoutTimer.showWarning = undefined
        logoutTimer.doLogout = undefined
      }

      logoutTimer.showWarning = setTimeout(() => {
        openAsyncAlert({ message: t('10분후 장시간 미사용으로 인해 <br/>로그아웃 처리 됩니다.') })
      }, 1000 * 60 * 50)

      logoutTimer.doLogout = setTimeout(() => {
        store.dispatch('signout').then(async res => {
          const resData = res.data
          if (resData.code === 'C0000') {
            await openAsyncAlert({ message: t('장시간 미사용으로 인해 <br/>로그아웃 처리 되었습니다.') })
            router.push({ path: '/login' })
          }
        })
      }, 1000 * 60 * 60)
    }

    // 로그인 여부 체크
    watch(() => routeInfo.value, (newVal, oldVal) => {
      if (newVal === null || newVal === undefined) {
        return
      }

      if (myInfo !== undefined && myInfo.userCd !== undefined) {
        return
      }

      const nowMyInfo = store.getters.getMyInfo()
      const authAnyone = newVal.meta.authAnyone !== undefined && newVal.meta.authAnyone
      if (!authAnyone && (nowMyInfo === undefined || nowMyInfo.userCd === undefined)) {
        window.location.href = '/login'
      }
      
      //그냥 다시 로그아웃 ->라우터 이동 한 다음에 다시 작동 시킬때 필요.
      if(Object.keys(nowMyInfo).length > 0){
        ['mousedown', 'scroll', 'keypress', 'load'].forEach((eventName) => {
          window.addEventListener(eventName, () => {
            setLogoutTimers(eventName)
          })
        })        
        setLogoutTimers('init')
      }else{
        ['mousedown', 'scroll', 'keypress', 'load'].forEach((eventName) => {
          window.removeEventListener(eventName, () => {})
        })
      }
    }, { immediate: true })

    //처음이나 새로 고침 했을 때 작동
    if(Object.keys(myInfo).length > 0){
      ['mousedown', 'scroll', 'keypress', 'load'].forEach((eventName) => {
        window.addEventListener(eventName, () => {
          setLogoutTimers(eventName)
        })
      })
      setLogoutTimers('init')
    }else{
      ['mousedown', 'scroll', 'keypress', 'load'].forEach((eventName) => {
        window.removeEventListener(eventName, () => {})
      })        
    }
    //~ 1시간 미사용시 로그아웃

    return {
      store,
      resolveLayout
    }
  }
}
</script>

<style lang="scss">

</style>

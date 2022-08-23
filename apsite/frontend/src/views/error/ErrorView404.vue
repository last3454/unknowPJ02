<template>
  <div class="error-cont">
    <!--error-cont-top-->
    <div class="error-cont-top">
      <div class="error-alert-icon"></div>
      <div class="error-title">
        <p v-html="t('common.msg.error_0002')"></p>
      </div>
    </div>
    <!--//error-cont-top-->

    <!--error-cont-mid-->
    <div class="error-cont-mid">
      <div class="error-des">
        <p class="error-des-top" v-html="t('common.msg.error_0003')"></p>
        <p class="error-des-bot">
          {{ t('common.msg.error_0004') }}
        </p>
      </div>
    </div>
    <!--//error-cont-mid-->
    <!--error-cont-bot-->
    <div class="error-cont-bot">
      <div class="btn-wrap center">
        <a href="#" @click.prevent="fnBack()" class="btn typeB gray"><span>{{ t('common.label.prev_page') }}</span></a>
        <a href="#" @click.prevent="fnHome()" class="btn typeB"><span>{{ t('common.label.home') }}</span></a>
      </div>
    </div>
    <!--//error-cont-bot-->
  </div>
</template>

<script>
import commonUtils from '@/utils/commonUtils'
import { useRouter } from 'vue-router'
import { inject } from 'vue'

export default {
  name: 'ErrorView404',
  components: {
  },
  setup() {
    const t = inject("t")

    const fnBack = () => {
      const historyBack = sessionStorage.getItem('historyBack')

      if (historyBack) {
        window.location.href = historyBack
      } else {
        fnHome()
      }
    }

    const router = useRouter()
    const fnHome = () => {
      router.push({path: '/main'})
    }

    return {
      t,
      fnHome,
      fnBack
    }
  },
}
</script>

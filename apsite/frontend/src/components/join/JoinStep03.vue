<template>
  <div class="login-txt">
    <p>{{ t('join.msg.desc24') }}</p>
    <p><em>{{ completeMsg }}</em></p>
    <p>{{ t('join.msg.desc25') }}</p>
  </div>
  <a href="#" @click.prevent="fnLogin()" class="login-btn">{{ t('join.label.login_page') }}</a>
</template>

<script>
import { inject } from 'vue'
import { useRouter } from 'vue-router'

export default {
  name: 'JoinStep3',
  props: {
    parentInfo: {
      type: Object,
      default: null
    }
  },
  setup(props) {
    const t = inject('t')
    const router = useRouter()
    let completeMsg = t('join.msg.desc26')
    if(props.parentInfo.masterFlag !== 'Y'){
      completeMsg = t('join.msg.desc27')
    }

    const fnLogin = () => {
      router.push({path: '/login', query: { },})
    }
    // const ini
    return {
      t,
      fnLogin,
      completeMsg
    }
  },
}
</script>
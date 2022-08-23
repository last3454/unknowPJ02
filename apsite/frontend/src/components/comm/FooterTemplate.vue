<template>
  <div class="footer-inner">
    <ul>
      <li v-if="langCd === 'ko'"><a href="#" @click.prevent="fnOpenPopup('PrivacyPolicyPop')">개인정보처리방침</a></li>
      <li>{{ t('main.label.footer.desc0') }}</li>
      <li>{{ t('main.label.footer.desc1') }}</li>
      <li>{{ t('main.label.footer.desc2') }}</li>
    </ul>
    <p>ⓒ AMOREPACIFIC R&amp;D. All rights reserved.</p>
  </div>
  <teleport to="#common-modal" v-if="popupContent">
    <ap-popup id="privacyPolicy">
      <component :is="popupContent"/>
    </ap-popup>
  </teleport>
</template>

<script>
import { inject, defineAsyncComponent, ref } from 'vue'
import { useActions } from 'vuex-composition-helpers'
import { useStore } from 'vuex'

export default {
  name: 'FooterTemplate',
  components: {
    ApPopup: defineAsyncComponent(() => import('@/components/comm/ApPopup.vue')),
    PrivacyPolicyPop: defineAsyncComponent(() => import('@/components/popup/PrivacyPolicyPop.vue'))
  },
  setup() {
    const t = inject('t')
    const store = useStore()
    const langCd = store.getters.getLangCd()
    const { openAsyncPopup } = useActions(['openAsyncPopup'])
    const popupContent = ref(null)

    const fnOpenPopup = (compNm, width, popupId) => {
      popupContent.value = compNm

      openAsyncPopup({ minWidth: width ?? 600, popupId: 'privacy-policy-popup' })
        .then(res => {})
        .catch(err => {
          console.log(err)
        })
        .finally(() => {
          popupContent.value = null
        })
    }

    return {
      t,
      langCd,
      popupContent,
      fnOpenPopup
    }
  }
}
</script>

<style>
</style>
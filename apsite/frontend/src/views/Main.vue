<template>
  <div>
    <div class="main-top">
      <div class="inner">
        <h2 class="main-top-tit">INGREDIENT INFORMATION<br>INFRA - SYSTEM</h2>
      </div>
    </div>
    <div class="main-cont">
      <div class="inner">
        <div class="main-cont-top">
          <MainNotice></MainNotice>
        </div>
        <div class="main-cont-mid">
          <MainCommunication></MainCommunication>
          <MainSeminar></MainSeminar>
          <!-- <MainCompanyAccount></MainCompanyAccount> -->
          <MainEmptyArea></MainEmptyArea>
        </div>
        <div
          v-if="myInfo.compCd !== 'AP'"
          class="main-cont-bot"
        >
          <MainIngredient></MainIngredient>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import { defineAsyncComponent } from 'vue'
import { useStore } from 'vuex'
export default {
  name: 'MainPage',
  components: {
    MainNotice: defineAsyncComponent(() => import('@/components/main/MainNotice.vue')),
    MainCommunication: defineAsyncComponent(() => import('@/components/main/MainCommunication.vue')),
    MainSeminar: defineAsyncComponent(() => import('@/components/main/MainSeminar.vue')),
    // MainCompanyAccount: defineAsyncComponent(() => import('@/components/main/MainCompanyAccount.vue')),
    MainEmptyArea: defineAsyncComponent(() => import('@/components/main/MainEmptyArea.vue')),
    MainIngredient: defineAsyncComponent(() => import('@/components/main/MainIngredient.vue'))
  },
  setup() {
    const store = useStore()
    const myInfo = store.getters.getMyInfo()

    return {
      myInfo
    }
  },
}
</script>

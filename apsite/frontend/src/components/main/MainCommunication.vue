<template>
  <div class="box main-box main-box-communication">
    <div class="box-cont">
      <div class="cont-top">
        <h3 class="cont-top-title"><i class="ct-communication"></i>{{ t('main.label.communication.title') }}</h3><!-- Communication -->
      </div>
      <div class="cont-mid">
        <ul class="main-comm-tab-list tab-list">
          <li :class="params.menuFlag === 'R' ? 'active' : ''">
            <a href="#" @click.prevent="fnChgFlag('R')">
              <span>{{ t('main.label.communication.material') }}</span><!-- 소통원료 목록 -->
            </a>
          </li>
          <li :class="params.menuFlag === 'M' ? 'active' : ''">
            <a href="#" @click.prevent="fnChgFlag('M')">
              <span>{{ t('main.label.communication.mine') }}</span> <!-- 나의 댓글 목록 -->
            </a>
          </li>
        </ul>
        <div class="main-comm-tab-cont-wrap tab-cont-wrap">
          <MainCommunicationMate v-if="params.menuFlag === 'R'"></MainCommunicationMate>
          <MainCommunicationMine v-if="params.menuFlag === 'M'"></MainCommunicationMine>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { defineAsyncComponent, inject } from 'vue'
import { useCommunication } from '@/compositions/useCommunication'

export default {
  name: 'MainCommunication',
  components: {
    MainCommunicationMate: defineAsyncComponent(() => import('@/components/main/MainCommunicationMate.vue')),
    MainCommunicationMine: defineAsyncComponent(() => import('@/components/main/MainCommunicationMine.vue'))
  },
  setup () {
    const t = inject('t')
    const commonUtils = inject('commonUtils')

    const {
      params,
      fnChgFlag
    } = useCommunication()

    return {
      t,
      params,
      commonUtils,
      fnChgFlag
    }
  }
}
</script>
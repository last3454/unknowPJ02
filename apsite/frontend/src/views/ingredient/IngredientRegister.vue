<template>
  <div class="box">
    <div class="box-cont last">
      <div class="cont-top cont-top2">
        <h2 class="cont-top-title">{{ t('ingredient.label.reg_raw') }}</h2>
      </div>
      <div class="cont-mid">
        <IngredientProgress
          v-if="params.rawTypeCd"
          @set-step-seq="setStepSeq"
          :step-seq="params.viewStepSeq"
          :section-info="params.sctnCompleteInfo"
          :raw-type-cd="params.rawTypeCd"
        ></IngredientProgress>
      </div>
    </div>
    <div class="box-cont">
      <IngredientRegisterStep01
        v-if="params.viewStepSeq === 1"
        :cmmt-count="params.commentCountInfo ? params.commentCountInfo.step1Count : 0"
        @set-step-seq="setStepSeq"
        @show-comment="showComment"
      ></IngredientRegisterStep01>
      <IngredientRegisterStep02
        v-if="params.viewStepSeq === 2"
        :cmmt-count="params.commentCountInfo ? params.commentCountInfo.step2Count : 0"
        @set-step-seq="setStepSeq"
        @show-comment="showComment"
      >
      </IngredientRegisterStep02>
      <IngredientRegisterStep03
        v-if="params.viewStepSeq === 3"
        :cmmt-count="params.commentCountInfo ? params.commentCountInfo.step3Count : 0"
        @set-step-seq="setStepSeq"
        @show-comment="showComment"
      >
      </IngredientRegisterStep03>
      <IngredientRegisterStep04
        v-if="params.viewStepSeq === 4"
        :cmmt-count="params.commentCountInfo ? params.commentCountInfo.step4Count : 0"
        @set-step-seq="setStepSeq"
        @show-comment="showComment"
      >
      </IngredientRegisterStep04>
      <IngredientRegisterStep05
        v-if="params.viewStepSeq === 5"
        :cmmt-count="params.commentCountInfo ? params.commentCountInfo.step5Count : 0"
        @set-step-seq="setStepSeq"
        @show-comment="showComment"
      >
      </IngredientRegisterStep05>
      <IngredientRegisterStep06
        v-if="params.viewStepSeq === 6"
        :cmmt-count="params.commentCountInfo ? params.commentCountInfo.step6Count : 0"
        @set-step-seq="setStepSeq"
        @show-comment="showComment"
      >
      </IngredientRegisterStep06>
      <IngredientRegisterStep07
        v-if="params.viewStepSeq === 7"
        :cmmt-count="params.commentCountInfo ? params.commentCountInfo.step7Count : 0"
        @set-step-seq="setStepSeq"
        @show-comment="showComment"
      >
      </IngredientRegisterStep07>
      <IngredientRegisterStep08
        v-if="params.viewStepSeq === 8"
        :cmmt-count="params.commentCountInfo ? params.commentCountInfo.step8Count : 0"
        @set-step-seq="setStepSeq"
        @show-comment="showComment"
      >
      </IngredientRegisterStep08>
      <IngredientRegisterStep09
        v-if="params.viewStepSeq === 9"
        :cmmt-count="params.commentCountInfo ? params.commentCountInfo.step9Count : 0"
        @show-comment="showComment"
      >
      </IngredientRegisterStep09>
      <IngredientRegisterStep10
        v-if="params.viewStepSeq === 10"
        :cmmt-count="params.commentCountInfo ? params.commentCountInfo.step10Count : 0"
        @show-comment="showComment"
      >
      </IngredientRegisterStep10>
      <aside id="bot-content" class="communication">
        <IngredientCommunication
          v-if="params.viewStepSeq && params.recordCd"
          :stepSeq="params.viewStepSeq"
          :showFlag="showFlag"
          :recordCd="params.recordCd"
          @reset-show-flag="resetShowFlag"
          @refresh-count="fetchCommentCount(params.recordCd)"
          ref="rawComment"
        />
      </aside>
    </div>
  </div>
</template>

<script>

import { defineAsyncComponent, inject, provide, ref } from 'vue'
import { useRoute  } from 'vue-router'
import { useIngredient } from '@/compositions/useIngredient'

export default {
  name: 'IngredientRegister',
  components: {
    IngredientProgress: defineAsyncComponent(() => import('@/components/ingredient/IngredientProgress.vue')),
    IngredientRegisterStep01: defineAsyncComponent(() => import('@/components/ingredient/IngredientRegisterStep01.vue')),
    IngredientRegisterStep02: defineAsyncComponent(() => import('@/components/ingredient/IngredientRegisterStep02.vue')),
    IngredientRegisterStep03: defineAsyncComponent(() => import('@/components/ingredient/IngredientRegisterStep03.vue')),
    IngredientRegisterStep04: defineAsyncComponent(() => import('@/components/ingredient/IngredientRegisterStep04.vue')),
    IngredientRegisterStep05: defineAsyncComponent(() => import('@/components/ingredient/IngredientRegisterStep05.vue')),
    IngredientRegisterStep06: defineAsyncComponent(() => import('@/components/ingredient/IngredientRegisterStep06.vue')),
    IngredientRegisterStep07: defineAsyncComponent(() => import('@/components/ingredient/IngredientRegisterStep07.vue')),
    IngredientRegisterStep08: defineAsyncComponent(() => import('@/components/ingredient/IngredientRegisterStep08.vue')),
    IngredientRegisterStep09: defineAsyncComponent(() => import('@/components/ingredient/IngredientRegisterStep09.vue')),
    IngredientRegisterStep10: defineAsyncComponent(() => import('@/components/ingredient/IngredientRegisterStep10.vue')),
    IngredientCommunication: defineAsyncComponent(() => import('@/components/ingredient/IngredientCommunication.vue'))
  },
  setup() {
    const t = inject('t')
    const commonUtils = inject('commonUtils')
    const route = useRoute()
    const checkRmqcAuth = commonUtils.checkAuth('SGG000011|SGG000012|SGG000013|SGG000014')
    const {
      params,
      fetchIngredient,
      fetchCommentCount
    } = useIngredient()

    const showFlag = ref(null)
    const viewStepSeq = ref(null)

    const getIngredientInfo = async (recordCd) => {
      await fetchIngredient(recordCd, '')
      params.value.viewStepSeq = params.value.stepSeq
    }

    const init = async () => {
      const recordCd = route.query.recordCd
      if (!recordCd) {
        params.value.stepSeq = 1
        params.value.viewStepSeq = 1
      } else {
        await getIngredientInfo(recordCd)

        const sctnCompleteInfo = params.value.sctnCompleteInfo

        if (sctnCompleteInfo.sctn8Yn === 'Y') {
          const safetyFlag = route.query.safetyFlag
          if (safetyFlag === 'Y') {
            params.value.stepSeq = 9
            params.value.viewStepSeq = 9
          }

          const rmqcFlag = route.query.rmqcFlag
          if (rmqcFlag === 'Y' && checkRmqcAuth) {
            params.value.stepSeq = 10
            params.value.viewStepSeq = 10
          }
        }
      }
    }

    init()
    provide('rawInfo', params)

    const setStepSeq = (seq) => {
      params.value.viewStepSeq = seq
    }
    
    const showComment = (step) => {
      viewStepSeq.value = ''

      setTimeout(() => {
        showFlag.value = true
        viewStepSeq.value = step
      }, 500)
    }

    const resetShowFlag = () => {
      showFlag.value = false
    }

    return {
      t,
      params,
      setStepSeq,
      showFlag,
      showComment,
      resetShowFlag,
      fetchCommentCount
    }
  },
}
</script>

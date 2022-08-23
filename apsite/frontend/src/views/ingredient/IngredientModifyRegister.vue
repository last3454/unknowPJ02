<template>
  <div class="box">
    <div class="box-cont">
      <div class="cont-top cont-top2">
        <h2 class="cont-top-title">{{ t('ingredient.label.raw_modify') }}</h2>
      </div>
      <div class="cont-mid">
        <div class="cont-form noTop noBottom">
          <fieldset class="cont-input">
            <legend class="hidden">{{ t('ingredient.label.raw_modify') }}</legend>
            <div class="sec-wrap div-modify-area" v-if="params.recordCd">
              <IngredientRegisterStep01
                action-flag="MODIFY"
                ref="step01"
                :cmmt-count="params.commentCountInfo.step1Count"
                @show-comment="showComment"
              >
              </IngredientRegisterStep01>
              <IngredientRegisterStep02
                action-flag="MODIFY"
                ref="step02"
                :cmmt-count="params.commentCountInfo.step2Count"
                @show-comment="showComment"
              >
              </IngredientRegisterStep02>
              <IngredientRegisterStep03
                action-flag="MODIFY"
                ref="step03"
                :cmmt-count="params.commentCountInfo.step3Count"
                @show-comment="showComment"
              >
              </IngredientRegisterStep03>
              <IngredientRegisterStep04
                action-flag="MODIFY"
                ref="step04"
                :cmmt-count="params.commentCountInfo.step4Count"
                @show-comment="showComment"
              >
              </IngredientRegisterStep04>
              <IngredientRegisterStep05
                action-flag="MODIFY"
                ref="step05"
                :cmmt-count="params.commentCountInfo.step5Count"
                @show-comment="showComment"
              >
              </IngredientRegisterStep05>
              <IngredientRegisterStep06
                action-flag="MODIFY"
                ref="step06"
                :cmmt-count="params.commentCountInfo.step6Count"
                @show-comment="showComment"
              >
              </IngredientRegisterStep06>
              <IngredientRegisterStep07
                v-if="params.rawTypeCd !== 'RT03'"
                action-flag="MODIFY"
                ref="step07"
                :cmmt-count="params.commentCountInfo.step7Count"
                @show-comment="showComment"
              >
              </IngredientRegisterStep07>
              <IngredientRegisterStep08
                action-flag="MODIFY"
                ref="step08"
                :cmmt-count="params.commentCountInfo.step8Count"
                @show-comment="showComment"
              >
              </IngredientRegisterStep08>
              <IngredientRegisterStep09
                action-flag="MODIFY"
                ref="step09"
                :cmmt-count="params.commentCountInfo.step9Count"
                @show-comment="showComment"
              >
              </IngredientRegisterStep09>
            </div>
          </fieldset>
        </div>
      </div>
      <div class="cont-bot">
        <div class="btn-wrap center">
          <a class="btn typeA gray" @click.prevent="fnHistoryBack()"><span>{{ t('common.label.confirm_cancel') }}</span></a>
          <template v-if="params.verStatusCd === 'VS060'">
            <a class="btn typeB sky" @click.prevent="fnTempSave"><span>{{ t('common.label.temp_save') }}</span></a>
            <a class="btn typeB" @click.prevent="fnSave('Y')"><span class="complete">{{ t('ingredient.label.save_and_submit') }}</span></a>
          </template>
          <template v-else>
            <a class="btn typeB" @click.prevent="fnSave('N')"><span>{{ t('common.label.save') }}</span></a>
          </template>
        </div>
      </div>
      <aside id="bot-content" class="communication">
        <IngredientCommunication
          v-if="params.recordCd"
          :recordCd="params.recordCd"
          :showFlag="showFlag"
          :stepSeq="viewStepSeq"
          @reset-show-flag="resetShowFlag"
          @refresh-count="fetchCommentCount(params.recordCd)"
          mode="VIEW"
          ref="rawComment"
        />
      </aside>
    </div>
  </div>
</template>

<script>
import { defineAsyncComponent, inject, provide, reactive, ref } from 'vue'
import { useRoute  } from 'vue-router'
import { useIngredient } from '@/compositions/useIngredient'
import { useActions } from 'vuex-composition-helpers'
import { useIngredientModify } from '@/compositions/useIngredientModify'

export default {
  name: 'IngredientModify',
  components: {
    IngredientRegisterStep01: defineAsyncComponent(() => import('@/components/ingredient/IngredientRegisterStep01.vue')),
    IngredientRegisterStep02: defineAsyncComponent(() => import('@/components/ingredient/IngredientRegisterStep02.vue')),
    IngredientRegisterStep03: defineAsyncComponent(() => import('@/components/ingredient/IngredientRegisterStep03.vue')),
    IngredientRegisterStep04: defineAsyncComponent(() => import('@/components/ingredient/IngredientRegisterStep04.vue')),
    IngredientRegisterStep05: defineAsyncComponent(() => import('@/components/ingredient/IngredientRegisterStep05.vue')),
    IngredientRegisterStep06: defineAsyncComponent(() => import('@/components/ingredient/IngredientRegisterStep06.vue')),
    IngredientRegisterStep07: defineAsyncComponent(() => import('@/components/ingredient/IngredientRegisterStep07.vue')),
    IngredientRegisterStep08: defineAsyncComponent(() => import('@/components/ingredient/IngredientRegisterStep08.vue')),
    IngredientRegisterStep09: defineAsyncComponent(() => import('@/components/ingredient/IngredientRegisterStep09.vue')),
    IngredientCommunication: defineAsyncComponent(() => import('@/components/ingredient/IngredientCommunication.vue'))
  },
  setup () {
    const t = inject('t')
    const route = useRoute()
    const showArea = ref(false)

    const step01 = ref(null)
    const step02 = ref(null)
    const step03 = ref(null)
    const step04 = ref(null)
    const step05 = ref(null)
    const step06 = ref(null)
    const step07 = ref(null)
    const step08 = ref(null)
    const step09 = ref(null)

    let showFlag = ref(null)
    let viewStepSeq = ref(null)

    let regParams = reactive({
      completeFlag: '',
      recordCd: '',
      prdCd: '',
      rawNmKo: '',
      rawNmEn: '',
      rawDesc: '',
      stdRawCd: '',
      bestMm: '',
      stzYn: '',
      mixedYn: '',
      gmoYn: '',
      fragDesc: '',
      manfNmKo: '',
      manfNmEn: '',
      manfCtrCd: '',
      manfAddrKo: '',
      manfAddrEn: '',
      manfCdRa: '',
      amlTestYn: '',
      residueYn: '',
      fragAlgnYn: '',
      fragChmNm: '',
      fragCasno: '',
      impYn: '',
      impMatrDesc: '',
      strgCndDesc: '',
      strgCndStatus: '',
      liqVsct: '',
      rawNmCn: '',
      rawRepNo: '',
      regCompNm: '',
      extrPrsEn: '',
      extrPrsCn: '',
      extrPartEn: '',
      extrPartCn: '',
      extrSolvEn: '',
      extrSolvCn: '',
      laborCd: '',
      smplLotNo: '',
      safetyEtcDesc: '',
      palmYn: '',
      agentSapcd: ''
    })

    const { openAsyncConfirm, openAsyncAlert } = useActions(['openAsyncConfirm', 'openAsyncAlert'])

    const {
      page,
      params,
      fetchIngredient,
      fnList,
      fnHistoryBack,
      fetchCommentCount
    } = useIngredient()

    const {
      detail,
      fetchModifyReqInfo,
      fnRawModInfoUpdate,
      fnModList
    } = useIngredientModify()

    const fnMappingParams = () => {
      let fileList = []

      step04.value.fnMappingParams()
      step07.value.fnMappingParams()

      const step01Files = step01.value.fnMappingParams()
      const step05Files = step05.value.fnMappingParams()
      const step06Files = step06.value.fnMappingParams()
      const step07Files = step07.value.fnMappingParams()
      const step09Files = step09.value.fnMappingParams()

      const obj = {
        ...step01.value.params,
        ...step02.value.params,
        ...step03.value.params,
        ...step04.value.params,
        ...step05.value.params,
        ...step06.value.params,
        ...step07.value.params,
        ...step08.value.params,
        ...step09.value.params
      }

      step01Files.forEach(vo => {
        fileList.push(vo)
      })

      step05Files.forEach(vo => {
        fileList.push(vo)
      })

      step06Files.forEach(vo => {
        fileList.push(vo)
      })

      step07Files.forEach(vo => {
        fileList.push(vo)
      })

      step09Files.forEach(vo => {
        fileList.push(vo)
      })

      regParams = { ...regParams, ...obj }
      regParams.files = fileList
    }

    const fnFoodMappingParams = () => {
      let fileList = []

      step04.value.fnMappingParams()

      const step01Files = step01.value.fnMappingParams()
      const step05Files = step05.value.fnMappingParams()
      const step06Files = step06.value.fnMappingParams()
      const step09Files = step09.value.fnMappingParams()

      const obj = {
        ...step01.value.params,
        ...step02.value.params,
        ...step03.value.params,
        ...step04.value.params,
        ...step05.value.params,
        ...step06.value.params,
        ...step08.value.params,
        ...step09.value.params
      }

      step01Files.forEach(vo => {
        fileList.push(vo)
      })

      step05Files.forEach(vo => {
        fileList.push(vo)
      })

      step06Files.forEach(vo => {
        fileList.push(vo)
      })

      step09Files.forEach(vo => {
        fileList.push(vo)
      })

      regParams = { ...regParams, ...obj }
      regParams.files = fileList
    }

    const T = Number('1e'+ 10)
    const fnAmountCheck = () => {
      let isOk = true
      const rawTypeCd = params.value.rawTypeCd
      if (rawTypeCd !== 'RT03') {
        const concdList = params.value.concdList
        const totalAmount = Number(concdList[0].sumAmount)
        if (totalAmount !== 100) {
          isOk = false
        }
      } else {
        const foodRawList = params.value.foodRawList

        let totalAmount = 0
        foodRawList.forEach((raw, idx) => {
          if (raw.topIdx === idx) {
            totalAmount += Math.round(Number(raw.amount) * T) / T
          }
        })

        totalAmount = Math.round(totalAmount * T) / T

        if (totalAmount !== 100) {
          isOk = false
        }
      }

      return isOk
    }

    const fnValidation = async () => {
      let isOk = true
      if (!await step01.value.fnValidation()) {
        isOk = false
      } 
      
      if(!await step04.value.fnValidation()) {
        isOk = false
      } 
      
      if (!await step05.value.fnValidation()) {
        isOk = false
      } 
      
      if (!await step06.value.fnValidation()) {
        isOk = false
      } 
      
      if (params.value.rawTypeCd !== 'RT03' && !await step07.value.fnValidation()) {
        isOk = false
      }
      
      if (!await step08.value.fnValidation()) {
        isOk = false
      }

      return isOk
    }

    const fnTempSave = async () => {
      regParams.completeFlag = 'N'

      if (params.value.rawTypeCd !== 'RT03') {
        fnMappingParams()
      } else {
        fnFoodMappingParams()
      }

      const result = await fnRawModInfoUpdate(regParams)

      if (result) {
        fnModList()
      }
    }

    const fnSave = async (completeFlag) => {
      if (!await fnValidation()) {
        openAsyncAlert({ message: t('common.msg.check_essential_item') })
        return
      }
      regParams.completeFlag = completeFlag
      if (params.value.rawTypeCd !== 'RT03') {
        fnMappingParams()
      } else {
        fnFoodMappingParams()
      }

      if (params.value.rawTypeCd !== 'RT03' && !fnAmountCheck()) {
        openAsyncAlert({ message: t('ingredient.msg.food_sum_amount_msg3') })
        return
      } else if (params.value.rawTypeCd === 'RT03' && !fnAmountCheck()) {
        if (!await openAsyncConfirm({ message: t('ingredient.msg.food_sum_amount_msg1')})) {
          return
        }
      }

      const result = await fnRawModInfoUpdate(regParams)

      if (result) {
        if (completeFlag === 'Y') { // 변경신청일때
          fnModList()
        } else {
          fnList()
        }
      }
    }

    // init
    const modSeq = route.query.modSeq
    const init = async () => {
      if (modSeq) {
        await fetchModifyReqInfo(modSeq)
        if (detail.value.recordCd && detail.value.recordCd !== 0) {
          fetchIngredient(detail.value.recordCd, '')
        }
      }

      const recordCd = route.query.recordCd
      if (recordCd) {
        fetchIngredient(recordCd, '')
      }
    }

    provide('rawInfo', params)
    init()

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
      page,
      params,
      showArea,
      fnTempSave,
      fnSave,
      step01,
      step02,
      step03,
      step04,
      step05,
      step06,
      step07,
      step08,
      step09,
      fnHistoryBack,
      showComment,
      resetShowFlag,
      showFlag,
      viewStepSeq,
      fetchCommentCount
    }
  }
}
</script>

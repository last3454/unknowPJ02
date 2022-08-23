<template>
  <div class="box">
    <div class="box-cont">
      <div class="cont-top cont-top2">
        <h2 class="cont-top-title">{{ t('ingredient.label.raw_detail_info2') }}</h2>
      </div>
      <div class="cont-mid">
        <div class="cont-form noTop">
          <IngredientHistoryInfo
            v-if="params.recordCd"
            @refresh="refresh"
            :pgNo="pgNo"
          >
          </IngredientHistoryInfo>
          <template v-if="params.recordCd && params.sctnCompleteInfo">
            <IngredientViewStep01
              v-if="showArea(params.sctnCompleteInfo.sctn1Yn)"
              :cmmt-count="params.commentCountInfo.step1Count"
              @close-pop="refreshPage"
              @show-comment="showComment"
            >
            </IngredientViewStep01>
            <IngredientViewStep02
              v-if="showArea(params.sctnCompleteInfo.sctn2Yn)"
              :cmmt-count="params.commentCountInfo.step2Count"
              @close-pop="refreshPage"
              @show-comment="showComment"
            >
            </IngredientViewStep02>
            <IngredientViewStep03
              v-if="params.signKey"
              :cmmt-count="params.commentCountInfo.step3Count"
              @show-comment="showComment"
            >
            </IngredientViewStep03>
            <IngredientViewStep04
              v-if="showArea(params.sctnCompleteInfo.sctn4Yn)"
              :cmmt-count="params.commentCountInfo.step4Count"
              @show-comment="showComment"
            >
            </IngredientViewStep04>
            <IngredientViewStep05
              v-if="showArea(params.sctnCompleteInfo.sctn5Yn)"
              :cmmt-count="params.commentCountInfo.step5Count"
              @show-comment="showComment"
            >
            </IngredientViewStep05>
            <IngredientViewStep06
              v-if="showArea(params.sctnCompleteInfo.sctn6Yn)"
              :cmmt-count="params.commentCountInfo.step6Count"
              @show-comment="showComment"
            >
            </IngredientViewStep06>
            <IngredientViewStep07
              v-if="params.rawTypeCd !== 'RT03' && showArea(params.sctnCompleteInfo.sctn7Yn)"
              :cmmt-count="params.commentCountInfo.step7Count"
              @show-comment="showComment"
            >
            </IngredientViewStep07>
            <IngredientViewStep08
              v-if="showArea(params.sctnCompleteInfo.sctn8Yn)"
              :cmmt-count="params.commentCountInfo.step8Count"
              @show-comment="showComment"
            >
            </IngredientViewStep08>
            <IngredientViewStep09
              v-if="showArea(params.sctnCompleteInfo.sctn9Yn)"
              :cmmt-count="params.commentCountInfo.step9Count"
              @show-comment="showComment"
            >
            </IngredientViewStep09>
            <IngredientViewStep10
              v-if="showRmqcArea(params.sctnCompleteInfo.sctn10Yn)"
              :cmmt-count="params.commentCountInfo.step10Count"
              @show-comment="showComment"
            >
            </IngredientViewStep10>
            <IngredientViewAddition
              v-if="showAdditionArea && params.sapCd"
            ></IngredientViewAddition>
          </template>
        </div>
      </div>
    </div>
    <aside id="bot-content" class="communication">
      <IngredientCommunication
        v-if="params.recordCd"
        :recordCd="params.recordCd"
        :showFlag="showFlag"
        :stepSeq="viewStepSeq"
        :rawTypeCd="params.rawTypeCd"
        @reset-show-flag="resetShowFlag"
        @refresh-count="fetchCommentCount(params.recordCd)"
        mode="VIEW"
        ref="rawComment"
        />
    </aside>
    <aside id="btn-sub-area">
      <div class="inner">
        <div class="cont-bot">
          <div class="btn-wrap flex">
            <div class="btn-wrap left">
              <a class="btn typeB sky" v-if="showSafetyBtn()" @click.prevent="fnSafetyReg(recordCd)"><span>{{ t('ingredient.label.safety') }}</span></a> <!-- 안전성 -->
              <a class="btn typeB sky" v-if="showRmqcBtn()" @click.prevent="fnRmqcReg(recordCd)"><span>{{ t('ingredient.label.rmqc') }}</span></a> <!-- RMQC -->
              <a class="btn typeB" v-if="showModreqBtn()" @click.prevent="fnModifyReq(recordCd)"><span class="mod">{{ t('ingredient.label.modreq') }}</span></a> <!-- 변경신청 -->
              <a class="btn typeB gray" v-if="showDiscontinuedBtn()" @click.prevent="fnDiscontinued()"><span>{{ t('ingredient.label.discountinued2') }}</span></a> <!-- 단종 -->
              <a class="btn typeB" v-if="showReviewBtn()" @click.prevent="fnReview()"><span class="complete">{{ t('ingredient.label.review') }}</span></a> <!-- 검토 -->
            </div>
            <div class="btn-wrap right">
              <!-- 수정 : RMQC 검토중 or 등록 상태에서만 가능 -->
              <a class="btn typeB gray" v-if="showDeleteBtn()" @click.prevent="fnDelete()"><span class="del">{{ t('common.label.delete') }}</span></a>
              <a class="btn typeA" v-if="showModifyBtn()" @click.prevent="goModify(recordCd)"><span class="mod">{{ t('common.label.modify') }}</span></a> <!-- 수정 -->
              <a class="btn typeB" @click.prevent="fnList()"><span class="list">{{ t('common.label.list') }}</span></a> <!-- 목록 -->
            </div>
          </div>
        </div>
      </div>
    </aside>
    <teleport to="#common-modal" v-if="showPopup">
      <ap-popup>
        <component
          :is="popupContent"
          :parent-info="popParams"
          @callBack="popSelectFunc"
        />
      </ap-popup>
    </teleport>
  </div>
</template>

<script>
import { defineAsyncComponent, inject, provide, ref, watch } from 'vue'
import { useActions } from 'vuex-composition-helpers'
import { useRoute  } from 'vue-router'
import { useIngredient } from '@/compositions/useIngredient'

export default {
  name: 'IngredientView',
  components: {
    IngredientViewStep01: defineAsyncComponent(() => import('@/components/ingredient/IngredientViewStep01.vue')),
    IngredientViewStep02: defineAsyncComponent(() => import('@/components/ingredient/IngredientViewStep02.vue')),
    IngredientViewStep03: defineAsyncComponent(() => import('@/components/ingredient/IngredientViewStep03.vue')),
    IngredientViewStep04: defineAsyncComponent(() => import('@/components/ingredient/IngredientViewStep04.vue')),
    IngredientViewStep05: defineAsyncComponent(() => import('@/components/ingredient/IngredientViewStep05.vue')),
    IngredientViewStep06: defineAsyncComponent(() => import('@/components/ingredient/IngredientViewStep06.vue')),
    IngredientViewStep07: defineAsyncComponent(() => import('@/components/ingredient/IngredientViewStep07.vue')),
    IngredientViewStep08: defineAsyncComponent(() => import('@/components/ingredient/IngredientViewStep08.vue')),
    IngredientViewStep09: defineAsyncComponent(() => import('@/components/ingredient/IngredientViewStep09.vue')),
    IngredientViewStep10: defineAsyncComponent(() => import('@/components/ingredient/IngredientViewStep10.vue')),
    IngredientViewAddition: defineAsyncComponent(() => import('@/components/ingredient/IngredientViewAddition.vue')),
    IngredientCommunication: defineAsyncComponent(() => import('@/components/ingredient/IngredientCommunication.vue')),
    IngredientHistoryInfo: defineAsyncComponent(() => import('@/components/ingredient/IngredientHistoryInfo.vue')),
    ApPopup: defineAsyncComponent(() => import('@/components/comm/ApPopup.vue')),
    IngredientReviewPop: defineAsyncComponent(() => import('@/components/popup/IngredientReviewPop.vue')),
    IngredientDiscontinuedPop: defineAsyncComponent(() => import('@/components/popup/IngredientDiscontinuedPop.vue'))
  },
  setup () {
    const t = inject('t')
    const commonUtils = inject('commonUtils')
    const route = useRoute()
    const showPopup = ref(false)
    const { closeAsyncPopup, openAsyncAlert, openAsyncConfirm } = useActions(['closeAsyncPopup', 'openAsyncAlert', 'openAsyncConfirm'])

    let beforeInfo = ref({})
    let recordCd = ref(null)
    let showFlag = ref(null)
    let viewStepSeq = ref(null)
    let pgNo = ref(null)
    const showAdditionArea = ref(false)
    const {
      params,
      fetchIngredient,
      fetchIngredientReturnData,
      fnRawModify,
      fnModify,
      fnModifyReq,
      fnSafetyReg,
      fnRmqcReg,
      fnList,
      fnDetailWithHistory,
      fnOpenPopup,
      popupContent,
      popParams,
      popSelectFunc,
      fnDeleteIngredient,
      checkRmqcAuth,
      checkLaborAuth,
      compCd,
      fetchCommentCount
    } = useIngredient()

    const init = async () => {
      params.value = {}
      recordCd = route.query.recordCd
      setTimeout(() => {
        showFlag.value = route.query.showFlag ? true : false
      }, 1000)
      pgNo = route.query.pgNo
      await fetchIngredient(recordCd, 'VIEW')

      if (params.value.preRecordCd) {
        beforeInfo.value = await fetchIngredientReturnData(params.value.preRecordCd)
      }

      showAdditionArea.value = true // SAP 코드 수정시 추가 정보 영역 refresh를 위해
    }

    init()

    provide('rawDetailInfo', params)
    provide('beforeInfo', beforeInfo)

    const showArea = (saveFlag) => {
      let isVisible = true

      if ((compCd === 'AP' && saveFlag !== 'Y') || (compCd !== 'AP' && commonUtils.isEmpty(saveFlag))) {
        isVisible = false
      }

      return isVisible
    }

    const showRmqcArea = (saveFlag) => {
      let isVisible = false
      if (commonUtils.isNotEmpty(saveFlag) && (saveFlag === 'Y' || compCd === 'AP')) {
        isVisible = true
      }

      return isVisible
    }

    const refresh = (obj) => {
      fnDetailWithHistory(obj)
    }

    const fnDiscontinued = () => { // 단종
      showPopup.value = true

      popParams.value = {
        recordCd: recordCd
      }

      popSelectFunc.value = closePopup
      fnOpenPopup('IngredientDiscontinuedPop', 420)
    }

    const fnReview = () => { // 검토
      if (!params.value.sapCd) {
        openAsyncAlert({ message: t('ingredient.msg.sapcd_required') })
        return
      }
      showPopup.value = true
      popParams.value = {
        recordCd: recordCd,
        rawTypeCd: params.value.rawTypeCd
      }

      popSelectFunc.value = closePopup
      fnOpenPopup('IngredientReviewPop', 420)
    }

    const closePopup = (refreshFlag) => {
      showPopup.value = false
      closeAsyncPopup({ message: '닫기' })
      if (refreshFlag === 'Y') {
        refreshPage('Y')
      }
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

    const refreshPage = (refreshFlag) => {
      if (refreshFlag === 'Y') {
        params.value.recordCd = ''
        showAdditionArea.value = false
        setTimeout(async () => {
          params.value.recordCd = recordCd
          await init()
        }, 100)
      }
    }

    const showSafetyBtn = () => {
      let isVisible = false
      const verStatusCd = params.value.verStatusCd

      const completeInfo = params.value.sctnCompleteInfo
      const sctn9Yn = completeInfo && completeInfo.sctn9Yn ? completeInfo.sctn9Yn : ''

      if (verStatusCd === 'VS040' && (checkLaborAuth || checkRmqcAuth || (compCd !== 'AP' && commonUtils.isNotEmpty(sctn9Yn)) )) {
        isVisible = true
      }

      return isVisible
    }

    const fnDelete = async () => {
      if (!await openAsyncConfirm({ message: t('common.msg.delete_confirm_msg') })) {
        return
      }

      const result = await fnDeleteIngredient(params.value.recordCd)
      if (result) {
        fnList()
      }
    }

    const showModreqBtn = () => {
      let isVisible = false
      if (params.value.recordCd) {
        const verStatusCd = params.value.verStatusCd
        const statusCd = params.value.statusCd
  
        if ((statusCd === 'RS003' || statusCd === 'RS004') && verStatusCd && 'VS050|VS051|VS055|VS100|VS999'.indexOf(verStatusCd) > -1) {
          isVisible = true
        }
  
        const maxVerStatusCd = params.value.maxVerStatusCd
        if (maxVerStatusCd && 'VS050|VS051|VS055|VS060|VS070|VS080|VS100|VS999'.indexOf(maxVerStatusCd) > -1) {
          isVisible = false
        }
  
        if (!checkLaborAuth && !checkRmqcAuth && compCd === 'AP') {
          isVisible = false
        }
      }

      return isVisible
    }

    const showReviewBtn = () => {
      let isVisible = false
      const verStatusCd = params.value.verStatusCd

      if ((checkLaborAuth || checkRmqcAuth) && verStatusCd === 'VS070') {
        isVisible = true
      }

      return isVisible
    }

    const showModifyBtn = () => {
      let isVisible = false
      const verStatusCd = params.value.verStatusCd

      if (verStatusCd && 'VS000|VS010|VS040'.indexOf(verStatusCd) > -1 && (checkLaborAuth || compCd !== 'AP')) {
        isVisible = true
      }

      return isVisible
    }

    const showRmqcBtn = () => {
      let isVisible = false
      const verStatusCd = params.value.verStatusCd

      if (verStatusCd === 'VS040' && checkRmqcAuth) {
        isVisible = true
      }

      return isVisible
    }

    const showDiscontinuedBtn = () => {
      let isVisible = false
      const statusCd = params.value.statusCd

      if (statusCd === 'RS004' && ((checkRmqcAuth || checkLaborAuth) || compCd !== 'AP')) {
        isVisible = true
      }

      return isVisible
    }

    const showDeleteBtn = () => {
      let isVisible = false
      const statusCd = params.value.statusCd

      if (statusCd === 'RS001' && ((checkRmqcAuth || checkLaborAuth) || compCd !== 'AP')) {
        isVisible = true
      }

      return isVisible
    }

    const goModify = (recordCd) => {
      const verStatusCd = params.value.verStatusCd

      if (verStatusCd === 'VS040') {
        fnRawModify(recordCd)
      } else {
        fnModify(recordCd)
      }
    }

    watch(() => route.query, (newVal) => {
      params.value.recordCd = ''
      if (newVal) {
        setTimeout(() => {
          params.value.recordCd = newVal.recordCd
          init()
        }, 100)
      }
    })

    return {
      t,
      params,
      compCd,
      goModify,
      fnModifyReq,
      fnList,
      recordCd,
      showArea,
      showRmqcArea,
      fnSafetyReg,
      fnRmqcReg,
      refresh,
      pgNo,
      showFlag,
      fnDiscontinued,
      fnReview,
      popupContent,
      popParams,
      popSelectFunc,
      showPopup,
      refreshPage,
      fnDelete,
      showAdditionArea,
      checkRmqcAuth,
      showSafetyBtn,
      showModreqBtn,
      showReviewBtn,
      showModifyBtn,
      showRmqcBtn,
      showDiscontinuedBtn,
      showDeleteBtn,
      showComment,
      viewStepSeq,
      resetShowFlag,
      fetchCommentCount
    }
  }
}
</script>
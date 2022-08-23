<template>
  <div class="box">
    <div class="box-cont">
      <div class="cont-top cont-top2">
        <h2 class="cont-top-title">{{ t('ingredient.label.raw_change_req') }}</h2>
      </div>
      <div class="cont-mid">
        <div class="cont-form noTop noBottom">
          <fieldset class="cont-input">
            <legend class="hidden">{{ t('ingredient.label.raw_change_req') }}</legend>
            <div class="sec-wrap">
              <div class="sec-top">
                <h3 class="sec-top-title">{{ t('ingredient.label.modreq_info') }}</h3>
              </div>
            </div>
            <div class="sec-mid">
              <IngredientReqInfoView></IngredientReqInfoView>
            </div>
          </fieldset>
        </div>
      </div>
      <div class="cont-bot">
        <div class="btn-wrap center">
          <a class="btn typeB gray"
            v-if="showModifyBtn()"
            @click.prevent="fnDelete(detail)"
          >
            <span class="del">{{ t('common.label.delete') }}</span>
          </a>
          <a class="btn typeA"
            v-if="showModifyBtn()"
            @click.prevent="fnRawModify(detail.modSeq)"
          >
            <span class="mod">{{ t('ingredient.label.modreq_info_modify') }}</span>
          </a>
          <a class="btn typeA"
            @click.prevent="fnDetail(detail.recordCd)"
          >
            <span>{{ t('ingredient.label.raw_detail_info') }}</span>
          </a>
          <a class="btn typeB" @click.prevent="fnModList()"><span class="list">{{ t('common.label.list') }}</span></a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { defineAsyncComponent, inject, provide, reactive, ref } from 'vue'
import { useStore } from 'vuex'
import { useRoute  } from 'vue-router'
import { useActions } from 'vuex-composition-helpers'
import { useIngredient } from '@/compositions/useIngredient'
import { useIngredientModify } from '@/compositions/useIngredientModify'

export default {
  name: 'IngredientModifyView',
  components: {
    IngredientReqInfoView: defineAsyncComponent(() => import('@/components/ingredient/IngredientReqInfoView.vue'))
  },
  setup () {
    const t = inject('t')
    const commonUtils = inject('commonUtils')
    const checkRmqcAuth = commonUtils.checkAuth('SGG000011|SGG000012|SGG000013|SGG000014')
    const checkLaborAuth = commonUtils.checkAuth('SGG000003')
    const route = useRoute()
    const store = useStore()
    const compCd = store.getters.getMyInfo().compCd
    const { openAsyncConfirm } = useActions(['openAsyncConfirm'])
    let beforeRaw = ref({})

    const {
      detail,
      fetchModifyReqInfo,
      fnRawModify,
      fnModList,
      deleteModreqInfo
    } = useIngredientModify()

    const {
      params,
      fetchIngredient,
      fetchIngredientReturnData,
      fnDetail
    } = useIngredient()

    const modSeq = route.query.modSeq
    const init = async () => {
      if (modSeq) {
        await fetchModifyReqInfo(modSeq)

        if (detail.value.recordCd && detail.value.recordCd !== 0) {
          fetchIngredient(detail.value.recordCd, '')
        }

        if (detail.value.preRecordCd && detail.value.preRecordCd !== 0) {
          beforeRaw.value = await fetchIngredientReturnData(detail.value.preRecordCd, '')
        }
      }
    }

    const fnDelete = async (obj) => {
      if (!await openAsyncConfirm({ message: t('common.msg.delete_confirm_msg') })) {
        return
      }
      const payload = {
        recordCd: obj.recordCd,
        modSeq: obj.modSeq
      }

      const result = await deleteModreqInfo(payload)
      if (result) {
        fnModList()
      }
    }

    const showModifyBtn = () => {
      let isVisible = false

      if ((checkRmqcAuth || checkLaborAuth || compCd !== 'AP') && detail.value.verStatusCd === 'VS060') {
        isVisible = true
      }

      return isVisible
    }

    init()
    provide('rawInfo', params)
    provide('beforeRaw', beforeRaw)
    provide('detail', detail)

    return {
      t,
      detail,
      params,
      fnRawModify,
      fnDetail,
      fnModList,
      fnDelete,
      showModifyBtn
    }
  }
}
</script>
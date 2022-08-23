<template>
  <div class="box">
    <div class="box-cont">
      <div class="cont-top">
        <h2 class="cont-top-title">{{ t('ingredient.label.raw_change_req') }}</h2>
      </div>
      <div class="cont-mid">
        <div class="cont-form noBottom">
          <fieldset class="cont-input">
            <legend class="hidden">{{ t('ingredient.label.raw_change_req') }}</legend>
            <ul class="ig-sec-list">
              <li class="sec-input-wrap">
                <div class="sec-input-wrap__center">
                  <h3 class="sec-input-tit">{{ t('ingredient.label.selected_raw') }}</h3>
                  <ul class="sec-input-list cont-600">
                    <li class="cont-input-list-item">
                      <div class="cont-table">
                        <table class="tb typeA">
                          <caption class="hidden">{{ t('ingredient.label.selected_raw') }}</caption>
                          <thead>
                            <tr>
                              <th class="w-15">{{ t('ingredient.label.serial_number') }}</th>
                              <th class="w-30">{{ t('ingredient.label.raw_nm') }}</th>
                              <th class="w-30">{{ t('ingredient.label.manufacturer') }}</th>
                              <th class="w-15">{{ t('ingredient.label.agency') }}</th>
                            </tr>
                          </thead>
                          <tbody>
                            <tr>
                              <td class="t-left">{{ params.rawCd }}</td>
                              <td class="t-left">{{ params['rawNm' + langCd] }}</td>
                              <td class="t-left">{{ params['manfNm' + langCd] }}</td>
                              <td class="t-left">{{ params['compNm' + langCd] }}</td>
                            </tr>
                          </tbody>
                        </table>
                      </div>
                    </li>
                  </ul>
                </div>
              </li>
              <li class="sec-input-wrap">
                <IngredientModifyReqNormal
                  v-if="params.rawTypeCd !== 'RT03'"
                  :record-cd="recordCd"
                  ref="reqNormal"
                >
                </IngredientModifyReqNormal>
                <IngredientModifyReqFood
                  v-else
                  :record-cd="recordCd"
                  ref="reqFood"
                >
                </IngredientModifyReqFood>
              </li>
            </ul>
          </fieldset>
        </div>
      </div>
      <div class="cont-bot">
        <div class="btn-wrap center">
          <a class="btn typeA gray" @click.prevent="fnHistoryBack()"><span>{{ t('common.label.confirm_cancel') }}</span></a>
          <a class="btn typeB" @click.prevent="fnModReqComplete()"><span class="complete">{{ t('common.label.complete') }}</span></a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { defineAsyncComponent, inject, ref, reactive } from 'vue'
import { useRoute  } from 'vue-router'
import { useIngredient } from '@/compositions/useIngredient'
import { useIngredientModify } from '@/compositions/useIngredientModify'
import { useStore } from 'vuex'
import { useActions } from 'vuex-composition-helpers'

export default {
  name: 'IngredientModifyRegister',
  components: {
    IngredientModifyReqNormal: defineAsyncComponent(() => import('@/components/ingredient/IngredientModifyReqNormal.vue')),
    IngredientModifyReqFood: defineAsyncComponent(() => import('@/components/ingredient/IngredientModifyReqFood.vue'))
  },
  setup () {
    const t = inject('t')
    const route = useRoute()
    const store = useStore()
    const storeLangCd = store.getters.getLangCd()
    const commonUtils = inject('commonUtils')
    const { openAsyncConfirm, openAsyncAlert } = useActions(['openAsyncConfirm', 'openAsyncAlert'])
    const langCd = commonUtils.capitalize(storeLangCd)
    
    const recordCd = route.query.recordCd
    const reqNormal = ref(null)
    const reqFood = ref(null)

    let regParams = reactive({
      recordCd: recordCd,
      rawTypeCd: '',
      files: []
    })

    const {
      params,
      fetchIngredient,
      setSaveFileInfo,
      fnHistoryBack
    } = useIngredient()

    const {
      fnModReqInsert,
      fnRawModify
    } = useIngredientModify()

    if (recordCd) {
      fetchIngredient(recordCd, '')
    }

    const fnValidation = () => {
      let isOk = true

      if (params.value.rawTypeCd !== 'RT03') {
        isOk = reqNormal.value.fnValidationNormal()
      } else {
        isOk = reqFood.value.fnValidationFood()
      }

      return isOk
    }
    
    const fnModReqComplete = async () => {
      if (!fnValidation()) {
        openAsyncAlert({ message: t('common.msg.check_essential_item') })
        return
      }

      if (!await openAsyncConfirm({ message: t('common.msg.save_cofirm_msg') })) {
        return
      }
      
      let fileList = []
      if (params.value.rawTypeCd !== 'RT03') {
        fileList = setSaveFileInfo(reqNormal.value.uploadFileList)
        regParams = { ...regParams, ...reqNormal.value.regParams }
      } else {
        fileList = setSaveFileInfo(reqFood.value.uploadFileList)
        regParams = { ...regParams, ...reqFood.value.regParams }
      }
      regParams.files = fileList
      regParams.rawTypeCd = params.value.rawTypeCd

      const modSeq = await fnModReqInsert(regParams)

      if (modSeq) {
        fnRawModify(modSeq)
      }
    }

    return {
      t,
      params,
      regParams,
      langCd,
      recordCd,
      reqNormal,
      reqFood,
      fnModReqComplete,
      fnHistoryBack
    }
  }
}
</script>
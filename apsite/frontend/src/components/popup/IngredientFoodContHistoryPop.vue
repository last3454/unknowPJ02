<template>
  <div class="modal-content">
    <div class="modal-tit-wrap">
      <p class="modal-tit">{{ t('ingredient.label.change_record') }}</p>
    </div>
    <div class="modal-cont-wrap">
      <h3 class="pop-subtit mb05">{{ t('ingredient.label.before_change') }}</h3>
      <table class="tb typeA typeA__input mb10">
        <caption class="hidden">{{ t('ingredient.label.ingrd_info') }}</caption>
        <thead>
          <tr>
            <th class="w-15">{{ t('ingredient.label.raw_nm') }}</th>
            <th class="w-8">{{ t('ingredient.label.std_raw_nm') }}</th>
            <th class="w-8">{{ t('ingredient.label.food_type') }}</th>
            <th class="w-6">{{ t('ingredient.label.ratio') }}</th>
            <th class="w-6">{{ t('ingredient.label.functionality_yn') }}</th>
            <th class="w-8">{{ t('ingredient.label.functionality_nm') }}</th>
            <th class="w-8">{{ t('ingredient.label.place_origin') }}</th>
            <th class="w-7">{{ t('ingredient.label.algn_yn') }}</th>
            <th class="w-7">{{ t('ingredient.label.radiation_processing_yn') }}</th>
          </tr>
        </thead>
        <tbody>
          <template v-if="beforeFoodRawList && beforeFoodRawList.length > 0">
            <template v-for="(vo, idx) in beforeFoodRawList" :key="'concd_' + idx">
              <tr :class="'ig-reg-step4-2-lv' + vo.step">
                <td class="ig-reg-step4-2-name">
                  <span :class="vo.materialNmIsDiff ? 'change' : ''">
                    {{ vo.materialNm }}
                  </span>
                  <template v-if="vo.step !== 1">
                    <span :class="vo.amountIsDiff ? 'change' : ''">
                      ( {{ vo.amount }}% )
                    </span>
                  </template>
                </td>
                <td class="t-left">
                  <span :class="vo.resourceNmIsDiff ? 'change' : ''">
                    {{ vo.resourceNm }}
                  </span>
                </td>
                <td class="t-left">
                  <span :class="vo.foodTypeNmIsDiff ? 'change' : ''">
                    {{ vo.foodTypeNm }}
                  </span>
                </td>
                <td class="t-right">
                  <template v-if="vo.step === 1">
                    <span :class="vo.amountIsDiff ? 'change' : ''">
                      {{ vo.amount }}
                    </span>
                  </template>
                </td>
                <td>
                  <template v-if="vo.flagFunctional">
                    <span :class="vo.flagFunctionalIsDiff ? 'change' : ''">
                      {{ vo.flagFunctional === 'Y' ? 'YES' : 'NO' }}
                    </span>
                  </template>
                </td>
                <td class="t-left">
                  <span :class="vo.functionalDescIsDiff ? 'change' : ''">
                    {{ vo.functionalDesc }}
                  </span>
                </td>
                <td class="t-left">
                  <template v-if="vo.countryNmList">
                    <span :class="vo.countryNmListIsDiff ? 'change' : ''">
                      {{ vo.countryNmList.join(', ') }}
                    </span>
                  </template>
                </td>
                <td>
                  <span :class="vo.allergyNmIsDiff ? 'change' : ''">
                    {{ vo.allergyNm }}
                  </span>  
                </td>
                <td>
                  <template v-if="vo.flagRadiation">
                    <span :class="vo.flagRadiationIsDiff ? 'change' : ''">
                      {{ vo.flagRadiation === 'Y' ? 'YES' : 'NO' }}
                    </span>
                  </template>
                </td>
              </tr>
            </template>
            <tr>
              <th colspan="3" class="t-right backgrey">{{ t('ingredient.label.sum_amount') }}</th>
              <td class="t-right">{{ beforeSumAmount }}</td>
              <th colspan="5" class="backgrey"></th>
            </tr>
          </template>
          <template v-else>
            <tr>
              <td colspan="99">{{ t('common.msg.no_data') }}</td>
            </tr>
          </template>
        </tbody>
      </table>
      <h3 class="pop-subtit mb05">{{ t('ingredient.label.after_change') }}</h3>
      <table class="tb typeA typeA__input mb10">
        <caption class="hidden">{{ t('ingredient.label.ingrd_info') }}</caption>
        <thead>
          <tr>
            <th class="w-15">{{ t('ingredient.label.raw_nm') }}</th>
            <th class="w-8">{{ t('ingredient.label.std_raw_nm') }}</th>
            <th class="w-8">{{ t('ingredient.label.food_type') }}</th>
            <th class="w-6">{{ t('ingredient.label.ratio') }}</th>
            <th class="w-6">{{ t('ingredient.label.functionality_yn') }}</th>
            <th class="w-8">{{ t('ingredient.label.functionality_nm') }}</th>
            <th class="w-8">{{ t('ingredient.label.place_origin') }}</th>
            <th class="w-7">{{ t('ingredient.label.algn_yn') }}</th>
            <th class="w-7">{{ t('ingredient.label.radiation_processing_yn') }}</th>
          </tr>
        </thead>
        <tbody>
          <template v-if="foodRawList && foodRawList.length > 0">
            <template v-for="(vo, idx) in foodRawList" :key="'concd_af_' + idx">
              <tr :class="'ig-reg-step4-2-lv' + vo.step">
                <td class="ig-reg-step4-2-name">
                  <span :class="vo.materialNmIsDiff ? 'change' : ''">
                    {{ vo.materialNm }}
                  </span>
                  <template v-if="vo.step !== 1">
                    <span :class="vo.amountIsDiff ? 'change' : ''">
                      ( {{ vo.amount }}% )
                    </span>
                  </template>
                </td>
                <td class="t-left">
                  <span :class="vo.resourceNmIsDiff ? 'change' : ''">
                    {{ vo.resourceNm }}
                  </span>
                </td>
                <td class="t-left">
                  <span :class="vo.foodTypeNmIsDiff ? 'change' : ''">
                    {{ vo.foodTypeNm }}
                  </span>
                </td>
                <td class="t-right">
                  <template v-if="vo.step === 1">
                    <span :class="vo.amountIsDiff ? 'change' : ''">
                      {{ vo.amount }}
                    </span>
                  </template>
                </td>
                <td>
                  <template v-if="vo.flagFunctional">
                    <span :class="vo.flagFunctionalIsDiff ? 'change' : ''">
                      {{ vo.flagFunctional === 'Y' ? 'YES' : 'NO' }}
                    </span>
                  </template>
                </td>
                <td class="t-left">
                  <span :class="vo.functionalDescIsDiff ? 'change' : ''">
                    {{ vo.functionalDesc }}
                  </span>
                </td>
                <td class="t-left">
                  <template v-if="vo.countryNmList">
                    <span :class="vo.countryNmListIsDiff ? 'change' : ''">
                      {{ vo.countryNmList.join(', ') }}
                    </span>
                  </template>
                </td>
                <td>
                  <span :class="vo.allergyNmIsDiff ? 'change' : ''">
                    {{ vo.allergyNm }}
                  </span>  
                </td>
                <td>
                  <template v-if="vo.flagRadiation">
                    <span :class="vo.flagRadiationIsDiff ? 'change' : ''">
                      {{ vo.flagRadiation === 'Y' ? 'YES' : 'NO' }}
                    </span>
                  </template>
                </td>
              </tr>
            </template>
            <tr>
              <th colspan="3" class="t-right backgrey">{{ t('ingredient.label.sum_amount') }}</th>
              <td class="t-right">{{ sumAmount }}</td>
              <th colspan="5" class="backgrey"></th>
            </tr>
          </template>
          <template v-else>
            <tr>
              <td colspan="99">{{ t('common.msg.no_data') }}</td>
            </tr>
          </template>
        </tbody>
      </table>
      <div class="btn-wrap">
        <a class="btn typeB gray" @click.prevent="fnClose()"><span>{{ t('common.label.close') }}</span></a>
      </div>
    </div>
    <div class="modal-close-btn">
      <a @click.prevent="fnClose()" :title="t('common.label.pop_close')">{{ t('common.label.pop_close') }}</a>
    </div>
  </div>
</template>

<script>
import { inject, ref } from 'vue'

export default {
  name: 'IngredientFoodContHistoryPop',
  props: {
    parentInfo: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  setup (props, { emit }) {
    const t = inject('t')
    const commonUtils = inject('commonUtils')
    const diffFoodInfo = ref({})
    const foodRawList = ref([])
    const beforeFoodRawList = ref([])
    const sumAmount = ref(0)
    const beforeSumAmount = ref(0)

    const getSumAmount = () => {
      foodRawList.value.forEach(raw => {
        if (raw.topIdx === raw.idx) {
          sumAmount.value += Math.round(Number(raw.amount) * T) / T
        }
      })

      beforeFoodRawList.value.forEach((vo, idx) => {
        if (vo.topIdx === vo.idx) {
          beforeSumAmount.value += Math.round(Number(vo.amount) * T) / T
        }
      })
    }

    const init = () => {
      if (props.parentInfo.diffrentFoodList && props.parentInfo.diffrentFoodList.length > 0) {
        const diffrentFoodList = props.parentInfo.diffrentFoodList
        const afterFoodMap = props.parentInfo.afterFoodMap
        const beforeFoodMap = props.parentInfo.beforeFoodMap
        foodRawList.value = props.parentInfo.foodRawList
        beforeFoodRawList.value = props.parentInfo.beforeFoodRawList

        diffrentFoodList.forEach((vo, idx) => {
          const result = commonUtils.compareObject(afterFoodMap[vo], beforeFoodMap[vo])
          if (result && result.length > 0) {
            diffFoodInfo.value[Number(vo)] = result
          }
        })

        beforeFoodRawList.value.forEach((vo, idx) => {
          const keyLists = Object.keys(vo)

          keyLists.forEach(key => {
            setHistoryBtn(vo, idx, key)
          })

          calSumAmount(vo, beforeFoodRawList.value)
        })

        foodRawList.value.forEach((vo, idx) => {
          const keyLists = Object.keys(vo)

          keyLists.forEach(key => {
            setHistoryBtn(vo, idx, key)
          })

          calSumAmount(vo, foodRawList.value)
        })
      }

      getSumAmount()
    }

    const T = Number('1e'+ 10)
    const calSumAmount = (info, foodRawList) => {
      const parentIdx = info.parentIdx
      const topIdx = info.topIdx
      const idx = info.idx

      if (topIdx !== idx) {
        let totalAmount = 0

        foodRawList.forEach((raw, idx) => {
          raw.idx = idx
          if (raw.parentIdx === parentIdx && raw.step != 1) {
            totalAmount += Math.round(Number(raw.amount) * T) / T
          }
        })

        foodRawList[parentIdx].sumAmount = totalAmount
      }
    }

    const setHistoryBtn = (vo, idx, key) => {
      let isChange = false

      if (diffFoodInfo.value && diffFoodInfo.value[idx] && (diffFoodInfo.value[idx].includes(key) || diffFoodInfo.value[idx].includes('ERROR_CONSTRUCTOR'))) {
        isChange = true
      }

      vo[key + 'IsDiff'] = isChange
    }

    init()
    
    const fnClose = () => {
      emit('callBack')
    }

    return {
      t,
      props,
      fnClose,
      commonUtils,
      beforeFoodRawList,
      foodRawList,
      diffFoodInfo,
      sumAmount,
      beforeSumAmount
    }
  }
}
</script>

<style scoped>
  .change {background-color: #FAED7D}
</style>
<template>
  <div class="main-comm-tab-cont tab-cont active">
    <div class="cont-table">
      <div v-if="list?.length" class="cont-table">
        <table class="tb typeA small">
        <caption class="hidden">{{ t('main.label.communication.material') }}</caption><!-- 소통 원료 목록 -->
        <thead>
          <tr>
              <th>{{ t('main.label.communication.raw_nm') }}</th><!-- 원료명 -->
              <th>{{ t('main.label.communication.manu_nm') }}</th><!-- 업체명 -->
              <th></th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="(vo, idx) in list"
            :key="`ing_${idx}`"
          >
            <td class="t-left">
              <span>{{ vo['rawNm' + langCd] }}</span>
            </td>
            <td class="t-left">
              <span>{{ vo['manfNm' + langCd] }}</span>
            </td>
            <td>
              <button type="button" @click.prevent="fnMoveCommu(vo)">{{ t('main.label.communication.go_detail') }}</button><!-- 바로가기 -->
            </td>
          </tr>
        </tbody>
        </table>
      </div>
      <div v-else class="con-table active">
        <div class="noContent">
          {{ t('main.msg.communication.no_list') }}
        </div><!-- 소통원료 목록이 없습니다. -->
      </div>
    </div>
  </div>
</template>

<script>
import { inject,reactive } from 'vue'
import { useStore } from 'vuex'
import { useCode } from '@/compositions/useCode'
import { useIngredient } from '@/compositions/useIngredient'
import { useCommunication } from '@/compositions/useCommunication'

export default {
  name: 'MainCommunicationMate',
  setup () {
    const t = inject('t')
    const store = useStore()
    const myInfo = store.getters.getMyInfo()
    const commonUtils = inject('commonUtils')
    const storeLangCd = store.getters.getLangCd()
    const { fetchCodeGroupMaps, codeGroupMaps, getCodeNm } = useCode()
    const langCd = commonUtils.capitalize(storeLangCd)

    const {
      list,
      fetchMainCommuIngredientList
    } = useIngredient()

    fetchMainCommuIngredientList()

    const {
      params,
      fnDetail,
      fnMoveCommu,
      fetchMainComments,
    } = useCommunication()

    // 공통코드(RAW_STEP_MENU) 가져오기
    fetchCodeGroupMaps(['RAW_STEP_MENU'])

    fetchMainComments()

    return {
      t,
      params,
      myInfo,
      fnDetail,
      fnMoveCommu,
      commonUtils,
      codeGroupMaps,
      getCodeNm,
      langCd,
      list
    }
  }
}
</script>
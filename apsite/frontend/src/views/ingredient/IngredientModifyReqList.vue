<template>
  <div class="box">
    <div class="box-cont">
      <div class="cont-top cont-top2">
        <h2 class="cont-top-title">{{ t('ingredient.label.modify_req_list') }}</h2>
        <div class="search-wrap p-right">
          <fieldset>
            <legend class="hidden">{{ t('ingredient.label.modify_req_list') }}</legend>
            <div class="search-select">
              <select v-model="searchParams.type" class="slt">
                <option value="">SELECT</option>
                <option value="rawNm">{{ t('ingredient.label.product_nm') }}</option>
                <option value="manfNm">{{ t('ingredient.label.manufacturer') }}</option>
                <option v-if="compCd === 'AP'" value="compNm">{{ t('ingredient.label.agency') }}</option>
                <option v-if="compCd === 'AP'" value="sapCd">{{ t('ingredient.label.sap_cd') }}</option>
              </select>
            </div>
            <div class="search-input">
              <input type="search" v-model="searchParams.keyword" :placeholder="t('common.msg.search_msg')" @keypress.enter="fnSearch(1)">
              <button type="button" @click="fnSearch(1)" class="search-btn" :title="t('common.label.search')">{{ t('common.label.search') }}</button>
            </div>
          </fieldset>
        </div>
      </div>
      <div class="cont-mid">
        <div class="cont-table">
          <table class="tb typeA small">
            <caption class="hidden">{{ t('ingredient.label.modify_req_list') }}</caption>
            <thead>
              <tr>
                <th class="w-11">{{ t('ingredient.label.serial_number') }}</th>
                <th>{{ t('ingredient.label.product_nm') }}</th>
                <th class="w-11">{{ t('ingredient.label.manufacturer') }}</th>
                <th v-if="compCd === 'AP'" class="w-11">{{ t('ingredient.label.agency') }}</th>
                <th class="w-7">{{ t('ingredient.label.registration_date') }}</th>
                <th class="w-7">{{ t('ingredient.label.modify_reason') }}</th>
                <th class="w-15">{{ t('ingredient.label.modify_range') }}</th>
                <th class="w-7">{{ t('ingredient.label.change_time') }}</th>
                <th class="w-5">{{ t('ingredient.label.version') }}</th>
                <th class="w-3">{{ t('ingredient.label.stock_cnt') }}</th>
              </tr>
            </thead>
            <tbody>
              <template v-if="list && list.length > 0">
                <tr v-for="(vo, idx) in list" :key="'raw_list_' + idx">
                  <td><a @click="goModreqDetail(vo.modSeq)">{{ vo.rawCd }}</a></td>
                  <td class="t-left"><a @click="goModreqDetail(vo.modSeq)">{{ vo['rawNm' + langCd] }}</a></td>
                  <td class="t-left">{{ vo['manfNm' + langCd] }}</td>
                  <td class="t-left" v-if="compCd === 'AP'">{{ vo['compNm' + langCd] }}</td>
                  <td>{{ vo.regDtm.substring(0, 10) }}</td>
                  <td>{{ vo.modReasonNm }}</td>
                  <td class="t-left">{{ vo.modRangeNm }}</td>
                  <td>{{ vo.changeDt }}</td>
                  <td>{{ vo.rawVer }}</td>
                  <td>{{ vo.stockCnt }}</td>
                </tr>
              </template>
              <template v-else>
                <tr>
                  <td colspan="99">{{ t('common.msg.no_data') }}</td>
                </tr>
              </template>
            </tbody>
          </table>
        </div>
      </div>
      <div class="cont-bot">
        <pagination :page-info="page" @go-page-num="fnSearch" />
      </div>
    </div>
  </div>
</template>

<script>
import { defineAsyncComponent, inject, reactive } from 'vue'
import { useIngredientModify } from '@/compositions/useIngredientModify'
import { useStore } from 'vuex'

export default {
  name: 'IngredientModifyReqList',
  components: {
    Pagination: defineAsyncComponent(() => import('@/components/comm/Pagination.vue'))
  },
  setup () {
    const t = inject('t')
    const store = useStore()
    const commonUtils = inject('commonUtils')
    const storeLangCd = store.getters.getLangCd()
    const langCd = commonUtils.capitalize(storeLangCd)
    const compCd = store.getters.getMyInfo().compCd

    const {
      page,
      list,
      fetchModifyReqList,
      fnRawModifyView
    } = useIngredientModify()

    let searchParams = reactive({
      type: '',
      keyword: '',
      nowPageNo: 1
    })

    const fnSearch = (pg) => {
      if (!pg) {
        pg = 1
      }

      searchParams.nowPageNo = pg
      sessionStorage.setItem('modreqSearchParams', JSON.stringify(searchParams))
      fetchModifyReqList(searchParams)
    }

    const goModreqDetail = (modSeq) => {
      fnRawModifyView(modSeq)
    }

    const init = () => {
      const sessionParam = JSON.parse(sessionStorage.getItem('modreqSearchParams'))
      let pg = 1

      if (sessionParam && sessionParam.nowPageNo) {
        pg = sessionParam.nowPageNo
      }

      searchParams = { ...searchParams, ...sessionParam }
      fnSearch(pg)

      sessionStorage.removeItem('modreqSearchParams')
    }

    init()

    return {
      t,
      langCd,
      compCd,
      page,
      list,
      searchParams,
      fnSearch,
      goModreqDetail
    }
  }
}
</script>

<style scoped>
  .search-select { width: max-content; }
  .search-input { width: 21.5em; max-width: inherit; }
  .slt { width: max-content; min-width: 110px; }
</style>
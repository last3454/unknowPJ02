<template>
  <div class="box">
    <div class="box-cont">
      <div class="cont-top cont-top2">
        <h2 class="cont-top-title">{{ t('ingredient.label.raw_list') }}</h2>
        <div class="search-wrap p-right">
          <fieldset>
            <legend class="hidden">{{ t('ingredient.label.raw_list_search') }}</legend>
            <div class="search-select">
              <select class="slt" v-model="searchParams.rawTypeCd">
                <option value="">{{ t('ingredient.label.raw_type') }}</option>
                <option v-for="(vo, idx) in codeGroupMaps['RAW_TYPE']" :key="'rawType_' + idx" :value="vo.code">{{ vo.codeNm }}</option>
              </select>
            </div>
            <div class="search-select">
              <select class="slt" v-model="searchParams.statusCd">
                <option value="">{{ t('ingredient.label.step') }}</option>
                <option v-for="(vo, idx) in codeGroupMaps['RAW_STATUS']" :key="'rawStatus_' + idx" :value="vo.code">{{ vo.codeNm }}</option>
              </select>
            </div>
            <div class="search-select">
              <select class="slt" v-model="searchParams.verStatusCd">
                <option value="">Status</option>
                <option v-for="(vo, idx) in codeGroupMaps['RAW_VER_STATUS']" :key="'rawVerStatus_' + idx" :value="vo.code">{{ vo.codeNm }}</option>
              </select>
            </div>
            <div class="search-select">
              <select v-model="searchParams.type" class="slt">
                <option value="">SELECT</option>
                <option value="rawNm">{{ t('ingredient.label.product_nm') }}</option>
                <option value="manfNm">{{ t('ingredient.label.manufacturer') }}</option>
                <option v-if="compCd === 'AP'" value="compNm">{{ t('ingredient.label.agency') }}</option>
                <option value="sapCd">{{ t('ingredient.label.sap_cd') }}</option>
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
            <caption class="hidden">{{ t('ingredient.label.raw_list') }}</caption>
            <thead>
              <tr>
                <th class="w-11">{{ t('ingredient.label.serial_number') }}</th>
                <th>{{ t('ingredient.label.product_nm') }}</th>
                <th class="w-11">{{ t('ingredient.label.manufacturer') }}</th>
                <th class="w-11" v-if="compCd === 'AP'">{{ t('ingredient.label.agency') }}</th>
                <th class="w-7">{{ t('ingredient.label.registration_date') }}</th>
                <th class="w-5">{{ t('ingredient.label.step') }}</th>
                <th class="w-4_5">{{ t('ingredient.label.status') }}</th>
                <th class="w-6">{{ t('ingredient.label.sap_cd') }}</th>
                <th class="w-7">{{ t('ingredient.label.change_date') }}</th>
                <th class="w-5">{{ t('ingredient.label.version') }}</th>
                <th class="w-3">{{ t('ingredient.label.stock_cnt') }}</th>
              </tr>
            </thead>
            <tbody>
              <template v-if="list && list.length > 0">
                <tr v-for="(vo, idx) in list" :key="'raw_list_' + idx">
                  <td><a @click="goDetail(vo.recordCd)">{{ vo.rawCd }}</a></td>
                  <td class="t-left">
                    <a @click="goDetail(vo.recordCd)">
                      <template v-if="vo['rawNm' + langCd]">
                        {{ vo['rawNm' + langCd] }}
                      </template>
                      <template v-else>
                        {{ vo.rawNmKo }}
                      </template>
                    </a>
                  </td>
                  <td class="t-left">{{ vo['manfNm' + langCd] }}</td>
                  <td class="t-left" v-if="compCd === 'AP'">{{ vo['compNm' + langCd] }}</td>
                  <td>{{ vo.regDtm.substring(0, 10) }}</td>
                  <td>{{ vo.statusNm }}</td>
                  <td>{{ vo.verStatusNm }}</td>
                  <td>{{ vo.sapCd }}</td>
                  <td>{{ vo.updDtm.substring(0, 10) }}</td>
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
        <!-- components 로 작업 예정 -->
        <pagination :page-info="page" @go-page-num="fnSearch" />
        <!-- components 로 작업 예정 -->
        <div class="btn-wrap right">
          <a class="btn typeB" v-if="showRegisterBtn()" @click.prevent="goRegiser()"><span class="reg">{{ t('ingredient.label.reg_raw') }}<!-- 등록 --></span></a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { defineAsyncComponent, inject, ref } from 'vue'
import { useIngredient } from '@/compositions/useIngredient'
import { useCode } from '@/compositions/useCode'
import { useStore } from 'vuex'
export default {
  name: 'IngredientList',
  components: {
    Pagination: defineAsyncComponent(() => import('@/components/comm/Pagination.vue'))
  },
  setup () {
    const t = inject('t')
    const store = useStore()
    const storeLangCd = store.getters.getLangCd()
    const commonUtils = inject('commonUtils')

    const langCd = commonUtils.capitalize(storeLangCd)

    const {
      page,
      list,
      fetchIngredientList,
      fnLinkReg,
      fnDetail,
      checkRmqcAuth,
      checkLaborAuth,
      compCd
    } = useIngredient()

    const {
      fetchCodeGroupMaps,
      codeGroupMaps
    } = useCode()

    fetchCodeGroupMaps(['RAW_TYPE', 'RAW_STATUS', 'RAW_VER_STATUS'])

    let searchParams = ref({
      type: '',
      keyword: '',
      nowPageNo: 1,
      rawTypeCd: '',
      statusCd: '',
      verStatusCd: ''
    })

    const setSessionSearcmParams = () => {
      sessionStorage.setItem('rawSearchParams', JSON.stringify(searchParams.value))
    }

    const fnSearch = (pg) => {
      if (!pg) {
        pg = 1
      }

      searchParams.value.nowPageNo = pg
      setSessionSearcmParams()
      fetchIngredientList(searchParams.value)
    }

    const init = () => {
      const sessionParam = JSON.parse(sessionStorage.getItem('rawSearchParams'))
      let pg = 1

      if (sessionParam && sessionParam.nowPageNo) {
        pg = sessionParam.nowPageNo
      }

      searchParams.value = { ...searchParams.value, ...sessionParam }
      fnSearch(pg)

      sessionStorage.removeItem('rawSearchParams')
    }

    init()

    const goDetail = (recordCd) => {
      setSessionSearcmParams()
      fnDetail(recordCd)
    }

    const goRegiser = () => {
      setSessionSearcmParams()
      fnLinkReg()
    }

    const showRegisterBtn = () => {
      let isVisible = false

      if (checkRmqcAuth || checkLaborAuth || compCd !== 'AP') {
        isVisible = true
      }

      return isVisible
    }

    return {
      t,
      langCd,
      compCd,
      page,
      list,
      searchParams,
      codeGroupMaps,
      goRegiser,
      goDetail,
      fnSearch,
      showRegisterBtn
    }
  }
}
</script>

<style scoped>
  .search-select { width: max-content; }
  .search-input { width: 21.5em; max-width: inherit; }
  .slt { width: max-content; min-width: 110px; }
</style>
<template>
  <div class="modal-content">
    <div class="modal-tit-wrap">
      <p class="modal-tit">{{ t('popup.label.concd_search') }}</p>
    </div>
    <div class="modal-cont-wrap modal-cont-wrap2">
      <div class="cont-table-top">
        <div class="search-wrap full">
          <fieldset>
            <legend class="hidden">{{ t('popup.label.concd_search') }}</legend>
            <div class="search-select" style="width: 75em">
              <a class="btn typeA large" @click.prevent="fnConInfoDirect()"><span>{{ t('common.label.direct_regist') }}</span></a>
            </div>
            <div class="search-input">
              <input
                type="search"
                v-model="searchParams.keyword"
                :placeholder="t('common.msg.search_msg')"
                @keypress.enter="searchConInfo()"
              >
              <button type="button" class="search-btn" :title="t('common.label.search')" @click="searchConInfo()">{{ t('common.label.search') }}</button>
            </div>
          </fieldset>
        </div>
      </div>
    </div>
    <div class="cont-table cont-table-scrollY">
      <table class="tb typeA typeA__input" style="table-layout:fixed">
        <caption class="hidden">{{ t('popup.label.concd_search') }}</caption>
        <colgroup>
          <col width="10%"/>
          <col width="10%"/>
          <col width="40%"/>
          <col width="40%"/>
        </colgroup>
        <thead>
          <tr>
            <th>{{ t('ingredient.label.concd') }}</th>
            <th>{{ t('popup.label.casno') }}</th>
            <th>{{ t('popup.label.psname_ko') }}</th>
            <th>{{ t('popup.label.psname_en') }}</th>
          </tr>
        </thead>
        <tbody>
          <template v-if="list && list.length > 0">
            <tr v-for="(vo, idx) in list" :key="'conlist_' + idx">
              <td><a @click.prevent="selectConInfo(vo)">{{ vo.vconcd }}</a></td>
              <td>{{ vo.vcasno }}</td>
              <td class="t-left"><a @click.prevent="selectConInfo(vo)">{{ vo.vpsnameKo }}</a></td>
              <td class="t-left"><a @click.prevent="selectConInfo(vo)">{{ vo.vpsnameEn }}</a></td>
            </tr>
          </template>
          <template v-else>
            <tr>
              <td colspan="100">{{ t('common.msg.no_data') }}</td>
            </tr>
          </template>
        </tbody>
      </table>
    </div>
    <div class="btn-wrap">
      <pagination :page-info="page" @go-page-num="searchConInfo" />
    </div>
  </div>
  <div class="modal-close-btn">
    <a @click.prevent="fnClose()" :title="t('common.label.pop_close')">{{ t('common.label.pop_close') }}</a>
  </div>
</template>

<script>
import { defineAsyncComponent, inject, onMounted, reactive } from 'vue'
import { useComm } from '@/compositions/useComm'
import { useActions } from 'vuex-composition-helpers'

export default {
  name: 'ConInfoSearchPop',
  components: {
    Pagination: defineAsyncComponent(() => import('@/components/comm/Pagination.vue'))
  },
  props: {
    popParams: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  setup (props, { emit }) {
    const t = inject('t')
    const { closeAsyncPopup } = useActions(['closeAsyncPopup'])
    
    const searchParams = reactive({
      keyword: '',
      nowPageNo: 1
    })

    const searchConInfo = (pg) => {
      if (!pg) {
        pg = 1
      }

      searchParams.nowPageNo = pg
      fetchConList(searchParams)
    }

    const fnClose = () => {
      closeAsyncPopup({ message: '닫기' })
    }

    const selectConInfo = (vo) => {
      vo.idx = props.popParams.idx
      vo.vconnameEn = vo.vconnameEn.trim()
      vo.isDirect = false
      emit('selectFunc', vo)
    }

    const fnConInfoDirect = () => {
      const vo = {
        vcasno: '',
        vconnameEn: '',
        vconcd: '',
        idx: props.popParams.idx,
        isDirect: true
      }

      emit('selectFunc', vo)
    }

    const {
      list,
      page,
      fetchConList
    } = useComm()

    onMounted(() => {
      if (props.popParams.searchKeyword !== undefined) {
        searchParams.keyword = props.popParams.searchKeyword
        searchConInfo()
      }
    })

    return {
      t,
      searchParams,
      list,
      page,
      searchConInfo,
      selectConInfo,
      fnConInfoDirect,
      fnClose
    }
  }
}
</script>

<style scoped>
  td { font-size: 14px !important; }
  .modal .btn-wrap { margin-top: 10px;}
  .cont-table-scrollY { height: 300px;}
</style>
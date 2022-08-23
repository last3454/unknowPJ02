<template>
  <div class="modal-content">
    <div class="modal-tit-wrap">
      <p class="modal-tit">{{ t('popup.label.food_std_search') }}</p>
    </div>
    <div class="modal-cont-wrap modal-cont-wrap2">
      <div class="cont-table-top">
        <div class="search-wrap full">
          <fieldset>
            <legend class="hidden">{{ t('popup.label.food_std_search') }}</legend>
            <div class="search-input">
              <input
                type="search"
                v-model="searchParams.keyword"
                :placeholder="t('common.msg.search_msg')"
                @keypress.enter="searchFoodStdResource()"
              >
              <button type="button" class="search-btn" :title="t('common.label.search')" @click="searchFoodStdResource()">{{ t('common.label.search') }}</button>
            </div>
          </fieldset>
        </div>
      </div>
    </div>
    <div class="cont-table cont-table-scrollY">
      <table class="tb typeA typeA__input" style="table-layout:fixed">
        <caption class="hidden">{{ t('popup.label.food_std_search') }}</caption>
        <colgroup>
          <col width="33%" />
          <col width="33%" />
          <col width="34%" />
        </colgroup>
        <thead>
          <tr>
            <th>{{ t('ingredient.label.raw_nm') }}</th>
            <th>{{ t('popup.label.alias') }}</th>
            <th>{{ t('popup.label.scientific_name') }}</th>
          </tr>
        </thead>
        <tbody>
          <template v-if="list && list.length > 0">
            <tr v-for="(vo, idx) in list" :key="'list_' + idx">
              <td class="t-left"><a @click.prevent="selectFoodStdResourceInfo(vo)">{{ vo.vresourceNm }}</a></td>
              <td class="t-left">{{ vo.valias }}</td>
              <td class="t-left"><span class="txt_ellipsis">{{ vo.vscientific }}</span></td>
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
      <pagination :page-info="page" @go-page-num="searchFoodStdResource" />
    </div>
  </div>
  <div class="modal-close-btn">
    <a @click.prevent="fnClose()" :title="t('common.label.pop_close')">{{ t('common.label.pop_close') }}</a>
  </div>
</template>

<script>
import { defineAsyncComponent, inject, reactive } from 'vue'
import { useIngredient } from '@/compositions/useIngredient'
import { useActions } from 'vuex-composition-helpers'

export default {
  name: 'FoodStdResourceSearchPop',
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

    const {
      fetchFoodStdNmList,
      list,
      page
    } = useIngredient()

    const searchParams = reactive({
      keyword: '',
      nowPageNo: 1,
      pageYn: 'Y'
    })

    const searchFoodStdResource = (pg) => {
      if (!pg) {
        pg = 1
      }

      searchParams.nowPageNo = pg
      fetchFoodStdNmList(searchParams)
    }

    const fnClose = () => {
      closeAsyncPopup({ message: '닫기' })
    }

    const selectFoodStdResourceInfo = (vo) => {
      vo.idx = props.popParams.idx
      emit('selectFunc', vo)

      fnClose()
    }

    searchFoodStdResource(1)

    return {
      t,
      searchParams,
      list,
      page,
      searchFoodStdResource,
      fnClose,
      selectFoodStdResourceInfo
    }
  }
}
</script>

<style scoped>
  .cont-table-scrollY { height: 300px;}
</style>
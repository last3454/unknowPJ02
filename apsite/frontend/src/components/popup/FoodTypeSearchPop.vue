<template>
  <div class="modal-content">
    <div class="modal-tit-wrap">
      <p class="modal-tit">{{ t('popup.label.food_type_search') }}</p>
    </div>
    <div class="modal-cont-wrap modal-cont-wrap2">
      <div class="cont-table-top">
        <div class="search-wrap full">
          <fieldset>
            <legend class="hidden">{{ t('popup.label.food_type_search') }}</legend>
            <div class="search-input">
              <input
                type="search"
                v-model="searchParams.keyword"
                :placeholder="t('common.msg.search_msg')"
                @keypress.enter="searchFoodType()"
              >
              <button type="button" class="search-btn" :title="t('common.label.search')" @click="searchFoodType()">{{ t('common.label.search') }}</button>
            </div>
          </fieldset>
        </div>
      </div>
    </div>
    <div class="cont-table cont-table-scrollY">
      <table class="tb typeA typeA__input" style="table-layout:fixed">
        <caption class="hidden">{{ t('popup.label.food_std_search') }}</caption>
        <colgroup>
          <col width="40%" />
          <col width="60%" />
        </colgroup>
        <thead>
          <tr>
            <th>{{ t('common.label.classification') }}</th>
            <th>{{ t('ingredient.label.food_type') }}</th>
          </tr>
        </thead>
        <tbody>
          <template v-if="list && list.length > 0">
            <tr v-for="(vo, idx) in list" :key="'list_' + idx">
              <td class="t-left"><a @click.prevent="selectFoodTypeInfo(vo)">{{ vo.vmaterialTypeNm }}</a></td>
              <td class="t-left"><a @click.prevent="selectFoodTypeInfo(vo)" class="txt_ellipsis">{{ vo.vtargetProduct }}</a></td>
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
      <pagination :page-info="page" @go-page-num="searchFoodType" />
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
  name: 'FoodTypeSearchPop',
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
      fetchFoodTypeList,
      list,
      page
    } = useIngredient()

    const searchParams = reactive({
      keyword: '',
      nowPageNo: 1,
      pageYn: 'Y'
    })

    const searchFoodType = (pg) => {
      if (!pg) {
        pg = 1
      }

      searchParams.nowPageNo = pg
      fetchFoodTypeList(searchParams)
    }

    const fnClose = () => {
      closeAsyncPopup({ message: '닫기' })
    }

    const selectFoodTypeInfo = (vo) => {
      vo.idx = props.popParams.idx
      emit('selectFunc', vo)

      fnClose()
    }

    searchFoodType(1)

    return {
      t,
      searchParams,
      list,
      page,
      searchFoodType,
      selectFoodTypeInfo,
      fnClose
    }
  }
}
</script>

<style scoped>
  .cont-table-scrollY { height: 300px;}
</style>
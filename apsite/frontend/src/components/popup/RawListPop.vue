<template>
  <div class="modal-content">
    <div class="modal-tit-wrap">
      <p class="modal-tit">{{ t('ingredient.label.raw_list') }}<!-- 원료목록 --></p>
    </div>
    <div class="modal-cont-wrap-no-limit-h" style="height: 600px;">
      <div class="cont-table-top">
        <div class="search-wrap full">
          <fieldset>
            <legend class="hidden">원료 검색</legend>
            <div class="search-select">
              <select
                v-model="searchParams.type"
                class="slt"
              >
                <option value="">{{ t('common.label.select') }}<!-- 선택 --></option>
                <option value="rawCd">{{ t('ingredient.label.raw_cd') }}<!-- 원료코드 --></option>
                <option value="rawNm">{{ t('ingredient.label.raw_nm') }}<!-- 원료명 --></option>
              </select>
            </div>
            <div class="search-input">
              <input
                v-model="searchParams.keyword"
                type="search"
                :placeholder="t('common.msg.search_msg')"
                @keyup.enter="fnSearch()"
              />
              <button
                type="button"
                class="search-btn"
                title="검색하기"
                @click="fnSearch()"
              >
                검색하기
              </button>
            </div>
          </fieldset>
        </div>
      </div>
      <div class="cont-table cont-table-scrollY">
        <table class="tb typeA typeA__input">
          <caption class="hidden">
            원료목록
          </caption>
          <thead>
            <tr>
              <th class="w-1"></th>
              <th class="w-35 cont-input-tit">
                {{ t('ingredient.label.raw_nm') }}<!-- 원료명 --><span class="desc">{{ t('common.label.english') }}<!-- 영문 --></span>
              </th>
              <th class="w-35 cont-input-tit">
                {{ t('ingredient.label.raw_nm') }}<!-- 원료명 --><span class="desc">{{ t('common.label.korean') }}<!-- 국문 --></span>
              </th>
              <th class="w-12">{{ t('ingredient.label.comp_nm') }}<!-- 업체명 --></th>
              <th class="w-8">{{ t('ingredient.label.upd_user') }}<!-- 수정자 --></th>
              <th class="w-9">{{ t('ingredient.label.upd_dtm') }}<!-- 변경일 --></th>
            </tr>
          </thead>
          <tbody>
            <template v-if="list && list.length > 0">
              <tr v-for="(raw, idx) in list" :key="idx">
                <td class="narrow">
                  <div class="input-check-tb">
                    <input
                      v-model="selectedList"
                      type="checkbox"
                      class="chk typeA"
                      :id="`b_council_r_m_chk${idx}`"
                      :value="raw"
                      :disabled="raw.readOnly"
                    />
                    <label :for="`b_council_r_m_chk${idx}`"></label>
                  </div>
                </td>
                <td class="t-left">{{ raw.rawNmEn }}</td>
                <td class="t-left">{{ raw.rawNmKo }}</td>
                <td class="t-center">{{ raw.compNmKo }}</td>
                <td>{{ raw.updUserNm }}</td>
                <td>{{ raw.updDtm?.split(' ')[0].split('-').join('.') ?? '' }}</td>
              </tr>
            </template>
            <template v-else>
              <tr>
                <td colspan="6">{{ t('common.msg.no_data') }}</td>
              </tr>
            </template>
          </tbody>
        </table>
      </div>
      <div class="cont-bot">
        <pagination style="margin-top: 10px;"
          :page-info="page"
          @go-page-num="fnSearch"
        />
      </div>
      <div class="btn-wrap">
        <a href="#" @click="onCancel" class="btn typeB gray"
          ><span>{{ t('common.label.cancel') }}<!-- 취소 --></span></a
        >
        <a href="#" @click="onConfirm" class="btn typeB"
          ><span class="complete">{{ t('common.label.select') }}<!-- 선택 --></span></a
        >
      </div>
    </div>
    <div class="modal-close-btn">
      <a href="#" @click="onCancel" title="팝업창 닫기">팝업창 닫기</a>
    </div>
  </div>
</template>

<script>
import { useIngredient } from '@/compositions/useIngredient'

import { defineAsyncComponent, inject, ref, watch } from 'vue'
import { useActions } from 'vuex-composition-helpers'
import { useRoute } from 'vue-router'

export default {
  name: 'RawListPop',
  components: {
    Pagination: defineAsyncComponent(() => import('@/components/comm/Pagination.vue')),
  },
  props: {
    parentInfo: {
      type: Object,
      default () {
        return {
          rawInfo: [],
          flag: 'R'
        }
      }
    }
  },
  setup(props) {
    const t = inject('t')
    const route = useRoute()
    const { closeAsyncPopup } = useActions(['closeAsyncPopup'])

    const {
      searchParams,
      list,
      page,
      fetchIngredientList
    } = useIngredient()

    searchParams.value.flag = 'popup'

    const selectedList = ref([])
    const tempArr = ref([])

    watch (() => selectedList.value, (newVal, oldVal) => {
      tempArr.value = selectedList.value
    })

    // 검색
    const fnSearch = async (pg) => {
      if(!pg){
        pg = 1
      }      
      searchParams.value.nowPageNo = pg
      await fetchIngredientList(searchParams.value)

      // 기존 등록한 원료 정보가 존재하는 경우
      if (tempArr.value.length === 0) {
        tempArr.value = props.parentInfo.rawInfo
      }

      if ((list.value && list.value.length > 0) && (tempArr.value && tempArr.value.length > 0)) {
        if (route.name.indexOf('council') > -1) {
          const templist = list.value
  
          outer: for (let i = 0; i < templist.length; i++) {
            const o1 = templist[i];
            
            inner: for (let j = 0; j < tempArr.value.length; j++) {
              const o2 = tempArr.value[j];
              
              if (o1.rawCd === o2.rawCd && o2.readOnly) {
                o1.readOnly = true
                break inner
              }
            }
          }
        }

        list.value.forEach((element, i) => {
          if (tempArr.value.some(raw => element.rawCd === raw.rawCd) && !selectedList.value.some(selRaw => element.rawCd === selRaw.rawCd)) {
            selectedList.value.push(element)
          }
        })
      }
    }

    fnSearch()

    // '확인' 이벤트
    const onConfirm = () => {
      selectedList.value.sort((a, b) => {
        if (a.rawCd < b.rawCd) return 1
        if (a.rawCd > b.rawCd) return -1

        return 0
      })

      closeAsyncPopup({ message: '선택', flag: props.parentInfo.flag, emitValue: selectedList.value })
    }

    // '취소' 이벤트
    const onCancel = () => {
      closeAsyncPopup({ message: '취소' })
    }

    return {
      t,
      selectedList,
      tempArr,
      searchParams,
      list,
      page,
      fnSearch,
      onConfirm,
      onCancel,
    }
  },
}
</script>

<style scoped>
.cont-table-scrollY {
    overflow-y: auto;
    height: 390px;
}
.modal .btn-wrap {
    margin-top: 1em;
}
.modal table.tb.typeA > thead > tr > th {
    height: 1.750em;
}
table.tb.typeA.typeA__input > tbody > tr > td {
    padding: 6.5px;
}
</style>

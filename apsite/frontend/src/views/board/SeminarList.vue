<template>
  <div>
    <div class="box">
      <div class="box-cont">
        <div class="cont-top cont-top2">
          <h2 class="cont-top-title">{{ t('board.label.seminar.list_title') }}<!-- 세미나 --></h2>
          <div class="search-wrap p-right">
            <fieldset>
              <legend class="hidden">세미나 검색</legend>
              <div class="search-input">
                <input
                  v-model="searchParams.keyword"
                  type="search"
                  :placeholder="t('common.msg.search_msg')"
                  @keyup.enter="fnSearch()"
                ><!-- 검색어를 입력하세요. -->
                <button type="button" @click="fnSearch()" class="search-btn">검색하기</button>
              </div>
            </fieldset>
          </div>
        </div>
          <div class="cont-mid">
            <div class="cont-table">
              <table class="tb typeA">
                <caption class="hidden">{{ t('board.label.seminar.list_title') }}<!-- 세미나 --></caption>
                <thead>
                  <tr>
                    <th class="w-5">{{ t('common.label.seq') }}<!-- 연번 --></th>
                    <th class="w-6">{{ t('board.label.seminar.place') }}<!-- 장소 --></th>
                    <th>{{ t('board.label.seminar.title') }}<!-- 제목 --></th>
                    <th class="w-13">{{ t('board.label.seminar.smn_dtm') }}<!-- 세미나일시 --></th>
                    <th class="w-16">{{ t('board.label.seminar.apply_dtm') }}<!-- 신청기간 --></th>
                    <th class="w-8">{{ t('board.label.seminar.reg_user_nm') }}<!-- 등록자 --></th>
                    <th class="w-10">{{ t('board.label.seminar.reg_dtm') }}<!-- 등록일 --></th>
                    <th class="w-3">{{ t('board.label.seminar.like') }}<!-- 좋아요 --></th>
                    <th class="w-3">{{ t('board.label.seminar.recommend') }}<!-- 추천 --></th>
                  </tr>
                </thead>
                <tbody>
                  <template v-if="list && list.length > 0">
                    <tr v-for="(vo, idx) in list" :key="'tr_' + idx">
                      <td>
                        {{ page.totalCnt - ((page.pageSize * (page.nowPageNo-1)) + idx)}}
                      </td>
                      <td>
                        {{ vo.smnPlcClsDesc }}
                      </td>
                      <td class="t-left">
                        <a href="#" @click.prevent="fnLinkView(vo.seq)">
                          {{ vo?.title ?? '' }}
                        </a>
                      </td>
                      <td>
                        {{ vo.smnDtmDesc }}
                      </td>
                      <td>
                        {{ vo.smnRegStartDt }} ~ {{ vo.smnRegEndDt }}
                      </td>
                      <!-- <td>{{ vo?.regUserNm }}</td> -->
                      <td>{{ vo.regUserNm }}</td>
                      <td>{{ vo.regDtm.substring(0, 10) }}</td>
                      <td>{{ vo.likeCnt }}</td>
                      <td>{{ vo.rcmdCnt }}</td>
                    </tr>
                  </template>
                  <template v-else>
                    <tr>
                      <td colspan="9">{{ t('common.msg.no_data') }}</td><!-- 데이터가 존재하지 않습니다. -->
                    </tr>
                  </template>
                </tbody>
              </table>
            </div>
          </div>
          <div class="cont-bot">
            <pagination
              :page-info="page"
              @go-page-num="fnSearch"
            />
            <div
              v-if="commonUtils.checkAuth('SGG000003|SGG000005|SGG000006|SGG000011|SGG000012|SGG000013|SGG000014')"
              class="btn-wrap right"
            >
              <a
                href="#"
                class="btn typeB"
                @click.prevent="fnLinkReg()"
              >
                <span class="reg">{{ t('common.label.register') }}<!-- 등록 --></span>
              </a>
            </div>
          </div>
      </div>
    </div>
  </div>
</template>

<script>

import { defineAsyncComponent, inject } from 'vue'
import { useSeminar } from '@/compositions/useSeminar'

export default {
  name: 'SeminarList',
  components: {
    Pagination: defineAsyncComponent(() => import('@/components/comm/Pagination.vue')),
  },
  setup() {
    const commonUtils = inject('commonUtils')
    const {
      t,
      searchParams,
      page,
      list,
      fnLinkReg,
      fnLinkView,
      fnSearch
    } = useSeminar()

    fnSearch() 

    return {
      commonUtils,
      t,
      page,
      list,
      searchParams,
      fnLinkView,
      fnLinkReg,
      fnSearch
    }
  }
}
</script>

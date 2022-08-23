<template>
  <div>
    <div class="box">
      <div class="box-cont">
        <div class="cont-top cont-top2">
          <h2 class="cont-top-title">{{ t('board.label.council.list_title') }}<!-- 신원료협의체 --></h2>
          <div class="search-wrap p-right">
            <fieldset>
              <legend class="hidden">{{ t('board.label.council.list_title2') }}<!-- 신원료협의체 검색 --></legend>
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
                <caption class="hidden">{{ t('board.label.council.list_title') }}<!-- 신원료협의체 --></caption>
                <thead>
                  <tr>
                    <th class="w-5">{{ t('common.label.seq') }}<!-- 연번 --></th>
                    <th class="w-6">{{ t('board.label.council.place') }}<!-- 장소 --></th>
                    <th>{{ t('board.label.council.title') }}<!-- 제목 --></th>
                    <th class="w-13">{{ t('board.label.council.cfrc_dtm') }}<!-- 회의일시 --></th>
                    <th class="w-6">{{ t('board.label.council.cfrc_status') }}</th><!-- 진행상태 -->
                    <th class="w-8">{{ t('board.label.council.reg_user_nm') }}<!-- 등록자 --></th>
                    <th class="w-10">{{ t('board.label.council.reg_dtm') }}<!-- 등록일자 --></th>
                    <th class="w-5">{{ t('board.label.council.view_cnt') }}<!-- 조회 --></th>
                  </tr>
                </thead>
                <tbody>
                  <template v-if="list && list.length > 0">
                    <tr v-for="(vo, idx) in list" :key="'tr_' + idx">
                      <td>{{ page.totalCnt - ((page.pageSize * (page.nowPageNo-1)) + idx)}}</td>
                      <td>{{ vo.cfrcPlcClsDesc }}</td>
                      <td class="t-left">
                        <!-- <template v-if="vo.cfrcStatus === 'CCS0010'">
                          <a href="#" @click.prevent="fnLinkReg(vo.seq)">
                            {{ vo?.title ?? '' }}
                          </a>
                        </template>
                        <template v-else>
                          <a href="#" @click.prevent="fnLinkView(vo.seq)">
                            {{ vo?.title ?? '' }}
                          </a>
                        </template> -->
                        <a href="#" @click.prevent="fnLinkView(vo.seq)">
                          {{ vo?.title ?? '' }}
                        </a>
                      </td>
                      <td>{{ vo.cfrcDtmDesc }}</td>
                      <td>{{ vo?.cfrcStatusDesc ?? '' }}</td>
                      <td>{{ vo.regUserNm }}</td>
                      <td>{{ vo?.regDtm.split(' ')[0] ?? '' }}</td>
                      <td>{{ vo?.viewCnt ?? 0 }}</td>
                    </tr>
                  </template>
                  <template v-else>
                    <tr>
                      <td colspan="8">{{ t('common.msg.no_data') }}</td><!-- 데이터가 존재하지 않습니다. -->
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
              v-if="commonUtils.checkAuth('SGG000003|SGG000007|SGG000011|SGG000012|SGG000013|SGG000014')"
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
import { useCouncil } from '@/compositions/useCouncil'

export default {
  name: 'CouncilList',
  components: {
    Pagination: defineAsyncComponent(() => import('@/components/comm/Pagination.vue')),
  },
  setup() {
    const commonUtils = inject('commonUtils')
    const {
      myInfo,
      t,
      searchParams,
      page,
      list,
      fnLinkReg,
      fnLinkView,
      fnSearch
    } = useCouncil()

    fnSearch() 

    return {
      commonUtils,
      myInfo,
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

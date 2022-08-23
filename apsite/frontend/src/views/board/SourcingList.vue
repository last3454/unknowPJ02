<template>
  <div class="inner narrow">
    <div class="box">
      <div class="box-cont">
        <div class="cont-top cont-top2">
          <h2 class="cont-top-title">{{ t('board.label.sourcing.title') }}</h2><!-- 원료소싱 -->
          <div class="search-wrap p-right">
            <fieldset>
              <legend class="hidden">{{ t('board.label.sourcing.search2') }}</legend><!-- 원료소싱 검색 -->
              <div class="search-input">
                <input type="search" :placeholder="t('board.label.notice.L003')" v-model="searchParams.keyword" @keyup.enter="fnSearch()">
                <button type="button" class="search-btn" :title="t('board.label.notice.L004')" @click.prevent="fnSearch()">{{ t('board.label.notice.L004') }}<!-- 검색어를 입력하세요 --></button>
              </div>
            </fieldset>
          </div>
        </div>
        <div class="cont-mid">
          <div class="cont-table">
            <table class="tb typeA">
              <caption class="hidden">{{ t('board.label.sourcing.title') }}</caption><!-- 원료소싱 -->
              <thead>
                <tr>
                  <th class="w-7">{{ t('board.label.sourcing.no') }}</th><!-- No -->
                  <th>{{ t('board.label.sourcing.title2') }}</th><!-- 제목 -->
                  <th v-if="isApUser === 'Y'" class="w-10">{{ t('board.label.sourcing.postYn') }}</th><!-- 상태 -->
                  <th class="w-10">{{ t('board.label.sourcing.regUser') }}</th><!-- 등록자 -->
                  <th class="w-12">{{ t('board.label.sourcing.regDate') }}</th><!-- 등록일 -->
                  <th class="w-7">{{ t('board.label.sourcing.search') }}</th><!-- 조회 -->
                </tr>
              </thead>
              <tbody v-if="sourcingList">
                <tr v-for="(dto, idx) in sourcingList" :key="'sourcing_' + idx">
                  <td>
                    {{ page.totalCnt - ((page.pageSize * (page.nowPageNo-1)) + idx)}}
                  </td>
                  <td class="t-left">
                    <router-link :to="{ path: '/sourcing/view', query: { seq: dto.seq } }"><span v-if="dto.openAllYn === 'Y'">[{{ t('board.label.sourcing.open') }}] </span>{{ dto.title }}</router-link><!-- 공개 -->
                  </td>
                  <td v-if="isApUser === 'Y'">
                    {{ dto.postStatus === 'Y' ? t('board.label.sourcing.post') : t('board.label.sourcing.registed') }}
                  </td>
                  <!-- 게시/검토요청/등록 -->
                  <td>{{ dto.regUserNm }}</td>
                  <td>{{ commonUtils.convertDt(dto.regDtm) }}</td>
                  <td>{{ dto.viewCnt }}</td>
                </tr>
              </tbody>
              <tbody v-else>
                <tr>
                <td colspan="6">
                  {{ t('board.msg.sourcing.desc3') }}
                </td><!-- 데이터가 존재하지 않습니다. -->
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div class="cont-bot">
          <pagination :page-info="page" @go-page-num="fnSearch" />
          <div class="btn-wrap right">
            <a v-if="isApUser === 'Y'" href="#" class="btn typeB" @click.prevent="goRegister">
              <span class="reg">{{ t('board.label.sourcing.reg') }}</span><!-- 등록 -->
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { defineAsyncComponent, reactive } from 'vue'
import { useSourcing } from '@/compositions/useSourcing'

export default {
  name: 'SourcingList',
  components: {
    Pagination: defineAsyncComponent(() => import('@/components/comm/Pagination.vue')),
  },
  setup() {
    const {
      t,
      page,
      isApUser,
      commonUtils,
      sourcingList,
      goRegister,
      goPageNum,
      fetchSourcing
    } = useSourcing()

    const searchParams = reactive({
      keyword: '',
      nowPageNo: 1,
      isApUser: isApUser
    })

    const fnSearch = (pg) => {
      if (!pg) {
        pg = 1
      }
      searchParams.nowPageNo = pg
      fetchSourcing(searchParams)
    }

    fnSearch()

    return {
      t,
      fnSearch,
      isApUser,
      searchParams,
      sourcingList,
      page,
      goRegister,
      goPageNum,
      commonUtils
    }
  },
}
</script>
<template>
  <div class="inner narrow">
    <div class="box">
      <div class="box-cont">
        <div class="cont-top cont-top2">
          <h2 class="cont-top-title">{{ t('board.label.errorinq.error_inq') }}<!-- 오류 문의 --></h2>
          <div class="search-wrap p-right">
            <fieldset>
              <legend class="hidden">{{ t('board.label.errorinq.search_error') }}<!-- 오류문의 검색 --></legend>
              <div class="search-input">
                <input type="search" :placeholder="t('board.msg.errorinq.search_msg1')" v-model="searchParams.keyword" @keyup.enter="fnSearch()">
                <button type="button" class="search-btn" :title="t('board.label.notice.L004')" @click.prevent="fnSearch()">{{ t('board.label.errorinq.search') }}<!-- 검색 --></button>
              </div>
            </fieldset>
          </div>
        </div>
        <div class="cont-mid">
          <div class="cont-table">
            <table class="tb typeA">
              <caption class="hidden">{{ t('board.label.errorinq.error_inq') }}<!-- 오류문의 --></caption>
              <thead>
                <tr>
                  <th class="w-5">{{ t('board.label.errorinq.no') }}<!-- No --></th>
                  <th>{{ t('board.label.errorinq.title') }}<!-- 제목 --></th>
                  <th class="w-8">{{ t('board.label.errorinq.reply_yn') }}<!-- 답변 여부 --></th>
                  <th class="w-8">{{ t('board.label.errorinq.req_user') }}<!-- 등록자 --></th>
                  <th class="w-10">{{ t('board.label.errorinq.req_dtm') }}<!-- 등록일 --></th>
                  <th class="w-5">{{ t('board.label.errorinq.hits') }}<!-- 조회 수--></th>
                </tr>
              </thead>
              <tbody v-if="list?.length > 0">
                <tr v-for="(dto, idx) in list" :key="'errorinq_' + idx">
                  <th>
                    {{ page.totalCnt - ((page.pageSize * (page.nowPageNo-1)) + idx)}}
                  </th>
                  <th class="t-left">
                    <router-link :to="{ path: '/errorinq/view', query: { seq: dto.seq } }">{{ dto.title }}</router-link>
                  </th>
                  <th>{{ getCodeNm('ERR_INQ_STATE', dto.status) }}</th>
                  <th>{{ dto.regUserNm }}</th>
                  <th>{{ commonUtils.convertDt(dto.regDtm) }}</th>
                  <th>{{ dto.viewCnt }}</th>
                </tr>
              </tbody>
              <tbody v-else>
                <tr>
                  <th colspan="6">{{ t('board.msg.notice.desc1') }}</th><!-- 데이터가 존재하지 않습니다. -->
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div class="cont-bot">
          <pagination :page-info="page" @go-page-num="fnSearch" />
          <div class="btn-wrap right">
            <a href="#" class="btn typeB" @click.prevent="goRegister">
              <span class="reg">{{ t('common.label.register') }}<!-- 등록 --></span>
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import { defineAsyncComponent, reactive, inject } from 'vue'
import { useErrorInq } from '@/compositions/useErrorInq'
import { useCode } from '@/compositions/useCode'
import { useRouter } from 'vue-router'

export default {
  name: 'ErrorInqList',
  components: {
    Pagination: defineAsyncComponent(() => import('@/components/comm/Pagination.vue')),
  },
  setup() {
    const t = inject('t')
    const commonUtils = inject('commonUtils')
    const { fetchCodeGroupMaps, codeGroupMaps, getCodeNm } = useCode()
    const router = useRouter()

    const searchParams = reactive({
      keyword: '',
      nowPageNo: 1
    })

    const {
      list,
      page,
      fetchBoards
    } = useErrorInq()

    const fnSearch = (pg) => {
      if(!pg){
        pg = 1
      }
      searchParams.nowPageNo = pg
      fetchBoards(searchParams)
    }

    const goRegister = () => {
      router.push({ path: '/errorinq/register' })
    }

    const goPageNum = (pg) => {
      console.log(pg)
    }

    fnSearch()

    if (commonUtils.isNotEmpty(list.status)) {
      fetchCodeGroupMaps(['ERR_INQ_STATE'], list.status)
    } else {
      fetchCodeGroupMaps(['ERR_INQ_STATE'])
    }

    return {
      searchParams,
      list,
      page,
      getCodeNm,
      fnSearch,
      goRegister,
      goPageNum,
      commonUtils,
      t
    }
  }
}
</script>
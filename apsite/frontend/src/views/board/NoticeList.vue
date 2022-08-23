<template>
  <div class="inner narrow">
    <div class="box">
      <div class="box-cont">
        <div class="cont-top cont-top2">
          <h2 class="cont-top-title">{{ t('board.label.notice.L001') }}<!-- 공지사항 --></h2>
          <div class="search-wrap p-right">
            <fieldset>
              <legend class="hidden">{{ t('board.label.notice.L002') }}<!-- 공지사항 검색 --></legend>
              <div class="search-input">
                <input type="search" :placeholder="t('board.label.notice.L003')" v-model="searchParams.keyword" @keyup.enter="fnSearch()">
                <button type="button" class="search-btn" :title="t('board.label.notice.L004')" @click.prevent="fnSearch()">{{ t('board.label.notice.L004') }}<!-- 검색하기 --></button>
              </div>
            </fieldset>
          </div>
        </div>
        <div class="cont-mid">
          <div class="cont-table">
            <table class="tb typeA">
              <caption class="hidden">{{ t('board.label.notice.L001') }}<!-- 공지사항 --></caption>
              <thead>
                <tr>
                  <th class="w-7">{{ t('board.label.notice.L005') }}<!-- No --></th>
                  <th>{{ t('board.label.notice.L006') }}<!-- 제목 --></th>
                  <th class="w-10">{{ t('board.label.notice.L007') }}<!-- 등록자 --></th>
                  <th class="w-12">{{ t('board.label.notice.L008') }}<!-- 등록일 --></th>
                  <th class="w-7">{{ t('board.label.notice.L009') }}<!-- 조회 --></th>
                </tr>
              </thead>
              <tbody v-if="list?.length > 0">
                <tr class="t-notice" v-for="(dto, idx) in topList" :key="'top_notice_' + idx">
                  <th>{{ t('board.label.notice.L011') }}<!-- 공지 --></th>
                  <th class="t-left">
                    <router-link :to="{ path: '/notice/view', query: { seq: dto.seq } }">{{ dto.title }}</router-link>
                  </th>
                  <th>{{ dto.regUserNm }}</th>
                  <th>{{ commonUtils.convertDt(dto.regDtm) }}</th>
                  <th>{{ dto.viewCnt }}</th>
                </tr>
                <tr v-for="(dto, idx) in list" :key="'notice_' + idx">
                  <th>
                    {{ page.totalCnt - ((page.pageSize * (page.nowPageNo-1)) + idx)}}
                  </th>
                  <th class="t-left">
                    <router-link :to="{ path: '/notice/view', query: { seq: dto.seq } }">{{ dto.title }}</router-link>
                  </th>
                  <th>{{ dto.regUserNm }}</th>
                  <th>{{ commonUtils.convertDt(dto.regDtm) }}</th>
                  <th>{{ dto.viewCnt }}</th>
                </tr>
              </tbody>
              <tbody v-else>
                <tr>
                  <th colspan="5">{{ t('board.msg.notice.desc1') }}</th><!-- 데이터가 존재하지 않습니다. -->
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div class="cont-bot">
          <pagination :page-info="page" @go-page-num="fnSearch" />
          <div class="btn-wrap right">
            <a v-if="isAdmin" href="#" class="btn typeB" @click.prevent="goRegister">
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
import { useNotice } from '@/compositions/useNotice'
import { useCode } from '@/compositions/useCode'
import { useRouter } from 'vue-router'

export default {
  name: 'NoticeList',
  components: {
    Pagination: defineAsyncComponent(() => import('@/components/comm/Pagination.vue')),
  },
  setup() {
    const router = useRouter()
    const {
      fetchCodeGroupMaps,
      codeGroupMaps,
      getCodeNm
    } = useCode()

    fetchCodeGroupMaps(['TEST01', 'TEST02'])

    const searchParams = reactive({
      keyword: '',
      nowPageNo: 1
    })

    const {
      t,
      commonUtils,
      isAdmin,
      topList,
      list,
      page,
      goRegister,
      goPageNum,
      fetchBoards
    } = useNotice()

    const fnSearch = (pg) => {
      if(!pg){
        pg = 1
      }
      searchParams.nowPageNo = pg
      fetchBoards(searchParams)
    }

    fnSearch()

    return {
      t,
      commonUtils,
      isAdmin,
      searchParams,
      topList,
      list,
      page,
      codeGroupMaps,
      getCodeNm,
      fnSearch,
      goRegister,
      goPageNum
    }
  }
}
</script>
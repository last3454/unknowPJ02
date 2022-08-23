<template>
  <div class="box main-box main-box-seminar">
    <div class="box-cont">
      <div class="cont-top clearfix">
        <h3 class="cont-top-title f-left"><i class="ct-seminar"></i>{{ t('main.label.seminar.title') }}<!-- 세미나 일정 --></h3>
        <div v-if="params.seq" class="btn-wrap right f-right">
          <a
            href="#"
            class="btn typeB sky medium"
            @click.prevent="fnLinkList()"
          ><span class="next">{{ t('main.label.seminar.to_list') }}<!-- 전체 보기 --></span></a>
        </div>
      </div>
      <div
        v-if="params.seq"
        class="cont-mid"
      >
        <div class="cont-table">
          <table class="tb typeB">
            <caption class="hidden">가입정보</caption>
            <tbody>
              <tr>
                <th>{{ t('board.label.seminar.title') }}<!-- 제목 --></th>
                <td>{{ params.title }}</td>
              </tr>
              <tr>
                <th>{{ t('board.label.seminar.smn_dtm') }}<!-- 세미나일시 --></th>
                <td>{{ params.smnDtmDesc }}</td>
              </tr>
              <tr>
                <th>{{ t('board.label.seminar.smn_place') }}<!-- 세미나장소 --></th>
                <template v-if="params.smnPlcCls === 'ON'">
                  <td>{{ params.smnPlcClsDesc }} / <span><a :href="params.smnPlcRmk" target="_blank">{{ params.smnPlcRmk }}</a></span></td>
                </template>
                <template v-else>
                  <td>{{ params.smnPlcClsDesc }} / {{ params.smnPlcRmk }}</td>
                </template>
              </tr>
              <tr>
                <th>{{ t('board.label.seminar.apply_dtm') }}<!-- 신청기간 --></th>
                <td>{{ params.smnRegStartDt }} ~ {{ params.smnRegEndDt }}</td>
              </tr>
              <tr>
                <th>{{ t('board.label.seminar.req_user_cnt') }}<!-- 신청 인원수 --></th>
                <td>{{ params.applyCnt }} / {{ params.applyMaxCnt }} {{ t('board.label.seminar.user_cnt') }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div v-else class="main-cl-list">
        <div class="noContent">
          {{ t('main.msg.seminar.no_seminar') }}
        </div><!-- 세미나 정보가 없습니다. -->
      </div>
    </div>
  </div>
</template>

<script>

import { inject } from 'vue'
import { useSeminar } from '@/compositions/useSeminar'

export default {
  name: 'MainSeminar',
  setup() {
    const t = inject('t')
    const {
      params,
      fnLinkList,
      fetchMainSeminar
    } = useSeminar()

    fetchMainSeminar()

    return {
      t,
      params,
      fnLinkList
    }
  }
}
</script>

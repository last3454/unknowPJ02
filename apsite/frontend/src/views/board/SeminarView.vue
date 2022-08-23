<template>
  <div>
    <div class="box">
      <div class="box-cont">
        <div class="cont-top cont-top2">
          <h2 class="cont-top-title">{{ t('board.label.seminar.list_title') }}<!-- 세미나 --></h2>
        </div>
        <div class="cont-mid">
          <div class="b-v-wrap">
            <div class="b-v-top">
              <div class="b-v-tit-wrap p">
                <h3 class="b-v-tit">
                  {{ params.title }}
                </h3>
                <div class="b-v-like-wrap p-right">
                  <button
                    :class="['like', Object.keys(params.likeInfo).length > 0 ? 'on' : '']"
                    @click="fnToggleLike"
                    :title="t('board.label.seminar.like')"
                  >{{ params.likeCnt }}</button>
                  <button
                    :class="['recommend', Object.keys(params.recommendInfo).length > 0 ? 'on' : '']"
                    @click="fnToggleRecommend"
                    :title="t('board.label.seminar.recommend')"
                  >{{ params.rcmdCnt }}</button>
                </div>
              </div>
              <div class="b-v-info-wrap">
                <div class="b-v-info-from">
                  <!-- <span class="name">{{ params.regUserNm }}</span> -->
                  <span class="name">{{ params.regUserNm }}</span>
                  <span class="date">{{ commonUtils.convertDt(params.regDtm) }}</span>
                  <span class="date">{{ t('common.label.view_cnt') }} {{ params.viewCnt }}</span>
                </div>
              </div>
            </div>
            <div class="b-v-mid">
              <div class="cont-table">
                <table class="tb typeB typeB__input__border">
                  <caption class="hidden">
                    세미나 정보
                  </caption>
                  <tbody>
                    <tr>
                      <th class="wf-200">{{ t('board.label.seminar.smn_dtm') }}<!-- 세미나일시 --></th>
                      <td>{{ params.smnDtmDesc }}</td>
                    </tr>
                    <tr>
                      <th class="wf-200">{{ t('board.label.seminar.smn_place') }}<!-- 세미나장소 --></th>
                      <template v-if="params.smnPlcCls === 'ON'">
                        <td>{{ params.smnPlcClsDesc }} / <span><a :href="params.smnPlcRmk" target="_blank">{{ params.smnPlcRmk }}</a></span></td>
                      </template>
                      <template v-else>
                        <td>{{ params.smnPlcClsDesc }} / {{ params.smnPlcRmk }}</td>
                      </template>
                    </tr>
                    <tr>
                      <th class="wf-200">{{ t('board.label.seminar.apply_dtm') }}<!-- 신청기간 --></th>
                      <td>{{ params.smnRegStartDt }} ~ {{ params.smnRegEndDt }}</td>
                    </tr>
                    <tr>
                      <th class="wf-200">{{ t('board.label.seminar.req_user_cnt') }}<!-- 신청 인원수 --></th>
                      <td>
                        <div class="cont-input-wrap__flex start">
                          <template v-if="params.applyCnt > 0">
                            <a href="#" @click="fnOpenPopup('SeminarAttendListPop', 420)">{{ params.applyCnt }}</a>&nbsp;/&nbsp;{{ params.applyMaxCnt }} {{ t('board.label.seminar.user_cnt') }}<!-- 명 -->
                          </template>
                          <template v-else>
                            {{ params.applyCnt }}&nbsp;/&nbsp;{{ params.applyMaxCnt }} {{ t('board.label.seminar.user_cnt') }}<!-- 명 -->
                          </template>
                          <a
                            v-if="todayBetweenReqDate && Object.keys(params.attendInfo).length === 0 && (params.applyCnt < params.applyMaxCnt)"
                            href="#"
                            @click.prevent="fnReqAttendSeminar"
                            class="btn medium typeB sky mLeft15"
                          >
                            <span class="next">{{ t('board.label.seminar.apply_req') }}<!-- 참석신청 --></span>
                          </a>
                          <a
                            v-if="todayBetweenReqDate && Object.keys(params.attendInfo).length > 0"
                            href="#"
                            @click.prevent="fnCancelAttendSeminar"
                            class="btn medium typeB gray mLeft15"
                          >
                            <span>{{ t('board.label.seminar.apply_cancel') }}<!-- 참석취소 --></span>
                          </a>
                        </div>
                      </td>
                    </tr>
                    <tr>
                      <th class="wf-200">{{ t('board.label.seminar.raw_info') }}<!-- 원료정보 --></th>
                      <td>
                        <template v-if="params.rawInfo && params.rawInfo.length > 0">
                          <span
                            v-for="(raw, idx) in params.rawInfo"
                            :key="idx"
                            class="b-v-ig-item"
                          >
                            {{ raw.rawNmKo }}
                          </span>
                        </template>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
              <div class="b-v-cont-wrap b-v-cont-wrap2"
                v-html="params.content"
              />
              <div class="cont-table">
                <table class="tb typeB typeB__input__border">
                  <caption class="hidden">
                    첨부파일
                  </caption>
                  <tbody>
                    <tr>
                      <th class="wf-200">{{ t('common.label.attach2') }}<!-- 첨부파일 --></th>
                      <td>
                        <div
                          v-if="filesMap['SEMINAR01'] && filesMap['SEMINAR01'].length > 0"
                          class="b-v-file-wrap"
                        >
                          <a
                            v-for="item in filesMap['SEMINAR01']"
                            :key="`attach_${item.seq}`"
                            href="#"
                            @click.prevent="downloadFile(item.seq, item.fileNm)"
                            class="i-btn medium typeB gray"
                          >
                            <span class="down">{{ item.fileNm }}</span>
                          </a>
                        </div>
                      </td>
                    </tr>
                    <tr v-if="params.smnStatus === 'SMN0040'">
                      <th class="wf-200">{{ t('board.label.seminar.smn_cancel_reason') }}<!-- 취소사유 --></th>
                      <td>{{ params.smnCanclReason }}</td>
                    </tr>
                  </tbody>
                </table>
              </div>
              <div
                v-if="params.ytbUrl"
                class="b-v-video-wrap"
              >
                <iframe
                  width="1000"
                  height="600"
                  :src="params.ytbUrl"
                  title="YouTube video player"
                  frameborder="0"
                  allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                  allowfullscreen
                />
              </div>
            </div>
          </div>
        </div>
        <div class="cont-bot">
          <div class="btn-wrap center">
            <template v-if="smnCancelConfirmAuth">
              <a
                href="#"
                class="btn typeB long"
                @click.prevent="fnConfirmSeminar()"
              >
                <span>{{ t('board.label.seminar.smn_confirm') }}<!-- 세미나 개최 --></span>
              </a>
              <a
                href="#b_seminar_v_cancel_box"
                @click="fnToggleCancelArea(true)"
                class="btn typeB gray long"
              >
                <span>{{ t('board.label.seminar.smn_cancel') }}<!-- 세미나 취소 --></span>
              </a>
            </template>
            <template v-if="smnUpdateDeleteAuth">
              <a
                href="#"
                class="btn typeB gray"
                @click.prevent="fnLinkReg(params.seq)"
              >
                <span class="mod">{{ t('common.label.modify') }}<!-- 수정 --></span>
              </a>
              <a
                href="#"
                class="btn typeB gray"
                @click.prevent="fnDelete()"
              >
                <span class="del">{{ t('common.label.delete') }}<!-- 삭제 --></span>
              </a>
            </template>
            <a
              href="#"
              class="btn typeA"
              @click.prevent="fnLinkList()"
            >
              <span class="list">{{ t('common.label.list') }}<!-- 목록 --></span>
            </a>
            <a
              v-if="myInfo.userCd === params.regUserCd && (smnFinishFlag || params.smnStatus === 'SMN0040')"
              href="#"
              class="btn typeB long"
              @click.prevent="fnLinkReReq()"
            >
              <span>{{ t('board.label.seminar.smn_reapply') }}<!-- 세미나 재신청 --></span>
            </a>
          </div>
        </div>
      </div>
    </div>
    <div
      v-show="smnCancelAreaDispFlag"
      class="box"
      id="b_seminar_v_cancel_box"
    >
      <div class="box-cont">
        <div class="cont-top">
          <h2 class="cont-top-title">{{ t('board.label.seminar.smn_cancel') }}<!-- 세미나 취소 --></h2>
        </div>
        <div class="cont-mid">
          <div class="cont-table">
            <table class="tb typeB typeB__input__border">
              <caption class="hidden">
                취소사유
              </caption>
              <tbody>
                <tr>
                  <th class="wf-200">{{ t('board.label.seminar.smn_cancel_reason') }}<!-- 취소사유 --></th>
                  <td>
                    <input
                      v-model="params.smnCanclReason"
                      type="text"
                    />
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div class="cont-bot">
          <div class="btn-wrap center">
            <a
              href="#"
              @click.prevent="fnToggleCancelArea(false)"
              class="btn typeB gray long"
            >
              <span>{{ t('common.label.cancel') }}<!-- 취소 --></span>
            </a>
            <a
              href="#"
              @click.prevent="fnCancelSeminar"
              class="btn typeB"
            >
              <span class="complete">{{ t('common.label.confirm_ok') }}<!-- 확인 --></span>
            </a>
          </div>
        </div>
      </div>
    </div>
    <!-- <div
      v-if="smnFinishFlag || params.smnStatus === 'SMN0040'"
      class="box"
    > -->
    <div class="box">
      <div class="box-cont">
        <div class="cont-top">
          <h2 class="cont-top-title">Communication</h2>
        </div>
        <div class="cont-mid">
          <div class="b-v-comment-wrap">
            <template v-if="params.commuList.length > 0">
              <ul class="b-v-comment-list">
                <li v-for="(item, idx) in params.commuList" :key="idx">
                  <dl>
                    <!-- <dt>{{ item.regUserNm }}</dt> -->
                    <dt>{{ item.regUserNm }}</dt>
                    <dd class="date">{{ commonUtils.convertDt(item.regDtm) }}</dd>
                    <dd class="comment">{{ item.delYn === 'Y' ? t('common.msg.del_comment') : item?.content ?? '' }}</dd>
                    <dd
                      v-if="item.delYn === 'N' && myInfo.userCd === item.regUserCd"
                      class="del"
                    >
                      <a
                        href="#"
                        @click.prevent="fnDelComment(idx)"
                        title="댓글 삭제"
                      >삭제</a>
                    </dd>
                  </dl>
                </li>
              </ul>
            </template>
            <template v-else>
              <ul class="b-v-comment-list">
                <li>{{ t('board.msg.no_comment') }}<!-- 등록된 댓글이 없습니다. --></li>
              </ul>
            </template>
          </div>
        </div>
        <div class="cont-bot">
          <div class="b-v-comment-write-wrap p">
            <text-area-with-byte 
              v-model:value="params.commuContent"
              :cols="30"
              :rows="4"
              :placeholder="t('board.msg.comment_placeholder')"
              :maxlength="900"
              ref="commentArea"
            />
            <a
              href="#"
              @click.prevent="fnRegComment(commentArea)"
              class="btn typeB square comment-btn pa"
            >comment</a>
          </div>
        </div>
      </div>
    </div>
    <teleport to="#common-modal">
      <ap-popup>
        <component
          :is="popupContent"
          :parent-info="{ seq: params.seq }"
        />
      </ap-popup>
    </teleport>
  </div>
</template>

<script>

import { defineAsyncComponent, ref, inject } from 'vue'
import { useRoute } from 'vue-router'
import { useSeminar } from '@/compositions/useSeminar'
import { useUploadFile } from '@/compositions/useUploadFile'

export default {
  name: 'SeminarView',
  components: {
    ApPopup: defineAsyncComponent(() => import('@/components/comm/ApPopup.vue')),
    SeminarAttendListPop: defineAsyncComponent(() => import('@/components/popup/SeminarAttendListPop.vue')),
    TextAreaWithByte: defineAsyncComponent(() => import('@/components/comm/TextAreaWithByte.vue'))
  },
  setup (){
    const route = useRoute()
    const seq = route.query.seq
    const commonUtils = inject('commonUtils')
    const commentArea = ref(null)

    const {
      t,
      myInfo,
      params,
      popupContent,
      smnFinishFlag,
      smnCancelAreaDispFlag,
      smnCancelConfirmAuth,
      smnUpdateDeleteAuth,
      todayBetweenReqDate,
      fetchSeminar,
      fnLinkList,
      fnLinkReg,
      fnLinkReReq,
      fnToggleLike,
      fnToggleRecommend,
      fnReqAttendSeminar,
      fnCancelAttendSeminar,
      fnToggleCancelArea,
      fnCancelSeminar,
      fnConfirmSeminar,
      fnRegComment,
      fnDelComment,
      fnTextareaResize,
      fnDelete,
      fnOpenPopup
    } = useSeminar()

    const {
      filesMap,
      fetchUploadFiles,
      downloadFile
    } = useUploadFile()

    if (seq) {
      fetchSeminar(seq)
      fetchUploadFiles({ targetKey: '' + seq, uploadCd: 'SEMINAR01' })
    }

    return {
      commentArea,
      t,
      commonUtils,
      myInfo,
      params,
      popupContent,
      smnFinishFlag,
      smnCancelAreaDispFlag,
      smnCancelConfirmAuth,
      smnUpdateDeleteAuth,
      todayBetweenReqDate,
      filesMap,
      fnLinkList,
      fnLinkReg,
      fnLinkReReq,
      fnToggleLike,
      fnToggleRecommend,
      fnReqAttendSeminar,
      fnCancelAttendSeminar,
      fnToggleCancelArea,
      fnCancelSeminar,
      fnConfirmSeminar,
      fnRegComment,
      fnDelComment,
      fnTextareaResize,
      fnDelete,
      fnOpenPopup,
      downloadFile
    }
  }
}
</script>

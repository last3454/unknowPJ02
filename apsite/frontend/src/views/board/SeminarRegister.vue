<template>
  <div>
    <div class="box">
      <div class="box-cont">
        <div class="cont-top cont-top2">
          <h2 class="cont-top-title">{{ t('board.label.seminar.list_title') }}<!-- 세미나 --></h2>
        </div>
        <div class="cont-mid">
          <div class="cont-table">
            <table class="tb typeB typeB__input__border">
              <caption class="hidden">{{ t('board.label.seminar.list_title') }}<!-- 세미나 --></caption>
              <tbody>
                <tr>
                  <th class="wf-200">{{ t('board.label.seminar.title') }}<!-- 제목 --></th>
                  <td :class="(errors.title ? 'error' : '')">
                    <Field as="input" name="title" v-model="params.title" />
                    <p class="error-msg">{{ errors.title }}</p>
                  </td>
                </tr>
                <tr>
                  <!-- <th class="wf-200">{{ t('board.label.seminar.smn_dtm') }}</th> --><!-- 세미나 일시 -->
                  <th class="wf-200 tooltip">
                    <span class="txt">{{ t('board.label.seminar.smn_dtm') }}<!-- 세미나 일시 --></span>
                    <span class="box right">{{ t('board.msg.seminar.vali_term_smndtm_reg_end_day') }}<!-- 세미나 일시는 신청 종료 일자와 일주일 이상 차이가 있어야 합니다. --></span>
                  </th>
                  <td :class="(errors.smnDtm ? 'error' : '')">
                    <ap-date-time-picker
                      v-model:date="params.smnDtm"
                      :with-end-time="true"
                      :smn-end-date="params.smnEndDtm"
                      :read-only="true"
                      :style-class="'ro-white'"
                      @selectFunc="selectSmDtmFunc"
                    />
                    <Field as="input" name="smnDtm" v-model="params.smnDtm" hidden/>
                    <Field as="input" name="smnEndDtm" v-model="params.smnEndDtm" hidden/>
                    <p class="error-msg">{{ errors.smnDtm }}</p>
                  </td>
                </tr>
                <tr>
                  <th class="wf-200">{{ t('board.label.seminar.apply_dtm') }}<!-- 신청 기간 --></th>
                  <td :class="errors.smnRegStartDt ? 'error' : ''">
                    <ap-date-picker-range
                      v-model:startDt="params.smnRegStartDt"
                      v-model:endDt="params.smnRegEndDt"
                      :read-only="true"
                      :style-class="'ro-white'"
                    />
                    <Field as="input" name="smnRegStartDt" v-model="params.smnRegStartDt" hidden/>
                    <p class="error-msg">{{ errors.smnRegStartDt }}</p>
                  </td>
                </tr>
                <tr>
                  <th class="wf-200">{{ t('board.label.seminar.smn_place') }}<!-- 세미나 장소 --></th>
                  <td class="b-seminar-r-place">
                    <ul>
                      <li class="cont-input-wrap">
                        <div class="cont-input-wrap__flex">
                          <div class="input-radio">
                            <div class="input-radio-indiv">
                              <input
                                type="radio"
                                v-model="params.smnPlcCls"
                                id="b_inquiry_r_radio1"
                                name="b_inquiry_r_radio"
                                value="ON"
                                @change="fnChangeSmnPlcCls"
                              >
                              <label for="b_inquiry_r_radio1">{{ t('board.label.seminar.online_smn') }}<!-- 온라인 화상 세미나 --></label>
                            </div>
                          </div>
                          <div :class="['input-wrap', errors.smnPlcRmkOn ? 'error': '']" id="b_inquiry_r_cont1">
                            <Field
                              as="input"
                              name="smnPlcRmkOn"
                              v-model="params.smnPlcRmkOn"
                              :placeholder="t('board.msg.seminar.online_placeholder')"
                              :disabled="params.smnPlcCls === 'OFF'"
                            />
                            <p class="error-msg">{{ errors.smnPlcRmkOn }}</p>
                          </div>
                        </div>
                      </li>
                      <li class="cont-input-wrap">
                        <div class="cont-input-wrap__flex">
                          <div class="input-radio">
                            <div class="input-radio-indiv">
                              <input
                                type="radio"
                                v-model="params.smnPlcCls"
                                id="b_inquiry_r_radio2"
                                name="b_inquiry_r_radio"
                                value="OFF"
                                @change="fnChangeSmnPlcCls"
                              >
                              <label for="b_inquiry_r_radio2">{{ t('common.label.offline') }}<!-- 오프라인 --></label>
                            </div>
                          </div>
                          <div :class="['input-wrap', errors.smnPlcRmkOff ? 'error': '']" id="b_inquiry_r_cont1">
                            <Field
                              as="input"
                              name="smnPlcRmkOff"
                              v-model="params.smnPlcRmkOff"
                              :placeholder="t('board.msg.seminar.offline_placeholder')"
                              :disabled="params.smnPlcCls === 'ON'"
                            />
                            <p class="error-msg">{{ errors.smnPlcRmkOff }}</p>
                          </div>
                        </div>
                      </li>
                    </ul>
                    <Field as="input" name="smnPlcCls" v-model="params.smnPlcCls" hidden/>
                  </td>
                </tr>
                <tr v-if="params.seq">
                  <th class="wf-200 tooltip"><span class="txt">{{ t('board.label.seminar.apply_reg_cnt') }}<!-- 참석 신청 수 --></span><span class="box right">{{ t('board.msg.seminar.apply_desc') }}<!-- 현재까지 참석 신청한 인원수입니다. --></span></th>
                  <td class="b-seminar-r-apply"><span>{{ params.applyCnt }} / {{ params.applyMaxCnt }}</span> {{ t('board.label.seminar.user_cnt') }}<!-- 명 --></td>
                </tr>
                <tr>
                  <th class="wf-200 tooltip">
                    <span class="txt">{{ t('board.label.seminar.apply_cnt') }}<!-- 참석 인원 수 --></span>
                    <span class="box right">{{ t('board.msg.seminar.smn_cancel_desc') }}<!-- 신청 인원 수가 최소 인원수 미만인 경우 세미나가 취소될 수 있습니다. --></span>
                  </th>
                  <td class="b-seminar-r-num">
                    <div :class="errors.applyMinCnt ? 'error' : ''">
                      <div class="cont-input-wrap__flex start">
                        <div class="cont-input-wrap__pos">
                          <ap-input-number
                            :input-class="'cont-input-month t-center'"
                            v-model:value="params.applyMinCnt"
                            :placeholder="t('board.label.seminar.min')"
                          />
                          <Field
                            as="input"
                            type="tel"
                            name="applyMinCnt"
                            v-model="params.applyMinCnt"
                            hidden
                          />
                        </div>
                        <span>-</span>
                        <div class="cont-input-wrap__pos">
                          <ap-input-number
                            :input-class="'cont-input-month t-center'"
                            v-model:value="params.applyMaxCnt"
                            :placeholder="t('board.label.seminar.max')"
                          />
                          <Field
                            as="input"
                            type="tel"
                            name="applyMaxCnt"
                            v-model="params.applyMaxCnt"
                            hidden
                          />
                        </div>
                        <span>{{ t('board.label.seminar.user_cnt') }}</span>
                      </div>
                      <p class="error-msg">{{ errors.applyMinCnt }}</p>
                    </div>
                  </td>
                </tr>
                <tr>
                  <th class="wf-200">{{ t('board.label.seminar.raw_info') }}<!-- 원료 정보 --></th>
                  <td>
                    <div class="cont-input-wrap__flex">
                      <div class="cont-input-wrap__border full">
                        <template v-if="params.rawInfo && params.rawInfo.length > 0">
                          <p
                            v-for="(raw, idx) in params.rawInfo"
                            :key="idx"
                            class="item">
                            {{ raw.rawNmKo }}
                            <button
                              class='del-btn'
                              @click="fnDelTargetRaw(idx)"
                            ></button>
                          </p>
                        </template>
                      </div>
                      <a
                        href="#"
                        class="btn typeA square search-btn-indiv"
                        @click="fnOpenPopup('RawListPop', 1200)"
                      ><span>search</span></a>
                    </div>
                  </td>
                </tr>
                <tr>
                  <th class="wf-200">{{ t('board.label.seminar.content') }}<!-- 내용 --></th>
                  <td :class="errors.content ? 'error' : ''">
                    <jodit-editor
                      v-model="params.content"
                      :height="'400px'"
                      :uploadCd="uploadParams.uploadCd"
                    />
                    <Field as="input" name="content" v-model="params.content" hidden/>
                    <p class="error-msg">{{ errors.content }}</p>
                  </td>
                </tr>
                <tr>
                  <th class="wf-200">{{ t('common.label.attach1') }}<!-- 파일첨부 --></th>
                  <td>
                    <upload-file :upload-cd="uploadParams.uploadCd" server-del="Y" file-btn-class="file-large-btn"/>
                  </td>
                </tr>
                <tr>
                  <th class="wf-200">Youtube URL</th>
                  <td :class="(errors.ytbUrl ? 'error' : '')">
                    <div class="wf-200">https://youtube.com/embed/<Field as="input" name="ytbUrl" style="width:76%;" v-model="params.ytbUrl" :placeholder="t('board.msg.notice.desc10')" /></div><!-- Youtube Url 코드를 입력해주십시오 -->
                    <!-- <Field as="input" name="ytbUrl" v-model="params.ytbUrl" /> -->
                    <p class="error-msg">{{ errors.ytbUrl }}</p>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div class="cont-bot">
          <div class="btn-wrap center">
            <a href="#" @click.prevent="fnCancel" class="btn typeB gray"><span>{{ t('common.label.cancel') }}<!-- 취소 --></span></a>
            <a href="#" @click.prevent="fnSave" class="btn typeB">
              <template v-if="params.seq">
                <span class="mod">{{ t('common.label.modify') }}<!-- 수정 --></span>
              </template>
              <template v-else>
                <span class="complete">{{ t('common.label.register') }}<!-- 등록 --></span>
              </template>
            </a>
          </div>
        </div>
      </div>
    </div>
    <teleport to="#common-modal">
      <ap-popup>
        <component
          :is="popupContent"
          :parent-info="{ rawInfo: params.rawInfo }"
        />
      </ap-popup>
    </teleport>
  </div>
</template>

<script>
import { useSeminar } from '@/compositions/useSeminar'

import { defineAsyncComponent, provide } from 'vue'
import { useRoute } from 'vue-router'
import { Field } from 'vee-validate'

import 'jodit/build/jodit.min.css'

export default {
  name: 'SeminarRegister',
  components: {
    UploadFile: defineAsyncComponent(() => import('@/components/comm/UploadFile.vue')),
    JoditEditor: defineAsyncComponent(() => import('@/components/comm/JoditEditor.vue')),
    ApDateTimePicker: defineAsyncComponent(() => import('@/components/comm/ApDateTimePicker.vue')),
    ApPopup: defineAsyncComponent(() => import('@/components/comm/ApPopup.vue')),
    ApInputNumber: defineAsyncComponent(() => import('@/components/comm/ApInputNumber.vue')),
    RawListPop: defineAsyncComponent(() => import('@/components/popup/RawListPop.vue')),
    Field
  },
  setup () {
    const route = useRoute()
    const seq = route.query.seq
    const routeParams = route.query.params

    const {
      t,
      params,
      uploadParams,
      popupContent,
      errors,
      fetchSeminar,
      fnChangeSmnPlcCls,
      fnSave,
      fnCancel,
      fnOpenPopup,
      fnDelTargetRaw,
      fnSettingReReqSmnInfo,
      selectSmDtmFunc
    } = useSeminar()

    provide(`upload-${uploadParams.value.uploadCd}`, uploadParams.value)

    if (seq) {
      uploadParams.value.targetKey = '' + seq
      fetchSeminar(seq).then(res => {
        params.value.ytbUrl = params.value.ytbUrl.replace('https://www.youtube.com/embed/', '')
      })
    } else if (routeParams) {
      fnSettingReReqSmnInfo(JSON.parse(routeParams))
    }

    return {
      t,
      params,
      popupContent,
      fnChangeSmnPlcCls,
      errors,
      uploadParams,
      fnSave,
      fnCancel,
      fnOpenPopup,
      fnDelTargetRaw,
      selectSmDtmFunc
    }
  }
}
</script>

<style>

</style>
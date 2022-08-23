<template>
  <div>
    <div class="box">
      <div class="box-cont">
        <div class="cont-top cont-top2">
          <h2 class="cont-top-title">{{ t('board.label.council.list_title') }}<!-- 신원료협의체 --></h2>
        </div>
        <div class="cont-mid">
          <div class="cont-table">
            <table class="tb typeB typeB__input__border">
              <caption class="hidden">{{ t('board.label.council.list_title') }}<!-- 신원료협의체 --></caption>
              <tbody>
                <tr>
                  <th class="wf-200">{{ t('board.label.council.title') }}<!-- 제목 --></th>
                  <td :class="(errors.title ? 'error' : '')">
                    <Field as="input" name="title" v-model="params.title" />
                    <p class="error-msg">{{ errors.title }}</p>
                  </td>
                </tr>
                <tr>
                  <th class="wf-200">{{ t('board.label.council.cfrc_type') }}<!-- 회의분류 --></th>
                  <td>
                    <div class="input-radio">
                      <div class="input-radio-indiv">
                        <input
                          v-model="params.cfrcType"
                          type="radio"
                          id="b_council_r_radio1_1"
                          name="b_council_r_radio1_"
                          value="EX"
                        >
                        <label for="b_council_r_radio1_1">{{ t('board.label.council.be_exam') }}<!-- 검토예정 --></label>
                      </div>
                      <div class="input-radio-indiv">
                        <input
                          v-model="params.cfrcType"
                          type="radio"
                          id="b_council_r_radio1_2"
                          name="b_council_r_radio1_"
                          value="IN"
                        >
                        <label for="b_council_r_radio1_2">{{ t('board.label.council.be_intro') }}<!-- 도입예정 --></label>
                      </div>
                    </div>
                  </td>
                </tr>
                <tr>
                  <th class="wf-200">{{ t('board.label.council.cfrc_dtm') }}<!-- 회의 일시 --></th>
                  <td :class="(errors.cfrcDtm ? 'error' : '')">
                    <ap-date-time-picker
                      v-model:date="params.cfrcDtm"
                      :read-only="true"
                      :style-class="'ro-white'"
                    />
                    <Field as="input" name="cfrcDtm" v-model="params.cfrcDtm" hidden/>
                    <p class="error-msg">{{ errors.cfrcDtm }}</p>
                  </td>
                </tr>
                <tr>
                  <th class="wf-200">{{ t('board.label.council.cfrc_place') }}<!-- 회의 장소 --></th>
                  <td class="b-council-r-place">
                    <ul>
                      <li class="cont-input-wrap">
                        <div class="cont-input-wrap__flex">
                          <div class="input-radio">
                            <div class="input-radio-indiv">
                              <input
                                type="radio"
                                v-model="params.cfrcPlcCls"
                                id="b_council_r_radio2_1"
                                name="b_council_r_radio2_"
                                value="ON"
                                @change="fnChangeCfrcPlcCls"
                              >
                              <label for="b_council_r_radio2_1">{{ t('common.label.online') }}<!-- 온라인 --></label>
                            </div>
                          </div>
                          <div :class="['input-wrap', errors.cfrcPlcRmkOn ? 'error': '']" id="b_inquiry_r_cont1">
                            <Field
                              as="input"
                              name="cfrcPlcRmkOn"
                              v-model="params.cfrcPlcRmkOn"
                              :placeholder="t('board.msg.council.online_placeholder')"
                              :disabled="params.cfrcPlcCls === 'OFF'"
                            />
                            <p class="error-msg">{{ errors.cfrcPlcRmkOn }}</p>
                          </div>
                        </div>
                      </li>
                      <li class="cont-input-wrap">
                        <div class="cont-input-wrap__flex">
                          <div class="input-radio">
                            <div class="input-radio-indiv">
                              <input
                                type="radio"
                                v-model="params.cfrcPlcCls"
                                id="b_council_r_radio2_2"
                                name="b_council_r_radio2_"
                                value="OFF"
                                @change="fnChangeCfrcPlcCls"
                              >
                              <label for="b_council_r_radio2_2">{{ t('common.label.offline') }}<!-- 오프라인 --></label>
                            </div>
                          </div>
                          <div :class="['input-wrap', errors.cfrcPlcRmkOff ? 'error': '']" id="b_inquiry_r_cont2">
                            <Field
                              as="input"
                              name="cfrcPlcRmkOff"
                              v-model="params.cfrcPlcRmkOff"
                              :placeholder="t('board.msg.council.offline_placeholder')"
                              :disabled="params.cfrcPlcCls === 'ON'"
                            />
                            <p class="error-msg">{{ errors.cfrcPlcRmkOff }}</p>
                          </div>
                        </div>
                      </li>
                    </ul>
                    <Field as="input" name="cfrcPlcCls" v-model="params.cfrcPlcCls" hidden/>
                  </td>
                </tr>
                <tr>
                  <th class="wf-200">{{ t('board.label.council.target_raw') }}<!-- 대상 원료 --></th>
                  <td :class="[ errors.rawInfoYn ? 'error' : '' ]">
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
                        @click="fnOpenPopup('RawListPop')"
                      ><span>search</span></a>
                    </div>
                    <p class="error-msg">{{ errors.rawInfoYn }}</p>
                    <Field as="input" name="rawInfoYn" v-model="rawInfoYn" hidden/>
                  </td>
                </tr>
                <!-- <tr>
                  <th class="wf-200">{{ t('board.label.council.content') }}</th>
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
                  <th class="wf-200">{{ t('common.label.attach1') }}</th>
                  <td>
                    <upload-file :upload-cd="uploadParams.uploadCd" server-del="Y" file-btn-class="file-large-btn"/>
                  </td>
                </tr> -->
              </tbody>
            </table>
          </div>
        </div>
        <div class="cont-bot">
          <div class="btn-wrap center">
            <a href="#" @click.prevent="fnCancel" class="btn typeB gray"><span>{{ t('common.label.cancel') }}<!-- 취소 --></span></a>
            <a 
              v-if="params.cfrcStatus === 'CCS0010'"
              href="#"
              @click.prevent="fnSave('TEMP')"
              class="btn typeB gray"
            >
              <span class="mod">{{ t('common.label.temp_save') }}<!-- 임시저장 --></span>
            </a>
            <a
              href="#"
              @click.prevent="fnSave('CONFIRM')"
              class="btn typeB"
            >
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
          :parent-info="{ rawInfo: params.rawInfo, flag: 'R' }"
        />
      </ap-popup>
    </teleport>
  </div>
</template>

<script>
import { useCouncil } from '@/compositions/useCouncil'

import { defineAsyncComponent, provide } from 'vue'
import { useRoute } from 'vue-router'
import { Field } from 'vee-validate'

import 'jodit/build/jodit.min.css'

export default {
  name: 'CouncilRegister',
  components: {
    // UploadFile: defineAsyncComponent(() => import('@/components/comm/UploadFile.vue')),
    // JoditEditor: defineAsyncComponent(() => import('@/components/comm/JoditEditor.vue')),
    ApDateTimePicker: defineAsyncComponent(() => import('@/components/comm/ApDateTimePicker.vue')),
    ApPopup: defineAsyncComponent(() => import('@/components/comm/ApPopup.vue')),
    RawListPop: defineAsyncComponent(() => import('@/components/popup/RawListPop.vue')),
    Field
  },
  setup () {
    const route = useRoute()
    const seq = route.query.seq

    const {
      t,
      params,
      rawInfoYn,
      // uploadParams,
      popupContent,
      errors,
      fetchCouncil,
      fnChangeCfrcPlcCls,
      fnSave,
      fnCancel,
      fnOpenPopup,
      fnDelTargetRaw
    } = useCouncil()

    // provide(`upload-${uploadParams.value.uploadCd}`, uploadParams.value)

    if (seq) {
      // uploadParams.value.targetKey = '' + seq
      fetchCouncil(seq)
    }

    return {
      t,
      params,
      rawInfoYn,
      popupContent,
      fnChangeCfrcPlcCls,
      errors,
      // uploadParams,
      fnSave,
      fnCancel,
      fnOpenPopup,
      fnDelTargetRaw
    }
  }
}
</script>

<style scoped>
  .error .cont-input-wrap__border { border-color: #FF3636; }
</style>

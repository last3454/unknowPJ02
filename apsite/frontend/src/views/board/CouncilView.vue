<template>
  <div>
    <div class="box">
      <div class="box-cont">
        <div class="cont-top cont-top2">
          <h2 class="cont-top-title">{{ t('board.label.council.list_title') }}<!-- 신원료협의체 --></h2>
        </div>
        <div class="cont-mid">
          <div class="b-v-wrap">
            <div class="b-v-top">
              <div class="b-v-tit-wrap p">
                <h3 class="b-v-tit">
                  {{ params.title }}
                </h3>
              </div>
              <div class="b-v-info-wrap">
                <div class="b-v-info-from">
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
                    신원료협의 정보
                  </caption>
                  <tbody>
                    <tr>
                      <th class="wf-200">{{ t('board.label.council.cfrc_type') }}<!-- 회의분류 --></th>
                      <td>{{ params.cfrcTypeDesc }}</td>
                    </tr>
                    <tr>
                      <th class="wf-200">{{ t('board.label.council.cfrc_dtm') }}<!-- 회의일시 --></th>
                      <td>{{ params.cfrcDtmDesc }}</td>
                    </tr>
                    <tr>
                      <th class="wf-200">{{ t('board.label.council.cfrc_place') }}<!-- 회의장소 --></th>
                      <template v-if="params.cfrcPlcCls === 'ON'">
                        <td>{{ params.cfrcPlcClsDesc }} / <span><a :href="params.cfrcPlcRmk" target="_blank">{{ params.cfrcPlcRmk }}</a></span></td>
                      </template>
                      <template v-else-if="params.cfrcPlcCls === 'OFF'">
                        <td>{{ params.cfrcPlcClsDesc }} / {{ params.cfrcPlcRmk }}</td>
                      </template>
                      <template v-else>
                        <td></td>
                      </template>
                    </tr>
                    <tr>
                      <th class="wf-200">{{ t('board.label.council.target_raw') }}<!-- 대상원료 --></th>
                      <td>
                        <div class="cont-input-wrap__flex">
                            <div class="cont-input-wrap__border full">
                                <p
                                  v-for="(raw, idx) in params.rawInfo"
                                  :key="idx"
                                  class="item"
                                >
                                  {{ raw.rawNmKo }}
                                  <button
                                    v-if="params.cfrcStatus !== 'CCS0030' && !raw.readOnly"
                                    class="del-btn"
                                    @click="fnDeleteTargetRaw(raw)"
                                  />
                                </p>
                            </div>
                            <a
                              v-if="params.cfrcStatus !== 'CCS0030' && commonUtils.checkAuth('SGG000003|SGG000007|SGG000011|SGG000012|SGG000013|SGG000014')"
                              href="#"
                              class="btn typeA square search-btn-indiv"
                              @click="fnOpenPopup('RawListPop')"
                              title="대상원료 추가"
                            ><span>추가</span></a>
                        </div>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
        <div class="cont-bot">
          <div class="btn-wrap center">
            <a
              href="#"
              class="btn typeB gray"
              @click.prevent="fnLinkList()"
            >
              <span class="list">{{ t('common.label.list') }}<!-- 목록 --></span>
            </a>
            <template v-if="updateDeleteAuth">
              <a
                href="#"
                class="btn typeB gray"
                @click.prevent="fnDelete()"
              >
                <span class="del">{{ t('common.label.delete') }}<!-- 삭제 --></span>
              </a>
              <a
                href="#"
                class="btn typeB"
                @click.prevent="fnLinkReg(params.seq)"
              >
                <span class="mod">{{ t('common.label.modify') }}<!-- 수정 --></span>
              </a>
            </template>
            <a
              v-if="cfrcResultRegAuth"
              href="#b_council_v_result_box"
              class="btn typeB long"
              @click="fnToggleCfrcFinishBtn(true)"
            >
              <span class="complete">{{ t('board.label.council.cfrc_finish') }}<!-- 회의완료 --></span>
            </a>
          </div>
        </div>
      </div>
    </div>
    <div
      v-show="cfrcFinishAreaDispFlag || params.cfrcStatus === 'CCS0030'"
      class="box"
      id="b_council_v_result_box"
    >
      <div class="box-cont">
        <div class="cont-top">
          <h2 class="cont-top-title">{{ t('board.label.council.cfrc_result') }}<!-- 회의결과 --></h2>
        </div>
        <div class="cont-mid">
          <template v-if="params.rawInfo && params.rawInfo.length > 0">
            <div
              v-for="(raw, idx) in params.rawInfo"
              :key="idx"
              class="cont-table"
            >
              <table class="tb typeB typeB__input__border">
                <caption class="hidden">회의결과</caption>
                <tbody>
                  <tr>
                    <th class="wf-200">{{ t('board.label.council.target_raw') }}<!-- 대상원료 --></th>
                    <td>{{ raw.rawNmEn }}</td>
                  </tr>
                  <tr>
                    <th class="wf-200">{{ t('board.label.council.intro_yn') }}<!-- 도입여부 --></th>
                    <td>
                      <template v-if="params.cfrcStatus === 'CCS0030' && !commonUtils.checkAuth('SGG000003|SGG000007|SGG000011|SGG000012|SGG000013|SGG000014')">
                        {{ raw.introCdDesc }}
                      </template>
                      <template v-else>
                        <div class="input-radio">
                          <div class="input-radio-indiv"><input v-model="raw.introCd" type="radio" :id="`b_council_v_radio${idx}_1`" :name="`b_council_v_radio${idx}_`" value="RIS0010"><label :for="`b_council_v_radio${idx}_1`">{{ t('board.label.council.intro_cancel') }}<!-- 도입취소 --></label></div>
                          <div class="input-radio-indiv"><input v-model="raw.introCd" type="radio" :id="`b_council_v_radio${idx}_2`" :name="`b_council_v_radio${idx}_`" value="RIS0020"><label :for="`b_council_v_radio${idx}_2`">{{ t('board.label.council.intro_defer') }}<!-- 도입보류 --></label></div>
                          <div class="input-radio-indiv"><input v-model="raw.introCd" type="radio" :id="`b_council_v_radio${idx}_3`" :name="`b_council_v_radio${idx}_`" value="RIS0030"><label :for="`b_council_v_radio${idx}_3`">{{ t('board.label.council.intro_approval') }}<!-- 도입승인 --></label></div>
                        </div>
                      </template>
                    </td>
                  </tr>
                  <tr>
                    <th class="wf-200">{{ t('board.label.council.cfrc_opn') }}<!-- 회의의견 --></th>
                    <td>
                      <template v-if="params.cfrcStatus === 'CCS0030' && !commonUtils.checkAuth('SGG000003|SGG000007|SGG000011|SGG000012|SGG000013|SGG000014')">
                        {{ raw.cfrcOpn }}
                      </template>
                      <template v-else>
                        <input v-model="raw.cfrcOpn" type="text">
                      </template>
                    </td>
                  </tr>
                  <tr>
                    <th class="wf-200">{{ t('common.label.attach2') }}<!-- 첨부파일 --></th>
                    <td>
                      <!-- 진행상태가 '회의완료'인 경우, 파일 목록 가져와야 함.(다운로드만 가능) -->
                      <template v-if="params.cfrcStatus === 'CCS0030' && !commonUtils.checkAuth('SGG000003|SGG000007|SGG000011|SGG000012|SGG000013|SGG000014')">
                        <div
                          v-if="filesMap['COUNCIL02']"
                          class="b-v-file-wrap"
                        >
                          <a
                            v-for="(item, i) in filesMap['COUNCIL02'][`${params.seq}_${idx+1}`]"
                            :key="`attach_${idx}_${i}`"
                            href="#"
                            @click.prevent="downloadFile(item.seq, item.fileNm)"
                            class="i-btn medium typeB gray"
                          >
                            <span class="down">{{ item.fileNm }}</span>
                          </a>
                        </div>
                      </template>
                      <!-- 그 외의 경우, 첨부 가능 상태 -->
                      <template v-else>
                        <upload-file
                          upload-cd="COUNCIL02"
                          server-del="Y"
                          file-btn-class="file-large-btn"
                          :parent-info="rawUploadParamsList[idx]"
                        />
                      </template>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </template>
        </div>
        <div
          v-if="params.cfrcStatus !== 'CCS0030'"
          class="cont-bot"
        >
          <div class="btn-wrap center">
            <a href="#" @click.prevent="fnToggleCfrcFinishBtn(false)" class="btn typeB gray long"><span>{{ t('common.label.cancel') }}<!-- 취소 --></span></a>
            <a href="#" @click.prevent="fnSaveCfrcResult('INSERT')" class="btn typeB"><span class="complete">{{ t('common.label.register') }}<!-- 등록 --></span></a>
          </div>
        </div>
        <div
          v-else-if="params.cfrcStatus == 'CCS0030' && commonUtils.checkAuth('SGG000003|SGG000007|SGG000011|SGG000012|SGG000013|SGG000014')"
          class="cont-bot"
        >
          <div class="btn-wrap center">
            <a href="#" @click.prevent="fnSaveCfrcResult('UPDATE')" class="btn typeB"><span class="complete">{{ t('common.label.modify') }}<!-- 수정 --></span></a>
          </div>
        </div>
      </div>
    </div>
    <teleport to="#common-modal">
      <ap-popup>
        <component
          :is="popupContent"
          :parent-info="{ rawInfo: params.rawInfo, flag: 'V' }"
        />
      </ap-popup>
    </teleport>
  </div>
</template>

<script>
import { defineAsyncComponent, inject } from 'vue'
import { useRoute } from 'vue-router'
import { useCouncil } from '@/compositions/useCouncil'
import { useUploadFile } from '@/compositions/useUploadFile'

export default {
  name: 'CouncilView',
  components: {
    UploadFile: defineAsyncComponent(() => import('@/components/comm/UploadFile.vue')),
    ApPopup: defineAsyncComponent(() => import('@/components/comm/ApPopup.vue')),
    RawListPop: defineAsyncComponent(() => import('@/components/popup/RawListPop.vue'))
  },
  setup() {
    const commonUtils = inject('commonUtils')
    const route = useRoute()
    const seq = route.query.seq

    const {
      myInfo,
      t,
      params,
      popupContent,
      updateDeleteAuth,
      cfrcResultRegAuth,
      cfrcFinishAreaDispFlag,
      rawUploadParamsList,
      fetchCouncil,
      fnLinkList,
      fnLinkReg,
      fnToggleCfrcFinishBtn,
      fnSaveCfrcResult,
      fnDelete,
      fnOpenPopup,
      fnDeleteTargetRaw
    } = useCouncil()

    const {
      filesMap,
      fetchUploadMultiFiles,
      downloadFile
    } = useUploadFile()

    const init = async () => {
      if (seq) {
        await fetchCouncil(seq)

        if (params.value.rawInfo.length > 0) {
          let idx = 0
          for (const raw of params.value.rawInfo) {
            await fetchUploadMultiFiles({ targetKey: `${seq}_${idx+1}`, uploadCd: 'COUNCIL02' })
            idx++
          }
        }
      }
    }

    init()

    return {
      commonUtils,
      myInfo,
      t,
      params,
      popupContent,
      updateDeleteAuth,
      cfrcResultRegAuth,
      cfrcFinishAreaDispFlag,
      rawUploadParamsList,
      filesMap,
      fnLinkList,
      fnLinkReg,
      fnToggleCfrcFinishBtn,
      downloadFile,
      fnSaveCfrcResult,
      fnDelete,
      fnOpenPopup,
      fnDeleteTargetRaw
    }
  }
}
</script>

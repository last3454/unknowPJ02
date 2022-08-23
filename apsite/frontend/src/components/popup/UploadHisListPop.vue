<template>
  <div class="modal-content">
    <div class="modal-tit-wrap">
      <p class="modal-tit">{{ t('common.label.mod_his') }}<!-- 변경이력 --></p>
    </div>
    <div class="modal-cont-wrap">
      <div class="cont-table cont-table-scrollY">
        <table class="tb typeA">
          <caption class="hidden">
            변경이력
          </caption>
          <thead>
            <tr>
              <th class="w-12">Ver</th>
              <th>{{ t('common.label.attach_nm') }}<!-- 파일명 --></th>
              <th class="w-15">Date</th>
              <th class="w-24" v-if="isReviewBtn">{{ t('common.label.fixed_yn') }}</th> <!-- 확정여부 -->
            </tr>
          </thead>
          <tbody>
            <template v-if="hisList && hisList.length > 0">
              <template v-for="(his, idx) in hisList" :key="idx">
                <tr>
                  <td
                    v-if="his.rowNum === '1'"
                    :rowspan="his.verCnt"
                  >
                    {{ String(his.targetKey) === String(props.parentInfo.targetKey) ? props.parentInfo.rawVer : props.parentInfo.preRawVer }}
                  </td>
                  <td class="t-left">
                    <a
                      href="#"
                      class="t-link"
                      @click.prevent="downloadFile(his.seq, his.fileNm)"
                    >{{ his.fileNm }}</a>
                  </td>
                  <td>{{ his?.regDtm.split(' ')[0].split('-').join('.') ?? '' }}</td>
                  <td v-if="isReviewBtn">
                    <template v-if="his.fixedYn">
                      {{ his.fixedYn === 'Y' ? t('common.label.fixed_y') : t('common.label.fixed_n') }}
                    </template>
                  </td>
                </tr>
              </template>
            </template>
            <template v-else>
              <tr>
                <td colspan="3">{{ t('common.msg.no_data') }}</td>
              </tr>
            </template>
          </tbody>
        </table>
      </div>
      <div class="btn-wrap">
        <a @click.prevent="onConfirm" class="btn typeB">
          <span>{{ t('common.label.alert_ok') }}<!-- 확인 --></span>
        </a>
      </div>
    </div>
    <div class="modal-close-btn">
      <a @click.prevent="onConfirm" title="팝업창 닫기">팝업창 닫기</a>
    </div>
  </div>
</template>

<script>
import { useUploadFile } from '@/compositions/useUploadFile'

import { inject } from 'vue'

export default {
  name: 'UploadHisListPop',
  props: {
    parentInfo: {
      type: Object,
      default() {
        return {
          targetKey: '',
          uploadCd: '',
          isReviewBtn: false
        }
      },
    },
  },
  setup(props, { emit }) {
    const t = inject('t')
    const commonUtils = inject('commonUtils')

    const {
      hisList,
      downloadFile,
      fetchUploadFileHistory
    } = useUploadFile()

    if (!commonUtils.isEmpty(props.parentInfo.targetKey) && !commonUtils.isEmpty(props.parentInfo.uploadCd) && !commonUtils.isEmpty(props.parentInfo.preRecordCd)) {
      const obj = {
        targetKey: props.parentInfo.targetKey,
        uploadCd: props.parentInfo.uploadCd,
        preRecordCd: props.parentInfo.preRecordCd
      }
      fetchUploadFileHistory(obj)
    }

    // '확인' 이벤트
    const onConfirm = () => {
      emit('callBack')
    }

    return {
      t,
      props,
      hisList,
      downloadFile,
      onConfirm,
    }
  },
}
</script>

<style scoped>
  .cont-table-scrollY {min-height: 140px; height: inherit; max-height: 350px;}
</style>

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
              <th>{{ t('common.label.attach_nm') }}<!-- 파일명 --></th>
              <th class="w-15">Date</th>
              <th class="w-24">{{ t('common.label.fixed_yn') }}</th> <!-- 확정여부 -->
            </tr>
          </thead>
          <tbody>
            <template v-if="hisList && hisList.length > 0">
              <tr v-for="(his, idx) in hisList" :key="idx">
                <template v-if="his.ver === his.maxVer">
                  <td class="t-left">
                    <a
                      href="#"
                      class="t-link"
                      @click.prevent="downloadFile(his.seq, his.fileNm)"
                    >{{ his.fileNm }}</a>
                  </td>
                  <td>{{ his?.regDtm.split(' ')[0].split('-').join('.') ?? '' }}</td>
                  <td>
                    <template v-if="isAuth">
                      <select
                        class="slt"
                        v-model="his.fixedYn"
                        @change="changeFixedInfo(his.seq, his.ver)"
                      >
                        <option value="N">{{ t('common.label.fixed_n') }}</option>
                        <option value="Y">{{ t('common.label.fixed_y') }}</option>
                      </select>
                    </template>
                    <template v-else-if="his.fixedYn">
                      {{ his.fixedYn === 'Y' ? t('common.label.fixed_y') : t('common.label.fixed_n') }}
                    </template>
                  </td>
                </template>
              </tr>
            </template>
            <template v-else>
              <tr>
                <td colspan="3">{{ t('common.msg.no_data') }}</td>
              </tr>
            </template>
          </tbody>
        </table>
      </div>
      <div class="btn-wrap" v-if="!isAuth">
        <a @click.prevent="onConfirm" class="btn typeB">
          <span>{{ t('common.label.alert_ok') }}<!-- 확인 --></span>
        </a>
      </div>
      <div class="btn-wrap" v-else>
        <a class="btn typeB gray" @click.prevent="onConfirm"><span>{{ t('common.label.confirm_cancel') }}</span></a>
        <a class="btn typeB" @click.prevent="fnSave"><span>{{ t('common.label.save') }}</span></a>
      </div>
    </div>
    <div class="modal-close-btn">
      <a @click.prevent="onConfirm" :title="t('common.label.pop_close')">{{ t('common.label.pop_close') }}</a>
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
          uploadCd: ''
        }
      },
    },
  },
  setup(props, { emit }) {
    const t = inject('t')
    const commonUtils = inject('commonUtils')

    // RMQC 권한체크
    const isAuth = commonUtils.checkAuth('SGG000011|SGG000012|SGG000013|SGG000014')

    const {
      hisList,
      downloadFile,
      fetchUploadFileHistory,
      updateFixedFlag
    } = useUploadFile()

    if (!commonUtils.isEmpty(props.parentInfo.targetKey) && !commonUtils.isEmpty(props.parentInfo.uploadCd)) {
      const obj = {
        targetKey: props.parentInfo.targetKey,
        uploadCd: props.parentInfo.uploadCd,
        reviewFlag: 'Y'
      }
      fetchUploadFileHistory(obj)
    }

    // '확인' 이벤트
    const onConfirm = () => {
      emit('callBack', 'N')
    }

    const changeFixedInfo = (seq, ver) => {
      const exceptList = hisList.value.filter(vo => vo.seq !== seq && vo.ver === ver)

      if (exceptList) {
        exceptList.forEach(vo => {
          vo.fixedYn = 'N'
        })
      }
    }

    const fnSave = async () => {
      const maxVer = hisList.value[0].maxVer
      const fixFileList = hisList.value.filter(vo => vo.ver === maxVer)

      const result = await updateFixedFlag(fixFileList)
      if (result) {
        emit('callBack', 'Y')
      }
    }

    return {
      t,
      hisList,
      isAuth,
      downloadFile,
      onConfirm,
      changeFixedInfo,
      fnSave
    }
  },
}
</script>

<style scoped>
  .cont-table-scrollY { height: 300px;}
</style>

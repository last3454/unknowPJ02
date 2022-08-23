<template>
  <div class="modal-content">
    <div class="modal-tit-wrap">
      <p class="modal-tit">{{ t('board.label.seminar.attend_list_title') }}<!-- 참석신청내역 --></p>
    </div>
    <div class="modal-cont-wrap">
      <div class="cont-table cont-table-scrollY">
        <table class="tb typeA typeA__input">
          <caption class="hidden">
            참석신청내역
          </caption>
          <thead>
            <tr>
              <th class="w-30">{{ t('board.label.seminar.attend_list_name') }}<!-- 이름 --></th>
              <th>{{ t('board.label.seminar.attend_list_comp_nm') }}<!-- 부서/회사명 --></th>
            </tr>
          </thead>
          <tbody>
            <template v-if="attendList && attendList.length > 0">
              <tr v-for="(attend, idx) in attendList" :key="idx">
                <td class="t-left">{{ attend.userNm }}</td>
                <td class="t-left">{{ attend.compNm }}</td>
              </tr>
            </template>
            <template v-else>
              <tr>
                <td colspan="2">{{ t('common.msg.no_data') }}</td>
              </tr>
            </template>
          </tbody>
        </table>
      </div>
      <div class="btn-wrap">
        <a
          href="javascript:;"
          @click="onConfirm"
          class="btn typeB"
          ><span class="complete">{{ t('common.label.alert_ok') }}<!-- 확인 --></span></a
        >
      </div>
    </div>
    <div class="modal-close-btn">
      <a href="#" @click="onConfirm" title="팝업창 닫기">팝업창 닫기</a>
    </div>
  </div>
</template>

<script>
import { useSeminar } from '@/compositions/useSeminar'

import { inject } from 'vue'
import { useActions } from 'vuex-composition-helpers'

export default {
  name: 'SeminarAttendListPop',
  props: {
    parentInfo: {
      type: Object,
      default () {
        return {
          seq: 0
        }
      }
    }
  },
  setup(props) {
    const t = inject('t')
    const { closeAsyncPopup } = useActions(['closeAsyncPopup'])

    const {
      attendList,
      fetchSeminarAttendList
    } = useSeminar()

    if (props && props.parentInfo && props.parentInfo.seq !== 0) {
      fetchSeminarAttendList(props.parentInfo.seq)
    }

    // '확인' 이벤트
    const onConfirm = () => {
      closeAsyncPopup({ message: '확인' })
    }

    return {
      t,
      attendList,
      onConfirm
    }
  },
}
</script>

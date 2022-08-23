<template>
  <div v-if="mainCmmtList?.length" class="main-comm-tab-cont tab-cont active">
    <ul class="comment-list">
      <li
        v-for="(comment, idx) in mainCmmtList"
        :key="`comment_${idx}`"
        :class="comment.displayFlag"
      >
        <p class="root">{{ getCodeNm('RAW_STEP_MENU', comment.stepCd) }}</p>
        <p v-if="comment?.regUserNm" class="writer">
          {{ comment.regUserNm }}<span>{{ commonUtils.isNotEmpty(comment.userCompNm) ? comment.userCompNm : comment.apUserCompNm }}</span>
        </p>
        <p v-else class="writer">
          {{ t('ingredient.msg.commu_desc6') }}
        </p><!-- 유저정보가 없습니다. -->
        <p class="txt">
          <span v-html="comment.cmmtCont"></span>
          <button type="button" v-if="comment.cmmtLevel == 0" @click.prevent="fnMoveCommu(comment)">{{ t('main.label.communication.go_detail') }}</button><!-- 바로가기 -->
        </p>
        <p class="time">{{ comment.regDtm }}</p>
      </li>
    </ul>
  </div>
  <div v-else class="main-comm-tab-cont tab-cont active">
    <div class="noContent">
      {{ t('ingredient.msg.commu_desc5') }}
    </div><!-- Communication이 없습니다. -->
  </div>
</template>

<script>
import { inject } from 'vue'
import { useStore } from 'vuex'
import { useCode } from '@/compositions/useCode'
import { useCommunication } from '@/compositions/useCommunication'

export default {
  name: 'MainCommunicationMine',
  setup () {
    const t = inject('t')
    const commonUtils = inject('commonUtils')
    const { fetchCodeGroupMaps, codeGroupMaps, getCodeNm } = useCode()

    const store = useStore()
    const myInfo = store.getters.getMyInfo()

    const {
      params,
      fnDetail,
      fnMoveCommu,
      mainCmmtList,
      fetchMainComments,
    } = useCommunication()

    // 공통코드(RAW_STEP_MENU) 가져오기
    fetchCodeGroupMaps(['RAW_STEP_MENU'])

    fetchMainComments()

    return {
      t,
      params,
      myInfo,
      fnDetail,
      fnMoveCommu,
      commonUtils,
      codeGroupMaps,
      getCodeNm,
      mainCmmtList
    }
  }
}
</script>
<template>
  <div class="inner narrow">
    <div class="box">
      <div class="box-cont">
        <div class="cont-top cont-top2">
          <h2 class="cont-top-title">{{ t('board.label.sourcing.title') }}<!-- 원료소싱 --></h2>
        </div>
        <div class="cont-mid">
          <div class="b-v-wrap">
            <div class="b-v-top">
              <div class="b-v-tit-wrap">
                <h3 class="b-v-tit">{{ params.title }}</h3>
              </div>
                <div class="b-v-info-wrap">
                  <div class="b-v-info-from">
                    <span class="name">{{ params.regUserNm }}</span>
                    <span class="date">{{ commonUtils.convertDt(params.regDtm) }}</span>
                  </div>
                </div>
            </div>
            <div class="b-v-mid">
                <div v-html="params.content" class="b-v-cont-wrap"></div>
                <div v-if="filesMap['SOURCING01'] !== undefined && filesMap['SOURCING01'].length > 0" class="b-v-file-wrap">
                    <a href="#" v-for="item in filesMap['SOURCING01']" :key="'attach_' + item.seq" class="i-btn medium typeB gray" @click.prevent="downloadFile(item.seq, item.fileNm)">
                      <span class="down">{{ item?.fileNm ?? '' }}</span>
                    </a>
                </div>
            </div>
          </div>
          <div class="cont-bot">
            <div class="btn-wrap center">
              <a href="#" class="btn typeB gray" @click.prevent="goList()">
                <span class="list">{{ t('common.label.list') }}</span><!-- 목록 -->
              </a>
              <a v-if="isApUser === 'Y' && myInfo.userCd === params.regUserCd" href="#" class="btn typeB gray" @click.prevent="fnDelete(seq)">
                <span class="del">{{ t('common.label.delete') }}</span><!-- 삭제 -->
              </a>
              <a v-if="isApUser === 'Y' && myInfo.userCd === params.regUserCd" href="#" class="btn typeB"  @click.prevent="fnModify(seq)">
                <span class="mod">{{ t('common.label.modify') }}</span><!-- 수정 -->
              </a>
              <a href="#" class="btn typeB"  @click.prevent="fnMailPop()">
                <span class="mod">{{ t('board.label.sourcing.sendEmail')}}</span><!-- 메일발송 -->
              </a>
            </div>
          </div>
        </div>
      </div>
      <teleport to="#common-modal">
        <ap-popup>
          <component
            :is="popupContent"
            :seq="params.seq"
            :title="params.title"
            :toUserCd="params.regUserCd"
            :toUserNm="params.regUserNm"
          />
        </ap-popup>
      </teleport>
    </div>
  </div>
</template>

<script>
import { defineAsyncComponent, inject } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { useSourcing } from '@/compositions/useSourcing'
import { useUploadFile } from '@/compositions/useUploadFile'

export default {
  name: 'SourcingView',
  components: {
    ApPopup: defineAsyncComponent(() => import('@/components/comm/ApPopup.vue')),
    RawSourcingInquiryPop: defineAsyncComponent(() => import('@/components/popup/RawSourcingInquiryPop.vue'))
  },
  setup () {
    const t = inject('t')
    const route = useRoute()
    const router = useRouter()
    const store = useStore()
    const seq = route.query.seq
    const myInfo = store.getters.getMyInfo()

    const {
      params,
      commonUtils,
      popupContent,
      fetchSourcingView,
      updateViewCnt,
      isApUser,
      goList,
      fnModify,
      fnDelete,
      fnMailPop,
      fnPost
    } = useSourcing()

    const {
      filesMap,
      downloadFile,
      fetchUploadFiles
    } = useUploadFile()

    if (seq) {
      fetchSourcingView(seq).catch(err => {
        console.log(err)
        router.back()
      })

      fetchUploadFiles({ targetKey: '' + seq, uploadCd: 'SOURCING01' })

      if (isApUser.value === 'N') {
        updateViewCnt(seq)
      }
    }

    return {
      t,
      seq,
      params,
      myInfo,
      commonUtils,
      popupContent,
      isApUser,
      filesMap,
      downloadFile,
      goList,
      fnModify,
      fnDelete,
      fnMailPop,
      fnPost
    }
  }
}
</script>

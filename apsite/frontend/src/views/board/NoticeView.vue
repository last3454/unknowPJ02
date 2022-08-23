<template>
  <div class="inner narrow">
    <div class="box">
      <div class="box-cont">
        <div class="cont-top cont-top2">
          <h2 class="cont-top-title">{{ t('board.label.notice.L001') }}<!-- 공지사항 --></h2>
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
                <div v-if="filesMap['NOTICE01'] !== undefined && filesMap['NOTICE01'].length > 0" class="b-v-file-wrap">
                    <a href="#" v-for="item in filesMap['NOTICE01']" :key="'attach_' + item.seq" class="i-btn medium typeB gray" @click.prevent="downloadFile(item.seq, item.fileNm)">
                      <span class="down">{{ item.fileNm }}</span>
                    </a>
                </div>
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
          <div class="cont-bot">
            <div class="btn-wrap center">
              <a href="#" class="btn typeB gray" @click.prevent="goList()"><span class="list">{{ t('common.label.list')}}<!-- 목록 --></span></a>
              <a v-if="isAdmin" href="#" class="btn typeB gray" @click.prevent="onDelete(params.seq)"><span class="del">{{ t('common.label.delete')}}<!-- 삭제 --></span></a>
              <a v-if="isAdmin" href="#" class="btn typeB"  @click.prevent="goModify(params.seq)"><span class="mod">{{ t('common.label.modify')}}<!-- 수정 --></span></a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import { useRoute, useRouter } from 'vue-router'
import { useNotice } from '@/compositions/useNotice'
import { useUploadFile } from '@/compositions/useUploadFile'

export default {
  name: 'NoticeView',
  setup () {
    const route = useRoute()
    const router = useRouter()
    const seq = route.query.seq

    const {
      t,
      commonUtils,
      params,
      isAdmin,
      fetchBoard,
      onDelete,
      goList,
      goModify,
      updateNoticeViewCnt,
    } = useNotice()

    const {
      filesMap,
      fetchUploadFiles,
      downloadFile
    } = useUploadFile()

    if (seq) {
      fetchBoard(seq)
      .catch(err => {
        console.error(err)
        router.back()
      })

      fetchUploadFiles({ targetKey: '' + seq, uploadCd: 'NOTICE01' })

      if (!isAdmin) {
        updateNoticeViewCnt(seq)
      }
    }

    return {
      t,
      commonUtils,
      isAdmin,
      params,
      filesMap,
      goList,
      goModify,
      onDelete,
      downloadFile
    }
  }
}
</script>

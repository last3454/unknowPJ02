<template>
  <div class="inner narrow">
    <div class="box">
      <div class="box-cont">
        <div class="cont-top cont-top2">
          <h2 class="cont-top-title">{{ t('board.label.errorinq.error_inq') }}<!-- 오류문의 --></h2>
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
                <div v-if="filesMap['ERRORINQ01'] !== undefined && filesMap['ERRORINQ01'].length > 0" class="b-v-file-wrap">
                    <a href="#" v-for="item in filesMap['ERRORINQ01']" :key="'attach_' + item.seq" class="i-btn medium typeB gray" @click.prevent="downloadFile(item.seq, item.fileNm)">
                      <span class="down">{{ item.fileNm }}</span>
                    </a>
                </div>
            </div>
          </div>
          <div class="cont-bot">
            <div class="btn-wrap center">
              <a href="#" class="btn typeB gray" @click.prevent="goList()"><span class="list">{{ t('common.label.list')}}<!-- 목록 --></span></a>
              <a v-if="params.regUserCd === myInfo.userCd || isAdmin" href="#" class="btn typeB gray" @click.prevent="onDelete()"><span class="del">{{ t('common.label.delete')}}<!-- 삭제 --></span></a>
              <a v-if="params.regUserCd === myInfo.userCd || isAdmin" href="#" class="btn typeB"  @click.prevent="goModify()"><span class="mod">{{ t('common.label.modify')}}<!-- 수정 --></span></a>
              <a v-if="isAdmin && params.status !== 'EIS0030'" href="#" @click.prevent="fnAnswer(seq)" class="btn typeB"><span class="complete">{{ t('board.label.errorinq.btn_answer') }}<!-- 답변처리 --></span></a>
            </div>
          </div>
          <!-- 소통공간 영역 -->
          <div class="box">
            <div class="box-cont">
              <div class="cont-top">
                <h2 class="cont-top-title">{{ t('board.label.errorinq.commu') }}</h2><!-- 소통공간 -->
              </div>
              <div class="cont-mid">
                <div class="b-v-comment-wrap">
                  <template v-if="commuList?.length > 0">
                    <ul class="b-v-comment-list">
                      <li v-for="(item, idx) in commuList" :key="idx">
                        <dl>
                          <dt>{{ item?.regUserNm ?? t('board.msg.errorinq.no_user_msg1') }}</dt><!-- 유저정보가 없습니다 -->
                          <dd class="date">{{ item?.regDtm ?? '' }}</dd>
                          <dd class="comment">{{ item.delYn === 'Y' ? t('common.msg.del_comment') : item?.content ?? '' }}</dd>
                          <dd
                            v-if="item.delYn === 'N' && myInfo.userCd === item.regUserCd"
                            class="del"
                          >
                            <a
                              href="#"
                              @click.prevent="fnDelComment(seq, item.seq)"
                              title="댓글 삭제"
                            >{{ t('board.label.errorinq.delete') }}</a><!-- 삭제 -->
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
                  <textarea
                    v-model="commuContent.content"
                    name=""
                    id=""
                    cols="30"
                    rows="0"
                    :placeholder="t('board.msg.errorinq.view_msg1')"
                  ></textarea><!-- 오류문의에 대한 의견을 남겨주세요 -->
                  <a
                    href="#"
                    @click.prevent="insertComment(commuContent)"
                    class="btn typeB square comment-btn pa"
                  >{{ t('board.label.errorinq.comment') }}</a>
                </div><!-- comment -->
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import { inject } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { useErrorInq } from '@/compositions/useErrorInq'
import { useUploadFile } from '@/compositions/useUploadFile'
import { useActions } from 'vuex-composition-helpers'

export default {
  name: 'ErrorInqView',
  setup () {
    const t = inject('t')
    const commonUtils = inject('commonUtils')
    const route = useRoute()
    const router = useRouter()
    const store = useStore()
    const seq = route.query.seq
    const isAdmin = commonUtils.checkAuth('SGG000004')
    const myInfo = store.getters.getMyInfo()
    const { openAsyncConfirm, openAsyncAlert } = useActions(['openAsyncConfirm', 'openAsyncAlert'])

    const {
      params,
      commuList,
      fnAnswer,
      fetchBoard,
      fetchComment,
      fnInsertComment,
      fnDeleteComment,
      updateErrorInqViewCnt,
      deleteErrorInq
    } = useErrorInq()

    const {
      filesMap,
      fetchUploadFiles,
      downloadFile
    } = useUploadFile()

    fetchBoard(seq)
      .catch(err => {
        console.error(err)
        router.back()
    })

    fetchUploadFiles({ targetKey: '' + seq, uploadCd: 'ERRORINQ01' })

    if (!isAdmin) {
      updateErrorInqViewCnt(seq)
    }

    // 목록
    const goList = () => {
      router.push({ path: '/errorinq' })
    }

    // 수정
    const goModify = () => {
      router.push({ path: '/errorinq/modify', query: { seq: seq } })
    }

    // 삭제 버튼
    const onDelete = async () => {
      if (!await openAsyncConfirm({ message: t('common.msg.delete_confirm_msg') })) { // 정말로 삭제 하시겠습니까?
        return
      }

      deleteErrorInq(seq).then(async res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          await openAsyncAlert({ message: t('common.msg.delete_ok') }) // 삭제 되었습니다.
          router.push({ path: '/errorinq/list' })
        } else {
          openAsyncAlert({ message: resData.message })
        }
      })
    }

    const commuContent = {
      seq: seq,
      content: ''
    }

    const insertComment = async (item) => {
      if (commonUtils.isEmpty(commuContent.content)) {
        openAsyncAlert({ message: t('board.msg.errorinq.view_msg6') }) // 댓글을 입력해주십시오
        return
      }

      if (!await openAsyncConfirm({ message: t('board.msg.errorinq.view_msg2') })) { // 댓글을 등록하시겠습니까?
        return
      }

      fnInsertComment(item).then(async res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          fetchComment(seq)
          commuContent.content = ''
        } else {
          openAsyncAlert({ message: t('board.msg.errorinq.view_msg3') }) // 댓글 등록에 실패하였습니다
        }
      })
    }

    const fnDelComment = async (seq, comSeq) => {
      if (!await openAsyncConfirm({ message: t('board.msg.errorinq.view_msg4') })) { // 댓글을 삭제하시겠습니까?
        return
      }

      const items = {
        seq: seq,
        comSeq: comSeq
      }

      fnDeleteComment(items).then(async res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          fetchComment(seq)
        } else {
          openAsyncAlert({ message: t('board.msg.errorinq.view_msg5') }) // 댓글 삭제에 실패하였습니다
        }
      })
    }

    return {
      t,
      seq,
      myInfo,
      commonUtils,
      isAdmin,
      params,
      fnAnswer,
      commuList,
      filesMap,
      commuContent,
      insertComment,
      fnDelComment,
      goList,
      goModify,
      onDelete,
      downloadFile
    }
  }
}
</script>

<style scoped>
  .b-v-cont-wrap { overflow: auto; }
</style>

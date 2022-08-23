<template>
  <div class="inner narrow">
    <div class="box">
      <div class="box-cont">
        <div class="cont-top cont-top2">
          <h2 class="cont-top-title">{{ t('board.label.notice.L001') }}<!-- 공지사항 --></h2>
          <Field as="input" name="modifyYn" v-model="modifyYn" hidden/>
        </div>
        <div class="cont-mid">
          <div class="cont-table">
            <form id="frm">
              <table class="tb typeB typeB__input__border">
                <caption class="hidden">{{ t('board.label.notice.L001') }}</caption><!-- 공지사항 -->
                <tbody>
                  <tr>
                    <th class="wf-200">{{ t('board.label.notice.L006') }}</th><!-- 제목 -->
                    <td :class="(errors.title ? 'error' : '')">
                      <Field as="input" name="title" v-model="params.title" :placeholder="t('board.msg.sourcing.desc1')" />
                      <p class="error-msg">{{ errors.title }}</p>
                    </td>
                  </tr>
                  <tr>
                    <th class="wf-200">{{ t('board.label.notice.L012') }}</th><!-- 공지등록 -->
                    <td>
                      <div class="input-check">
                        <div class="input-check-fr">
                          <input type="checkbox" id="noticeYn" class="chk typeB" v-model="params.noticeYn" true-value="Y" false-value="N" />
                          <label for="noticeYn">{{ t('board.msg.notice.desc2') }}</label><!-- 공지사항으로 등록합니다 -->
                          <Field as="input" name="noticeYn" v-model="params.noticeYn" hidden/>
                          <p class="error-msg">{{ errors.noticeYn }}</p>
                        </div>
                      </div>
                    </td>
                  </tr>
                  <tr v-show="params.noticeYn === 'Y'">
                    <th class="wf-200">{{ t('board.label.notice.L013') }}</th><!-- 공지기간 -->
                    <td :class="errors.noticeStartDt ? 'error' : ''">
                      <ap-date-picker-range
                        v-model:startDt="params.noticeStartDt"
                        v-model:endDt="params.noticeEndDt"
                      />
                      <Field as="input" name="noticeStartDt" v-model="params.noticeStartDt" hidden/>
                      <p class="error-msg">{{ errors.noticeStartDt }}</p>
                    </td>
                  </tr>
                  <tr>
                    <th class="wf-200">{{ t('board.label.notice.L014') }}</th><!-- 내용 -->
                    <td>
                      <jodit-editor v-model="params.content" :height="'400px'" :uploadCd="uploadParams.uploadCd"></jodit-editor>
                    </td>
                  </tr>
                  <tr>
                    <th class="wf-200">{{ t('board.label.notice.L015') }}</th><!-- 파일첨부 -->
                    <td>
                      <upload-file
                        :upload-cd="'NOTICE01'"
                        server-del="Y"
                        file-btn-class="file-large-btn"
                      />
                    </td>
                  </tr>
                  <tr>
                    <th class="wf-200">{{ t('board.label.notice.L016') }}</th><!-- 동영상 URL -->
                    <td :class="(errors.ytbUrl ? 'error' : '')">
                      <div class="wf-200">https://youtube.com/embed/<Field as="input" name="ytbUrl" style="width:76%;" v-model="params.ytbUrl" :placeholder="t('board.msg.notice.desc10')" /></div><!-- Youtube Url을 등록해주십시오 -->
                      <p class="error-msg">{{ errors.ytbUrl }}</p>
                    </td>
                  </tr>
                </tbody>
              </table>

              <div class="cont-bot">
                <div class="btn-wrap center">
                    <a href="#" @click.prevent="fnCancel(modifyYn, params.seq)" class="btn typeB gray">
                      <span>{{ t('board.label.notice.L017') }}</span>
                    </a><!-- 취소 -->
                    <a href="#" @click.prevent="fnTempSave(uploadParams)" class="btn typeB">
                      <span class="complete">{{ t('board.label.notice.L018') }}</span>
                    </a><!-- 등록/수정 -->
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import { computed, reactive, defineAsyncComponent, provide, ref } from 'vue'
import { useNotice } from '@/compositions/useNotice'
import { useRoute, useRouter } from 'vue-router'
import { useActions } from 'vuex-composition-helpers'
import { Field } from 'vee-validate'

export default {
  name: 'NoticeRegister',
  components: {
    UploadFile: defineAsyncComponent(() => import('@/components/comm/UploadFile.vue')),
    JoditEditor: defineAsyncComponent(() => import('@/components/comm/JoditEditor.vue')),
    Field
  },
  setup () {
    const route = useRoute()
    const router = useRouter()
    const tempEditor = ref(null)
    const targetKey = ref('1')
    const { openAsyncAlert } = useActions(['openAsyncAlert'])

    const {
      t,
      commonUtils,
      params,
      errors,
      fnCancel,
      fetchBoard,
      fnTempSave
    } = useNotice()

     // 등록/수정화면 여부
    const modifyYn = computed(() => {
      if (route.name === 'notice-modify') {
        return 'Y'
      }
      return 'N'
    })

    // 업로드 관련
    const uploadParams = reactive({
      targetKey: '',
      uploadCd: 'NOTICE01',
      items: []
    })

    provide('upload-NOTICE01', uploadParams)

    if (modifyYn.value === 'Y') {
      const seq = route.query.seq
      targetKey.value = '' + seq
      uploadParams.targetKey = '' + seq

      fetchBoard(seq).then(res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          params.value.ytbUrl = params.value.ytbUrl.replace('https://www.youtube.com/embed/', '')
        }
      }).catch(err => {
          console.log(err)
          openAsyncAlert({ message: t('board.msg.notice.desc3') }) // 페이지를 실행할 수 없습니다.
          router.back()
        })
    }

    return {
      t,
      commonUtils,
      errors,
      params,
      modifyYn,
      tempEditor,
      uploadParams,
      targetKey,
      fnTempSave,
      fnCancel
    }
  }
}
</script>

<style scoped>
.box-form {
  width: 1000px;
  margin: auto;
  text-align: left;
  padding : 20px 10px 10px 10px;
  border: 1px solid #d8d8d8;
  background-color: #ffffff;
}
.box-form > div {
  padding-top: 5px;
  padding-bottom: 5px;
}
</style>
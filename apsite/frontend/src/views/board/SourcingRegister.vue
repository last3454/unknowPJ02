<template>
  <div class="inner narrow">
    <div class="box">
      <div class="box-cont">
        <div class="cont-top cont-top2">
          <h2 class="cont-top-title">{{ t('board.label.sourcing.title') }}</h2><!-- 원료소싱 -->
        </div>
        <div class="cont-mid">
          <div class="cont-table">
            <table class="tb typeB typeB__input__border">
              <caption class="hidden">{{ t('board.label.sourcing.title') }}</caption><!-- 원료소싱 -->
              <tbody>
                <tr>
                  <th class="wf-200">{{ t('board.label.sourcing.title2') }}</th><!-- 제목 -->
                  <td :class="(errors.title ? 'error' : '')">
                    <Field as="input" name="title" v-model="params.title" :placeholder="t('board.msg.sourcing.desc1')" />
                    <p class="error-msg">{{ errors.title }}</p>
                  </td>
                </tr>
                <tr>
                  <th class="wf-200">{{ t('board.label.sourcing.content') }}</th><!-- 내용 -->
                  <td>
                    <jodit-editor
                      v-model="params.content"
                      :height="'400px'"
                      :uploadCd="uploadParams.uploadCd"
                    ></jodit-editor>
                  </td>
                </tr>
                <tr>
                  <th class="wf-200">{{ t('board.label.sourcing.file') }}</th><!-- 파일첨부 -->
                  <td>
                    <upload-file
                      :upload-cd="'SOURCING01'"
                      server-del="Y"
                      file-btn-class="file-large-btn"
                    >
                    </upload-file>
                  </td>
                </tr>
                <tr>
                  <th class="wf-200">{{ t('board.label.sourcing.open') }}</th><!-- 공개 -->
                  <td>
                    <div class="cont-input-wrap__flex start">
                      <div class="input-check wf">
                        <div class="input-check-fr">
                          <input
                            type="checkbox"
                            class="chk typeB"
                            id="b_share_r_chk1"
                            v-model="params.openAllYn"
                            true-value="Y"
                            false-value="N"
                            @change="fnCngOpenAllYn()"
                          />
                          <label for="b_share_r_chk1">{{ t('board.label.sourcing.all') }}</label><!-- 전체 -->
                        </div>
                      </div>
                      <div class="cont-input-wrap__flex" id="b_share_r_cont1">
                        <div class="cont-input-wrap__flex large">
                          <input type="text" id="insertIgCompanyTxt" :value="langCd === 'ko' ? params.openRawCompNm : params.openRawCompEnm" :placeholder="t('board.label.sourcing.rawComp')" class="flatpickr-input" disabled><!-- 원료사 -->
                          <a href="#" v-if="params.openAllYn === 'N'" @click.prevent="fnSearchCompPop()" class="btn typeA square search-btn-indiv">
                            <span>{{ t('board.label.sourcing.search') }}</span>
                          </a><!-- 조회 -->
                        </div>
                      </div>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div class="cont-bot">
          <div class="btn-wrap right">
            <a href="#" @click.prevent="fnCancel(modifyYn, params.seq)" class="btn typeB gray">
              <span>{{ t('common.label.cancel') }}</span>
            </a><!-- 취소 -->
            <a href="#" @click.prevent="fnTempSave(uploadParams)" class="btn typeB">
              <span class="complete">{{ t('board.label.sourcing.regModify') }}</span>
            </a><!-- 등록/수정 -->
            <a v-if="myInfo.userCd === params.regUserCd && params.postStatus === 'N'" href="#" class="btn typeB"  @click.prevent="fnPost(seq, params.postStatus)">
              <span class="mod">{{ t('board.label.sourcing.post') }}</span>
            </a><!-- 게시 -->
            <a v-if="myInfo.userCd === params.regUserCd && params.postStatus === 'Y'" href="#" class="btn typeB"  @click.prevent="fnPost(seq, params.postStatus)">
              <span class="mod">{{ t('board.label.sourcing.postCancel')}}</span>
            </a><!-- 게시취소 -->
          </div>
        </div>
      </div>
    </div>
  </div>
  <teleport to="#common-modal">
    <ap-popup>
      <component
        :is="popupContent"
        :pop-params="popParams"
        @selectFunc="popSelectFunc"
      />
    </ap-popup>
  </teleport>
</template>

<script>
import {
  computed,
  reactive,
  defineAsyncComponent,
  provide,
  ref
} from 'vue'

import { useRoute, useRouter } from 'vue-router'
import { useSourcing } from '@/compositions/useSourcing'
import { useActions } from 'vuex-composition-helpers'
import { Field } from 'vee-validate'
import { useStore } from 'vuex'
import 'jodit/build/jodit.min.css'

export default {
  name: 'SourcingRegister',
  components: {
    UploadFile: defineAsyncComponent(() => import('@/components/comm/UploadFile.vue')),
    JoditEditor: defineAsyncComponent(() => import('@/components/comm/JoditEditor.vue')),
    ApPopup: defineAsyncComponent(() => import('@/components/comm/ApPopup.vue')),
    CompSearchPop: defineAsyncComponent(() => import('@/components/popup/CompSearchPop.vue')),
    Field
  },
  setup() {
    const route = useRoute()
    const router = useRouter()
    const tempEditor = ref(null)
    const targetKey = ref('1')
    const store = useStore()
    const myInfo = store.getters.getMyInfo()
    const langCd = store.getters.getLangCd()
    const seq = route.query.seq
    const { openAsyncAlert } = useActions(['openAsyncConfirm', 'openAsyncAlert'])

    const {
        t,
        params,
        errors,
        fnPost,
        popupContent,
        popParams,
        popSelectFunc,
        fnCngOpenAllYn,
        fetchSourcingView,
        fnSearchCompPop,
        fnTempSave,
        fnCancel
      } = useSourcing()

    // 등록/수정화면 여부
    const modifyYn = computed(() => {
      if (route.name === 'sourcing-modify') {
        return 'Y'
      }
      return 'N'
    })

    // 업로드 관련
    const uploadParams = reactive({
      targetKey: '',
      uploadCd: 'SOURCING01',
      items: [],
    })

    provide('upload-SOURCING01', uploadParams)


    if (modifyYn.value === 'Y') {
      const seq = route.query.seq
      targetKey.value = '' + seq
      uploadParams.targetKey = '' + seq

      fetchSourcingView(seq)
      .catch((err) => {
        console.log(err)
        openAsyncAlert(t('board.msg.sourcing.desc4')) // 페이지를 실행할 수 없습니다.
        router.back()
      })
    }

    return {
      t,
      seq,
      errors,
      params,
      fnPost,
      modifyYn,
      tempEditor,
      langCd,
      myInfo,
      uploadParams,
      targetKey,
      popupContent,
      popParams,
      popSelectFunc,
      fnSearchCompPop,
      fnTempSave,
      fnCngOpenAllYn,
      fnCancel
    }
  },
}
</script>

<style scoped>
.box-form {
  width: 1000px;
  margin: auto;
  text-align: left;
  padding: 20px 10px 10px 10px;
  border: 1px solid #d8d8d8;
  background-color: #ffffff;
}
.box-form > div {
  padding-top: 5px;
  padding-bottom: 5px;
}
</style>
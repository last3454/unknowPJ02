<template>
  <div class="inner">
    <div class="box">
      <div class="box-cont">
        <div class="cont-top cont-top2">
          <h2 class="cont-top-title">{{ t('board.label.errorinq.error_inq') }}</h2><!--오류문의 -->
        </div>
        <div class="cont-mid">
          <div class="cont-table">
            <form id="frm">
              <table class="tb typeB typeB__input__border">
                <caption class="hidden">{{ t('board.label.errorinq.error_inq') }}</caption><!--오류문의 -->
                <tbody>
                  <tr>
                    <th class="wf-200">{{ t('board.label.errorinq.title') }}<!-- 제목 --></th>
                    <td :class="(errors.title ? 'error' : '')">
                      <Field as="input" name="title" v-model="params.title" :placeholder="t('board.msg.sourcing.desc1')" />
                      <p class="error-msg">{{ errors.title }}</p>
                    </td>
                  </tr>
                  <tr>
                    <th class="wf-200">{{ t('board.label.errorinq.select_menu') }}</th><!-- 메뉴 선택 -->
                    <td class="b-inquiry-r-select">
                    <div class="cont-input-wrap__flex start">
                      <div class="select-wrap" :class ="errors.menuCtg1 ? 'error' : ''">
                        <Field as="select" name="menuCtg1" v-model="params.menuCtg1" id="b_inquiry_r_slt1" class="slt" @change="fnSetMenu2()">
                          <option value="">{{ t('board.label.errorinq.selectMenu1') }}</option><!-- 메뉴 1 선택 -->
                          <option
                            v-for="(m, i) in menu1List" :key="'errorMenu1_' + i"
                            :value="m.menuid"
                          >{{ m.menunm }}</option>
                        </Field>
                        <p class="error-msg">{{ errors.menuCtg1 }}</p>
                      </div>
                      <div class="select-wrap" :class ="errors.menuCtg2 ? 'error' : ''">
                        <Field as="select" name="menuCtg2" v-model="params.menuCtg2" id="b_inquiry_r_slt2" class="slt">
                          <option value="">{{ t('board.label.errorinq.selectMenu2') }}</option><!-- 메뉴 2 선택 -->
                          <option
                            v-for="(m, i) in menu2List" :key="'errorMenu2_' + i"
                            :value="m.menuid"
                          >{{ m.menunm }}</option>
                        </Field>
                        <p class="error-msg">{{ errors.menuCtg2 }}</p>
                      </div>
                    </div>
                  </td>
                </tr>
                  <tr>
                    <th class="wf-200">{{t('board.label.errorinq.content')}}<!-- 내용 --></th>
                    <td>
                      <jodit-editor v-model="params.content" :height="'400px'" :uploadCd="uploadParams.uploadCd"></jodit-editor>
                    </td>
                  </tr>
                  <tr>
                    <th class="wf-200">{{t('board.label.errorinq.att_file')}}<!-- 파일 첨부 --></th>
                    <td>
                      <upload-file
                        :upload-cd="'ERRORINQ01'"
                        server-del="Y"
                        file-btn-class="file-large-btn"
                      >
                      </upload-file>
                    </td>
                  </tr>
                </tbody>
              </table>

              <div class="cont-bot">
                <div class="btn-wrap center">
                    <a href="#" @click.prevent="fnCancel()" class="btn typeB gray">
                      <span>{{ t('common.label.confirm_cancel') }}<!-- 취소 --></span>
                    </a>
                    <a href="#" @click.prevent="fnSave()" class="btn typeB">
                      <span class="complete">{{ t('common.label.save') }}<!-- 저장 --></span>
                    </a>
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

import { computed, reactive, defineAsyncComponent, provide, ref, inject, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useActions } from 'vuex-composition-helpers'
import { useErrorInq } from '@/compositions/useErrorInq'
import { Field } from 'vee-validate'
import 'jodit/build/jodit.min.css'

// 밸리데이션 관련
import validation from '@/utils/validation'
import { useForm, useField } from 'vee-validate'
import * as yup from 'yup'

export default {
  name: 'ErrorInqReg',
  components: {
    UploadFile: defineAsyncComponent(() => import('@/components/comm/UploadFile.vue')),
    JoditEditor: defineAsyncComponent(() => import('@/components/comm/JoditEditor.vue')),
    Field
  },
  setup () {
    const t = inject('t')
    const route = useRoute()
    const router = useRouter()
    const tempEditor = ref(null)
    const targetKey = ref('1')
    const seq = route.query.seq
    const { openAsyncConfirm, openAsyncAlert, openAsyncPopup, closeAsyncPopup } = useActions(['openAsyncConfirm', 'openAsyncAlert', 'openAsyncPopup', 'closeAsyncPopup'])

    let isSaving = false

    const modifyYn = computed(() => {
      if (route.name === 'errorinq-modify') {
        return 'Y'
      }
      return 'N'
    })

    const {
      params,
      menu1List,
      menu2List,
      fetchBoard,
      fnSetMenu2,
      insertErrorInq,
      updateErrorInq
    } = useErrorInq()

    const uploadParams = reactive({
      targetKey: '',
      uploadCd: 'ERRORINQ01',
      items: []
    })

    provide('upload-ERRORINQ01', uploadParams)

    if (modifyYn.value === 'Y') {
      targetKey.value = '' + seq
      uploadParams.targetKey = '' + seq

      fetchBoard(seq)
        .catch(err => {
          console.log(err)
          alert('오류!')
          router.back()
        })
    }

    //////[S] Validation /////////////////////////////////////////////////////////////////

    validation.init()

    const schema = yup.object().shape({
      title: yup.string().required(),
      menuCtg1: yup.string().required(),
      menuCtg2: yup.string().required()
    })

    const { handleSubmit, errors } = useForm({
      validationSchema: schema
    })

    const useFieldSet = ['title', 'menuCtg1', 'menuCtg2']
    useFieldSet.forEach(str => {
      useField(str)
    })

    function onInvalidSubmit({ values, errors, results }) {
      //validation 체크 걸렸을때 들어오는 function
      console.log(values)
    }

    const fnAction = handleSubmit(async values => {
      //validation 체크가 성공 후 들어오는 부분
      const msg = t('common.msg.save_cofirm_msg')
      if (!await openAsyncConfirm({ message : msg })) {
        return
      } else {
        fnSaveErrorInq()
      }

    }, onInvalidSubmit)

    const fnSave = () => {
      if (isSaving) {
        return
      }

      fnAction()
    }

    const fnSaveErrorInq = () => {
      params.value.files = uploadParams.items

      isSaving = true

      if (modifyYn.value === 'Y') {
        updateErrorInq(params.value)
          .then(async res => {
            const resData = res.data
            if (resData.code === 'C0000') {
              await openAsyncAlert({ message: t('board.msg.errorinq.update_msg1') }) // 수정이 완료되었습니다
              router.push({ path: '/errorinq/view', query: { seq: params.value.seq } })
            } else {
              alert(resData.message)
            }
          })
          .finally( () => {
            isSaving = false
          })
      } else {
        insertErrorInq(params.value)
          .then(async res => {
            const resData = res.data
            if (resData.code === 'C0000') {
              await openAsyncAlert({ message: t('board.msg.errorinq.update_msg2') }) // 등록이 완료되었습니다
              router.push({ path: '/errorinq' })
            } else {
              alert(resData.message)
            }
          })
          .finally( () => {
            isSaving = false
          })
      }
    }

    const fnCancel = () => {
      if (modifyYn.value === 'Y') {
        router.push({ path: '/errorinq/view', query: { seq: params.value.seq } })
      } else {
        router.push({ path: '/errorinq/list' })
      }
    }

    return {
      t,
      seq,
      errors,
      params,
      menu1List,
      menu2List,
      tempEditor,
      fnSetMenu2,
      uploadParams,
      targetKey,
      fnSave,
      fnCancel
    }
  }
}
</script>
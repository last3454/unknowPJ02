<template>
  <div class="modal-content">
    <div class="modal-tit-wrap">
      <p class="modal-tit">{{ t('myinfo.label.chkPwd.title') }}</p><!-- 비밀번호 확인 -->
    </div>
    <div class="modal-cont-wrap">
      <div class="cont-table-top">
        <legend class="hidden">{{ t('myinfo.label.chkPwd.title') }}</legend><!-- 비밀번호 확인 -->
          <ul class="cont-input-list">
            <li class="cont-input-list-item">
              <dl>
                <dt class="cont-input-tit">{{ t('myinfo.msg.chkPwd.desc1') }}</dt><!-- 비밀번호를 입력하세요 -->
                <dd :class="(errors.pwd) ? 'cont-input-wrap error' : 'cont-input-wrap'">
                  <Field
                    as="input"
                    v-model="popParams.pwd"
                    name="pwd"
                    type="password"
                    maxlength="300"
                    ref="password"
                  />
                  <p class="error-msg">{{ errors.pwd }}</p>
                </dd>
              </dl>
            </li>
          </ul>
      </div>
      <div class="btn-wrap">
        <a
          href="#"
          @click.prevent="onCancel"
          class="btn typeB gray"
          ><span>{{ t('myinfo.label.chkPwd.cancel') }}</span> <!-- 취소 -->
        </a>
        <a
          href="#"
          @click.prevent="fnSend"
          class="btn typeB"
          ><span class="complete">{{ t('myinfo.label.chkPwd.submit') }}</span><!-- 확인 -->
        </a>
      </div>
    </div>
    <div class="modal-close-btn">
      <a
        href="#"
        @click.prevent="onCancel"
        title="팝업창 닫기"
        >{{ t('myinfo.label.chkPwd.close') }}<!-- 팝업창 닫기 -->
      </a>
    </div>
  </div>
</template>

<script>
import { reactive, inject } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { useActions } from 'vuex-composition-helpers'
import { Field, useField, useForm } from 'vee-validate'
import axios from '@/utils/customAxios'
import validation from '@/utils/validation'
import * as yup from 'yup';

export default {
  name: 'ChkPwdPop',
  props: {
    flag: String
  },
  components: {
    Field
  },
  setup(props, { emit }) {
    const t = inject('t')
    const router = useRouter()
    const commonUtils = inject('commonUtils')
    const { openAsyncAlert, closeAsyncPopup } = useActions(['openAsyncAlert', 'closeAsyncPopup'])

    const store = useStore()
    const myInfo = store.getters.getMyInfo()

    // 비밀번호 체크 관련
    let popParams = reactive({
      pwd: ''
    })

    // Validation
    validation.init()

    const schema = yup.object().shape({
      pwd: yup.string().required()
    })

    const { handleSubmit, errors } = useForm({
      validationSchema: schema
    })

    const useFieldSet = ['pwd']
    useFieldSet.forEach(str => {
      useField(str)
    })

    function onInvalidSubmit({ values, errors, results }) {
      console.log(values); // current form values
    }

    const fnAction = handleSubmit(async values => {
      const obj = {
        pwd: popParams.pwd,
        saveFlag: 'Y'
      }
      emit('callBack', obj)
    }, onInvalidSubmit)


    // 팝업 취소 버튼
    const onCancel = () => {
      closeAsyncPopup({ message: '취소' })
    }

    // 확인 버튼
    const fnSend = async () => {
      await fnAction()
    }

    // 비밀번호 체크 로직
    /*
    const fnCheckPwd = (popParams) => {
      return axios({
        url: '/api/myInfo/chkPwd',
        method: 'get',
        params: popParams
      })
      .then(async res => {
        const resData = res.data
        if (resData.data === true) {
          const path = props.flag === 'P' ? '/myInfop/modify' : '/myInfoc/modify'
          closeAsyncPopup()
          router.push({
            path: path
          })
        } else {
          await openAsyncAlert({ message: t('myinfo.msg.chkPwd.desc2') }) // 비밀번호를 확인하세요.
          popParams.pwd = ''
          return
        }
      })
    }
    */

    return {
      t,
      errors,
      popParams,
      fnSend,
      onCancel
    }
  },
}
</script>

<template>
  <div class="modal-content">
    <div class="modal-tit-wrap">
      <p class="modal-tit">{{ t('board.label.sourcing.sendEmail') }}</p><!-- 메일 발송 -->
    </div>
    <div class="modal-cont-wrap">
      <div class="cont-table-top">
        <legend class="hidden">{{ t('board.label.sourcing.sendEmail') }}</legend><!-- 메일 발송 -->
          <ul class="cont-input-list">
            <li class="cont-input-list-item">
              <dl>
                <dt class="cont-input-tit">{{ t('board.label.sourcing.toUserNm') }}</dt><!-- 받는 사람 -->
                <dd class="cont-input-wrap">
                    <input type="text" v-model="mailParams.toUserNm" disabled>
                </dd>
              </dl>
            </li>
            <li class="cont-input-list-item">
              <dl>
                <dt class="cont-input-tit">{{ t('board.label.sourcing.fromUserNm') }}</dt><!-- 보내는 사람 -->
                <dd :class="(errors.fromUserNm) ? 'cont-input-wrap error' : 'cont-input-wrap'">
                  <Field as="input" name="fromUserNm" v-model="mailParams.fromUserNm" :placeholder="t('board.msg.sourcing.desc15')" />
                  <p class="error-msg">{{ errors.fromUserNm }}</p>
                </dd>
              </dl>
            </li>
            <li class="cont-input-list-item">
              <dl>
                <dt class="cont-input-tit">{{ t('board.label.sourcing.fromEmail') }}</dt><!-- 보내는분 이메일 -->
                <dd :class="(errors.fromEmail) ? 'cont-input-wrap error' : 'cont-input-wrap'">
                  <Field as="input" name="fromEmail" v-model="mailParams.fromEmail" :placeholder="t('board.msg.sourcing.desc16')" />
                  <p class="error-msg">{{ errors.fromEmail }}</p>
                </dd>
              </dl>
            </li>
            <li class="cont-input-list-item">
              <dl>
                <dt class="cont-input-tit">{{ t('board.label.sourcing.mailCont') }}</dt><!-- 의견 내용 -->
                <dd :class="(errors.mailCont) ? 'cont-input-wrap error' : 'cont-input-wrap'">
                  <Field as="textarea" cols="30" rows="5" name="mailCont" v-model="mailParams.mailCont" maxlength="500" :placeholder="t('board.msg.sourcing.desc17')" />
                  <p class="error-msg">{{ errors.mailCont }}</p>
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
          ><span>{{ t('board.label.sourcing.cancel') }}</span><!-- 취소 -->
        </a>
        <a
          href="#"
          @click.prevent="fnSend"
          class="btn typeB"
          ><span class="complete">{{ t('board.label.sourcing.send') }}</span><!-- 제출 -->
        </a>
      </div>
    </div>
    <div class="modal-close-btn">
      <a
        href="#"
        @click="onCancel"
        title="팝업창 닫기"
        >{{ t('board.label.sourcing.closePop') }}<!-- 팝업창 닫기 -->
      </a>
    </div>
  </div>
</template>

<script>
import { reactive, inject } from 'vue'
import { useStore } from 'vuex'
import { useActions } from 'vuex-composition-helpers'
import { Field, useField, useForm } from 'vee-validate'
import axios from '@/utils/customAxios'
import validation from '@/utils/validation'
import * as yup from 'yup';

export default {
  name: 'RawSourcingInquiryPop',
  props: {
    seq: Number,
    title: String,
    toUserCd: String,
    toUserNm: String,
  },
  components: {
    Field
  },
  setup(props) {
    const t = inject('t')
    const store = useStore()
    const commonUtils = inject('commonUtils')
    const { openAsyncConfirm, openAsyncAlert, closeAsyncPopup } = useActions(['openAsyncConfirm', 'openAsyncAlert', 'closeAsyncPopup'])

    // 의견보내기 메일 관련
    const mailParams = reactive({
      // 순번
      seq: props.seq,
      // 받는 사람 (글 작성자)
      toUserCd: props.toUserCd,
      // 받는 사람 이름
      toUserNm: props.toUserNm,
      // 보내는 사람
      fromUserCd: '',
      // 보내는 사람 이름
      fromUserNm: '',
      // 보내는 사람 업체 코드
      fromCompCd: '',
      // 보내는 사람 이메일
      fromEmail: '',
      // 타이틀
      title: props.title,
      // 메일 내용
      mailCont: ''
    })

    // 보내는 사람 정보 세팅
    mailParams.fromUserCd = commonUtils.isNotEmpty(store.getters.getMyInfo().userCd) ? store.getters.getMyInfo().userCd : ''
    mailParams.fromUserNm = commonUtils.isNotEmpty(store.getters.getMyInfo().userNm) ? store.getters.getMyInfo().userNm : ''
    mailParams.fromCompCd = commonUtils.isNotEmpty(store.getters.getMyInfo().compCd) ? store.getters.getMyInfo().compCd : ''

    // Validation
    validation.init()

    const schema = yup.object().shape({
      fromEmail: yup.string().required().email(t('board.msg.sourcing.desc18')), // 올바른 형식의 메일 주소를 입력해주세요.
      fromUserNm: yup.string().required(),
      mailCont: yup.string().required()
    })

    const { handleSubmit, errors } = useForm({
      validationSchema: schema
    })

    const useFieldSet = ['fromUserNm', 'fromEmail', 'mailCont']
    useFieldSet.forEach(str => {
      useField(str)
    })

    function onInvalidSubmit({ values, errors, results }) {
      //validation 체크 걸렸을때 들어오는 function
      console.log(values); // current form values
    }

    const fnAction = handleSubmit(async values => {
      // 해당 의견을 제출하시겠습니까?
      if (!await openAsyncConfirm({ message : t('board.msg.sourcing.desc19') })) {
        return
      } else {
        sendEmail(mailParams).then(async res => {
          const resData = res.data
          if (resData.code === 'C0000') {
            await openAsyncAlert({ message: t('board.msg.sourcing.desc20') }) // 의견이 정상적으로 전송되었습니다.
            closeAsyncPopup()
          } else {
            alert(resData.message)
          }
        })
      }

    }, onInvalidSubmit)


    // 팝업 취소 버튼
    const onCancel = () => {
      closeAsyncPopup()
    }

    // 제출 버튼
    const fnSend = async () => {
      fnAction()
    }

    // 의견보내기 이메일 발송
    const sendEmail = (payload) => {
      return axios({
        url: '/api/sourcing/sendEmail',
        method: 'post',
        data: payload
      })
    }

    return {
      t,
      errors,
      mailParams,
      fnSend,
      onCancel
    }
  },
}
</script>

<template>
  <div class="modal-content">
    <div class="modal-tit-wrap">
      <p class="modal-tit">{{ t('ingredient.label.discontinued') }}</p>
    </div>
    <div class="modal-cont-wrap modal-cont-wrap2">
      <fieldset class="cont-input">
        <legend class="hidden">{{ t('ingredient.label.discontinued') }}</legend>
        <ul class="cont-input-list">
          <li class="cont-input-list-item">
            <dl>
              <dt class="cont-input-tit">{{ t('ingredient.msg.discontinued_reason_required') }}</dt>
              <dd class="cont-input-wrap" :class="chkValidation && errors.dsctdReason ? 'error' : ''">
                <Field as="input"
                  v-model="regParams.dsctdReason"
                  name="dsctdReason"
                  maxlength="300"
                  :placeholder="t('ingredient.msg.discontinued_reason_required')"
                >
                </Field>
                <p class="error-msg">{{ errors.dsctdReason }}</p>
              </dd>
            </dl>
          </li>
        </ul>
      </fieldset>
      <div class="btn-wrap">
        <a @click.prevent="fnClose()" class="btn typeB gray"><span>{{ t('common.label.confirm_cancel') }}</span></a>
        <a @click.prevent="fnSave()" class="btn typeB"><span>{{ t('common.label.register') }}</span></a>
      </div>
    </div>
    <div class="modal-close-btn">
      <a @click.prevent="fnClose()" :title="t('common.label.pop_close')">{{ t('common.label.pop_close') }}</a>
    </div>
  </div>
</template>

<script>
import { inject, reactive, ref } from 'vue'
import { useActions } from 'vuex-composition-helpers'
import { useIngredient } from '@/compositions/useIngredient'
import { useForm, useField, Field } from 'vee-validate'

import validation from '@/utils/validation'
import * as yup from 'yup';

export default {
  name: 'IngredientDiscontinuedPop',
  components: {
    Field
  },
  props: {
    parentInfo: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  setup (props, { emit }) {
    const t = inject('t')
    const commonUtils = inject('commonUtils')
    const { openAsyncConfirm, openAsyncAlert } = useActions(['openAsyncConfirm', 'openAsyncAlert'])
    const chkValidation = ref(false)
    const regParams = reactive({
      dsctdReason: '',
      recordCd: props.parentInfo.recordCd
    })

    const {
      fnUpdateDiscontinued
    } = useIngredient()

    const fnClose = () => {
      emit('callBack', 'N')
    }

    // [START validation]
    validation.init()

    const schema = yup.object().shape({
      dsctdReason: yup.string().required(),
    })

    const { handleSubmit, errors } = useForm({
      validationSchema: schema
    })

    const useFieldSet = ['dsctdReason']
    useFieldSet.forEach(str => {
      useField(str)
    })

    const fnHandelValidation = handleSubmit(async values => {
    })

    const fnValidation = async () => {
      if (commonUtils.isEmpty(regParams.recordCd)) {
        openAsyncAlert({ message: t('common.msg.no_essential_data') })
        return
      }
      chkValidation.value = true
      await fnHandelValidation()

      let isOk = true

      if (commonUtils.isNotEmpty(errors.value)) {
        isOk = false
      }

      return isOk
    }
    // [END validation]

    const fnSave = async () => {
      if (!await fnValidation()) {
        openAsyncAlert({ message: t('common.msg.check_essential_item') })
        return
      }

      if (!await openAsyncConfirm({ message: t('ingredient.msg.discontinued_confirm_msg')})) {
        return
      }
      
      const result = await fnUpdateDiscontinued(regParams)
      if (result) {
        emit('callBack', 'Y')
      }
    }

    return {
      t,
      errors,
      regParams,
      chkValidation,
      fnClose,
      fnSave
    }
  }
}
</script>
<template>
  <div class="modal-content">
    <div class="modal-tit-wrap">
      <p class="modal-tit">{{ t('ingredient.label.maker_nm') }} <span class="desc">{{ t('ingredient.label.ra_lab') }}</span></p>
    </div>
    <div class="modal-cont-wrap modal-cont-wrap2">
      <fieldset class="cont-input">
        <legend class="hidden">{{ t('ingredient.label.maker_nm') }}</legend>
        <ul class="cont-input-list">
          <li class="cont-input-list-item">
            <dl>
              <dt class="cont-input-tit">{{ t('ingredient.label.maker_nm') }}</dt>
              <dd class="cont-input-wrap">
                <div :class="chkValidation && (errors.manfNmRa || errors.manfCdRa) ? 'error' : ''">
                  <div class="cont-input-wrap__flex">
                    <Field as="input"
                      v-model="regParams.manfNmRa"
                      name="manfNmRa"
                      maxlength="100"
                      @keypress.enter="fnSearchMaker(regParams.manfNmRa)"
                    >
                    </Field>
                    <Field as="input" v-model="regParams.manfCdRa" name="manfCdRa" hidden></Field>
                    <a class="i-btn typeA large" @click.prevent="fnSearchMaker(regParams.manfNmRa)"><span>{{ t('common.label.search') }}</span></a>
                  </div>
                  <p class="error-msg">{{ errors.manfNmRa || errors.manfCdRa }}</p>
                </div>
                <div class="cont-input-scroll-area" :class="showArea ? '' : ' d_none'">
                  <ul class="cont-input-scroll-list">
                    <template v-if="searchMakerList && searchMakerList.length > 0">
                      <li class="cont-input-scroll-item" v-for="(vo, idx) in searchMakerList" :key="'maker_' + idx">
                        <p class="cont-input-scroll-tit">{{ vo.codeNmEn }} ({{ vo.codeNm }})</p>
                        <a @click.prevent="setMakerInfo(vo)">{{ t('common.label.select') }}</a>
                      </li>
                    </template>
                    <template v-else>
                      <li class="cont-input-scroll-item t-center">
                        {{ t('common.msg.no_data') }}
                      </li>
                    </template>
                  </ul>
                </div>
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
  </div>
</template>

<script>
import { inject, reactive, ref } from 'vue'
import { useIngredient } from '@/compositions/useIngredient'
import { useCode } from '@/compositions/useCode'
import { useActions } from 'vuex-composition-helpers'
import { useForm, useField, Field } from 'vee-validate'

import validation from '@/utils/validation'
import * as yup from 'yup';

export default {
  name: 'IngredientMakerRegisterPop',
  props: {
    parentInfo: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  components: {
    Field
  },
  setup (props, { emit }) {
    const t = inject('t')
    const searchMakerList = ref([])
    const makerList = ref([])
    const showArea = ref(false)
    const chkValidation = ref(false)
    const commonUtils = inject('commonUtils')
    const { openAsyncAlert, openAsyncConfirm } = useActions(['openAsyncAlert', 'openAsyncConfirm'])

    const regParams = reactive({
      recordCd: props.parentInfo.recordCd,
      manfCdRa: props.parentInfo.manfCdRa,
      manfNmRa: props.parentInfo.manfNmRa
    })

    const {
      fetchTiumCodeGroupMaps,
      codeGroupMaps
    } = useCode()

    const {
      fnUpdateMaker
    } = useIngredient()

    validation.init()

    const schema = yup.object().shape({
      manfCdRa: yup.string().required(t('common.msg.search_reg_required')),
      manfNmRa: yup.string().required()
    })

    const { handleSubmit, errors } = useForm({
      validationSchema: schema
    })

    const useFieldSet = ['manfCdRa', 'manfNmRa']
    useFieldSet.forEach(str => {
      useField(str)
    })

    const fnHandleValidation = handleSubmit(async values => {
    })

    const fnValidation = async () => {
      if (commonUtils.isEmpty(regParams.recordCd)) {
        openAsyncAlert({ message: t('common.msg.no_essential_data') })
        return
      }

      chkValidation.value = true
      await fnHandleValidation()

      let isOk = true

      if (commonUtils.isNotEmpty(errors.value)) {
        isOk = false
      }

      return isOk
    }

    const fnSearchMaker = async (keyword) => {
      if (!keyword) {
        openAsyncAlert({ message: t('common.msg.search_msg') })
        showArea.value = false
        return
      }

      if (!makerList.value || makerList.value.length === 0) {
        await fetchTiumCodeGroupMaps(['MI_TR_MAKER'])
        makerList.value = codeGroupMaps.value['MI_TR_MAKER']
      }

      if (makerList.value.length > 0) {
        searchMakerList.value = makerList.value.filter(
          vo => vo.buffer1 === 'GRP' &&
          (vo.codeNm.toUpperCase().indexOf(keyword.toUpperCase()) > -1 || vo.codeNmEn.toUpperCase().indexOf(keyword.toUpperCase()) > -1)
        )
      }

      showArea.value = true
    }

    const setMakerInfo = (vo) => {
      regParams.manfNmRa = vo.codeNmEn
      regParams.manfCdRa = vo.code
      showArea.value = false
    }

    const fnClose = () => {
      emit('callBack', 'N')
    }

    const fnSave = async () => {
      if (!await fnValidation()) {
        openAsyncAlert({ message: t('common.msg.check_essential_item') })
        return
      }

      if (!await openAsyncConfirm({ message: t('common.msg.save_cofirm_msg')})) {
        return
      }

      const result = await fnUpdateMaker(regParams)
      if (result) {
        emit('callBack', 'Y')
      }
    }

    return {
      t,
      regParams,
      showArea,
      fnSearchMaker,
      errors,
      setMakerInfo,
      searchMakerList,
      fnClose,
      fnSave
    }
  }
}
</script>


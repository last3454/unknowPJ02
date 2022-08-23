<template>
  <div class="modal-content">
    <div class="modal-tit-wrap">
      <p class="modal-tit">{{ t('ingredient.label.review_result') }}</p>
    </div>
    <div class="modal-cont-wrap modal-cont-wrap2">
      <fieldset class="cont-input">
        <legend class="hidden">{{ t('ingredient.label.review_result') }}</legend>
        <ul class="cont-input-list">
          <li class="cont-input-list-item">
            <dl>
              <dt class="cont-input-tit">{{ t('ingredient.msg.review_result_type_required') }}</dt>
              <dd class="cont-input-wrap">
                <div class="input-radio cont-input-wrap__flex center">
                  <div class="input-radio-indiv">
                    <input type="radio" id="reviewCd_1" v-model="regParams.reviewCd" name="reviewCd" value="NEW">
                    <label for="reviewCd_1">{{ t('ingredient.label.new_record') }}</label>
                  </div>
                  <div class="input-radio-indiv">
                    <input type="radio" id="reviewCd_2" v-model="regParams.reviewCd" name="reviewCd" value="UPD" :disabled="props.parentInfo.rawTypeCd === 'RT04'">
                    <label for="reviewCd_2">{{ t('ingredient.label.update_record') }}</label>
                  </div>
                </div>
              </dd>
            </dl>
          </li>
        </ul>
      </fieldset>
      <div class="btn-wrap">
        <a class="btn typeB gray" @click.prevent="fnClose()"><span>{{ t('common.label.confirm_cancel') }}</span></a>
        <a class="btn typeB" @click.prevent="fnSave()"><span>{{ t('common.label.confirm_ok') }}</span></a>
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

export default {
  name: 'IngredientReviewPop',
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
    const { openAsyncAlert } = useActions(['openAsyncAlert'])
    const regParams = reactive({
      reviewCd: '',
      recordCd: props.parentInfo.recordCd
    })

    const {
      fnReviewResultSave
    } = useIngredient()

    const fnClose = () => {
      emit('callBack', 'N')
    }

    const fnSave = async () => {
      if (commonUtils.isEmpty(regParams.reviewCd)) {
        openAsyncAlert({ message: t('ingredient.msg.review_type_required') })
        return
      }

      const result = await fnReviewResultSave(regParams)
      if (result) {
        emit('callBack', 'Y')
      }
    }

    if (props.parentInfo.rawTypeCd === 'RT04') {
      regParams.reviewCd = 'NEW'
    }

    return {
      t,
      props,
      regParams,
      fnClose,
      fnSave
    }
  }
}
</script>
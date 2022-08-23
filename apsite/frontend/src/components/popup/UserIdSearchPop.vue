<template>
  <div class="modal-wrap">
    <div class="modal-content">
      <div class="modal-tit-wrap">
        <p class="modal-tit">{{ t('join.label.id_search')}}</p>
      </div>
      <div class="modal-cont-wrap modal-cont-wrap2">
        <fieldset class="cont-input">
          <legend class="hidden">{{ t('join.label.id_search')}}</legend>
          <ul class="cont-input-list">
            <li class="cont-input-list-item">
              <dl>
                <dt class="cont-input-tit">{{ t('join.msg.desc9') }}</dt>
                <dd class="cont-input-wrap" :class="(errors.email ? 'error' : '')">
                  <Field as="input" name="email" v-model="email" :placeholder="t('myinfo.msg.profile.desc3')" />
                  <p class="error-msg">{{ errors.email }}</p>
                </dd>
              </dl>
            </li>
          </ul>
        </fieldset>
        <div class="btn-wrap">
          <a href="javascript:;" @click.prevent="fnAction()" class="btn typeB long"><span>{{ t('join.label.id_email') }}</span></a>
        </div>
      </div>
      <div class="modal-close-btn">
        <a href="javascript:;" @click.prevent="fnClose()" :title="t('common.label.pop_close')">{{ t('common.label.pop_close') }}</a>
      </div>        
    </div>
    <teleport to="#common-modal">
      <ap-alert ref="modal"></ap-alert>
    </teleport>    
  </div>
</template>

<script>

import validation from '@/utils/validation'
import { useLogin } from '@/compositions/useLogin'
import { useJoin } from '@/compositions/useJoin'

import { inject } from 'vue'
import { useForm, useField, Field } from 'vee-validate'
import { useActions } from 'vuex-composition-helpers'
import * as yup from 'yup';

export default {
  name:'UserIdSearchPop',
  components:{
    Field
  },
  setup() {
    const { openAsyncAlert, closeAsyncPopup } = useActions(['openAsyncAlert', 'closeAsyncPopup'])
    const t = inject('t')
    const {
      email,
      fetchSearchhId
    } = useLogin()

    const {
      fetchEmailCheck
    } = useJoin()    

    validation.init()

    const schema = yup.object().shape({
      email: yup.string().required().email(t('join.msg.desc6'))
    })
    
    const { handleSubmit, errors } = useForm({
      validationSchema: schema
    })
    
    const useFieldSet = ['email']
    useFieldSet.forEach(str => {
      useField(str)
    })

    const fnAction = handleSubmit(async values => {
      await fetchEmailCheck({email : email.value}).then(async (flag)=>{
        if(flag){
          await openAsyncAlert({ message: t('join.msg.desc10') })  
        }else{
          const flag = await fetchSearchhId({email : email.value})
          if(flag){
            await openAsyncAlert({ message: t('join.msg.desc11') })
          }
        }
      })
      closeAsyncPopup({ message: '', emitValue: '' })
    }, (values)=>{
      console.log(values)
    })

    const fnClose = () => {
      closeAsyncPopup({ message: '' })
    }    

    return{
      t,
      errors,
      email,
      fnClose,
      fnAction
    }
  },
}
</script>
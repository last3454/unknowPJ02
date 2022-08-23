<template>
<div class="box">
  <div class="box-cont">
    <div class="cont-top">
      <h2 class="cont-top-title">{{ t('company.label.company_reg') }}</h2>
      <p class="cont-top-desc">{{ t('company.msg.desc1') }}</p>
    </div>
    <div class="cont-mid">
      <div class="cont-form">
        <form id="frm">
          <fieldset class="cont-input cont-narrow">
            <legend class="hidden">{{ t('company.label.company_reg') }}</legend>
            <ul class="cont-input-list">
              <li class="cont-input-list-item">
                <dl>
                  <dt class="cont-input-tit">{{ t('company.msg.desc2') }}</dt>
                  <dd class="cont-input-wrap">
                    <div class="cont-input-wrap__flex">
                      <input type="text" name="keyword" v-model="checkParams.keyword" :placeholder="t('company.label.compnm_or_compno')" @keyup.enter="fnCheck()">
                      <a href="#" @click.prevent="fnCheck()" class="i-btn typeA large">{{ t('common.label.search2') }}</a>
                    </div>
                  </dd>
                </dl>
              </li><!-- 신규 업체 등록시에 아래 입력사항들 활성화 -->
              <li class="cont-input-list-item">
                <dl>
                  <dt class="cont-input-tit">{{ t('company.label.company_nm') }}<span class="desc">{{ t('common.label.korean') }}</span></dt>
                  <dd class="cont-input-wrap" :class="(errors.compNm ? 'error' : '')">
                    <Field as="input" name="compNm" v-model="params.compNm" :placeholder="t('company.msg.desc3')" @input="fnCompNm()" />
                    <p class="error-msg">{{ errors.compNm }}</p>
                  </dd>
                </dl>
              </li>
              <li class="cont-input-list-item">
                <dl>
                  <dt class="cont-input-tit">{{ t('company.label.company_nm') }}<span class="desc">{{ t('common.label.english') }}</span></dt>
                  <dd class="cont-input-wrap" :class="(errors.compEnm ? 'error' : '')">
                    <Field as="input" name="compEnm" v-model="params.compEnm" :placeholder="t('company.msg.desc4')" />
                    <p class="error-msg">{{ errors.compEnm }}</p>
                  </dd>
                </dl>
              </li>
              <li class="cont-input-list-item">
                <dl>
                  <dt class="cont-input-tit">{{ t('company.label.company_no') }}</dt>
                  <dd class="cont-input-wrap" :class="(errors.compNum || (params.compNum && !checkParams.isCompCheck) ? 'error' : '')">
                    <Field as="input" name="compNum" v-model="params.compNum" :placeholder="t('company.msg.desc5')"  maxlength="10" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');" @blur="fnCompNumChk()"/>
                    <p class="error-msg">{{ errors.compNum }}</p>
                    <p class="error-msg" v-show="params.compNum.length > 9 && !errors.compNum && params.compNum && !checkParams.isCompCheck">{{ t('company.msg.desc31') }}</p>
                  </dd>
                  <dd class="cont-input-wrap">
                    <upload-file
                      :upload-cd="'COMPANY01'"
                      :place-holder="t('company.label.company_no') + t('company.msg.desc34')"
                      @callBack="fnAfterFileUpload"                      
                      file-btn-class="file-large-btn"
                    />
                  </dd>                  
                </dl>
              </li>
              <li v-show="myInfo.compCd === 'AP'" class="cont-input-list-item">
                <dl>
                  <dt class="cont-input-tit">{{ t('company.label.company_code') }}</dt>
                  <dd class="cont-input-wrap">
                    <input type="text" name="compSapCd" v-model="params.compSapCd" :placeholder="t('company.msg.desc33')"/>
                  </dd>
                </dl>
              </li>
              <li class="cont-input-list-item">
                <dl>
                  <dt class="cont-input-tit">{{ t('company.label.company_represent') }}</dt>
                  <dd class="cont-input-wrap" :class="(errors.compRepresentNm ? 'error' : '')">
                    <Field as="input" type="tel" name="compRepresentNm" v-model="params.compRepresentNm" :placeholder="t('company.msg.desc32')"/>
                    <p class="error-msg">{{ errors.compRepresentNm }}</p>
                  </dd>
                </dl>
              </li>
              <li class="cont-input-list-item">
                <dl>
                  <dt class="cont-input-tit">{{ t('company.label.tel') }}</dt>
                  <dd class="cont-input-wrap" :class="(errors.compPhone ? 'error' : '')">
                    <Field as="input" type="tel" name="compPhone" v-model="params.compPhone" :placeholder="t('common.msg.only_number')" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');" maxlength="12"/>
                    <p class="error-msg">{{ errors.compPhone }}</p>
                  </dd>
                </dl>
              </li>
              <li class="cont-input-list-item">
                <dl>
                  <dt class="cont-input-tit">{{ t('common.label.website') }}</dt>
                  <dd class="cont-input-wrap" :class="(errors.compSite ? 'error' : '')">
                    <Field as="input" name="compSite" v-model="params.compSite" :placeholder="t('company.msg.desc6')" />
                    <p class="error-msg">{{ errors.compSite }}</p>
                  </dd>
                </dl>
              </li>
              <li class="cont-input-list-item">
                <dl>
                  <dt class="cont-input-tit">{{ t('common.label.postno') }}</dt>
                  <dd class="cont-input-wrap">
                    <div class="cont-input-wrap__ib" :class="(errors.compZip ? 'error' : '')">
                      <Field as="input" name="compZip" v-model="params.compZip" readonly />
                      <a href="javascript:;" @click.prevent="fnZipPop()" class="i-btn typeA large">{{ t('company.label.addr_search') }}</a>
                    </div>
                    <p class="error-msg">{{ errors.compZip }}</p>
                  </dd>
                </dl>
              </li>
              <li class="cont-input-list-item">
                <dl>
                  <dt class="cont-input-tit">{{ t('company.label.addr') }}<span class="desc">{{ t('common.label.korean') }}</span></dt>
                  <dd class="cont-input-wrap" :class="(errors.compAddr ? 'error' : '')">
                    <Field as="input" name="compAddr" v-model="params.compAddr" />
                    <p class="error-msg">{{ errors.compAddr }}</p>
                  </dd>
                </dl>
              </li>
              <li class="cont-input-list-item">
                <dl>
                  <dt class="cont-input-tit">{{ t('company.label.addr') }}<span class="desc">{{ t('common.label.english') }}</span></dt>
                  <dd class="cont-input-wrap" :class="(errors.compEnaddr ? 'error' : '')">
                    <Field as="input" name="compEnaddr" v-model="params.compEnaddr" />
                    <p class="error-msg">{{ errors.compEnaddr }}</p>
                  </dd>
                </dl>
              </li>
              <li class="cont-input-list-item">
                <dl>
                  <dt class="cont-input-tit">{{ t('company.label.reg_charger') }}</dt>
                  <dd class="cont-input-wrap" id="researcherBox">
                    <div class="cont-input-wrap__flex" :class="(errors.compApUserNm ? 'error' : '')">
                      <Field as="input" name="compApUserNm" v-model="params.compApUserNm" @keyup.enter="fnRsearch()" :placeholder="t('company.msg.desc7')" id="insertResearcherTxt"/>
                      <a href="#" @click.prevent="fnRsearch()" class="i-btn typeA large">{{ t('common.label.search2') }}</a>
                    </div>
                    <div class="cont-input-scroll-area" id="findResearcher">
                      <ul class="cont-input-scroll-list">
                        <li v-for="(vo,idx) in rSearchParam.list" :key="'li_comp_'+idx" class="cont-input-scroll-item">
                          <p class="cont-input-scroll-tit">{{ vo.userNm }}&nbsp;( {{ vo.deptNm }} )</p>
                          <a href="javascript:;" @click.prevent="fnRsearchSelect(vo.userCd, vo.userNm)">{{ t('common.label.select') }}</a>
                        </li>
                      </ul>
                    </div>                    
                    <p class="error-msg">{{ errors.compApUserNm }}</p>
                  </dd>
                </dl>
              </li>
              <li class="cont-input-list-item">
                <dl>
                  <dt class="cont-input-tit">{{ t('company.label.company_desc') }}</dt>
                  <dd class="cont-input-wrap" :class="(errors.compContent ? 'error' : '')">
                    <Field name="compContent" as="textarea" v-model="params.compContent" cols="30" rows="5" :placeholder="t('company.msg.desc8')"></Field>
                    <p class="error-msg">{{ errors.compContent }}</p>
                  </dd>
                </dl>
              </li>
            </ul>
          </fieldset>
        </form>
      </div>
    </div>  
    <div class="cont-bot">
      <div class="btn-wrap center">
        <a href="javascript:;" @click.prevent="fnCancel()" class="btn typeB gray"><span>{{ t('common.label.cancel') }}</span></a>
        <a href="javascript:;" @click.prevent="fnSave(params)" class="btn typeB">
          <template v-if="params.compSeq">
            <span class="mod">{{ t('company.label.company_mod') }}</span>
          </template>
          <template v-else>
            <span class="reg">{{ t('company.label.company_reg') }}</span>
          </template>            
        </a>
      </div>
    </div>      
  </div>
</div>
</template>

<script>
import validation from '@/utils/validation'
import { useCompany } from '@/compositions/useCompany'

import { inject, reactive, defineAsyncComponent, provide } from 'vue'
import { useRoute, useRouter  } from 'vue-router'
import { useStore } from 'vuex'
import { loadScript } from "vue-plugin-load-script";
import { useActions } from 'vuex-composition-helpers'
import { useForm, useField, Field } from 'vee-validate'
import * as yup from 'yup';
export default {
  name: 'CompanyRegister',
  components:{
    Field,
    UploadFile: defineAsyncComponent(() => import('@/components/comm/UploadFile.vue'))
  },
  setup (){
    loadScript("//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js")
    .then(() => {
    })
    .catch(() => {
      // Failed to fetch script
    })

    const t = inject('t')
    const route = useRoute()
    const router = useRouter()
    const store = useStore()
    const path  = router.currentRoute.value.path
    const myInfo = store.getters.getMyInfo()
    const mFlag = (path.toLowerCase().indexOf('companym') > -1)    
    const seq = route.query.seq
    const { openAsyncConfirm, openAsyncAlert } = useActions(['openAsyncConfirm', 'openAsyncAlert'])

    const checkParams = reactive({
      keyword : '',
      isCheck : false,
      isCompCheck : true,
      isFileChek : false
    })

    const {
      params,
      fnZipPop,
      fnLinkView,
      fnLinkList,
      fnManagerLinkView,      
      fnManagerLinkList,
      fetchCompany,
      fetchUpdateCompany,
      fetchSaveCompany,
      fetchCompCheck,
      fetchResearch
    } = useCompany()

    const rSearchParam = reactive({
      list:[]
    })    

    //[S] 업로드 관련
    const uploadParams = reactive({
      targetKey: '',
      uploadCd: 'COMPANY01',
      items: []
    })    

    provide('upload-COMPANY01', uploadParams)

    const fnAfterFileUpload = (returnObj) =>{
      const uploadList = returnObj.items
      if (!uploadList || uploadList.length === 0) {
        checkParams.isFileChek = false
      } else {
        checkParams.isFileChek = true
      }
      params.value.files = uploadList
    }
    //[E] 업로드 관련

    //정보 조회
    if(seq){
      //수정일때는 체크 안하게 추가
      checkParams.isCheck = true
      checkParams.isCompCheck = true
      checkParams.isFileChek = true
      uploadParams.targetKey = seq
      fetchCompany(seq)
    }

    //[S]validation 체크 추가
    validation.init()

    const schema = yup.object().shape({
      compNm: yup.string().required(),
      compEnm: yup.string().required(),
      compNum: yup.string().required(),
      compRepresentNm: yup.string().required(),
      compPhone: yup.string().required(),
      compSite: yup.string().required().url(t('company.msg.desc15')),
      compZip: yup.string().required(),
      compAddr: yup.string().required(),
      compEnaddr: yup.string().required(),
      compApUserNm: yup.string().required(),
      compContent: yup.string().required().max(500, t('company.msg.desc8'))
    })
    //blur은 기본
    const { handleSubmit, errors } = useForm({
      validationSchema: schema
    })
    //선언시 keyup으로 작동. keyup으로 안할꺼면 선언안해도 될듯
    const useFieldSet = ['compNm','compEnm','compNum', 'compRepresentNm', 'compPhone','compSite','compZip','compAddr','compEnaddr','compApUserNm','compContent']
    useFieldSet.forEach(str => {
      useField(str)
    })
    function onInvalidSubmit({ values, errors, results }) {
      //validation 체크 걸렸을때 들어오는 function
      console.log(values); // current form values
      //console.log(errors); // a map of field names and their first error message
      //console.log(results); // a detailed map of field names and their validation results
    }
    //[E]validation 체크 추가

    const fnCheck = async () => {
      await fetchCompCheck(checkParams).then((res)=>{
        if (res) {
          checkParams.isCheck = true
        }
      })
    }

    const fnList = () => {
      if(mFlag) {
        fnManagerLinkList()
      }else{
        fnLinkList()
      }
    }

    const fnView = (seq) => {
      if(mFlag) {
        fnManagerLinkView(seq)
      }else{
        fnLinkView(seq)
      }
    }

    const fnAction = handleSubmit(async values => {      
      if(!checkParams.isCompCheck){
        await openAsyncAlert({ message: t('company.msg.desc31') })
        return
      }
      if(!checkParams.isFileChek){
        await openAsyncAlert({ message: t('company.msg.desc35') })
        return
      }      
      //validation 체크가 성공 후 들어오는 부분
      const msg = (params.value.compSeq ? t('common.label.modify') : t('common.label.register'))
      const message = t('company.msg.desc9', { msg })
      if (!await openAsyncConfirm({ message })) {
        return
      }

      if(params.value.compSeq){        
        fetchUpdateCompany(params.value).then((seq)=>{
          fnView(seq)
        })
      }else{
        fetchSaveCompany(params.value).then((rvo)=>{
          fnView(rvo.data.compSeq)
        })
      }  
    }, onInvalidSubmit);    
    
    const fnSave = async () => {
      if(!checkParams.isCheck){
        await openAsyncAlert({ message: t('company.msg.desc10') })
        return
      }    
      fnAction()
    }

    const fnCancel = async () => {
      if (!await openAsyncConfirm({ message: t('company.msg.desc14') })) {
        return
      }      
      fnList()
    }

    const fnCompNm = () => {
      if(checkParams.keyword !== params.value.compNm){
        checkParams.isCheck = false
      }
    }

    const fnRsearch = () => {
      fetchResearch(params.value.compApUserNm).then((res)=>{
        rSearchParam.list = res.data
      })
    }

    const fnCompNumChk = async () => {
      if(!params.value.compNum){
        await openAsyncAlert({ message: t('company.msg.desc5') }) // 사업자 등록번호를 입력해주세요.
        return
      }      
      const compNumParams = {
        keyword : params.value.compNum,
        type : 'compNum',
        noAlertFlag : true
      }
      fetchCompCheck(compNumParams).then((res)=>{
        checkParams.isCompCheck = res
      })
    }

    const fnRsearchSelect = (userId, userNm) => {
      rSearchParam.userNm = userNm
      rSearchParam.list =[]
      params.value.compApUserId = userId
      params.value.compApUserNm = userNm
    }

    return {
      errors,
      myInfo,
      checkParams,
      rSearchParam,
      uploadParams,
      params,
      fnCompNm,
      fnZipPop,
      fnCancel,
      fnCheck,
      fnSave,
      fnRsearch,
      fnCompNumChk,
      fnRsearchSelect,
      fnAfterFileUpload,
      t
    }
  }
}

</script>
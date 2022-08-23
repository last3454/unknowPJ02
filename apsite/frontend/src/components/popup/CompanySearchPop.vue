<template>
  <div class="modal-wrap">
    <div class="modal-content">
      <div class="modal-tit-wrap">
          <p class="modal-tit">{{t('company.label.title')}}</p>
      </div>
      <div class="modal-cont-wrap modal-cont-wrap2">
        <fieldset class="cont-input">
          <legend class="hidden">{{t('company.label.title')}}</legend>
          <ul class="cont-input-list">
            <li class="cont-input-list-item">
              <dl>
                <dt class="cont-input-tit">{{t('company.msg.desc0')}}</dt>
                <dd class="cont-input-wrap">
                  <div class="cont-input-wrap__flex">
                    <input type="text" v-model="cSearchParam.keyword" @keyup.enter="fnSearch()" :placeholder="t('company.label.company_nm_num')" id="insertCompanyTxt">
                    <a href="javascript:;" @click.prevent="fnSearch()" class="i-btn typeA large">{{t('common.label.search2')}}</a>
                  </div>
                  <div class="cont-input-scroll-area" id="findCompany" :show="cSearchParam.isSubShow">
                    <ul class="cont-input-scroll-list">
                      <li v-for="(vo,idx) in list" :key="'li_comp_'+idx" class="cont-input-scroll-item">
                        <p class="cont-input-scroll-tit">
                          {{ vo.compNm }}
                          <span v-if="vo.compStatusCd === '00'" style="color:red;">
                            &nbsp; {{t('company.label.company_wait')}}
                          </span>
                         </p>
                        <p class="cont-input-scroll-txt">{{ commonUtils.phoneFormat(vo.compPhone) }}</p>
                        <a href="javascript:;" @click.prevent="fnCompSelect(vo.compSeq, vo.compStatusCd)">{{t('common.label.select')}}</a>
                      </li>
                    </ul>
                  </div>
                  <p style="font-size:0.875rem;color:#FF3636;" v-show="!cSearchParam.isReadOnly">{{t('company.msg.desc20')}}</p>
                </dd>
                <dd class="cont-input-wrap" :class="(errors.compNm ? 'error' : '')">
                  <Field as="input" name="compNm" v-model="params.compNm" :placeholder="t('company.label.company_name')" :readonly="cSearchParam.isReadOnly" />
                  <p class="error-msg">{{ errors.compNm }}</p>
                </dd>
                <dd class="cont-input-wrap" :class="(errors.compEnm ? 'error' : '')">
                  <Field as="input" name="compEnm" v-model="params.compEnm" :placeholder="t('company.label.company_en_name')" :readonly="cSearchParam.isReadOnly" />
                  <p class="error-msg">{{ errors.compEnm }}</p>
                </dd>
                <dd class="cont-input-wrap" :class="(errors.compNum || (params.compNum && !cSearchParam.isCompCheck) ? 'error' : '')">
                  <Field as="input" name="compNum" v-model="params.compNum" :placeholder="t('company.msg.desc5')" maxlength="10" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');" :readonly="cSearchParam.isReadOnly" @blur="fnCompNumChk()"/>
                  <p class="error-msg">{{ errors.compNum }}</p>
                  <p class="error-msg" v-show="params.compNum.length > 9 && !errors.compNum && params.compNum && !cSearchParam.isCompCheck">{{ t('company.msg.desc31') }}</p>
                </dd>
                <dd v-if="!cSearchParam.isReadOnly" class="cont-input-wrap">
                  <upload-file
                    :upload-cd="'COMPANY01'"
                    :place-holder="t('company.label.company_no') + t('company.msg.desc34')"
                    @callBack="fnAfterFileUpload"                      
                    file-btn-class="file-large-btn"
                  />
                </dd>                   
                <dd class="cont-input-wrap">
                  <div class="cont-input-wrap__flex" :class="(errors.compZip ? 'error' : '')">
                    <Field as="input" name="compZip" v-model="params.compZip" readonly />
                    <a href="javascript:;" @click.prevent="fnZipPop()" class="i-btn typeA large">{{t('company.label.addr_search')}}</a>
                  </div>
                  <p class="error-msg">{{ errors.compZip }}</p>
                </dd>
                <dd class="cont-input-wrap" :class="(errors.compAddr ? 'error' : '')">
                  <Field as="input" name="compAddr" v-model="params.compAddr" :placeholder="t('company.label.company_adrr')" :readonly="cSearchParam.isReadOnly" />
                  <p class="error-msg">{{ errors.compAddr }}</p>
                </dd>
                <dd class="cont-input-wrap" :class="(errors.compEnaddr ? 'error' : '')">
                  <Field as="input" name="compEnaddr" v-model="params.compEnaddr" :placeholder="t('company.label.company_en_adrr')" :readonly="cSearchParam.isReadOnly" />
                  <p class="error-msg">{{ errors.compEnaddr }}</p>
                </dd>
                <dd class="cont-input-wrap" :class="(errors.compRepresentNm ? 'error' : '')">
                  <Field as="input" name="compRepresentNm" v-model="params.compRepresentNm" :placeholder="t('company.label.company_represent')" :readonly="cSearchParam.isReadOnly"/>
                  <p class="error-msg">{{ errors.compRepresentNm }}</p>
                </dd>
                <dd class="cont-input-wrap" :class="(errors.compPhone ? 'error' : '')">
                  <Field as="input" type="tel" name="compPhone" v-model="params.compPhone" :placeholder="t('company.label.company_phone')" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');"  :readonly="cSearchParam.isReadOnly"/>
                  <p class="error-msg">{{ errors.compPhone }}</p>
                </dd>
              </dl>
            </li>
            <li v-if="rSearchParam.isShow" class="cont-input-list-item">
              <dl>
                <dt class="cont-input-tit hidden">{{t('company.label.reg_charger')}}</dt>
                <dd class="cont-input-wrap">
                  <div class="cont-input-wrap__border cont-input-wrap__flex signup-input-researcher">
                    <p>{{t('company.msg.desc21')}}</p>
                    <div class="input-radio">
                      <div class="input-radio-indiv">
                        <input type="radio" v-model="rSearchParam.konwFlag" id="su_i_researcher_Y" name="su_i_researcher" value="Y">
                        <label for="su_i_researcher_Y">{{t('company.label.yes')}}</label>
                      </div>
                      <div class="input-radio-indiv">
                        <input type="radio" v-model="rSearchParam.konwFlag" id="su_i_researcher_N" name="su_i_researcher" value="N">
                        <label for="su_i_researcher_N">{{t('company.label.no')}}</label>
                      </div>
                    </div>
                  </div>
                </dd>
                <dd v-if="rSearchParam.konwFlag === 'Y'" class="cont-input-wrap" id="researcherBox">
                  <div class="cont-input-wrap__flex">
                    <input type="text" v-model="rSearchParam.userNm" :placeholder="t('company.label.reg_charger_nm')" id="insertResearcherTxt" @keyup.enter="fnRsearch()">
                    <a href="javascript:;" @click.prevent="fnRsearch()" class="i-btn typeA large">{{t('common.label.search2')}}</a>
                  </div>
                  <div class="cont-input-scroll-area" id="findResearcher">
                    <ul class="cont-input-scroll-list">
                      <li v-for="(vo,idx) in rSearchParam.list" :key="'li_comp_'+idx" class="cont-input-scroll-item">
                        <p class="cont-input-scroll-tit">{{ vo.userNm }}&nbsp;( {{ vo.deptNm }} )</p>
                        <a href="javascript:;" @click.prevent="fnRsearchSelect(vo.userCd, vo.userNm)">{{t('common.label.select')}}</a>
                      </li>
                    </ul>
                  </div>
                  <p class="error-msg">{{t('company.msg.desc22')}}</p>
                </dd>
              </dl>
            </li>
            <li class="cont-input-list-item">
              <dl>
                <dt class="cont-input-tit hidden">{{t('company.label.company_etc_info')}}</dt>
                <dd class="cont-input-wrap" :class="(errors.compSite ? 'error' : '')">
                  <Field as="input" name="compSite" v-model="params.compSite" :placeholder="t('company.msg.desc6')" :readonly="cSearchParam.isReadOnly" />
                  <p class="error-msg">{{ errors.compSite }}</p>
                </dd>
                <dd class="cont-input-wrap" :class="(errors.compContent ? 'error' : '')">
                  <Field name="compContent" as="textarea" v-model="params.compContent" cols="30" rows="5" :placeholder="t('company.label.company_desc')" :readonly="cSearchParam.isReadOnly" />
                  <p class="error-msg">{{ errors.compContent }}</p>
                </dd>
              </dl>
            </li>
          </ul>
        </fieldset>
        <div class="btn-wrap">
            <a href="javascript:;" @click.prevent="fnSelect()" class="btn typeB long"><span>{{t('company.label.company_choose')}}</span></a>
        </div>
      </div>
      <div class="modal-close-btn">
        <a href="javascript:;" @click.prevent="fnClose()" :title="t('common.label.pop_close')">{{t('common.label.pop_close')}}</a>
      </div>
    </div>
  </div>
</template>

<script>
import validation from '@/utils/validation'
import commonUtils from '@/utils/commonUtils'
import { useCompany } from '@/compositions/useCompany'

import { provide, inject, reactive, defineAsyncComponent } from 'vue'
import { loadScript } from "vue-plugin-load-script";
import { useForm, useField, Field } from 'vee-validate'
import { useActions } from 'vuex-composition-helpers'
import * as yup from 'yup';

export default {
  name: 'CompanySearchPop',
  components:{
    Field,
    UploadFile: defineAsyncComponent(() => import('@/components/comm/UploadFile.vue'))
  },
  setup() {
    loadScript("//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js")
    .then(() => {
    })
    .catch(() => {
      // Failed to fetch script
    })

    const { openAsyncAlert, closeAsyncPopup } = useActions(['openAsyncAlert', 'closeAsyncPopup'])
    const t = inject('t')
    const {
      list,
      params,
      fnZipPop,
      fnParamsInit,
      fetchCompany,
      fetchCompanys,
      fetchCompCheck,
      fetchResearch
    } = useCompany()    
    
    const cSearchParam = reactive({
      type:'compNm',
      keyword: '',
      nowPageNo: 1,
      isReadOnly: true,
      isSubShow: false,
      isCheck : false,
      isCompCheck : true,
      isFileChek : false
    })

    const rSearchParam = reactive({
      isShow: true,
      konwFlag:'N',
      userNm: '',
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
        cSearchParam.isFileChek = false
      } else {
        cSearchParam.isFileChek = true
      }
      params.value.files = uploadList
    }
    //[E] 업로드 관련    

    validation.init()

    const schema = yup.object().shape({
      compNm: yup.string().required(t('company.msg.desc23')),
      compEnm: yup.string().required(t('company.msg.desc24')),
      compNum: yup.string().required(),
      compRepresentNm: yup.string().required(),
      compPhone: yup.string().required(),
      compSite: yup.string().required().url(t('company.msg.desc15')),
      compZip: yup.string().required(t('company.msg.desc25')),
      compAddr: yup.string().required(),
      compEnaddr: yup.string().required(),
      compContent: yup.string().required().max(500, t('company.msg.desc8'))      
    })

    const { handleSubmit, errors } = useForm({
      validationSchema: schema
    })
    const useFieldSet = ['compNm','compEnm','compNum','compRepresentNm', 'compPhone','compSite','compZip','compAddr','compEnaddr','compContent']
    useFieldSet.forEach(str => {
      useField(str)
    })

    const fnAction = handleSubmit(async values => {
      if(!cSearchParam.isCompCheck){
        await openAsyncAlert({ message: t('company.msg.desc31') })
        return
      }

      if(!cSearchParam.isCheck && !cSearchParam.isFileChek){
        await openAsyncAlert({ message: t('company.msg.desc35') })
        return
      }      
      const masterFlag = cSearchParam.isSubShow ? 'N' : 'Y'
      params.value.masterFlag = masterFlag
      closeAsyncPopup({ message: t('common.label.select'), emitValue: params.value })
    }, (values)=>{
      console.log(values)
    })

    const fnSelect = async () => {
      fnAction()
    }

    const fnSearch = async () => {
      if(!cSearchParam.keyword){
        await openAsyncAlert({ message: t('company.msg.desc11') })
        return
      }

      fetchCompanys(cSearchParam).then((flag)=>{
        cSearchParam.isSubShow = flag
        cSearchParam.isReadOnly = flag
        cSearchParam.isCheck = flag
        //조회해서 업체가 없을 시 마스터로 등록
        //연구원 담당자 선택 할 수 있는 화면 노출
        rSearchParam.isShow = !flag
        rSearchParam.konwFlag = 'N'
        rSearchParam.userNm = ''
        rSearchParam.list = []
        
        if(!flag){
          fnParamsInit()
        }
      })
    }

    const fnRsearch = () => {
      fetchResearch(rSearchParam.userNm).then((res)=>{
        rSearchParam.list = res.data
      })
    }

    const fnRsearchSelect = (userId, userNm) => {
      rSearchParam.userNm = userNm
      rSearchParam.list =[]
      params.value.compApUserId = userId
      params.value.compApUserNm = userNm
    }

    const fnCompSelect =(compSeq, compStatusCd) => {
      if(compStatusCd === '00'){
        openAsyncAlert({ message: t('company.msg.desc16') })
        return
      }
      fetchCompany(compSeq)
    }

    const fnClose = () => {
      closeAsyncPopup({ message: t('common.label.pop_close') })
    }

    const fnCompNumChk = async () => {
      //readonly 상태이면 굳이 작동하지 않아도 되는 기능
      if(cSearchParam.isReadOnly){
        return
      }

      if(!params.value.compNum){
        await openAsyncAlert({ message: t('company.msg.desc5') }) // 사업자 등록번호를 입력해주세요.
        return
      }      
      const compParams = {
        keyword : params.value.compNum,
        type : 'compNum',
        noAlertFlag : true
      }
      fetchCompCheck(compParams).then((res)=>{
        cSearchParam.isCompCheck = res
      })
    }

    return {
      t,
      errors,
      list,
      params,
      fnZipPop,
      commonUtils,
      rSearchParam,
      cSearchParam,
      fnClose,
      fnSelect,
      fnSearch,
      fnRsearch,
      fnCompSelect,
      fnCompNumChk,
      fnRsearchSelect,
      fnAfterFileUpload
    }
  }
}
</script>
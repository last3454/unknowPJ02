<template>
  <div class="inner">
    <div class="box">
      <div class="box-cont">
        <div class="cont-top">
          <h2 class="cont-top-title">{{ t('myinfo.label.company.title2') }}</h2><!-- 소속회사정보 수정 -->
        </div>
        <div class="cont-mid">
          <div class="cont-form">
            <fieldset class="cont-input cont-narrow">
              <legend class="hidden">{{ t('myinfo.label.company.title2') }}</legend><!-- 소속회사정보 수정 -->
              <ul class="cont-input-list">
                <li class="cont-input-list-item">
                  <dl>
                    <dt class="cont-input-tit">{{ t('company.label.company_nm') }}<span class="desc">{{ t('common.label.korean') }}</span></dt><!-- 회사명 국문 -->
                    <dd class="cont-input-wrap" :class="(errors.compNm ? 'error' : '')">
                      <Field as="input" name="compNm" v-model="companyInfo.compNm" :placeholder="t('company.msg.desc3')" @input="fnCompNm()" />
                      <p class="error-msg">{{ errors.compNm }}</p>
                    </dd>
                  </dl>
                </li>
                <li class="cont-input-list-item">
                  <dl>
                    <dt class="cont-input-tit">{{ t('company.label.company_nm') }}<span class="desc">{{ t('common.label.english') }}</span></dt><!-- 회사명 영문 -->
                    <dd class="cont-input-wrap" :class="(errors.compEnm ? 'error' : '')">
                      <Field as="input" name="compEnm" v-model="companyInfo.compEnm" :placeholder="t('company.msg.desc4')" />
                      <p class="error-msg">{{ errors.compEnm }}</p>
                    </dd>
                  </dl>
                </li>
                <li class="cont-input-list-item">
                  <dl>
                    <dt class="cont-input-tit">{{ t('myinfo.label.company.compNo') }}</dt><!-- 사업자등록번호 -->
                    <dd class="cont-input-wrap" :class="(errors.compNum || (companyInfo.compNum && !checkParams.isCompCheck) ? 'error' : '')">
                      <Field as="input" name="compNum" v-model="companyInfo.compNum" :placeholder="t('company.msg.desc5')" maxlength="10" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');" @blur="fnCompNumChk()"/>
                      <p class="error-msg">{{ errors.compNum }}</p>
                      <p class="error-msg" v-show="!errors.compNum && companyInfo.compNum && !checkParams.isCompCheck">{{ t('company.msg.desc31') }}</p>
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
                <li class="cont-input-list-item">
                  <dl>
                    <dt class="cont-input-tit">{{ t('myinfo.label.company.compTel') }}</dt><!-- 대표전화 -->
                    <dd class="cont-input-wrap" :class="(errors.compPhone ? 'error' : '')">
                      <Field as="input" type="tel" name="compPhone" v-model="companyInfo.compPhone" :placeholder="t('common.msg.only_number')" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');" />
                      <p class="error-msg">{{ errors.compPhone }}</p>
                    </dd>
                  </dl>
                </li>
                <li class="cont-input-list-item">
                  <dl>
                    <dt class="cont-input-tit">{{ t('myinfo.label.company.compRepresentNm') }}</dt><!-- 대표자명 -->
                    <dd class="cont-input-wrap" :class="(errors.compRepresentNm ? 'error' : '')">
                      <Field as="input" name="compRepresentNm" v-model="companyInfo.compRepresentNm" :placeholder="t('company.msg.desc32')" />
                      <p class="error-msg">{{ errors.compRepresentNm }}</p>
                    </dd>
                  </dl>
                </li>
                <li class="cont-input-list-item">
                  <dl>
                    <dt class="cont-input-tit">{{ t('myinfo.label.company.compUrl') }}</dt><!-- 웹사이트 -->
                    <dd class="cont-input-wrap" :class="(errors.compSite ? 'error' : '')">
                      <Field as="input" name="compSite" v-model="companyInfo.compSite" :placeholder="t('company.msg.desc6')" />
                      <p class="error-msg">{{ errors.compSite }}</p>
                    </dd>
                  </dl>
                </li>
                <li class="cont-input-list-item">
                  <dl>
                    <dt class="cont-input-tit">{{ t('myinfo.label.company.compPostNo') }}</dt><!-- 우편번호 -->
                    <dd class="cont-input-wrap">
                      <div class="cont-input-wrap__ib" :class="(errors.compZip ? 'error' : '')">
                        <Field as="input" name="compZip" v-model="companyInfo.compZip" readonly />
                        <a href="#" @click.prevent="fnZipPop()" class="i-btn typeA large">{{ t('company.label.addr_search') }}</a>
                      </div>
                      <p class="error-msg">{{ errors.compZip }}</p>
                    </dd>
                  </dl>
                </li>
                <li class="cont-input-list-item">
                  <dl>
                    <dt class="cont-input-tit">{{ t('company.label.addr') }}<span class="desc">{{ t('common.label.korean') }}</span></dt>
                    <dd class="cont-input-wrap" :class="(errors.compAddr ? 'error' : '')">
                      <Field as="input" name="compAddr" v-model="companyInfo.compAddr" />
                      <p class="error-msg">{{ errors.compAddr }}</p>
                    </dd>
                  </dl>
                </li>
                <li class="cont-input-list-item">
                  <dl>
                    <dt class="cont-input-tit">{{ t('company.label.addr') }}<span class="desc">{{ t('common.label.english') }}</span></dt>
                    <dd class="cont-input-wrap" :class="(errors.compEnaddr ? 'error' : '')">
                      <Field as="input" name="compEnaddr" v-model="companyInfo.compEnaddr" />
                      <p class="error-msg">{{ errors.compEnaddr }}</p>
                    </dd>
                  </dl>
                </li>
                <li class="cont-input-list-item">
                  <dl>
                    <dt class="cont-input-tit">{{ t('myinfo.label.company.reg_charger') }}</dt><!-- 담당 연구원 -->
                    <dd class="cont-input-wrap" id="researcherBox">
                      <div class="cont-input-wrap__flex" :class="(errors.compApUserNm ? 'error' : '')">
                        <Field as="input" name="compApUserNm" v-model="companyInfo.compApUserNm" :placeholder="t('company.msg.desc7')" id="insertResearcherTxt"/>
                        <a href="#" @click.prevent="fnRsearch()" class="i-btn typeA large">{{ t('common.label.search2') }}</a><!-- 조회 -->
                      </div>
                      <div class="cont-input-scroll-area" id="findResearcher">
                        <ul class="cont-input-scroll-list">
                          <li v-for="(vo,idx) in rSearchParam.list" :key="'li_comp_'+idx" class="cont-input-scroll-item">
                            <p class="cont-input-scroll-tit">{{ vo.userNm }}&nbsp;( {{ vo.deptNm }} )</p>
                            <a href="#" @click.prevent="fnRsearchSelect(vo.userCd, vo.userNm)">{{ t('board.label.sourcing.select') }}</a><!-- 선택 -->
                          </li>
                        </ul>
                      </div>
                      <p class="error-msg">{{ errors.compApUserNm }}</p>
                    </dd>
                  </dl>
                </li>
                <li class="cont-input-list-item">
                  <dl>
                    <dt class="cont-input-tit">{{ t('myinfo.label.company.compDesc') }}</dt><!-- 회사 특징 -->
                    <dd class="cont-input-wrap" :class="(errors.compContent ? 'error' : '')">
                      <Field name="compContent" as="textarea" v-model="companyInfo.compContent" cols="30" rows="5" :placeholder="t('company.msg.desc8')"></Field>
                      <p class="error-msg">{{ errors.compContent }}</p>
                    </dd>
                  </dl>
                </li>
              </ul>
            </fieldset>
          </div>
        </div>
      </div>
      <div class="btn-wrap center">
        <a href="#" @click.prevent="onCancel('C')" class="btn typeB gray">
          <span>{{ t('myinfo.label.company.btnCancel') }}</span><!-- 취소 -->
        </a>
        <a href="#" @click.prevent="fnModify()" class="btn typeB">
            <span class="reg">{{ t('myinfo.label.company.btnSave') }}</span><!-- 저장 -->
        </a>
      </div>
    </div>
    <teleport to="#common-modal">
      <ap-popup>
        <component
          :is="popupContent"
          :pop-params="popParams"
          @callBack="popFunc"
        />
      </ap-popup>
    </teleport>
  </div>
</template>

<script>
import { inject, defineAsyncComponent, reactive, provide } from 'vue'
import { loadScript } from "vue-plugin-load-script";
import { useCompany } from '@/compositions/useCompany'
import { useMyInfo } from '@/compositions/useMyInfo'
import { useActions } from 'vuex-composition-helpers'
import { useStore } from 'vuex'

// validation 관련
import { Field, useField, useForm } from 'vee-validate'
import validation from '@/utils/validation'
import * as yup from 'yup';

export default {
  name: 'MyCompanyModify',
  props: {
    userCd: String,
    compCd: String
  },
  components:{
    Field,
    ApPopup: defineAsyncComponent(() => import('@/components/comm/ApPopup.vue')),
    ChkPwdPop: defineAsyncComponent(() => import('@/components/popup/ChkPwdPop.vue')),
    UploadFile: defineAsyncComponent(() => import('@/components/comm/UploadFile.vue'))
  },
  setup(props) {
    loadScript("//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js")
    .then(() => {
    })
    .catch((e) => {
      console.log(e)
    })

    const t = inject('t')
    const commonUtils = inject('commonUtils')
    const store = useStore()
    const myInfo = store.getters.getMyInfo()
    const { openAsyncConfirm, openAsyncAlert } = useActions(['openAsyncConfirm', 'openAsyncAlert'])

    const checkParams = reactive({
      keyword : '',
      isCheck : false,
      isCompCheck : true,
      isFileChek : false
    })

    const uploadParams = reactive({
      targetKey: '',
      uploadCd: 'COMPANY01',
      items: []
    })

    const {
      onCancel,
      fnModifyInfo,
      fnChkPwdPop,
      popupContent,
      popParams,
      popFunc
    } = useMyInfo()

    const {
      fnZipPop,
      fetchCompCheck,
      fetchResearch,
      companyInfo,
      fetchCompanyInfo
    } = useCompany()

    const getCompInfo = async () => {
      const res = await fetchCompanyInfo()
      if (res.data) {
        const resData = res.data
        checkParams.isFileChek = true
        uploadParams.targetKey = resData.data.compSeq
      }
    }

     // 로그인 여부 체크
    if (commonUtils.isNotEmpty(myInfo.userCd)) {
      getCompInfo()
    } else {
      router.push({ path: '/login' })
    }

    //////[S] Validation /////////////////////////////////////////////////////////////////
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

    const { handleSubmit, errors } = useForm({
      validationSchema: schema
    })

    const useFieldSet = ['compNm','compEnm','compNum', 'compRepresentNm', 'compPhone','compSite','compZip','compAddr','compEnaddr','compApUserNm','compContent']
    useFieldSet.forEach(str => {
      useField(str)
    })

    function onInvalidSubmit({ values, errors, results }) {
    }

    const fnAction = handleSubmit(async values => {
      if(!checkParams.isCompCheck){
        await openAsyncAlert({ message: t('company.msg.desc31') }) // 이미 등록되어 있는 사업자 번호입니다.
        return
      }
      if(!checkParams.isFileChek){
        await openAsyncAlert({ message: t('company.msg.desc35') }) //사업자 등록증을 첨부해주세요.
        return
      }
      // 입력하신 내용을 저장하시겠습니까?
      if (!await openAsyncConfirm({ message : t('myinfo.msg.company.desc2') })) {
        return
      } else {
        //fnModifyInfo('C')
        fnPassChkPopOpen()
      }

    }, onInvalidSubmit)

    const fnPassChkPopOpen = () => {
      popFunc.value = callBackPass
      fnChkPwdPop()
    }

    const callBackPass = (obj) => {
      if (obj.saveFlag === 'Y') {
        fnModifyInfo('C', obj.pwd)
      }
    }

    // 저장 버튼 이벤트
    const fnModify = () => {
      fnAction()
    }

    // 사업자등록번호 관련
    const fnCompNumChk = async () => {
      if(!companyInfo.value.compNum){
        await openAsyncAlert({ message: t('company.msg.desc5') }) // 사업자 등록번호를 입력해주세요.
        return
      }
      const compNumParams = {
        keyword : companyInfo.value.compNum,
        type : 'compNum',
        noAlertFlag : true
      }
      fetchCompCheck(compNumParams).then((res)=>{
        checkParams.isCompCheck = res
      })
    }

    //[S] 업로드 관련
    provide('upload-COMPANY01', uploadParams)

    const fnAfterFileUpload = (returnObj) =>{
      const uploadList = returnObj.items
      if (!uploadList || uploadList.length === 0) {
        checkParams.isFileChek = false
      } else {
        checkParams.isFileChek = true
      }
      // params.value.files = uploadList
      companyInfo.value.files = uploadList
    }
    //[E] 업로드 관련

    // 담당연구원 관련
    const rSearchParam = reactive({
      list:[]
    })

    const fnRsearch = () => {
      fetchResearch(companyInfo.value.compApUserNm).then((res)=>{
        rSearchParam.list = res.data
      })
    }

    const fnRsearchSelect = (userId, userNm) => {
      rSearchParam.userNm = userNm
      rSearchParam.list =[]
      companyInfo.value.compApUserId = userId
      companyInfo.value.compApUserNm = userNm
    }

    return {
      errors,
      fnModify,
      companyInfo,
      rSearchParam,
      checkParams,
      fnZipPop,
      onCancel,
      fnRsearch,
      fnCompNumChk,
      fnRsearchSelect,
      fnAfterFileUpload,
      popupContent,
      popParams,
      popFunc,
      t
    }
  }
}
</script>
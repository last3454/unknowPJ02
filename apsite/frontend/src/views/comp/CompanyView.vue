<template>
<div class="box">
  <div class="box-cont">
    <div class="cont-top">
      <h2 class="cont-top-title">{{t('company.label.view_title')}}</h2>
      <p class="cont-top-desc">{{t('company.msg.desc26')}}</p>
    </div>
    <div class="cont-mid">
      <div class="cont-form">
        <fieldset class="cont-input cont-narrow">
          <legend class="hidden">{{ t('company.label.company_reg') }}</legend>
          <ul class="cont-input-list">
            <li class="cont-input-list-item">
              <dl>
                <dt class="cont-input-tit">{{ t('company.label.company_nm') }}<span class="desc">{{ t('common.label.korean') }}</span></dt>
                <dd class="cont-input-wrap">
                  <input type="text" readonly :value="params.compNm">
                </dd>
              </dl>
            </li>
            <li class="cont-input-list-item">
              <dl>
                <dt class="cont-input-tit">{{ t('company.label.company_nm') }}<span class="desc">{{ t('common.label.english') }}</span></dt>
                <dd class="cont-input-wrap">
                  <input type="text" readonly :value="params.compEnm">
                </dd>
              </dl>
            </li>
            <li class="cont-input-list-item">
              <dl>
                <dt class="cont-input-tit">{{ t('company.label.company_no') }}</dt>
                <dd class="cont-input-wrap">
                  <input type="text" readonly :value="params.compNum">
                  <div v-if="isCauth && filesMap['COMPANY01'] !== undefined && filesMap['COMPANY01'].length > 0" class="b-v-file-wrap">
                      <a href="#" v-for="item in filesMap['COMPANY01']" :key="'attach_' + item.seq" class="i-btn medium typeB gray" @click.prevent="downloadFile(item.seq, item.fileNm)">
                        <span class="down">{{ item.fileNm }}</span>
                      </a>
                  </div>                  
                </dd>
              </dl>
            </li>
            <li v-show="myInfo.compCd === 'AP'" class="cont-input-list-item">
              <dl>
                <dt class="cont-input-tit">{{ t('company.label.company_code') }}</dt>
                <dd class="cont-input-wrap">
                  <input type="text" readonly :value="params.compSapCd">
                </dd>
              </dl>
            </li>
            <li class="cont-input-list-item">
              <dl>
                <dt class="cont-input-tit">{{ t('company.label.company_represent') }}</dt>
                <dd class="cont-input-wrap">
                  <input type="text" readonly :value="params.compRepresentNm">
                </dd>
              </dl>
            </li>            
            <li class="cont-input-list-item">
              <dl>
                <dt class="cont-input-tit">{{ t('company.label.tel') }}</dt>
                <dd class="cont-input-wrap">
                  <input type="text" readonly :value="params.compPhone">
                </dd>
              </dl>
            </li>
            <li class="cont-input-list-item">
              <dl>
                <dt class="cont-input-tit">{{ t('common.label.website') }}</dt>
                <dd class="cont-input-wrap">
                  <input type="text" readonly :value="params.compSite">
                </dd>
              </dl>
            </li>
            <li class="cont-input-list-item">
              <dl>
                <dt class="cont-input-tit">{{ t('common.label.postno') }}</dt>
                <dd class="cont-input-wrap">
                  <input type="text" readonly :value="params.compZip">
                </dd>
              </dl>
            </li>
            <li class="cont-input-list-item">
              <dl>
                <dt class="cont-input-tit">{{ t('company.label.addr') }}<span class="desc">{{ t('common.label.korean') }}</span></dt>
                <dd class="cont-input-wrap">
                  <input type="text" readonly :value="params.compAddr">
                </dd>
              </dl>
            </li>
            <li class="cont-input-list-item">
              <dl>
                <dt class="cont-input-tit">{{ t('company.label.addr') }}<span class="desc">{{ t('common.label.english') }}</span></dt>
                <dd class="cont-input-wrap">
                  <input type="text" readonly :value="params.compEnaddr">
                </dd>
              </dl>
            </li>
            <li class="cont-input-list-item">
              <dl>
                <dt class="cont-input-tit">{{ t('company.label.reg_charger') }}</dt>
                <dd class="cont-input-wrap">
                  <input type="text" readonly :value="params.compApUserNm">
                </dd>
              </dl>
            </li>
            <li class="cont-input-list-item">
              <dl>
                <dt class="cont-input-tit">{{ t('company.label.company_desc') }}</dt>
                <dd class="cont-input-wrap">
                  <textarea name="" id="" cols="30" rows="5" readonly v-model="params.compContent"></textarea>
                </dd>
              </dl>
            </li>
          </ul>
        </fieldset>
      </div>
    </div>
    <div class="cont-bot">
      <div class="btn-wrap center">
        <a href="#" @click.prevent="fnList()" class="btn typeA"><span class="list">{{ t('common.label.list') }}</span></a>
        <a v-if="isCauth" href="#" @click.prevent="fnReg(params.compSeq)" class="btn typeB long"><span class="mod">{{t('company.label.company_mod2')}}</span></a>
      </div>
    </div>
  </div>
</div>
</template>

<script>
import { inject } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { useCompany } from '@/compositions/useCompany'
import { useUploadFile } from '@/compositions/useUploadFile'

export default {
  name: 'CompanyView',
  setup (){
    const t = inject('t')
    const route = useRoute()
    const router = useRouter()
    const store = useStore()
    const path  = router.currentRoute.value.path
    const myInfo = store.getters.getMyInfo()    
    const mFlag = (path.toLowerCase().indexOf('companym') > -1)
    const seq = route.query.seq
    const commonUtils = inject('commonUtils')

    const {
      params,
      fetchCompany,
      fnLinkList,
      fnLinkReg,
      fnManagerLinkList,
      fnManagerLinkReg
    } = useCompany()

    const fnList = () => {
      if(mFlag) {
        fnManagerLinkList()
      }else{
        fnLinkList()
      }
    }

    const fnReg = (seq) =>{
      if(mFlag) {
        fnManagerLinkReg(seq)
      }else{
        fnLinkReg(seq)
      }
    }

    const {
      filesMap,
      fetchUploadFiles,
      downloadFile
    } = useUploadFile()    

    const init = async () => {
      await fetchCompany(seq)
      await fetchUploadFiles({ targetKey: '' + seq, uploadCd: 'COMPANY01' })
    }

    init()

    const isCauth = commonUtils.checkAuth('SGG000001|SGG000003|SGG000005|SGG000011|SGG000012|SGG000013|SGG000014')

    return {
      isCauth,
      myInfo,
      params,
      fnList,
      fnReg,
      filesMap,
      downloadFile,
      t
    }
  }
}

</script>
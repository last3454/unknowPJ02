<template>
  <div class="box">
      <div class="box-cont">
        <div class="cont-top cont-top2">
          <h2 class="cont-top-title">{{ t('company.label.list_title') }}</h2>
          <div class="search-wrap p-right">
              <fieldset>
                  <legend class="hidden">{{ t('company.label.list_title2') }}</legend>
                  <div class="search-select">
                    <select class="slt" v-model="searchParams.type" >
                      <option value="">ALL</option>
                      <option value="compNm">{{ t('company.label.company_nm') }}</option>
                      <option value="addr">{{ t('company.label.addr') }}</option>
                    </select>
                  </div>
                  <div class="search-input">
                    <input type="search" v-model="searchParams.keyword" @keyup.enter="fnSearch(1)" :placeholder="t('common.msg.search_msg')">
                    <button type="button" @click="fnSearch(1)" class="search-btn" :title="t('common.label.search')">{{ t('common.label.search') }}</button>
                  </div>
              </fieldset>
          </div>
        </div>
        <div class="cont-mid">
            <div class="cont-table">
                <table class="tb typeA">
                    <caption class="hidden">{{ t('company.label.list_title') }}</caption>
                    <thead>
                      <tr>
                        <th class="w-5">{{ t('common.label.number') }}</th>
                        <th class="w-20">{{ t('company.label.company_nm') }}</th>
                        <th class="w-14">{{ t('company.label.company_code2') }} / {{ t('company.label.company_code') }}</th>
                        <th>{{ t('company.label.addr') }}</th>
                        <th class="w-10">{{ t('company.label.tel') }}</th>
                        <th class="w-10">{{ t('company.label.reg_div') }}</th>
                        <th class="w-7">{{ t('common.label.reg_user') }}</th>
                        <th class="w-7">{{ t('company.label.manager_function') }}</th>
                      </tr>
                    </thead>
                    <tbody>
                      <template v-if="list && list.length > 0">
                        <tr v-for="(vo, idx) in list" :key="'tr_' + idx">
                          <td>
                            {{ page.totalCnt - ((page.pageSize * (page.nowPageNo-1)) + idx)}}
                          </td>
                          <td class="t-left">
                            <a href="#" @click.prevent="fnLinkView(vo.compSeq)" class="t-link">                              
                              {{langCd === 'ko' ? vo.compNm : vo.compEnm}}
                            </a>
                          </td>
                          <td class="t-left">
                            {{vo.compCd}} / {{vo.compSapCd}}
                          </td>
                          <td class="t-left">
                            ({{vo.compZip}})
                            <br/>
                            {{langCd === 'ko' ? vo.compAddr : vo.compEnaddr}}
                          </td>
                          <td>{{commonUtils.phoneFormat(vo.compPhone)}}</td>
                          <td>{{vo.regUserType === 'GRP' ? t('company.label.company_out') : t('company.label.company_in')}}</td>
                          <td>{{commonUtils.maskingName(vo.regUserNm)}}</td>
                          <td>
                            <template v-if="isCauth">
                              <template v-if="vo.compStatusCd === 'CS000'">
                                <div class="cont-input-wrap__flex">
                                  <a href="#" @click.prevent="fnApproval(vo.compSeq, 'APPROVAL')" class="i-btn typeB small gray"><span>{{t('common.label.approval')}}</span></a>
                                  <a href="#" @click.prevent="fnApproval(vo.compSeq, 'REJECT')" class="i-btn typeB small gray ml5"><span>{{t('common.label.reject')}}</span></a>
                                </div>
                              </template>
                              <template v-else>
                                <a href="#" @click.prevent="fnDelete(vo.compSeq)" class="i-btn typeB small gray mt05"><span>{{t('common.label.delete')}}</span></a>
                              </template>
                            </template>
                          </td>
                        </tr>
                      </template>
                      <template v-else>
                        <tr>
                          <td colspan="99">{{ t('common.msg.no_data') }}</td>
                        </tr>
                      </template>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="cont-bot">
          <!-- components 로 작업 예정 -->
          <pagination :page-info="page" @go-page-num="fnSearch" />
          <!-- components 로 작업 예정 -->
          <div v-if="isCauth" class="btn-wrap right">
              <a href="#" class="btn typeB" @click.prevent="fnLinkReg()"><span class="reg">{{ t('common.label.register') }}<!-- 등록 --></span></a>
          </div>
        </div>                    
      </div>
  </div>
</template>

<script>

import { useCompany } from '@/compositions/useCompany'

import { defineAsyncComponent, inject, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { useActions } from 'vuex-composition-helpers'

export default {
  name: 'CompanyList',
  components: {
    Pagination: defineAsyncComponent(() => import('@/components/comm/Pagination.vue'))
  },  
  setup() {
    const { openAsyncConfirm, openAsyncAlert } = useActions(['openAsyncConfirm', 'openAsyncAlert'])
    const t = inject('t')
    const router = useRouter()
    const store = useStore()
    const langCd = store.getters.getLangCd()
    const commonUtils = inject('commonUtils')
    const {
      page,
      list,
      fnLinkReg,
      fnLinkView,
      fetchDelete,
      fetchApproval,
      fetchCompanys
    } = useCompany()

    // const page = computes.page
    // const list = computes.list

    const searchParams = reactive({
      type:'',
      keyword: '',
      nowPageNo: 1
    })

    const fnSearch = (pg) => {
      if(!pg){
        pg = 1
      }      
      searchParams.nowPageNo = pg
      fetchCompanys(searchParams)
    }
    //Composition에서 받아서 실행
    fnSearch() 

    const fnApproval = async (compSeq, type) =>{
      const message = t('company.msg.desc17',{typeNm : (type === 'APPROVAL' ? t('common.label.approval') : t('common.label.reject'))})
      if (!await openAsyncConfirm({ message })) {
        return
      }

      const params = {
        compSeq,
        compStatusCd: type,
        masterFlag: 'Y'
      }

      fetchApproval(params).then(() => {
        router.go(router.currentRoute)
      })
    }

    const fnDelete = async (compSeq) => {
      const message = t('company.msg.desc30')
      if (!await openAsyncConfirm({ message })) {
        return
      }

      fetchDelete({compSeq}).then(async (flag) => {
        if(flag){
          await openAsyncAlert({ message: t('common.msg.delete_ok') }) // 처리 되었습니다.
          router.go(router.currentRoute)
        }
      })
    }

    const isCauth = commonUtils.checkAuth('SGG000001|SGG000003|SGG000011|SGG000012|SGG000013|SGG000014')

    return {
      isCauth,
      langCd,
      page,
      list,
      commonUtils,
      searchParams,
      fnLinkView,
      fnLinkReg,
      fnSearch,
      fnDelete,
      fnApproval,
      t
    }
  }
}

</script>

<template>
  <div class="box">
      <div class="box-cont">
        <div class="cont-top cont-top2">
          <h2 class="cont-top-title">{{ t('company.label.manager_title') }}</h2>
          <div class="search-wrap p-right">
              <fieldset>
                  <legend class="hidden">{{ t('company.label.list_title2') }}</legend>
                  <div class="search-select">
                    <select class="slt" v-model="searchParams.type" >
                      <template v-if="searchParams.compCd === 'AP'">
                        <option value="">ALL</option>
                        <option value="compNm">{{ t('company.label.company_nm') }}</option>
                      </template>
                      <option value="id">{{ t('company.label.manager_id') }}</option>
                    </select>
                  </div>                
                  <legend class="hidden">{{ t('company.label.manager_title2') }}</legend>
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
                        <th class="w-15">{{ t('company.label.company_nm') }}</th>
                        <th class="w-15">{{ t('common.label.name') }}</th>
                        <th class="w-15">{{ t('company.label.manager_id') }}</th>
                        <th class="w-20">{{ t('company.label.manager_reg_dtm') }}</th>
                        <th>{{ t('company.label.manager_last_dtm') }}</th>
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
                            <a href="#" @click.prevent="fnManagerLinkView(vo.compSeq)" class="t-link" :title="t('company.label.company_search')">                              
                              {{langCd === 'ko' ? vo.compNm : vo.compEnm}}
                            </a>
                          </td>
                          <td>
                            <template v-if="vo.masterFlag === 'Y'">
                              <!-- <div class="master_y">
                                <span class="name">{{commonUtils.maskingName(vo.userNm)}}</span>
                              </div> -->
                              {{commonUtils.maskingName(vo.userNm)}}(마스터)
                            </template>
                            <template v-else>
                              {{commonUtils.maskingName(vo.userNm)}}
                            </template>
                          </td>
                          <td>{{commonUtils.maskingId(vo.loginId)}}</td>
                          <td>{{vo.regDtm}}</td>
                          <td>{{vo.lastLoginDt}}</td>
                          <td>
                            <template v-if="vo.statusCd === 'US000'">
                              <div class="cont-input-wrap__flex">
                                <a href="#" @click.prevent="fnApproval(vo.compSeq, vo.userCd, 'APPROVAL')" class="i-btn typeB small gray"><span>{{t('common.label.approval')}}</span></a>
                                <a href="#" @click.prevent="fnApproval(vo.compSeq, vo.userCd, 'REJECT')" class="i-btn typeB small gray ml5"><span>{{t('common.label.reject')}}</span></a>
                              </div>
                            </template>
                            <template v-else>
                              <div class="cont-input-wrap__flex">
                                <a href="#" v-if="vo.masterFlag !== 'Y'" @click.prevent="fnRetire(vo.userNm, vo.userCd)" class="i-btn typeB small gray"><span>{{ t('company.label.retire') }}</span></a>
                                <a  v-if="vo.masterFlag !== 'Y'" href="#" @click.prevent="fnMaster(vo.compCd, vo.userNm, vo.userCd)" class="i-btn typeB small ml5"><span>{{t('company.label.company_master')}}</span></a> 
                              </div>
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
          <pagination :page-info="page" @go-page-num="fnSearch" />
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
  name: 'CompanyManagerList',
  components: {
    Pagination: defineAsyncComponent(() => import('@/components/comm/Pagination.vue'))
  },  
  setup() {

    const { openAsyncConfirm, openAsyncAlert } = useActions(['openAsyncConfirm', 'openAsyncAlert'])
    const t = inject('t')
    const commonUtils = inject('commonUtils')
    const router = useRouter()
    const store = useStore()
    const myInfo = store.getters.getMyInfo()
    const langCd = store.getters.getLangCd()

    const {
      page,
      list,
      fetchRetire,
      fetchMaster,
      fetchApproval,
      fnManagerLinkView,
      fetchManagerCompanys
    } = useCompany()

    const searchParams = reactive({
      type: myInfo.compCd !=='AP' ? 'id' : '',
      compCd: myInfo.compCd,
      keyword: '',
      nowPageNo: 1
    })    

    const fnSearch = async (pg, type) => {
      if(!pg){
        pg = 1
      }      

      if(type != 'INIT' && !searchParams.keyword){
        await openAsyncAlert({ message: t('common.msg.search_msg') })
        return
      }

      searchParams.nowPageNo = pg
      fetchManagerCompanys(searchParams)
    }
    //Composition에서 받아서 실행
    if(searchParams.compCd !== 'AP'){
      fnSearch(1,'INIT')    
    }

    const fnApproval = async (compSeq, userCd, type) =>{    
      const message = t('company.msg.desc17',{typeNm : (type === 'APPROVAL' ? t('common.label.approval') : t('common.label.reject'))})
      if (!await openAsyncConfirm({ message })) {
        return
      }
      const params = {
        compSeq: compSeq,
        compUserStatusCd: type,
        compUserCd: userCd,
        masterFlag: 'N'
      }

      fetchApproval(params).then(() => {
        router.go(router.currentRoute)
      })      
    }

    const fnMaster = async (compCd, userNm, userCd) => {
      const masterNm = list.value.filter(obj => obj.masterFlag === 'Y')[0].userNm
      const message = t('company.msg.desc18',{ masterNm, userNm })
      if (!await openAsyncConfirm({ message })) {
        return
      }
      const params = {
        compCd: compCd,
        compUserCd: userCd
      }
      fetchMaster(params).then(async (res)=>{
        if("AP" !== myInfo.compCd){
          await openAsyncAlert({ message: t('company.msg.desc19') })
          store.dispatch('signout')
          .then(res => {
            const resData = res.data
            if (resData.code === 'C0000') {
              router.push({ path: '/login' })
            } else {
              alert(res.data.message)
            }
          })
        }
      })
    }

    const fnRetire = async (userNm, userCd) =>{
      const message = t('company.msg.desc28',{ userNm })
      if (!await openAsyncConfirm({ message })) {
        return
      }

      fetchRetire(userCd, userNm).then(async (flag) =>{
        if(flag){
          await openAsyncAlert({ message: t('company.msg.desc29') })
          router.go(router.currentRoute)
        }
      })
    }

    const isAuth = commonUtils.checkAuth('SGG000001|SGG000005')

    return {
      page,
      list,
      isAuth,
      commonUtils,
      searchParams,
      fnSearch,
      fnRetire,
      fnMaster,
      fnApproval,
      fnManagerLinkView,
      langCd,
      t
    }
  },
}
</script>

<style scoped>
.i-btn.medium {
    width: auto;
    min-height: 32px;
    padding: 0 12px;
    font-size: 14px;
}
.master_y {
  background-size: cover;
  width : 100%; 
  height : 33px; 
  background-position: 0px 3px;
  background-image : url('@/assets/img/master_label.svg');
}
.master_y .name {color : white; line-height: 27px;}
</style>
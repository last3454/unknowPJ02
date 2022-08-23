<template>
  <div class="inner">
    <div class="box">
      <div class="box-cont">
        <div class="cont-top cont-top2">
          <h2 class="cont-top-title">원료관리시스템 테이블, 컬럼 맵핑</h2>
        </div>
        <div class="cont-mid">
          <div class="cont-table">
              <table class="tb typeA typeA__input__border" style="font-size:12px">
                <caption class="hidden">원료관리시스템 테이블, 맵핑 정보</caption>
                <thead>
                  <tr>
                    <th>등록된 테이블</th>
                    <th class="w-35">RDP</th>
                    <th class="w-35">GRP</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>
                      <div class="search-select" style="width:100%;">
                        <select class="slt" v-model="params.selectOpt" @change="fnSelect()">
                          <option value="">골라 골라 테이블을 골라</option>
                          <option v-for="(vo,idx) in params.tableList" :key="'opt_'+idx">
                            {{vo.FROM_TABLE_NM}}
                          </option>
                        </select>
                      </div>
                    </td>
                    <td>
                      <div class="search-input" style="max-width:100%;">
                        <input type="search" v-model="searchParams.fromKeyword" @keyup.enter="fnSearch('RDP')">
                        <button type="button" @click="fnSearch('RDP')" class="search-btn"></button>
                      </div>
                    </td>
                    <td>
                      <div class="search-input" style="max-width:100%;">
                        <input type="search" v-model="searchParams.toKeyword" @keyup.enter="fnSearch('GRP')">
                        <button type="button" @click="fnSearch('GRP')" class="search-btn"></button>
                      </div>
                    </td>
                  </tr>
                </tbody>
              </table>            
              <table class="tb typeA typeA__input__border" style="font-size:12px">
                <caption class="hidden">원료관리시스템 테이블, 맵핑 정보</caption>
                <thead>
                  <tr>
                    <th class="w-15">FROM 컬럼</th>
                    <th class="w-15">FROM 코멘트</th>
                    <th class="w-10">FROM 데이터 형식</th>
                    <th class="w-15">TO 컬럼</th>
                    <th class="w-15">TO 코멘트</th>                    
                    <th class="w-10">TO 데이터 형식</th>
                    <th class="w-20">비고</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(vo, idx) in params.fromList" :key="'tr_' + idx" :style="vo.color">
                    <td>{{vo.COLUMN_NAME}}</td>
                    <td>{{vo.COMMENTS}}</td>
                    <td>{{vo.DATA_TYPE}}({{vo.DATA_LENGTH}})</td>
                    <td>
                      <input type="text" :name="'opt_' + idx" :id="'input_opt_' + idx" :list="'opt_' + idx" v-model="vo.TO_COLUMN_CD" @change="fnOptSelect(idx, vo.COLUMN_NAME)"/>
                      <datalist :id="'opt_' + idx">
                        <option v-for="(svo, sidx) in params.toList" :key="'opt_' + idx +'_'+ sidx">{{svo.COLUMN_NAME}}</option>
                      </datalist>
                    </td>
                    <td>
                      {{vo.TO_COMMENT}}
                    </td>
                    <td>
                      {{vo.TO_DATA}}
                    </td>
                    <td>
                      <input type="text" v-model="vo.BIGO"/>
                    </td>                    
                  </tr>
                </tbody>
              </table>
              <div class="cont-bot">
                <div class="btn-wrap center">
                    <a href="#" @click.prevent="fnSave()" class="btn typeB">
                      <span class="complete">저장</span>
                    </a>
                </div>
              </div>              
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from '@/utils/customAxios'
import { reactive } from 'vue'
export default {
 name: "IntegrationDataSample",
 setup() {
  //TODO 검증할때만 쓰고 다 삭제 하자
  //TODO 검증할때만 쓰고 다 삭제 하자
  //TODO 검증할때만 쓰고 다 삭제 하자
  //TODO 검증할때만 쓰고 다 삭제 하자
  const params = reactive({
    selectOpt: '',
    tableList: [],
    fromList:[],
    toList:[]
  })

  const searchParams = reactive({
    type:'',
    keyword: '',
    fromKeyword : '',
    toKeyword : ''
  })

  const fnRegTable = () => {
    axios({
      url: '/api/test/table-reg-list',
      method: 'get',
      isLoading: true,
      params: searchParams
    })
    .then(async res => {
      const resData = res.data
      params.tableList = resData.data
    })
  }
  
  fnRegTable()

  const fnSelect = () =>{
    searchParams.fromKeyword = params.selectOpt
    fnSearch('RDP')
  }

  const fnSearch = async (type) =>{
    searchParams.type = type
    if('RDP' === type){
      if(searchParams.fromKeyword === ''){
        alert('검색 테이블을 입력해주세요.')
        return
      }
      searchParams.keyword = searchParams.fromKeyword
    }else{
      if(searchParams.fromKeyword === ''){
        alert('RDP 데이터 부터 먼저 조회해주세요.')
        return
      }
      searchParams.keyword = searchParams.toKeyword
    }
    await axios({
      url: '/api/test/table-list',
      method: 'get',
      isLoading: true,
      params: searchParams
    })
    .then(async res => {
      params.selectOpt = ''
      const resData = res.data
      if (resData.code === 'C0000') {
        if('RDP' === type){
          params.fromList = resData.data
          const toObj = params.fromList.filter(obj => obj.TO_TABLE_NM != undefined)[0]
          if(toObj){
            searchParams.toKeyword = toObj.TO_TABLE_NM
            await fnSearch('GRP')
            params.fromList.forEach((obj,idx) => {
              if(obj.TO_TABLE_NM != undefined){
                fnOptSelect(idx,obj.COLUMN_NAME)
              }
            })            
          }
        }else{
          params.toList = resData.data           
        }
      } else {
        alert(resData.message)
      }
    })
  }

  const fnOptSelect = (idx, fromColumn) => {
    const inputOpt = document.getElementById('input_opt_' + idx)
    const fromObj = params.fromList.filter(obj => obj.COLUMN_NAME == fromColumn)[0]
    const toObj = params.toList.filter(obj => obj.COLUMN_NAME == inputOpt.value)[0]
    if(toObj){
      if(toObj.DATA_TYPE != fromObj.DATA_TYPE || toObj.DATA_LENGTH != fromObj.DATA_LENGTH){
        fromObj.color ="background-color:yellow"
      }else{
        fromObj.color =""
      }
      fromObj.TO_COLUMN_CD  = toObj.COLUMN_NAME
      fromObj.TO_COMMENT = toObj.COMMENTS
      fromObj.TO_DATA = toObj.DATA_TYPE +'('+ toObj.DATA_LENGTH +')'
    }else{
      fromObj.color =""
      fromObj.TO_COLUMN_CD  = ''
      fromObj.TO_COMMENT = ''
      fromObj.TO_DATA = ''
    }
  }

  const fnSave = async () => {
    const list = []
    const fromList = params.fromList.filter(obj => ((obj.TO_COLUMN_CD != undefined && obj.TO_COLUMN_CD != '') || (obj.BIGO != undefined && obj.BIGO != '')))
    fromList.forEach((obj) => {
      list.push({
        fromTableNm : searchParams.fromKeyword,
        fromColumnCd: obj.COLUMN_NAME,
        toTableNm : searchParams.toKeyword,
        toColumnCd: obj.TO_COLUMN_CD || '',
        bigo: obj.BIGO || ''
      })
    })

    axios({
      url: '/api/test/table-save',
      method: 'post',
      isLoading: true,
      data: {
        list : list
      }
    })
    .then(res => {
      const resData = res.data
      if (resData.code === 'C0000') {
        
      } else {
        alert(resData.message)
      }
    })  
  }

  return {
    params,
    searchParams,
    fnSave,
    fnSelect,
    fnSearch,
    fnOptSelect
  }
 }
}
</script>

<style>

</style>
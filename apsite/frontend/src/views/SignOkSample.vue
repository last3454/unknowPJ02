<template>
  <div class="box">
    <div class="box-cont">
      <div class="cont-top">
        <h2 class="cont-top-title">SIGN-OK SAMPLE PAGE</h2>
      </div>
      <div class="cont-mid">
        <div class="cont-form noBottom">
          <fieldset class="cont-input">
            <ul class="ig-sec-list">
              <li class="sec-input-wrap" style="">
                <div class="sec-input-wrap__center">
                  <h3 class="sec-input-tit" style="right: 54%;">TOKEN 발급</h3>
                  <ul class="sec-input-list cont-700">
                    <li class="cont-input-list-item">
                      <div class="cont-table">
                        <table class="tb typeA">
                          <thead>
                            <tr>
                              <th class="w-20"></th>
                              <th>내용</th>
                            </tr>
                          </thead>
                          <tbody>
                            <tr>
                              <td class="t-left">
                                <a href="#" @click.prevent="fetchAccessToken()" class="i-btn typeB medium gray"><span>토큰 생성</span></a> 
                              </td>
                              <td class="t-left" style="word-break: break-all;">
                                {{ params.accessToken}}
                              </td>
                            </tr>
                          </tbody>
                        </table>
                      </div>
                    </li>
                  </ul>
                </div>
              </li>
              <li class="sec-input-wrap">
                <div class="sec-input-wrap__center">
                  <h3 class="sec-input-tit" style="right: 54%;">TEMPLATE 목록 조회</h3>
                  <ul class="sec-input-list cont-700">
                    <li class="cont-input-list-item">
                      <div class="cont-table">
                        <table class="tb typeA">
                          <thead>
                            <tr>
                              <th class="w-20"></th>
                              <th>내용</th>
                            </tr>
                          </thead>
                          <tbody>
                            <tr>
                              <td class="t-left">
                                <a href="#" @click.prevent="fetchTemplateList()" class="i-btn typeB medium gray"><span>조회</span></a> 
                              </td>
                              <td class="t-left" style="word-break: break-all;">
                                <div v-for="(vo, idx) in params.templateList"  :key="'div_' + idx">
                                  문서명 : {{ vo.title}} / 문서 아이디 : {{vo.id}} <br/>
                                </div>
                              </td>
                            </tr>
                          </tbody>
                        </table>
                      </div>
                    </li>
                  </ul>
                </div>
              </li>              
              <li class="sec-input-wrap">
                <div class="sec-input-wrap__center">
                  <h3 class="sec-input-tit" style="right: 54%;">TEMPLATE 상세 조회</h3>
                  <ul class="sec-input-list cont-700">
                    <li class="cont-input-list-item">
                      <div class="cont-table">
                        <table class="tb typeA">
                          <thead>
                            <tr>
                              <th class="w-20"></th>
                              <th>내용</th>
                            </tr>
                          </thead>
                          <tbody>
                            <tr>
                              <td class="t-left">
                                <input type="text" v-model="params.templateId" style="margin-bottom:5px;"/>
                                <a href="#" @click.prevent="fetchTemplateInfo()" class="i-btn typeB medium gray"><span>조회</span></a> 
                              </td>
                              <td class="t-left" style="word-break: break-all;">
                                <span v-if="params.templateInfo.title" style="font-weight:bold;">
                                  [문서명 : {{ params.templateInfo.title }}]
                                </span>
                                <div v-for="(vo, idx) in params.templateInfo.item"  :key="'div_' + idx">
                                {
                                  alias : {{ vo.alias }},
                                  key : {{ vo.key }},
                                  type : {{vo.type }}
                                }<br/>
                                </div>
                              </td>
                            </tr>
                          </tbody>
                        </table>
                      </div>
                    </li>
                  </ul>
                </div>
              </li>
              <li class="sec-input-wrap">
                <div class="sec-input-wrap__center">
                  <h3 class="sec-input-tit" style="right: 54%;">문서 생성</h3>
                  <ul class="sec-input-list cont-700">
                    <li class="cont-input-list-item">
                      <div class="cont-table">
                        <table class="tb typeA">
                          <thead>
                            <tr>
                              <th class="w-20"></th>
                              <th>내용</th>
                            </tr>
                          </thead>
                          <tbody>
                            <tr>
                              <td colspan="2">
                                <span v-if="params.templateInfo.title" style="font-weight:bold;">
                                  [문서명 : {{ params.templateInfo.title }}]
                                </span>
                                <ul class="cont-input-list">
                                  <span style="font-weight:bold;color:red;">{{ params.documentInfo.message}}</span>
                                  <li>
                                    [생성 문서 ID] : {{ params.documentInfo.id}}
                                  </li>
                                  <li v-for="(vo, idx) in params.documentInfo.signer"  :key="'div_' + idx">
                                    {{ vo.name }} [접속 주소] : <a :href="vo.view_url" target="_blank">{{ vo.view_url }}</a>
                                  </li>
                                </ul>
                              </td>
                            </tr>
                            <tr>
                              <td class="t-left">
                                <a href="#" @click.prevent="fetchDocumentCreate()" class="i-btn typeB medium gray"><span>생성</span></a> 
                              </td>
                              <td class="t-left" style="word-break: break-all;">
                                <ul class="cont-input-list">
                                  <li v-for="(vo, idx) in params.templateInfo.item"  :key="'div_' + idx" class="cont-input-list-item">
                                    <dd class="cont-input-wrap">
                                      <div class="cont-input-wrap__flex">
                                        {{ vo.alias }} [{{ vo.key }}] : <input type="text" v-model="vo.value" style="width:250px;"/>
                                      </div>
                                    </dd>
                                  </li>
                                </ul>
                              </td>
                            </tr>
                          </tbody>
                        </table>
                      </div>
                    </li>
                  </ul>
                </div>
              </li>                           
              <li class="sec-input-wrap">
                <div class="sec-input-wrap__center">
                  <h3 class="sec-input-tit" style="right: 54%;">문서 리스트 조회</h3>
                  <ul class="sec-input-list cont-700">
                    <li class="cont-input-list-item">
                      <div class="cont-table">
                        <table class="tb typeA">
                          <thead>
                            <tr>
                              <th class="w-20"></th>
                              <th>내용</th>
                            </tr>
                          </thead>
                          <tbody>
                            <tr>
                              <td class="t-left">
                                <a href="#" @click.prevent="fetchDocumentStastusList()" class="i-btn typeB medium gray"><span>조회</span></a> 
                              </td>
                              <td class="t-left" style="word-break: break-all;">
                                <ul class="cont-input-list">
                                  <li v-for="(vo, idx) in params.documentList"  :key="'div_' + idx" class="cont-input-list-item">
                                    <dd class="cont-input-wrap">
                                      <div class="cont-input-wrap__flex">
                                        ID: {{ vo.id }}, 문서명 : {{ vo.title }} → 작성자: [{{ vo.creator_name }}], 상태 [{{vo.status_description}}]
                                      </div>
                                    </dd>
                                  </li>
                                </ul>
                              </td>
                            </tr>
                          </tbody>
                        </table>
                      </div>
                    </li>
                  </ul>
                </div>
              </li>                           
              <li class="sec-input-wrap">
                <div class="sec-input-wrap__center">
                  <h3 class="sec-input-tit" style="right: 54%;">문서 상세 조회</h3>
                  <ul class="sec-input-list cont-700">
                    <li class="cont-input-list-item">
                      <div class="cont-table">
                        <table class="tb typeA">
                          <thead>
                            <tr>
                              <th class="w-20"></th>
                              <th>내용</th>
                            </tr>
                          </thead>
                          <tbody>
                            <tr>
                              <td colspan="2">
                                  {{ params.documentStatusInfo }}
                                <span v-if="params.documentStatusInfo.title" style="font-weight:bold;">
                                  [문서명 : {{ params.documentStatusInfo.title }}]
                                </span>
                                <ul class="cont-input-list">
                                  <span style="font-weight:bold;color:red;">{{ params.documentStatusInfo.message}}</span>
                                  <li>
                                    [문서 ID] : {{ params.documentStatusInfo.id}}
                                  </li>
                                  <li v-for="(vo, idx) in params.documentStatusInfo.signer"  :key="'div_' + idx">
                                    {{ vo.name }} [접속 주소] : <a :href="vo.view_url" target="_blank">{{ vo.view_url }}</a>
                                  </li>
                                  <li v-for="(vo, idx) in params.documentStatusInfo.signer"  :key="'div_' + idx">
                                    {{ vo.name }} [다운로드 주소] : <a :href="vo.download_url" target="_blank">{{ vo.download_url }}</a>
                                  </li>
                                </ul>
                              </td>
                            </tr>                            
                            <tr>
                              <td class="t-left">
                                <input type="text" v-model="params.documentId" style="margin-bottom:5px;"/>
                                <a href="#" @click.prevent="fetchDocumentStatusInfo()" class="i-btn typeB medium gray"><span>조회</span></a> 
                              </td>
                              <td class="t-left" style="word-break: break-all;">
                                <ul class="cont-input-list">
                                  <li v-for="(vo, idx) in params.documentStatusInfo.item"  :key="'div_' + idx" class="cont-input-list-item">
                                    <dd class="cont-input-wrap">
                                      <div class="cont-input-wrap__flex">
                                        {{ vo.alias }} [{{ vo.key }}] : {{vo.value}}
                                      </div>
                                    </dd>
                                  </li>
                                </ul>
                              </td>
                            </tr>
                          </tbody>
                        </table>
                      </div>
                    </li>
                  </ul>
                </div>
              </li>                           
              <li class="sec-input-wrap">
                <div class="sec-input-wrap__center">
                  <h3 class="sec-input-tit" style="right: 54%;">문서 승인 URL 조회</h3>
                  <ul class="sec-input-list cont-700">
                    <li class="cont-input-list-item">
                      <div class="cont-table">
                        <table class="tb typeA">
                          <thead>
                            <tr>
                              <th class="w-20"></th>
                              <th>내용</th>
                            </tr>
                          </thead>
                          <tbody>
                            <tr>
                              <td colspan="2">
                                  {{ params.approValUrl }}
                              </td>
                            </tr>                            
                            <tr>
                              <td class="t-left">
                                <input type="text" v-model="params.documentId" style="margin-bottom:5px;"/>
                                <a href="#" @click.prevent="fetcApprovalUrl()" class="i-btn typeB medium gray"><span>조회</span></a> 
                              </td>
                              <td class="t-left" style="word-break: break-all;"></td>
                            </tr>
                          </tbody>
                        </table>
                      </div>
                    </li>
                  </ul>
                </div>
              </li>                           
              <li class="sec-input-wrap">
                <div class="sec-input-wrap__center">
                  <h3 class="sec-input-tit" style="right: 54%;">문서 다운로드</h3>
                  <ul class="sec-input-list cont-700">
                    <li class="cont-input-list-item">
                      <div class="cont-table">
                        <table class="tb typeA">
                          <thead>
                            <tr>
                              <th class="w-20"></th>
                              <th>내용</th>
                            </tr>
                          </thead>
                          <tbody>
                            <tr>
                              <td colspan="2">
                                <span v-if="params.documentStatusInfo.title" style="font-weight:bold;">
                                  [문서명 : {{ params.documentStatusInfo.title }}]
                                </span>
                                <ul class="cont-input-list">
                                  <li>
                                    [생성 문서 ID] : {{ params.documentStatusInfo.id}}
                                  </li>
                                </ul>
                                <embed width=100% height=100% type="application/pdf" :src="params.documentData"/>
                              </td>
                            </tr>
                            <tr>
                              <td colspan="2">
                                <input type="text" v-model="params.documentId" style="margin-bottom:5px;text-align:center;"/>
                                <a href="#" @click.prevent="fetchDocumentDownload()" class="i-btn typeB medium gray"><span>다운로드</span></a> 
                                <a href="#" @click.prevent="downloadFile(178, 'technologyForm.pdf')" class="i-btn typeB medium gray mt10"><span>S3 다운로드</span></a> 
                              </td>
                            </tr>
                          </tbody>
                        </table>
                      </div>
                    </li>
                  </ul>
                </div>
              </li>                           
            </ul>
          </fieldset>
        </div>
      </div>      
    </div>
  </div>
</template>

<script>
import axios from '@/utils/customAxios'
import { useUploadFile } from '@/compositions/useUploadFile'
import { inject, reactive, toRefs } from 'vue'
export default {
  name: 'SignOkSample',
  setup() {

    const state = reactive({
      params :{
        accessToken: '',
        documentId: '',
        documentData: '',
        templateId: '',
        templateTitle: '',
        approValUrl: '',
        templateList: [],
        templateInfo: [],
        documentList: [],
        documentInfo: [],
        documentStatusInfo: [],
      }
    })

    const {
      downloadFile
    } = useUploadFile()    

    const fetchAccessToken = () => {
      return axios({
        url: '/api/auth/sign-token',
        method: 'get',
        params: {}
      })
      .then(async res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          state.params.accessToken = resData.data.access_token
        } else {
          state.params.accessToken = '토큰 생성 실패'
        }
      })
    }

    const fetchTemplateList = () => {
      return axios({
        url: '/api/auth/sign-template-list',
        method: 'get',
        params: {
          accessToken : state.params.accessToken
        }
      })
      .then(async res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          state.params.templateList = resData.data.template
        } else {
          state.params.templateList = []
        }
      })
    }

    const fetchTemplateInfo = () => {
      return axios({
        url: '/api/auth/sign-template-info',
        method: 'get',
        params: {
          accessToken : state.params.accessToken,
          templateId : state.params.templateId
        }
      })
      .then(async res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          state.params.templateInfo = {
            title: resData.data.template.title,
            item : resData.data.template.item,
            signer : resData.data.template.signer
          }
        } else {
          state.params.templateList = []
        }
      })
    }

    const fetchDocumentCreate = () => {
      state.params.documentInfo = []
      return axios({
        url: '/api/auth/sign-doc-create',
        method: 'post',
        data: {
          signerCount: state.params.templateInfo.signer.length,
          templateTitle : state.params.templateInfo.title,
          templateId : state.params.templateId,
          accessToken : state.params.accessToken,
          docParams : state.params.templateInfo.item
        }
      })
      .then(async res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          state.params.documentInfo = resData.data.document
        }
      })
    }

    const fetchDocumentStastusList = () => {
      state.params.documentInfo = []
      return axios({
        url: '/api/auth/sign-doc-status-list',
        method: 'get',
        params: {
          accessToken : state.params.accessToken
        }
      })
      .then(async res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          state.params.documentList = resData.data.document
        }
      })
    }

    const fetchDocumentStatusInfo = () => {
      state.params.documentInfo = []
      return axios({
        url: '/api/auth/sign-doc-status',
        method: 'get',
        params: {
          documentId : state.params.documentId,
          accessToken : state.params.accessToken
        }
      })
      .then(async res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          state.params.documentStatusInfo = resData.data.document
        }
      })
    }

    const fetcApprovalUrl = () => {
      state.params.documentInfo = []
      return axios({
        url: '/api/auth/sign-approval-url',
        method: 'get',
        params: {
          accessToken : state.params.accessToken,
          documentId : state.params.documentId
        }
      })
      .then(async res => {
        const resData = res.data
        if (resData.code === 'C0000') {
          state.params.approValUrl = resData.data.document.approve.approval_url
        }
      })
    }

    const fetchDocumentDownload = () => {
      state.params.documentInfo = []
      return axios({
        url: '/api/auth/sign-doc-download',
        method: 'get',
        responseType: 'blob',
        params: {
          documentId : state.params.documentId,
          accessToken : state.params.accessToken
        }
      })
      .then(async res => {
        const resData = res.data
        // const blob = new Blob([resData],{type : 'application / octet-stream'})
        // const link = document.createElement('a');
        // link.href = window.URL.createObjectURL(blob);
        // link.download = 'download.pdf';
        // link.click();
        // window.URL.revokeObjectURL(link.href)

        // const blob = new Blob([resData]);
        // const url = window.URL.createObjectURL(blob)
        // const link = document.createElement('a')
        // link.href = url
        // link.setAttribute('download', "기술자료_요구서.pdf")
        // link.style.cssText = 'display:none'
        // document.body.appendChild(link)
        // link.click()
        // link.remove()
        // window.URL.revokeObjectURL(url)
      })
    }

    return {
      ...toRefs(state),
      downloadFile,
      fetcApprovalUrl,
      fetchAccessToken,
      fetchTemplateInfo,
      fetchTemplateList,
      fetchDocumentCreate,
      fetchDocumentDownload,
      fetchDocumentStatusInfo,
      fetchDocumentStastusList
    }
  },
}
</script>
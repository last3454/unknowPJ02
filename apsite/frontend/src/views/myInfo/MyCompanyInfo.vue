<template>
  <div class="inner">
    <div class="box">
      <div class="box-cont">
        <div class="cont-top">
            <h2 class="cont-top-title">{{ t('myinfo.label.company.title') }}</h2><!-- 소속회사 정보 -->
        </div>
        <div class="cont-mid">
          <div class="cont-form noBottom">
            <div class="cont-table">
              <table class="tb typeB tb-narrow">
                <caption class="hidden">{{ t('myinfo.label.company.title') }}</caption><!-- 소속회사 정보 -->
                <tbody v-if="companyInfo?.compNm">
                  <tr>
                    <th>{{ t('myinfo.label.company.compNmKo') }}</th><!-- 회사명 (국문) -->
                    <td>{{ companyInfo.compNm }}</td>
                  </tr>
                  <tr>
                    <th>{{ t('myinfo.label.company.compNmEn') }}</th><!-- 회사명 (영문) -->
                    <td>{{ companyInfo.compEnm }}</td>
                  </tr>
                  <tr>
                    <th>{{ t('myinfo.label.company.compNo') }}</th><!-- 사업자등록번호 -->
                    <td>
                      {{ companyInfo.compNum }}
                      <div v-if="filesMap['COMPANY01'] !== undefined && filesMap['COMPANY01'].length > 0" class="b-v-file-wrap">
                        <a href="#" v-for="item in filesMap['COMPANY01']" :key="'attach_' + item.seq" class="i-btn medium typeB gray" @click.prevent="downloadFile(item.seq, item.fileNm)">
                          <span class="down">{{ item.fileNm }}</span>
                        </a>
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <th>{{ t('myinfo.label.company.compTel') }}</th><!-- 대표전화 -->
                    <td>{{ commonUtils.phoneFormat(companyInfo.compPhone) }}</td>
                  </tr>
                  <tr>
                    <th>{{ t('myinfo.label.company.compRepresentNm') }}</th><!-- 대표자 명 -->
                    <td>{{ companyInfo.compRepresentNm ?? t('myinfo.msg.company.desc3') }}</td><!-- 대표자 명이 없습니다 -->
                  </tr>
                  <tr>
                    <th>{{ t('myinfo.label.company.compUrl') }}</th><!-- 회사 웹사이트 -->
                    <td>{{ companyInfo.compSite }}</td>
                  </tr>
                  <tr>
                    <th>{{ t('myinfo.label.company.compPostNo') }}</th><!-- 우편번호 -->
                    <td>{{ companyInfo.compZip }}</td>
                  </tr>
                  <tr>
                    <th>{{ t('myinfo.label.company.compAddrKo') }}</th><!-- 주소 (국문) -->
                    <td>{{ companyInfo.compAddr }}</td>
                  </tr>
                  <tr>
                    <th>{{ t('myinfo.label.company.compAddrEn') }}</th><!-- 주소 (영문) -->
                    <td>{{ companyInfo.compEnaddr }}</td>
                  </tr>

                  <tr>
                    <th>{{ t('myinfo.label.company.reg_charger') }}</th><!-- 담당 연구원 -->
                    <td>{{ companyInfo.compApUserNm }}</td>
                  </tr>
                  <tr>
                    <th>{{ t('myinfo.label.company.compDesc') }}</th><!-- 회사 특징 -->
                    <td>{{ companyInfo.compContent }}</td>
                  </tr>
                </tbody>
                <tbody v-else>
                  <tr>
                    <td style="text-align:center;">{{ t('myinfo.msg.company.desc1') }}</td><!-- 소속회사정보가 없습니다. -->
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
      <div class="btn-wrap center">
          <a href="#" v-if="companyInfo?.compNm" class="btn typeB" @click.prevent="goCompModify()">
            <span class="mod">{{ t('myinfo.label.company.btnModify') }}</span><!-- 수정 -->
          </a>
      </div>
    </div>
    <teleport to="#common-modal">
      <ap-popup>
        <component
          :is="popupContent"
          flag='C'
        />
      </ap-popup>
    </teleport>
  </div>
</template>

<script>
import { useStore } from 'vuex'
import { inject } from 'vue'
import { useMyInfo } from '@/compositions/useMyInfo'
import { useCompany } from '@/compositions/useCompany'
import { useUploadFile } from '@/compositions/useUploadFile'

export default {
  name: 'MyCompanyInfo',
  components: {},
  setup() {
    const t = inject('t')
    const commonUtils = inject('commonUtils')
    const store = useStore()
    const myInfo = store.getters.getMyInfo()

    const {
      errors,
      popupContent,
      goCompModify
    } = useMyInfo()

    const {
      fetchCompanyInfo,
      companyInfo
    } = useCompany()

    const {
      filesMap,
      fetchUploadFiles,
      downloadFile
    } = useUploadFile()

    const getCompInfo = async () => {
      const res = await fetchCompanyInfo()
      if (res.data) {
        const resData = res.data
        fetchUploadFiles({ targetKey: '' + resData.data.compSeq, uploadCd: 'COMPANY01' })
      }
    }

    // 로그인 여부 체크
    if (commonUtils.isNotEmpty(myInfo.userCd)) {
      getCompInfo()
    } else {
      router.push({ path: '/login' })
    }

    return {
      t,
      errors,
      companyInfo,
      goCompModify,
      popupContent,
      commonUtils,
      filesMap,
      downloadFile
    }
  }
}
</script>
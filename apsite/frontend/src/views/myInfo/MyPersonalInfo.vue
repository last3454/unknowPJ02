<template>
  <div class="inner">
    <div class="box">
      <div class="box-cont">
        <div class="cont-top">
          <h2 class="cont-top-title">{{ t('myinfo.label.profile.title') }}</h2><!-- 가입정보 -->
        </div>
        <div class="cont-mid">
          <div class="cont-form noBottom">
            <div class="cont-table">
              <table class="tb typeB tb-narrow">
                <caption class="hidden">{{ t('myinfo.label.profile.title') }}</caption><!-- 가입정보 -->
                <tbody v-if="personalInfo?.userNm">
                  <tr>
                    <th>{{ t('myinfo.label.profile.userNm') }}</th><!-- 성명 -->
                    <td>{{ personalInfo.userNm }}</td>
                  </tr>
                  <tr>
                    <th>{{ t('myinfo.label.profile.mobileNo') }}</th><!-- 휴대폰번호 -->
                    <td>{{ commonUtils.phoneFormat(personalInfo.mobileNo) }}</td>
                  </tr>
                  <tr>
                    <th>{{ t('myinfo.label.profile.loginId') }}</th><!-- 게정 아이디 -->
                    <td>{{ personalInfo.loginId }}</td>
                  </tr>
                  <tr>
                    <th>{{ t('myinfo.label.profile.email') }}</th><!-- 이메일 주소 -->
                    <td>{{ personalInfo.email ?? t('myinfo.msg.profile.desc8') }}</td><!-- 이메일이 없습니다 -->
                  </tr>
                  <tr>
                    <th>{{ t('myinfo.label.company.compNmKo') }}</th><!-- 회사명 (국문) -->
                    <td>{{ companyInfo.compNm }}</td>
                  </tr>
                  <tr>
                    <th>{{ t('myinfo.label.company.compNmEn') }}</th><!-- 회사명 (영문) -->
                    <td>{{ companyInfo.compEnm }}</td>
                  </tr>
                  <tr>
                    <th>{{ t('myinfo.label.profile.userCompDeptNm') }}</th><!-- 소속 -->
                    <td>{{ personalInfo.userCompDeptNm ?? t('myinfo.msg.profile.desc9') }}</td><!-- 소속이 없습니다 -->
                  </tr>
                </tbody>
                <tbody v-else>
                  <tr>
                    <td style="text-align:center;">{{ t('myinfo.msg.profile.desc1') }}</td><!-- 가입정보가 없습니다. -->
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
      <div class="btn-wrap center">
        <a href="#" v-if="personalInfo.userCd === myInfo.userCd && personalInfo?.userNm" class="btn typeB" @click.prevent="goModify()">
          <span class="mod">{{ t('myinfo.label.profile.btnModify') }}</span><!-- 수정 -->
        </a>
        <a href="#" v-if="personalInfo.userCd === myInfo.userCd && personalInfo?.userNm" class="btn typeB large gray" @click.prevent="fnPersonalRetire(personalInfo.userNm)">
          <span class="mod">{{ t('company.label.retire') }}</span><!-- 탈퇴 -->
        </a>
      </div>
    </div>
  </div>
</template>

<script>
import { inject } from 'vue'
import { useStore } from 'vuex'
import { useMyInfo } from '@/compositions/useMyInfo'

export default {
  name: 'MyPersonalInfo',
  components: {},
  setup () {
    const t = inject('t')
    const commonUtils = inject('commonUtils')
    const store = useStore()
    const myInfo = store.getters.getMyInfo()

    const {
      errors,
      personalInfo,
      companyInfo,
      fnPersonalRetire,
      fetchProfileInfo,
      goModify
    } = useMyInfo()

     // 로그인 여부 체크
    if (commonUtils.isNotEmpty(myInfo.userCd)) {
      fetchProfileInfo()
    } else {
      router.push({ path: '/login' })
    }

    return {
      t,
      errors,
      myInfo,
      personalInfo,
      companyInfo,
      fnPersonalRetire,
      commonUtils,
      goModify
    }
  }
}
</script>
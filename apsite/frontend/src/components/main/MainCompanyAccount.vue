<template>
  <div class="box main-box main-box-comList">
    <div class="box-cont" v-if="commonUtils.checkAuthOnly('SGG000005')">
      <div class="cont-top">
        <h3 class="cont-top-title">
          <i class="ct-comList"></i>{{ t('main.label.company.title') }}<!-- 회사 계정 리스트 -->
        </h3>
      </div>
      <div v-if="mainCompUserList?.length > 0" class="cont-mid">
        <ul class="main-cl-list">
          <li v-for="(user, idx) in mainCompUserList" :key="'compUser_' + idx">
            <dl class="clearfix">
              <dt>{{ user.userNm }}</dt>
              <dd class="phone">{{ commonUtils.maskingPhone(user.mobileNo) }}</dd>
              <dd class="email txt_ellipsis">{{ user.email }}</dd>
              <dd class="date">{{ user.regDtm }}</dd>
            </dl>
          </li>
        </ul>
      </div>
      <div v-else class="main-cl-list">
        <div class="noContent">
          {{ t('main.msg.communication.no_user') }}
        </div><!-- 계정정보가 없습니다. -->
      </div>
    </div>
    <div v-else class="box-cont noContent">
      {{ t('main.msg.communication.no_content') }}
    </div><!-- 컨텐츠 준비중입니다. -->
  </div>
</template>

<script>
import { inject } from 'vue'
import { useStore } from 'vuex'
import { useRoute, useRouter } from 'vue-router'
import { useMyInfo } from '@/compositions/useMyInfo'

export default {
  name: 'MainCompanyAccount',
  setup() {
    const t = inject('t')
    const router = useRouter()
    const commonUtils = inject('commonUtils')

    const store = useStore()
    const myInfo = store.getters.getMyInfo()

    const {
      mainCompUserList,
      fetchCompanyList
    } = useMyInfo()

    fetchCompanyList()
    

    return {
      t,
      myInfo,
      commonUtils,
      mainCompUserList
    }
  }
}

</script>

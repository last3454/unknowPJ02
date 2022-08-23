<template>
  <header id="header">
    <div class="h-inner h-top">
    <div class="h-lang-wrap">
      <div class="h-lang clearfix" :class="langCd">
      <button class="h-lang-kr" :class="langCd === 'ko' ? 'on' : ''" title="KOREAN" type="button" @click.prevent="fnChangeLangCd('ko')"><span>KR</span></button>
      <button class="h-lang-en" :class="langCd === 'en' ? 'on' : ''" title="ENGLISH" type="button" @click.prevent="fnChangeLangCd('en')"><span>EN</span></button>
      </div>
    </div>
    <div class="h-access clearfix">
      <p v-html="welcomeMsg"></p>
      <p class="h-access-info">{{ lastLoginMsg }}</p>
    </div>
    <div class="h-logout">
      <a href="#" :title="t('common.label.header_logout')" @click.prevent="fnLogout()">{{ t('common.label.header_logout') }}<!-- 로그아웃 --></a>
    </div>
    </div>
    <div class="h-inner h-bot">
    <div class="h-logo">
      <!-- <a href="../html/main.html" title="아모레퍼시픽 원료정보 시스템 Ingredient Information Infra system"> -->
      <router-link :to="{ path: '/main' }">
      <h1>
        <img class="w-view" src="@/assets/img/logo.svg" alt="아모레퍼시픽 원료정보 시스템 Ingredient Information Infra system">
        <img class="m-view" src="@/assets/img/logo.svg" alt="아모레퍼시픽 원료정보 시스템 Ingredient Information Infra system">
      </h1>
      </router-link>
    </div>
    <nav id="nav">
      <h2 class="hidden">GNB</h2>
      <ul class="nav-list">
      <li v-for="(vo, idx) in menuList" :key="'gnb_' + idx" class="nav-link" :class="vo.clsName">
        <a href="#"><span>{{ vo.name }}</span></a>
        <ul v-if="vo.children" class="nav-sub-list">
        <li v-for="(vo2, idx2) in vo.children" :key="'gnb_c_' + idx2">
          <router-link :to="{path: vo2.link}"><span>{{ vo2.name }}</span></router-link>
        </li>
        </ul>
      </li>
      </ul>
      <div class="nav-bg"></div>
    </nav>
    </div>
  </header>
</template>
<script>

import { inject, reactive } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'

export default {
  name: 'HeaderMenuTree',
  setup () {
    const t = inject('t')
    const store = useStore()
    const router = useRouter()
    const langCd = store.getters.getLangCd()
    const myInfo = store.getters.getMyInfo()
    const welcomeMsg = t('common.label.header_welcom_msg', { userNm: myInfo.userNm, loginId: myInfo.loginId })
    const lastLoginMsg = t('common.label.header_last_login', { dt: myInfo.lastLoginDt, ip: myInfo.ip })
    const fnChangeLangCd = (langCd) => {
      store.dispatch('changeLangCd', langCd)
      window.location.href = '/main'
    }

    const fnLogout = () => {
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

    const menuList = reactive([])
    const menuSetting = async () => {
      let list = store.getters.getGnb()
      if (myInfo.userCd && list.length === 0) {
        await store.dispatch('findGnb', myInfo.groups).then(menuRes => {
          if (menuRes.data.code === 'C0000') {
            list = menuRes.data.data
          }
        })      
      }
    
      const len = list.length;
      let seq = 0
      for(let i=0; i<len; i++){
        if(list[i].level === 2){
          menuList.push(
            {
              name: langCd === 'ko' ? `${list[i].menunm}` : `${list[i].menunmEn}`,
              clsName: `${list[i].router}`,
              link: '#',
              component: null,
              children: []
            }
          )
          seq++         
        } else {
          menuList[seq-1].children.push({
              name: langCd === 'ko' ? `${list[i].menunm}` : `${list[i].menunmEn}`
            , link: (list[i].pageid ? "/"+list[i].pageid.replace('-','/') : '')
          })
        }
      }
    } 
    menuSetting()

    return {
      t,
      welcomeMsg,
      lastLoginMsg,
      langCd,
      myInfo,
      menuList,
      fnLogout,
      fnChangeLangCd
    }
  }
}
</script>

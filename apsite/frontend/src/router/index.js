import { nextTick } from 'vue'
import { createRouter, createWebHistory, START_LOCATION } from "vue-router"
import { store } from '../store/index.js'
import commonUtils from '@/utils/commonUtils'

const routes = [
  {
    path: '/:pathMatch(.*)*',
    redirect: "/error/404"
  },
  {
    path: "/error",
    name: "error",
    component: () => import('@/views/Blank.vue'),
    meta: {
      layout: 'error',
      authAnyone: true,
      title: ''
    },    
    children: [
      {
        path: "404",
        name: "error-404",
        component: () => import('@/views/error/ErrorView404.vue')
      },
      {
        path: "500",
        name: "error-500",
        component: () => import('@/views/error/ErrorView500.vue')
      }      
    ]
  },  
  {
    path: '/',
    name: 'home',
    redirect: '/main',
    title: '메인'
  },
  {
    path: '/main',
    name: 'main',
    component: () => import('@/views/Main.vue'),
    meta: {
      title: '메인'
    }    
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('@/views/Login.vue'),
    meta: {
      layout: 'blank',
      authAnyone: true,
      title: '로그인'
    }
  },
  {
    path: '/join',
    name: 'join',
    component: () => import('@/views/join/JoinRegister.vue'),
    meta: {
      layout: 'blank',
      authAnyone: true,
      title: '회원가입'
    }
  },
  {
    path: '/nice-return',
    name: 'nice-return',
    component: () => import('@/views/NiceReturn.vue'),
    meta: {
      layout: 'blank',
      authAnyone: true
    }
  }
  // ,{
  //   path: '/signok-sample',
  //   name: 'signok-sample',
  //   component: () => import('@/views/SignOkSample.vue'),
  //   meta: {
  //     layout: 'blank',
  //     authAnyone: true
  //   }
  // }
  // ,{
  //   path: '/integration-data-sample',
  //   name: 'integration-data-sample',
  //   component: () => import('@/views/IntegrationDataSample.vue'),
  //   meta: {
  //     authAnyone: true
  //   }
  // }     
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to,from) => {
  //에러 페이지에 왔는데 로그인이 안되어 있는 상태이면
  //로그인 페이지로 보낸다.
  if(to.fullPath.indexOf('error') > -1){
    const myInfo = store.getters.getMyInfo()
    if(!myInfo.userCd){
      router.push({ path: '/login' })
    }
  } else if(to.fullPath.indexOf('companym') > -1 || to.fullPath.indexOf('myinfop') > -1 || to.fullPath.indexOf('myinfoc') > -1){
    //보안성 추가
    commonUtils.protectionInfo()
  }else{
    //보안성 해제
    commonUtils.noProtectionInfo()
  }
  const title = to.meta.title === undefined ? '' : '원료정보시스템>' + to.meta.title;
  nextTick(() => {
    document.title = title;
  });  
})

router.onError((error) => {
  console.log('router error 발생', error)
})
export default router
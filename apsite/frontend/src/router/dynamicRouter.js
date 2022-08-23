
import router from '@/router'
import { store } from '../store/index.js'

export default {
  init () {
    this.setDefault()
  },
  setDefault () {
    const userCd = store.getters.getMyInfo().userCd
    if (userCd) {
      let routerList = JSON.parse(sessionStorage.getItem("routerList"))
      if (routerList) {
        //기존 라우터 그리고
        this.gridRouter(routerList)        
      }
      //새로 호출되면서 가져오는 라우터로 엎는다.
      store.dispatch('findRouter', store.getters.getMyInfo().groups).then(async resData => {
        if (resData.data.code === 'C0000') {
          this.gridRouter(resData.data.data)
        }
      })      
    }
  },
  gridRouter(routerList) {
    const routerMap = []
    const list = routerList
    //기존 @ shymbol은 롤업 제한 때문에 먹지 않는 상황
    //동적 ../ 주소는 빌드 할때 무슨 이유에서인지 정상 작동하지 않아
    //vite 기능 중 views 폴더안에 존재하는 모든 모듈 가져오는 glob 기능 사용 => vue 가져오기
    const vues = import.meta.glob('../views/**/*.vue')
    for(let i=0; i<list.length; i++){
      if(list[i].level === 3){
        routerMap.push(
          {
            path: `/${list[i].router}`,
            name: `${list[i].router}`,
            redirect: `/${list[i].router}/list`,
            component: () => import('@/views/Blank.vue'),
            children: [],
            meta : { title: `${list[i].menunm}`}
          }
        )
        const subList = list[i].subList
        if(subList){
          subList.forEach(obj=>{
            if(obj.pageid.indexOf('-') > - 1){
              routerMap[(routerMap.length-1)].children.push({
                path: `${obj.pageid.split('-')[1]}`  ,
                name: `${obj.pageid}`,
                component: vues[`../views/${obj.pageUrl}.vue`],
                meta : {title: `${obj.menunm}`}
              })
            }
          })
        }
      }
    }
    routerMap.forEach(obj => {
      if(router.hasRoute(obj.name)){
        router.removeRoute(obj.name)
      }
      router.addRoute(obj)
    })
  }
}
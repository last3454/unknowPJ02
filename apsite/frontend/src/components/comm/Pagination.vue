<template>
  <div class="pagination" v-if="pageInfo">
    <button @click="fnPageNum(1)" class="first" title="첫 페이지로" >첫 페이지로</button>
    <button @click="fnPageNum((pageInfo.nowPageNo-1))" class="prev" title="이전 페이지로" >이전 페이지로</button>
    <ul class="pagination-list">
      <template v-for="idx in pagingCnt()" :key="idx">
        <li :class="[idx === pageInfo.nowPageNo ? 'active' : '']" @click="fnPageNum(idx)">
          <a>{{idx}}</a>
        </li>
      </template>
    </ul>
    <button @click="fnPageNum((pageInfo.nowPageNo+1))" class="next" title="다음 페이지로">다음 페이지로</button>
    <button @click="fnPageNum(pageInfo.totalPageCnt)" class="last" title="마지막 페이지로">마지막 페이지로</button>
  </div>
</template>

<script>
import { useActions } from 'vuex-composition-helpers'

export default {
  name: 'PaginationView',
  props: {
    pageInfo: {
      type: Object,
      default: null
    }
  },
  setup(props, { emit }) {
    const { openAsyncAlert } = useActions(['openAsyncAlert'])

    function pagingCnt(){
      const list = []
      const startPage = props.pageInfo.startPage
      const endPage = props.pageInfo.endPage
      for (let i = startPage; i <= endPage; i ++){
        list.push(i);
      }
      return list;
    }

    function fnPageNum (pageNum){
      const totalPageCnt = props.pageInfo.totalPageCnt
      if(pageNum < 1){
        openAsyncAlert({ message: '앞 페이지는 없습니다.' })
        return
      }

      if(pageNum > totalPageCnt){
        openAsyncAlert({ message: '뒷 페이지는 없습니다.' })
        return
      }

      emit('goPageNum',pageNum);
    }

    return {
      pagingCnt,
      fnPageNum
    }
  },
}
</script>
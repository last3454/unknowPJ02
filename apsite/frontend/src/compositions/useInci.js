import { reactive, computed, toRefs } from 'vue'
import axios from '@/utils/customAxios'
import router from '../router'
import { useActions } from 'vuex-composition-helpers'
import { inject } from 'vue'

export const useInci = () => {

    const fetchMaterial = () => {
      alert('원료 불러오기')
    }

    const fetchContent = () => {
      alert('내용물 불러오기')
    }

    const fnWidthScroll = () =>{
      const contScrollX = document.querySelectorAll('.cont-scrollX');
      if(contScrollX.length > 0) {
        for(let i=0;i<contScrollX.length;i++) {
          let scrollCont = contScrollX[i],
              scrollBar = scrollCont.querySelector('.cont-scrollX-bar'),
              scrollWrap = scrollCont.querySelector('.cont-scrollX-wrap'),
              scrollArea = scrollCont.querySelector('.cont-scrollX-area'),
              scrollWidth = parseInt(scrollCont.getAttribute('data-width'));

          scrollBar.querySelector('.bar').style.width = scrollWidth + 'px';
          scrollArea.style.width = scrollWidth + 'px';

          scrollBar.addEventListener('scroll', function(){ scrollWrap.scrollLeft = scrollBar.scrollLeft; });
          scrollWrap.addEventListener('scroll', function(){ scrollBar.scrollLeft = scrollWrap.scrollLeft; });
          
          function scrollHorizontally(e) {
              e = window.event || e;
              var delta = Math.max(-1, Math.min(1, (e.wheelDelta || -e.detail)));
              scrollWrap.scrollLeft -= (delta * 60);
              e.preventDefault();
          }
          if (scrollWrap.addEventListener) {
              scrollWrap.addEventListener('mousewheel', scrollHorizontally, false);
              scrollWrap.addEventListener('DOMMouseScroll', scrollHorizontally, false);
          } else {
              scrollWrap.attachEvent('onmousewheel', scrollHorizontally);
          }
        }
      }
    }

    return {
      fetchMaterial,
      fetchContent,
      fnWidthScroll
    }
  }
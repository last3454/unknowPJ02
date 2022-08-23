<template>
  <div class="box blue main-box main-box-notice">
    <div class="box-cont">
      <div class="main-notice-left">
        <h3 class="cont-top-title white"><i class="ct-notice"></i>{{ t('main.label.notice.title') }}<!-- 공지사항 --></h3>
      </div>
      <div class="main-notice-right">
        <div class="swiper main-notice-slide">
          <swiper
            :modules="swiperModules"
            :direction="'vertical'"
            :slides-per-view="3"
            :centered-slides="true"
            :loop="true"
            :space-between="12"
            :autoplay="{ delay: 5000, disableOnInteraction: false }"
            @swiper="onSwiper"
            @slideChange="onSlideChange"
            class="swiper-wrapper"
          >
            <swiper-slide v-for="dto in mainList" :key="dto.seq" class="swiper-slide">
              <a href="#" v-if="dto?.ytbUrl" @click.prevent="fnYtbPop(dto.ytbUrl, dto.title)">
                <p class="main-notice-tit">{{ dto.title }} <i class="ico-video"></i></p>
                <span class="main-notice-date">{{ commonUtils.convertDt(dto.regDtm) }}</span>
              </a>
              <router-link v-else :to="{ path: '/notice/view', query: { seq: dto.seq } }">
                <p class="main-notice-tit">{{ dto.title }}</p>
                <span class="main-notice-date">{{ commonUtils.convertDt(dto.regDtm) }}</span>
              </router-link>
            </swiper-slide>
          </swiper>
        </div>
      </div>
    </div>
    <teleport to="#common-modal" v-if="popupContent">
      <ap-popup>
        <component
          :is="popupContent"
          :popParams="popParams"
        />
      </ap-popup>
    </teleport>
  </div>
</template>

<script>
import { Autoplay } from 'swiper'
import { Swiper, SwiperSlide } from 'swiper/vue'
import { defineAsyncComponent, inject, reactive } from 'vue'
import { useNotice } from '@/compositions/useNotice'
import 'swiper/css'

export default {
  name: 'MainNotice',
  components: {
    Swiper,
    SwiperSlide,
    ApPopup: defineAsyncComponent(() => import('@/components/comm/ApPopup.vue')),
    NoticeYtbPop: defineAsyncComponent(() => import('@/components/popup/NoticeYtbPop.vue'))
  },
  setup() {
    // console.log('init - MainNotice')
    const t = inject('t')
    const commonUtils = inject('commonUtils')
    const swiperModules = reactive([Autoplay])
    const {
        mainList,
        fetchMainBoards,
        fnYtbPop,
        popParams,
        popupContent
        } = useNotice()

    const onSwiper = (swiper) => {
      // console.log(swiper);
    }
    const onSlideChange = () => {
      // console.log('slide change');
    }

    fetchMainBoards()
    return {
      t,
      popParams,
      commonUtils,
      mainList,
      swiperModules,
      popupContent,
      fnYtbPop,
      onSwiper,
      onSlideChange
    }
  }
}
</script>

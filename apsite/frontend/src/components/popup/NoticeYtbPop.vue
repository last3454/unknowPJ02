<template>
  <div class="modal-content" style="height: 500px;">
    <div class="modal-tit-wrap noBottom">
      <p class="modal-tit">{{ props.popParams.title }}</p>
    </div>
    <div class="modal-cont-video-wrap">
      <div v-if="props?.popParams" class="modal-video-wrap">
        <iframe
          width="750"
          height="400"
          :src="props.popParams.popUrl"
          title="YouTube video player"
          frameborder="0"
          allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
          allowfullscreen
        />
      </div>
    </div>
    <div class="modal-close-btn">
      <a href="#" @click.prevent="onClose()" title="팝업창 닫기">{{ t('board.label.sourcing.closePop') }}</a><!-- 팝업창 닫기 -->
    </div>
  </div>
</template>

<script>
import { inject } from 'vue'
import { useActions } from 'vuex-composition-helpers'

export default {
  name: 'NoticeYtbPop',
  props: {
    popParams: Object
  },
  setup(props) {
    const t = inject('t')
    const { closeAsyncPopup } = useActions(['closeAsyncPopup'])

    // 닫기 버튼
    const onClose = () => {
      closeAsyncPopup()
    }

    return {
      t,
      props,
      onClose
    }
  },
}
</script>

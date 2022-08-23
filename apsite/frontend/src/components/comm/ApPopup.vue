<template>
  <div :class="['modal', popupInfo.isOpen ? 'open' : '']">
    <div :class="popupInfo.noLimitH ? 'modal-wrap-no-limit-h' : 'modal-wrap'" :style="[popupInfo.minWidth ? {'min-width':  `${popupInfo.minWidth}px`} : '']">
      <slot />
    </div>
  </div>
</template>

<script>
import { computed } from 'vue'
import { useStore } from 'vuex-composition-helpers'

export default {
  name: 'ApPopup',
  setup() {
    const store = useStore()
    const popupInfo = computed(() => store.getters.getPopupInfo())

    return {
      popupInfo
    }
  }
}
</script>

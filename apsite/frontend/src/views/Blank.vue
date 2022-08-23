<template>
  <div class="inner">
    <router-view></router-view>
    <teleport to="#common-modal">
      <ap-alert ref="modal"></ap-alert>
    </teleport>
  </div>
</template>
<script>

export default {
  name: 'BlankView',
  setup() {
    return {
    }
  },
}
</script>
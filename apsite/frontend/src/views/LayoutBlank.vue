<template>
  <div id="wrap">
    <router-view></router-view>
    <footer id="footer">
      <footer-template></footer-template>
    </footer>
    <teleport to="#common-modal">
      <ap-alert ref="modal"></ap-alert>
    </teleport>
  </div>
</template>

<script>
import FooterTemplate from '@/components/comm/FooterTemplate.vue'

export default {
  name: 'LayoutBlank',
  components: {
    FooterTemplate: FooterTemplate
  },
  setup() {
    return {}
  },
}
</script>

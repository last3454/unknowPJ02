<template>
  <div id="wrap">
    <header-menu-tree></header-menu-tree>
    <main id="main_wht">
      <section id="content_wht">
        <router-view></router-view>
      </section>
    </main>
    <footer id="footer">
      <footer-template></footer-template>
    </footer>
    <teleport to="#common-modal">
      <ap-alert ref="modal"></ap-alert>
    </teleport>
  </div>
</template>

<script>
import HeaderMenuTree from '@/components/comm/HeaderMenuTree.vue'
import FooterTemplate from '@/components/comm/FooterTemplate.vue'

export default {
  name: 'LayoutError',
  components: {
    HeaderMenuTree: HeaderMenuTree,
    FooterTemplate: FooterTemplate
  },
  setup() {
    return {}
  },
}
</script>

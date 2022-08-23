<template>
  <div id="wrap">
    <div id="top-btn" class="on">
      <a href="#top" title="맨 위로">맨 위로</a>
    </div>
    <header-menu-tree></header-menu-tree>
    <main id="main">
      <section id="content">
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
  name: 'LayoutType01',
  components: {
    HeaderMenuTree: HeaderMenuTree,
    FooterTemplate: FooterTemplate
  },
  setup() {
    return {}
  },
}
</script>

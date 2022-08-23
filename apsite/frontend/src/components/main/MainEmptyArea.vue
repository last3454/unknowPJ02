<template>
  <div class="box main-box main-box-comList">
    <div class="box-cont noContent">
      {{ t('main.msg.communication.no_content') }}
    </div><!-- 컨텐츠 준비중입니다. -->
  </div>
</template>

<script>
import { inject } from 'vue'

export default {
  name: 'MainEmptyArea',
  setup() {
    const t = inject('t')

    return {
      t
    }
  }
}
</script>

<style>

</style>
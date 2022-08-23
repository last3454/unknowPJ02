<template>
  <div class="loading_overlay" v-if="loading">
    <div class="ld-loading">
      <div></div>
      <div></div>
      <div></div>
    </div>
  </div>
</template>

<script>
export default {
  name:'SpinnerView',
  props: {
    loading: {
      type: Boolean,
      required: true,
    },
  },
}
</script>

<style>
.loading_overlay {
  position: fixed;
  top:0px;
  left:0px;
  width:100%;
  height:100%;
  z-index:1000;
  background-color: rgb(0, 0, 0, 0.2);
}
.ld-loading {
  display: inline-block;
  position: absolute;
  width: 74px;
  height: 74px;
  top: 48%;
  left: 48%;
}
.ld-loading div {
  display: inline-block;
  position: absolute;
  left: 6px;
  width: 20px;
  background: #094B85;
  animation: ld-loading 1.2s cubic-bezier(0, 0.5, 0.5, 1) infinite;
}
.ld-loading div:nth-child(1) {
  left: 6px;
  animation-delay: -0.24s;
}
.ld-loading div:nth-child(2) {
  left: 30px;
  animation-delay: -0.12s;
}
.ld-loading div:nth-child(3) {
  left: 54px;
  animation-delay: 0;
}
@keyframes ld-loading {
  0% {
    top: 6px;
    height: 51px;
  }
  50%, 100% {
    top: 19px;
    height: 26px;
  }
}
</style>
<template>
  <div class="box main-box main-box-igList">
    <div class="box-cont">
      <div class="main-ig-left">
        <h3 class="cont-top-title"><i class="ct-igList"></i>{{ t('main.label.ingredient.title') }}<!-- MY 원료리스트 --></h3>
        <div class="btn-wrap left">
          <a href="#" @click.prevent="goRegister()" class="btn typeB sky medium"><span class="next">{{ t('main.label.ingredient.reg') }}<!-- 신규등록 --></span></a>
        </div>
      </div>
      <div
        v-if="list && list.length > 0"
        class="main-ig-right"
      >
        <ul>
          <li v-for="(vo, idx) in list" :key="idx">
            <a
              href="#"
              @click.prevent="goDetail(vo.recordCd)"
            >
              <figure>
                <img
                  v-if="vo.imgSrc"
                  :src="vo.imgSrc"
                  :alt="vo.rawNmKo"
                  @error="fnImageError($event)"
                >
                <img
                  v-else
                  class="no-image"
                  :alt="vo.rawNmKo"
                >
              </figure>
              <div class="main-ig-desc">
                <span>{{ vo.statusNm }}</span>
                <p>{{ vo.rawNmKo }}</p>
              </div>
            </a>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>

import { inject } from 'vue'
import { useIngredient } from '@/compositions/useIngredient'

export default {
  name: 'MainIngredient',
  setup () {
    const t = inject('t')
    const commonUtils = inject('commonUtils')

    const {
      list,
      fetchMainIngredientList,
      fnLinkReg,
      fnDetail
    } = useIngredient()

     const goDetail = (recordCd) => {
      fnDetail(recordCd)
    }

    const goRegister = () => {
      fnLinkReg()
    }

    const fnImageError = (e) => {
      const img = e.target
      img.classList.add('no-image')
    }

    const init = async () => {
      await fetchMainIngredientList()
      if(list.value){
        list.value.forEach(c => {
          if (commonUtils.isNotEmpty(c.fileBytea)) {
            c.imgSrc = 'data:image/' + c.fileExt + ';base64,' + c.fileBytea
          }
        })
      }
    }

    init()

    return {
      t,
      list,
      goDetail,
      goRegister,
      fnImageError
    }
  }
}
</script>

<style>

</style>

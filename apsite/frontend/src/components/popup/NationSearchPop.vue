<template>
  <div class="modal-content">
    <div class="modal-tit-wrap">
      <p class="modal-tit">{{ t('ingredient.label.select_country') }}</p>
    </div>
    <div class="modal-cont-wrap multiSlt-wrap">
      <div class="cont-table-top">
        <ul class="cont-input-list popup_search_input">
          <li class="cont-input-wrap">
            <div class="cont-input-wrap__flex">
              <input type="text" v-model="keyword" @keypress.enter="fnSearch()">
              <a class="i-btn typeA large" @click.prevent="fnSearch()">{{ t('common.label.search2') }}</a>
            </div>
          </li>
        </ul>
        <div class="multiSlt-view clearfix">
          <span v-for="(vo, idx) in chkList" :key="'checked_' + idx">
            {{ vo.codeNm }}
            <button type="button" @click="removeChkNation(vo.code)">{{ t('common.label.delete') }}</button>
          </span>
        </div>
      </div>
      <div class="cont-table cont-table-scrollY">
        <ul class="multiSlt-list clearfix" v-if="ctrList">
          <template  v-for="(vo, idx) in ctrList" :key="'nation_' + idx">
            <li v-show="vo.isShow === 'Y'">
              <div class="input-check-fr">
                <input
                  type="checkbox"
                  name="chk_nation"
                  class="chk typeB"
                  :id="'chk_nation_' + idx"
                  :true-value="vo.code"
                  false-value=""
                  @click="chkNation($event, vo)"
                  :disabled="chkDisabled"
                  :checked="chkList.filter(c => c.code === vo.code).length > 0"
                >
                <label :for="'chk_nation_' + idx">{{ vo.codeNm }}</label>
              </div>
          </li>
          </template>
        </ul>
      </div>
      <div class="btn-wrap">
        <a class="btn typeB gray" @click.prevent="fnClose()"><span>{{ t('common.label.cancel') }}</span></a>
        <a class="btn typeB" @click.prevent="selectComplete()"><span class="complete">{{ t('common.label.alert_ok') }}</span></a>
      </div>
    </div>
  </div>
</template>

<script>
import { inject, ref, onMounted, nextTick } from 'vue'
import { useCode } from '@/compositions/useCode'
import { useActions } from 'vuex-composition-helpers'

export default {
  name: 'NationSearchPop',
  props: {
    popParams: {
      type: Object,
      default: () => {
        return {
          isMulti: 'Y',
          checkedList: [],
          idx: 0
        }
      }
    }
  },
  setup (props, { emit }) {
    const t = inject('t')
    const commonUtils = inject('commonUtils')
    const { closeAsyncPopup, openAsyncAlert } = useActions(['closeAsyncPopup', 'openAsyncAlert'])
    let chkList = ref([])
    let ctrList = ref([])
    let keyword = ref(null)
    const chkDisabled = ref(false)

    const {
      fetchTiumCodeGroupMaps,
      codeGroupMaps
    } = useCode()

    const fnClose = () => {
      closeAsyncPopup({ message: '닫기' })
    }

    const chkNation = async (e, vo) => {
      if (e.target.checked && props.popParams.isMulti !== 'Y' && chkList.value.length > 0) {
        e.target.checked = false
        await openAsyncAlert({ message: t('common.msg.select_only') })
        return
      }

      if (e.target.checked) {
        chkList.value.push(vo)
      } else {
        removeChkNation(vo.code)
      }
    }

    const removeChkNation = (code) => {
      chkList.value = chkList.value.filter(vo => vo.code !== code)

      const chkNation = document.getElementsByName('chk_nation')

      for (const c of chkNation) {
        if (c.value === code) {
          c.checked = false
          break
        }
      }
    }

    const selectComplete = () => {
      const obj = {
        idx: props.popParams.idx
      }
      if (props.popParams.isMulti !== 'Y') {
        obj.returnObj = chkList.value[0]
      } else {
        obj.returnObj = chkList.value
      }

      emit('selectFunc', obj)
      fnClose()
    }

    const init = async () => {
      await fetchTiumCodeGroupMaps(['ORIGIN_AREA'])
      ctrList.value = codeGroupMaps.value['ORIGIN_AREA']
      const tempList = []
      codeGroupMaps.value['ORIGIN_AREA'].forEach((c, i) => {
        c.isShow = 'Y'
        for (const code of props.popParams.checkedList) {
          if (c.code === code) {
            tempList.push(c)
            break
          }
        }
      })

      chkList.value = tempList
    }

    const fnSearch = () => {
      if (commonUtils.isNotEmpty(keyword.value)) {
        ctrList.value.forEach(vo => {
          if (vo.codeNm.indexOf(keyword.value) > -1 || vo.codeNmEn.indexOf(keyword.value) > -1) {
            vo.isShow = 'Y'
          } else {
            vo.isShow = 'N'
          }
        })
      } else {
        ctrList.value.forEach(vo => {
          vo.isShow = 'Y'
        })
      }
      //
    }

    onMounted(async () => {
      await nextTick()
      await init()
    })

    return {
      t,
      props,
      keyword,
      codeGroupMaps,
      chkList,
      chkDisabled,
      fnClose,
      selectComplete,
      chkNation,
      removeChkNation,
      ctrList,
      fnSearch
    }
  }
}
</script>

<style scoped>
  .cont-table-scrollY { height: 250px; }
  .popup_search_input {margin-bottom: 10px; margin-top: -20px;}
</style>
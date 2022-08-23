<template>
  <div class="modal-content">
    <div class="modal-tit-wrap">
      <p class="modal-tit">{{ t('board.label.sourcing.selectComp') }}</p><!-- 원료사 선택 -->
    </div>
    <div class="modal-cont-wrap multiSlt-wrap">
      <div class="cont-table-top">
        <ul class="cont-input-list popup_search_input">
          <li class="cont-input-wrap">
            <div class="cont-input-wrap__flex">
              <input type="text" v-model="keyword" @keypress.enter="fnSearch()" :placeholder="t('board.msg.sourcing.desc21')"><!-- 원료사명을 입력해주십시오. -->
              <a class="i-btn typeA large" @click.prevent="fnSearch()">{{ t('common.label.search2') }}</a>
            </div>
          </li>
        </ul>
        <div class="multiSlt-view clearfix">
          <span v-for="(vo, idx) in chkList" :key="'checked_' + idx">
            {{ vo.compNm }}
            <button type="button" @click="removeChkComp(vo.compSeq)">{{ t('common.label.delete') }}</button>
          </span>
        </div>
      </div>
      <div class="cont-table cont-table-scrollY">
        <ul class="multiSlt-list clearfix" v-if="compList?.length">
          <template v-for="(vo, idx) in compList" :key="'comp_' + idx">
            <li v-show="vo.isShow === 'Y'">
              <div class="input-check-fr">
                <input
                  type="checkbox"
                  name="chk_comp"
                  class="chk typeB"
                  :id="'chk_comp_' + idx"
                  :true-value="vo.compSeq"
                  false-value=""
                  @click="chkComp($event, vo)"
                  :disabled="chkDisabled"
                  :checked="chkList.filter(c => c.compSeq === vo.compSeq).length > 0"
                >
                <label :for="'chk_comp_' + idx">{{ vo.compNm }}</label>
              </div>
          </li>
          </template>
        </ul>
      </div>
      <div class="btn-wrap">
        <a class="btn typeB gray" @click.prevent="fnClose()"><span>{{ t('common.label.cancel') }}</span></a><!-- 취소 -->
        <a class="btn typeB" @click.prevent="selectComplete()"><span class="complete">{{ t('common.label.alert_ok') }}</span></a><!-- 확인 -->
      </div>
    </div>
  </div>
</template>

<script>
import { inject, ref, onMounted, nextTick } from 'vue'
import { useActions } from 'vuex-composition-helpers'
import { useSourcing } from '@/compositions/useSourcing'

export default {
  name: 'CompSearchPop',
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
    let compList = ref([])
    let keyword = ref(null)
    const chkDisabled = ref(false)

    const {
        fnSearchComp
      } = useSourcing()

    const fnClose = () => {
      closeAsyncPopup({ emitValue: chkList.value })
    }

    const chkComp = async (e, vo) => {
      if (e.target.checked && props.popParams.isMulti !== 'Y' && chkList.value.length > 0) {
        e.target.checked = false
        await openAsyncAlert({ message: t('common.msg.select_only') }) // 하나만 선택해 주십시오.
        return
      }

      if (e.target.checked) {
        chkList.value.push(vo)
      } else {
        removeChkComp(vo.compSeq)
      }
    }

    const removeChkComp = (compSeq) => {
      chkList.value = chkList.value.filter(vo => vo.compSeq !== compSeq)

      const chkComp = document.getElementsByName('chk_comp')

      for (const c of chkComp) {
        if (c.value === compSeq) {
          c.checked = false
          break
        }
      }
    }

    const selectComplete = () => {
      const obj = {}
      if (props.popParams.isMulti !== 'Y') {
        obj.returnObj = chkList.value[0]
      } else {
        obj.returnObj = chkList.value
      }

      emit('selectFunc', obj)
      fnClose()
    }

    // 시작 값
    const init = async () => {
      await fnSearchComp().then((res) => {
        compList.value = res.data.list
      })
      const tempList = []

      compList.value.forEach((c, i) => {
        c.isShow = 'Y'

        if (!props.popParams.rawRegister) {
          props.popParams.checkedList.forEach(item => {
            if (c.compSeq === item.compSeq) {
              tempList.push(c)
            }
          })
        } else {
          props.popParams.checkedList.forEach(item => {
            if (c.compCd === item) {
              tempList.push(c)
            }
          })
        }
      })

      chkList.value = tempList
    }

    const fnSearch = () => {
      if (commonUtils.isNotEmpty(keyword.value)) {
        compList.value.forEach(vo => {
          if (vo.compNm.indexOf(keyword.value) > -1 || vo.compEnm.indexOf(keyword.value) > -1) {
            vo.isShow = 'Y'
          } else {
            vo.isShow = 'N'
          }
        })
      } else {
        compList.value.forEach(vo => {
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
      chkList,
      chkDisabled,
      fnClose,
      selectComplete,
      chkComp,
      removeChkComp,
      compList,
      fnSearch
    }
  }
}
</script>

<style scoped>
  .cont-table-scrollY { height: 250px; }
  .popup_search_input {margin-bottom: 10px; margin-top: -20px;}
</style>
<template>
  <div class="modal msg open" v-show="alertInfo.isOpen">
    <div class="modal-wrap">
      <div class="modal-content">
        <div class="modal-cont-wrap">
          <div class="modal-cont-msg">
            <p class="tit" v-html="alertInfo.message"></p>
          </div>
          <div v-if="alertInfo.type === 'ALERT'" class="btn-wrap">
            <a href="#" @click.prevent="onOk()" class="btn typeB"><span>{{ alertInfo.alertOk !== undefined ? alertInfo.alertOk : alertOk }}</span></a>
          </div>
          <div v-else-if="alertInfo.type === 'CONFIRM'" class="btn-wrap">
            <a href="#" @click.prevent="onCancel()" class="btn typeB gray"><span>{{ alertInfo.confirmCancel !== undefined ? alertInfo.confirmCancel : confirmCancel }}</span></a>
            <a href="#" @click.prevent="onConfirm()" class="btn typeB"><span>{{ alertInfo.confirmOk !== undefined ? alertInfo.confirmOk : confirmOk }}</span></a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { computed, ref, inject } from 'vue'
import { useStore, useActions } from 'vuex-composition-helpers'

export default {
  name: 'ApAlert',
  setup() {
    const t = inject('t')
    const store = useStore()
    const alertInfo = computed(() => store.getters.getAlertInfo())
    const { closeAsyncConfirm, closeAsyncAlert } = useActions(['closeAsyncConfirm', 'closeAsyncAlert'])
    const alertOk = ref(t('common.label.alert_ok'))
    const confirmOk = ref(t('common.label.confirm_ok'))
    const confirmCancel = ref(t('common.label.confirm_cancel'))

    const onConfirm = () => {
      closeAsyncConfirm('OK')
    }
    const onCancel = () => {
      closeAsyncConfirm('CANCEL')
    }
    const onOk = () => {
      closeAsyncAlert()
    }

    return {
      t,
      alertInfo,
      alertOk,
      confirmOk,
      confirmCancel,
      onOk,
      onCancel,
      onConfirm
    }
  }
}
</script>

<template>
  <div class="cont-input-wrap__flex start">
    <ap-date-picker
      v-model:date="calData.date"
      :with-time="true"
      :style-class="styleClass"
      :read-only="readOnly"
    />
    <ap-time-picker
      v-model:date="calData.date"
      :with-margin-left="20"
      :style-class="styleClass"
      :read-only="readOnly"
    />
    <template v-if="withEndTime">
      <span style="margin: 0 7px 0 7px;">~</span>
      <ap-time-picker
        v-model:date="calEndData.date"
        :style-class="styleClass"
        :read-only="readOnly"
      />
    </template>
  </div>
</template>

<script>
import { reactive, watch, inject } from 'vue'

import ApDatePicker from '@/components/comm/ApDatePicker.vue'
import ApTimePicker from '@/components/comm/ApTimePicker.vue'

export default {
  name: 'ApDateTimePicker',
  components: {
    ApDatePicker,
    ApTimePicker
  },
  props: {
    date: String,
    withEndTime: { type: Boolean, default: false },
    smnEndDate: { type: String, default: '' },
    styleClass: { type: String, default: '' },
    readOnly: { type: Boolean, default: false }
  },
  emits: ['update:date', 'selectFunc'],
  setup (props, context) {
    const commonUtils = inject('commonUtils')

    const calData = reactive({
      date: '',
      dateString: ''
    })
    const calEndData = reactive({
      date: '',
      dateString: ''
    })

    watch(() => props.date, (newVal, oldVal) => {
      if (calData.dateString !== newVal) {
        calData.date = newVal
        calData.dateString = newVal
      }
    })
    watch(() => props.smnEndDate, (newVal, oldVal) => {
      if (calEndData.dateString !== newVal) {
        calEndData.date = newVal
        calEndData.dateString = newVal
      }
    })

    watch(() => calData.date, (newVal, oldVal) => {
      const date = newVal

      if (props.date !== date) {
        context.emit('update:date', date)
        
        if (props.withEndTime) {
          let tmpDate = null
          if (!oldVal) {
            tmpDate = commonUtils.convertStrToDateTime(date)
            tmpDate.setHours(tmpDate.getHours() + 1)
          } else {
            tmpDate = commonUtils.convertStrToDateTime([newVal.split(' ')[0], calEndData.date.split(' ')[1]].join(' '))
          }
          context.emit('selectFunc', commonUtils.convertDateTimeToStr(tmpDate))
        }
      }
    })
    watch(() => calEndData.date, (newVal, oldVal) => {
      const date = newVal

      if (props.withEndTime && props.smnEndDate !== date) {
        context.emit('selectFunc', date)
      }
    })

    return {
      calData,
      calEndData
    }
  }
}
</script>
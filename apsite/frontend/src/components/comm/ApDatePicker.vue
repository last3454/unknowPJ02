<template>
  <date-picker mode="date" v-model="calData.date" :popover="{ visibility: 'click' }">
    <template v-slot="{ inputValue, inputEvents }">
      <div class="datepicker-box">
        <input
          :class="['flatpickr-input', styleClass]"
          :value="inputValue"
          v-on="inputEvents"
          @input="input"
          :readonly="readOnly"
        />
      </div>
    </template>
  </date-picker>
</template>

<script>
import { reactive, inject, watch } from 'vue'

export default {
  name: 'ApDatePicker',
  props: {
    date: String,
    withTime: { type: Boolean, default: false },
    styleClass: { type: String, default: '' },
    readOnly: { type: Boolean, default: false }
  },
  emits: ['update:date'],
  setup (props, context) {
    const commonUtils = inject('commonUtils')
    const calData = reactive({
      date: null,
      dateString: ''
    })

    if (props.date) {
      calData.date = props.withTime ? commonUtils.convertStrToDateTime(props.date) : commonUtils.convertStrToDate(props.date)
    }

    watch(() => props.date, (newVal, oldVal) => {
      if (calData.dateString !== newVal) {
        calData.date = props.withTime ? commonUtils.convertStrToDateTime(newVal) : commonUtils.convertStrToDate(newVal)
        calData.dateString = newVal
      }
    })

    watch(() => calData.date, (newVal, oldVal) => {
      const date = props.withTime ? commonUtils.convertDateTimeToStr(newVal) : commonUtils.convertDateToStr(newVal)

      if (props.date !== date) {
        context.emit('update:date', date)
      }
    })

    return {
      calData
    }
  }
}
</script>

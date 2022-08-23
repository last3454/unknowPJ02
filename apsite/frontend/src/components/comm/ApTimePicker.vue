<template>
  <date-picker mode="time" v-model="calData.date" :minute-increment="5" :popover="{ visibility: 'click' }">
    <template v-slot="{ inputValue, inputEvents }">
      <div :class="['timepicker-box', withMarginLeft ? `mLeft${withMarginLeft}` : '']">
        <input
          :class="['flatpickr-input', styleClass]"
          :value="inputValue"
          v-on="inputEvents"
          :readonly="readOnly"
        />
      </div>
    </template>
  </date-picker>
</template>

<script>
import { reactive, inject, watch } from 'vue'

export default {
  name: 'ApTimePicker',
  props: {
    date: String,
    withMarginLeft: Number,
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
      calData.date = commonUtils.convertStrToDateTime(props.date)
    }

    watch(() => props.date, (newVal, oldVal) => {
      if (calData.dateString !== newVal) {
        calData.date = commonUtils.convertStrToDateTime(newVal)
        calData.dateString = newVal
      }
    })

    watch(() => calData.date, (newVal, oldVal) => {
      const date = commonUtils.convertDateTimeToStr(newVal)

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

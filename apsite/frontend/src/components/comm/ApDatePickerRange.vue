<template>
  <date-picker v-model="calData.range" is-range :popover="{ visibility: 'click' }">
    <template v-slot="{ inputValue, inputEvents }">
      <div class="datepicker-box multiple">
        <input
          type="text"
          :value="inputValue.start"
          v-on="inputEvents.start"
          :disabled="calData.disabledYn === 'Y'"
          :class="['flatpickr-input', styleClass]"
          :readonly="readOnly"
        >
        <span>~</span>
        <input
          type="text"
          :value="inputValue.end"
          v-on="inputEvents.end"
          :disabled="calData.disabledYn === 'Y'"
          :class="['flatpickr-input', styleClass]"
          :readonly="readOnly"
        >
      </div>
    </template>
  </date-picker>
</template>

<script>
import { reactive, inject, watch } from 'vue'

export default {
  name: 'ApDatePickerRange',
  props: {
    startDt: String,
    endDt: String,
    disabledYn: String,
    styleClass: { type: String, default: '' },
    readOnly: { type: Boolean, default: false }
  },
  emits: ['update:startDt', 'update:endDt'],
  setup (props, context) {
    const commonUtils = inject('commonUtils')
    const calData = reactive({
      range: {
        start: null,
        end: null
      },
      rangeString: {
        start: '',
        end: ''
      },
      disabledYn: ''
    })

    if (props.startDt && props.endDt) {
      calData.range = {
        start: commonUtils.convertStrToDate(props.startDt),
        end: commonUtils.convertStrToDate(props.endDt)
      }
    } else if (props.startDt) {
      calData.range = {
        start: commonUtils.convertStrToDate(props.startDt),
        end: null
      }
    } else if (props.endDt) {
      calData.range = {
        start: null,
        end: commonUtils.convertStrToDate(props.endDt)
      }
    }

    watch(() => props.disabledYn, () => {
      calData.disabledYn = props.disabledYn === 'Y' ? 'Y' : 'N'
    })

    watch(() => props.startDt, (newVal, oldVal) => {
      if (calData.rangeString.start !== newVal) {
        // console.log('watch - props.startDt : ', newVal)
        calData.range = {
          start: commonUtils.convertStrToDate(newVal),
          end: calData.range.end
        }
        calData.rangeString.start = newVal
      }
    })
    watch(() => props.endDt, (newVal, oldVal) => {
      if (calData.rangeString.end !== newVal) {
        // console.log('watch - props.endDt : ', newVal)
        calData.range = {
          start: calData.range.start,
          end: commonUtils.convertStrToDate(newVal)
        }
        calData.rangeString.end = newVal
      }
    })

    watch(() => calData.range, (newVal, oldVal) => {
      const startDt = commonUtils.convertDateToStr(newVal.start)
      const endDt = commonUtils.convertDateToStr(newVal.end)
      // const isChange = props.startDt !== startDt || props.endDt !== endDt

      // if (isChange) {
      //   console.log('watch - range : ', newVal)
      // }

      if (props.startDt !== startDt) {
        context.emit('update:startDt', startDt)
      }
      if (props.endDt !== endDt) {
        context.emit('update:endDt', endDt)
      }
    })
    return {
      calData
    }
  }
}
</script>

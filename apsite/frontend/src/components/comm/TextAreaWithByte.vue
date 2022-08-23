<template>
  <textarea
    :id="id"
    :readonly="readOnly"
    :disabled="disabled"
    @input="onInput($event)"
    :cols="cols"
    :rows="rows"
    :value="value"
    :placeholder="placeholder"
  ></textarea>
  <p class="byte_check_txt">
    <span :class="checkByte > maxlength ? 'txt_red' : ''"
      :id="'input_byte_' + id"
    >{{ commonUtils.setNumberComma(checkByte) }}</span> / {{ commonUtils.setNumberComma(maxlength) }}
  </p>
</template>

<script>
import { inject, ref } from 'vue'
export default {
  name: 'TextAreaWithByte',
  props: {
    id: { type: String, default: '', required: false },
    readOnly: { type: Boolean, default: false, required: false },
    value: { type: String, default: '', required: false },
    maxlength: { type: Number, default: null, required: false },
    disabled: { type: Boolean, default: false, required: false },
    placeholder: { type: String, default: '', required: false },
    cols: { type: Number, default: 30, required: false },
    rows: { type: Number, default: 10, required: false }
  },
  emits: ['update:value'],
  setup (props, context) {
    const commonUtils = inject('commonUtils')
    const checkByte = ref(0)
    const onInput = (event) => {
      const inputVal = event.target.value
      checkByte.value = commonUtils.calcByte(inputVal)
      context.emit('update:value', inputVal)
    }

    const init = () => {
      checkByte.value = commonUtils.calcByte(props.value)

      if (commonUtils.isEmpty(props.value)) {
        context.emit('update:value', '')
      }
    }

    init()

    return {
      commonUtils,
      onInput,
      checkByte,
      init
    }
  }
}
</script>

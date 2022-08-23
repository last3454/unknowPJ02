<template>
  <input
    type="text"
    :id="id"
    :name="name"
    :class="inputClass"
    :readonly="readonly"
    @input="onInput($event)"
    @keypress="onKeypress($event)"
    @blur="onBlur($event)"
    :value="value"
    :placeholder="placeholder"
    :maxlength="maxlength"
  />
</template>

<script>
export default {
  name: 'ApInputNumber',
  props: {
    id: { type: String, default: '', required: false },
    name: { type: String, default: '', required: false },
    inputClass: { type: String, default: '', required: false },
    readonly: { type: Boolean, default: false, required: false },
    value: { type: [String, Number], default: '', required: false },
    isComma: { type: Boolean, default: false, required: false }, // 콤마 여부
    point: { type: Number, default: 0, required: false }, // 허용가능 소수점 자리수
    digitMax: { type: Number, default: 0, required: false }, // 정수 허용 숫자 (0 일경우 제한 없음)
    isPhone: { type: Boolean, default: false, required: false }, // 정수가 0부터 시작가능여부
    isMinus: { type: Boolean, default: false, required: false },
    isReturnNumber: { type: Boolean, default: false, required: false },
    placeholder: { type: String, default: '', required: false },
    maxlength: { type: Number, default: null, required: false }
  },
  emits: ['update:value'],
  setup (props, context) {

    const onInput = (e) => {
      const rtn = numberWithCommas(e.target.value)
      e.target.value = rtn.str

      const rtnText = rtn.num
      const rtnNumber = rtn.num === '' ? 0 : Number(rtn.num)

      if (rtnText !== '' && props.isReturnNumber) {
        context.emit('update:value', rtnNumber)
      } else {
        context.emit('update:value', rtnText)
      }
    }

    const onKeypress = (e) => {
      const keyCode = e.keyCode
      // - (마이너스)
      if (props.isMinus && keyCode === 45) {
        return
      }
      // .
      if (props.point !== 0 && keyCode === 46) {
        return
      }
      // 숫자
      if (keyCode >= 48 && keyCode <= 57) {
        return
      }
      e.preventDefault()
    }

    const onBlur = (e) => {
      const rtn = numberWithCommas(e.target.value)
      e.target.value = rtn.str

      const rtnText = rtn.num
      const rtnNumber = rtn.num === '' ? 0 : Number(rtn.num)

      if (rtnText !== '' && props.isReturnNumber) {
        context.emit('update:value', rtnNumber)
      } else {
        context.emit('update:value', rtnText)
      }
    }

    const numberWithCommas = (val) => {
      const rtn = {
        str: '',
        num: ''
      }

      if (val === '') {
        return rtn
      }

      if (val === '0') {
        rtn.str = '0'
        rtn.num = '0'
        return rtn
      }

      const len = val.length
      const arrNum = []
      const arrPoint = []
      let hasMinus = false // 마이너스 여부
      let hasPoint = false // 소수점 여부

      for (let i = 0; i < len; i++) {
        if (val.charAt(i) === '-') {
          if (props.isMinus && i === 0) {
            hasMinus = true
          }
          continue
        } else if (val.charAt(i) === '.') {
          if (!hasPoint) {
            hasPoint = true
          }
          continue
        }

        if (val.charAt(i) >= '0' && val.charAt(i) <= '9') {
          if (!hasPoint) {
            arrNum.push(val.charAt(i))
          } else {
            arrPoint.push(val.charAt(i))
          }
        }
      }

      let num = arrNum.join('')
      let point = arrPoint.join('')
      let isPoint = props.point !== 0 && hasPoint

      if (!props.isPhone && num !== '' && num !== '0') {
        while (num !== '0' && num.indexOf('0') === 0) {
          num = num.substring(1)
        }
      }

      if (props.digitMax > 0 && num.length > props.digitMax) {
        num = num.substring(0, props.digitMax)
      }

      if (props.point > 0 && point.length > props.point) {
        point = point.substr(0, props.point)
      }

      if (isPoint && num === '') {
        num = '0'
      }

      if (props.isComma) {
        let reg = /(^[+-]?\d+)(\d{3})/
        let ret = (num + '')

        while (reg.test(ret)) {
          ret = ret.replace(reg, '$1' + ',' + '$2')
        }

        rtn.str = (hasMinus ? '-' : '') + ret + (isPoint ? '.' + point : '')
        rtn.num = (hasMinus ? '-' : '') + num + (isPoint ? '.' + point : '')
      } else {
        rtn.str = (hasMinus ? '-' : '') + num + (isPoint ? '.' + point : '')
        rtn.num = (hasMinus ? '-' : '') + num + (isPoint ? '.' + point : '')
      }

      return rtn
    }

    return {
      onInput,
      onKeypress,
      onBlur
    }
  }
}
</script>

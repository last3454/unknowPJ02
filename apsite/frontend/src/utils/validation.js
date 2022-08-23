import { inject } from 'vue'
import { MagicString } from 'vue/compiler-sfc';
import * as yup from 'yup';

export default {
  init () {
    this.setDefault()
  },
  setDefault () {
    const t = inject('t')
    yup.setLocale({
      mixed: {
        required: t('common.msg.essential'),
        notType: function notType(_ref) {
          const type = _ref.type
          if (type === 'number') {
            const msg = t('common.msg.essential')
            return msg;
          }
        }
      },
      number: {
        positive: t('common.msg.more_than')
      },
    })
  }
}
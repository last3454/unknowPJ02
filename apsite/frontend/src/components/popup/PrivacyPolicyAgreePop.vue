<template>
  <div class="modal-content">
    <div class="modal-tit-wrap">
      <p class="modal-tit">{{ t('privacy.label.privacy_agree_title') }}<!-- 개인정보 수집 및 이용 동의 --></p>
    </div>
    <div class="modal-cont-wrap">
      <div class="pp-top">
        <div class="pp-p">
          <p>
            {{ t('privacy.msg.agree_desc1') }}
            <!-- (주)아모레퍼시픽은 원료정보시스템 운영을 위하여 하기와 같이 개인정보를 수집 및 이용합니다. -->
          </p>
        </div>
        <br>
        <div class="pp-p">
          <p>
            {{ t('privacy.msg.agree_desc2') }}
            <!-- - 수집하는 개인정보의 항목 : 성명, 생년월일, 성별, 휴대전화번호, ID/PW, 이메일주소, 소속회사 정보, DI -->
          </p>
          <p>
            {{ t('privacy.msg.agree_desc3') }}
            <!-- - 수집 및 이용목적 : 아모레퍼시픽 원료정보시스템 가입자 본인확인, 공지사항 전달 -->
          </p>
        </div>
        <div class="pp-p">
          <span>{{ t('privacy.msg.agree_desc4') }}</span><!-- - 보유 및 이용기간 :  -->
          <span style="font-size:17px; color:#e8a200;">{{ t('privacy.msg.agree_desc5') }}</span><!-- 회원탈퇴 또는 퇴사 시 까지 -->
        </div>
        <br>
        <div class="pp-p">
          <p>{{ t('privacy.msg.agree_desc6') }}</p><!-- 이용자는 개인정보 수집 및 이용 동의에 거부할 수 있습니다. 다만, 거부하는 경우 원료정보시스템 가입이 불가능 합니다.  -->
        </div>
        <br>
        <div class="input-radio">
          <div class="input-radio-indiv">
            <span class="agree1">
              <input
                type="radio"
                v-model="agreeYn"
                name="agreeYn_radio"
                id="p_r_chk1"
                value="Y"
              />
              <label for="p_r_chk1">{{ t('privacy.msg.agree_desc7') }}</label><!-- 동의합니다. -->
            </span>
            <span class="agree2">
              <input
                type="radio"
                v-model="agreeYn"
                name="agreeYn_radio"
                id="p_r_chk2"
                value="N"
              />
              <label for="p_r_chk2">{{ t('privacy.msg.agree_desc8') }}</label><!-- 동의하지 않습니다. -->
            </span>
          </div>
        </div>
        <br>
        <div class="pp-p">
          <p style="font-weight:bold;">
            {{ t('privacy.msg.agree_desc9') }}
            <!-- * 개인정보 처리 위탁에 대한 안내 -->
          </p>
          <span>
            {{ t('privacy.msg.agree_desc10') }}
            <!-- 아모레퍼시픽은 서비스 향상 및 원활한 전산 처리 등을 위하여 이용자의 개인정보 관리를 외부 전문업체에 위탁하고 있습니다. 아모레퍼시픽의 업무를 위탁받는 자 및 업무의 내용은 -->
          </span>
          <a href="javascript:;" style="color:#e8a200; text-decoration:underline;" @click.prevent="fnPolicyPop()" class="popup_link">
            {{ t('privacy.msg.agree_desc11') }}
            <!-- 개인정보처리방침 -->
          </a>
          <span>
            {{ t('privacy.msg.agree_desc12') }}
            <!-- 에서 확인 하실 수 있습니다. -->
          </span>
        </div>
      </div>
      <div class="btn-wrap">
        <a
          href="#"
          @click.prevent="fnClose"
          class="btn typeB"
          ><span>{{ t('common.label.alert_ok') }}</span> <!-- 확인 -->
        </a>
      </div>
    </div>
    <div class="modal-close-btn">
      <a
        href="#"
        @click.prevent="onCancel"
        title="팝업창 닫기"
        >팝업창 닫기</a>
    </div>
    <teleport to="#common-modal" v-if="showPopup">
      <ap-popup id="privacyPolicy">
        <component
          :is="popupContent"
        />
      </ap-popup>
    </teleport>
  </div>
</template>

<script>
import { inject, ref, defineAsyncComponent } from 'vue'
import { useJoin } from '@/compositions/useJoin'
import { useActions } from 'vuex-composition-helpers'

export default {
  name: 'PrivacyPolicyAgreePop',
  props: {
    isAgree: String
  },
  components: {
    ApPopup: defineAsyncComponent(() => import('@/components/comm/ApPopup.vue')),
    PrivacyPolicyPop : defineAsyncComponent(() => import('@/components/popup/PrivacyPolicyPop.vue'))
  },
  setup(props, { emit }) {
    const t = inject('t')
    const commonUtils = inject('commonUtils')
    const { closeAsyncPopup, openAsyncPopup, openAsyncAlert } = useActions(['closeAsyncPopup', 'openAsyncPopup', 'openAsyncAlert'])

    let agreeYn = ref(null)
    let showPopup = ref(false)
    let popupContent = ref(null)

    const {
      fnOpenPopup
    } = useJoin()

    if (props.isAgree === 'Y') {
      agreeYn.value = 'Y'
    } else {
      agreeYn.value = 'N'
    }

    const onCancel = () => {
      closeAsyncPopup({ message: '취소' })
    }

    const fnClose = async () => {
      if (commonUtils.isEmpty(agreeYn.value)) {
        await openAsyncAlert({ message: t('privacy.msg.agree_desc13') }) // 동의여부를 선택해주십시오.
        return
      }

      // emit('callBack', agreeYn.value)
      closeAsyncPopup({ message: t('common.label.select'), emitValue: agreeYn.value })
    }

    const fnPolicyPop = () => {
      showPopup.value = true
      fnPolicyOpenPopup('PrivacyPolicyPop')
    }

    const fnPolicyOpenPopup = (compNm) => {
      popupContent.value = compNm

      openAsyncPopup({ minWidth: 800 })
        .then(res => {
          showPopup.value = false
          fnOpenPopup('PrivacyPolicyAgreePop')
        })
        .catch(err => {
          console.log(err)
        })
        .finally(() => {
          popupContent.value = null
        })
    }

    return {
      t,
      agreeYn,
      popupContent,
      fnPolicyPop,
      showPopup,
      fnClose,
      onCancel
    }
  },
}
</script>

<style>
.agree2 {
  margin-left: 15px;
}
</style>
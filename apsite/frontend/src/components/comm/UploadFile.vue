<template>
  <div class="file-wrap" :class="disabled ? 'disabled' : ''">
    <!-- <ul>
      <li v-for="(item, idx) in uploadParams.items" :key="idx">
        {{ dto.fileNm }} 
        <button type="button" @click.prevent="fnRemoveFiles(idx)">삭제</button>
      </li>
    </ul> -->
    <div class="file-name div-file-name">
      <span class="file-placeholder" v-show="!uploadParams.items || uploadParams.items.length === 0">{{ props.placeHolder }}</span>
      <!-- 이미지 첨부만 가능한 경우(썸네일 이미지는 목록에 보이지 않도록 처리) -->
      <template v-if="props.fileType === 'image'">
        <template v-for="(item, idx) in uploadParams.items">
          <p v-if="item.fileType !== 'Thumb'" :key="'attach_' + idx" class="p_uploadTitle txt_ellipsis">
            {{ item.fileNm }}
            <button type="button" class="file-del-btn" @click="fnRemoveFiles(idx)"> </button>
          </p>
        </template>
      </template>
      <!-- 파일 첨부는 모두 보이도록 -->
      <template v-else>
        <p v-for="(item, idx) in uploadParams.items" :key="'attach_' + idx" class="p_uploadTitle txt_ellipsis">
          {{ item.fileNm }}
          <button type="button" class="file-del-btn" @click="fnRemoveFiles(idx)"> </button>
        </p>
      </template>
    </div>
    <span class="file-btn" :class="props.fileBtnClass" @click="fnSelectFiles()">
      <input type="file" ref="uploadFile" multiple @change="fnChangeFile($event)" v-show="false"/>
      <span>{{ t('common.label.search3') }}</span>
    </span>
  </div>
</template>

<script>
import { ref, inject, watch } from 'vue'
import { useActions } from 'vuex-composition-helpers'
import { useUploadFile } from '@/compositions/useUploadFile'
// import upload from '@/api/comm/upload'

export default {
  name: 'UploadFile',
  props: {
    uploadCd: {
      type: String,
      required: true
    },
    disabled: {
      type: Boolean,
      default: false
    },
    placeHolder: {
      tyle: String,
      default: ''
    },
    fileBtnClass: {
      type: String,
      default: 'file-small-btn'
    },
    parentInfo: {
      type: Object,
      default: null
    },
    fileType: {
      type: String,
      default: 'file'
    },
    uploadCount: {
      type: Number,
      default: 0
    }
  },
  setup (props, { emit }) {
    const t = inject('t')
    const uploadFile = ref(null)
    let uploadParams = props.parentInfo || inject('upload-' + props.uploadCd)
    const { openAsyncAlert } = useActions(['openAsyncAlert'])

    const {
      setModifyUploadFiles,
      addUploadFiles,
      delUploadFile
    } = useUploadFile()

    // 파일선택
    const fnSelectFiles = () => {
      if (props.uploadCount != 0 && props.uploadCount <= uploadParams.items.length) {
        openAsyncAlert({ message: t('common.msg.upload_count_msg', {count: props.uploadCount }) })
        return
      }
      if (props.disabled) {
        return
      }

      uploadFile.value.click()
    }

    // 파일선택 완료시
    const fnChangeFile = async (evt) => {
      const files = evt.target.files
      if (props.uploadCount != 0 && props.uploadCount < files.length) {
        openAsyncAlert({ message: t('common.msg.upload_count_msg', {count: props.uploadCount }) })
        return
      }
      const resData = await addUploadFiles(evt, props.uploadCd, uploadParams.items, props.fileType)
      if (resData) {
        callBack()
      }
    }

    // 첨부파일 삭제
    const fnRemoveFiles = async (idx) => {
      const target = JSON.parse(JSON.stringify(uploadParams.items[idx]))

      uploadParams.items.splice(idx, 1)

      if (props.fileType === 'image') {
        let searchYn = 'N'
        let thumbIdx = 0

        uploadParams.items.forEach((item, idx) => {
          if (item.fileNm === target.fileNm && item.fileType === 'Thumb') {
            searchYn = 'Y'
            thumbIdx = idx
          }
        })

        if (searchYn === 'Y') {
          uploadParams.items.splice(thumbIdx, 1)
        }
      }
      
      callBack()
    }

    const callBack = () => {
      try {
        const returnObj = {
          items: uploadParams.items,
          uploadCd: props.uploadCd
        }
        emit('callBack', returnObj)
      } catch (err) {}
    }

    watch(() => uploadParams.targetKey, (newValue) => {
      if (newValue === '') {
        uploadParams.items = []
      } else {
        setModifyUploadFiles({ targetKey: newValue, uploadCd: props.uploadCd })
          .then(items => {
            uploadParams.items = items
            callBack()
          })
      }
      // console.log('watch1 - uploadfile :', newValue)
    })

    if (uploadParams.targetKey) {
      // console.log('uploadParams.targetKey :', uploadParams.targetKey)
      setModifyUploadFiles({ targetKey: uploadParams.targetKey, uploadCd: props.uploadCd })
        .then(items => {
          uploadParams.items = items
          callBack()
        })
    }

    return {
      t,
      props,
      fnSelectFiles,
      fnChangeFile,
      fnRemoveFiles,
      uploadFile,
      uploadParams
    }
  }
}
</script>


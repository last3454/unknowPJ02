<template>
  <textarea ref="el"></textarea>
</template>

<script>
import { Jodit } from 'jodit'
import { useStore } from 'vuex'
import { watch, computed, onMounted, onBeforeUnmount, ref } from 'vue'
import 'jodit/build/jodit.min.css'

export default {
  name: 'JoditEditor',
  props: {
    modelValue:{
      type: String,
      required: false
    },
    uploadCd:{
      type: String,
      required: false
    }
  },
  emits: ['update:modelValue'],
  setup (props, context) {
    const content = computed(() => props.modelValue)
    const el = ref(null)
    let editor
    onMounted(() => {
      editor = new Jodit.make(el.value, {
        textIcons: false,
        iframe: false,
        iframeStyle: '*,.jodit_wysiwyg {color:red;}',
        height: 'auto',
        minHeight: 400,
        maxHeight: 600,
        defaultMode: Jodit.MODE_WYSIWYG,
        imageDefaultWidth: '100%',
        observer: {
          timeout: 100
        }, 
        uploader: {
          readonly: false,
          url: '/upload/image-files',
          imagesExtensions: ['jpg', 'png', 'jpeg', 'gif'],
          enableDragAndDropFileToEditor: true,
          insertImageAsBase64URI: false,
          pathVariableName: 'path',
          filesVariableName: function (t) {
            return `files`
          },                        
          processFileName: (key, file, name) => {
            return [key, file, name];
          },            
          method: 'POST',
          format: 'json',
          headers: {'authorization': useStore().getters.getAccessToken()},        
          prepareData: function (formdata) {
            formdata.append('uploadCd','EDITER_'+props.uploadCd)
            formdata.append('fileType','image')
            return formdata
          },
          process: function (resp) {
            return true;
          },            
          isSuccess : (res) =>{
            const resData = res.data
            resData.forEach(dto => {
              editor.selection.insertImage(`data:image${dto.fileExt};base64,${dto.fileBytea}`)
            })
            return true;
          }
        },                    
        commandToHotkeys: {
          'openreplacedialog': 'ctrl+p'
        }        
      })
      editor.value = content.value
      editor.events.on('change', newValue => context.emit('update:modelValue', newValue))
    })

    onBeforeUnmount(() => {
      editor.destruct()
    })

    watch(content, (newValue) => {
      if (editor.value !== newValue) {
        editor.value = newValue
      }
    })

    return {
      el
    }
  }
}
</script>

<style>
strong{
 font-weight: bold !important;
}
.jodit-drag-and-drop__file-box input[type="file"]{
  width: 100% !important;
  height: 100% !important;
}
</style>
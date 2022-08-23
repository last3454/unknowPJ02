import { useStore } from 'vuex'

export default {
  isAdmin () {
    const store = useStore()
    const myInfo = store.getters.getMyInfo()

    if (myInfo == undefined || myInfo.roles == undefined) {
      return false
    }

    for (let i in myInfo.roles) {
      if (myInfo.roles[i] === 'ADMIN') {
        return true
      }
    }

    return false
  },
  /**
   * 권한 체크
   * 시스템 관리자(SGG000001)는 모든 권한 부여(Default)
   * @param {*} groupStr - 허용되는 그룹(Multi 작성 시, '|'로 구분), ex) 'SGG000003', 'SGG000005|SGG000007'
   * @returns
   */
  checkAuth (groupStr) {
    if (!groupStr && typeof groupStr !== 'string') return false

    groupStr = `SGG000001${!this.isEmpty(groupStr) ? '|' : ''}${groupStr ?? ''}`

    const store = useStore()
    const myInfo = store.getters.getMyInfo()

    if (myInfo === undefined || myInfo.groups === undefined || myInfo.groups.length === 0) {
      return false
    }

    // 슈퍼계정(jslee)인 경우는 모든 권한 부여, 추후에 아래 코드로 수정할 수 있음.
    const auth = myInfo.groups.some(group => groupStr.indexOf(group) > -1)
    // const auth = myInfo.groups.some(group => groupStr.indexOf(group) > -1)

    return auth
  },
  checkAuthOnly (groupStr) {
    if (!groupStr && typeof groupStr !== 'string') return false

    groupStr = `${!this.isEmpty(groupStr) ? '|' : ''}${groupStr ?? ''}`

    const store = useStore()
    const myInfo = store.getters.getMyInfo()

    if (myInfo === undefined || myInfo.groups === undefined || myInfo.groups.length === 0) {
      return false
    }

    // 슈퍼계정(jslee)인 경우는 모든 권한 부여, 추후에 아래 코드로 수정할 수 있음.
    const auth = myInfo.groups.some(group => groupStr.indexOf(group) > -1)
    // const auth = myInfo.groups.some(group => groupStr.indexOf(group) > -1)

    return auth
  },
  isEmpty (val) {
    if (val === null || val === undefined) {
      return true
    }

    if (typeof val === 'string') {
      if (val.trim() === '') {
        return true
      } else {
        return false
      }
    } else if (typeof val === 'object') {
      if (Object.keys(val).length === 0) {
        return true
      } else {
        return false
      }
    } else if (typeof val === 'number') {
      if (val === null) {
        return true
      } else {
        return false
      }
    }
    return true
  },
  isNotEmpty (val) {
    return !this.isEmpty(val)
  },
  capitalize (str) {
    if (this.isEmpty(str)) {
      return ''
    }

    return str.charAt(0).toUpperCase() + str.slice(1)
  },
  convertDt (dt) {
    if (dt === undefined) {
      return ''
    }
    return dt.substring(0, 10)
  },
  // 2022-04-28 => Date()
  convertStrToDate (str) {
    if (str === null || str === undefined) {
      return null
    }
    const arr = str.split('-')
    if (arr.length < 3) {
      return null
    }

    return new Date(parseInt(arr[0], 10), parseInt(arr[1], 10) -1, parseInt(arr[2], 10))
  },
  // 2022-04-28 16:00 => Date()
  convertStrToDateTime (str) {
    if (!str) {
      return null
    }
    const arr = str.split(' ')
    if (arr.length < 2) {
      return null
    }
    const dateArr = arr[0].split('-')
    if (dateArr.length < 3) {
      return null
    }
    const timeArr = arr[1].split(':')
    if (timeArr.length < 2) {
      return null
    }

    return new Date(parseInt(dateArr[0], 10), parseInt(dateArr[1], 10) -1, parseInt(dateArr[2], 10), parseInt(timeArr[0], 10), parseInt(timeArr[1], 10))
  },
  // 현재 날짜 가져오기 (YYYY-MM-DD)
  getToday () {
    let today = new Date()
    let year = today.getFullYear() //연도
    let month = ('0' + (today.getMonth() + 1)).slice(-2) //월
    let date = ('0' + today.getDate()).slice(-2) //날짜

    return year + '-' + month + '-' + date
  },
  // Date() => 2022-04-28
  convertDateToStr (dt) {
    if (dt === null || dt === undefined) {
      return ''
    }
    const y = dt.getFullYear()
    const m = dt.getMonth() + 1
    const d = dt.getDate()

    return y + '-' + (m > 9 ? m : '0' + m) + '-' + (d > 9 ? d : '0' + d)
  },
  // Date() => 2022-04-28 16:00
  convertDateTimeToStr (dt) {
    if (!dt) {
      return ''
    }
    const y = dt.getFullYear()
    const m = dt.getMonth() + 1
    const d = dt.getDate()
    const hours = dt.getHours()
    const minutes = dt.getMinutes()

    return `${y}-${m > 9 ? m : '0' + m}-${d > 9 ? d : '0' + d} ${hours > 9 ? hours : '0' + hours}:${minutes > 9 ? minutes : '0' + minutes}`
  },
  getAddDay (strDate, addDay, char) {
    let date = null

    if (this.isEmpty(strDate)) {
      date = new Date()
    } else {
      const year = strDate.substring(0, 4)
      const month = strDate.substring(4, 6)
      const day = strDate.substring(6, 8)
      date = new Date(Number(year), Number(month) - 1, Number(day))
    }

    let returnDate = new Date(date.setDate(date.getDate() + addDay))

    const year = returnDate.getFullYear()
    let month = returnDate.getMonth() + 1
    month = month < 10 ? '0' + month : month

    let day = returnDate.getDate()
    day = day < 10 ? '0' + day : day

    return year + char + month + char + day
  },
  /**
   * 에디터 설정 파일
   * @returns
   */
  getEditorConfig () {
    const store = useStore()
    const accessToken = store.getters.getAccessToken()
    return {
      uploader: {
        url: '/upload/images',
        format: 'json',
        insertImageAsBase64URI: false,
        headers: {
          'authorization': accessToken
        },
        data: {},
        error: (e) => {
          console.log('Editor Error', e)
        },
        isSuccess: (resp) => {
          console.log('isSuccess :', resp)
          return resp.success
        },
        getMessage: (resp) => {
          return resp.data.message
        }
      }
    }
  },
  historyBack() {
    window.history.back();
  },
  scrollXHandler () {
    const contScrollX = document.querySelectorAll('.cont-scrollX')
    if(contScrollX.length > 0) {
      for(let i=0; i < contScrollX.length; i++) {
        let scrollCont = contScrollX[i],
            scrollBar = scrollCont.querySelector('.cont-scrollX-bar'),
            scrollWrap = scrollCont.querySelector('.cont-scrollX-wrap'),
            scrollArea = scrollCont.querySelector('.cont-scrollX-area'),
            scrollWidth = parseInt(scrollCont.getAttribute('data-width'))

        //scrollBar.querySelector('.bar').style.width = scrollWidth + 'px'
        //scrollArea.style.width = scrollWidth + 'px';

        scrollBar.addEventListener('scroll', function(){ scrollWrap.scrollLeft = scrollBar.scrollLeft; })
        scrollWrap.addEventListener('scroll', function(){ scrollBar.scrollLeft = scrollWrap.scrollLeft; })

        function scrollHorizontally(e) {
          e = window.event || e;
          var delta = Math.max(-1, Math.min(1, (e.wheelDelta || -e.detail)))
          scrollWrap.scrollLeft -= (delta * 60);
          e.preventDefault();
        }
        /* if (scrollWrap.addEventListener) {
          scrollWrap.addEventListener('mousewheel', scrollHorizontally, false)
          scrollWrap.addEventListener('DOMMouseScroll', scrollHorizontally, false)
        } else {
          scrollWrap.attachEvent('onmousewheel', scrollHorizontally)
        } */
      }
    }
  },
  tableFix () {
    const tableFixHead = document.querySelectorAll('.tableFixHead');
    if(tableFixHead.length > 0) {
      for(let i=0;i<tableFixHead.length;i++) {
        let table = tableFixHead[i],
        th = table.querySelectorAll('tbody th');
        table.addEventListener('scroll', function(){
          th.forEach(el=>{
            el.style.transform = `translateX(${table.scrollLeft}px)`;
          })
        })
      }
    }
  },
  compNumFormat (number) {
    if(!number || isNaN(number)){
      return ''
    }

    if(typeof(number) !== 'string'){
      number = number.toString()
    }

    if(number.length === 10){
      return number.replace(/(\d{3})(\d{2})(\d{5})/, "$1-$2-$3")
    }
    return number
  },
  phoneFormat (number) {
    if(!number || isNaN(number)){
      return ''
    }

    if(typeof(number) !== 'string'){
      number = number.toString()
    }

    if(number.length === 10){
      return number.replace(/(\d{3})(\d{3})(\d{4})/, "$1-$2-$3")
    } else if(number.length > 10){
      return number.replace(/(\d{3})(\d{4})(\d{4})/, "$1-$2-$3")
    }
    return number
  },
  resetValue(obj, arrKey) {
    if (arrKey === undefined || arrKey.length === 0) {
      return
    }

    for (const key of arrKey) {
      obj[key] = ''
    }
  },
  isNumber (e) {
    e = e ? e : window.event
    const charCode = (e.which) ? e.which : e.keyCode
    if ((charCode > 31 && (charCode < 48 || charCode > 57)) && charCode !== 46) {
      e.preventDefault()
    } else {
      return true
    }
  },
	maskingName (str) {
		let originStr = str
		let strLength

		if(originStr === '' || originStr === undefined){
			return ''
		}
		strLength = originStr.length

    if (strLength > 2) {
      const originName = originStr.split('');
      originName.forEach(function(name, i) {
        if (i === 0 || i === originName.length - 1) return;
        originName[i] = '*';
      });

      const joinName = originName.join();
      return joinName.replace(/,/g, '');
    } else {
      const pattern = /.$/; // 정규식
      return originStr.replace(pattern, '*');
    }
	},
  maskingId (str) {
		let originStr = str
		let maskingStr
		let strLength

		if(originStr === '' || originStr === undefined){
			return ''
		}
    strLength = originStr.length
		if(strLength <= 4){
			maskingStr = originStr.replace(/(?<=.{3})./gi, "＊")
		}else {
			maskingStr = originStr.replace(/(?<=.{4})./gi, "*")
		}
    return maskingStr
  },
  maskingPhone (str) {
    let originStr = str
    let phoneStr = str.match(/\d{2,3}-\d{3,4}-\d{4}/gi)
    let maskingStr

		if(originStr === '' || originStr === undefined){
			return ''
		}

    if(/-[0-9]{3}-/.test(phoneStr)) {
      maskingStr = originStr.toString().replace(phoneStr, phoneStr.toString().replace(/-[0-9]{3}-/g, "-***-"));
    } else if(/-[0-9]{4}-/.test(phoneStr)) {
      maskingStr = originStr.toString().replace(phoneStr, phoneStr.toString().replace(/-[0-9]{4}-/g, "-****-"));
    }

    return maskingStr
  },
  maskingEmail (str) {
    let originStr = str
    let strLength
    let maskingStr

		if(originStr === '' || originStr === undefined){
			return ''
		}

    const splitEmail = originStr.toString().split('@')
    const emailId = splitEmail[0]
    strLength = emailId.length

    if(strLength < 5){
			maskingStr = emailId.substring(0, strLength - 1) + '*'
		}else {
      maskingStr = emailId.replace(/(?<=.{4})./gi, "*")
		}

    return maskingStr + '@' + splitEmail[1]
  },
  maskingContent (str) {
    let originStr = str
		let maskingStr

		if(originStr === '' || originStr === undefined){
			return ''
		}
    maskingStr = originStr.replace(/(?<=.{0})./gi, "*")

    return maskingStr
  },
  protectionInfo () {
    document.onselectstart = function (e) {
      return false;
    }
    document.addEventListener("keyup", function (e) {
      const keyCode = e.keyCode ? e.keyCode : e.which;
      if (keyCode == 44) {
        var inpFld = document.createElement("input");
        inpFld.setAttribute("value", ".");
        inpFld.setAttribute("width", "0");
        inpFld.style.height = "0px";
        inpFld.style.width = "0px";
        inpFld.style.border = "0px";
        document.body.appendChild(inpFld);
        inpFld.select();
        document.execCommand("copy");
        inpFld.remove(inpFld);
      }
    });
  },
  noProtectionInfo () {
    document.onselectstart = function (e) {
      return true;
    }
    document.removeEventListener("keyup", function (e) {});
  },
  compareObject (obj1, obj2, excludeKeys) {
    const diffKeys = []

    if (!obj1 || !obj2 || (obj1.constructor !== obj2.constructor)) {
      diffKeys.push('ERROR_CONSTRUCTOR')
      return diffKeys
    }

    const keyLists = Object.keys(obj1)
    const len = keyLists.length
    let key

    for (let i = 0; i < len; i++) {
      key = keyLists[i]
      if (this.isDiffObject(obj1, obj2, key, excludeKeys, '')) {
        diffKeys.push(key)
      }
    }

    return diffKeys
  },
  // 변경 체크 제외 key 인지 체크
  isExcludeKeys (key, excludeKeys, parentKey) {
    const defExcludeKeys = ['regUserCd', 'regDtm', 'updUserCd', 'updDtm']
    const f1 = defExcludeKeys.find(k => k === key)
    if (f1) {
      return true
    }
    if (excludeKeys === null || excludeKeys === undefined) {
      return false
    }

    if (parentKey === null || parentKey === undefined) {
      parentKey = ''
    }

    const f2 = excludeKeys.find(k => {
      if (k.indexOf('.') > -1) {
        return k === parentKey + '.' + key
      } else {
        return k === key
      }
    })

    if (f2) {
      return true
    } else {
      return false
    }
  },
  isDiffObject (obj1, obj2, key, excludeKeys, parentKey) {
    if (this.isExcludeKeys(key, excludeKeys, parentKey)) {
      return false
    }

    if ((obj1[key] === null || obj1[key] === undefined || obj1[key] === '') && (obj2[key] === null || obj2[key] === undefined)) {
      return false
    }
    if ((obj1[key] !== null && obj1[key] !== undefined) && (obj2[key] === null || obj2[key] === undefined)) {
      return true
    }
    if ((obj1[key] === null || obj1[key] === undefined) && (obj2[key] !== null && obj2[key] !== undefined)) {
      return true
    }

    if (excludeKeys === null || excludeKeys === undefined) {
      excludeKeys = []
    }
    if (parentKey === null || parentKey === undefined) {
      parentKey = ''
    }

    const o1 = obj1[key]
    const o2 = obj2[key]
    const t1 = typeof o1
    const t2 = typeof o2

    if (t1 !== t2) {
      return true
    }

    if (t1 === 'object') {
      if (o1.constructor === Object && o2.constructor !== Object) {
        return true
      } else if (o1.constructor === Array && o2.constructor !== Array) {
        return true
      }

      if (o1.constructor === Object && o2.constructor === Object) {
        const keyLists = Object.keys(o1)
        const len = keyLists.length
        const tempParentKey = parentKey === '' ? key : parentKey + '.' + key
        const tempExcludeKeys = excludeKeys.filter(k => k.indexOf(tempParentKey + '.') === 0)

        for (let i = 0; i < len; i++) {
          if (this.isDiffObject(o1, o2, keyLists[i], tempExcludeKeys, tempParentKey)) {
            return true
          }
        }
      } else if (o1.constructor === Array && o2.constructor === Array) {
        const len1 = o1.length
        const len2 = o2.length

        if (len1 !== len2) {
          return true
        }

        const tempParentKey = parentKey === '' ? key : parentKey + '.' + key
        const tempExcludeKeys = excludeKeys.filter(k => k.indexOf(tempParentKey + '.') === 0)

        for (let i = 0; i < len1; i++) {
          if (o1[i].constructor === Object) {
            const keyLists = Object.keys(o1[i])
            const len = keyLists.length
            for (let j = 0; j < len; j++) {
              if (this.isDiffObject(o1[i], o2[i], keyLists[j], tempExcludeKeys, tempParentKey)) {
                return true
              }
            }
          } else {
            if (o1[i] !== o2[i]) {
              return true
            }
          }
        }
      } else if (o1 !== o2) {
        return true
      }
    } else if (o1 !== o2) {
      return true
    }
    return false
  },
  checkByte (byteString, maxLength) {
    if (!byteString) {
      return true
    }

    if (!maxLength || typeof byteString !== 'string' || typeof maxLength !== 'number') {
      return false
    }

    const byteLength = ((s, b,i,c) => {
      for(b=i=0;c=s.charCodeAt(i++);b+=c>>11?3:c>>7?2:1);
      return b
    })(byteString)

    return byteLength > maxLength ? false : true
  },
  calcByte (val) {
    let tcount = 0
    let tmpStr = String(val)
    let onechar = ''
    let k = ''
    const len = tmpStr.length

    for (k = 0; k < len; k++) {
      onechar = tmpStr.charAt(k)
      if (escape(onechar).length > 4) {
        tcount += 3
      } else {
        tcount += 1
      }
    }

    return tcount
  },
  setNumberComma (val) {
    return String(val).replace(/\B(?=(\d{3})+(?!\d))/g, ',')
  }
}
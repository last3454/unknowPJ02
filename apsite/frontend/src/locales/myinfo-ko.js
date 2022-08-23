// common-ko.js 파일 참고
const myinfo = {
  label: {
    profile: {},
    company: {},
    chkPwd: {}
  },
  msg: {
    profile: {},
    company: {},
    chkPwd: {}
  }
}

// 개인정보수정
myinfo.label.profile.title = '개인정보'
myinfo.label.profile.title2 = '개인정보 수정'
myinfo.label.profile.userNm = '성명'
myinfo.label.profile.mobileNo = '휴대폰번호'
myinfo.label.profile.mobileNo2 = '휴대폰번호 (- 없이)'
myinfo.label.profile.loginId = '계정 아이디'
myinfo.label.profile.loginPwd = '계정 비밀번호'
myinfo.label.profile.email = '이메일 주소'
myinfo.label.profile.btnModify = '수정'
myinfo.label.profile.btnCancel = '취소'
myinfo.label.profile.btnSave = '저장'
myinfo.label.profile.chgPwd = '비밀번호 변경'
myinfo.label.profile.userCompDeptNm = '회사 부서'

myinfo.msg.profile.desc1 = '가입정보가 없습니다.'
myinfo.msg.profile.desc2 = '휴대폰 번호를 입력하세요.'
myinfo.msg.profile.desc3 = '이메일 주소를 입력하세요.'
myinfo.msg.profile.desc4 = '올바른 형식의 메일 주소를 입력해주세요.'
myinfo.msg.profile.desc5 = '입력하신 내용을 저장하시겠습니까?'
myinfo.msg.profile.desc6 = '입력하신 내용을 취소하시겠습니까?'
myinfo.msg.profile.desc7 = '새 비밀번호를 입력하세요.'
myinfo.msg.profile.desc8 = '이메일이 없습니다'
myinfo.msg.profile.desc9 = '소속이 없습니다'
myinfo.msg.profile.desc10 = '소속을 입력하세요.'
myinfo.msg.profile.desc11 = '{userNm}님은 업체 마스터 계정입니다.<br> 마스터계정 위임 후에 탈퇴처리가 가능합니다.'


// 업체정보수정
myinfo.label.company.title = '회사 정보'
myinfo.label.company.title2 = '회사정보 수정'
myinfo.label.company.compNmKo = '회사명 (국문)'
myinfo.label.company.compNmEn = '회사명 (영문)'
myinfo.label.company.compNo = '사업자등록번호'
myinfo.label.company.compTel = '대표전화'
myinfo.label.company.compRepresentNm = '대표자 명'
myinfo.label.company.compUrl = '회사 웹사이트'
myinfo.label.company.compPostNo = '우편번호'
myinfo.label.company.compAddrKo = '주소 (국문)'
myinfo.label.company.compAddrEn = '주소 (영문)'
myinfo.label.company.reg_charger = '담당 연구원'
myinfo.label.company.compDesc = '회사 특징'
myinfo.label.company.btnModify = '수정'
myinfo.label.company.btnCancel = '취소'
myinfo.label.company.btnSave = '저장'

myinfo.msg.company.desc1 = '소속회사정보가 없습니다.'
myinfo.msg.company.desc2 = '입력하신 내용을 저장하시겠습니까?'
myinfo.msg.company.desc3 = '대표자 명이 없습니다'


// 비밀번호 체크 팝업
myinfo.label.chkPwd.title = '비밀번호 확인'
myinfo.label.chkPwd.cancel = '취소'
myinfo.label.chkPwd.submit = '확인'
myinfo.label.chkPwd.close = '팝업창 닫기'

myinfo.msg.chkPwd.desc1 = '비밀번호를 입력하세요'
myinfo.msg.chkPwd.desc2 = '비밀번호를 확인하세요'

export default myinfo
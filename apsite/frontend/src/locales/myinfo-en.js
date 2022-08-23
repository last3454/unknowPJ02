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
myinfo.label.profile.title = 'Personal Information'
myinfo.label.profile.title2 = 'Edit'
myinfo.label.profile.userNm = 'Name'
myinfo.label.profile.mobileNo = 'Mobile Number'
myinfo.label.profile.mobileNo2 = 'Mobile Number (without -)'
myinfo.label.profile.loginId = 'ID'
myinfo.label.profile.loginPwd = 'Password'
myinfo.label.profile.email = 'E-mail Address'
myinfo.label.profile.btnModify = 'Edit'
myinfo.label.profile.btnCancel = 'Cancel'
myinfo.label.profile.btnSave = 'Save'
myinfo.label.profile.chgPwd = 'Change Password'
myinfo.label.profile.userCompDeptNm = 'Company Department'

myinfo.msg.profile.desc1 = 'There is no registered information'
myinfo.msg.profile.desc2 = 'Please enter your mobile number'
myinfo.msg.profile.desc3 = 'Please enter your e-mail address'
myinfo.msg.profile.desc4 = 'Please enter a valid e-mail address'
myinfo.msg.profile.desc5 = 'Are you sure you want to save your entries?'
myinfo.msg.profile.desc6 = 'Are you sure you want to cancel your input?'
myinfo.msg.profile.desc7 = 'Please enter a new password'
myinfo.msg.profile.desc8 = 'There is no e-mail'
myinfo.msg.profile.desc9 = 'There is no company department'
myinfo.msg.profile.desc10 = 'Please enter your company department'

// 업체정보수정
myinfo.label.company.title = 'Company Information'
myinfo.label.company.title2 = 'Edit Company Information'
myinfo.label.company.compNmKo = 'Company Name (Korean)'
myinfo.label.company.compNmEn = 'Company Name (English)'
myinfo.label.company.compNo = 'Company Registration Number'
myinfo.label.company.compTel = 'Main Phone'
myinfo.label.company.compRepresentNm = 'Representative Name'
myinfo.label.company.compUrl = 'Company Website'
myinfo.label.company.compPostNo = 'Zip Code'
myinfo.label.company.compAddrKo = 'Address (Korean)'
myinfo.label.company.compAddrEn = 'Address (English)'
myinfo.label.company.reg_charger = 'Researcher in charge'
myinfo.label.company.compDesc = 'Company Features'
myinfo.label.company.btnModify = 'Edit'
myinfo.label.company.btnCancel = 'Cancel'
myinfo.label.company.btnSave = 'Save'

myinfo.msg.company.desc1 = 'There is No company information'
myinfo.msg.company.desc2 = 'Are you sure you want to save your entries?'
myinfo.msg.company.desc3 = 'There is no representative'

// 비밀번호 체크 팝업
myinfo.label.chkPwd.title = 'Confirm Password'
myinfo.label.chkPwd.cancel = 'Cancel'
myinfo.label.chkPwd.submit = 'Confirm'
myinfo.label.chkPwd.close = 'Close'

myinfo.msg.chkPwd.desc1 = 'Please enter a password.'
myinfo.msg.chkPwd.desc2 = 'Check your password.'

export default myinfo
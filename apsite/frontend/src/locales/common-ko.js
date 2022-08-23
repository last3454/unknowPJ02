const common = {
  label: {},
  msg: {}
}

common.label.request = '요청'
common.label.name = '이름'
common.label.alert_ok = '확인'
common.label.confirm_ok = '확인'
common.label.confirm_cancel = '취소'
common.label.register = '등록'
common.label.modify = '수정'
common.label.save = '저장'
common.label.temp_save = '임시저장'
common.label.list = '목록'
common.label.cancel = '취소'
common.label.delete = '삭제'
common.label.header_welcom_msg = '<em>{userNm}[{loginId}]</em>님 안녕하세요'
common.label.header_last_login = 'Last Login: {dt} / IP: {ip}'
common.label.header_logout = '로그아웃'
common.label.search = "검색하기"
common.label.search2 = "조회"
common.label.search3 = "찾아보기"
common.label.seq = "연번"
common.label.reg_user = "등록자명"
common.label.korean = "국문"
common.label.english = "영문"
common.label.chinese = "중문"
common.label.website = "웹사이트"
common.label.postno = "우편번호"
common.label.select = "선택"
common.label.basic_info = "기본정보"
common.label.essential = "필수"
common.label.next = "다음 단계"
common.label.multi_select = "복수 선택"
common.label.classify = "분류"
common.label.add = "추가"
common.label.online = '온라인'
common.label.offline = '오프라인'
common.label.view_cnt = '조회수'
common.label.attach1 = '파일첨부'
common.label.attach2 = '첨부파일'
common.label.download = "다운로드"
common.label.pop_close = "팝업창 닫기"
common.label.close = "닫기"
common.label.approval = "승인"
common.label.reject = "반려"
common.label.classification = "구분"
common.label.number = "번호"
common.label.state = "상태"
common.label.etc = "기타"
common.label.direct_regist = "직접입력"
common.label.complete = "완료"
common.label.mod_his = "변경이력"
common.label.attach_nm = '파일명'
common.label.date = "날짜"
common.label.input = "입력"
common.label.fixed_yn = "확정여부"
common.label.fixed_y = "확정"
common.label.fixed_n = "미확정"
common.label.prev_page = "이전 페이지"
common.label.home = "홈으로 이동"

common.msg.search_msg = "검색어를 입력하세요."
common.msg.no_data = "데이터가 존재하지 않습니다."
common.msg.no_auth = "권한이 없습니다."
common.msg.essential = "필수 입력항목 입니다."
common.msg.only_number = "숫자만 입력하세요."
common.msg.byte_msg = "{byteSize}자 이내로 작성해주세요."
common.msg.byte_msg2 = "내용이 {byteSize} Byte를 초과할 수 없습니다."
common.msg.save_cofirm_msg = "저장하시겠습니까?"
common.msg.delete_confirm_msg = '정말로 삭제 하시겠습니까?'
common.msg.delete_ok = '삭제 되었습니다.'
common.msg.temp_save_ok = '임시저장 완료되었습니다.'
common.msg.process_ok = '처리 완료되었습니다.'
common.msg.process_ok2 = "처리되었습니다."
common.msg.save_ok = '등록 완료되었습니다.'
common.msg.save_ok2 = "저장되었습니다."
common.msg.modify_ok = '수정 완료되었습니다.'
common.msg.select_only = '하나만 선택 가능합니다.'
common.msg.search_reg_required = "검색하여 입력해 주세요."
common.msg.min_date = "이전 날짜는 입력하실 수 없습니다."
common.msg.content_required = "내용을 입력하세요."
common.msg.more_than = "0 이상 입력해 주세요."
common.msg.only_image = "이미지를 첨부해 주세요."
common.msg.check_essential_item = "필수 입력항목을 확인해 주세요."
common.msg.check_file_ext = "업로드 불가능한 확장 파일을 포함하고 있습니다."
common.msg.etc_content_required = "기타 내용을 입력하세요."
common.msg.del_comment = "삭제된 댓글입니다."
common.msg.no_essential_data = "필수값이 없습니다.<br>관리자에게 문의하시기 바랍니다."
common.msg.upload_count_msg = "해당 첨부파일은 {count}개 이상 업로드하실 수 없습니다."
common.msg.save_fail = "저장에 실패했습니다.<br>관리자에게 문의하시기 바랍니다."
common.msg.server_err_msg = "작업중 오류가 발생하였습니다."
common.msg.password_rule = "사용 가능한 특수문자 : "
common.msg.request_complete = "요청 완료되었습니다."

common.msg.login_0001 = "아이디/비밀번호를 확인해 주세요."
common.msg.login_0002 = "아이디/비밀번호가 {fail}회 틀렸습니다."
common.msg.login_0003 = "비밀번호 재설정이 필요합니다."
common.msg.login_0004 = "아이디/비밀번호가 5회 이상 틀렸습니다. <p class=\"txt\">비밀번호 재 설정후 로그인 해주시기 바랍니다.</p>"
common.msg.login_0005 = "기존 비밀번호를 제외한 신규 비밀번호로 입력 부탁드립니다."
common.msg.login_0006 = "기존 비밀번호와 동일합니다. </br>다른 비밀 번호로 변경 부탁드립니다."
common.msg.login_0007 = "기존에 변경했던 비밀번호와 동일합니다. </br>다른 비밀 번호로 변경 부탁드립니다."
common.msg.login_0008 = "비밀번호 변경이 필요합니다."
common.msg.login_0009 = "회원님은 <span class='txt-type01'>3개월 이상 동일한 비밀번호</span>를 사용하고 계십니다.<br/>소중한 개인 정보보호를 위해 비밀번호를 주기적으로 변경해 주세요."
common.msg.login_0010 = "현재 비밀번호가 일치하지 않습니다."
common.msg.login_0011 = "장기 미 접속으로 인해 <strong>휴면회원</strong> 전환 상태입니다. <br/>휴면 해제를 진행 하시겠습니까?"
common.msg.login_0012 = "비밀번호가 7일 이후에 만료 예정입니다. </br> 비밀번호 변경이 필요합니다."
common.msg.login_0013 = "동일한 아이디로 로그인 된 이력이 존재합니다. <p class=\"txt\">접속 하시겠습니까?</p>"
common.msg.login_0014 = "비밀번호가 5회 이상 틀렸습니다. <p class=\"txt\">사이트 담당자에게 비밀번호 초기화를 요청해주세요.</p>"
common.msg.login_0015 = "인증 정보가 올바르지 않습니다. </br>관리자에게 문의해 주시기 바랍니다."

common.msg.error_0001 = "죄송합니다. <br/>일시적 오류가 발생했습니다."
common.msg.error_0002 = "죄송합니다. <br/>요청하신 페이지를 찾을 수 없습니다."
common.msg.error_0003 = "웹페이지 주소가 바뀌었거나 잘못 입력하신 경우입니다. <br/>입력하신 페이지 주소가 정확한지 다시 한번 확인해 보시거나 새로고침해 보시기 바랍니다."
common.msg.error_0004 = "계속 같은 문제가 반복적으로 발생할 경우 고객센터로 문의해 주세요."
common.msg.error_0005 = "페이지 확인 후 다시 접속해 주시기 바랍니다."
common.msg.error_0006 = "계속 같은 문제가 반복적으로 발생할 경우 고객센터로 문의해 주세요."

export default common

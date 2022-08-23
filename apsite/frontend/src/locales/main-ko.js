// common-ko.js 파일 참고
const main = {
  label: {
    notice: {},
    communication: {},
    seminar: {},
    company: {},
    ingredient:{},
    footer: {}
  },
  msg: {
    notice: {},
    communication: {},
    seminar: {},
    company: {},
    ingredient:{}
  }
}

// 공지사항
main.label.notice.title = "공지사항"

// Communication
main.label.communication.title = "Communication"
main.label.communication.material = "소통원료 목록"
main.label.communication.mine = "나의 댓글 목록"
main.label.communication.raw_nm = "원료명"
main.label.communication.manu_nm = "업체명"
main.label.communication.go_detail = "바로가기"

main.msg.communication.no_list = "소통원료 목록이 없습니다."
main.msg.communication.no_move = "해당 댓글로 이동할 수 없습니다."
main.msg.communication.no_user = "계정정보가 없습니다."
main.msg.communication.no_content = "컨텐츠 준비중입니다."

// 세미나 일정
main.label.seminar.title = "세미나 일정"
main.label.seminar.to_list = "전체 보기"

main.msg.seminar.no_seminar = "세미나 정보가 없습니다."

// 회사 계정 리스트
main.label.company.title = "회사 계정 리스트"

// MY 원료리스트
main.label.ingredient.title = "MY 원료리스트"
main.label.ingredient.reg = "신규등록"

// Footer
main.label.footer.desc0 = "대표이사 안세홍"
main.label.footer.desc1 = "사업자등록번호 106-86-43373"
main.label.footer.desc2 = "대표번호 02-000-000"

export default main
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
main.label.notice.title = "Notice"

// Communication
main.label.communication.title = "Communication"
main.label.communication.material = "List of products in Communication"
main.label.communication.mine = "My comment list"
main.label.communication.raw_nm = "product name"
main.label.communication.manu_nm = "company name"
main.label.communication.go_detail = "Go detail"

main.msg.communication.no_list = "There is no list of products in Communication."
main.msg.communication.no_move = "You cannot move to that comment."
main.msg.communication.no_user = "Account information is missing."
main.msg.communication.no_content = "There is no list of products being communicated"

// 세미나 일정
main.label.seminar.title = "Seminar Schedule"
main.label.seminar.to_list = "Full view"

main.msg.seminar.no_seminar = "There is no seminar information available"

// 회사 계정 리스트
main.label.company.title = "Company Account List"

// MY 원료리스트
main.label.ingredient.title = "My product list"
main.label.ingredient.reg = "New"

// Footer
main.label.footer.desc0 = "president Ahn Se-hong"
main.label.footer.desc1 = "Business registration number 106-86-43373"
main.label.footer.desc2 = "Representative number 02-000-000"

export default main
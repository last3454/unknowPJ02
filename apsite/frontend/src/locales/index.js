import boardEn from './board-en'
import boardKo from './board-ko'
import commonEn from './common-en'
import commonKo from './common-ko'
import companyEn from './company-en'
import companyKo from './company-ko'
import documentEn from './document-en'
import documentKo from './document-ko'
import ingredientEn from './ingredient-en'
import ingredientKo from './ingredient-ko'
import myinfoEn from './myinfo-en'
import myinfoKo from './myinfo-ko'
import popupEn from './popup-en'
import popupKo from './popup-ko'
import joinEn from './join-en'
import joinKo from './join-ko'
import mainEn from './main-en'
import mainKo from './main-ko'
import privacyEn from './privacy-en'
import privacyKo from './privacy-ko'

const ko = {
  common: commonKo,
  board: boardKo,
  company: companyKo,
  document: documentKo,
  ingredient: ingredientKo,
  myinfo: myinfoKo,
  popup: popupKo,
  join: joinKo,
  main: mainKo,
  privacy: privacyKo
}

const en = {
  common: commonEn,
  board: boardEn,
  company: companyEn,
  document: documentEn,
  ingredient: ingredientEn,
  myinfo: myinfoEn,
  popup: popupEn,
  join: joinEn,
  main: mainEn,
  privacy: privacyEn
}

const messages = {
  ko,
  en
}

export default messages
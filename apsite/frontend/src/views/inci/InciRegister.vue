<template>
  <div class="box inci">
    <div class="box-cont">
      <div class="cont-top cont-top2">
        <h2 class="cont-top-title">성분 등록</h2>
        <div class="search-wrap p-right">
          <fieldset>
            <legend class="hidden">성분 등록 검색</legend>
            <div class="search-select">
              <select name="" id="" class="slt">
                <option value="select" disabled selected>select</option>
                <option value="성분 별">성분 별</option>
                <option value="무소구 항목별">무소구 항목별</option>
              </select>
            </div>
            <div class="search-input">
              <input type="search" placeholder="검색어를 입력하세요." />
              <button type="button" class="search-btn" title="검색하기">
                검색하기
              </button>
            </div>
          </fieldset>
        </div>
      </div>
      <div class="cont-mid">
        <div class="cont-table-top clearfix">
          <div class="btn-wrap f-left">
            <a href="javascript:;" class="btn typeA small"
              ><span class="import">원료 불러오기</span></a
            >
            <a href="javascript:;" class="btn typeA small"
              ><span class="import">내용물 불러오기</span></a
            >
            <a href="javascript:;" class="btn typeC gray-overall small"
              ><span class="complete">양식 저장</span></a
            >
            <a href="javascript:;" class="btn typeC gray-overall small"
              ><span class="import">양식 불러오기</span></a
            >
          </div>
          <div class="btn-wrap f-right">
            <a href="javascript:;" class="btn typeA green small"
              ><span class="down">엑셀 양식 다운로드</span></a
            >
            <a href="javascript:;" class="btn typeA green small"
              ><span class="up">엑셀 일괄 업로드</span></a
            >
            <a href="javascript:;" class="btn typeB sky small"
              ><span>항목 추가</span></a
            >
            <a href="javascript:;" class="btn typeB sky small"
              ><span>항목 삭제</span></a
            >
          </div>
        </div>
        <div class="cont-scrollX" data-width="1646">
          <!-- 항목추가될 때마다 134씩 증가 -->
          <div class="cont-scrollX-bar"><div class="bar"></div></div>
          <div class="cont-scrollX-wrap">
            <div class="cont-scrollX-area">
              <table class="tb typeA typeA__input small">
                <caption class="hidden">
                  성분 등록
                </caption>
                <thead>
                  <tr>
                    <th class="wf-108 inci-tit sap">
                      <div class="inci-sort-tit">
                        <a href="javascript:;" class="inci-sort-tit-ico"
                          >ZPLM34E<br />성분리스트</a
                        >
                      </div>
                    </th>
                    <th class="wf-108 inci-tit sap">
                      <div class="inci-sort-tit">
                        <a href="javascript:;" class="inci-sort-tit-ico"
                          >AP 향성분<br />평가</a
                        >
                      </div>
                    </th>
                    <th class="wf-108 inci-tit sap">
                      <div class="inci-sort-tit">
                        <a href="javascript:;" class="inci-sort-tit-ico"
                          >기능(영문)</a
                        >
                      </div>
                    </th>
                    <th class="wf-108 inci-tit sap">
                      <div class="inci-sort-tit">
                        <a href="javascript:;" class="inci-sort-tit-ico"
                          >영문표시명</a
                        >
                      </div>
                    </th>
                    <th class="wf-108 inci-tit">
                      <div class="input-check-tb">
                        <input
                          type="checkbox"
                          class="chk typeA white"
                          id="inci_reg_chk1"
                        />
                        <label for="inci_reg_chk1"></label>
                      </div>
                      <div class="inci-sort-tit">
                        <a href="javascript:;" class="inci-sort-tit-ico"
                          >동물성 원료</a
                        >
                      </div>
                    </th>
                    <th class="wf-108 inci-tit">
                      <div class="input-check-tb">
                        <input
                          type="checkbox"
                          class="chk typeA white"
                          id="inci_reg_chk2"
                        />
                        <label for="inci_reg_chk2"></label>
                      </div>
                      <div class="inci-sort-tit">
                        <a href="javascript:;" class="inci-sort-tit-ico"
                          >광물성 오일</a
                        >
                      </div>
                    </th>
                    <th class="wf-108 inci-tit">
                      <div class="input-check-tb">
                        <input
                          type="checkbox"
                          class="chk typeA white"
                          id="inci_reg_chk3"
                        />
                        <label for="inci_reg_chk3"></label>
                      </div>
                      <div class="inci-sort-tit">
                        <a href="javascript:;" class="inci-sort-tit-ico"
                          >오일</a
                        >
                      </div>
                    </th>
                    <th class="wf-108 inci-tit">
                      <div class="input-check-tb">
                        <input
                          type="checkbox"
                          class="chk typeA white"
                          id="inci_reg_chk4"
                        />
                        <label for="inci_reg_chk4"></label>
                      </div>
                      <div class="inci-sort-tit">
                        <a href="javascript:;" class="inci-sort-tit-ico">PEG</a>
                      </div>
                    </th>
                    <th class="wf-108 inci-tit">
                      <div class="input-check-tb">
                        <input
                          type="checkbox"
                          class="chk typeA white"
                          id="inci_reg_chk5"
                        />
                        <label for="inci_reg_chk5"></label>
                      </div>
                      <div class="inci-sort-tit">
                        <a href="javascript:;" class="inci-sort-tit-ico"
                          >댈크</a
                        >
                      </div>
                    </th>
                    <th class="wf-108 inci-tit">
                      <div class="input-check-tb">
                        <input
                          type="checkbox"
                          class="chk typeA white"
                          id="inci_reg_chk6"
                        />
                        <label for="inci_reg_chk6"></label>
                      </div>
                      <div class="inci-sort-tit">
                        <a href="javascript:;" class="inci-sort-tit-ico"
                          >트리클로산</a
                        >
                      </div>
                    </th>
                    <th class="wf-108 inci-tit">
                      <div class="input-check-tb">
                        <input
                          type="checkbox"
                          class="chk typeA white"
                          id="inci_reg_chk7"
                        />
                        <label for="inci_reg_chk7"></label>
                      </div>
                      <div class="inci-sort-tit">
                        <a href="javascript:;" class="inci-sort-tit-ico"
                          >실리카</a
                        >
                      </div>
                    </th>
                    <th class="wf-108 inci-tit">
                      <div class="input-check-tb">
                        <input
                          type="checkbox"
                          class="chk typeA white"
                          id="inci_reg_chk8"
                        />
                        <label for="inci_reg_chk8"></label>
                      </div>
                      <div class="inci-sort-tit">
                        <a href="javascript:;" class="inci-sort-tit-ico"
                          >불소</a
                        >
                      </div>
                    </th>
                    <th class="wf-108 inci-tit">
                      <div class="input-check-tb">
                        <input
                          type="checkbox"
                          class="chk typeA white"
                          id="inci_reg_chk9"
                        />
                        <label for="inci_reg_chk9"></label>
                      </div>
                      <div class="inci-sort-tit">
                        <a href="javascript:;" class="inci-sort-tit-ico"
                          >암모니아</a
                        >
                      </div>
                    </th>
                    <th class="wf-108 inci-tit">
                      <div class="input-check-tb">
                        <input
                          type="checkbox"
                          class="chk typeA white"
                          id="inci_reg_chk10"
                        />
                        <label for="inci_reg_chk10"></label>
                      </div>
                      <div class="inci-sort-tit">
                        <a href="javascript:;" class="inci-sort-tit-ico"
                          >파라벤</a
                        >
                      </div>
                    </th>
                    <th class="wf-134 inci-tit add">
                      <div class="input-check-tb">
                        <input
                          type="checkbox"
                          class="chk typeA white"
                          id="inci_reg_chk11"
                        />
                        <label for="inci_reg_chk11"></label>
                      </div>
                      <div>
                        <div class="cont-input-wrap__flex">
                          <input type="text" />
                          <a href="javascript:;" class="inci-sort-tit-ico"
                            >필터 정렬</a
                          >
                        </div>
                      </div>
                    </th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td class="t-left sap">10001</td>
                    <td class="t-left sap"></td>
                    <td class="t-left sap"></td>
                    <td class="t-left sap"></td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left td-input">
                      <input type="text" value="0" />
                    </td>
                  </tr>
                  <tr>
                    <td class="t-left sap">10002</td>
                    <td class="t-left sap"></td>
                    <td class="t-left sap"></td>
                    <td class="t-left sap"></td>
                    <td class="t-left">0</td>
                    <td class="t-left">불가</td>
                    <td class="t-left">불가</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left td-input"><input type="text" /></td>
                  </tr>
                  <tr>
                    <td class="t-left sap">10003</td>
                    <td class="t-left sap">정보없음</td>
                    <td class="t-left sap"></td>
                    <td class="t-left sap"></td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left td-input"><input type="text" /></td>
                  </tr>
                  <tr>
                    <td class="t-left sap">10004</td>
                    <td class="t-left sap">정보없음</td>
                    <td class="t-left sap"></td>
                    <td class="t-left sap"></td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left td-input"><input type="text" /></td>
                  </tr>
                  <tr>
                    <td class="t-left sap">10005</td>
                    <td class="t-left sap">정보없음</td>
                    <td class="t-left sap"></td>
                    <td class="t-left sap"></td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left td-input"><input type="text" /></td>
                  </tr>
                  <tr>
                    <td class="t-left sap">10006</td>
                    <td class="t-left sap">정보없음</td>
                    <td class="t-left sap"></td>
                    <td class="t-left sap"></td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left td-input"><input type="text" /></td>
                  </tr>
                  <tr>
                    <td class="t-left sap">10007</td>
                    <td class="t-left sap">정보없음</td>
                    <td class="t-left sap"></td>
                    <td class="t-left sap"></td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left td-input"><input type="text" /></td>
                  </tr>
                  <tr>
                    <td class="t-left sap">10008</td>
                    <td class="t-left sap">정보없음</td>
                    <td class="t-left sap"></td>
                    <td class="t-left sap"></td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left td-input"><input type="text" /></td>
                  </tr>
                  <tr>
                    <td class="t-left sap">10009</td>
                    <td class="t-left sap">정보없음</td>
                    <td class="t-left sap"></td>
                    <td class="t-left sap"></td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left td-input"><input type="text" /></td>
                  </tr>
                  <tr>
                    <td class="t-left sap">10010</td>
                    <td class="t-left sap"></td>
                    <td class="t-left sap"></td>
                    <td class="t-left sap"></td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left td-input"><input type="text" /></td>
                  </tr>
                  <tr>
                    <td class="t-left sap">10011</td>
                    <td class="t-left sap"></td>
                    <td class="t-left sap"></td>
                    <td class="t-left sap"></td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left td-input">
                      <div class="select-wrap">
                        <select name="" id="" class="slt">
                          <option value="검토" selected>검토</option>
                          <option value="불가">불가</option>
                        </select>
                      </div>
                    </td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left td-input"><input type="text" /></td>
                  </tr>
                  <tr>
                    <td class="t-left sap">10012</td>
                    <td class="t-left sap">정보없음</td>
                    <td class="t-left sap"></td>
                    <td class="t-left sap"></td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">검토</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left td-input"><input type="text" /></td>
                  </tr>
                  <tr>
                    <td class="t-left sap">10013</td>
                    <td class="t-left sap">정보없음</td>
                    <td class="t-left sap"></td>
                    <td class="t-left sap"></td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left td-input"><input type="text" /></td>
                  </tr>
                  <tr>
                    <td class="t-left sap">10014</td>
                    <td class="t-left sap">정보없음</td>
                    <td class="t-left sap"></td>
                    <td class="t-left sap"></td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left td-input"><input type="text" /></td>
                  </tr>
                  <tr>
                    <td class="t-left sap">10015</td>
                    <td class="t-left sap">정보없음</td>
                    <td class="t-left sap"></td>
                    <td class="t-left sap"></td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left">0</td>
                    <td class="t-left td-input"><input type="text" /></td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
      <div class="cont-bot">
        <div class="btn-wrap right">
          <a href="../infra/i_list.html" class="btn typeB gray"
            ><span>취소</span></a
          >
          <a
            href="javascript:;"
            onclick="modalPopup('#popReg')"
            class="btn typeB"
            ><span class="complete">저장</span></a
          >
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { inject, reactive, onMounted } from 'vue'
import { useInci } from '@/compositions/useInci'
export default {
  name: 'InciRegister',
  components: {
  },
  setup() {
    const {
      fnWidthScroll
    } = useInci()    

    onMounted(() => {
      fnWidthScroll()
    })
    return {}
  },
}
</script>

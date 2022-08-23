<template>
  <div class="box">
    <div class="box-cont">
      <div class="cont-top cont-top2">
        <h2 class="cont-top-title">성분 목록</h2>
        <div class="search-wrap p-right">
          <fieldset>
            <legend class="hidden">성분 목록 검색</legend>
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
        <div class="cont-table">
          <table class="tb typeA small">
            <caption class="hidden">
              성분 목록
            </caption>
            <thead>
              <tr>
                <th class="w-5">연번</th>
                <th>성분명(영문)</th>
                <th class="w-10">성분명(국문)</th>
                <th class="w-8">성분코드</th>
                <th>INCI명</th>
                <th class="w-20">CAS번호</th>
                <th class="w-7">등록일</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>121</td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >Copper Powder/CI 77400</a
                  >
                </td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >구리가루</a
                  >
                </td>
                <td class="t-left">AP12345-123</td>
                <td class="t-left">Copper Powder/CI 77400</td>
                <td class="t-left">7440-50-8</td>
                <td>2018-12-24</td>
              </tr>
              <tr>
                <td>120</td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >Diatomaceous Earth</a
                  >
                </td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >규조토</a
                  >
                </td>
                <td class="t-left">AP12345-123</td>
                <td class="t-left">Diatomaceous Earth</td>
                <td class="t-left">68855-54-9 / 61790-53-2</td>
                <td>2018-12-21</td>
              </tr>
              <tr>
                <td>119</td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >Disodium Phosphate</a
                  >
                </td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >디소듐포스페이트</a
                  >
                </td>
                <td class="t-left">AP12345-123</td>
                <td class="t-left">Disodium Phosphate</td>
                <td class="t-left">7558-79-4 / 7782-85-6</td>
                <td>2018-12-12</td>
              </tr>
              <tr>
                <td>118</td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >Dicalcium Phosphate</a
                  >
                </td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >디칼슘포스페이트</a
                  >
                </td>
                <td class="t-left">AP12345-123</td>
                <td class="t-left">Dicalcium Phosphate</td>
                <td class="t-left">7747-93-9</td>
                <td>2018-12-10</td>
              </tr>
              <tr>
                <td>117</td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >Dicalcium phosphate dihydrate</a
                  >
                </td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >디칼슘포스페이트디하이드레이트</a
                  >
                </td>
                <td class="t-left">AP12345-123</td>
                <td class="t-left">Dicalcium phosphate dihydrate</td>
                <td class="t-left">7789-77-7</td>
                <td>2018-11-24</td>
              </tr>
              <tr>
                <td>116</td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >Magnesium Sulfate</a
                  >
                </td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >마그네슘설페이트</a
                  >
                </td>
                <td class="t-left">AP12345-123</td>
                <td class="t-left">Magnesium Sulfate</td>
                <td class="t-left">
                  10034-99-8(heptahydrate) / 18939-43-0 / 7487-88-9
                </td>
                <td>2018-11-21</td>
              </tr>
              <tr>
                <td>115</td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >Magnesium Silicate</a
                  >
                </td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >마그네슘실리케이트</a
                  >
                </td>
                <td class="t-left">AP12345-123</td>
                <td class="t-left">Magnesium Silicate</td>
                <td class="t-left">1343-88-0</td>
                <td>2018-11-12</td>
              </tr>
              <tr>
                <td>114</td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >Magnesium Aluminium Silicate</a
                  >
                </td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >마그네슘알루미늄실리케이트</a
                  >
                </td>
                <td class="t-left">AP12345-123</td>
                <td class="t-left">Magnesium Aluminium Silicate</td>
                <td class="t-left">12199-37-0 / 12511-31-8</td>
                <td>2018-11-10</td>
              </tr>
              <tr>
                <td>113</td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >Magnesium Oxide / CI 77711</a
                  >
                </td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >마그네슘옥사이드</a
                  >
                </td>
                <td class="t-left">AP12345-123</td>
                <td class="t-left">Magnesium Oxide / CI 77711</td>
                <td class="t-left">1309-48-4</td>
                <td>2018-10-24</td>
              </tr>
              <tr>
                <td>112</td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >Magnesium Carbonate / CI 77713 / Magnesite</a
                  >
                </td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >마그네슘카보네이트</a
                  >
                </td>
                <td class="t-left">AP12345-123</td>
                <td class="t-left">
                  Magnesium Carbonate / CI 77713 / Magnesite
                </td>
                <td class="t-left">546-93-0 / 7757-69-9</td>
                <td>2018-12-24</td>
              </tr>
              <tr>
                <td>111</td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >Magnesium Chloride</a
                  >
                </td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >마그네슘클로라이드</a
                  >
                </td>
                <td class="t-left">AP12345-123</td>
                <td class="t-left">Magnesium Chloride</td>
                <td class="t-left">7786-30-3 / 7791-18-6(hydrate)</td>
                <td>2018-10-12</td>
              </tr>
              <tr>
                <td>110</td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >Magnesium Carbonate Hydroxide</a
                  >
                </td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >마그네슘카보네이트하이드록사이드</a
                  >
                </td>
                <td class="t-left">AP12345-123</td>
                <td class="t-left">Magnesium Carbonate Hydroxide</td>
                <td class="t-left">7760-50-1 / 39409-82-0 / 12125-28-9</td>
                <td>2018-10-10</td>
              </tr>
              <tr>
                <td>109</td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >Magnesium Hydroxide</a
                  >
                </td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >마그네슘하이드록사이드</a
                  >
                </td>
                <td class="t-left">AP12345-123</td>
                <td class="t-left">Magnesium Hydroxide</td>
                <td class="t-left">1309-42-8</td>
                <td>2018-09-24</td>
              </tr>
              <tr>
                <td>108</td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >Mica</a
                  >
                </td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >마이카</a
                  >
                </td>
                <td class="t-left">AP12345-123</td>
                <td class="t-left">Mica</td>
                <td class="t-left">12001-26-2</td>
                <td>2018-09-21</td>
              </tr>
              <tr>
                <td>107</td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >Malachite</a
                  >
                </td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >말라카이트</a
                  >
                </td>
                <td class="t-left">AP12345-123</td>
                <td class="t-left">Copper Powder/CI 77400</td>
                <td class="t-left">1319-53-5</td>
                <td>2018-09-12</td>
              </tr>
              <tr>
                <td>106</td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >Manganese bis orthophosphate / CI 77745</a
                  >
                </td>
                <td class="t-left">
                  <a
                    href="../infra/i_detail.html"
                    title="성분 정보 조회"
                    class="t-link"
                    >망가니즈비스오르토포스페이트</a
                  >
                </td>
                <td class="t-left">AP12345-123</td>
                <td class="t-left">Manganese bis orthophosphate / CI 77745</td>
                <td class="t-left">10124-54-6</td>
                <td>2018-09-10</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div class="cont-bot">
        <pagination :page-info="page" @go-page-num="fnSearch" />
        <div class="btn-wrap right">
          <a href="#" class="btn typeB" @click.prevent="fnLinkReg()"><span class="reg">성분 등록</span></a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { defineAsyncComponent, inject, reactive, onMounted } from 'vue'
export default {
  name: 'InciList',
  components: {
    Pagination: defineAsyncComponent(() => import('@/components/comm/Pagination.vue'))
  },
  setup() {

    const fnLinkReg = () => {
      alert('등록 페이지 개발중')
    }

    const fnSearch = () => {

    }

    return {
      fnSearch,
      fnLinkReg
    }
  },
}
</script>

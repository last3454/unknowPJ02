<template>
  <li class="sec-input-wrap">
    <div class="sec-input-wrap__block">
      <h3 class="sec-input-tit">기술정보 제공 요구 동의서 · 비밀 유지 계약서</h3>
      <ul class="sec-input-list">
        <li class="cont-input-list-item">
            <dl>
              <dt class="cont-input-tit">기술정보 제공 요구동의서, 비밀 유지 계약서 내용</dt>
                <dd class="cont-input-wrap">
                    <div id="agree_doc" class="cont-input-wrap__border">
                        <div class="ad-wrap">
                            <h4 class="ad-tit fs-h4 fw-medium fc-333 t-center">기술자료 요구서</h4>
                            <div class="ad-cont">
                                <table>
                                    <caption class="hidden">기술자료 요구서</caption>
                                    <tbody>
                                        <tr>
                                          <th colspan="8" class="tit fs-p1">1. 원사업자와 수급사업자</th>
                                        </tr>
                                        <tr>
                                          <th rowspan="4" class="t-center">원사업자</th>
                                          <th class="t-center">사업자명</th>
                                          <td colspan="2">㈜아모레퍼시픽</td>
                                          <th colspan="2" class="t-center">법인등록번호 또는 사업자등록번호</th>
                                          <td colspan="2">106-86-43373</td>
                                        </tr>
                                        <tr>
                                          <th class="t-center">대표자성명</th>
                                          <td colspan="2">안 세 홍</td>
                                          <th colspan="2" class="t-center">전화번호</th>
                                          <td colspan="2">{{ docInfo.ap_phone }}</td>
                                        </tr>
                                        <tr>
                                          <th class="t-center">주소</th>
                                          <td colspan="6">서울 용산구 한강대로 100</td>
                                        </tr>
                                        <tr>
                                          <th class="w-10 t-center">담당자</th>
                                          <th class="w-10 t-center">성명</th>
                                          <td class="w-15">{{ docInfo.ap_u_name }}</td>
                                          <th class="w-10 t-center">소속</th>
                                          <td class="w-15">{{ docInfo.ap_u_dept }}</td>
                                          <th class="w-10 t-center">전화번호</th>
                                          <td class="w-15">{{ docInfo.ap_u_phone }}</td>
                                        </tr>
                                        <tr>
                                          <th rowspan="4" class="t-center">수급사업자</th>
                                          <th class="t-center">사업자명</th>
                                          <td colspan="2">{{ docInfo.comp_name }}</td>
                                          <th colspan="2" class="t-center">법인등록번호 또는 사업자등록번호</th>
                                          <td colspan="2">{{ commonUtils.compNumFormat(docInfo.comp_num) }}</td>
                                        </tr>
                                        <tr>
                                          <th class="t-center">대표자성명</th>
                                          <td colspan="2">{{ docInfo.comp_rname }}</td>
                                          <th colspan="2" class="t-center">전화번호</th>
                                          <td colspan="2">{{ commonUtils.phoneFormat(docInfo.comp_phone) }}</td>
                                        </tr>
                                        <tr>
                                          <th class="t-center">주소</th>
                                          <td colspan="6">{{ docInfo.comp_addr }}</td>
                                        </tr>
                                        <tr>
                                          <th class="t-center">담당자</th>
                                          <th class="t-center">성명</th>
                                          <td>{{ docInfo.comp_u_name }}</td>
                                          <th class="t-center">소속</th>
                                          <td>{{ docInfo.comp_u_dept }}</td>
                                          <th class="t-center">전화번호</th>
                                          <td>{{ commonUtils.phoneFormat(docInfo.comp_u_phone) || docInfo.comp_u_phone }}</td>
                                        </tr>
                                        <tr>
                                          <th colspan="8" class="tit fs-p1">2. 기술자료 요구 관련 사항(증빙자료가 있는 경우 첨부)</th>
                                        </tr>
                                        <tr>
                                            <th colspan="2">1) 기술자료 내역</th>
                                            <td colspan="6">요구하는 기술정보․자료의 명칭과 범위 등 구체적 내역을 명시하여 기재(특허등본원부 등 기술자료에 대한 증빙자료 첨부)</td>
                                        </tr>
                                        <tr>
                                            <td colspan="8">원재료 성분표 및 조성비(SPEC, COA, CCPP, MSDS, TDS, 향/색소 정보 등), 원료 시험방법, 제조방법(공정도), 제품/작업표준서(지시서), 배합 요령 및 비율, 임상시험보고서</td>
                                        </tr>
                                        <tr>
                                            <th colspan="2">2) 요구 목적</th>
                                            <td colspan="6">원사업자가 기술자료를 요구하는 정당한 사유 기재</td>
                                        </tr>
                                        <tr>
                                            <td colspan="8">구성성분 검토, 원료 품질관리 항목 도출, 자사 안전관리 기준 부합 여부 확인, 원료 내 유해물질 여부 및 이슈 발생 가능성 검토, 국내 허가 및 수출 국가의 원료 관련 규정 준수여부 확인 및 대응, 품질 문제 발생시 원인 분석</td>
                                        </tr>
                                        <tr>
                                            <th colspan="2">3) 권리 귀속 관계</th>
                                            <td colspan="6">(ⅰ) 원사업자가 요구하는 기술자료의 현재 권리 귀속자, (ⅱ) 상호 간 기술이전계약 체결 여부, (ⅲ) 요구하는 기술이 공동개발한 기술인지 여부, (ⅳ) 기술자료가 제공된 후 권리귀속관계에 대한 상호 합의 사항 등</td>
                                        </tr>
                                        <tr>
                                            <td colspan="8">(i) 수급사업자, (ii) 체결사항 없음, (iii) 공동개발 기술 미해당, (iv) 별도 합의 없음</td>
                                        </tr>
                                        <tr>
                                            <th colspan="2">4) 대 가</th>
                                            <td colspan="6">기술자료 제공에 따른 대가에 대한 구체적 사항</td>
                                        </tr>
                                        <tr>
                                            <td colspan="8">별도 대가 지급 없음</td>
                                        </tr>
                                        <tr>
                                            <th colspan="2">5) 인도일 및 인도방법</th>
                                            <td colspan="6">당해 기술자료의 인도일, 구체적인 인도방법 등을 기재</td>
                                        </tr>
                                        <tr>
                                            <td colspan="8">기술자료 제공 합의 후 원사업자 요청일로부터 30일 이내에 별도 협의한 방식으로 인도함</td>
                                        </tr>
                                        <tr>
                                            <th colspan="2">6) 그 밖의 사항</th>
                                            <td colspan="6">기술자료 임치계약 체결 여부, 기술자료 요구 시 원사업자와 수급사업자간 기타 합의한 사항 등</td>
                                        </tr>
                                        <tr>
                                            <td colspan="8">기술자료 임치계약 미체결, 기타 합의 사항 없음</td>
                                        </tr>
                                        <tr>
                                            <td colspan="8">
                                                <p>원사업자 ㈜아모레퍼시픽과 수급사업자 <span>{{ docInfo.comp_name }}</span>는 <span>[{{ docInfo.raw_nm_ko }}] 관련</span> 원사업자의 기술자료 요구 시 위 사항을 상호 협의하여 정함을 확인하고, 위 사항이 기재된 본 서면을 교부하여 원사업자가 수급사업자에게 기술자료를 요구하는 바입니다.</p>
                                                <p class="t-center fw-medium fc-333">{{ docInfo.year }} 년&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; {{ docInfo.month }}월&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; {{ docInfo.date }}일</p>
                                                <table class="sign">
                                                    <caption class="hidden">기술자료 요구서 서명</caption>
                                                    <tbody>
                                                        <tr>
                                                          <th>원사업자명</th>
                                                          <td>㈜아모레퍼시픽</td>
                                                          <th>대표자</th>
                                                          <td>안 세 홍 (인)</td>
                                                        </tr>
                                                        <tr>
                                                          <th>수급사업자명</th>
                                                          <td>{{ docInfo.comp_name }}</td>
                                                          <th>대표자</th>
                                                          <td>{{ docInfo.comp_rname }}&nbsp;(인)</td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="ad-wrap">
                            <h4 class="ad-tit fs-h4 fw-medium fc-333 t-center">표준비밀유지계약서(본문)</h4>
                            <div class="ad-cont">
                                <p><span>[{{ docInfo.raw_nm_ko }}] 관련</span> 수급사업자의 기술자료 제공과 관련하여 원사업자와 수급사업자는 다음과 같이 비밀유지계약을 체결한다.</p>
                                <ol>
                                  <li>
                                    <dl>
                                      <dt class="fw-medium fc-333">제1조 (계약의 목적)</dt>
                                      <dd><p>이 계약은 수급사업자가 원사업자에게 수급사업자의 기술자료를 제공하는 경우 해당 기술자료를 비밀로 유지하고 보호하기 위하여 필요한 제반 사항을 규정함을 목적으로 한다.</p></dd>
                                    </dl>
                                  </li>
                                  <li>
                                      <dl>
                                          <dt class="fw-medium fc-333">제2조 (기술자료의 정의)</dt>
                                          <dd>
                                              <ol>
                                                  <li>
                                                      <p>① 이 계약에서 ‘기술자료’라 함은 수급사업자에 의해 비밀로 관리되고 있는 것으로서 다음 각 목의 어느 하나에 해당하는 정보∙자료를 말한다.</p>
                                                      <ol>
                                                          <li>
                                                              <p>가.	제조∙수리∙시공 또는 용역수행 방법에 관한 정보∙자료</p>
                                                          </li>
                                                          <li>
                                                              <p>나.	특허권, 실용신안권, 디자인권, 저작권 등의 지식재산권과 관련된 기술정보∙자료로서 수급사업자의 기술개발(R&D)∙생산∙영업활동에 기술적으로 유용하고 독립된 경제적 가치가 있는 것</p>
                                                          </li>
                                                          <li>
                                                              <p>다.	시공프로세스 매뉴얼, 장비 제원, 설계도면, 연구자료, 연구개발보고서 등 가목 또는 나목에 포함되지 않는 기타 사업자의 정보∙자료로서 수급사업자의 기술개발(R&D)∙생산∙영업활동에 기술적으로 유용하고 독립된 경제적 가치가 있는 것</p>
                                                          </li>
                                                      </ol>
                                                  </li>
                                                  <li>
                                                      <p>② 수급사업자가 기술자료를 제공함에 있어, 비밀임을 알리는 문구(비밀 또는 대외비 등의 국문 또는 영문 표시 등을 의미)가 표시되어 있지 아니하더라도 비밀로 관리되고 있는지 여부에는 영향을 미치지 아니한다.</p>
                                                  </li>
                                                  <li>
                                                      <p>③ 원사업자는 수급사업자의 기술자료가 비밀로 관리되고 있는지 여부(기술자료에서 제외되는지 여부)에 대해 의문이 있는 때에는 수급사업자에게 그에 대한 확인을 요청할 수 있다. 이 경우 수급사업자는 확인 요청을 받은 날로부터 15일 이내에 원사업자에게 해당 기술자료가 비밀로 관리되고 있는지 여부를 서면으로 발송하여야 한다.</p>
                                                  </li>
                                              </ol>
                                          </dd>
                                      </dl>
                                  </li>
                                  <li>
                                      <dl>
                                          <dt class="fw-medium fc-333">제3조 (기술자료의 목적외 사용금지)</dt>
                                          <dd>
                                              <ol>
                                                  <li><p>① 원사업자는 수급사업자의 기술자료를 「표준비밀유지계약서(별첨) 1-2.」에서 정한 목적으로만 사용하여야 한다.</p></li>
                                                  <li><p>② 원사업자는 수급사업자의 기술자료를 부당하게 제3자에게 제공하지 않는다. <span>다만, 원사업자의 계열사가 원사업자가 개발한 처방을 확인한 후 원료 활용 여부 결정 및 구매를 위한 자료로 활용하고자 하는 경우, 원사업자는 계열사에 이를 제공할 수 있다.</span></p></li>
                                                  <li><p>③ 원사업자는 「표준비밀유지계약서(별첨) 2.」에 기재되어 있는 임직원들에 한하여 수급사업자의 기술자료를 보유할 수 있도록 필요한 합리적인 조치를 취하여야 하며, 해당 임직원 각자에게 수급사업자의 기술자료에 대한 비밀유지의무를 주지시켜야 한다. 이때 수급사업자는 원사업자에게 해당 임직원으로부터 비밀유지서약서를 제출받는 등의 방법으로 해당 기술자료의 비밀성을 유지하기 위하여 필요한 합리적인 조치를 취해줄 것을 요구할 수 있다.</p></li>
                                              </ol>
                                          </dd>
                                      </dl>
                                  </li>
                                  <li>
                                    <dl>
                                      <dt class="fw-medium fc-333">제4조 (기술자료의 비밀유지 의무)</dt>
                                      <dd>
                                        <ol>
                                          <li><p>① 수급사업자가 사전에 서면(전자문서 포함)으로 동의하지 아니하는 경우, 원사업자는 제공받은 기술자료를 타인에게 누설하거나 공개하여서는 아니된다.</p></li>
                                          <li><p>② 원사업자는 수급사업자의 기술자료가 외부로 유출되는 것을 방지하기 위하여 물리적 설비 설치 및 내부비밀관리지침 마련, 정보보안교육 실시 등 기술자료를 보호하고 관리하는 데에 필요한 합리적인 조치를 취하여야 한다.</p></li>
                                        </ol>
                                      </dd>
                                    </dl>
                                  </li>
                                  <li>
                                    <dl>
                                      <dt class="fw-medium fc-333">제5조 (기술자료의 반환 또는 폐기방법)</dt>
                                      <dd>
                                        <p>「표준비밀유지계약서(별첨) 1-4.」에서 정한 기술자료의 반환일까지 원사업자는 수급사업자의 기술자료 원본을 즉시 수급사업자에게 반환하여야 하며, 일체의 복사본 등을 보유하여서는 아니된다. 단, 수급사업자의 선택에 의해 이를 반환하는 대신 폐기하는 경우에는 「표준비밀유지계약서(별첨) 1-4.」에서 정한 시점까지 이를 폐기하고, 원사업자는 그 폐기를 증명하는 서류를 수급사업자에게 제공하여야 한다.</p>
                                      </dd>
                                    </dl>
                                  </li>
                                  <li>
                                    <dl>
                                      <dt class="fw-medium fc-333">제6조 (권리의 부존재 등)</dt>
                                      <dd>
                                        <ol>
                                          <li><p>① 이 계약은 수급사업자의 기술자료를 제공받는 원사업자에게 기술자료에 관한 어떤 권리나 사용권을 부여하는 것으로 해석되지 않는다. 단, 원사업자가 「표준비밀유지계약서(별첨) 1-2.」에서 정한 목적에 따라 사용하는 경우에 대해서는 그러하지 아니하다.</p></li>
                                          <li><p>② 이 계약은 원사업자와 수급사업자 간에 향후 어떠한 확정적인 계약의 체결, 제조물의 판매나 구입, 실시권의 허락 등을 암시하거나 이를 강제하지 않는다.</p></li>
                                          <li><p>③ 수급사업자는 기술자료를 제공할 적법한 자격이 있음을 원사업자에 대하여 보증한다.</p></li>
                                        </ol>
                                      </dd>
                                    </dl>
                                  </li>
                                  <li>
                                    <dl>
                                      <dt class="fw-medium fc-333">제7조 (비밀유지의무 위반시 배상)</dt>
                                        <dd>
                                          <p>원사업자가 이 계약을 위반한 경우, 이로 인하여 발생한 수급사업자의 손해를 배상하여야 한다. 다만, 원사업자가 고의 또는 과실이 없음을 입증한 경우에는 그러하지 아니하다.</p>
                                        </dd>
                                    </dl>
                                  </li>
                                  <li>
                                    <dl>
                                      <dt class="fw-medium fc-333">제8조 (권리의무의 양도 및 계약의 변경)</dt>
                                      <dd>
                                        <ol>
                                          <li><p>① 수급사업자가 사전에 서면(전자문서 포함)으로 동의하지 아니하는 경우, 원사업자는 이 계약상의 권리의무를 제3자에게 양도하거나 이전할 수 없다.</p></li>
                                          <li><p>② 이 계약의 수정이나 변경은 양 당사자의 정당한 대표자가 기명날인 또는 서명한 서면(전자문서 포함) 합의로만 이루어질 수 있다.</p></li>
                                          <li><p>③ 「표준비밀유지계약서(별첨) 2.」에 기재되어 있는 임직원들의 퇴직, 전직, 조직/업무변경 등으로 인하여 명단이 변경되어야 할 때에는 원사업자는 수급사업자의 사전 동의를 받은 후, 해당 명단을 서면으로 수급사업자에게 통지하는 것으로 이 계약의 변경을 갈음할 수 있다.</p></li>
                                        </ol>
                                      </dd>
                                    </dl>
                                  </li>
                                  <li>
                                    <dl>
                                      <dt class="fw-medium fc-333">제9조 (일부무효의 특칙)</dt>
                                      <dd>
                                        <p>이 계약의 내용 중 일부가 무효인 경우에도 이 계약의 나머지 규정의 유효성에 영향을 미치지 않는다. 다만, 유효인 부분만으로 계약의 목적을 달성할 수 없는 경우에는 전부 무효로 한다.</p>
                                      </dd>
                                    </dl>
                                  </li>
                                </ol>
                                <p>이 계약의 체결사실 및 계약내용을 증명하기 위하여 이 계약서를 2통 작성하여 계약 당사자가 각각 서명 또는 기명날인한 후 각자 1통씩 보관한다.</p>
                                <p class="t-center fw-medium fc-333"> {{ docInfo.year }}년&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; {{ docInfo.month }}월&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; {{ docInfo.date }}일</p>
                                <table class="sign">
                                    <caption class="hidden">표준비밀유지계약서(본문) 서명</caption>
                                    <thead>
                                        <tr>
                                          <th colspan="2" class="w-50">원사업자</th>
                                          <th colspan="2" class="w-50">수급사업자</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                          <th>상호 또는 명칭 : </th>
                                          <td>㈜아모레퍼시픽</td>
                                          <th>상호 또는 명칭 : </th>
                                          <td>{{ docInfo.comp_name }}</td>
                                        </tr>
                                        <tr>
                                          <th>주소 : </th>
                                          <td>서울 용산구 한강대로 100</td>
                                          <th>주소 : </th>
                                          <td>{{ docInfo.comp_addr }}</td>
                                        </tr>
                                        <tr>
                                          <th>대표자 성명 : </th>
                                          <td>안세홍 (인)</td>
                                          <th>대표자 성명 : </th>
                                          <td>{{ docInfo.comp_rname }}&nbsp;(인)</td>
                                        </tr>
                                        <tr>
                                          <th>사업자(법인)번호 : </th>
                                          <td>106-86-43373</td>
                                          <th>사업자(법인)번호 : </th>
                                          <td>{{ commonUtils.compNumFormat(docInfo.comp_num) }}</td>
                                        </tr>
                                    </tbody>
                                </table>
                                <p>첨부 : 1. 표준비밀유지계약서(별첨)</p>
                            </div>
                        </div>
                        <div class="ad-wrap">
                            <p class="fw-medium fc-333">[첨부 1]</p>
                            <h4 class="ad-tit fs-h4 fw-medium fc-333 t-center">표준비밀유지계약서(별첨)</h4>
                            <div class="ad-cont">
                                <ol>
                                    <li>
                                        <ol>
                                            <li>
                                                <dl>
                                                    <dt class="fw-medium fc-333">1-1. 수급사업자로부터 제공받는 기술자료의 명칭 및 범위</dt>
                                                    <dd>
                                                        <p>* 요구하는 기술자료의 명칭과 범위 등 구체적 내역을 명시하여 기재</p>
                                                        <div class="ad-gray-box">
                                                            원재료 성분표 및 조성비(SPEC, COA, CCPP, MSDS, TDS, 향/색소 정보 등), 원료 시험방법, 제조방법(공정도), 제품/작업표준서(지시서), 배합 요령 및 비율, 임상시험보고서
                                                        </div>
                                                    </dd>
                                                </dl>
                                            </li>
                                            <li>
                                                <dl>
                                                    <dt class="fw-medium fc-333">1-2.	[1-1. 기술자료]를 제공받는 목적</dt>
                                                    <dd>
                                                        <p>* 원사업자가 기술자료를 요구하는 정당한 사유 기재</p>
                                                        <div class="ad-gray-box">
                                                            구성성분 검토, 원료 품질관리 항목 도출, 자사 안전관리 기준 부합 여부 확인, 원료 내 유해물질 여부 및 이슈 발생 가능성 검토, 국내 허가 및 수출 국가의 원료 관련 규정 준수여부 확인 및 대응, 품질 문제 발생시 원인 분석, 원사업자의 계열사가 원사업자가 개발한 처방을 확인 후, 수급사업자의 원료 활용 여부 결정 및 구매를 위한 자료로 제공
                                                        </div>
                                                    </dd>
                                                </dl>
                                            </li>
                                            <li>
                                                <dl>
                                                    <dt class="fw-medium fc-333">1-3. [1-1. 기술자료]의 사용기간 : </dt>
                                                    <dd>
                                                        <p>해당 원료를 사용한 제품의 마지막 생산일로부터 10년</p>
                                                    </dd>
                                                </dl>
                                            </li>
                                            <li>
                                                <dl>
                                                    <dt class="fw-medium fc-333">1-4. [1-1. 기술자료]의 반환일 또는 폐기일 : </dt>
                                                    <dd>
                                                        <p>사용기간 만료 또는 수급사업자의 요청이 있은 때로부터 30일 이내</p>
                                                    </dd>
                                                </dl>
                                            </li>
                                        </ol>
                                    </li>
                                    <li>
                                        <dl>
                                            <dt class="fw-medium fc-333">2. 기술자료를 보유할 임직원의 명단</dt>
                                            <dd>
                                                <p>수급사업자의 기술자료를 보유할 아래 임직원은 본건 비밀유지계약서상 의무를 성실히 준수할 것을 서약합니다.</p>
                                                <table class="list">
                                                    <caption class="hidden">기술자료를 보유할 임직원의 명단</caption>
                                                    <thead>
                                                        <tr>
                                                            <th class="w-10">No.</th>
                                                            <th class="w-30">보유자</th>
                                                            <th>이메일</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                      <template v-for="(vo, idx) in userList" :key="'tr_' + idx">
                                                        <tr v-if="vo.record_name">
                                                          <th class="t-center">{{ idx + 1}}</th>
                                                          <td>{{ vo.record_name }}</td>
                                                          <td>{{ vo.record_email }}</td>
                                                        </tr>
                                                      </template>
                                                    </tbody>
                                                </table>
                                                <p>* 위 임직원의 명단은 본 계약의 체결 및 이행을 위해서만 사용될 수 있는 것으로서 이를 무단으로 전송∙배포할 수 없으며, 일부의 내용이라도 공개∙복사해서는 안됨</p>
                                                <p>** 본 건 기술자료를 1-3.의 사용기간 중 보유할 임직원 명단을 기재 </p>
                                                <p>*** 임직원들의 퇴직, 전직, 조직/업무변경 등으로 인하여 명단이 변경될 경우 해당 명단을 서면으로 수급사업자에게 통지하는 것으로 이 계약의 변경을 갈음할 수 있음</p>
                                            </dd>
                                        </dl>
                                    </li>
                                </ol>
                            </div>
                        </div>
                    </div>
                </dd>
            </dl>
        </li>
    </ul>
  </div>
</li>
</template>

<script>
import axios from '@/utils/customAxios'
import { inject, reactive, toRefs, watch } from 'vue'
import { useStore } from 'vuex'

export default {
  name: 'TechnologyDoc',
  props : {
    parentInfo: {
      type: Object,
      default: null
    }
  },
  setup(props) {
    watch(() => props.parentInfo, (newVal) => {
      if (newVal) {
        fnDocInfo()
      }
    })

    const store = useStore()
    const myInfo = store.getters.getMyInfo()
    const commonUtils = inject('commonUtils')
    const state = reactive({
      docInfo : {},
      userList: []
    })

    const userList = []
    const docUserList = () =>{
      for(let i=0; i<30; i++){
        state.userList.push({
          record_email : state.docInfo[`record${(i+1)}_email`],
          record_name :  state.docInfo[`record${(i+1)}_name`]
        });
      }
    }

    const fnDocInfo = () => {
      return axios({
        method: 'get',
        url: '/api/ingrd/technology-doc',
        params: {
          compCd : props.parentInfo.agentCd || props.parentInfo.compCd || myInfo.compCd,
          recordCd : props.parentInfo.recordCd || '',
          laborCd : props.parentInfo.laborCd || '',
          userCd : props.parentInfo.regUserCd || ''
        }
      })
      .then(res => {
        const resData = res.data
        if(resData.code === 'C0000'){
          state.docInfo = resData.data
          state.docInfo.raw_nm_ko = props.parentInfo.rawNmKo
        }
      })
    }
    if(props.parentInfo.recordCd){
      fnDocInfo().then(()=>{
        docUserList()
      })      
    }
    return {
      userList,
      commonUtils,
      ...toRefs(state)
    }
  },
}
</script>
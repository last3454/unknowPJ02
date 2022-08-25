
# Vue 3 + Vite

This template should help get you started developing with Vue 3 in Vite. The template uses Vue 3 `<script setup>` SFCs, check out the [script setup docs](https://v3.vuejs.org/api/sfc-script-setup.html#sfc-script-setup) to learn more.

## Recommended IDE Setup

- [VSCode](https://code.visualstudio.com/) + [Volar](https://marketplace.visualstudio.com/items?itemName=johnsoncodehk.volar)

# (1) JAVA 11 이상 설치
    URL : https://developer.oracle.com/kr/java/

# (1 - 1) LOMBOK 설치
    URL : https://projectlombok.org/download

 다운로드 후 LOMBOK.jar 더블 클릭 → 이클립스 또는 STS 실행 파일에 설정

 .INI 파일 들어가서 직접 설정해줘도 됩니다.

# (2) 노드 js 설치(14버전 이상)
    URL : https://nodejs.org/ko/

 설치 완료 후 윈도우 → cmd(명령 프롬포트) → node -version

 VERSION이 노출 되면 정상 설치

 # (3) GIT 설치
    URL : https://git-scm.com/

 설치 완료 후 윈도우 → cmd(명령 프롬포트) → git version

 VERSION이 노출 되면 정상 설치

 WORKSPACE 설정 후 git_bash 창에다가 명령어 실행

 EX) git clone -b develop

 PS : git_bash 가 귀찮다면 소스트리를 받으셔도 됩니다.  

 URL : https://www.sourcetreeapp.com/

# (4) 실행
 해당 git workspace 에 접근 후

 git_bash 또는 terminal 창에 "npm i" 로 프로젝트에서 사용중인 javascript 플러그인 모두 다운로드

 package.json에 설정되어 있는 npm run dev로 실행

 PS : 빌드는 기존 cli를 쓰지 않고 vite를 사용

# (5) 라이브러리
## VUE 패키지
```
sanitize-html
    XSS 관련 대응, INPUT, TEXTAREA 등 태그등에 불필요한 요소를 제거할 수 있음 패키지
```
```
vee-validate
    유효성 체크 패키지
```
```
vue-i18n
    다국어 처리 패키지
```
```
vue-plugin-load-script
    외부 스크립트 불러오는 패키지
```
```
v-calendar
    달력 패키지
```
```
vuex-composition-helpers
    Vuex와 Composition API를 쉽게 사용할 수있는 유틸리티 패키지
```
```
js-sha512
    SHA-512 해싱 알고리즘 암호화 패키지
```
## JAVA 패키지
```
commons-csv
    CSV 파일 생성하는 라이브러리
```
```
janino
     logback 의 조건부 처리 기능을 사용하기 위한 라이브러리
```
```
shedlock-spring
     스켸줄러 교착상태 관리를 위한 라이브러리
```
```
gson
     구글에서 제공하는 json 변환 라이브러리
```
```
hibernate
    ORM 프레임워크
```

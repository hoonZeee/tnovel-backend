# TNovel 백엔드 프로젝트


### ERD 구조도
![ERD 다이어그램](./docs/tnovel-erd.png)


## 📖작업일지 

### 2025-08-30
- 프로젝트 기본 세팅
- Dockerfile 작성 (개발용 + 운영용 yml)

### 2025-08-31
- 전체 요구사항 분석 및 ERD 설계

### 2025-09-02
- 인증 방식 설정 : JWT 토큰
- cors 설정 및 env 추가
- PasswordEncoder : BCryptPasswordEncoder
- PhoneEncryptor : AES (대칭키 암호화, 인증번호 및 마캐팅 목적)


### 2025-09-03
- 먼저 해야할 것 exception 핸들러 로직 먼저 추가
- yml 정리
- Oauth 로그인 추가
- entity 마무리
- swagger 
- validation 검증
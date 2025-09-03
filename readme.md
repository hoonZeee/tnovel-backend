# TNovel 백엔드 프로젝트


### ERD 구조도
![ERD 다이어그램](./docs/tnovel-erd.png)

---
### Exception 설계
- `ErrorCode` 인터페이스를 기반으로 도메인별 에러 코드(enum)를 일관성 있게 정의
- `BusinessException`을 상위 예외 클래스로 하여, 도메인별 예외(`UserException` 등)를 세분화
- 전역 예외 핸들러 `ExceptionAdvice`에서 `@ExceptionHandler(BusinessException.class)`로 통합 처리
- Validation 관련 예외 (`MethodArgumentNotValidException`, `ConstraintViolationException`) 및 `ResponseStatusException`도 별도 처리

---
### 로그인 방식
- 지원: LOCAL, KAKAO, GOOGLE, APPLE, NAVER
- 카카오 로그인: 구현 완료 (API 호출 성공 및 DB 저장 정상 동작)
- 로컬 회원가입 및 모든 소셜 로그인 시 JWT 토큰 발급 (username + role 기반)
![로그인 성공 API](./docs/kakao-login.png)

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
- 먼저 해야할 것 exception 핸들러 로직 먼저 추가 완료
- Oauth 로그인 추가
- entity 마무리
- swagger 추가 완료
- 로컬 회원가입 Validation 검증 완료
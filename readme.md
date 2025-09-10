# TNovel 백엔드 프로젝트

### ERD 구조도

![ERD 다이어그램](./docs/tnovel-erd.png)

---

### Exception 설계

- `ErrorCode` 인터페이스를 기반으로 도메인별 에러 코드(enum)를 일관성 있게 정의
- `BusinessException`을 상위 예외 클래스로 하여, 도메인별 예외(`UserException` 등)를 세분화
- 전역 예외 핸들러 `ExceptionAdvice`에서 `@ExceptionHandler(BusinessException.class)`로 통합 처리
- Validation 관련 예외 (`MethodArgumentNotValidException`, `ConstraintViolationException`) 및 `ResponseStatusException`도 별도
  처리

---

### 인증 방식

- JWT 기반 무상태 인증 방식 사용
- 로그인 성공 시 Access Token이 발급되며, 이후 요청 시 Authorization 헤더를 통해 인증을 수행

### 로그인 방식

- 지원: LOCAL, KAKAO, GOOGLE, APPLE, NAVER
- 카카오 로그인: 구현 완료 (API 호출 성공 및 DB 저장 정상 동작)
- 로컬 회원가입 및 모든 소셜 로그인 시 JWT 토큰 발급 (username + role 기반)
- swagger에서 카카오톡 로그인 api를 시행할 수 없기에 성공 이미지로 대체합니다.
- 카카오 로그인 url : http://localhost:8080/oauth2/authorization/kakao
  ![로그인 성공 API](./docs/kakao-login.png)

---

### 개인정보처리방침 스케쥴러

- 새벽 3시마다 검증, 최후 개인정보처리방침이 1년이 지날 경우 추후 로직(이메일 전송, 페이지 리다이렉팅 등) 할수 있도록 스케쥴러 구현

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
- -로컬 로그인 추가
- Oauth 로그인 추가
- swagger 추가 완료
- 로컬 회원가입 Validation 검증 완료
- 회원가입시 개인정보처리방침 동의 추가, kakao 경우 로그인시 프론트의 추가 redirect 및 별도 POST가 필요하지만, API 호출 불가로 생략(프론트 미구현)
- 스케쥴러 기능 추가
- 어드민 페이지 회원가입, 로그인, 어드민 권한으로 전체 유저 조회 기능 및 Swagger 추가.

### 2025-09-04

- 어드민 관련 API 추가 (모두 페이지네이션 적용)
    + 전체 유저 조회 기능 구현 완료
    + 해당 키워드 포함 아이디를 가진 유저 검색 기능 구현 완료
    + 해당 키워드 포함 닉네임을 가진 유저 검색 기능 구현 완료
    + 해당 날짜에 회원가입한 유저 검색 검색 기능 구현 완료
    + 유저 벤하기 API 추가 완료
    + User 엔티티에 lastLoginAt 추가 후 어드민페이지에서 마지막 로그인 시간도 같이 조회 할수 있게 추가
    + 중복 검색..통합검색을 늦게 확인하여 단일 검색 실컷만들고 통합 검색도 구현.. 단일 검색은 아까워서 일단 놔둡니다 ㅠㅠ

### 2025-09-05

- 결제 관련 API 추가
- prepare / verify / confirm 3단계로 구성
- verify는 실제 결제가 이루어 질수 없는 점을 감안해서 코드 구현만 되어있고 API 테스트가 불가합니다.
- 성공시 : prepare > verify > successConfirm
- 실패시 : prepare > verify > failConfirm (실제 결제취소 API를 PortoneService에 구현해뒀지만, 실결제가 이루어질 수 없어 주석처리 하였습니다.)

### 2025-09-06

- 한시간마다 기대금액 = 실결제금액이 같은지 확인하는 스케쥴러 구현 완료
- 매일 저녁 7시마다 구독만료일 정검및 만료 시 결제 스케쥴러 구현 완료
- 구독관련 어드민 API 완성 (최근 날짜 순으로 전체 구독 상태 조회, 각 조건에 맞는 통합 조회 API 구현)

### 2025-09-07

- ERD 수정본 업데이트

### 2025-09-09

- 게시물 관련 로직 추가
- Swagger 검증 및 테스트 편의를 위해 @RequestBody 기반 JSON 형식으로 게시글 등록을 구현함.
- 실제 운영 환경에서는 @ModelAttribute 기반의 MultipartFile 업로드 방식으로 확장 가능하도록 설계되어 있으며, 해당 코드는 주석 처리로 보존되어 있음.
- 각 미디어 파일의 URL과 타입(IMAGE, VIDEO 등)을 함께 받도록 DTO를 설계하여 유연한 확장성 확보.
- 첨부파일 저장 및 URL 생성 로직은 service > domain > ImageUtils 유틸 클래스에 구현함.
- 댓글 작성 API 구현
- 게시물 클릭시 댓글이 최신순으로 10개씩 조회되는 API 구현
- 게시글 삭제 API 구현
- 유저 휴먼계정 전환 구현 - soft delete
- 유저(어드민포함) 회원탈퇴 기능 구현 - soft delete
- 게시물 신고 기능 추가 - 신고 10회이상 누적시 게시물 비활성
- 어드민 페이지 게시물 전체 조회 로직 추가
- 어드민 페이지 게시물 조건 검색 로직 추가
- 신고된 게시물 목록 조회 API 추가
- 허위 신고 삭제하는 API 추가
- 신고된 게시물 블라인드 처리하는 API 추가 완료

### 2025-09-10

- 댓글 삭제 기능 추가 (댓글은 하드 딜리트로 인한 void 반환 204)
- History 테이블 추가 및 어드민 히스토리 GET 로직 추가 (유저/구독/포스트/댓글/신고)
- 개발 마무리
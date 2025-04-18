# Newsfeed Project

## 📌 Git Commit Message Convention

* Feat : 새로운 기능 추가
* Fix : 버그 수정
* Style : 코드 포맷팅, 코드 오타, 함수명 수정 등 스타일 수정
* Refactor : 코드 리팩토링(동일 기능 내 코드 개선)
* Comment : 주석 수정 및 삭제
* Docs : 문서와 관련된 모든 것


## 📌 프로젝트 개요
Newsfeed는 Spring Boot를 기반으로 한 SNS 백엔드 시스템입니다.
회원가입부터 프로필 관리, 게시물 CRUD, 뉴스피드 기능, 친구 관리 등 
소셜 네트워크 서비스에서 필수적인 기능들을 구현합니다.

사용자는 회원가입을 통해 SNS에 가입하고, 자신의 프로필을 관리하며
친구를 맺은 사용자의 최신 게시글을 뉴스피드 형태로 확인할 수 있습니다.


## 📌 개발 환경 및 기술 스택

### 개발 환경
* 언어: Java 17
* 프레임워크: Spring Boot 3.4.4
* 빌드 도구: Gradle
* 데이터베이스: MySQL
* ORM: Spring Data JPA
* 인증/인가: Cookie, Session
* API 테스트: Postman

### 기술 스택
* Spring Boot: REST API 및 웹 애플리케이션 개발
* Spring Data JPA: 데이터베이스와의 인터페이스 관리
* Lombok: 보일러플레이트 코드 제거 및 유지보수성 향상
* Spring Security: 인증 및 인가 처리
* Hibernate: ORM을 통한 객체-관계 매핑
* JPA Auditing: 자동으로 생성일/수정일 관리
* Validation: 입력 값 검증


## 📌 주요 기능 

### 사용자 인증 및 관리
* 회원가입
>* 이메일 형식의 아이디, 복잡도 있는 비밀번호 검사
>* 비밀번호는 암호화
* 로그인 (인증 구현)
* 회원 탈퇴
>* 비밀번호 일치 확인 후 탈퇴
* 예외처리
> * 중복 이메일, 형식 오류, 인증 실패 등

### 프로필 관리
* 마이페이지 조회
* 사용자 정보 수정
>* 본인만 수정 가능
>* 비밀번호 수정 시 현재 비밀번호 확인 필요
* 비밀번호 수정 조건
>* 현재 비밀번호와 동일하면 변경 불가
>* 형식 미달 시 예외 발생(고려)

### 게시물 관리
* 게시물 작성 / 조회 / 수정 / 삭제
>* 작성자 본인만 수정 및 삭제 가능
* 예외처리
>* 권한 없는 접근 차단
* 데이터 정렬
>* 생성일 기준 최신순 정렬

### 뉴스피드 기능
* 본인 게시물과 팔로우한 사용자의 게시물을 최신순으로 조회
* 페이지네이션 방식 적용 (10개 단위)

### 팔로우
* 사용자는 다른 사용자를 팔로우할 수 있음
* 친구 수락 기능이 어려운 경우, 단방향 팔로우 방식으로 구현
* 팔로우한 사용자의 게시물이 뉴스피드에 반영됨

### 예외 처리 (핵심만 요약)
* 이메일 및 비밀번호 형식 검증
* 권한 없는 수정/삭제 차단
* 잘못된 비밀번호 입력 시 예외 반환
* 탈퇴된 사용자 접근 제한


## 📌 API 명세서

https://documenter.getpostman.com/view/43163091/2sB2cVdgQ7


## 📌 ERD

<img src="https://github.com/classseoha/newsfeed/blob/dev/newsfeed_ERD.png?raw=true">
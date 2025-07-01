# 영어 플래시카드 웹앱 🃏

Cursor AI를 활용하여 만든 영어 단어 학습용 플래시카드 웹 애플리케이션입니다.

## 🚀 기술 스택

### 프론트엔드
- **Vue 3** + **TypeScript**
- **Ant Design Vue** (UI 컴포넌트)
- **Vue Router** (라우팅)
- **Pinia** (상태 관리)
- **Axios** (HTTP 클라이언트)
- **Vite** (빌드 도구)

### 백엔드
- **Spring Boot 3.3.6**
- **Java 21**
- **H2 Database** (인메모리 DB)
- **Spring Data JPA**
- **Maven** (의존성 관리)

## 📋 주요 기능

### 1. 카드 관리
- 영어-한글 단어 카드 CRUD 기능
- 카테고리별 분류 (동물, 음식, 동사)
- 30개의 샘플 데이터 제공

### 2. 학습 모드
- 플래시카드 형태의 학습 인터페이스
- 카드 플립 애니메이션
- "알고 있음"/"모름" 버튼으로 학습 진행
- 진행률 표시

### 3. 퀴즈 모드
- **객관식**: 4지선다 문제 (같은 카테고리 내 오답 자동 생성)
- **빈칸 채우기**: 직접 입력 방식
- 실시간 피드백 제공
- 점수 및 진행률 표시

### 4. 통계 관리
- 카테고리별 학습 통계
- 정답률 및 학습 진행도 추적
- 시각적 프로그레스 바
- 통계 초기화 기능

## 🏃‍♂️ 실행 방법

### 사전 요구사항
- Node.js v22+
- Java 21+
- Maven 3.6+

### 백엔드 실행
```bash
cd backend
./mvnw spring-boot:run
```
- 서버: http://localhost:8080
- H2 콘솔: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: (비어있음)

### 프론트엔드 실행
```bash
cd frontend
npm install
npm run dev
```
- 개발 서버: http://localhost:5173

## 📁 프로젝트 구조

```
cardgame/
├── backend/                 # Spring Boot 백엔드
│   ├── src/main/java/
│   │   └── com/cardgame/backend/
│   │       ├── controller/  # REST API 컨트롤러
│   │       ├── entity/      # JPA 엔티티
│   │       ├── repository/  # 데이터 접근 계층
│   │       └── DataLoader.java # 샘플 데이터 로더
│   └── src/main/resources/
│       └── application.properties
├── frontend/                # Vue.js 프론트엔드
│   ├── src/
│   │   ├── components/      # 재사용 가능한 컴포넌트
│   │   ├── views/          # 페이지 컴포넌트
│   │   ├── router/         # 라우터 설정
│   │   └── assets/         # 정적 자원
│   └── package.json
├── specification.md         # 요구사항 명세서
├── TODO.md                 # 개발 단계별 할일 목록
└── README.md               # 프로젝트 설명서
```

## 🛠️ API 엔드포인트

### 카드 관리
- `GET /api/cards` - 모든 카드 조회
- `GET /api/cards?category=동물` - 카테고리별 카드 조회
- `GET /api/cards/{id}` - 특정 카드 조회
- `POST /api/cards` - 카드 생성
- `PUT /api/cards/{id}` - 카드 수정
- `DELETE /api/cards/{id}` - 카드 삭제

### 통계 관리
- `GET /api/statistics` - 통계 조회
- `GET /api/statistics/{category}` - 카테고리별 통계 조회
- `POST /api/statistics/{category}` - 통계 업데이트
- `DELETE /api/statistics/{category}/reset` - 통계 초기화

### 기타
- `GET /api/test` - 연결 테스트
- `GET /api/health` - 헬스체크

## 🎯 주요 화면

1. **홈** - 프로젝트 소개 및 시작 가이드
2. **카테고리 선택** - 동물/음식/동사 카테고리 선택
3. **학습** - 플래시카드 학습 모드
4. **퀴즈** - 객관식/빈칸채우기 퀴즈
5. **통계** - 학습 진행도 및 성과 확인

## 🔧 개발 환경 설정

### IDE 설정
- IntelliJ IDEA 또는 VS Code 권장
- Node.js v22 사용
- Java 21 JDK 설정

### 데이터베이스
- H2 인메모리 데이터베이스 사용
- 애플리케이션 재시작 시 데이터 초기화
- 개발용으로 적합한 설정

## 📝 라이선스

이 프로젝트는 학습 목적으로 제작되었습니다.

## 🤝 기여하기

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

📚 **Happy Learning!** 🎉 
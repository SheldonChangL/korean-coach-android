# Korean Coach Android

A modern Android app to teach practical basic Korean to absolute beginners (40+ demographic), using right-brain memory techniques, daily micro-lessons, and spaced repetition.

---

## Architecture

Single-module Android app with clean package layering and a **token-based Design System**:

```
app/src/main/java/com/koreancoach/app/
├── data/               # Repositories, DAOs, Room, DataStore, Curriculum
├── di/                 # Hilt dependency injection
├── domain/             # Business logic, models, ASR evaluation
└── ui/
    ├── common/         # Shared components & animations (Confetti)
    ├── feature/        # Polished feature-specific screens & ViewModels
    ├── theme/          # Refined Design System:
    │   ├── Color.kt    # Sophisticated brand palette
    │   ├── Type.kt     # Optimized typography rhythm
    │   ├── Spacing.kt  # Coherent layout grid
    │   ├── Shapes.kt   # Consistent rounding strategy
    │   └── Theme.kt    # Unified Material3 implementation
    └── Navigation.kt   # Seamless screen transitions
```

**Stack:**
- Kotlin 2.0.21
- Jetpack Compose + Material3
- Navigation Compose 2.8.5
- Hilt 2.52 (dependency injection)
- Room 2.6.1 (local database, v4)
- DataStore 1.1.1 (user preferences)
- WorkManager 2.10.0 (daily reminder notifications)
- Android SpeechRecognizer (on-device ASR, falls back to fake scorer when unavailable)
- Kotlin Coroutines + Flow
- kotlinx.serialization (lesson content JSON)

---

## Setup & Build

### Prerequisites
- Android Studio Ladybug (2024.2+) or newer
- JDK 17
- Android SDK 35 (compile), minSdk 26

### Clone & Open
```bash
git clone <repo-url>
cd korean-coach-android
```
Open in Android Studio. Let Gradle sync complete.

### Build
```bash
./gradlew assembleDebug
```

### Run Tests
```bash
./gradlew test
```

---

## Features

| Feature | Description |
|---|---|
| **Premium UI/UX** | Product-grade design system with refined tokens, responsive layouts, and cohesive brand identity |
| **Onboarding** | High-delight 6-screen flow with smooth transitions and personalized "human-centric" microcopy |
| **Dashboard** | Motivational hub with daily greeting, streak visualization, progress stats, and quick-practice cards |
| **Lesson Detail** | Clear hierarchy with interactive Vocabulary, Phrases, Pronunciation, and Memory tabs |
| **Flashcards** | Immersive practicing with 3D flip animations, haptic feedback, and swipe-to-rate gestures |
| **Quiz** | Engaging MCQ with progress tracking, instant explanation, and high-impact confetti celebrations |
| **Spaced Review** | Intelligent SM-2 queue that prioritizes the right words at the right time |
| **Hangul Writing** | Interactive tracing canvas with stroke-order validation and ghost guides |
| **Pronunciation** | Real-time ASR-powered feedback with phoneme-level tips and waveform visualizations |

---

## Learning Design

Each lesson uses evidence-based memory techniques:
- **Chunking**: 5 vocab items per lesson — manageable, not overwhelming
- **Memory Hooks**: Every word has a funny/vivid story mnemonic
- **Romanization**: Always shown alongside Hangul — no guessing
- **Right-brain anchors**: Visual descriptions, sound associations, body humor
- **Spaced Repetition**: SM-2 algorithm schedules review at optimal intervals
- **Immediate feedback**: Quiz shows explanation right after each answer

---

## Curriculum (Sprint 3 — 25 lessons)

### Hangul Writing Characters
| Type | Characters |
|------|-----------|
| Consonants | ㄱ (기역), ㄴ (니은), ㄷ (디귿), ㅁ (미음), ㅅ (시옷) |
| Syllables | 가, 나, 다, 마, 사 |

### Week 1 — Survival Basics
| Day | Topic | Key Words |
|-----|-------|-----------|
| 1 | Hello, Korea! | 안녕하세요, 감사합니다, 네, 아니요 |
| 2 | Hangul Basics | 가, 나, 다, 마, 사 + writing system |
| 3 | Café & Food | 커피, 주세요, 물, 밥, 맛있어요 |
| 4 | Getting Around | 지하철, 버스, 어디, 가다 |
| 5 | Numbers & Shopping | 일이삼, 원, 비싸요, 얼마예요 |

### Week 2 — Everyday Life
| Day | Topic | Key Words |
|-----|-------|-----------|
| 1 | At the Restaurant | 메뉴, 김치, 비빔밥, 매워요 |
| 2 | Shopping Trip | 옷, 사이즈, 싸요, 영수증 |
| 3 | Numbers, Part 2 | 하나, 둘, 셋, 넷, 다섯 |
| 4 | Weather Talk | 더워요, 추워요, 비가 와요, 날씨 |
| 5 | Family & Feelings | 엄마, 아빠, 친구, 행복해요, 피곤해요 |

### Week 3 — Travel & Culture
| Day | Topic | Key Words |
|-----|-------|-----------|
| 1 | Hotel Check-In | 예약, 방, 열쇠, 조식, 체크아웃 |
| 2 | At the Airport | 여권, 비행기, 게이트, 짐, 연착 |
| 3 | Directions | 왼쪽, 오른쪽, 직진, 출구 |
| 4 | Emergency Korean | 도와주세요, 병원, 경찰, 119 |
| 5 | K-pop Daily Phrases | 대박, 화이팅, 오빠, 사랑해 |

### Week 4 — Real Life Korea
| Day | Topic | Key Words |
|-----|-------|-----------|
| 1 | Making Friends | 친구, 선배, 후배, 만나다, 반갑습니다 |
| 2 | Health & Body | 병원, 아파요, 약, 머리, 배 |
| 3 | Tech & Daily Life | 핸드폰, 인터넷, 비밀번호, 사진, 배달 |
| 4 | Korean Food Deep Dive | 라면, 김치, 불고기, 반찬, 맛있어요 |
| 5 | Time & Schedules | 오늘, 내일, 어제, 요일, 약속 |

### Week 5 — Beginner Stepping Up
| Day | Topic | Key Words |
|-----|-------|-----------|
| 1 | Running Errands | 은행, 우체국, 돈, 계좌, 소포 |
| 2 | Making Plans | 같이, 시작, 계획, 출발, 확인 |
| 3 | Describing Things | 크다, 작다, 예쁘다, 비싸다, 싸다 |
| 4 | Asking for Help | 도와주세요, 이해해요, 모르겠어요, 다시 한 번, 천천히 |
| 5 | You've Got This! | 연습, 실력, 자신감, 노력, 수고했어요 |

# Project Status

## Completed (v2.1 — UI/UX Sprint)

### Premium Design System
- [x] **Refined Color Palette** — Professional Korean-inspired brand colors (KoreanRed, KoreanBlue, GoldAccent)
- [x] **Optimized Typography** — Clearer hierarchy and better readability using modern weights and spacing
- [x] **Consistent Shapes** — Standardized rounding (8dp/16dp/24dp) across all components for a cohesive feel
- [x] **System UI Integration** — Consistent status bar and navigation bar styling across light/dark modes

### Core Journey Polish
- [x] **High-Delight Onboarding** — Smooth horizontal transitions, personalized greetings, and human-centric microcopy
- [x] **Motivational Dashboard** — Time-aware greetings (좋은 아침!), enhanced streak visualization, and status-aware lesson cards
- [x] **Interactive Lessons** — Tabbed content with animated indicators, polished vocabulary/phrase cards with integrated memory hooks
- [x] **Impactful Quiz** — Refined progress tracking, haptic feedback, and scale-animated confetti celebrations (≥80%)
- [x] **Immersive Practice** — 3D flip-card practicing with immersive swipe gestures and reward-focused completion states

### Accessibility & Polish
- [x] **Motion & Transitions** — Used `AnimatedVisibility` and `SizeTransform` for fluid screen-to-screen movement
- [x] **Micro-interactions** — Added interaction hints (Tap to reveal, Swipe to rate) and haptic feedback
- [x] **Visual Hierarchy** — Better use of whitespace, tonal elevations, and consistent iconography

---

## Completed (v2.0 — Real Pronunciation)
- [x] **Real Pronunciation Pipeline** — SpeechRecognizer + PronunciationEvaluator
- [x] **UX State Machine** — Phase-aware UI (Listening, Processing, Result)
- [x] **Data / Analytics** — Persistence of ASR results and weekly trend charts

---

## Roadmap

| Milestone | Status | Description |
|-----------|--------|-------------|
| v1.0 | Done | Full scaffold, 6 features, Week 1 content |
| v1.1 | Done | Weeks 2–3, streak, notifications, settings, analytics |
| v1.2 | Done | Pronunciation screen, Hangul writing, Weeks 4–5 |
| v2.0 | Done | Real ASR pipeline, state machine UX, pronunciation analytics |
| v2.1 | **Done** | Product-grade UI/UX polish, motion, design system refinement |
| v2.2 | Future | AI tutor integration (Claude API) |
| v2.3 | Future | Social features (study buddy, leaderboard) |

---

## Future TODO (v2.2+)
- [ ] **AI Tutor Integration** — Real-time conversational practice powered by Claude API
- [ ] **Social Features** — Study groups, friend leaderboards, and shared goals
- [ ] **Gamification 2.0** — Unlockable badges, level-up system, and achievement gallery
- [ ] **Performance Optimization** — Image caching, lazy list pre-fetching, and background sync improvements
- [ ] **Accessibility 2.0** — Full TalkBack support, dynamic text scaling verification, and high-contrast color mode
- [ ] **Advanced Analytics** — Detailed mastery breakdown per category (Food, Travel, etc.)

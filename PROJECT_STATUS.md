# Korean Coach Android — Project Status

更新時間：2026-03-21
分支：`main`

## Current Snapshot

- 目前主線已從舊的 Week 1-5 結構，重心轉到 `Hangul Sprint`。
- 最新功能基線：Issue #1 grouped practice UX 完成，資產升至 v5。
- 目前 repo 內正式使用的 Hangul 資產檔：
  - `app/src/main/assets/curriculum/hangul_sprint_en.json`
  - `app/src/main/assets/curriculum/hangul_sprint_zh_tw.json`
- 舊版資產 `hangul_sprint_v1.json` / `hangul_sprint_v2.json` 已移除。

## Done

- 已完成 `Hangul Path`、`Hangul Explore`、`Hangul Stage` 主流程。
- 已完成 onboarding 後的 Hangul-first 導向。
- 已完成 `CurriculumBootstrapper`，修掉首次進入 `HangulPath` 會卡在 `0/0` 的問題。
- 已完成 `zh-TW` UI 文案與 Hangul 課程內容雙層本地化。
- 已完成繁中 / 英文 Hangul Sprint 課程結構對齊，6 個 stage 的 `scriptItems / writingTargets / readingDrills / dialogueItems / checkpointItems` 現在一致。
- 已修正英文資產裡錯誤的韓文字母內容，包含基本母音與複合母音的錯字。
- 已把 `밥 / 물 / 국` 加進 `HangulCharacterData`，第 6 階段寫字練習不再指向不存在的字元 id。
- 已修正 Hangul flow 的返回邏輯、Explore 畫面裁切、深色模式對比問題。
- 已完成 grouped practice UX：新增 `practiceGroupId` 欄位於 `WritingTarget` 和 `ReadingDrill`，同一個音的讀一讀＋寫一寫合併成同一塊練習卡。資產升至 v5（en / zh-tw）。
- 已把 `Listen & Learn`（`聽 & 學`）重新命名為 `See the Rules`（`看懂規則`），避免誤導使用者。
- 已修正 `HangulCharacterDataTest`：syllable 數量從 5 更新為 8（加入 밥/물/국 後應同步）。
- **Issue #1 已關閉。**

## Current Behavior

- 零基礎使用者完成 onboarding 後，會直接進入 Hangul 主線。
- `Hangul Path` 在作為根頁時不顯示左上返回。
- 完成 stage 後，底部按鈕會回到 path。
- `See the Rules`（原 Listen & Learn）是純閱讀說明卡，不顯示 TTS 喇叭。
- 同一個音的讀一讀＋寫一寫現在合成一張 `GroupedPracticeCard`，減少 40 音學習時的上下文切換。
- TTS 保留在字母 / 拼讀 / 對話 / 單字等真正需要聽聲音的地方。
- `Hangul Sprint` 資產版本目前是：
  - 英文：`hangul-sprint-v5-en`
  - 繁中：`hangul-sprint-v5-zh-tw`

## Recommended Next Work

- Survival lessons 目前仍有一部分是 Kotlin seed，之後可再搬到版本化內容檔。
- 考慮把目前 ungrouped 的 writingTargets（如 vowel_yeo, vowel_yo 等）也補上對應的 readingDrill，讓每個音都有完整的 grouped practice 體驗。

## Issue Mapping

- `Issue #1` ✅ 已關閉：
  - 40 音完整主線 ✅
  - Hangul path / explore / stage ✅
  - 繁中 / 英文雙資產 ✅
  - Grouped practice UX + `practiceGroupId` 規格 ✅
  - `See the Rules` 重新命名 ✅
  - 編譯與 unit tests 通過 ✅

## Clone / Run Notes

- 換到另一台電腦後，直接 clone `main` 即可取得目前狀態。
- 首次跑 app 後如果看到舊的 Hangul 內容，請完整關掉 app 再重開一次，讓 bootstrap 重新 seed 最新資產（v4 → v5 會自動 reseed）。
- 驗證指令：

```bash
./gradlew compileDebugKotlin
./gradlew testDebugUnitTest
```

## Repo Notes

- 以下檔案目前仍是本地工作檔，沒有納入正式版本：
  - `.idea/`
  - `DEV_STATUS_2026-03-20.md`
  - `PROJECT_STATUS_2026-03-20.md`
  - `claude_mvp_plan_v1.md`

package com.koreancoach.app.data.curriculum

import com.koreancoach.app.domain.model.*

object CurriculumData {

    fun getWeek1Lessons(): List<Lesson> = listOf(
        Lesson(
            id = "w1d1", weekNumber = 1, dayNumber = 1,
            title = "Hello, Korea!", subtitle = "Your first Korean words",
            emoji = "🇰🇷", estimatedMinutes = 10,
            isUnlocked = true, isCompleted = false,
            vocabulary = listOf(
                VocabItem(
                    id = "v_annyeong", korean = "안녕하세요",
                    romanization = "annyeonghaseyo", english = "Hello (formal)",
                    exampleSentence = "안녕하세요! 저는 학생입니다.",
                    exampleTranslation = "Hello! I am a student.",
                    memoryHook = "Think: 'On young ha-say-yo' — a young person saying hey-yo!",
                    category = VocabCategory.GREETING
                ),
                VocabItem(
                    id = "v_annyeong_casual", korean = "안녕",
                    romanization = "annyeong", english = "Hi / Bye (casual)",
                    exampleSentence = "안녕! 잘 있어?",
                    exampleTranslation = "Hi! How are you? (casual)",
                    memoryHook = "Short and sweet — 'annyeong' for friends. Like 'aloha' — works both ways!",
                    category = VocabCategory.GREETING
                ),
                VocabItem(
                    id = "v_gamsahamnida", korean = "감사합니다",
                    romanization = "gamsahamnida", english = "Thank you (formal)",
                    exampleSentence = "도와주셔서 감사합니다.",
                    exampleTranslation = "Thank you for helping me.",
                    memoryHook = "Gam-sa: think 'gamma ray of appreciation' shooting at 'ham' — you're thanking the ham!",
                    category = VocabCategory.GREETING
                ),
                VocabItem(
                    id = "v_ne", korean = "네",
                    romanization = "ne", english = "Yes",
                    exampleSentence = "네, 맞습니다.",
                    exampleTranslation = "Yes, that's right.",
                    memoryHook = "네 sounds like 'nay' — like neighing a horse to say YES!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_aniyo", korean = "아니요",
                    romanization = "aniyo", english = "No",
                    exampleSentence = "아니요, 괜찮아요.",
                    exampleTranslation = "No, it's okay.",
                    memoryHook = "'Ani-yo' — ANI-mation? No! No cartoons here!",
                    category = VocabCategory.GENERAL
                )
            ),
            phrases = listOf(
                PhraseItem(
                    id = "p_nice_to_meet", korean = "처음 뵙겠습니다",
                    romanization = "cheoeum boepgesseumnida",
                    english = "Nice to meet you (formal)",
                    context = "First meeting, business or respectful situations",
                    usageTip = "Bow slightly when saying this — about 15-30 degrees"
                ),
                PhraseItem(
                    id = "p_how_are_you", korean = "잘 지내셨어요?",
                    romanization = "jal jinaesyeosseoyo",
                    english = "How have you been?",
                    context = "Greeting someone you haven't seen in a while",
                    usageTip = "For casual friends use: 잘 지냈어? (jal jinaesseo?)"
                ),
                PhraseItem(
                    id = "p_im_fine", korean = "잘 지냈어요",
                    romanization = "jal jinaesseoyo",
                    english = "I've been well",
                    context = "Response to 'how have you been'",
                    usageTip = "'Jal' means 'well' — you'll see it a lot!"
                )
            ),
            pronunciationTips = listOf(
                PronunciationTip(
                    sound = "ㅎ (h)", description = "Breathy 'h' sound",
                    englishComparison = "Like the 'h' in 'hello' but slightly breathier",
                    commonMistake = "Don't make it too harsh — keep it soft"
                ),
                PronunciationTip(
                    sound = "요 (-yo)", description = "Polite sentence ending",
                    englishComparison = "Like 'yo' in 'yoga'",
                    commonMistake = "Don't drop this ending in formal settings — it shows respect"
                )
            ),
            memoryHooks = listOf(
                MemoryHook(
                    targetWord = "안녕하세요",
                    story = "Imagine a young (young = 안녕) Korean student saying 'hey-yo!' to greet you on the street.",
                    visualDescription = "A smiling student waving energetically"
                )
            )
        ),
        Lesson(
            id = "w1d2", weekNumber = 1, dayNumber = 2,
            title = "Hangul Basics", subtitle = "The Korean alphabet in 20 minutes",
            emoji = "🔤", estimatedMinutes = 10,
            isUnlocked = false, isCompleted = false,
            vocabulary = listOf(
                VocabItem(
                    id = "v_hangul_ga", korean = "가", romanization = "ga", english = "ga (syllable)",
                    exampleSentence = "가다 — to go", exampleTranslation = "ga-da = to go",
                    memoryHook = "ㄱ looks like a right angle — like a 'G'un pointing right. ㅏ is a vertical line with a short right arm = 'ah'",
                    category = VocabCategory.HANGUL
                ),
                VocabItem(
                    id = "v_hangul_na", korean = "나", romanization = "na", english = "I / me",
                    exampleSentence = "나는 학생입니다.", exampleTranslation = "I am a student.",
                    memoryHook = "나 = 'Na!' Like a child pointing at themselves going 'NA! MINE!'",
                    category = VocabCategory.HANGUL
                ),
                VocabItem(
                    id = "v_hangul_da", korean = "다", romanization = "da", english = "all / every (also verb ending)",
                    exampleSentence = "다 먹었어요.", exampleTranslation = "I ate it all.",
                    memoryHook = "ㄷ looks like a door on its side. 'Da' door is open for everything!",
                    category = VocabCategory.HANGUL
                ),
                VocabItem(
                    id = "v_hangul_ma", korean = "마", romanization = "ma", english = "ma (syllable, as in 엄마 = mom)",
                    exampleSentence = "엄마! = Mom!", exampleTranslation = "Mom!",
                    memoryHook = "ㅁ looks like a square mouth — 'Mmm', and ㅏ = 'ah'. Your mouth going M-AH for mama!",
                    category = VocabCategory.HANGUL
                ),
                VocabItem(
                    id = "v_hangul_sa", korean = "사", romanization = "sa", english = "four (4) / buy / person",
                    exampleSentence = "사람 = person", exampleTranslation = "sa-ram = person",
                    memoryHook = "ㅅ looks like a person with legs spread = 'S'tanding person. Four legs on a table!",
                    category = VocabCategory.HANGUL
                )
            ),
            phrases = listOf(
                PhraseItem(
                    id = "p_spell_name", korean = "제 이름은 ___ 입니다",
                    romanization = "je ireumeun ___ imnida",
                    english = "My name is ___",
                    context = "Introducing yourself",
                    usageTip = "Replace ___ with your name written in Hangul or just say it in English"
                ),
                PhraseItem(
                    id = "p_from_where", korean = "어디서 오셨어요?",
                    romanization = "eodiseo osyeosseoyo",
                    english = "Where are you from?",
                    context = "Common icebreaker question",
                    usageTip = "Answer: 저는 ___ 에서 왔어요 (I came from ___)"
                )
            ),
            pronunciationTips = listOf(
                PronunciationTip(
                    sound = "ㅏ (a)", description = "Open 'ah' sound",
                    englishComparison = "Like 'a' in 'father'",
                    commonMistake = "Don't pronounce like 'cat' — keep it open and round"
                ),
                PronunciationTip(
                    sound = "ㅡ (eu)", description = "No English equivalent — unrounded back vowel",
                    englishComparison = "Like saying 'uh' but with your lips spread flat (not rounded)",
                    commonMistake = "Beginners say 'oo' — keep lips flat and wide"
                )
            ),
            memoryHooks = listOf(
                MemoryHook(
                    targetWord = "Hangul consonants",
                    story = "Each consonant is shaped like your mouth/tongue position when making that sound. ㄱ = back of throat (K/G), ㄴ = tongue tip touching roof (N), ㅁ = mouth closed (M).",
                    visualDescription = "Diagrams of mouth positions corresponding to letter shapes"
                )
            )
        ),
        Lesson(
            id = "w1d3", weekNumber = 1, dayNumber = 3,
            title = "Café & Food", subtitle = "Order coffee like a local",
            emoji = "☕", estimatedMinutes = 10,
            isUnlocked = false, isCompleted = false,
            vocabulary = listOf(
                VocabItem(
                    id = "v_coffee", korean = "커피", romanization = "keopi", english = "coffee",
                    exampleSentence = "커피 한 잔 주세요.", exampleTranslation = "One cup of coffee, please.",
                    memoryHook = "커피 = 'keo-pi' — just like 'coffee' with a Korean accent. Easy!",
                    category = VocabCategory.FOOD
                ),
                VocabItem(
                    id = "v_juseyo", korean = "주세요", romanization = "juseyo", english = "Please give me",
                    exampleSentence = "물 주세요.", exampleTranslation = "Water, please.",
                    memoryHook = "'Joo-say-yo' — like 'juice, say yo!' Ask for your juice politely.",
                    category = VocabCategory.FOOD
                ),
                VocabItem(
                    id = "v_water", korean = "물", romanization = "mul", english = "water",
                    exampleSentence = "물 한 잔 주세요.", exampleTranslation = "One glass of water, please.",
                    memoryHook = "'Mul' — like 'mool' in a swimming pool. Water!",
                    category = VocabCategory.FOOD
                ),
                VocabItem(
                    id = "v_rice", korean = "밥", romanization = "bap", english = "rice / meal",
                    exampleSentence = "밥 먹었어요?", exampleTranslation = "Did you eat? (Common greeting!)",
                    memoryHook = "'Bap' — like 'bop' a bowl of rice. Common greeting in Korea = 'Did you rice?'",
                    category = VocabCategory.FOOD
                ),
                VocabItem(
                    id = "v_delicious", korean = "맛있어요", romanization = "masisseoyo", english = "It's delicious",
                    exampleSentence = "김치가 맛있어요!", exampleTranslation = "Kimchi is delicious!",
                    memoryHook = "'Ma-shi-sseo-yo' — 'Mash it? Yes-o!' Mash the delicious food!",
                    category = VocabCategory.FOOD
                )
            ),
            phrases = listOf(
                PhraseItem(
                    id = "p_order", korean = "이거 주세요", romanization = "igeo juseyo",
                    english = "This one, please",
                    context = "Pointing at a menu item",
                    usageTip = "Safe ordering phrase — just point and say this!"
                ),
                PhraseItem(
                    id = "p_how_much", korean = "얼마예요?", romanization = "eolmayeyo",
                    english = "How much is it?",
                    context = "Asking for price anywhere",
                    usageTip = "'Eol-ma-ye-yo' — remember it as 'All my? Yo!' (it costs all my money!)"
                ),
                PhraseItem(
                    id = "p_takeout", korean = "포장이요", romanization = "pojangiyo",
                    english = "To go / takeout",
                    context = "At a café or restaurant",
                    usageTip = "Say this at the counter if you want your order to go"
                )
            ),
            pronunciationTips = listOf(
                PronunciationTip(
                    sound = "ㅂ (b/p)", description = "Sounds like 'b' between vowels, 'p' at end",
                    englishComparison = "밥 (bap) — the initial ㅂ is 'b', final ㅂ is 'p'",
                    commonMistake = "English speakers often make it too aspirated — Korean stops are softer"
                )
            ),
            memoryHooks = listOf(
                MemoryHook(
                    targetWord = "주세요",
                    story = "You're at a juice bar. You say 'JUICE-AY-YO!' and the barista understands — juseyo!",
                    visualDescription = "A colorful juice bar counter with a friendly barista"
                )
            )
        ),
        Lesson(
            id = "w1d4", weekNumber = 1, dayNumber = 4,
            title = "Getting Around", subtitle = "Transport & directions",
            emoji = "🚌", estimatedMinutes = 10,
            isUnlocked = false, isCompleted = false,
            vocabulary = listOf(
                VocabItem(
                    id = "v_subway", korean = "지하철", romanization = "jihacheol", english = "subway / metro",
                    exampleSentence = "지하철역이 어디예요?", exampleTranslation = "Where is the subway station?",
                    memoryHook = "'Ji-ha-cheol' — 'G-ha-chul'. Underground (ha = under) + iron (cheol). Iron underground!",
                    category = VocabCategory.TRANSPORT
                ),
                VocabItem(
                    id = "v_bus", korean = "버스", romanization = "beoseu", english = "bus",
                    exampleSentence = "버스 타세요.", exampleTranslation = "Please take the bus.",
                    memoryHook = "버스 = 'beo-seu' — just like 'bus' in English. Korea borrowed it!",
                    category = VocabCategory.TRANSPORT
                ),
                VocabItem(
                    id = "v_taxi", korean = "택시", romanization = "taeksi", english = "taxi",
                    exampleSentence = "택시 불러주세요.", exampleTranslation = "Please call a taxi.",
                    memoryHook = "택시 = 'taek-si' = taxi. Same word, Korean pronunciation!",
                    category = VocabCategory.TRANSPORT
                ),
                VocabItem(
                    id = "v_where", korean = "어디", romanization = "eodi", english = "where",
                    exampleSentence = "화장실이 어디예요?", exampleTranslation = "Where is the bathroom?",
                    memoryHook = "'Eo-di' — 'Where'd he go?' — WHERE is 어디!",
                    category = VocabCategory.TRANSPORT
                ),
                VocabItem(
                    id = "v_go", korean = "가다", romanization = "gada", english = "to go",
                    exampleSentence = "서울역에 가고 싶어요.", exampleTranslation = "I want to go to Seoul Station.",
                    memoryHook = "'Ga-da' — gonna 'go-da'? GA-DA means GO!",
                    category = VocabCategory.TRANSPORT
                )
            ),
            phrases = listOf(
                PhraseItem(
                    id = "p_to_location", korean = "___ 에 가주세요",
                    romanization = "___ e gajuseyo",
                    english = "Please take me to ___",
                    context = "Telling a taxi driver your destination",
                    usageTip = "e.g., '명동에 가주세요' (Myeongdong e gajuseyo) = Please take me to Myeongdong"
                ),
                PhraseItem(
                    id = "p_where_is", korean = "___ 이/가 어디예요?",
                    romanization = "___ i/ga eodiyeyo",
                    english = "Where is ___?",
                    context = "Asking for directions",
                    usageTip = "Use 이 after consonant, 가 after vowel. Don't stress it — locals will understand!"
                ),
                PhraseItem(
                    id = "p_how_long", korean = "얼마나 걸려요?",
                    romanization = "eolmana geollyeoyo",
                    english = "How long does it take?",
                    context = "Asking travel duration",
                    usageTip = "Point at your watch for extra clarity!"
                )
            ),
            pronunciationTips = listOf(
                PronunciationTip(
                    sound = "ㄹ (r/l)", description = "Between R and L — a flap sound",
                    englishComparison = "Like the quick 'dd' in American English 'ladder' or 'butter'",
                    commonMistake = "Don't use a hard English R or a clear L — it's a light flap"
                )
            ),
            memoryHooks = listOf(
                MemoryHook(
                    targetWord = "어디",
                    story = "You've lost your friend. 'WHERE'D he go? WHERE'D she go?' WHERE'D = 어디! Point around frantically!",
                    visualDescription = "Person looking confused in a busy Seoul street"
                )
            )
        ),
        Lesson(
            id = "w1d5", weekNumber = 1, dayNumber = 5,
            title = "Numbers & Shopping", subtitle = "Count and pay like a pro",
            emoji = "🛍️", estimatedMinutes = 10,
            isUnlocked = false, isCompleted = false,
            vocabulary = listOf(
                VocabItem(
                    id = "v_one", korean = "일", romanization = "il", english = "one (Sino-Korean)",
                    exampleSentence = "일 층에 있어요.", exampleTranslation = "It's on the first floor.",
                    memoryHook = "'Il' — 'ill' — one sick person. (Sino-Korean: used for phone, money, floors)",
                    category = VocabCategory.NUMBER
                ),
                VocabItem(
                    id = "v_two", korean = "이", romanization = "i", english = "two (Sino-Korean)",
                    exampleSentence = "이 층에 가주세요.", exampleTranslation = "Please go to the second floor.",
                    memoryHook = "'I' — I have TWO eyes. I = 2!",
                    category = VocabCategory.NUMBER
                ),
                VocabItem(
                    id = "v_three", korean = "삼", romanization = "sam", english = "three (Sino-Korean)",
                    exampleSentence = "삼만 원입니다.", exampleTranslation = "It's 30,000 won.",
                    memoryHook = "'Sam' — SAM has THREE letters. Sam = 3!",
                    category = VocabCategory.NUMBER
                ),
                VocabItem(
                    id = "v_won", korean = "원", romanization = "won", english = "Korean currency (won)",
                    exampleSentence = "오천 원 주세요.", exampleTranslation = "Please give me 5,000 won.",
                    memoryHook = "'Won' — you WON the lottery in Korean won!",
                    category = VocabCategory.NUMBER
                ),
                VocabItem(
                    id = "v_expensive", korean = "비싸요", romanization = "bissayo", english = "It's expensive",
                    exampleSentence = "너무 비싸요!", exampleTranslation = "It's too expensive!",
                    memoryHook = "'Bi-ssa-yo' — BIG SACK? Yo, it's expensive to fill a big sack!",
                    category = VocabCategory.SHOPPING
                )
            ),
            phrases = listOf(
                PhraseItem(
                    id = "p_how_much_shop", korean = "이거 얼마예요?",
                    romanization = "igeo eolmayeyo",
                    english = "How much is this?",
                    context = "Shopping at a market or store",
                    usageTip = "Point at the item while asking"
                ),
                PhraseItem(
                    id = "p_too_expensive", korean = "너무 비싸요. 깎아주세요.",
                    romanization = "neomu bissayo. kkakka juseyo.",
                    english = "Too expensive. Please give a discount.",
                    context = "Bargaining at traditional markets (not department stores)",
                    usageTip = "Works at Namdaemun or Dongdaemun market — smile when you say it!"
                ),
                PhraseItem(
                    id = "p_ill_take_it", korean = "이거 살게요",
                    romanization = "igeo salgeyo",
                    english = "I'll take this / I'll buy this",
                    context = "Deciding to purchase",
                    usageTip = "'Sal' = buy. 살게요 = I will buy it"
                )
            ),
            pronunciationTips = listOf(
                PronunciationTip(
                    sound = "ㄲ (kk)", description = "Tense/double consonant — sharper K sound",
                    englishComparison = "Like saying 'k' with extra tension in your throat",
                    commonMistake = "Don't aspirate it — it's tense and clipped, not breathy"
                )
            ),
            memoryHooks = listOf(
                MemoryHook(
                    targetWord = "비싸요",
                    story = "You see a price tag. 'BIG SACK-A-YO!' You need a big sack of money for this expensive item!",
                    visualDescription = "Shocked face looking at a huge price tag"
                )
            )
        )
    )

    fun getWeek2Lessons(): List<Lesson> = listOf(
        Lesson(
            id = "w2d1", weekNumber = 2, dayNumber = 1,
            title = "At the Restaurant", subtitle = "Order a full meal",
            emoji = "🍜", estimatedMinutes = 10,
            isUnlocked = false, isCompleted = false,
            vocabulary = listOf(
                VocabItem(
                    id = "v_menu", korean = "메뉴", romanization = "menyu", english = "menu",
                    exampleSentence = "메뉴 주세요.", exampleTranslation = "Menu, please.",
                    memoryHook = "메뉴 = 'meh-nyu' = menu. Borrowed word — easy!",
                    category = VocabCategory.FOOD
                ),
                VocabItem(
                    id = "v_kimchi", korean = "김치", romanization = "kimchi", english = "kimchi",
                    exampleSentence = "김치찌개 주세요.", exampleTranslation = "Kimchi stew, please.",
                    memoryHook = "You already know kimchi! The world knows kimchi. 김치!",
                    category = VocabCategory.FOOD
                ),
                VocabItem(
                    id = "v_bibimbap", korean = "비빔밥", romanization = "bibimbap", english = "bibimbap (mixed rice bowl)",
                    exampleSentence = "비빔밥 하나 주세요.", exampleTranslation = "One bibimbap, please.",
                    memoryHook = "'BEE-bim-bap' — BEEP BEEP BAP! A robot mixing your rice bowl!",
                    category = VocabCategory.FOOD
                ),
                VocabItem(
                    id = "v_spicy", korean = "매워요", romanization = "maewoyo", english = "It's spicy",
                    exampleSentence = "이거 매워요?", exampleTranslation = "Is this spicy?",
                    memoryHook = "'Mae-wo-yo' — MAY WHOA! Whoa! It's so spicy, MAY I have water?",
                    category = VocabCategory.FOOD
                ),
                VocabItem(
                    id = "v_check", korean = "계산서", romanization = "gyesanseo", english = "bill / check",
                    exampleSentence = "계산서 주세요.", exampleTranslation = "Bill, please.",
                    memoryHook = "'Gye-san-seo' — 'KAY-SAN-Sir' — Sir, Kay wants to see the bill!",
                    category = VocabCategory.FOOD
                )
            ),
            phrases = listOf(
                PhraseItem(
                    id = "p_table_for", korean = "___ 명이요", romanization = "___ myeongiyo",
                    english = "___ people (at restaurant)",
                    context = "Telling host how many people in your party",
                    usageTip = "Hold up fingers too — 두 명이요 (two people) with two fingers up"
                ),
                PhraseItem(
                    id = "p_not_spicy", korean = "안 맵게 해주세요",
                    romanization = "an maepge haejuseyo",
                    english = "Please make it not spicy",
                    context = "Requesting mild food",
                    usageTip = "Very useful for beginners — Korean food can be very spicy!"
                ),
                PhraseItem(
                    id = "p_delicious", korean = "맛있어요!", romanization = "massisseoyo",
                    english = "This is delicious!",
                    context = "Complimenting food — Koreans love to hear this!",
                    usageTip = "Say with enthusiasm and you'll make your host very happy"
                )
            ),
            pronunciationTips = listOf(
                PronunciationTip(
                    sound = "ㅐ (ae)", description = "Like 'e' in 'bed'",
                    englishComparison = "매 (mae) — like 'may' but with a shorter vowel",
                    commonMistake = "Don't confuse with ㅔ (e) — they sound similar but are distinct"
                )
            ),
            memoryHooks = listOf(
                MemoryHook(
                    targetWord = "비빔밥",
                    story = "BEE BIM BAP! Imagine a bee mixing (bim = mix) rice (bap) in a bowl. The bee says BIM BAP!",
                    visualDescription = "Cartoon bee stirring a colorful rice bowl"
                )
            )
        ),
        Lesson(
            id = "w2d2", weekNumber = 2, dayNumber = 2,
            title = "Shopping Trip", subtitle = "Clothes, sizes, and bargaining",
            emoji = "🏪", estimatedMinutes = 12,
            isUnlocked = false, isCompleted = false,
            vocabulary = listOf(
                VocabItem(
                    id = "v_clothes", korean = "옷", romanization = "ot", english = "clothes",
                    exampleSentence = "이 옷이 마음에 들어요.", exampleTranslation = "I like these clothes.",
                    memoryHook = "'Ot' — O-T for Outfit! 옷 = clothes.",
                    category = VocabCategory.SHOPPING
                ),
                VocabItem(
                    id = "v_size", korean = "사이즈", romanization = "saijeu", english = "size",
                    exampleSentence = "제 사이즈가 없어요.", exampleTranslation = "They don't have my size.",
                    memoryHook = "'Sa-ee-jeu' — it's just 'SIZE' with a Korean accent. Borrowed word!",
                    category = VocabCategory.SHOPPING
                ),
                VocabItem(
                    id = "v_cheap", korean = "싸요", romanization = "ssayo", english = "It's cheap / inexpensive",
                    exampleSentence = "여기 정말 싸요!", exampleTranslation = "It's really cheap here!",
                    memoryHook = "'Ssa-yo' — 'SA-YO' like 'Say Yo to savings!' Cheap = 싸요!",
                    category = VocabCategory.SHOPPING
                ),
                VocabItem(
                    id = "v_try_on", korean = "입어봐도 돼요?", romanization = "ibeobbwado dwaeyo",
                    english = "May I try this on?",
                    exampleSentence = "이거 입어봐도 돼요?", exampleTranslation = "Can I try this on?",
                    memoryHook = "'Ip-eo' = wear/put on. 'Bwa-do dway-yo' = may I see? Ask to wear-see!",
                    category = VocabCategory.SHOPPING
                ),
                VocabItem(
                    id = "v_receipt", korean = "영수증", romanization = "yeongsujeung", english = "receipt",
                    exampleSentence = "영수증 주세요.", exampleTranslation = "Receipt, please.",
                    memoryHook = "'Yeong-su-jeung' — 'YOUNG SUIT JUNG' — young Jung in a suit gets a receipt!",
                    category = VocabCategory.SHOPPING
                )
            ),
            phrases = listOf(
                PhraseItem(
                    id = "p_different_color", korean = "다른 색 있어요?",
                    romanization = "dareun saek isseoyo",
                    english = "Do you have a different color?",
                    context = "Looking for color options in a store",
                    usageTip = "'Dareun' = different, 'saek' = color. Combine to unlock all the colors!"
                ),
                PhraseItem(
                    id = "p_card_payment", korean = "카드로 해도 돼요?",
                    romanization = "kadeuro haedo dwaeyo",
                    english = "Can I pay by card?",
                    context = "Asking about payment method",
                    usageTip = "Korea is very card-friendly — most places accept cards"
                ),
                PhraseItem(
                    id = "p_bag_please", korean = "봉투 주세요",
                    romanization = "bongttu juseyo",
                    english = "Bag, please",
                    context = "Requesting a shopping bag",
                    usageTip = "In Korea, bags cost extra — 봉투 주세요 confirms you want one"
                )
            ),
            pronunciationTips = listOf(
                PronunciationTip(
                    sound = "ㅆ (ss)", description = "Tense 's' — like hissing sharply",
                    englishComparison = "싸요 (ssayo) — start the 's' with extra tension, like 'ssss-ayo'",
                    commonMistake = "Regular 's' sounds weak — tense it up for the double consonant"
                )
            ),
            memoryHooks = listOf(
                MemoryHook(
                    targetWord = "싸요 vs 비싸요",
                    story = "싸요 (cheap) vs 비싸요 (expensive). Add 비 (rain cloud 🌧️) to make it MORE — more expensive! Rain makes things cost more!",
                    visualDescription = "Price tags with rain cloud making them more expensive"
                )
            )
        ),
        Lesson(
            id = "w2d3", weekNumber = 2, dayNumber = 3,
            title = "Numbers, Part 2", subtitle = "Native Korean counting",
            emoji = "🔢", estimatedMinutes = 12,
            isUnlocked = false, isCompleted = false,
            vocabulary = listOf(
                VocabItem(
                    id = "v_native_one", korean = "하나", romanization = "hana", english = "one (native Korean)",
                    exampleSentence = "사과 하나 주세요.", exampleTranslation = "One apple, please.",
                    memoryHook = "'Ha-na' — 'HA! NA!' A magician pulls ONE rabbit and shouts HA-NA!",
                    category = VocabCategory.NUMBER
                ),
                VocabItem(
                    id = "v_native_two", korean = "둘", romanization = "dul", english = "two (native Korean)",
                    exampleSentence = "둘 다 주세요.", exampleTranslation = "Both of them, please.",
                    memoryHook = "'Dul' — like 'duel'. A duel needs TWO people. 둘 = two!",
                    category = VocabCategory.NUMBER
                ),
                VocabItem(
                    id = "v_native_three", korean = "셋", romanization = "set", english = "three (native Korean)",
                    exampleSentence = "셋 세요!", exampleTranslation = "Count to three!",
                    memoryHook = "'Set' — like a tennis SET. Three sets to win a match!",
                    category = VocabCategory.NUMBER
                ),
                VocabItem(
                    id = "v_native_four", korean = "넷", romanization = "net", english = "four (native Korean)",
                    exampleSentence = "넷이서 왔어요.", exampleTranslation = "Four of us came.",
                    memoryHook = "'Net' — a volleyball NET has FOUR posts. 넷 = four!",
                    category = VocabCategory.NUMBER
                ),
                VocabItem(
                    id = "v_native_five", korean = "다섯", romanization = "daseot", english = "five (native Korean)",
                    exampleSentence = "다섯 살이에요.", exampleTranslation = "I am five years old.",
                    memoryHook = "'Da-seot' — 'DA-SHOT' — hit the target FIVE times: DA-SHOT!",
                    category = VocabCategory.NUMBER
                )
            ),
            phrases = listOf(
                PhraseItem(
                    id = "p_how_old", korean = "몇 살이에요?",
                    romanization = "myeot sarieyo",
                    english = "How old are you?",
                    context = "Common question — Koreans often ask age to determine speech level",
                    usageTip = "Use native numbers for age: 마흔 살 (forty years old)"
                ),
                PhraseItem(
                    id = "p_what_time", korean = "몇 시예요?",
                    romanization = "myeot siyeyo",
                    english = "What time is it?",
                    context = "Asking the time (hours use native numbers)",
                    usageTip = "Hours = native numbers; minutes = Sino-Korean numbers"
                ),
                PhraseItem(
                    id = "p_count_items", korean = "개 주세요",
                    romanization = "___ gae juseyo",
                    english = "___ pieces, please",
                    context = "Ordering multiple items (개 is the general counter)",
                    usageTip = "하나 = 한, 둘 = 두 before a counter: 한 개, 두 개, 세 개"
                )
            ),
            pronunciationTips = listOf(
                PronunciationTip(
                    sound = "ㅅ at end (t)", description = "Final ㅅ is pronounced as unreleased 't'",
                    englishComparison = "셋 (set) — the final t is not released, like 'set' said with your tongue stuck to the roof",
                    commonMistake = "Don't release the final consonant — Koreans don't explode it"
                )
            ),
            memoryHooks = listOf(
                MemoryHook(
                    targetWord = "Native vs Sino-Korean numbers",
                    story = "Think of it as 'homegrown vs imported'. Native (하나,둘,셋) for counting objects & age. Sino-Korean (일,이,삼) for money, phone numbers, floors.",
                    visualDescription = "Two number lines: one with kimchi jars, one with gold coins"
                )
            )
        ),
        Lesson(
            id = "w2d4", weekNumber = 2, dayNumber = 4,
            title = "Weather Talk", subtitle = "Seasons, temperature & small talk",
            emoji = "🌦️", estimatedMinutes = 10,
            isUnlocked = false, isCompleted = false,
            vocabulary = listOf(
                VocabItem(
                    id = "v_hot", korean = "더워요", romanization = "deowoyo", english = "It's hot",
                    exampleSentence = "오늘 정말 더워요!", exampleTranslation = "It's really hot today!",
                    memoryHook = "'Deo-wo-yo' — 'DEW-whoa-yo!' It's so HOT, even the dew evaporates — whoa!",
                    category = VocabCategory.WEATHER
                ),
                VocabItem(
                    id = "v_cold", korean = "추워요", romanization = "chuwoyo", english = "It's cold",
                    exampleSentence = "오늘 많이 추워요.", exampleTranslation = "It's very cold today.",
                    memoryHook = "'Chu-wo-yo' — 'CHOO-whoa-yo!' The train goes CHOO CHOO in the cold winter!",
                    category = VocabCategory.WEATHER
                ),
                VocabItem(
                    id = "v_rain", korean = "비가 와요", romanization = "biga wayo", english = "It's raining",
                    exampleSentence = "오늘 비가 많이 와요.", exampleTranslation = "It's raining a lot today.",
                    memoryHook = "'Bi-ga wa-yo' — 'Be-ga way-yo!' Raindrops say 'Be on your WAY-yo!' when it rains.",
                    category = VocabCategory.WEATHER
                ),
                VocabItem(
                    id = "v_snow", korean = "눈이 와요", romanization = "nuni wayo", english = "It's snowing",
                    exampleSentence = "눈이 와요! 예뻐요.", exampleTranslation = "It's snowing! How pretty.",
                    memoryHook = "'Nu-ni' = eye/snow. Snowflakes look like tiny eyes falling. Eyes (눈) come down!",
                    category = VocabCategory.WEATHER
                ),
                VocabItem(
                    id = "v_weather", korean = "날씨", romanization = "nalsssi", english = "weather",
                    exampleSentence = "오늘 날씨가 어때요?", exampleTranslation = "How's the weather today?",
                    memoryHook = "'Nal-ssi' — 'NAL-see' — you NAAL-see the weather through the window every morning!",
                    category = VocabCategory.WEATHER
                )
            ),
            phrases = listOf(
                PhraseItem(
                    id = "p_todays_weather", korean = "오늘 날씨 어때요?",
                    romanization = "oneul nalsssi eottaeyo",
                    english = "How's the weather today?",
                    context = "Casual small talk — perfect conversation starter",
                    usageTip = "Koreans love talking about weather — this is your icebreaker!"
                ),
                PhraseItem(
                    id = "p_umbrella", korean = "우산 가져왔어요?",
                    romanization = "usan gajyeowasseoyo",
                    english = "Did you bring an umbrella?",
                    context = "When it looks like rain",
                    usageTip = "'우산' (u-san) = umbrella — like 'oo-SAN' the umbrella samurai!"
                ),
                PhraseItem(
                    id = "p_four_seasons", korean = "한국은 사계절이 뚜렷해요",
                    romanization = "hangugeun sagyejeori tturyeothaeyo",
                    english = "Korea has four distinct seasons",
                    context = "Cultural fact — Koreans are proud of their four seasons",
                    usageTip = "사 = four, 계절 = seasons. Great topic of conversation with Koreans!"
                )
            ),
            pronunciationTips = listOf(
                PronunciationTip(
                    sound = "ㄴ (n)", description = "Clean 'n' sound",
                    englishComparison = "날씨 (nalsssi) — start with tongue behind upper teeth, same as English 'n'",
                    commonMistake = "Keep it clean — don't nasalize the whole word"
                )
            ),
            memoryHooks = listOf(
                MemoryHook(
                    targetWord = "날씨",
                    story = "날씨 = weather. Imagine looking out the window each NAL (날 = day) to SEE (씨 sounds like 'see') the weather. NAL-see the weather!",
                    visualDescription = "Person peering through a window at changing seasons"
                )
            )
        ),
        Lesson(
            id = "w2d5", weekNumber = 2, dayNumber = 5,
            title = "Family & Feelings", subtitle = "People you love and how you feel",
            emoji = "👨‍👩‍👧‍👦", estimatedMinutes = 12,
            isUnlocked = false, isCompleted = false,
            vocabulary = listOf(
                VocabItem(
                    id = "v_mom", korean = "엄마", romanization = "eomma", english = "mom",
                    exampleSentence = "엄마, 배고파요!", exampleTranslation = "Mom, I'm hungry!",
                    memoryHook = "'Eomma' = MOM — sounds like 'umma'. Every language has a mama sound!",
                    category = VocabCategory.FAMILY
                ),
                VocabItem(
                    id = "v_dad", korean = "아빠", romanization = "appa", english = "dad",
                    exampleSentence = "아빠가 요리해요.", exampleTranslation = "Dad is cooking.",
                    memoryHook = "'Appa' — like 'papa' — universal dad sound! 아빠 = 아 (ah) + 빠 (pa). Ah-pa = dad!",
                    category = VocabCategory.FAMILY
                ),
                VocabItem(
                    id = "v_friend", korean = "친구", romanization = "chingu", english = "friend",
                    exampleSentence = "제 친구예요.", exampleTranslation = "This is my friend.",
                    memoryHook = "'Chin-gu' — CHIN-GOO! Your friend has a prominent chin. Chin-friend!",
                    category = VocabCategory.FAMILY
                ),
                VocabItem(
                    id = "v_happy", korean = "행복해요", romanization = "haengbokhaeyo", english = "I'm happy",
                    exampleSentence = "지금 행복해요!", exampleTranslation = "I'm happy right now!",
                    memoryHook = "'Haeng-bok-hae-yo' — 'HANG-BOK-hey-yo!' Hang the BOK (fortune) flag and say hey-yo — you're happy!",
                    category = VocabCategory.FEELINGS
                ),
                VocabItem(
                    id = "v_tired", korean = "피곤해요", romanization = "pigonhaeyo", english = "I'm tired",
                    exampleSentence = "오늘 너무 피곤해요.", exampleTranslation = "I'm very tired today.",
                    memoryHook = "'Pi-gon-hae-yo' — 'PIG-gone-hey-yo!' Even the pig is GONE to bed — so tired!",
                    category = VocabCategory.FEELINGS
                )
            ),
            phrases = listOf(
                PhraseItem(
                    id = "p_how_many_family", korean = "가족이 몇 명이에요?",
                    romanization = "gajogi myeot myeongieeyo",
                    english = "How many people are in your family?",
                    context = "Getting to know someone — common topic in Korean culture",
                    usageTip = "Family talk is important in Korean culture. Have your answer ready!"
                ),
                PhraseItem(
                    id = "p_feeling_good", korean = "기분이 좋아요",
                    romanization = "gibuni johayo",
                    english = "I'm in a good mood / I feel good",
                    context = "Expressing your emotional state",
                    usageTip = "'기분' (gibun) = mood/feeling. 좋아요 = good/like. Mood-good!"
                ),
                PhraseItem(
                    id = "p_miss_you", korean = "보고 싶어요",
                    romanization = "bogo sipeoyo",
                    english = "I miss you",
                    context = "Telling someone you miss them",
                    usageTip = "'Bo-go' = to see + '싶어요' = want. Literally 'I want to see you' — so romantic!"
                )
            ),
            pronunciationTips = listOf(
                PronunciationTip(
                    sound = "ㅇ (silent/ng)", description = "Silent at start, 'ng' at end of syllable",
                    englishComparison = "엄마 (eomma) — the ㅇ at the start is silent; 행 (haeng) — the ㅇ at the end is 'ng'",
                    commonMistake = "Don't add a consonant where ㅇ starts a syllable — it's purely a vowel"
                )
            ),
            memoryHooks = listOf(
                MemoryHook(
                    targetWord = "행복 (happiness)",
                    story = "행 (haeng) = lucky/fortunate + 복 (bok) = blessing. Happiness = lucky blessing! The Korean symbol for happiness is also seen on clothing during Lunar New Year.",
                    visualDescription = "Traditional Korean lucky charm with 복 character in red and gold"
                )
            )
        )
    )

    fun getWeek3Lessons(): List<Lesson> = listOf(
        Lesson(
            id = "w3d1", weekNumber = 3, dayNumber = 1,
            title = "Hotel Check-In", subtitle = "Arrive, settle in, get what you need",
            emoji = "🏨", estimatedMinutes = 12,
            isUnlocked = false, isCompleted = false,
            vocabulary = listOf(
                VocabItem(
                    id = "v_reservation", korean = "예약", romanization = "yeyak", english = "reservation / booking",
                    exampleSentence = "예약했어요.", exampleTranslation = "I have a reservation.",
                    memoryHook = "'Ye-yak' — 'YEY-yak!' You YEY! at a great reservation. Yak = the deal is done!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_room", korean = "방", romanization = "bang", english = "room",
                    exampleSentence = "방이 넓어요.", exampleTranslation = "The room is spacious.",
                    memoryHook = "'Bang' — BANG! You open the door to your room and it BANGS open!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_key", korean = "열쇠", romanization = "yeolsoe", english = "key",
                    exampleSentence = "열쇠 주세요.", exampleTranslation = "Key, please.",
                    memoryHook = "'Yeol-soe' — 'YELL-sway!' The door swayed open when you yelled — the key worked!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_breakfast", korean = "조식", romanization = "josik", english = "breakfast (hotel)",
                    exampleSentence = "조식 포함이에요?", exampleTranslation = "Is breakfast included?",
                    memoryHook = "'Jo-sik' — 'JO-SICK' — Morning Jo (coffee) + sick of missing breakfast. Jo-sik!",
                    category = VocabCategory.FOOD
                ),
                VocabItem(
                    id = "v_checkout", korean = "체크아웃", romanization = "chekeuaut", english = "checkout",
                    exampleSentence = "몇 시에 체크아웃이에요?", exampleTranslation = "What time is checkout?",
                    memoryHook = "'Che-keu-a-ut' = check-out. English borrowing — same word! Easy!",
                    category = VocabCategory.GENERAL
                )
            ),
            phrases = listOf(
                PhraseItem(
                    id = "p_check_in", korean = "___ 이름으로 예약했어요",
                    romanization = "___ ireumeuro yeyakhaesseoyo",
                    english = "I have a reservation under ___",
                    context = "At hotel reception giving your name",
                    usageTip = "Point to your passport while saying this for extra clarity"
                ),
                PhraseItem(
                    id = "p_wifi", korean = "와이파이 비밀번호가 뭐예요?",
                    romanization = "waipai bimilbeonhoga mwoyeyo",
                    english = "What is the Wi-Fi password?",
                    context = "Essential first question at any hotel!",
                    usageTip = "'와이파이' = WiFi (borrowed). '비밀번호' = password (secret + number)"
                ),
                PhraseItem(
                    id = "p_extra_towel", korean = "수건 더 주세요",
                    romanization = "sugeon deo juseyo",
                    english = "More towels, please",
                    context = "Requesting extras from hotel service",
                    usageTip = "'수건' = towel, '더' = more. Works for any item: 베개 더 주세요 (more pillows)"
                )
            ),
            pronunciationTips = listOf(
                PronunciationTip(
                    sound = "ㅈ (j)", description = "Soft 'j' sound, like 'ch' before certain vowels",
                    englishComparison = "조식 (josik) — the ㅈ is a soft 'j' as in 'jam'",
                    commonMistake = "Don't make it too hard — Korean ㅈ is softer than English 'j'"
                )
            ),
            memoryHooks = listOf(
                MemoryHook(
                    targetWord = "예약 (reservation)",
                    story = "예약 sounds like 'Yeah-yak!' You say 'YEAH!' when your hotel reservation is confirmed, then sign on the dotted line (yak = contract/agreement).",
                    visualDescription = "Person giving thumbs up at a hotel reception desk"
                )
            )
        ),
        Lesson(
            id = "w3d2", weekNumber = 3, dayNumber = 2,
            title = "At the Airport", subtitle = "Fly in and out with confidence",
            emoji = "✈️", estimatedMinutes = 12,
            isUnlocked = false, isCompleted = false,
            vocabulary = listOf(
                VocabItem(
                    id = "v_passport", korean = "여권", romanization = "yeogwon", english = "passport",
                    exampleSentence = "여권 보여주세요.", exampleTranslation = "Please show your passport.",
                    memoryHook = "'Yeo-gwon' — 'YEO-gone!' Your passport is GONE if you lose it — protect your yeo-gwon!",
                    category = VocabCategory.TRANSPORT
                ),
                VocabItem(
                    id = "v_flight", korean = "비행기", romanization = "bihaenggi", english = "airplane",
                    exampleSentence = "비행기가 연착됐어요.", exampleTranslation = "The flight is delayed.",
                    memoryHook = "'Bi-haeng-gi' — 'BEE-hang-gee!' A BEE that HANGS in the air with a GEAR. Airplane bee!",
                    category = VocabCategory.TRANSPORT
                ),
                VocabItem(
                    id = "v_gate", korean = "게이트", romanization = "geiteu", english = "gate",
                    exampleSentence = "몇 번 게이트예요?", exampleTranslation = "Which gate is it?",
                    memoryHook = "'Gei-teu' = gate. English borrowed! Just say gate with Korean accent.",
                    category = VocabCategory.TRANSPORT
                ),
                VocabItem(
                    id = "v_luggage", korean = "짐", romanization = "jim", english = "luggage / baggage",
                    exampleSentence = "짐이 너무 많아요.", exampleTranslation = "I have too much luggage.",
                    memoryHook = "'Jim' — your friend JIM always carries too much luggage. Jim = baggage!",
                    category = VocabCategory.TRANSPORT
                ),
                VocabItem(
                    id = "v_delayed", korean = "연착", romanization = "yeonchak", english = "delay / delayed arrival",
                    exampleSentence = "비행기 연착됐어요.", exampleTranslation = "The flight was delayed.",
                    memoryHook = "'Yeon-chak' — 'YEO-CHALK' — the delay is so long you could write a novel with chalk on the floor!",
                    category = VocabCategory.TRANSPORT
                )
            ),
            phrases = listOf(
                PhraseItem(
                    id = "p_where_baggage", korean = "수하물 찾는 곳이 어디예요?",
                    romanization = "suhamul channeun gosi eodiyeyo",
                    english = "Where is baggage claim?",
                    context = "After landing at Korean airport",
                    usageTip = "'수하물' = baggage. Look for signs too — Korean airports have great English signage"
                ),
                PhraseItem(
                    id = "p_customs", korean = "세관 신고할 게 없어요",
                    romanization = "segwan singohalkke eopseoyo",
                    english = "I have nothing to declare",
                    context = "At customs when entering Korea",
                    usageTip = "'세관' = customs. '신고' = declare/report. Keep it simple and honest!"
                ),
                PhraseItem(
                    id = "p_window_aisle", korean = "창가 자리로 주세요",
                    romanization = "changga jariro juseyo",
                    english = "Window seat, please",
                    context = "At check-in counter",
                    usageTip = "'창가' = window side, '통로' = aisle. Swap 창가 for 통로 for an aisle seat"
                )
            ),
            pronunciationTips = listOf(
                PronunciationTip(
                    sound = "ㅎ before vowel (h)", description = "Clear aspirated h",
                    englishComparison = "비행기 (bihaenggi) — the 행 starts with a clear breathy 'h'",
                    commonMistake = "Don't swallow the h — it needs to be heard"
                )
            ),
            memoryHooks = listOf(
                MemoryHook(
                    targetWord = "비행기 (airplane)",
                    story = "비 = fly/fly-like + 행 = travel + 기 = machine. A flying-travel-machine! Korean word structure tells the story: it's literally a 'flying-journey-device'.",
                    visualDescription = "An airplane with Korean characters spelling out its meaning"
                )
            )
        ),
        Lesson(
            id = "w3d3", weekNumber = 3, dayNumber = 3,
            title = "Directions", subtitle = "Find your way anywhere",
            emoji = "🗺️", estimatedMinutes = 12,
            isUnlocked = false, isCompleted = false,
            vocabulary = listOf(
                VocabItem(
                    id = "v_left", korean = "왼쪽", romanization = "oenjjok", english = "left",
                    exampleSentence = "왼쪽으로 가세요.", exampleTranslation = "Go to the left.",
                    memoryHook = "'Oen-jjok' — 'WEN-jjok!' When you WENT wrong, turn left! OEN = left!",
                    category = VocabCategory.TRANSPORT
                ),
                VocabItem(
                    id = "v_right", korean = "오른쪽", romanization = "oreunjjok", english = "right",
                    exampleSentence = "오른쪽에 있어요.", exampleTranslation = "It's on the right side.",
                    memoryHook = "'O-reun-jjok' — 'OH-RUN-jjok!' Oh, RUN to the RIGHT! O-RUN = right!",
                    category = VocabCategory.TRANSPORT
                ),
                VocabItem(
                    id = "v_straight", korean = "직진", romanization = "jikjin", english = "straight ahead",
                    exampleSentence = "직진하세요.", exampleTranslation = "Go straight.",
                    memoryHook = "'Jik-jin' — 'JICK-JIN!' Like a JICK-JIN laser going straight ahead. Laser = straight!",
                    category = VocabCategory.TRANSPORT
                ),
                VocabItem(
                    id = "v_crosswalk", korean = "횡단보도", romanization = "hoengdanbodo", english = "crosswalk / pedestrian crossing",
                    exampleSentence = "횡단보도를 건너세요.", exampleTranslation = "Cross at the crosswalk.",
                    memoryHook = "'Hoeng-dan-bo-do' — 'HONG-DAN-BO-DO!' A HONG Kong dancer BODOing across the street!",
                    category = VocabCategory.TRANSPORT
                ),
                VocabItem(
                    id = "v_exit", korean = "출구", romanization = "chulgu", english = "exit",
                    exampleSentence = "출구가 어디예요?", exampleTranslation = "Where is the exit?",
                    memoryHook = "'Chul-gu' — 'CHOOL-GOO!' School's out (chul = out/exit) through the goo-gate!",
                    category = VocabCategory.TRANSPORT
                )
            ),
            phrases = listOf(
                PhraseItem(
                    id = "p_turn_left", korean = "___ 에서 왼쪽으로 도세요",
                    romanization = "___ eseo oenjjogeuro doseyo",
                    english = "Turn left at ___",
                    context = "Giving or receiving directions",
                    usageTip = "Replace 왼쪽 with 오른쪽 for right turn. 도세요 = please turn"
                ),
                PhraseItem(
                    id = "p_how_far", korean = "여기서 얼마나 멀어요?",
                    romanization = "yeogiseo eolmana meoreoyo",
                    english = "How far is it from here?",
                    context = "Judging if walking is feasible",
                    usageTip = "'멀어요' = far. '가까워요' = close. Listen for these in the answer!"
                ),
                PhraseItem(
                    id = "p_on_map", korean = "지도에 표시해 주시겠어요?",
                    romanization = "jidoe pyosihae jusigesseoyo",
                    english = "Could you mark it on the map?",
                    context = "When verbal directions are confusing",
                    usageTip = "Hand them your phone with Google Maps — universally understood!"
                )
            ),
            pronunciationTips = listOf(
                PronunciationTip(
                    sound = "ㅈㅈ (jj) — tense", description = "Tense double consonant — sharp and clipped",
                    englishComparison = "왼쪽 (oenjjok) — the jj is tight, like saying 'j' with your throat closed",
                    commonMistake = "Don't aspirate it — no breath, just tension"
                )
            ),
            memoryHooks = listOf(
                MemoryHook(
                    targetWord = "왼쪽 vs 오른쪽",
                    story = "왼 = left (sounds like 'when' — when you make a wrong turn you go left!). 오른 = right (sounds like 'or-run' — or run to the right!). WHEN = left, OR-RUN = right.",
                    visualDescription = "Two road signs pointing left and right with Korean labels"
                )
            )
        ),
        Lesson(
            id = "w3d4", weekNumber = 3, dayNumber = 4,
            title = "Emergency Korean", subtitle = "Stay safe with critical phrases",
            emoji = "🚨", estimatedMinutes = 10,
            isUnlocked = false, isCompleted = false,
            vocabulary = listOf(
                VocabItem(
                    id = "v_help", korean = "도와주세요", romanization = "dowajuseyo", english = "Help me, please!",
                    exampleSentence = "도와주세요! 길을 잃었어요.", exampleTranslation = "Help! I'm lost.",
                    memoryHook = "'Do-wa-ju-se-yo' — 'DO-wa-JU-say-yo!' DO tell someone to JUICE SAY YO for help!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_hospital", korean = "병원", romanization = "byeongwon", english = "hospital",
                    exampleSentence = "병원이 어디예요?", exampleTranslation = "Where is the hospital?",
                    memoryHook = "'Byeong-won' — 'BYONG-won!' Being WRONG (sick) you go to byeong-won hospital!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_police", korean = "경찰", romanization = "gyeongchal", english = "police",
                    exampleSentence = "경찰을 불러주세요.", exampleTranslation = "Please call the police.",
                    memoryHook = "'Gyeong-chal' — 'GYONG-chal!' Going CHALK a line around the scene — police!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_lost", korean = "길을 잃었어요", romanization = "gireul ireotseoyo", english = "I'm lost",
                    exampleSentence = "저 길을 잃었어요. 도와주세요.", exampleTranslation = "I'm lost. Please help me.",
                    memoryHook = "'Gil' = road/path. '일었어요' = lost it. You lost the ROAD! Gil-lost!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_emergency", korean = "응급", romanization = "eunggeup", english = "emergency",
                    exampleSentence = "응급 상황이에요!", exampleTranslation = "It's an emergency!",
                    memoryHook = "'Eung-geup' — 'OONG-goop!' Emergency OONGH! Call for help — OONG-GOOP now!",
                    category = VocabCategory.GENERAL
                )
            ),
            phrases = listOf(
                PhraseItem(
                    id = "p_call_ambulance", korean = "119에 전화해 주세요",
                    romanization = "baegguipale jeonhwahae juseyo",
                    english = "Please call 119 (ambulance/fire)",
                    context = "Medical emergency — 119 is Korea's emergency number for ambulance & fire",
                    usageTip = "119 = ambulance & fire. 112 = police. These are Korea's emergency numbers!"
                ),
                PhraseItem(
                    id = "p_stolen", korean = "도둑맞았어요",
                    romanization = "dodungmajasseoyo",
                    english = "I've been robbed / something was stolen",
                    context = "Reporting theft to police or hotel",
                    usageTip = "'도둑' = thief. Point to what was taken for extra clarity"
                ),
                PhraseItem(
                    id = "p_embassy", korean = "대사관이 어디예요?",
                    romanization = "daesagwani eodiyeyo",
                    english = "Where is the embassy?",
                    context = "Passport stolen or serious emergency requiring consular help",
                    usageTip = "Save your country's Korean embassy number before you travel!"
                )
            ),
            pronunciationTips = listOf(
                PronunciationTip(
                    sound = "긴급 tone", description = "Speak clearly and loudly in emergencies",
                    englishComparison = "In emergencies, slow down and speak each syllable clearly: 도-와-주-세-요",
                    commonMistake = "Don't rush — clear slow speech is better understood under stress"
                )
            ),
            memoryHooks = listOf(
                MemoryHook(
                    targetWord = "119 & 112",
                    story = "119 = ambulance/fire (like 911 but flipped). 112 = police (think: 1-1-2, one-one-two, call police). Memorize both before your trip — it could save your life.",
                    visualDescription = "Emergency numbers on a phone screen with Korean flags"
                )
            )
        ),
        Lesson(
            id = "w3d5", weekNumber = 3, dayNumber = 5,
            title = "K-pop Daily Phrases", subtitle = "Speak like your favourite idol",
            emoji = "🎵", estimatedMinutes = 12,
            isUnlocked = false, isCompleted = false,
            vocabulary = listOf(
                VocabItem(
                    id = "v_daebak", korean = "대박", romanization = "daebak", english = "awesome / jackpot / epic",
                    exampleSentence = "대박이다!", exampleTranslation = "That's awesome / epic!",
                    memoryHook = "'Dae-bak' — 'DAY-back!' Every day you get something great back — DAEBAK!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_fighting", korean = "화이팅", romanization = "hwaiting", english = "Fighting! / You can do it! / Go!",
                    exampleSentence = "화이팅! 잘 할 수 있어!", exampleTranslation = "Fighting! You can do it!",
                    memoryHook = "'Hwa-i-ting' = 'FIGHTING!' From English — Korean fans shout this as a cheer!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_oppa", korean = "오빠", romanization = "oppa", english = "older brother (used by females) / term of endearment",
                    exampleSentence = "오빠, 도와줘!", exampleTranslation = "Oppa, help me!",
                    memoryHook = "'Op-pa' — 'OH-pa!' Oh! Pa(pa)! Like calling to an older brother-figure. 오빠!",
                    category = VocabCategory.FAMILY
                ),
                VocabItem(
                    id = "v_aegyo", korean = "애교", romanization = "aegyo", english = "cute charm / acting adorable",
                    exampleSentence = "애교 부려봐!", exampleTranslation = "Show some aegyo!",
                    memoryHook = "'Ae-gyo' — 'AY-gio!' Like saying 'A-G-O' backwards — Aegyo is about going back to being cute like a child!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_saranghae", korean = "사랑해", romanization = "saranghae", english = "I love you (casual)",
                    exampleSentence = "사랑해요! (formal) / 사랑해! (casual)",
                    exampleTranslation = "I love you!",
                    memoryHook = "'Sa-rang-hae' — 'SA-RANG-HEY!' Sarah rang and said hey — she loves you! SA-RANG-HEY!",
                    category = VocabCategory.FEELINGS
                )
            ),
            phrases = listOf(
                PhraseItem(
                    id = "p_jinjja", korean = "진짜요?",
                    romanization = "jinjjayo",
                    english = "Really? / Seriously?",
                    context = "Reacting with surprise — heard constantly in K-dramas",
                    usageTip = "'진짜' = real/true. Add 요 for politeness. Say with wide eyes for full effect!"
                ),
                PhraseItem(
                    id = "p_heol", korean = "헐",
                    romanization = "heol",
                    english = "Wow / OMG / No way (internet/slang)",
                    context = "Shocked reaction — Korean internet slang now used in speech",
                    usageTip = "Like 'OMG' or 'What?!' — short, punchy exclamation of disbelief"
                ),
                PhraseItem(
                    id = "p_jamkkanman", korean = "잠깐만요",
                    romanization = "jamkkanmanyo",
                    english = "Just a moment / Hold on",
                    context = "Asking someone to wait briefly",
                    usageTip = "'잠깐만' = just a moment. Add 요 to be polite. You'll hear this everywhere!"
                )
            ),
            pronunciationTips = listOf(
                PronunciationTip(
                    sound = "사랑 (sarang)", description = "Rolling through the syllables smoothly",
                    englishComparison = "sa-RANG-hae — the 'rang' rhymes with 'song', not 'rang' as in 'ring rang'",
                    commonMistake = "Don't anglicize the vowels — keep the Korean 'a' sounds open"
                )
            ),
            memoryHooks = listOf(
                MemoryHook(
                    targetWord = "사랑해 (I love you)",
                    story = "Sarah (사) rang (랑) to say hey (해) — she loves you! 사랑 = love, 해 = do/say. Doing love = saying 'I love you'.",
                    visualDescription = "Phone with hearts and 사랑해 on screen"
                )
            )
        )
    )

    fun getWeek4Lessons(): List<Lesson> = listOf(
        Lesson(
            id = "w4d1", weekNumber = 4, dayNumber = 1,
            title = "Making Friends", subtitle = "Social phrases & relationships",
            emoji = "🤝", estimatedMinutes = 12,
            isUnlocked = false, isCompleted = false,
            vocabulary = listOf(
                VocabItem(
                    id = "v_chingu", korean = "친구", romanization = "chingu", english = "Friend",
                    exampleSentence = "제 친구예요.", exampleTranslation = "This is my friend.",
                    memoryHook = "'Chin-goo' — your CHIN touches your friend's GOO-d cheek when you hug!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_sunbae", korean = "선배", romanization = "sunbae", english = "Senior / Mentor",
                    exampleSentence = "선배님, 잘 부탁드립니다.", exampleTranslation = "Senior, I'm in your care.",
                    memoryHook = "'Sun-bae' — the SUN before you, like someone who BAYS (blazes) the trail ahead!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_hubae", korean = "후배", romanization = "hubae", english = "Junior / Mentee",
                    exampleSentence = "저는 그의 후배예요.", exampleTranslation = "I am his junior.",
                    memoryHook = "'Hoo-bae' — you HOO (who?) comes after? The BAE who follows behind!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_mannada", korean = "만나다", romanization = "mannada", english = "To meet",
                    exampleSentence = "내일 만나요.", exampleTranslation = "Let's meet tomorrow.",
                    memoryHook = "'Man-na-da' — a MAN NAbbed a DAtE — they MEET!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_bangapda", korean = "반갑습니다", romanization = "bangapseumnida", english = "Nice to meet you",
                    exampleSentence = "안녕하세요, 반갑습니다!", exampleTranslation = "Hello, nice to meet you!",
                    memoryHook = "'Ban-gap-seum-nida' — 'BAN the GAP' — nice to close the distance between us!",
                    category = VocabCategory.GREETING
                )
            ),
            phrases = listOf(
                PhraseItem(
                    id = "p_kakaotalk", korean = "카카오톡 있어요?",
                    romanization = "kakaotok isseoyo",
                    english = "Do you have KakaoTalk?",
                    context = "Exchanging contact info — KakaoTalk is Korea's #1 messaging app",
                    usageTip = "This is the Korean equivalent of asking for WhatsApp or WeChat!"
                ),
                PhraseItem(
                    id = "p_coffee_chaja", korean = "커피 한 잔 할까요?",
                    romanization = "keopi han jan halkkayo",
                    english = "Shall we grab a coffee?",
                    context = "Casual invitation — very common way to bond with new friends",
                    usageTip = "'한 잔' = one cup. This phrase works for any beverage!"
                )
            ),
            pronunciationTips = listOf(
                PronunciationTip(
                    sound = "ㅂ (b/p)", description = "Unaspirated bilabial stop",
                    englishComparison = "Between English 'b' and 'p' — softer than English 'b'",
                    commonMistake = "Don't add a puff of air like English 'p' — keep it gentle"
                )
            ),
            memoryHooks = listOf(
                MemoryHook(
                    targetWord = "친구 (friend)",
                    story = "Your CHIN and your friend's CHIN meet in a hug — chin-goo!",
                    visualDescription = "Two people touching foreheads/chins in friendship"
                )
            )
        ),
        Lesson(
            id = "w4d2", weekNumber = 4, dayNumber = 2,
            title = "Health & Body", subtitle = "Basic medical vocabulary",
            emoji = "🏥", estimatedMinutes = 12,
            isUnlocked = false, isCompleted = false,
            vocabulary = listOf(
                VocabItem(
                    id = "v_byeongwon", korean = "병원", romanization = "byeongwon", english = "Hospital / Clinic",
                    exampleSentence = "병원에 가야 해요.", exampleTranslation = "I need to go to the hospital.",
                    memoryHook = "'Byeong-won' — 'BYEONG!' Won the illness battle? Head to the BYEONG-WON (hospital)!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_apayo", korean = "아파요", romanization = "apayo", english = "It hurts / I'm sick",
                    exampleSentence = "머리가 아파요.", exampleTranslation = "My head hurts.",
                    memoryHook = "'A-pa-yo' — 'AH-PA!' like the sound you make when something hurts: 'AH! PAin! Yo!'",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_yak", korean = "약", romanization = "yak", english = "Medicine / Pharmacy",
                    exampleSentence = "약 주세요.", exampleTranslation = "Please give me medicine.",
                    memoryHook = "'Yak' — 'YAK' medicine tastes like yak! Short word for when you feel yucky!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_meori", korean = "머리", romanization = "meori", english = "Head / Hair",
                    exampleSentence = "머리가 아파요.", exampleTranslation = "My head hurts.",
                    memoryHook = "'Meo-ri' — 'MORE-ee' — MORE pain in the head. Meo-ri means both head AND hair!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_bae", korean = "배", romanization = "bae", english = "Stomach / Belly",
                    exampleSentence = "배가 아파요.", exampleTranslation = "My stomach hurts.",
                    memoryHook = "'Bae' — like the name BAE, but your bae (belly) is hurting! Also means ship!",
                    category = VocabCategory.GENERAL
                )
            ),
            phrases = listOf(
                PhraseItem(
                    id = "p_eodi_apayo", korean = "어디가 아파요?",
                    romanization = "eodi apayo",
                    english = "Where does it hurt?",
                    context = "Doctor or nurse asking about your pain location",
                    usageTip = "'어디' = where. This is the first question any Korean doctor will ask!"
                ),
                PhraseItem(
                    id = "p_yaksuk", korean = "약 처방해 주세요",
                    romanization = "yak cheobalhae juseyo",
                    english = "Please prescribe medicine",
                    context = "Requesting a prescription at the clinic",
                    usageTip = "Korean healthcare is affordable — clinics (의원, uiwon) are everywhere"
                )
            ),
            pronunciationTips = listOf(
                PronunciationTip(
                    sound = "아 vs 어 vowels", description = "Bright 'a' vs dark 'eo'",
                    englishComparison = "'아' = 'ah' as in 'spa'. '어' = 'uh' as in 'ugh'",
                    commonMistake = "아파요: the 아 and 파 both use bright 'ah' — not 'uh'"
                )
            ),
            memoryHooks = listOf(
                MemoryHook(
                    targetWord = "아파요 (hurts/sick)",
                    story = "AH-PA! You stub your toe and yell AH-PA-YO — everyone knows you're in pain!",
                    visualDescription = "Person grabbing their foot yelling with pain"
                )
            )
        ),
        Lesson(
            id = "w4d3", weekNumber = 4, dayNumber = 3,
            title = "Tech & Daily Life", subtitle = "Phones, internet, and modern Korea",
            emoji = "📱", estimatedMinutes = 10,
            isUnlocked = false, isCompleted = false,
            vocabulary = listOf(
                VocabItem(
                    id = "v_handeupon", korean = "핸드폰", romanization = "haendeupon", english = "Mobile phone",
                    exampleSentence = "핸드폰 번호가 뭐예요?", exampleTranslation = "What's your phone number?",
                    memoryHook = "'Haen-deu-pon' — 'HAND-PON'! It's the phone you hold in your HAND. Easy!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_inteones", korean = "인터넷", romanization = "inteonet", english = "Internet",
                    exampleSentence = "인터넷이 빨라요.", exampleTranslation = "The internet is fast.",
                    memoryHook = "'In-teo-net' — it IS the internet! Korea has the world's fastest internet!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_bipbeon", korean = "비밀번호", romanization = "bimilbeonho", english = "Password",
                    exampleSentence = "비밀번호를 입력하세요.", exampleTranslation = "Please enter the password.",
                    memoryHook = "'Bi-mil-beon-ho' — 'BEE MILL BONE HO' — secret bees grinding bones: that's your secret code!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_saginjin", korean = "사진", romanization = "sajin", english = "Photo / Picture",
                    exampleSentence = "사진 찍어도 돼요?", exampleTranslation = "Can I take a photo?",
                    memoryHook = "'Sa-jin' — 'SA-GIN' — SAGin a photo with your phone. SA-JIN, snap!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_baedalhi", korean = "배달", romanization = "baedal", english = "Delivery",
                    exampleSentence = "배달 시켜요.", exampleTranslation = "Let's order delivery.",
                    memoryHook = "'Bae-dal' — 'BAE DAHL' — your BAE DAHL-ies and gets the delivery food!",
                    category = VocabCategory.GENERAL
                )
            ),
            phrases = listOf(
                PhraseItem(
                    id = "p_wifi_password", korean = "와파이 비밀번호가 뭐예요?",
                    romanization = "waipai bimilbeonhoga mwoeyo",
                    english = "What is the Wi-Fi password?",
                    context = "Essential phrase at cafés, restaurants, accommodations",
                    usageTip = "와이파이 (waipai) = Wi-Fi. Koreans are very generous with sharing passwords!"
                ),
                PhraseItem(
                    id = "p_sajin_jjikda", korean = "사진 찍어 주실래요?",
                    romanization = "sajin jjigeo jusillaeyo",
                    english = "Could you take a photo for me?",
                    context = "Asking a stranger to photograph you at a tourist spot",
                    usageTip = "Very polite form — locals are usually happy to help tourists!"
                )
            ),
            pronunciationTips = listOf(
                PronunciationTip(
                    sound = "ㅈ (j)", description = "Affricate similar to English 'j'",
                    englishComparison = "Like 'j' in 'jump' — but slightly crisper",
                    commonMistake = "사진: the 'j' in 'jin' should be clean — not 'zh' like French"
                )
            ),
            memoryHooks = listOf(
                MemoryHook(
                    targetWord = "배달 (delivery)",
                    story = "In Korea, BAE-DAL apps deliver EVERYTHING in 30 minutes. Bae-dal = your lifeline!",
                    visualDescription = "Scooter with delivery boxes racing through city streets"
                )
            )
        ),
        Lesson(
            id = "w4d4", weekNumber = 4, dayNumber = 4,
            title = "Korean Food Deep Dive", subtitle = "Order like a local",
            emoji = "🍜", estimatedMinutes = 12,
            isUnlocked = false, isCompleted = false,
            vocabulary = listOf(
                VocabItem(
                    id = "v_ramyeon", korean = "라면", romanization = "ramyeon", english = "Korean instant noodles",
                    exampleSentence = "라면 끓여 드릴까요?", exampleTranslation = "Shall I boil ramen for you?",
                    memoryHook = "'Ra-myeon' — 'RAH-MYUN' — a RATtling MYUNching sound of slurping noodles!",
                    category = VocabCategory.FOOD
                ),
                VocabItem(
                    id = "v_kimchi", korean = "김치", romanization = "kimchi", english = "Kimchi (fermented vegetables)",
                    exampleSentence = "김치 더 주세요.", exampleTranslation = "Please give me more kimchi.",
                    memoryHook = "'Kim-chi' — 'KIM-CHEE!' — Kim's cheese! But fierier! You already know this one!",
                    category = VocabCategory.FOOD
                ),
                VocabItem(
                    id = "v_bulgogi", korean = "불고기", romanization = "bulgogi", english = "Marinated grilled beef",
                    exampleSentence = "불고기 일 인분 주세요.", exampleTranslation = "One serving of bulgogi, please.",
                    memoryHook = "'Bul-go-gi' — 'BULL-GO-GEE!' — a BULL GOes GrIlling himself. Bul=fire, go=meat, gi=..it!",
                    category = VocabCategory.FOOD
                ),
                VocabItem(
                    id = "v_banchan", korean = "반찬", romanization = "banchan", english = "Side dishes",
                    exampleSentence = "반찬이 맛있어요.", exampleTranslation = "The side dishes are delicious.",
                    memoryHook = "'Ban-chan' — 'BAN CHAN' — not banning Chan's dishes! These free sides are the best part!",
                    category = VocabCategory.FOOD
                ),
                VocabItem(
                    id = "v_maisseo", korean = "맛있어요", romanization = "masisseoyo", english = "It's delicious",
                    exampleSentence = "정말 맛있어요!", exampleTranslation = "It's really delicious!",
                    memoryHook = "'Ma-sis-seo-yo' — 'MAS-IS-SO-YO' — MOST delicious? YES-SO-YO! The YES-O-YO of food!",
                    category = VocabCategory.FOOD
                )
            ),
            phrases = listOf(
                PhraseItem(
                    id = "p_ipun_juseyo", korean = "이 인분 주세요",
                    romanization = "i inbun juseyo",
                    english = "Two servings, please",
                    context = "Ordering at a Korean restaurant — servings are by person count",
                    usageTip = "일 인분 = 1 serving, 이 인분 = 2 servings, 삼 인분 = 3. Always check the minimum order!"
                ),
                PhraseItem(
                    id = "p_maepji", korean = "맵지 않게 해주세요",
                    romanization = "maepji ange haejuseyo",
                    english = "Please make it not spicy",
                    context = "Requesting mild spice level at Korean restaurants",
                    usageTip = "'맵다' = spicy. Korean mild can still be spicy by Western standards — ask twice!"
                )
            ),
            pronunciationTips = listOf(
                PronunciationTip(
                    sound = "맛있어요 (masisseoyo)", description = "Linking consonants across syllables",
                    englishComparison = "맛 (mat) + 있 (it) → sounds like 'ma-sis-seo-yo' when linked",
                    commonMistake = "Don't pause between 맛 and 있 — they link together in speech"
                )
            ),
            memoryHooks = listOf(
                MemoryHook(
                    targetWord = "반찬 (side dishes)",
                    story = "In Korea, BAN the CHANce of being hungry — free banchan comes automatically with every meal!",
                    visualDescription = "Table overflowing with colourful small dishes"
                )
            )
        ),
        Lesson(
            id = "w4d5", weekNumber = 4, dayNumber = 5,
            title = "Time & Schedules", subtitle = "Days, dates and appointments",
            emoji = "📅", estimatedMinutes = 12,
            isUnlocked = false, isCompleted = false,
            vocabulary = listOf(
                VocabItem(
                    id = "v_oneul", korean = "오늘", romanization = "oneul", english = "Today",
                    exampleSentence = "오늘 뭐 해요?", exampleTranslation = "What are you doing today?",
                    memoryHook = "'O-neul' — 'OH-NULL' — OH, today I have NULL plans!",
                    category = VocabCategory.TIME
                ),
                VocabItem(
                    id = "v_naeil", korean = "내일", romanization = "naeil", english = "Tomorrow",
                    exampleSentence = "내일 만나요.", exampleTranslation = "Let's meet tomorrow.",
                    memoryHook = "'Nae-il' — 'NAY-IL' — NAY, not today — tomorrow, in a WHILE (il)!",
                    category = VocabCategory.TIME
                ),
                VocabItem(
                    id = "v_eoje", korean = "어제", romanization = "eoje", english = "Yesterday",
                    exampleSentence = "어제 뭐 했어요?", exampleTranslation = "What did you do yesterday?",
                    memoryHook = "'Eo-je' — 'UH-JEH' — UGH, YEAH, that was yesterday's problem!",
                    category = VocabCategory.TIME
                ),
                VocabItem(
                    id = "v_yoyil", korean = "요일", romanization = "yoil", english = "Day of the week",
                    exampleSentence = "오늘 무슨 요일이에요?", exampleTranslation = "What day of the week is today?",
                    memoryHook = "'Yo-il' — 'YO-IL' — 'YO, what IL (day) is it?' — the suffix for all weekdays!",
                    category = VocabCategory.TIME
                ),
                VocabItem(
                    id = "v_yakso", korean = "약속", romanization = "yakssok", english = "Appointment / Promise / Plan",
                    exampleSentence = "약속 있어요.", exampleTranslation = "I have plans (an appointment).",
                    memoryHook = "'Yak-ssok' — 'YACK-SOCK!' — a yak in a sock = an absurd promise you'll never forget!",
                    category = VocabCategory.TIME
                )
            ),
            phrases = listOf(
                PhraseItem(
                    id = "p_myeossi", korean = "몇 시에 만날까요?",
                    romanization = "myeot sie mannalkkayo",
                    english = "What time shall we meet?",
                    context = "Arranging a meeting time with a friend or colleague",
                    usageTip = "'몇 시' = what time. '에' = at (a time). Essential for making any plans!"
                ),
                PhraseItem(
                    id = "p_woyoil", korean = "월요일에 시간 있어요?",
                    romanization = "woryoile sigan isseoyo",
                    english = "Are you free on Monday?",
                    context = "Checking someone's availability for a specific day",
                    usageTip = "Days: 월(Mon) 화(Tue) 수(Wed) 목(Thu) 금(Fri) 토(Sat) 일(Sun) — all end in 요일"
                )
            ),
            pronunciationTips = listOf(
                PronunciationTip(
                    sound = "약속 (yakssok)", description = "Tensed consonant ㅆ",
                    englishComparison = "The 'ss' sound is tense — like holding your breath while saying 's'",
                    commonMistake = "약속 has a tensed 'ss' in the middle — don't soften it to just 's'"
                )
            ),
            memoryHooks = listOf(
                MemoryHook(
                    targetWord = "약속 (appointment/promise)",
                    story = "A YAK in a SOCK shows up on time for every appointment — a yak-sok is a sacred promise!",
                    visualDescription = "A fluffy yak wearing one oversized sock, looking at a calendar"
                )
            )
        )
    )

    fun getWeek5Lessons(): List<Lesson> = listOf(
        Lesson(
            id = "w5d1", weekNumber = 5, dayNumber = 1,
            title = "Running Errands", subtitle = "Bank, post office & everyday tasks",
            emoji = "🏦", estimatedMinutes = 12,
            isUnlocked = false, isCompleted = false,
            vocabulary = listOf(
                VocabItem(
                    id = "v_eunhaeng", korean = "은행", romanization = "eunhaeng", english = "Bank",
                    exampleSentence = "은행이 어디 있어요?", exampleTranslation = "Where is the bank?",
                    memoryHook = "'Eun-haeng' — 'EUN-HANG' — money HANGS out at the bank! Un-HANG your money there!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_uche", korean = "우체국", romanization = "ucheguk", english = "Post office",
                    exampleSentence = "우체국에 가야 해요.", exampleTranslation = "I need to go to the post office.",
                    memoryHook = "'U-che-guk' — 'OO-CHE-GOOK' — OO! The CHEF cooks at the post GOOK (office)! Mail goes here!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_dolbyeok", korean = "돈", romanization = "don", english = "Money",
                    exampleSentence = "돈이 없어요.", exampleTranslation = "I don't have money.",
                    memoryHook = "'Don' — DON't spend all your DON! Short and sweet — money = don.",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_gyehwek", korean = "계좌", romanization = "gyejwa", english = "Bank account",
                    exampleSentence = "계좌 번호가 뭐예요?", exampleTranslation = "What is the account number?",
                    memoryHook = "'Gye-jwa' — 'GAY-JWA' — 'GAY-JAW dropping' interest rates at your account!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_sotpo", korean = "소포", romanization = "sopo", english = "Package / Parcel",
                    exampleSentence = "소포를 보내고 싶어요.", exampleTranslation = "I want to send a package.",
                    memoryHook = "'So-po' — 'SO-PO' — SO, POst the parcel! It's SO easy to send a SO-PO!",
                    category = VocabCategory.GENERAL
                )
            ),
            phrases = listOf(
                PhraseItem(
                    id = "p_hwanbul", korean = "환불 받을 수 있어요?",
                    romanization = "hwanbul badeul su isseoyo",
                    english = "Can I get a refund?",
                    context = "Shopping or returning items — a key consumer rights phrase",
                    usageTip = "'환불' = refund. Korea has clear return policies — knowing this phrase is empowering!"
                ),
                PhraseItem(
                    id = "p_haetaepon", korean = "현금으로 낼게요",
                    romanization = "hyeongeumeuro naelgeyo",
                    english = "I'll pay in cash",
                    context = "Specifying payment method at shops or markets",
                    usageTip = "'현금' = cash, '카드' = card. Many local markets prefer cash!"
                )
            ),
            pronunciationTips = listOf(
                PronunciationTip(
                    sound = "ㅇ (silent / ng)", description = "ㅇ is silent at the start, 'ng' at the end",
                    englishComparison = "은행: 'eun' ends with 'n', '행' has no initial sound — 'haeng'",
                    commonMistake = "은행 — don't pronounce the initial ㅇ in 행 as a hard 'g' — it's silent!"
                )
            ),
            memoryHooks = listOf(
                MemoryHook(
                    targetWord = "돈 (money)",
                    story = "돈 is SO short because money comes and goes so FAST. Don't blink or your DON is gone!",
                    visualDescription = "A tiny coin labelled 돈 with speed lines"
                )
            )
        ),
        Lesson(
            id = "w5d2", weekNumber = 5, dayNumber = 2,
            title = "Making Plans", subtitle = "Future tense & invitations",
            emoji = "🗓️", estimatedMinutes = 12,
            isUnlocked = false, isCompleted = false,
            vocabulary = listOf(
                VocabItem(
                    id = "v_gatchi", korean = "같이", romanization = "gachi", english = "Together",
                    exampleSentence = "같이 가요!", exampleTranslation = "Let's go together!",
                    memoryHook = "'Ga-chi' — 'GOTCHA!' Gotcha to come TOGETHER! Ga-chi = let's go!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_sijak", korean = "시작", romanization = "sijak", english = "Start / Beginning",
                    exampleSentence = "시작해요!", exampleTranslation = "Let's start!",
                    memoryHook = "'Si-jak' — 'SEE-JAK' — SEE? JACK started it! Si-jak = start!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_keunteun", korean = "계획", romanization = "gyehoek", english = "Plan",
                    exampleSentence = "계획이 있어요?", exampleTranslation = "Do you have a plan?",
                    memoryHook = "'Gye-hoek' — 'GAY-HOOK' — a GAY pirate HOOK draws the PLAN on the treasure map!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_chulbal", korean = "출발", romanization = "chulbal", english = "Departure / Set off",
                    exampleSentence = "출발해요!", exampleTranslation = "Let's go! (departure)",
                    memoryHook = "'Chul-bal' — 'CHOOL-BAL' — 'SCHOOL BUS leaving!' Chulbal = time to depart!",
                    category = VocabCategory.TRANSPORT
                ),
                VocabItem(
                    id = "v_hwakjin", korean = "확인", romanization = "hwakin", english = "Confirm / Check",
                    exampleSentence = "확인해 주세요.", exampleTranslation = "Please confirm / check.",
                    memoryHook = "'Hwa-kin' — 'WACK-KIN' — WACK it with a stamp to CONFIRM! Hwak-in!",
                    category = VocabCategory.GENERAL
                )
            ),
            phrases = listOf(
                PhraseItem(
                    id = "p_hamkke_gaja", korean = "함께 가자!",
                    romanization = "hamkke gaja",
                    english = "Let's go together! (casual)",
                    context = "Casual invitation to friends — energetic and common",
                    usageTip = "'함께' = together (slightly more formal than 같이). Use 가자 with friends only!"
                ),
                PhraseItem(
                    id = "p_mwehal_geoya", korean = "뭐 할 거예요?",
                    romanization = "mwo hal geoyeyo",
                    english = "What are you going to do?",
                    context = "Asking about someone's future plans — conversational",
                    usageTip = "'-ㄹ 거예요' is the go-to future tense ending in Korean. Very common!"
                )
            ),
            pronunciationTips = listOf(
                PronunciationTip(
                    sound = "같이 (gachi)", description = "Palatalization: ㅌ before ㅣ",
                    englishComparison = "같이 sounds like 'ga-chi' not 'ga-ti' — 't' softens to 'ch' before 'i'",
                    commonMistake = "Don't say 'ga-ti' — the t+i combination becomes 'chi' in Korean!"
                )
            ),
            memoryHooks = listOf(
                MemoryHook(
                    targetWord = "같이 (together)",
                    story = "GOTCHA coming together — ga-chi is the magic word that pulls friends into the same adventure!",
                    visualDescription = "Group of friends locking arms and walking forward"
                )
            )
        ),
        Lesson(
            id = "w5d3", weekNumber = 5, dayNumber = 3,
            title = "Describing Things", subtitle = "Adjectives & simple descriptions",
            emoji = "🎨", estimatedMinutes = 10,
            isUnlocked = false, isCompleted = false,
            vocabulary = listOf(
                VocabItem(
                    id = "v_keuda", korean = "크다", romanization = "keuda", english = "Big / Large",
                    exampleSentence = "이 방이 커요.", exampleTranslation = "This room is big.",
                    memoryHook = "'Keu-da' — 'KOO-DA' — a KUDOS-worthy LARGE achievement! Keu = big!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_jakda", korean = "작다", romanization = "jakda", english = "Small / Little",
                    exampleSentence = "이 방이 작아요.", exampleTranslation = "This room is small.",
                    memoryHook = "'Jak-da' — 'JACK-DA' — Cracker JACK fits in a small DA-mty box!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_yeppeuda", korean = "예쁘다", romanization = "yeppeuда", english = "Pretty / Beautiful",
                    exampleSentence = "정말 예뻐요!", exampleTranslation = "You're really pretty!",
                    memoryHook = "'Yep-peu-da' — 'YEP-PRETTY!' — 'YEP, it's PRETTY!' YEPPEUDA!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_bissada", korean = "비싸다", romanization = "bissada", english = "Expensive",
                    exampleSentence = "너무 비싸요.", exampleTranslation = "It's too expensive.",
                    memoryHook = "'Bis-sa-da' — 'BEE-SODA' — a BEE in your SODA is EXPENSIVE to remove!",
                    category = VocabCategory.SHOPPING
                ),
                VocabItem(
                    id = "v_ssada", korean = "싸다", romanization = "ssada", english = "Cheap / Inexpensive",
                    exampleSentence = "정말 싸요!", exampleTranslation = "It's really cheap!",
                    memoryHook = "'Ssa-da' — 'SA-DA' — SAle DAy! Everything's cheap — SSA-DA!",
                    category = VocabCategory.SHOPPING
                )
            ),
            phrases = listOf(
                PhraseItem(
                    id = "p_eottae", korean = "어때요?",
                    romanization = "eottaeyo",
                    english = "How is it? / What do you think?",
                    context = "Asking for someone's opinion on anything",
                    usageTip = "'어때요?' is incredibly versatile — use it for food, clothes, plans, everything!"
                ),
                PhraseItem(
                    id = "p_neomuttal", korean = "너무 달아요",
                    romanization = "neomu darayo",
                    english = "It's too sweet",
                    context = "Describing food taste — also template for any 너무 + adjective",
                    usageTip = "'너무' = too much. 너무 커요 (too big), 너무 비싸요 (too expensive) — very useful pattern!"
                )
            ),
            pronunciationTips = listOf(
                PronunciationTip(
                    sound = "예쁘다 (yeppeuда)", description = "Double consonant ㅃ",
                    englishComparison = "The 'pp' in 예쁘다 is tensed — hold back air then release sharply",
                    commonMistake = "Don't say 'ye-peu-da' — the 쁘 has a tense 'pp' sound, not a soft 'p'"
                )
            ),
            memoryHooks = listOf(
                MemoryHook(
                    targetWord = "너무 (too much)",
                    story = "NEO-MU — Neo from The Matrix dodges everything because everything is TOO MUCH for him. 너무!",
                    visualDescription = "Neo dodging bullets in slow motion, labelled 너무"
                )
            )
        ),
        Lesson(
            id = "w5d4", weekNumber = 5, dayNumber = 4,
            title = "Asking for Help", subtitle = "Getting assistance anywhere",
            emoji = "🆘", estimatedMinutes = 12,
            isUnlocked = false, isCompleted = false,
            vocabulary = listOf(
                VocabItem(
                    id = "v_dowa", korean = "도와주세요", romanization = "dowajuseyo", english = "Please help me",
                    exampleSentence = "도와주세요!", exampleTranslation = "Please help me!",
                    memoryHook = "'Do-wa-ju-se-yo' — 'DO-WAH-JU-SAY-YO!' — Do WAH, do WAH, say the HELP word yo!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_ihaehae", korean = "이해해요", romanization = "ihaehaeyo", english = "I understand",
                    exampleSentence = "네, 이해해요.", exampleTranslation = "Yes, I understand.",
                    memoryHook = "'I-hae-hae-yo' — 'EE-HAY-HAY-YO!' — EE-HAY, like understanding goes HEY HEY when it clicks!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_mogeunda", korean = "모르겠어요", romanization = "moreugeseoyo", english = "I don't know",
                    exampleSentence = "모르겠어요, 죄송합니다.", exampleTranslation = "I don't know, I'm sorry.",
                    memoryHook = "'Mo-reu-ges-eo-yo' — 'MORE-GUESS-O-YO' — MORE guessing? 'O YO' — I just don't know!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_dubeon", korean = "다시 한 번", romanization = "dasi han beon", english = "Once more / Again",
                    exampleSentence = "다시 한 번 말해 주세요.", exampleTranslation = "Please say that once more.",
                    memoryHook = "'Da-si han beon' — 'DA-SI! HAN! BEON!' — DA, SI? HAN more BEON (time) please!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_cheoncheonhi", korean = "천천히", romanization = "cheoncheonhi", english = "Slowly",
                    exampleSentence = "천천히 말해 주세요.", exampleTranslation = "Please speak slowly.",
                    memoryHook = "'Cheon-cheon-hi' — 'CHUN-CHUN-HEE' — CHUN CHUN (train chugging) slowly — HEE HEE!",
                    category = VocabCategory.GENERAL
                )
            ),
            phrases = listOf(
                PhraseItem(
                    id = "p_hangugeo_molla", korean = "한국어를 잘 못해요",
                    romanization = "hangugeoreul jal moshaeyo",
                    english = "I'm not very good at Korean",
                    context = "Humble opener that wins goodwill — Koreans appreciate any attempt",
                    usageTip = "Saying this usually makes Koreans MORE patient and helpful. A great social tool!"
                ),
                PhraseItem(
                    id = "p_yeongeo_haeyo", korean = "영어 하세요?",
                    romanization = "yeongeo haseyo",
                    english = "Do you speak English?",
                    context = "Politely checking if English is an option",
                    usageTip = "영어 = English. Always try Korean first — even a word earns massive goodwill!"
                )
            ),
            pronunciationTips = listOf(
                PronunciationTip(
                    sound = "천천히 (cheoncheonhi)", description = "Aspirated ㅊ consonant",
                    englishComparison = "ㅊ is like 'ch' in 'cheese' but with a strong puff of air",
                    commonMistake = "천천히: each 천 should have a clear aspirated 'ch' — don't reduce to 'ts'"
                )
            ),
            memoryHooks = listOf(
                MemoryHook(
                    targetWord = "천천히 (slowly)",
                    story = "A TRAIN going CHUN-CHUN-HEE slowly through mountains — cheoncheonhi, take it slow!",
                    visualDescription = "Old steam train winding slowly through misty Korean mountains"
                )
            )
        ),
        Lesson(
            id = "w5d5", weekNumber = 5, dayNumber = 5,
            title = "You've Got This!", subtitle = "Putting it all together",
            emoji = "🏆", estimatedMinutes = 15,
            isUnlocked = false, isCompleted = false,
            vocabulary = listOf(
                VocabItem(
                    id = "v_yeonsup", korean = "연습", romanization = "yeonsup", english = "Practice",
                    exampleSentence = "연습이 중요해요.", exampleTranslation = "Practice is important.",
                    memoryHook = "'Yeon-sup' — 'YEON-SOUP' — YEON stirs the SOUP every day: practice makes perfect!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_sillyeok", korean = "실력", romanization = "sillyeok", english = "Skill / Ability",
                    exampleSentence = "실력이 늘었어요!", exampleTranslation = "Your skills have improved!",
                    memoryHook = "'Sil-lyeok' — 'SILL-YORK' — a SILLY person from YORK has no SKILL? Prove them wrong!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_jasingam", korean = "자신감", romanization = "jasingam", english = "Confidence",
                    exampleSentence = "자신감을 가지세요!", exampleTranslation = "Have confidence!",
                    memoryHook = "'Ja-sin-gam' — 'JAR-SING-GAM' — JAR of SINGING GAM gives you CONFIDENCE!",
                    category = VocabCategory.FEELINGS
                ),
                VocabItem(
                    id = "v_nolyeok", korean = "노력", romanization = "noryeok", english = "Effort",
                    exampleSentence = "노력하면 돼요.", exampleTranslation = "You can do it with effort.",
                    memoryHook = "'No-ryeok' — 'NO-RYOK' — 'NO ROCK is too hard with RYOK (effort)!' Keep going!",
                    category = VocabCategory.GENERAL
                ),
                VocabItem(
                    id = "v_sugo", korean = "수고했어요", romanization = "sugohaesseoyo", english = "Good work / Well done",
                    exampleSentence = "수고했어요, 잘했어요!", exampleTranslation = "Good work, well done!",
                    memoryHook = "'Su-go-haes-seo-yo' — 'SUE-GO-HASS-O-YO' — SUE GOES HASSO the finish line: well done!",
                    category = VocabCategory.GENERAL
                )
            ),
            phrases = listOf(
                PhraseItem(
                    id = "p_hwaiting", korean = "화이팅!",
                    romanization = "hwaiting",
                    english = "Fighting! / You can do it! / Go for it!",
                    context = "Korean cheer for encouragement — heard at sports, exams, daily life",
                    usageTip = "From English 'fighting' — Koreans say this to cheer each other on constantly!"
                ),
                PhraseItem(
                    id = "p_cheoncheonhi_haja", korean = "천천히 하자",
                    romanization = "cheoncheonhi haja",
                    english = "Let's take it slow (casual)",
                    context = "Encouraging yourself or a friend to not rush",
                    usageTip = "'하자' = let's do (casual). Use 해요 form for polite: 천천히 해요."
                )
            ),
            pronunciationTips = listOf(
                PronunciationTip(
                    sound = "수고했어요 (sugohaesseoyo)", description = "Multi-syllable flow",
                    englishComparison = "su-go-haes-seo-yo: flow through all five syllables smoothly without pausing",
                    commonMistake = "Don't chop it into separate words — it should roll out as one flowing phrase"
                )
            ),
            memoryHooks = listOf(
                MemoryHook(
                    targetWord = "화이팅 (fighting / you can do it)",
                    story = "화이팅 is 'FIGHTING' — Koreans borrowed it to mean 'go go go!' Fist pump and shout 화이팅!",
                    visualDescription = "Crowd with fists raised cheering 화이팅!"
                )
            )
        )
    )

    fun getAllLessons(): List<Lesson> =
        getWeek1Lessons() + getWeek2Lessons() + getWeek3Lessons() + getWeek4Lessons() + getWeek5Lessons()

    fun getFlashCardsForLesson(lesson: Lesson): List<FlashCard> {
        val cards = mutableListOf<FlashCard>()
        lesson.vocabulary.forEach { vocab ->
            cards.add(
                FlashCard(
                    id = "fc_${lesson.id}_${vocab.id}",
                    lessonId = lesson.id,
                    front = vocab.korean,
                    frontSubtext = vocab.romanization,
                    back = vocab.english,
                    backSubtext = vocab.memoryHook,
                    memoryHook = vocab.memoryHook,
                    reviewState = ReviewState.NEW
                )
            )
        }
        lesson.phrases.forEach { phrase ->
            cards.add(
                FlashCard(
                    id = "fc_${lesson.id}_${phrase.id}",
                    lessonId = lesson.id,
                    front = phrase.korean,
                    frontSubtext = phrase.romanization,
                    back = phrase.english,
                    backSubtext = phrase.usageTip,
                    memoryHook = phrase.usageTip,
                    reviewState = ReviewState.NEW
                )
            )
        }
        return cards
    }

    fun getAllFlashCards(): List<FlashCard> =
        getAllLessons().flatMap { getFlashCardsForLesson(it) }
}

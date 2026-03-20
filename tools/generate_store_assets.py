#!/usr/bin/env python3

from __future__ import annotations

import math
from pathlib import Path

from PIL import Image, ImageDraw, ImageFilter, ImageFont


ROOT = Path(__file__).resolve().parent.parent
APP_RES = ROOT / "app" / "src" / "main" / "res"
STORE_ROOT = ROOT / "store-assets"
PHONE_SCREENSHOTS = STORE_ROOT / "phone-screenshots"
DRAWABLE_NODPI = APP_RES / "drawable-nodpi"
MIPMAPS = {
    "mdpi": 48,
    "hdpi": 72,
    "xhdpi": 96,
    "xxhdpi": 144,
    "xxxhdpi": 192,
}

COLORS = {
    "red": "#E63946",
    "blue": "#457B9D",
    "gold": "#F4A261",
    "teal": "#2A9D8F",
    "cream": "#FFF7EF",
    "cream_alt": "#FFEFD9",
    "navy": "#102C43",
    "navy_soft": "#173D58",
    "surface": "#FBFBFE",
    "surface_alt": "#F1F4F9",
    "ink": "#1B1B1F",
    "muted": "#5F6672",
    "line": "#D7DCE4",
    "white": "#FFFFFF",
    "mono": "#111111",
}

KOREAN_FONT = "/System/Library/Fonts/AppleSDGothicNeo.ttc"
LATIN_FONT = "/System/Library/Fonts/SFNSRounded.ttf"


def rgba(hex_value: str, alpha: int = 255) -> tuple[int, int, int, int]:
    hex_value = hex_value.lstrip("#")
    return tuple(int(hex_value[i : i + 2], 16) for i in (0, 2, 4)) + (alpha,)


def font(size: int, *, latin: bool = False) -> ImageFont.FreeTypeFont:
    font_path = LATIN_FONT if latin else KOREAN_FONT
    return ImageFont.truetype(font_path, size=size)


def rounded_box(draw: ImageDraw.ImageDraw, box, radius: int, fill, outline=None, width: int = 1) -> None:
    draw.rounded_rectangle(box, radius=radius, fill=fill, outline=outline, width=width)


def vertical_gradient(size: tuple[int, int], top: str, bottom: str) -> Image.Image:
    width, height = size
    image = Image.new("RGBA", size)
    draw = ImageDraw.Draw(image)
    top_rgb = rgba(top)
    bottom_rgb = rgba(bottom)
    for y in range(height):
        ratio = y / max(height - 1, 1)
        color = tuple(
            round(top_rgb[i] * (1 - ratio) + bottom_rgb[i] * ratio)
            for i in range(4)
        )
        draw.line((0, y, width, y), fill=color)
    return image


def radial_glow(size: tuple[int, int], center: tuple[float, float], radius: float, color: str, alpha: int) -> Image.Image:
    width, height = size
    layer = Image.new("RGBA", size, (0, 0, 0, 0))
    px = layer.load()
    cx, cy = center
    for x in range(width):
        for y in range(height):
            distance = math.dist((x, y), (cx, cy))
            if distance <= radius:
                falloff = 1 - (distance / radius)
                px[x, y] = rgba(color, int(alpha * (falloff ** 1.8)))
    return layer.filter(ImageFilter.GaussianBlur(radius=12))


def shadow_layer(size: tuple[int, int], box, radius: int, color: tuple[int, int, int, int], blur: int, offset=(0, 0)) -> Image.Image:
    shadow = Image.new("RGBA", size, (0, 0, 0, 0))
    draw = ImageDraw.Draw(shadow)
    shifted = (box[0] + offset[0], box[1] + offset[1], box[2] + offset[0], box[3] + offset[1])
    draw.rounded_rectangle(shifted, radius=radius, fill=color)
    return shadow.filter(ImageFilter.GaussianBlur(blur))


def build_taeguk(radius: int, *, rotate_degrees: float = -18.0, monochrome: bool = False) -> Image.Image:
    diameter = radius * 2
    image = Image.new("RGBA", (diameter, diameter), (0, 0, 0, 0))
    draw = ImageDraw.Draw(image)
    if monochrome:
        draw.ellipse((0, 0, diameter, diameter), fill=rgba(COLORS["mono"]))
        return image.rotate(rotate_degrees, resample=Image.Resampling.BICUBIC, expand=False)

    draw.pieslice((0, 0, diameter, diameter), 90, 270, fill=rgba(COLORS["red"]))
    draw.pieslice((0, 0, diameter, diameter), 270, 90, fill=rgba(COLORS["blue"]))
    draw.ellipse((radius // 2, 0, radius + radius // 2, radius), fill=rgba(COLORS["red"]))
    draw.ellipse((radius // 2, radius, radius + radius // 2, diameter), fill=rgba(COLORS["blue"]))
    draw.ellipse((radius // 2, radius // 4, radius + radius // 2, radius + radius // 4), outline=rgba(COLORS["surface"]), width=max(1, radius // 12))
    return image.rotate(rotate_degrees, resample=Image.Resampling.BICUBIC, expand=False)


def draw_icon_foreground(size: int, *, monochrome: bool = False) -> Image.Image:
    image = Image.new("RGBA", (size, size), (0, 0, 0, 0))
    draw = ImageDraw.Draw(image)

    def s(value: int) -> int:
        return max(1, round(value * size / 432))

    bubble_box = (s(78), s(62), size - s(78), size - s(84))
    tail = [
        (size // 2 - s(34), size - s(104)),
        (size // 2 + s(18), size - s(104)),
        (size // 2 - s(6), size - s(52)),
    ]
    fill = rgba(COLORS["white"] if monochrome else COLORS["cream"])
    outline = rgba(COLORS["mono"] if monochrome else COLORS["gold"], 255)

    image.alpha_composite(
        shadow_layer(
            (size, size),
            bubble_box,
            radius=s(84),
            color=(16, 44, 67, 44 if monochrome else 54),
            blur=s(18),
            offset=(0, s(14)),
        )
    )
    rounded_box(draw, bubble_box, radius=s(84), fill=fill, outline=outline, width=max(1, s(8)))
    draw.polygon(tail, fill=fill, outline=outline)
    draw.line((tail[0], tail[2], tail[1]), fill=outline, width=max(1, s(8)), joint="curve")

    if monochrome:
        draw.rounded_rectangle((s(140), s(122), size - s(140), s(250)), radius=s(56), fill=rgba(COLORS["mono"]))
        draw.rounded_rectangle((s(156), s(268), size - s(156), s(300)), radius=s(16), fill=rgba(COLORS["mono"]))
        for index, circle_x in enumerate((s(164), s(216), s(274))):
            circle_y = s(320 - index * 28)
            draw.ellipse((circle_x, circle_y, circle_x + s(34), circle_y + s(34)), fill=rgba(COLORS["mono"]))
        return image

    inner_box = (s(132), s(110), size - s(132), s(278))
    rounded_box(draw, inner_box, radius=s(56), fill=rgba(COLORS["surface"]), outline=rgba(COLORS["line"]), width=max(1, s(4)))
    taeguk = build_taeguk(s(68))
    image.alpha_composite(taeguk, (size // 2 - taeguk.width // 2, s(194) - taeguk.height // 2))
    draw.arc((s(150), s(294), size - s(150), s(358)), start=205, end=344, fill=rgba(COLORS["teal"]), width=max(1, s(8)))
    for idx, circle_x in enumerate((s(164), s(216), s(274))):
        circle_y = s(320 - idx * 28)
        circle_color = [COLORS["gold"], COLORS["blue"], COLORS["red"]][idx]
        draw.ellipse((circle_x, circle_y, circle_x + s(34), circle_y + s(34)), fill=rgba(circle_color))
    return image


def draw_full_icon(size: int) -> Image.Image:
    base = vertical_gradient((size, size), COLORS["navy_soft"], COLORS["navy"])
    base.alpha_composite(radial_glow((size, size), (size * 0.24, size * 0.16), size * 0.42, COLORS["gold"], 54))
    base.alpha_composite(radial_glow((size, size), (size * 0.78, size * 0.86), size * 0.46, COLORS["blue"], 58))
    base.alpha_composite(radial_glow((size, size), (size * 0.72, size * 0.14), size * 0.28, COLORS["red"], 46))
    foreground = draw_icon_foreground(size)
    base.alpha_composite(foreground)
    return base


def save_app_icons() -> None:
    DRAWABLE_NODPI.mkdir(parents=True, exist_ok=True)
    foreground = draw_icon_foreground(432)
    foreground.save(DRAWABLE_NODPI / "ic_launcher_foreground.png")
    monochrome = draw_icon_foreground(432, monochrome=True)
    monochrome.save(DRAWABLE_NODPI / "ic_launcher_monochrome.png")

    for density, size in MIPMAPS.items():
        icon = draw_full_icon(size)
        mipmap_dir = APP_RES / f"mipmap-{density}"
        mipmap_dir.mkdir(parents=True, exist_ok=True)
        icon.save(mipmap_dir / "ic_launcher.png")
        icon.save(mipmap_dir / "ic_launcher_round.png")


def add_soft_background(image: Image.Image) -> None:
    image.alpha_composite(radial_glow(image.size, (180, 220), 260, COLORS["red"], 36))
    image.alpha_composite(radial_glow(image.size, (910, 260), 320, COLORS["blue"], 44))
    image.alpha_composite(radial_glow(image.size, (540, 1600), 420, COLORS["gold"], 32))


def pill(draw: ImageDraw.ImageDraw, xy, text: str, fill: str, text_fill: str, size: int = 34) -> None:
    x, y, w, h = xy
    rounded_box(draw, (x, y, x + w, y + h), radius=h // 2, fill=rgba(fill))
    text_width = draw.textlength(text, font=font(size))
    draw.text((x + (w - text_width) / 2, y + (h - size) / 2 - 3), text, font=font(size), fill=rgba(text_fill))


def screenshot_canvas() -> Image.Image:
    image = vertical_gradient((1080, 1920), COLORS["surface"], "#F7F1EA")
    add_soft_background(image)
    return image


def draw_status_bar(draw: ImageDraw.ImageDraw, time_text: str) -> None:
    draw.text((72, 54), time_text, font=font(32, latin=True), fill=rgba(COLORS["ink"]))
    for index, width in enumerate((26, 18, 10)):
        draw.rounded_rectangle((916 + index * 18, 64 - width, 926 + index * 18, 64), radius=3, fill=rgba(COLORS["ink"]))
    draw.rounded_rectangle((980, 46, 1026, 72), radius=8, outline=rgba(COLORS["ink"]), width=3)
    draw.rounded_rectangle((1028, 54, 1036, 64), radius=3, fill=rgba(COLORS["ink"]))
    draw.rounded_rectangle((984, 50, 1018, 68), radius=5, fill=rgba(COLORS["ink"]))


def draw_icon_mark(image: Image.Image, box: tuple[int, int, int, int]) -> None:
    icon = draw_full_icon(box[2] - box[0]).resize((box[2] - box[0], box[3] - box[1]), Image.Resampling.LANCZOS)
    image.alpha_composite(icon, (box[0], box[1]))


def make_onboarding_screenshot(path: Path) -> None:
    image = screenshot_canvas()
    draw = ImageDraw.Draw(image)
    draw_status_bar(draw, "9:41")
    draw.rounded_rectangle((112, 204, 968, 1640), radius=72, fill=rgba(COLORS["white"], 222), outline=rgba("#E9D8C6"), width=3)

    progress_x = 344
    for index in range(6):
        is_selected = index == 0
        width = 86 if is_selected else 28
        draw.rounded_rectangle((progress_x, 276, progress_x + width, 300), radius=12, fill=rgba(COLORS["red"] if is_selected else "#D1D7E0"))
        progress_x += width + 18

    draw_icon_mark(image, (380, 382, 700, 702))
    draw.text((365, 778), "안녕하세요!", font=font(88), fill=rgba(COLORS["red"]))
    draw.text((434, 884), "Hello!", font=font(54, latin=True), fill=rgba(COLORS["muted"]))
    body = "Master practical Korean with memory hacks\nthat stick. 10 minutes a day is all you need."
    draw.multiline_text((208, 1010), body, font=font(42), fill=rgba(COLORS["ink"]), spacing=14, align="center")

    rounded_box(draw, (200, 1382, 880, 1488), radius=34, fill=rgba(COLORS["red"]))
    button_text = "Let's start! 시작!"
    text_width = draw.textlength(button_text, font=font(42))
    draw.text((540 - text_width / 2, 1414), button_text, font=font(42), fill=rgba(COLORS["white"]))
    image.save(path)


def draw_metric(draw: ImageDraw.ImageDraw, x: int, y: int, value: str, label: str) -> None:
    draw.text((x, y), value, font=font(48, latin=True), fill=rgba(COLORS["ink"]))
    draw.text((x, y + 60), label, font=font(28, latin=True), fill=rgba(COLORS["muted"]))


def lesson_card(draw: ImageDraw.ImageDraw, y: int, title: str, subtitle: str, accent: str) -> None:
    rounded_box(draw, (72, y, 1008, y + 228), radius=42, fill=rgba(COLORS["white"], 236), outline=rgba(COLORS["line"]), width=3)
    rounded_box(draw, (104, y + 38, 220, y + 154), radius=34, fill=rgba(accent, 220))
    draw.text((144, y + 70), "가", font=font(64), fill=rgba(COLORS["white"]))
    draw.text((262, y + 52), title, font=font(44), fill=rgba(COLORS["ink"]))
    draw.text((262, y + 110), subtitle, font=font(30), fill=rgba(COLORS["muted"]))
    rounded_box(draw, (262, y + 152, 414, y + 194), radius=18, fill=rgba("#FDE4E1"))
    draw.text((292, y + 161), "10 min", font=font(24, latin=True), fill=rgba(COLORS["red"]))
    rounded_box(draw, (838, y + 60, 952, y + 174), radius=26, fill=rgba("#F2F6FB"))
    draw.text((872, y + 96), ">", font=font(52, latin=True), fill=rgba(COLORS["blue"]))


def make_dashboard_screenshot(path: Path) -> None:
    image = screenshot_canvas()
    draw = ImageDraw.Draw(image)
    draw_status_bar(draw, "9:41")
    draw.text((72, 134), "좋은 아침이에요, Alex!", font=font(56), fill=rgba(COLORS["ink"]))
    draw.text((72, 206), "Let's learn some Korean today", font=font(30, latin=True), fill=rgba(COLORS["muted"]))

    rounded_box(draw, (72, 278, 1008, 600), radius=48, fill=rgba(COLORS["white"], 238), outline=rgba(COLORS["line"]), width=3)
    rounded_box(draw, (108, 322, 196, 410), radius=30, fill=rgba("#E8F0FB"))
    draw.text((130, 334), "7", font=font(52, latin=True), fill=rgba(COLORS["blue"]))
    draw.text((224, 328), "Day Streak", font=font(40, latin=True), fill=rgba(COLORS["ink"]))
    draw.text((224, 382), "Keep it up!", font=font(28, latin=True), fill=rgba(COLORS["muted"]))
    pill(draw, (782, 332, 152, 48), "Level 1", "#FFE7E2", COLORS["red"], 26)
    draw.line((108, 466, 972, 466), fill=rgba(COLORS["line"]), width=3)
    draw_metric(draw, 126, 510, "12", "Lessons")
    draw_metric(draw, 402, 510, "34", "Mastered")
    draw_metric(draw, 676, 510, "86m", "This week")

    draw.text((72, 670), "Your Path", font=font(46, latin=True), fill=rgba(COLORS["ink"]))
    draw.text((828, 676), "All Lessons", font=font(28, latin=True), fill=rgba(COLORS["red"]))
    lesson_card(draw, 742, "Hello, Korea!", "Week 1 • Greetings", COLORS["red"])
    lesson_card(draw, 1002, "Cafe & Food", "Week 1 • Ordering basics", COLORS["blue"])

    draw.text((72, 1298), "Quick Practice", font=font(46, latin=True), fill=rgba(COLORS["ink"]))
    rounded_box(draw, (72, 1384, 500, 1630), radius=42, fill=rgba("#FFEFE1"))
    draw.text((118, 1444), "Writing", font=font(40, latin=True), fill=rgba(COLORS["ink"]))
    draw.text((118, 1502), "Trace Hangul with stroke guides", font=font(26, latin=True), fill=rgba(COLORS["muted"]))
    rounded_box(draw, (580, 1384, 1008, 1630), radius=42, fill=rgba("#E5EFF8"))
    draw.text((626, 1444), "Speaking", font=font(40, latin=True), fill=rgba(COLORS["ink"]))
    draw.text((626, 1502), "Practice real pronunciation", font=font(26, latin=True), fill=rgba(COLORS["muted"]))
    image.save(path)


def tab(draw: ImageDraw.ImageDraw, x: int, label: str, selected: bool = False) -> None:
    text_fill = COLORS["red"] if selected else COLORS["muted"]
    draw.text((x, 640), label, font=font(28, latin=True), fill=rgba(text_fill))
    if selected:
        draw.rounded_rectangle((x - 4, 686, x + 104, 694), radius=4, fill=rgba(COLORS["red"]))


def vocab_card(draw: ImageDraw.ImageDraw, y: int, ko: str, roman: str, en: str) -> None:
    rounded_box(draw, (72, y, 1008, y + 220), radius=42, fill=rgba(COLORS["white"], 240), outline=rgba(COLORS["line"]), width=3)
    draw.text((116, y + 52), ko, font=font(62), fill=rgba(COLORS["red"]))
    draw.text((116, y + 128), roman, font=font(28, latin=True), fill=rgba(COLORS["muted"]))
    pill(draw, (728, y + 72, 200, 54), en, "#E4EFF6", COLORS["blue"], 28)
    draw.text((116, y + 172), "A travel-ready word with a sticky memory hook.", font=font(24, latin=True), fill=rgba(COLORS["muted"]))


def make_lesson_screenshot(path: Path) -> None:
    image = screenshot_canvas()
    draw = ImageDraw.Draw(image)
    draw_status_bar(draw, "9:41")
    draw.text((72, 138), "Cafe & Food", font=font(52, latin=True), fill=rgba(COLORS["ink"]))
    draw.text((72, 204), "Practical ordering Korean", font=font(28, latin=True), fill=rgba(COLORS["muted"]))

    rounded_box(draw, (72, 270, 1008, 548), radius=52, fill=rgba("#FFE4E2"))
    draw_icon_mark(image, (126, 326, 294, 494))
    draw.text((336, 330), "커피", font=font(70), fill=rgba(COLORS["red"]))
    draw.text((336, 410), "keopi • coffee", font=font(30, latin=True), fill=rgba(COLORS["muted"]))
    pill(draw, (336, 466, 150, 44), "Week 1", "#FFF7EF", COLORS["red"], 24)
    pill(draw, (504, 466, 150, 44), "10 min", "#FFF7EF", COLORS["red"], 24)
    pill(draw, (672, 466, 168, 44), "5 words", "#FFF7EF", COLORS["red"], 24)

    rounded_box(draw, (72, 596, 1008, 716), radius=34, fill=rgba(COLORS["white"], 222))
    tab(draw, 118, "Vocabulary", True)
    tab(draw, 348, "Phrases", False)
    tab(draw, 540, "Speaking", False)
    tab(draw, 742, "Memory", False)

    vocab_card(draw, 764, "주세요", "juseyo", "please")
    vocab_card(draw, 1010, "맛있어요", "masisseoyo", "delicious")

    rounded_box(draw, (72, 1696, 456, 1808), radius=38, fill=rgba(COLORS["navy"]))
    rounded_box(draw, (520, 1696, 1008, 1808), radius=38, fill=rgba(COLORS["red"]))
    draw.text((170, 1732), "Flashcards", font=font(34, latin=True), fill=rgba(COLORS["white"]))
    draw.text((700, 1732), "Start Quiz", font=font(34, latin=True), fill=rgba(COLORS["white"]))
    image.save(path)


def waveform(draw: ImageDraw.ImageDraw, x: int, y: int, width: int, height: int) -> None:
    bar_width = 18
    gap = 12
    values = [0.24, 0.42, 0.7, 0.92, 0.58, 0.34, 0.66, 0.88, 0.52, 0.3, 0.18, 0.48]
    for index, value in enumerate(values):
        bar_height = int(height * value)
        left = x + index * (bar_width + gap)
        top = y + (height - bar_height) // 2
        draw.rounded_rectangle((left, top, left + bar_width, top + bar_height), radius=9, fill=rgba(COLORS["teal"]))


def feedback_card(draw: ImageDraw.ImageDraw, y: int, title: str, body: str, accent: str) -> None:
    rounded_box(draw, (72, y, 1008, y + 170), radius=40, fill=rgba(COLORS["white"], 238), outline=rgba(COLORS["line"]), width=3)
    draw.rounded_rectangle((106, y + 40, 126, y + 130), radius=10, fill=rgba(accent))
    draw.text((156, y + 40), title, font=font(34, latin=True), fill=rgba(COLORS["ink"]))
    draw.text((156, y + 92), body, font=font(24), fill=rgba(COLORS["muted"]))


def make_pronunciation_screenshot(path: Path) -> None:
    image = screenshot_canvas()
    draw = ImageDraw.Draw(image)
    draw_status_bar(draw, "9:41")
    draw.text((72, 138), "Pronunciation Practice", font=font(50, latin=True), fill=rgba(COLORS["ink"]))
    draw.text((72, 206), "Listen, repeat, and get actionable feedback", font=font(28, latin=True), fill=rgba(COLORS["muted"]))

    rounded_box(draw, (72, 300, 1008, 860), radius=54, fill=rgba(COLORS["white"], 236), outline=rgba(COLORS["line"]), width=3)
    rounded_box(draw, (392, 372, 688, 668), radius=148, fill=rgba("#E7F2F0"))
    draw.rounded_rectangle((500, 446, 580, 576), radius=22, fill=rgba(COLORS["teal"]))
    draw.rounded_rectangle((474, 552, 606, 592), radius=20, fill=rgba(COLORS["teal"]))
    draw.text((434, 706), "Listening...", font=font(42, latin=True), fill=rgba(COLORS["ink"]))
    waveform(draw, 272, 770, 536, 72)

    rounded_box(draw, (72, 928, 450, 1134), radius=42, fill=rgba("#E8F0FB"))
    rounded_box(draw, (478, 928, 1008, 1134), radius=42, fill=rgba("#FFEFE1"))
    draw.text((112, 970), "Overall Score", font=font(28, latin=True), fill=rgba(COLORS["muted"]))
    draw.text((112, 1018), "86 / 100", font=font(54, latin=True), fill=rgba(COLORS["blue"]))
    draw.text((518, 970), "Best Tip", font=font(28, latin=True), fill=rgba(COLORS["muted"]))
    draw.text((518, 1018), "Soften the final yo", font=font(34), fill=rgba(COLORS["red"]))

    feedback_card(draw, 1200, "Syllable Clarity", "Your vowel shape is strong and stable.", COLORS["blue"])
    feedback_card(draw, 1410, "Rhythm", "Pause less between sounds to feel more natural.", COLORS["gold"])

    rounded_box(draw, (72, 1696, 1008, 1808), radius=38, fill=rgba(COLORS["red"]))
    text_width = draw.textlength("Start Listening", font=font(36, latin=True))
    draw.text((540 - text_width / 2, 1730), "Start Listening", font=font(36, latin=True), fill=rgba(COLORS["white"]))
    image.save(path)


def make_feature_graphic(path: Path) -> None:
    image = vertical_gradient((1024, 500), "#FFF7EF", "#E9F1F8")
    image.alpha_composite(radial_glow(image.size, (212, 118), 180, COLORS["gold"], 42))
    image.alpha_composite(radial_glow(image.size, (820, 132), 250, COLORS["blue"], 42))
    image.alpha_composite(radial_glow(image.size, (756, 412), 220, COLORS["red"], 30))
    draw = ImageDraw.Draw(image)

    rounded_box(draw, (36, 52, 438, 454), radius=102, fill=rgba(COLORS["navy"]))
    icon = draw_full_icon(338)
    image.alpha_composite(icon, (68, 84))

    draw.text((496, 92), "Korean Coach", font=font(64, latin=True), fill=rgba(COLORS["ink"]))
    draw.multiline_text(
        (500, 176),
        "10-minute Korean for\nabsolute beginners",
        font=font(30, latin=True),
        fill=rgba(COLORS["muted"]),
        spacing=8,
    )
    draw.text((500, 264), "Guided lessons, memory hooks, speaking practice", font=font(24), fill=rgba(COLORS["ink"]))
    pill(draw, (500, 316, 150, 46), "Daily path", "#FFE7E2", COLORS["red"], 24)
    pill(draw, (666, 316, 176, 46), "Pronunciation", "#E4EFF6", COLORS["blue"], 24)
    pill(draw, (500, 378, 188, 46), "Spaced review", "#E8F3F1", COLORS["teal"], 24)
    image.save(path)


def write_readme() -> None:
    STORE_ROOT.mkdir(parents=True, exist_ok=True)
    readme = STORE_ROOT / "README.md"
    readme.write_text(
        "\n".join(
            [
                "# Store Assets",
                "",
                "Generated by `tools/generate_store_assets.py`.",
                "",
                "## Included",
                "- `play-store-icon-512.png`: Google Play high-res app icon.",
                "- `feature-graphic-1024x500.png`: Google Play feature graphic.",
                "- `phone-screenshots/*.png`: portrait store images based on the current app UI structure.",
                "",
                "## Android Icon Architecture",
                "- `app/src/main/res/drawable/ic_launcher_background.xml`: solid adaptive icon background.",
                "- `app/src/main/res/drawable-nodpi/ic_launcher_foreground.png`: foreground artwork.",
                "- `app/src/main/res/drawable-nodpi/ic_launcher_monochrome.png`: monochrome Android 13+ artwork.",
                "- `app/src/main/res/mipmap-anydpi-v26/*.xml`: adaptive icon layer definitions.",
                "",
                "## Design Rationale",
                "- Speech-bubble card: guided speaking-first learning.",
                "- Taegeuk-inspired red/blue core: Korean identity without relying on text.",
                "- Rising three-dot path: structured lesson flow, review cadence, and progress momentum.",
            ]
        )
        + "\n",
        encoding="utf-8",
    )


def main() -> None:
    STORE_ROOT.mkdir(parents=True, exist_ok=True)
    PHONE_SCREENSHOTS.mkdir(parents=True, exist_ok=True)
    save_app_icons()
    draw_full_icon(512).save(STORE_ROOT / "play-store-icon-512.png")
    make_feature_graphic(STORE_ROOT / "feature-graphic-1024x500.png")
    make_onboarding_screenshot(PHONE_SCREENSHOTS / "01-onboarding-welcome.png")
    make_dashboard_screenshot(PHONE_SCREENSHOTS / "02-dashboard-path.png")
    make_lesson_screenshot(PHONE_SCREENSHOTS / "03-lesson-detail.png")
    make_pronunciation_screenshot(PHONE_SCREENSHOTS / "04-pronunciation-practice.png")
    write_readme()


if __name__ == "__main__":
    main()

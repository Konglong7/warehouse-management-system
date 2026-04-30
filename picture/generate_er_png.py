from PIL import Image, ImageDraw, ImageFont

WIDTH = 2600
HEIGHT = 1900
WHITE = (255, 255, 255)
BLACK = (0, 0, 0)


def load_font(size, bold=False):
    files = [
        "C:/Windows/Fonts/msyhbd.ttc" if bold else "C:/Windows/Fonts/msyh.ttc",
        "C:/Windows/Fonts/simhei.ttf",
        "C:/Windows/Fonts/simsun.ttc",
    ]
    for file in files:
        try:
            return ImageFont.truetype(file, size)
        except OSError:
            continue
    return ImageFont.load_default()


title_font = load_font(34, True)
entity_font = load_font(30, True)
attr_font = load_font(20, True)
relation_font = load_font(22, True)
note_font = load_font(18, False)
card_font = load_font(18, True)

img = Image.new("RGB", (WIDTH, HEIGHT), WHITE)
draw = ImageDraw.Draw(img)


def ctext(x, y, text, font):
    box = draw.textbbox((0, 0), text, font=font)
    w = box[2] - box[0]
    h = box[3] - box[1]
    draw.text((x - w / 2, y - h / 2 - 2), text, font=font, fill=BLACK)


def line(x1, y1, x2, y2):
    draw.line((x1, y1, x2, y2), fill=BLACK, width=3)


def entity(x, y, w, h, text):
    draw.rectangle((x, y, x + w, y + h), outline=BLACK, width=3)
    ctext(x + w / 2, y + h / 2, text, entity_font)


def attr(cx, cy, rx, ry, text):
    draw.ellipse((cx - rx, cy - ry, cx + rx, cy + ry), outline=BLACK, width=3)
    ctext(cx, cy, text, attr_font)


def relation(cx, cy, w, h, text):
    pts = [(cx, cy - h // 2), (cx + w // 2, cy), (cx, cy + h // 2), (cx - w // 2, cy)]
    draw.polygon(pts, outline=BLACK, fill=WHITE, width=3)
    ctext(cx, cy, text, relation_font)


def card(x, y, text):
    ctext(x, y, text, card_font)


ctext(WIDTH / 2, 45, "智能仓库管理系统总 E-R 图", title_font)

# 用户
entity(1110, 120, 280, 110, "用户")
attr(1250, 45, 125, 48, "用户ID")
line(1250, 92, 1250, 120)
attr(1015, 105, 125, 48, "用户名")
line(1090, 130, 1110, 155)
attr(1485, 105, 125, 48, "密码")
line(1410, 130, 1390, 155)
attr(1015, 245, 115, 48, "昵称")
line(1090, 220, 1110, 195)
attr(1485, 245, 115, 48, "角色")
line(1410, 220, 1390, 195)
attr(1250, 335, 130, 48, "手机号")
line(1250, 230, 1250, 287)
ctext(1250, 410, "注：用户实体在当前数据库中未与业务表建立外键联系", note_font)

# 供应商
entity(160, 710, 300, 110, "供应商")
attr(310, 605, 120, 48, "供应商ID")
line(310, 653, 310, 710)
attr(105, 710, 135, 48, "供应商编码")
line(160, 765, 240, 765)
attr(120, 860, 135, 48, "供应商名称")
line(170, 820, 235, 835)
attr(310, 945, 115, 48, "联系人")
line(310, 820, 310, 897)
attr(540, 885, 130, 48, "联系电话")
line(460, 810, 495, 848)
attr(600, 710, 110, 48, "地址")
line(460, 765, 490, 765)

# 物料
entity(1090, 710, 320, 110, "物料")
attr(1250, 605, 120, 48, "物料ID")
line(1250, 653, 1250, 710)
attr(1010, 655, 130, 48, "物料编码")
line(1090, 740, 1065, 700)
attr(920, 820, 130, 48, "物料名称")
line(1090, 770, 1045, 795)
attr(1030, 965, 120, 48, "物料分类")
line(1155, 820, 1085, 922)
attr(1180, 1040, 95, 48, "单位")
line(1220, 820, 1190, 993)
attr(1335, 1040, 130, 48, "预警库存")
line(1285, 820, 1315, 993)
attr(1495, 965, 130, 48, "当前库存")
line(1345, 820, 1420, 922)
attr(1590, 820, 100, 48, "备注")
line(1410, 770, 1490, 795)

# 仓位
entity(2070, 710, 300, 110, "仓位")
attr(2220, 605, 120, 48, "仓位ID")
line(2220, 653, 2220, 710)
attr(2430, 710, 130, 48, "仓位编码")
line(2370, 765, 2430, 765)
attr(2445, 860, 130, 48, "仓位名称")
line(2360, 820, 2400, 835)
attr(2220, 1040, 95, 48, "容量")
line(2220, 820, 2220, 992)
attr(2005, 965, 105, 48, "状态")
line(2135, 820, 2060, 922)
attr(1920, 820, 100, 48, "备注")
line(2070, 770, 2020, 795)
attr(2010, 605, 130, 48, "所属仓库")
line(2110, 710, 2055, 648)

# 入库记录
entity(520, 1425, 320, 110, "入库记录")
attr(680, 1325, 120, 48, "入库ID")
line(680, 1373, 680, 1425)
attr(470, 1420, 130, 48, "入库单号")
line(520, 1475, 545, 1475)
attr(430, 1560, 100, 48, "数量")
line(520, 1515, 495, 1535)
attr(615, 1660, 115, 48, "操作人")
line(620, 1535, 620, 1612)
attr(790, 1560, 115, 48, "备注")
line(760, 1535, 760, 1535)
attr(895, 1420, 130, 48, "入库时间")
line(840, 1475, 895, 1475)

# 出库记录
entity(1560, 1425, 320, 110, "出库记录")
attr(1720, 1325, 120, 48, "出库ID")
line(1720, 1373, 1720, 1425)
attr(1510, 1420, 130, 48, "出库单号")
line(1560, 1475, 1585, 1475)
attr(1470, 1560, 100, 48, "数量")
line(1560, 1515, 1535, 1535)
attr(1655, 1660, 115, 48, "操作人")
line(1660, 1535, 1660, 1612)
attr(1830, 1560, 115, 48, "备注")
line(1800, 1535, 1800, 1535)
attr(1935, 1420, 130, 48, "出库时间")
line(1880, 1475, 1935, 1475)

# 关系
relation(760, 765, 190, 150, "供应")
line(460, 765, 665, 765)
line(855, 765, 1090, 765)
card(585, 740, "1")
card(975, 740, "N")

relation(980, 1135, 190, 150, "对应物料")
line(1140, 820, 1045, 1060)
line(780, 1425, 915, 1210)
card(1105, 945, "1")
card(855, 1325, "N")

relation(1520, 1135, 190, 150, "对应物料")
line(1360, 820, 1455, 1060)
line(1660, 1425, 1585, 1210)
card(1395, 945, "1")
card(1635, 1325, "N")

relation(1280, 1345, 170, 130, "入库至")
line(840, 1475, 1195, 1370)
line(2070, 820, 1365, 1300)
card(1035, 1400, "N")
card(1730, 1045, "1")

relation(2010, 1345, 170, 130, "出库自")
line(1880, 1475, 1925, 1370)
line(2220, 820, 2095, 1280)
card(1910, 1400, "N")
card(2175, 1045, "1")

img.save("E:/bishe/基于SpringBoot与Vue的智能仓库管理系统设计与实现/picture/系统总E-R图_论文风格.png")

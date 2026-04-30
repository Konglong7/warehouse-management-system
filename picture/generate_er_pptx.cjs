const path = require("path");
const PptxGenJS = require("pptxgenjs");

const pptx = new PptxGenJS();
pptx.layout = "LAYOUT_WIDE";
pptx.author = "OpenAI Codex";
pptx.company = "OpenAI";
pptx.subject = "智能仓库管理系统陈氏E-R图";
pptx.title = "智能仓库管理系统整体陈氏E-R图";
pptx.lang = "zh-CN";

const slide = pptx.addSlide();
slide.background = { color: "FFFFFF" };

const blackLine = { color: "000000", pt: 1.6 };
const noFill = { color: "FFFFFF", transparency: 100 };
const entityText = {
  fontFace: "Microsoft YaHei",
  color: "000000",
  bold: true,
  fontSize: 21,
  align: "center",
  valign: "mid",
  margin: 0
};
const relationText = {
  fontFace: "Microsoft YaHei",
  color: "000000",
  bold: true,
  fontSize: 18,
  align: "center",
  valign: "mid",
  margin: 0
};
const noteText = {
  fontFace: "Microsoft YaHei",
  color: "000000",
  fontSize: 12,
  align: "center",
  valign: "mid",
  margin: 0
};
const titleText = {
  fontFace: "Microsoft YaHei",
  color: "000000",
  bold: true,
  fontSize: 18,
  align: "center",
  valign: "mid",
  margin: 0
};
const cardText = {
  fontFace: "Microsoft YaHei",
  color: "000000",
  bold: true,
  fontSize: 16,
  align: "center",
  valign: "mid",
  margin: 0
};

slide.addText("智能仓库管理系统整体陈氏 E-R 图", {
  x: 2.6, y: 0.2, w: 8.1, h: 0.4,
  ...titleText
});

function addEntity(x, y, w, h, text) {
  slide.addShape(pptx.ShapeType.roundRect, {
    x, y, w, h,
    rectRadius: 0.08,
    line: blackLine,
    fill: { color: "FFFFFF" }
  });
  slide.addText(text, { x, y, w, h, ...entityText });
}

function addRelation(x, y, w, h, text) {
  slide.addShape(pptx.ShapeType.diamond, {
    x, y, w, h,
    line: blackLine,
    fill: { color: "FFFFFF" }
  });
  slide.addText(text, { x, y, w, h, ...relationText });
}

function addLine(x1, y1, x2, y2) {
  slide.addShape(pptx.ShapeType.line, {
    x: x1,
    y: y1,
    w: x2 - x1,
    h: y2 - y1,
    line: blackLine
  });
}

function addCard(x, y, text) {
  slide.addText(text, { x, y, w: 0.28, h: 0.22, ...cardText });
}

// 实体
addEntity(0.7, 1.0, 1.75, 0.72, "用户");
addEntity(0.6, 3.5, 1.9, 0.78, "供应商");
addEntity(4.35, 3.5, 1.9, 0.78, "物料");
addEntity(7.95, 3.5, 1.9, 0.78, "仓位");
addEntity(2.0, 5.95, 1.95, 0.78, "入库记录");
addEntity(6.45, 5.95, 1.95, 0.78, "出库记录");

// 关系
addRelation(2.95, 3.34, 1.15, 1.1, "供应");
addRelation(3.35, 4.95, 1.1, 1.0, "对应");
addRelation(5.6, 4.95, 1.1, 1.0, "对应");
addRelation(1.2, 4.95, 1.1, 1.0, "存放");
addRelation(7.75, 4.95, 1.1, 1.0, "存放");

// 供应商 -> 供应 -> 物料
addLine(2.5, 3.89, 2.95, 3.89);
addLine(4.1, 3.89, 4.35, 3.89);
addCard(2.72, 3.56, "1");
addCard(4.13, 3.56, "N");

// 物料 -> 对应(入库)
addLine(5.0, 4.28, 4.75, 4.95);
// 入库记录 -> 对应(入库)
addLine(3.05, 5.95, 3.9, 5.95);
addLine(3.9, 5.95, 3.9, 5.72);
addCard(4.76, 4.72, "1");
addCard(3.72, 5.72, "N");

// 仓位 -> 对应(出库)
addLine(8.9, 4.28, 6.7, 5.15);
addCard(8.68, 4.72, "1");
addCard(6.78, 5.0, "N");

// 出库记录 -> 对应(出库)
addLine(6.95, 5.95, 6.15, 5.95);
addLine(6.15, 5.95, 6.15, 5.72);
addCard(6.06, 5.72, "N");

// 入库记录 -> 存放 -> 仓位
addLine(2.4, 5.95, 1.75, 5.95);
addLine(1.75, 5.95, 1.75, 5.45);
addLine(2.3, 5.45, 2.3, 5.22);
addLine(8.9, 3.89, 8.3, 3.89);
addLine(8.3, 3.89, 8.3, 5.22);
addCard(1.56, 5.7, "N");
addCard(8.08, 4.95, "1");

// 出库记录 -> 存放 -> 仓位
addLine(8.4, 5.95, 8.85, 5.95);
addLine(8.85, 5.95, 8.85, 5.45);
addLine(8.3, 5.45, 8.3, 5.22);
addCard(8.48, 5.7, "N");
addCard(8.94, 4.95, "1");

// 用户说明
slide.addText("注：当前数据库中，用户实体独立存在，未与业务表建立外键联系。", {
  x: 0.4, y: 7.05, w: 9.6, h: 0.3,
  ...noteText
});

const output = path.resolve(__dirname, "系统E-R图_可拖拽版.pptx");
pptx.writeFile({ fileName: output });

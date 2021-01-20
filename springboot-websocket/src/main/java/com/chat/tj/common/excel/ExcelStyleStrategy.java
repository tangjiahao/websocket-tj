package com.chat.tj.common.excel;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.style.AbstractCellStyleStrategy;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

/**
 * 通用的excel 格式
 *
 * @author tangjing
 * @date 2021/01/04 12:21
 */
public class ExcelStyleStrategy extends AbstractCellStyleStrategy {
    private XSSFCellStyle DEFAULT_CELL_STYLE = null;

    private XSSFCellStyle DEFAULT_CONTENT_CELL_STYLE = null;

    @Override
    protected void initCellStyle(Workbook workbook) {
        DEFAULT_CELL_STYLE = (XSSFCellStyle) workbook.createCellStyle();
        DEFAULT_CONTENT_CELL_STYLE = (XSSFCellStyle) workbook.createCellStyle();


        // 设置默认背景颜色
        XSSFColor defaultColor = new XSSFColor();
        byte[] byd = {(byte) 196, (byte) 223, (byte) 235};
        defaultColor.setRGB(byd);
        DEFAULT_CELL_STYLE.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        DEFAULT_CELL_STYLE.setFillForegroundColor(defaultColor);

        // XSSFColor color = new XSSFColor();
        // byte[] by = {(byte) 20, (byte) 46, (byte) 86};
        // color.setRGB(by);
        // XSSFColor colorB = new XSSFColor();
        // byte[] byb = {(byte) 196, (byte) 223, (byte) 235};
        // colorB.setRGB(byb);
        // 设置表头和内容的背景颜色
        // DEFAULT_CELL_STYLE.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // DEFAULT_CELL_STYLE.setFillForegroundColor(color);
        // DEFAULT_CONTENT_CELL_STYLE.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // DEFAULT_CONTENT_CELL_STYLE.setFillForegroundColor(colorB);

        // 设置字体
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontName("楷体");
        // font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        font.setFontHeightInPoints((short) 12);
        DEFAULT_CELL_STYLE.setFont(font);

        Font contentFont = workbook.createFont();
        contentFont.setFontName("楷体");
        contentFont.setFontHeightInPoints((short) 12);
        DEFAULT_CONTENT_CELL_STYLE.setFont(contentFont);


        XSSFColor whiteColor = new XSSFColor();
        byte[] byw = {(byte) 255, (byte) 255, (byte) 255};
        whiteColor.setRGB(byw);


        // 左右 居中
        DEFAULT_CELL_STYLE.setAlignment(HorizontalAlignment.CENTER);
        // 上下 居中v
        DEFAULT_CELL_STYLE.setVerticalAlignment(VerticalAlignment.CENTER);
        // 下边框
        DEFAULT_CELL_STYLE.setBorderBottom(BorderStyle.THIN);
        // 左边框
        DEFAULT_CELL_STYLE.setBorderLeft(BorderStyle.THIN);
        // 上边框
        DEFAULT_CELL_STYLE.setBorderTop(BorderStyle.THIN);
        // 右边框
        DEFAULT_CELL_STYLE.setBorderRight(BorderStyle.THIN);

        DEFAULT_CELL_STYLE.setRightBorderColor(whiteColor);
        DEFAULT_CELL_STYLE.setLeftBorderColor(whiteColor);


//        // 自动换行
//        DEFAULT_CELL_STYLE.setWrapText( true );


        DEFAULT_CONTENT_CELL_STYLE.setAlignment(HorizontalAlignment.CENTER);
        DEFAULT_CONTENT_CELL_STYLE.setBorderBottom(BorderStyle.THIN);
        // 左边框
        DEFAULT_CONTENT_CELL_STYLE.setBorderLeft(BorderStyle.THIN);
        // 上边框
        DEFAULT_CONTENT_CELL_STYLE.setBorderTop(BorderStyle.THIN);
        // 右边框
        DEFAULT_CONTENT_CELL_STYLE.setBorderRight(BorderStyle.THIN);
        DEFAULT_CONTENT_CELL_STYLE.setRightBorderColor(whiteColor);
        DEFAULT_CONTENT_CELL_STYLE.setBottomBorderColor(whiteColor);
        DEFAULT_CONTENT_CELL_STYLE.setLeftBorderColor(whiteColor);
        DEFAULT_CONTENT_CELL_STYLE.setTopBorderColor(whiteColor);

    }

    @Override
    protected void setHeadCellStyle(Cell cell, Head head, Integer integer) {
        cell.setCellStyle(DEFAULT_CELL_STYLE);
    }

    @Override
    protected void setContentCellStyle(Cell cell, Head head, Integer integer) {
        cell.setCellStyle(DEFAULT_CONTENT_CELL_STYLE);
    }
}

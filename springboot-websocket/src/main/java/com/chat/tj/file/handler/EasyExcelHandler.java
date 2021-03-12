package com.chat.tj.file.handler;

import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.util.IoUtils;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 导出图片实现的handler
 *
 * @author hepeng
 * @date 2021/2/24 10:37
 */
public class EasyExcelHandler implements CellWriteHandler {

    private Integer wight;
    private Integer len;
    private Drawing drawing;

    public EasyExcelHandler(int len, int wight) {

        this.len = len;
        this.wight = wight;

    }


    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer integer, Integer integer1, Boolean aBoolean) {

    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }

    @Override
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, CellData cellData, Cell cell, Head head, Integer integer, Boolean aBoolean) {
        if (aBoolean) {
            return;
        }
        if (cellData.getImageValue() != null || cellData.getData() instanceof ArrayList) {
            cellData.setType(CellDataTypeEnum.EMPTY);
        }
    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> list, Cell cell, Head head, Integer integer, Boolean aBoolean) {
        if (aBoolean) {
            return;
        }
        Sheet sheet = cell.getSheet();
        CellData cellData = list.get(0);
        byte[] image = cellData.getImageValue();
        this.insertImage(sheet, image, len, wight);

    }

    /**
     * 导出图片
     *
     * @param sheet sheet
     * @param field 图片
     * @param len   图片长几个excel行高
     * @param wight 图片占几个excel列宽
     */
    private void insertImage(Sheet sheet, byte[] field, int len, int wight) {
        int index = sheet.getWorkbook().addPicture(field, HSSFWorkbook.PICTURE_TYPE_PNG);
        if (drawing == null) {
            drawing = sheet.createDrawingPatriarch();
        }
        CreationHelper helper = sheet.getWorkbook().getCreationHelper();
        ClientAnchor anchor = helper.createClientAnchor();
        // 设置图片坐标
        anchor.setDx1(0);
        anchor.setDx2(0);
        anchor.setDy1(0);
        anchor.setDy2(0);

        //设置图片位置
        anchor.setCol1(0);
        anchor.setRow1(0);
        anchor.setCol2(wight);
        anchor.setRow2(len);

//        // 设置图片可以随着单元格移动
        anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
        drawing.createPicture(anchor, index);
    }

    private byte[] getImage(String field) {
        InputStream inputStream = null;
        byte[] bytes;
        try {
            URL url = new URL(field);
            inputStream = url.openStream();
            bytes = IoUtils.toByteArray(inputStream);
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


}

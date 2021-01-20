package com.chat.tj.Test;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.net.URL;

/**
 * @author tangjing
 * @date 2021/01/20 14:13
 */
@Data
@ContentRowHeight(100)
@ColumnWidth(100 / 8)
@ApiModel(value = "ExcelImgExport", description = "图片导出对象")
public class ExcelImgExport {

    @ExcelProperty("图片名称")
    @ApiModelProperty("图片名称")
    private String name;

    @ExcelProperty(value = {"url图片"})
    @ApiModelProperty("图片")
    private URL url;

    @ExcelProperty(value = {"String图片"}, converter = MyStringImageConverter.class)
    @ApiModelProperty("图片")
    private String string;
}

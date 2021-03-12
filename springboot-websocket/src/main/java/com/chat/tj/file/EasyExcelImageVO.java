package com.chat.tj.file;


import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author hepeng
 * @date 2021/2/19 10:53
 */
@Data
@ApiModel("图片base64的byte集合")
public class EasyExcelImageVO {
    @ExcelProperty(value = "")
    private byte[] name;
}

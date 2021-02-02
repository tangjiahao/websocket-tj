package com.chat.tj.Test;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.StringUtils;
import lombok.Data;

import java.io.IOException;
import java.util.Base64;

/**
 * 字符串图片转换工具
 *
 * @author tangjing
 * @date 2021/01/20 14:16
 */
@Data
public class MyStringImageConverter implements Converter<String> {
    @Override
    public Class supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.IMAGE;
    }

    @Override
    public String convertToJavaData(CellData cellData, ExcelContentProperty contentProperty,
                                    GlobalConfiguration globalConfiguration) {
        throw new UnsupportedOperationException("Cannot convert images to string");
    }

    //图片失效处理
    @Override
    public CellData convertToExcelData(String value, ExcelContentProperty contentProperty,
                                       GlobalConfiguration globalConfiguration) throws IOException {

        if (!StringUtils.isEmpty(value)) {
            // 将base64字符串解码
            byte[] decode = Base64.getDecoder().decode(value);
            return new CellData(decode);
        }

        //直接返回文字描述
        //FileUtils.readFileToByteArray(new File("/home/test.jpg"))
        return new CellData("无法加载图片");
    }

}

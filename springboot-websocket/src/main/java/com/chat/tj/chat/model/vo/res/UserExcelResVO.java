package com.chat.tj.chat.model.vo.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author tangjing
 * @date 2021/02/24 09:49
 */
@Data
public class UserExcelResVO {

    // @ExcelProperty("用户id")
    @ApiModelProperty("账号")
    private String userId;

    // @ExcelProperty("用户名")
    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty(value = "pwd")
    private String pwd;
}

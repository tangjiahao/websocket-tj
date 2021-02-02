package com.chat.tj.chat.model.vo.res;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description :
 * @Author TangJing
 * @Date 2020/12/20 12:43
 * @Version 1.0
 */
@Data
public class UserResVO {

    @ExcelProperty("用户id")
    @ApiModelProperty("用户id")
    private Integer userId;

    @ExcelProperty("用户名")
    @ApiModelProperty("用户名")
    private String userName;

    // @ExcelIgnore
    // @ApiModelProperty("创建时间")
    // private String createTime;

    @ApiModelProperty("是否是好友，true表示是好友，false表示不是好友")
    private Boolean friend;

}

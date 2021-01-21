package com.chat.tj.model.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 查询消息记录请求
 *
 * @author tangjing
 * @date 2021/01/21 13:43
 */
@Data
public class MessageRecordReqVO {

    @ApiModelProperty("用户一")
    @NotBlank(message = "用户1不能为空")
    private String nameOne;

    @ApiModelProperty("用户二，如果是查询群组，传All")
    @NotBlank(message = "用户2不能为空")
    private String nameTwo;

    @ApiModelProperty("频道号,查好友传-1，群组传群组号")
    @NotBlank(message = "频道号不能为空")
    private String roomId;

    @ApiModelProperty("查询记录条数，不传默认查询10条 1 查询所有信息")
    private Integer recordNum;

}

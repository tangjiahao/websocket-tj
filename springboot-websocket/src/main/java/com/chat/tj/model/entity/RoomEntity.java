package com.chat.tj.model.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description :
 * @Author TangJing
 * @Date 2020/12/20 11:01
 * @Version 1.0
 */
@Data

public class RoomEntity {

    @ApiModelProperty("频道号")
    private String roomId;

    @ApiModelProperty("房间名称")
    private String roomName;

}

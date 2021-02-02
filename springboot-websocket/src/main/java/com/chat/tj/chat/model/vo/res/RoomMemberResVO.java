package com.chat.tj.chat.model.vo.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author tangjing
 * @date 2020/12/30 14:53
 */
@Data
public class RoomMemberResVO extends UserResVO{

    @ApiModelProperty("成员类型,1群主，2管理员 3普通群成员")
    private Integer type;
}

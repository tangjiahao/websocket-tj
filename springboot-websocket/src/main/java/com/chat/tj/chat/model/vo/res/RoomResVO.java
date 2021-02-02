package com.chat.tj.chat.model.vo.res;

import com.chat.tj.chat.model.entity.RoomEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author tangjing
 * @date 2021/01/26 17:09
 */
@Data
public class RoomResVO extends RoomEntity {

    @ApiModelProperty("是否已加入")
    private Boolean memeber;
}

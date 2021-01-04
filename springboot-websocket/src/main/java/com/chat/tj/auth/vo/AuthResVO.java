package com.chat.tj.auth.vo;

import com.chat.tj.model.vo.res.UserResVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author tangjing
 * @date 2021/01/04 15:04
 */
@Data
public class AuthResVO {

    @ApiModelProperty("用户基础信息")
    private UserResVO userResVO;

    @ApiModelProperty("用户token(有效期为一周)")
    private String token;
}

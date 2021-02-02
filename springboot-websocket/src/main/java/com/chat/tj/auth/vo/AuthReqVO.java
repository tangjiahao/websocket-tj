package com.chat.tj.auth.vo;

import com.chat.tj.chat.model.vo.req.UserReqVO;
import io.swagger.annotations.Api;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author tangjing
 * @date 2021/01/05 09:57
 */
@Data
@Api("获取token请求")
public class AuthReqVO {

    @Valid
    private UserReqVO reqVO;

    @NotBlank(message = "访问密钥不能为空")
    private String accessKeyId;
}

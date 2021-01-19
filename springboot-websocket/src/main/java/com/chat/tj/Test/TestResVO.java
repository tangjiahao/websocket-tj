package com.chat.tj.Test;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author tangjing
 * @date 2021/01/19 16:52
 */
@Data
public class TestResVO {

    public String tryace;
    @ApiModelProperty("用户id")
    private Integer userId;
    @ApiModelProperty("用户名")
    private String userName;

    private void testReflect(int a) {
        System.out.println("测试反射机制的私有方法" + a);
    }
}

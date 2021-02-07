package com.chat.tj.chat.model.vo.res;

import com.chat.tj.chat.model.entity.UserEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户详细信息
 *
 * @author tangjing
 * @date 2021/02/07 10:48
 */
@EqualsAndHashCode(callSuper = true)// 在比较对象时会考虑父类和子类的所有属性
@Data
public class UserDetailResVO extends UserEntity {
    @ApiModelProperty("角色id")
    private Integer roleId;
}

package com.chat.tj.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chat.tj.chat.model.vo.res.UserResVO;
import com.chat.tj.common.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tangjing
 * @date 2021/02/05 14:51
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RedisTest {
    @Autowired
    private RedisUtil redisUtil;

    @Test
    void get() {
        System.out.println("开始get测试");
        // Object obj = redisUtil.get("userId");
        // UserResVO resVO = coverterObject(obj,UserResVO.class);
        // if(resVO!=null){
        //     System.out.println(resVO.toString());
        // }
        List<Object> temp = redisUtil.lGet("userinfo", 0, -1);
        List<UserResVO> result = coverterList(temp, UserResVO.class);
        if (!CollectionUtils.isEmpty(result)) {
            result.forEach(s -> System.out.println(s.toString()));
        }

    }

    @Test
    void set() {
        System.out.println("开始set测试");
        UserResVO userResVO = new UserResVO();
        userResVO.setUserId(1);
        userResVO.setUserName("测试");
        userResVO.setFriend(false);
        UserResVO resVO = new UserResVO();
        resVO.setUserId(2);
        resVO.setUserName("测试2");
        resVO.setFriend(true);
        List<UserResVO> voList = new ArrayList<>();
        voList.add(userResVO);
        voList.add(resVO);
        redisUtil.lSet("userinfo", voList);
        System.out.println("设置完毕");
    }

    public <T> T coverterObject(Object object, Class<T> tClass) {
        if (object == null) {
            return null;
        }
        JSONObject temp = JSONObject.parseObject(object.toString());
        return JSONObject.toJavaObject(temp, tClass);
    }

    public <T> List<T> coverterList(List<Object> list, Class<T> tClass) {
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        String json = JSON.toJSONString(list.get(0));
        return JSON.parseArray(json, tClass);
    }
}

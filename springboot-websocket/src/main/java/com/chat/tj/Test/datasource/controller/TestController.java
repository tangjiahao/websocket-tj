package com.chat.tj.Test.datasource.controller;


import com.chat.tj.Test.datasource.dao.db2.Mapper2;
import com.chat.tj.Test.datasource.vo.ResponseVo;
import com.chat.tj.chat.dao.UserMapper;
import com.chat.tj.chat.model.vo.res.UserResVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tangjing
 * @date 2021/03/11 14:34
 */
@RestController
@RequestMapping("api/test")
@Api("数据源测试接口")
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private UserMapper mapper1;

    @Autowired
    private Mapper2 mapper2;

    @GetMapping("/ace")
    @ApiOperation(value = "查询")
    public ResponseVo<String> getUserRooms() {
        List<String> result = mapper1.getRoomMemberList(1).stream().map(UserResVO::getUserName).collect(Collectors.toList());
        log.info("这是数据源1查出的数据：" + result.toString());
        List<String> result2 = mapper2.getinterfaceName();
        log.info("这是数据源2查出的数据：" + result2.toString());
        String s = result.toString() + result2.toString();
        return ResponseVo.content(s);
    }
}

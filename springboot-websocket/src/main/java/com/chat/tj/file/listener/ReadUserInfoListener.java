package com.chat.tj.file.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.chat.tj.chat.dao.UserMapper;
import com.chat.tj.chat.model.vo.res.UserExcelResVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * @author tangjing
 * @date 2021/02/24 09:44
 */
@Slf4j
public class ReadUserInfoListener extends AnalysisEventListener<UserExcelResVO> {

    /**
     * 存储用户信息，用于写入数据库
     */
    List<UserExcelResVO> list = new ArrayList<>();

    /**
     * 存储账号信息，用来验证xls中账号是否重复
     */
    HashSet<String> dataSet = new HashSet<>();

    /**
     * spring的方式需要通过构造函数传进来
     */
    private UserMapper userMapper;

    private String adminUser;

    public ReadUserInfoListener(UserMapper userMapper, String adminUser) {
        this.userMapper = userMapper;
        this.adminUser = adminUser;
    }

    /**
     * 验证是否符合的账号/密码格式
     *
     * @param target 目标字符串
     * @return 是否匹配
     */
    private static boolean isMatchString(String target) {
        Pattern p = compile("^[a-zA-Z0-9_]+$");
        Matcher m = p.matcher(target);
        return m.matches();
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param userExcelResVO one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(UserExcelResVO userExcelResVO, AnalysisContext context) {
        // 验证表头行
        Integer rowIndex = context.readRowHolder().getRowIndex();
        if (rowIndex == 0) {
            boolean okTemplate = true;
            if (!"账号".equals(userExcelResVO.getUserId())) {
                okTemplate = false;
            } else if (!"姓名".equals(userExcelResVO.getUserName())) {
                okTemplate = false;
            } else if (!"密码".equals(userExcelResVO.getPwd())) {
                okTemplate = false;
            }
            if (!okTemplate) {
                throw new RuntimeException("模板不正确");
            }
        } else {
            // 校验userId
            StringBuilder errorMsg = new StringBuilder();
            if (StringUtils.isEmpty(userExcelResVO.getUserId())) {
                errorMsg.append("账号不能为空");
            }
            // 校验姓名
            if (StringUtils.isEmpty(userExcelResVO.getUserName())) {
                errorMsg.append("姓名不能为空;");
            }
            // 验证密码
            if (StringUtils.isEmpty(userExcelResVO.getPwd())) {
                errorMsg.append("密码不能为空");
            } else if (!isMatchString(userExcelResVO.getPwd())) {
                errorMsg.append("密码不满足字母,数字,下划线规则,错误密码:").append(userExcelResVO.getPwd()).append(",对应用户为").append(userExcelResVO.getUserName());
            }
            //其它验证 验证xls表格中是否有重复的账号
            if (errorMsg.length() == 0) {
                if (!dataSet.add(userExcelResVO.getUserName())) {
                    errorMsg.append("账号【");
                    errorMsg.append(userExcelResVO.getUserName());
                    errorMsg.append("】在xls表中重复;");
                }
            }
            if (errorMsg.length() > 0) {
                errorMsg.insert(0, "第" + (rowIndex + 1) + "行:");
                throw new RuntimeException(errorMsg.toString());
            }
            list.add(userExcelResVO);
        }
        log.debug(userExcelResVO.toString());
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("xlsx所有数据解析完成！");
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        try {
            saveData();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("xlsx表格数据入库失败,表格中有数据已存在于数据库，请检查:" + e.getCause());
        }
    }

    private void saveData() throws Exception {
        log.info("{}条数据，开始存储数据库！", list.size());
        if (list.size() < 1) {
            throw new RuntimeException("xlsx表格中无数据，请编辑后再上传");
        }
        //入库
        userMapper.batchInsertUser(list);
        log.info("存储数据库成功！");
    }
}

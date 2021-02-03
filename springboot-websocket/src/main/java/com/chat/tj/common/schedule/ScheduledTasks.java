package com.chat.tj.common.schedule;


import com.chat.tj.chat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;

/**
 * 每隔一周定期清理上传文件
 *
 * @author tangjing
 * @date 2019/10/21 9:34
 */
@Slf4j
@Configuration
@EnableScheduling
public class ScheduledTasks {

    /**
     * 文件路径
     */
    @Value("${file.path}")
    private String dirPath;

    @Autowired
    private UserService userService;

    @Scheduled(cron = "0 0 23 ? * SUN")
    //@Scheduled(cron = "0/10 * * * * *")
    public void cleanFile() {
        log.info("每周日晚上23点执行一次定时任务，开始清理服务器文件...");
        File file = new File(dirPath);
        //将file子目录及子文件放进文件数组
        File[] files = file.listFiles();
        //如果包含文件进行删除操作
        if (files != null) {
            for (File value : files) {
                if (value.isFile()) {
                    //删除子文件
                    value.delete();
                }
            }
        }
        log.info("清理结束。");
    }

    @Scheduled(cron = "0 0 23 0/10 * ?")
    // @Scheduled(cron = "0/10 * * * * *")
    public void cleanRecord() {
        log.info("每10天执行一次定时任务，开始清理聊天记录...");
        userService.deleteRecord();
        log.info("清理结束。");
    }


}
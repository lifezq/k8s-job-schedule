package com.lifezq.schedule.tools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Package com.lifezq.schedule.tools
 * @ClassName Tool
 * @Description TODO
 * @Author ryan
 * @Date 2022/1/1
 */
@Component
public class Tool {
    private static final Logger logger = LogManager.getLogger(Tool.class);

    @Async("taskExecutor")
    public void asyncExecutor() {
        logger.info("thread[{}] is running...", Thread.currentThread().getId());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            logger.info("thread[{}] is exited...", Thread.currentThread().getId());
        }
    }
}

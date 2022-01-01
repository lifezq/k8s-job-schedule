package com.lifezq.schedule;

import com.lifezq.schedule.config.ThreadPoolTaskConfigure;
import com.lifezq.schedule.tools.Tool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = {Tool.class, ThreadPoolTaskConfigure.class})
class K8sClientApplicationTests {
    @Autowired
    private Tool tool;

    private static final Logger logger = LogManager.getLogger(K8sClientApplicationTests.class);

    @Test
    void contextLoads() {
    }

    @Test
    public void threadTest() {
        for (int i = 0; i < 10; i++) {
            tool.asyncExecutor();
        }
    }
}

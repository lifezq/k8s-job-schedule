package com.lifezq.schedule.controller;

import com.lifezq.schedule.base.Response;
import com.lifezq.schedule.bo.params.ScheduleJobRequest;
import com.lifezq.schedule.template.TemplateJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @Package com.lifezq.schedule.controller
 * @ClassName ScheduleTask
 * @Description TODO
 * @Author ryan
 * @Date 2021/12/24
 */
@RequestMapping("/v1/schedule")
@RestController
public class ScheduleController {
    private Logger logger = LoggerFactory.getLogger(ScheduleController.class);

    @PostMapping("/job")
    public Response job(@RequestBody ScheduleJobRequest req) {
        Response.ResponseBuilder response = Response.builder().data(req.getJobName() + ":" + req.getImage());

        try {
            String templateContent = TemplateJob.setTemplateContentForJob(1, req.getImage(), req.getJobName());
            logger.info("template content:{}", templateContent);
        } catch (IOException e) {
            response.returnCode(HttpStatus.BAD_REQUEST.value());
            response.returnMsg("get template content for job failed");
            response.returnUserMsg("get template content for job failed");
        }
        return response.build();
    }
}

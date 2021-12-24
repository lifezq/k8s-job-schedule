package com.lifezq.schedule.controller;

import com.lifezq.schedule.base.Response;
import com.lifezq.schedule.bo.params.ScheduleJobRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/job")
    public Response job(@RequestBody ScheduleJobRequest req) {
        Response.ResponseBuilder response = Response.builder().data(req.getJobName() + ":" + req.getImage());
        return response.build();
    }
}

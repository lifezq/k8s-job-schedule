package com.lifezq.schedule.controller;

import com.lifezq.schedule.base.Response;
import com.lifezq.schedule.bo.params.ScheduleJobRequest;
import com.lifezq.schedule.bo.params.ScheduleStatRequest;
import com.lifezq.schedule.template.TemplateJob;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

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
    private long jobId;
    private Logger logger = LogManager.getLogger(ScheduleController.class);

    @Autowired
    private RedisTemplate redisTemplate;

    ScheduleController() {

        try {

            String lastJobIdLabel = ".last.job.id";
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("system exit jobid:{}", this.jobId);
                File file = new File(lastJobIdLabel);
                if (file.exists()) {
                    try (FileOutputStream fo = new FileOutputStream(file, false)) {
                        fo.write(Long.toString(this.jobId).getBytes(StandardCharsets.UTF_8));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }));

            File file = new File(lastJobIdLabel);
            if (!file.exists()) {
                if (file.createNewFile()) {
                    try (FileOutputStream fo = new FileOutputStream(file, false)) {
                        this.jobId = 1;
                        fo.write(1);
                    }
                }
                return;
            }

            try (FileInputStream fi = new FileInputStream(file)) {
                this.jobId = Long.parseLong(new String(fi.readAllBytes(),
                        StandardCharsets.UTF_8)) + 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/job")
    public Response job(@RequestBody ScheduleJobRequest req) {
        Map<String, Long> data = new HashMap<>();
        data.put("job_id", jobId);
        Response.ResponseBuilder response = Response.builder().data(data);

        try {
            String templateYamlFilename = TemplateJob.setTemplateContentForJob(
                    jobId, req.getImage(), req.getMetadata(), req.getResources());
            logger.info("template content:{}", templateYamlFilename);
            jobId++;
            Process process = Runtime.getRuntime().exec(String.format(
                    "kubectl apply --kubeconfig=kube-config-local -f  %s", templateYamlFilename));
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                String outs = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                logger.info("job outs:{}", outs);
            } else {
                response.returnCode(HttpStatus.BAD_REQUEST.value());
                response.returnMsg("failed");
                response.returnUserMsg("failed");
                String outs = new String(process.getErrorStream().readAllBytes(), StandardCharsets.UTF_8);
                logger.error("job error:{}", outs);
            }
        } catch (IOException | InterruptedException e) {
            logger.error("job exception:{}", e.getMessage());
            response.returnCode(HttpStatus.BAD_REQUEST.value());
            response.returnMsg("get template content for job failed");
            response.returnUserMsg("get template content for job failed");
        }
        return response.build();
    }

    @PostMapping("/stat")
    public Response stat(@RequestBody ScheduleStatRequest req) {
        Response.ResponseBuilder response = Response.builder();
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c",
                    "kubectl --kubeconfig=kube-config-local get po|grep k8s-job-schedule-" +
                            req.getJobId() + "|awk '{print $3}'"});
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                String outs = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                logger.info("stat outs:{}", outs);
                response.data(outs.replace("\n", ""));
            } else {
                response.returnCode(HttpStatus.BAD_REQUEST.value());
                response.returnMsg("failed");
                response.returnUserMsg("failed");
                String outs = new String(process.getErrorStream().readAllBytes(), StandardCharsets.UTF_8);
                logger.error("stat error:{}", outs);
            }
        } catch (IOException | InterruptedException e) {
            logger.error("stat exception:{}", e.getMessage());
            response.returnCode(HttpStatus.BAD_REQUEST.value());
            response.returnMsg("failed");
            response.returnUserMsg("failed");
        }
        return response.build();
    }

    @PostMapping("/delete")
    public Response delete(@RequestBody ScheduleStatRequest req) {
        Response.ResponseBuilder response = Response.builder();
        try {
            Process process = Runtime.getRuntime().exec(String.format(
                    "kubectl delete --kubeconfig=kube-config-local -f  %s",
                    TemplateJob.getTemplateYamlFilenameByJobId(req.getJobId())));
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                String outs = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                logger.info("delete outs:{}", outs);
            } else {
                response.returnCode(HttpStatus.BAD_REQUEST.value());
                response.returnMsg("failed");
                response.returnUserMsg("failed");
                String outs = new String(process.getErrorStream().readAllBytes(), StandardCharsets.UTF_8);
                logger.error("delete error:{}", outs);
            }
        } catch (IOException | InterruptedException e) {
            logger.error("delete exception:{}", e.getMessage());
            response.returnCode(HttpStatus.BAD_REQUEST.value());
            response.returnMsg("failed");
            response.returnUserMsg("failed");
        } finally {
            File file = new File(TemplateJob.getTemplateYamlFilenameByJobId(req.getJobId()));
            if (file.exists()) {
                file.delete();
            }
        }
        return response.build();
    }

    @PostMapping("/test")
    public Response test() {
        Response.ResponseBuilder response = Response.builder();

        String key = "k1";
        redisTemplate.opsForValue().set(key, key + "_value", Duration.ofSeconds(3600));
        redisTemplate.expire(key, Duration.ofSeconds(60));
        logger.info("key value:{}--expire:{}", redisTemplate.opsForValue().get(key),
                " expire:" + redisTemplate.getExpire(key));
        return response.build();
    }
}

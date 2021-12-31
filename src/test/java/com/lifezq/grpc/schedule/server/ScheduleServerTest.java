package com.lifezq.grpc.schedule.server;

import com.lifezq.grpc.schedule.common.CommonReponse;
import com.lifezq.grpc.schedule.protogen.JobRequest;
import com.lifezq.grpc.schedule.protogen.Resources;
import com.lifezq.grpc.schedule.protogen.ResourcesLimits;
import com.lifezq.grpc.schedule.protogen.ScheduleGrpc;
import com.lifezq.schedule.K8sJobScheduleApplication;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @Package com.lifezq.grpc.schedule.server
 * @ClassName ScheduleServerTests
 * @Description TODO
 * @Author ryan
 * @Date 2021/12/31
 */
@SpringBootTest(classes = K8sJobScheduleApplication.class)
public class ScheduleServerTest {
    @Value("${grpc.server.port:50001}")
    private int port;

    private static final Logger logger = LogManager.getLogger(ScheduleServerTest.class);
    private ScheduleGrpc.ScheduleBlockingStub blockingStub;

    @PostConstruct
    void preTest() {
        logger.info("BeforeTestMethod...");

        // Access a service running on the local machine on port 50051
        String target = String.format("localhost:%d", port);


        // Create a communication channel to the server, known as a Channel. Channels are thread-safe
        // and reusable. It is common to create channels at the beginning of your application and reuse
        // them until the application shuts down.
        ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build();
        blockingStub = ScheduleGrpc.newBlockingStub(channel);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
    }

    @Test
    void job() {
        logger.info("Test job begin...");

        String jobName = "test-job";
        logger.info("Will try to test job [" + jobName + "] ...");
        JobRequest request = JobRequest.newBuilder().setJobName(jobName).
                setImage("lifezqy/dev-build-python3.6:latest").
                setMetadata("metadata content").setResources(Resources.newBuilder().setLimits(
                ResourcesLimits.newBuilder().setCpu("200m").setMemory("500Mi")).build()).build();
        CommonReponse response;
        try {
            response = blockingStub.job(request);
        } catch (StatusRuntimeException e) {
            logger.error("RPC failed: {}", e.getStatus());
            return;
        }
        logger.info("Schedule job got result: " + response.getData());
    }
}

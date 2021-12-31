package com.lifezq.grpc.schedule.server;

import com.alibaba.fastjson.JSON;
import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.lifezq.grpc.schedule.common.CommonReponse;
import com.lifezq.grpc.schedule.protogen.JobRequest;
import com.lifezq.grpc.schedule.protogen.ScheduleGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Package com.lifezq.grpc.schedule.server
 * @ClassName ScheduleServer
 * @Description TODO
 * @Author EDY
 * @Date 2021/12/31
 */
@Service
public class ScheduleServer {
    @Value("${grpc.server.port:50001}")
    private int port;

    private static final Logger logger = LogManager.getLogger(ScheduleServer.class);

    private Server server;

    /**
     * Main launches the server from the command line.
     */
    @PostConstruct
    public void startServer() {

        try {
            this.start();
        } catch (IOException e) {
            logger.error("Grpc server start failed error:{}", e.getMessage());
            e.printStackTrace();
        }

        new Thread(() -> {
            try {
                this.blockUntilShutdown();
            } catch (InterruptedException e) {
                logger.error("Grpc server exited with error:{}", e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    private void start() throws IOException {
        /* The port on which the server should run */
        server = ServerBuilder.forPort(port)
                .addService(new ScheduleServerImpl())
                .build()
                .start();
        logger.info("Grpc server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                logger.info("*** grpc server shutting down gRPC server since JVM is shutting down");
                try {
                    ScheduleServer.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                logger.info("*** grpc server shut down");
            }
        });
    }

    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }


    static class ScheduleServerImpl extends ScheduleGrpc.ScheduleImplBase {

        @Override
        public void job(JobRequest req, StreamObserver<CommonReponse> responseObserver) {
            logger.info("Grpc server Schedule.job recv:{}", req);
            Map<String, Long> data = new HashMap<>();
            data.put("jobId", 22L);

            CommonReponse reply = CommonReponse.newBuilder().setData(Any.newBuilder().setValue(
                    ByteString.copyFromUtf8(JSON.toJSONString(data))).build()).build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }
    }
}

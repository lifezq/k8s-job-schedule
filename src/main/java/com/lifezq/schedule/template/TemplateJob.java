package com.lifezq.schedule.template;

import com.lifezq.schedule.bo.params.Resources;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Package com.lifezq.schedule.template
 * @ClassName TemplateJob
 * @Description TODO
 * @Author ryan
 * @Date 2021/12/25
 */
public class TemplateJob {
    private static String jobScheduleYamlTpl = "job-schedule-%d.yaml";

    private static String tplJob = "apiVersion: v1\n" +
            "kind: PersistentVolume\n" +
            "metadata:\n" +
            "  name: job-pv-volume-#{[jobId]}\n" +
            "  namespace: default\n" +
            "  labels:\n" +
            "    type: local\n" +
            "spec:\n" +
            "  storageClassName: nfs\n" +
            "  capacity:\n" +
            "    storage: 3Gi\n" +
            "  accessModes:\n" +
            "    - ReadWriteMany\n" +
            "  hostPath:\n" +
            "    path: \"/mnt/data\"\n" +
            "---\n" +
            "apiVersion: v1\n" +
            "kind: PersistentVolumeClaim\n" +
            "metadata:\n" +
            "  name: pvc-job-#{[jobId]}\n" +
            "  namespace: default\n" +
            "spec:\n" +
            "  accessModes:\n" +
            "    - ReadWriteMany\n" +
            "  storageClassName: nfs\n" +
            "  resources:\n" +
            "    requests:\n" +
            "      storage: 3Gi\n" +
            "---\n" +
            "apiVersion: batch/v1\n" +
            "kind: Job\n" +
            "metadata:\n" +
            "  name: k8s-job-schedule-#{[jobId]}\n" +
            "  namespace: default\n" +
            "spec:\n" +
            "  template:\n" +
            "    spec:\n" +
            "      volumes:\n" +
            "        - name: job-pv-storage\n" +
            "          persistentVolumeClaim:\n" +
            "            claimName: pvc-job-#{[jobId]}\n" +
            "      imagePullSecrets:\n" +
            "        - name: harbor\n" +
            "      containers:\n" +
            "        - name: job-processing\n" +
            "          image: #{[image]}\n" +
            "          volumeMounts:\n" +
            "            - name: job-pv-storage\n" +
            "              mountPath: /tmp/job_model\n" +
            "          ports:\n" +
            "            - containerPort: 9991\n" +
            "              name: job-port\n" +
            "          imagePullPolicy: Always\n" +
            "          args:\n" +
            "            - --metadata=#{[metadata]}\n" +
            "          resources:\n" +
            "            requests:\n" +
            "              memory: #{[requestsMemory]}\n" +
            "              cpu: #{[requestsCpu]}\n" +
            "            limits:\n" +
            "              memory: #{[limitsMemory]}\n" +
            "              cpu: #{[limitsCpu]}\n" +
            "      restartPolicy: Never\n" +
            "  backoffLimit: 1";

    public static String setTemplateContentForJob(long jobId, String image, String metadata, Resources resources) throws IOException {
        ExpressionParser parser = new SpelExpressionParser();
        TemplateParserContext parserContext = new TemplateParserContext();
        Map<String, Object> params = new HashMap<>();
        params.put("jobId", jobId);
        params.put("image", image);
        params.put("metadata", metadata);
        params.put("requestsMemory", resources.getRequests().getMemory());
        params.put("requestsCpu", resources.getRequests().getCpu());
        params.put("limitsMemory", resources.getLimits().getMemory());
        params.put("limitsCpu", resources.getLimits().getCpu());

        String jobScheduleYamlFilename = String.format(jobScheduleYamlTpl, jobId);
        BufferedWriter out = new BufferedWriter(new FileWriter(jobScheduleYamlFilename));
        out.write(parser.parseExpression(tplJob, parserContext).getValue(params, String.class));
        out.close();
        return jobScheduleYamlFilename;
    }

    public static String getTemplateYamlFilenameByJobId(long jobId) {
        return String.format(jobScheduleYamlTpl, jobId);
    }
}

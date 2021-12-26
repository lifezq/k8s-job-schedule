package com.lifezq.schedule.bo.params;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @Package com.lifezq.schedule.bo.params
 * @ClassName ScheduleStatRequest
 * @Description TODO
 * @Author ryan
 * @Date 2021/12/26
 */
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleStatRequest {
    @JsonProperty("job_id")
    private long jobId;
}

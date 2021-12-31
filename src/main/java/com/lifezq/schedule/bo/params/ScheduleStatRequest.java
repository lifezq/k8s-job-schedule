package com.lifezq.schedule.bo.params;

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
    private long jobId;
}

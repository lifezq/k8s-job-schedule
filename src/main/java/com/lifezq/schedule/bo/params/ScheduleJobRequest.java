package com.lifezq.schedule.bo.params;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @Package com.lifezq.schedule.bo.params
 * @ClassName ScheduleJobRequest
 * @Description TODO
 * @Author ryan
 * @Date 2021/12/24
 */
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleJobRequest implements Serializable {
    private static final long serialVersionUID = -6814847434324877351L;

    @JsonProperty(value = "job_name")
    private String jobName;
    private String image;
    private String metadata;
    private Resources resources;
}

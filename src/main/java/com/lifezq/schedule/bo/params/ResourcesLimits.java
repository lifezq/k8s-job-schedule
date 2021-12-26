package com.lifezq.schedule.bo.params;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @Package com.lifezq.schedule.bo.params
 * @ClassName ResourcesLimits
 * @Description TODO
 * @Author ryan
 * @Date 2021/12/26
 */
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ResourcesLimits {
    @Builder.Default
    private String defaultMemory = "64Mi";
    @Builder.Default
    private String defaultCpu = "250m";

    @JsonProperty("mem")
    private String memory;
    private String cpu;

    public String getMemory() {
        if (!memory.toLowerCase().endsWith("mi")) {
            return defaultMemory;
        }
        return memory;
    }

    public String getCpu() {
        if (!cpu.toLowerCase().endsWith("m")) {
            return defaultCpu;
        }
        return cpu;
    }
}

package com.lifezq.schedule.bo.params;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @Package com.lifezq.schedule.bo.params
 * @ClassName Resources
 * @Description TODO
 * @Author ryan
 * @Date 2021/12/26
 */
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Resources {
    private ResourcesLimits requests;
    private ResourcesLimits limits;
}

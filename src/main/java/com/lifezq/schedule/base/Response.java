package com.lifezq.schedule.base;


import com.alibaba.fastjson.JSON;
import com.lifezq.schedule.enums.SuccessCodeEnum;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * @Package com.lifezq.schedule.bo
 * @ClassName Response
 * @Description The type Response.
 * @Author ryan
 * @Date 2021/12/24
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> implements Serializable {
    private static final long serialVersionUID = 10L;
    @Builder.Default
    private Integer returnCode = SuccessCodeEnum.SUCCESS.getValue();
    @Builder.Default
    private String returnMsg = "success";
    @Builder.Default
    private String returnUserMsg = SuccessCodeEnum.SUCCESS.getDesc();
    private T data;
    private List<T> list;

    /**
     * Sets error.
     *
     * @param code    the code
     * @param message the message
     */
    public void setError(Integer code, String message) {
        this.setReturnCode(code);
        this.setReturnMsg(message);
        this.setReturnUserMsg(message);
    }

    /**
     * Sets full error.
     *
     * @param code    the code
     * @param msg     the msg
     * @param userMsg the user msg
     */
    public void setFullError(Integer code, String msg, String userMsg) {
        this.setReturnCode(code);
        this.setReturnMsg(msg);
        this.setReturnUserMsg(userMsg);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static Response<Object> succ() {
        return new Response<>();
    }

    public static <T> Response<T> succ(T data) {
        Response<T> response = new Response<>();
        response.setData(data);
        return response;
    }

}
package com.lifezq.schedule.config;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @Package com.lifezq.schedule.config
 * @ClassName MyBasicErrorController
 * @Description 控制器错误自定义
 * @Author ryan
 * @Date 2021/12/24
 */
@Controller
public class MyBasicErrorController extends BasicErrorController {
    public MyBasicErrorController() {
        super(new DefaultErrorAttributes(), new ErrorProperties());
    }

    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {

        HttpStatus status = getStatus(request);
        Map<String, Object> map = new HashMap<String, Object>(16);
        Map<String, Object> originalMsgMap = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        String path = (String) originalMsgMap.get("path");
        String error = (String) originalMsgMap.get("error");
        StringJoiner joiner = new StringJoiner(" ", "path ", "");
        joiner.add(path).add(error);
        map.put("returnCode", (Integer) originalMsgMap.get("status"));
        map.put("returnMsg", joiner.toString());
        map.put("returnUserMsg", joiner.toString());
        return new ResponseEntity<Map<String, Object>>(map, status);
    }
}

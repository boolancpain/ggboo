package com.fyo.ggboo.infra;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fyo.ggboo.infra.message.CustomMessageSource;

/**
 * custom error controller
 * 
 * @author boolancpain
 */
@Component
public class CustomErrorController extends BasicErrorController {
    @Autowired
    private CustomMessageSource customMessageSource;

    public CustomErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties) {
        super(errorAttributes, serverProperties.getError());
    }

    /**
     * 500 error 응답 객체 변환
     */
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);

        Map<String, Object> body = new HashMap<>();
        body.put("message", customMessageSource.getMessage("error"));

        return ResponseEntity.status(status).body(body);
    }
}
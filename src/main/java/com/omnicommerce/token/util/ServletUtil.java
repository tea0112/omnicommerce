package com.omnicommerce.token.util;

import static com.omnicommerce.reponse.exception.ApiError.ApiErrors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

public class ServletUtil {
    public static void handleServletException(Logger log, HttpServletResponse response, String errorCode, String message, Throwable ex) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String responseBody = objectMapper.writeValueAsString(ApiErrors(log, errorCode, message, ex, HttpStatus.BAD_REQUEST));
        log.debug(responseBody);

        response.setContentType("application/json");
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.getWriter().write(responseBody);
    }
}

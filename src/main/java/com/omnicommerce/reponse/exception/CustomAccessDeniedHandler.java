package com.omnicommerce.reponse.exception;

import com.omnicommerce.golobal.exception.ErrorCodes;
import com.omnicommerce.token.util.ServletUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Log4j2
@Component("customAccessDeniedHandler")
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ServletUtil.handleServletException(log, response, ErrorCodes.E00006.name(), ErrorCodes.E00006.getMessage(), accessDeniedException);
    }
}

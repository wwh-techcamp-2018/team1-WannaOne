package com.wannaone.woowanote.security;

import com.wannaone.woowanote.common.SessionUtil;
import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.dto.LoginDto;
import com.wannaone.woowanote.exception.UnAuthenticationException;
import com.wannaone.woowanote.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.util.Base64;

public class BasicAuthInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(BasicAuthInterceptor.class);

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorization = request.getHeader("Authorization");
        User user;

        log.debug("Authorization : {}, null means not using basicAuth", authorization);
        if (authorization == null || !authorization.startsWith("Basic")) {
            return true;
        }

        try {
            user = getUserBasicAuth(request, authorization);
            log.debug("Login User Email : {}", user.getEmail());
        } catch (UnAuthenticationException e) {
            log.info("interceptor 걸렸네: " + e.getMessage());
        } finally {
            return true;
        }
    }

    private User getUserBasicAuth(HttpServletRequest request, String authorization) {
        String base64Credentials = authorization.substring("Basic".length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials), Charset.forName("UTF-8"));
        final String[] values = credentials.split(":", 2);
        log.debug("email : {}", values[0]);
        log.debug("password : {}", values[1]);
        User user = userService.login(new LoginDto(values[0], values[1]));
        request.getSession().setAttribute(SessionUtil.USER_SESSION_KEY, user);
        return user;
    }
}

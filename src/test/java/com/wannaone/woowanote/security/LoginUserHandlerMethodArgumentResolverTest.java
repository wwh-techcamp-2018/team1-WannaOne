package com.wannaone.woowanote.security;

import com.wannaone.woowanote.common.SessionUtil;
import com.wannaone.woowanote.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class LoginUserHandlerMethodArgumentResolverTest {
    private static final Logger log = LoggerFactory.getLogger(LoginUserHandlerMethodArgumentResolverTest.class);

    @Mock
    private MethodParameter parameter;

    @Mock
    private NativeWebRequest request;

    private LoginUserHandlerMethodArgumentResolver loginUserHandlerMethodArgumentResolver;

    @Before
    public void setup() {
        loginUserHandlerMethodArgumentResolver = new LoginUserHandlerMethodArgumentResolver();
    }

    @Test
    public void loginUser_normal() throws Exception {
        User sessionUser = new User("newUser@woowahan.com", "password", "name");
        when(request.getAttribute(SessionUtil.USER_SESSION_KEY, WebRequest.SCOPE_SESSION)).thenReturn(sessionUser);
        User loginUser = (User) loginUserHandlerMethodArgumentResolver.resolveArgument(parameter, null, request, null);
        log.debug(loginUser.getEmail());
        assertThat(loginUser).isEqualTo(sessionUser);
    }

    @Test
    public void supportsParameter_false() {
        when(parameter.hasParameterAnnotation(LoginUser.class)).thenReturn(false);

        assertThat(loginUserHandlerMethodArgumentResolver.supportsParameter(parameter)).isEqualTo(false);
    }

    @Test
    public void supportsParameter_true() {
        when(parameter.hasParameterAnnotation(LoginUser.class)).thenReturn(true);

        assertThat(loginUserHandlerMethodArgumentResolver.supportsParameter(parameter)).isEqualTo(true);
    }
}

package com.wannaone.woowanote.common;

import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.exception.UnAuthenticationException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;
import java.util.Optional;

public class SessionUtil {
    public static final String USER_SESSION_KEY = "sessionMember";

    public static Optional<User> getMember(HttpSession session) {
        User loginUser = (User) session.getAttribute(USER_SESSION_KEY);
        return Optional.ofNullable(loginUser);
    }

    public static void setMember(HttpSession session, User user) {
        session.setAttribute(USER_SESSION_KEY, user);
    }

    public static void logout(HttpSession session) {
        session.removeAttribute(USER_SESSION_KEY);
    }

    public static boolean matchMember(HttpSession session, User user) {
        User loginUser = getMember(session).orElseThrow(() -> new UnAuthenticationException("로그인이 필요합니다."));
        return loginUser.equals(user);
    }

    public static User getUserFromWebRequest(NativeWebRequest webRequest) {
        return (User) webRequest.getAttribute(USER_SESSION_KEY, WebRequest.SCOPE_SESSION);
    }
}

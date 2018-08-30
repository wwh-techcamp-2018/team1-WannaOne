package com.wannaone.woowanote.web;

import com.wannaone.woowanote.common.SessionUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String redirect(HttpSession session) {
        if (!SessionUtil.getUser(session).isPresent()) {
            return "/login";
        }
        return "/main";
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        if (SessionUtil.getUser(session).isPresent()) {
            return "/main";
        }
        return "/login";
    }

    @GetMapping("/signup")
    public String signup(HttpSession session) {
        if (SessionUtil.getUser(session).isPresent()) {
            return "/main";
        }
        return "/signup";
    }
}

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
    public String home(HttpSession session) {
        if (!SessionUtil.getUser(session).isPresent()) {
            return "login";
        }
        return "main";
    }

    @GetMapping("login")
    public String login(HttpSession session) {
        if (SessionUtil.getUser(session).isPresent()) {
            return "redirect:/main";
        }
        return "login";
    }

    @GetMapping("signup")
    public String signup(HttpSession session) {
        if (SessionUtil.getUser(session).isPresent()) {
            return "redirect:/main";
        }
        return "signup";
    }

    @GetMapping("main")
    public String main(HttpSession session) {
        if (SessionUtil.getUser(session).isPresent()) {
            return "main";
        }
        return "redirect:/login";
    }
}

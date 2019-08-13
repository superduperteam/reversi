package com.spring.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
//    @GetMapping("/")
//    public String home()
//    {
//        return "/pages/signup.html";
//    }

    @GetMapping("/rooms/{id}")
    public String room(@PathVariable String id)
    {
        return "../pages/preGameStart.html";
    }

    @GetMapping("/rooms/{id}/game")
    public String game(@PathVariable String id)
    {
        return "../../pages/gameStart.html";
    }
//
//    @GetMapping("/rooms/{id}/playgame")
//    public String playGame(@PathVariable String id)
//    {
//        return "../pages/gameStart.html";
//    }
}

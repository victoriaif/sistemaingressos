package br.com.sistemaingressos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @GetMapping({"/", "/index.html"})
    public String index() {
        return "index"; // o arquivo index.html em src/main/resources/templates/
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}

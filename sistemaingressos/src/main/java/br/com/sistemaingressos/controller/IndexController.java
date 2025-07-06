package br.com.sistemaingressos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.FragmentsRendering;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;

@Controller
public class IndexController {

    // 1) Página completa do índice (requisição “normal”)
    @GetMapping({ "/", "/index.html" })
    public String indexPage(Model model) {
        // se precisar passar algo ao template, via model.addAttribute(...)
        return "index";
    }

    // 2) Fragmento do índice (somente quando for HTMX)
    @HxRequest
    @GetMapping({ "/", "/index.html" })
    public View indexFragment() {
        return FragmentsRendering
            .with("index :: conteudo")                         // fragmento <main th:fragment="conteudo">
            .fragment("/layout/fragments/header :: header")    // atualiza também o header
            .build();
    }

    // 3) Página completa de login (requisição “normal”, sem HTMX)
    @GetMapping(path = "/login", headers = "!HX-Request")
    public String loginPage() {
        return "login";
    }

    // 4) Fragmento de login para HTMX (só o <form>)
    @HxRequest
    @GetMapping(path = "/login", headers = "HX-Request")
    public String loginFragment() {
        return "login :: formulario";  // fragmento <form th:fragment="formulario">
    }
}

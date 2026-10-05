package com.mundial.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** Solo renderiza vistas Thymeleaf. Los datos se consumen desde la API REST (/api). */
@Controller
public class MundialWebController {

    @GetMapping({ "/", "/index" })
    public String index() {
        return "index";
    }

    @GetMapping("/listar")
    public String listar() {
        return "listar";
    }
}

package com.build.stats.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping


@Controller
class LoginPageController {
    @RequestMapping("/")
    fun page(model: Model): String {
        return "index"
    }
}
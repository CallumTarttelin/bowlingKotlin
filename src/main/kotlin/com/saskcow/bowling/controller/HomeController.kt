package com.saskcow.bowling.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class HomeController {

    @RequestMapping(value = ["/", "league/**", "/add/league", "/player/**", "/league/**", "/game/**"])
    fun index() = "index"

    @RequestMapping(value = ["/login"])
    fun login() = "login"
}

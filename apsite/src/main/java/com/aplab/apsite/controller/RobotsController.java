package com.aplab.apsite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/robots.txt")
@Controller
public class RobotsController {

    @RequestMapping()
    @ResponseBody
    public String robots() {
        return "User-agent: *\nDisallow: /";
    }
}

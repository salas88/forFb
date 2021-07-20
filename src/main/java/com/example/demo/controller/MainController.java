package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@Controller
@RequestMapping("/")
public class MainController  {

    private final Logger logger = Logger.getLogger(MainController.class.getName());

    @GetMapping("/")
    public String mainPage(HttpServletRequest request) {


        logger.info("from controller");
        String check = (String) request.getAttribute("page");
        if(check.equals("detected")){
            logger.info("from page where was all the actions detected some folks");
            logger.info("################################################ User got a page");
            return "mypage";
        }
        logger.info("good Page ");
        logger.info("################################################ User got a page");
        return "main";
    }

}



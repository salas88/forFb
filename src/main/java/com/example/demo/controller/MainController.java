package com.example.demo.controller;

import com.example.demo.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private RequestService requestService;
    private final Logger logger = Logger.getLogger(MainController.class.getName());

    @GetMapping
    public String mainPage(HttpServletRequest request) {

        String clientId = requestService.getClientId(request);
        logger.info(clientId);

        return "main";
    }


}

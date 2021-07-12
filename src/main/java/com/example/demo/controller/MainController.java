package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@Controller
@RequestMapping("/")
public class MainController  {

    private final Logger logger = Logger.getLogger(MainController.class.getName());
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/")
    public String mainPage(HttpServletRequest request) {
        User user = new User();
        user.setName("newName");
        userRepository.save(user);

        logger.info("from controller");
        String check = (String) request.getAttribute("check");
        if(check != null){
            logger.info("from page where was all the actions");
            logger.info("################################################ User got a page");
            return "mypage";
        }
        logger.info("good Page ");
        logger.info("################################################ User got a page");
        return "main";
    }

}



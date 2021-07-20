package com.example.demo.filter;


import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
public class MyFilterRequest implements Filter, ApplicationListener<ApplicationReadyEvent> {

    private final Logger logger = Logger.getLogger(MyFilterRequest.class.getName());
    private List<String> collect = new ArrayList<>();



    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String clientIp = httpServletRequest.getHeader("x-client-ip");
        Optional<String> checkId = Optional.empty();


           if(clientIp != null && !clientIp.equals("")){
               checkId = collect.stream().filter(el -> el.equals(clientIp)).findFirst();
           }

           logger.info(String.valueOf(checkId.isPresent()));
           logger.info("#########################");


        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();

        if(headerNames != null){
            while (headerNames.hasMoreElements()){
                String s = headerNames.nextElement();
                logger.info(s + " " + httpServletRequest.getHeader(s));
            }
        }


        String user_agent = httpServletRequest.getHeader("User-Agent");
        boolean allInfoFacebook = user_agent.contains("developers.facebook.com") ||
                user_agent.contains("FacebookBot") || user_agent.contains("compatible; FacebookBot")
                || user_agent.equals("");


        if(allInfoFacebook || checkId.isPresent()){
          httpServletRequest.setAttribute("page" ,"detected");
        } else {
        httpServletRequest.setAttribute("page" ,"goodPage");
        }
        chain.doFilter(httpServletRequest,response);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try(Stream<String> stream = Files.lines(Paths.get("/local/site/wwwroot/route.txt"))) {
            collect = stream.map(el -> el.substring(7).trim()).collect(Collectors.toList());
        } catch (IOException | StringIndexOutOfBoundsException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}

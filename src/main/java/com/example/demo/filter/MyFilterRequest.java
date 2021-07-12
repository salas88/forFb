package com.example.demo.filter;


import com.example.demo.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
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
    @Autowired
    private RequestService requestService;
    private int count;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String clientId = requestService.getClientId(httpServletRequest);

        Optional<String> checkId = collect.stream().filter(el -> el.equals(clientId)).findFirst();

        String remoteAddr = httpServletRequest.getRemoteAddr();

        logger.info("################################################ Filter method № " + ++count);
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();

        if(headerNames != null){
            while (headerNames.hasMoreElements()){
                String s = headerNames.nextElement();
                logger.info(s + " " + httpServletRequest.getHeader(s));
            }
            String displayCountry = httpServletRequest.getLocale().getDisplayCountry();

            if(displayCountry != null){
                logger.info("Country : " + displayCountry);
            }
            if(clientId != null){
                logger.info("Client id : " + clientId);
            }
            if(remoteAddr != null){
                logger.info("Remote Adr : " + remoteAddr);
            }
            logger.info("---------------End Filter");
        }

        String user_agent = httpServletRequest.getHeader("User-Agent");
        boolean allInfoFacebook = user_agent.contains("developers.facebook.com") ||
                user_agent.contains("FacebookBot") || user_agent.contains("compatible; FacebookBot");

        if(allInfoFacebook || checkId.isPresent()){
          httpServletRequest.setAttribute("check" ,"1");
            chain.doFilter(httpServletRequest,response);
        } else {
            chain.doFilter(httpServletRequest,response);
        }

    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        event.getApplicationContext().getApplicationStartup();
        ClassLoader classLoader = getClass().getClassLoader();
        String fileName = "route.txt";
        File file = new File(classLoader.getResource(fileName).getFile());
        try {
            Stream<String> stream = Files.lines(Paths.get(file.getAbsolutePath()));
            collect = stream.map(el -> el.substring(7).trim()).collect(Collectors.toList());
        } catch (IOException | StringIndexOutOfBoundsException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}

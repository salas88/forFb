package com.example.demo.interceptor;

import com.example.demo.entity.GeoIp;
import com.example.demo.entity.User;
import com.example.demo.service.InterUserService;
import com.example.demo.service.RawDBDemoGeoIPLocationService;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.Optional;
import java.util.logging.Logger;

@Component
public class MyInterceptor implements HandlerInterceptor {
    @Autowired
    private InterUserService interUserService;
    @Autowired
    private RawDBDemoGeoIPLocationService rawDBDemoGeoIPLocationService;
    @Autowired
    private ApplicationContext applicationContext;
    private final Logger logger = Logger.getLogger(MyInterceptor.class.getName());


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String s = headerNames.nextElement();
            logger.info(s + " " + request.getHeader(s));
        }


        if(ex != null){
            ex.printStackTrace();
        }

        String userAgent = request.getHeader("user-agent");
        String headerXClientIp = request.getHeader("x-client-ip");
        String xForwardedFor = request.getHeader("x-forwarded-for");
        String pageAttribute = (String) request.getAttribute("page");

        if(headerXClientIp.equals("127.0.0.1"))
            return;

        DatabaseReader bean = applicationContext.getBean(DatabaseReader.class);

        GeoIp location = null;
               try {
                   location = rawDBDemoGeoIPLocationService.getLocation(headerXClientIp, bean);
               }catch (IOException | GeoIp2Exception exception){
                   logger.info("exception from Interceptor, and this means that something " +
                           "wrong with geoIp, but we catch this exception");
                   exception.printStackTrace();
               }

        Optional<User> userFromDb = interUserService.findAll()
                .stream()
                .filter(user -> (user.getClientIp().equals(xForwardedFor) || user.getClientIp().equals(headerXClientIp)
                            || user.getClientIp().contains(xForwardedFor))
                ).findFirst();


        User user;
        if(userFromDb.isPresent()){
            user = userFromDb.get();
            user.setLocalDateTime(LocalDateTime.now());
        } else {
          if(location != null){
              user = new User.Builder()
                      .withUserAgent(userAgent)
                      .withClientIp(headerXClientIp)
                      .withCountry(location.getNameCountry())
                      .withCity(location.getCity())
                      .withLanguage(request.getLocale().getLanguage())
                      .withPage(pageAttribute)
                      .build();
          } else {
              user = new User.Builder()
                      .withUserAgent(userAgent)
                      .withClientIp(headerXClientIp)
                      .withCountry("dont can did")
                      .withCity("dont can did")
                      .withLanguage(request.getLocale().getLanguage())
                      .withPage(pageAttribute)
                      .build();
          }
        }
        interUserService.saveUser(user);
    }
}

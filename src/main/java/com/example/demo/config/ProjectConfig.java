package com.example.demo.config;

import com.example.demo.interceptor.MyInterceptor;
import com.maxmind.db.CHMCache;
import com.maxmind.db.Reader;
import com.maxmind.geoip2.DatabaseReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;

@EnableWebMvc
@Configuration
public class ProjectConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private MyInterceptor myInterceptor;
    private static DatabaseReader databaseReader;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor);
    }


    @Bean(name = "databaseBean")
    public static synchronized DatabaseReader databaseReader() {
         if(databaseReader != null){
             return databaseReader;
         }

        FileSystemResource resource = new FileSystemResource("/local/site/wwwroot/GeoLite2-City.mmdb");

//        ClassPathResource classPathResource = new ClassPathResource("GeoLite2-City.mmdb");
        try {
                    databaseReader = new DatabaseReader.Builder(
                            resource.getFile()).fileMode(Reader.FileMode.MEMORY_MAPPED)
                            .withCache(new CHMCache()).build();
                } catch (IOException e){
                    e.printStackTrace();
                }
        return databaseReader;
    }


}

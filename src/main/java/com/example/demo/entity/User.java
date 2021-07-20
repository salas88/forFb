package com.example.demo.entity;


import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String clientIp;
    private String country;
    private String language;
    private String userAgent;
    private String page;
    private String city;
    @CreationTimestamp
    private LocalDateTime localDateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", clientIp='" + clientIp + '\'' +
                ", country='" + country + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", page='" + page + '\'' +
                ", localDateTime=" + localDateTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(clientIp, user.clientIp) && Objects.equals(country, user.country) && Objects.equals(userAgent, user.userAgent) && Objects.equals(page, user.page) && Objects.equals(localDateTime, user.localDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientIp, country, userAgent, page, localDateTime);
    }

    public static class Builder{
        private User newUser;

        public Builder(){
            newUser = new User();
        }

        public Builder withClientIp(String clientIp){
            newUser.setClientIp(clientIp);
            return this;
        }
        public Builder withCountry(String country){
            newUser.setCountry(country);
            return this;
        }
        public Builder withUserAgent(String userAgent){
            newUser.setUserAgent(userAgent);
            return this;
        }
        public Builder withPage(String page){
            newUser.setPage(page);
            return this;
        }
        public Builder withLanguage(String language){
            newUser.setLanguage(language);
            return this;
        }
        public Builder withCity(String city){
            newUser.setCity(city);
            return this;
        }

        public User build(){
            return newUser;
        }
    }


}

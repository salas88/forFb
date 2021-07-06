package com.example.demo.service;

import javax.servlet.http.HttpServletRequest;


public interface RequestService {
    String getClientId(HttpServletRequest request);
}

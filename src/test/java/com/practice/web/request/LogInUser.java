package com.practice.web.request;

import lombok.Data;

@Data
public class LogInUser {
    private String cookieKey;
    private String cookieValue;
    private String userName;
    private String password;
}

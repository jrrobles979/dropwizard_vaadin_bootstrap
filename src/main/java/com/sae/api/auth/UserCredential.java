package com.sae.api.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by ralmeida on 10/15/15.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserCredential {

    private String username;
    private String userpassword;
    private String device;
    private String app;

    public UserCredential(){
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }
}


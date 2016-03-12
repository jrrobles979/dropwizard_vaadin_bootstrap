package com.sae.event.core.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by ralmeida on 2/18/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewPasswordRecover {
    private UserItem user;
    private String email;

    public NewPasswordRecover() {
        email = "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserItem getUser() {
        return user;
    }

    public void setUser(UserItem user) {
        this.user = user;
    }
}

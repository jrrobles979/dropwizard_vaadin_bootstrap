package com.sae.event.core.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by ralmeida on 2/18/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewUserRequest {
    private String fullname;
    private String email;

    public NewUserRequest() {
        fullname = "";
        email = "";
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

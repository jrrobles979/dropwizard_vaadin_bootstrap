package com.sae.event.core.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by ralmeida on 2/18/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PasswordRecoverRequest {
    private String email;

    public PasswordRecoverRequest() {
        email = "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

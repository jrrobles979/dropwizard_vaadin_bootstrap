package com.sae.event.core.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sae.event.core.User;

import java.sql.Timestamp;

/**
 * Created by ralmeida on 10/15/15.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserItem {

    private String first_name;
    private String last_name;
    private String username;
    private String email;
    private int    active;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss z")
    private Timestamp last_active;

    public UserItem(){
    }

    public UserItem(User user){
        this.first_name = user.getFirst_name();
        this.last_name = user.getLast_name();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.active = user.getActive();
        this.last_active = user.getLast_active();
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public Timestamp getLast_active() {
        return last_active;
    }

    public void setLast_active(Timestamp last_active) {
        this.last_active = last_active;
    }
}


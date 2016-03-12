package com.sae.event.core.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sae.event.core.Session;

import java.sql.Timestamp;

/**
 * Created by ralmeida on 1/7/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionItem {
    private String device;
    private String app;
    private String ip;
    private UserItem user;
    private String token;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss z")
    private Timestamp updated_at;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss z")
    private Timestamp created_at;

    public SessionItem(){
    }

    public SessionItem(Session session){
        device = session.getDevice();
        app = session.getApp();
        ip = session.getIp();
        updated_at = session.getUpdated_at();
        created_at = session.getCreated_at();
        user = new UserItem(session.getUser());
        user.setLast_active(session.getUpdated_at());
        token = session.getToken();
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

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public UserItem getUser() {
        return user;
    }

    public void setUser(UserItem user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

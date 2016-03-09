package com.sae.api.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Created by ralmeida on 10/22/15.
 */

@Entity
@Table(name="sessions", uniqueConstraints = {@UniqueConstraint(columnNames={"id"})})
@NamedQueries({
        @NamedQuery(
                name = Session.findByUser,
                query = "FROM Session where user = :USER"
        ),
        @NamedQuery(
                name = Session.find,
                query = "FROM Session where id = :ID"
        ),
        @NamedQuery(
                name = Session.findByToken,
                query = "FROM Session where token = :TOKEN"
        )
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Session {

    public static final String find = "Session.find";
    public static final String findByUser = "Session.findByUser";
    public static final String findByToken = "Session.findByToken";

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @OneToOne (fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

//    @Column(name="user_id", nullable=true)
//    private long user_id;

    @Column(name="created_date", nullable=true)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss z")
    private Timestamp created_at;

    @Column(name="updated_date", nullable=true)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss z")
    private Timestamp updated_at;

    @Column(name="token", nullable=true)
    private String token;

    @Column(name="device", length=45, nullable=true)
    private String device;

    @Column(name="app", length=45, nullable=true)
    private String app;

    @Column(name="ip", length=45, nullable=true)
    private String ip;

    public Session(){
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

//    public long getUser_id() {
//        return user_id;
//    }
//
//    public void setUser_id(long user_id) {
//        this.user_id = user_id;
//    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Session)) {
            return false;
        }

        final Session that = (Session) o;

        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

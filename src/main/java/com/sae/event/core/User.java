package com.sae.event.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Created by ralmeida on 10/15/15.
 */

@Entity
@Table(name="users", uniqueConstraints = {@UniqueConstraint(columnNames={"id"})})
@NamedQueries({
        @NamedQuery(
                name = User.findAll,
                query = "SELECT p FROM User p order by first_name"
        ),
        @NamedQuery(
                name = User.findByUserAndPassword,
                query = "SELECT p FROM User p WHERE username = :user AND password_digest = :password AND active = 1"
        ),
        @NamedQuery(
                name = User.findByUser,
                query = "SELECT p FROM User p WHERE username = :USER"
        ),
        @NamedQuery(
                name = User.findByUserOrEmail,
                query = "SELECT p FROM User p WHERE username = :USER OR email = :EMAIL"
        ),
        @NamedQuery(
                name = User.findByEmail,
                query = "SELECT p FROM User p WHERE email = :EMAIL"
        ),
        @NamedQuery(
                name = User.findBySessionToken,
                query = "SELECT U FROM User U, Session S WHERE S.token = :TOKEN and U = S.user"
        )
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Principal {

    public static final String findBySessionToken = "User.findBySessionToken";
    public static final String findByUserAndPassword = "User.findByUserAndPassword";
    public static final String findByUser = "User.findByUser";
    public static final String findByUserOrEmail = "User.findByUserOrEmail";
    public static final String findByEmail = "User.findByEmail";
    public static final String findAll = "User.findAll";

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(name="username",  nullable=false)
    private String username;

    @Column(name="password_digest",  nullable=false)
    private String password_digest;

    @Column(name="first_name",  nullable=false)
    private String first_name;

    @Column(name="last_name",  nullable=false)
    private String last_name;

    @Column(name="active", nullable=false)
    private int active;

    @Column(name="email", nullable=true)
    private String email;

    @Transient
    @JsonProperty
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss z")
    private Timestamp last_active;

    @Transient
    private int[] roles;

    public User(){
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getPassword_digest() {
        return password_digest;
    }

    public void setPassword_digest(String password_digest) {
        this.password_digest = password_digest;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public int[] getRoles() {
        return roles;
    }

    @JsonIgnore
    public void setRoles(int[] roles) {
        this.roles = roles;
    }

    @JsonProperty
    public Timestamp getLast_active() {
        return last_active;
    }

    public void setLast_active(Timestamp last_active) {
        this.last_active = last_active;
    }

    @JsonIgnore


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }

        final User that = (User) o;

        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.username, that.username) &&
                Objects.equals(this.first_name, that.first_name) &&
                Objects.equals(this.last_name, that.last_name);
    }

    public String toString() {
        return getUsername();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, first_name, last_name);
    }

    public String getName() {
        return getFirst_name() + " " + getLast_name();
    }
}

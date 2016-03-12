package com.sae.event.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Created by ralmeida on 10/15/15.
 */

@Entity
@Table(name="event", uniqueConstraints = {@UniqueConstraint(columnNames={"id"})})
@NamedQueries({
        @NamedQuery(
                name = Event.findAll,
                query = "FROM Event"
        )
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {

    public static final String findAll = "Event.findAll";

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(name="name",  nullable=false)
    private String name;

    @Column(name="description",  nullable=false)
    private String description;

    @Column(name="active", nullable=false)
    private Integer active;

    @Column(name="type", nullable=false)
    private Integer type;

    @Column(name="capacity", nullable=false)
    private Integer capacity;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss z")
    @Column(name="start_date", nullable=true)
    private Timestamp start_date;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss z")
    @Column(name="end_date", nullable=true)
    private Timestamp end_date;

    @Column(name="location_lat", nullable=true)
    private Float location_lat;

    @Column(name="location_long", nullable=true)
    private Float location_long;

    public Event(){
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Timestamp getStart_date() {
        return start_date;
    }

    public void setStart_date(Timestamp start_date) {
        this.start_date = start_date;
    }

    public Timestamp getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Timestamp end_date) {
        this.end_date = end_date;
    }

    public Float getLocation_lat() {
        return location_lat;
    }

    public void setLocation_lat(Float location_lat) {
        this.location_lat = location_lat;
    }

    public Float getLocation_long() {
        return location_long;
    }

    public void setLocation_long(Float location_long) {
        this.location_long = location_long;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Event)) {
            return false;
        }

        final Event that = (Event) o;

        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}

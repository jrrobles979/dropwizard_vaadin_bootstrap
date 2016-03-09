package com.sae.api.core.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by ralmeida on 1/29/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusInfo {
    private String status;
    private String version;
    private String releaseDate;

    public StatusInfo() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}

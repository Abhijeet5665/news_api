package com.soumya.wwdablu.sangbad.network.model;

import java.util.LinkedList;

public class SourceResponse {

    private String status;
    private LinkedList<Sources> sources;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LinkedList<Sources> getSources() {
        return sources;
    }

    public void setSources(LinkedList<Sources> sources) {
        this.sources = sources;
    }
}

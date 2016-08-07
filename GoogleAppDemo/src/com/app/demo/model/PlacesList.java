package com.app.demo.model;

import java.io.Serializable;
import java.util.List;


public class PlacesList implements Serializable {
 
    public String status;
 
    public List<PlaceModel> results;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PlaceModel> getResults() {
        return results;
    }

    public void setResults(List<PlaceModel> results) {
        this.results = results;
    }
}

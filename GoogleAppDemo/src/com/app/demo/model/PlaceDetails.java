package com.app.demo.model;

import java.io.Serializable;


@SuppressWarnings("serial")
public class PlaceDetails implements Serializable {

    public String status;
    public PlaceModel result;

    
    
    public PlaceModel getResult() {
		return result;
	}

	public void setResult(PlaceModel result) {
		this.result = result;
	}



	@Override
    public String toString() {
        if (result != null) {
            return result.toString();
        }
        return super.toString();
    }


}

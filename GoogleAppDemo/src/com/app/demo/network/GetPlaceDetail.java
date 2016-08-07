package com.app.demo.network;

import android.util.Log;

import com.app.demo.model.PlaceDetails;
import com.app.demo.model.PlacesList;
import com.app.demo.parser.PlacesParser;

import org.json.JSONObject;

/**
 * Created by Sudesi infotech on 8/4/2016.
 */
public class GetPlaceDetail {

	public PlacesList getPlacesList(String toPassUrl) {
		String googlePlacesData = null;
		JSONObject googlePlacesJson = null;
		PlacesList placesList = new PlacesList();

		try {
			// googleMap = (GoogleMap) inputObj[0];
			String googlePlacesUrl = toPassUrl;
			Http http = new Http();
			googlePlacesData = http.read(googlePlacesUrl);

			PlacesParser placeJsonParser = new PlacesParser();

			try {

				googlePlacesJson = new JSONObject(googlePlacesData);
				placesList = placeJsonParser.parse(googlePlacesJson);
			} catch (Exception e) {
				Log.d("Exception", e.toString());
			}
		} catch (Exception e) {
			Log.d("Google Place Read Task", e.toString());
		}
		return placesList;
	}

	public PlaceDetails getPlacesDetail(String toPassUrl) {
		String googlePlacesData = null;
		JSONObject googlePlacesJson = null;
		PlaceDetails placesList = new PlaceDetails();

		try {
			// googleMap = (GoogleMap) inputObj[0];
			String googlePlacesUrl = toPassUrl;
			Http http = new Http();
			googlePlacesData = http.read(googlePlacesUrl);

			PlacesParser placeJsonParser = new PlacesParser();

			try {

				googlePlacesJson = new JSONObject(googlePlacesData);
				placesList = placeJsonParser.getPlaceDetailModel(googlePlacesJson);
			} catch (Exception e) {
				Log.d("Exception", e.toString());
			}
		} catch (Exception e) {
			Log.d("Google Place Read Task", e.toString());
		}
		return placesList;
	}

}

package com.app.demo.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.demo.model.PlaceModel;
import com.app.demo.model.PlaceModel.Photos;
import com.app.demo.model.PlaceDetails;
import com.app.demo.model.PlacesList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlacesParser {

	public PlacesList parse(JSONObject jsonObject) {
		JSONArray jsonArray = null;
		try {
			jsonArray = jsonObject.optJSONArray("results");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getPlaces(jsonArray);
	}

	private PlacesList getPlaces(JSONArray jsonArray) {
		int placesCount = jsonArray.length();
		PlacesList placeListObj = new PlacesList();
		List<PlaceModel> placesList = new ArrayList<PlaceModel>();

		for (int i = 0; i < placesCount; i++) {
			try {
				placesList.add(getPlaceObject((JSONObject) jsonArray.get(i)));

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		placeListObj.setResults(placesList);
		return placeListObj;
	}

	private HashMap<String, String> getPlace(JSONObject googlePlaceJson) {
		HashMap<String, String> googlePlaceMap = new HashMap<String, String>();
		String placeName = "-NA-";
		String vicinity = "-NA-";
		String latitude = "";
		String longitude = "";
		String reference = "";

		try {
			if (!googlePlaceJson.isNull("name")) {
				placeName = googlePlaceJson.optString("name");
			}
			if (!googlePlaceJson.isNull("vicinity")) {
				vicinity = googlePlaceJson.optString("vicinity");
			}
			latitude = googlePlaceJson.optJSONObject("geometry").optJSONObject("location").optString("lat");
			longitude = googlePlaceJson.optJSONObject("geometry").optJSONObject("location").optString("lng");
			reference = googlePlaceJson.optString("reference");
			googlePlaceMap.put("place_name", placeName);
			googlePlaceMap.put("vicinity", vicinity);
			googlePlaceMap.put("lat", latitude);
			googlePlaceMap.put("lng", longitude);
			googlePlaceMap.put("reference", reference);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return googlePlaceMap;
	}

	private PlaceModel getPlaceObject(JSONObject googlePlaceJson) {
		PlaceModel googlePlace = new PlaceModel();
		String placeName = "-NA-";
		String vicinity = "-NA-";
		String latitude = "";
		String longitude = "";
		String reference = "";

		try {
			if (googlePlaceJson.has("name")) {
				placeName = googlePlaceJson.optString("name");
			}
			if (!googlePlaceJson.has("vicinity")) {
				vicinity = googlePlaceJson.optString("vicinity");
			}
			// "icon":
			// "https://maps.gstatic.com/mapfiles/place_api/icons/bank_dollar-71.png",
			// "id": "4a10f9a77272108bbd05370326069253d6369f3c",
			// "name": "Shinhan Bank America",
			// "opening_hours": {
			// "open_now": false,
			// "weekday_text": [
			//
			// ]
			// },
			// "place_id": "ChIJzz1OXahZwokRWwoOTQji7do",
			if (googlePlaceJson.has("geometry")) {
				latitude = googlePlaceJson.optJSONObject("geometry").optJSONObject("location").optString("lat");
				longitude = googlePlaceJson.optJSONObject("geometry").optJSONObject("location").optString("lng");
			}
			reference = googlePlaceJson.optString("reference");
			PlaceModel.Location locationObj = googlePlace.new Location();
			locationObj.setLat(Double.parseDouble(latitude));
			locationObj.setLng(Double.parseDouble(longitude));

			PlaceModel.Geometry geometryObj = googlePlace.new Geometry();
			geometryObj.setLocation(locationObj);
			PlaceModel.OpeningHours openingHours = googlePlace.new OpeningHours();
			try {
				if (googlePlaceJson.has("opening_hours")) {
					openingHours.setOpenNow(googlePlaceJson.optJSONObject("opening_hours").optBoolean("open_now"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (googlePlaceJson.has("photos")) {

				JSONArray photoArray = googlePlaceJson.optJSONArray("photos");

				if (photoArray != null && photoArray.length() > 0) {
					List<Photos> photoList = new ArrayList<>();
					for (int i = 0; i < photoArray.length(); i++) {
						Photos photoObj = googlePlace.new Photos();
						JSONObject jObj = photoArray.optJSONObject(i);
						photoObj.setHeight(jObj.optString("height"));
						photoObj.setWidth(jObj.optString("width"));
						photoObj.setPhotoRefrence(jObj.optString("photo_reference"));
						photoList.add(photoObj);
					}

					googlePlace.setPhotoList(photoList);
				}

			}
			if (googlePlaceJson.has("types")) {

				JSONArray typesArray = googlePlaceJson.optJSONArray("types");
				String category = "";

				for (int j = 0; j < typesArray.length(); j++) {
					category = typesArray.optString(j) + "," + category;
				}
				googlePlace.setCategory(category);
			}

			googlePlace.setName(placeName);
			googlePlace.setVicinity(vicinity);
			googlePlace.setGeometry(geometryObj);
			googlePlace.setIcon(googlePlaceJson.optString("icon"));
			googlePlace.setId(googlePlaceJson.optString("id"));
			googlePlace.setPlace_id(googlePlaceJson.optString("place_id"));
			googlePlace.setReference(reference);
			googlePlace.setOpeningHours(openingHours);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return googlePlace;
	}

	public PlaceDetails getPlaceDetailModel(JSONObject jsonObject) {
		// TODO Auto-generated method stub

		PlaceDetails placeDetails = new PlaceDetails();

		JSONObject jsonObjectResult = null;
		try {
			jsonObjectResult = jsonObject.optJSONObject("result");

			PlaceModel placeModel = new PlaceModel();

			try {
				placeModel = (getPlaceObject(jsonObjectResult));

			} catch (Exception e) {
				e.printStackTrace();
			}

			placeDetails.setResult(placeModel);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return placeDetails;

	}

}
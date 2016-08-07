package com.app.demo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlaceModel implements Serializable {

	public String id;

	public String place_id;
	public String name;

	public String reference;

	public String icon;

	public String vicinity;

	public Geometry geometry;
	public String category;

	public String formatted_address;
	
	public List<Photos> photoList=new ArrayList<>();

	public List<Photos> getPhotoList() {
		return photoList;
	}

	public void setPhotoList(List<Photos> photoList) {
		this.photoList = photoList;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getPlace_id() {
		return place_id;
	}

	public void setPlace_id(String place_id) {
		this.place_id = place_id;
	}

	public OpeningHours getOpeningHours() {
		return openingHours;
	}

	public void setOpeningHours(OpeningHours openingHours) {
		this.openingHours = openingHours;
	}

	public OpeningHours openingHours;
	public String formatted_phone_number;

	@Override
	public String toString() {
		return name + " - " + id + " - " + reference;
	}

	public class Geometry implements Serializable {

		public Location location;

		public Location getLocation() {
			return location;
		}

		public void setLocation(Location location) {
			this.location = location;
		}
	}

	public class Photos implements Serializable {

		String height, width;
		String photoRefrence;
		public String getHeight() {
			return height;
		}
		public void setHeight(String height) {
			this.height = height;
		}
		public String getWidth() {
			return width;
		}
		public void setWidth(String width) {
			this.width = width;
		}
		public String getPhotoRefrence() {
			return photoRefrence;
		}
		public void setPhotoRefrence(String photoRefrence) {
			this.photoRefrence = photoRefrence;
		}
		
	}

	public class Location implements Serializable {

		public double lat;

		public double lng;

		public double getLat() {
			return lat;
		}

		public void setLat(double lat) {
			this.lat = lat;
		}

		public double getLng() {
			return lng;
		}

		public void setLng(double lng) {
			this.lng = lng;
		}
	}

	public class OpeningHours implements Serializable {

		boolean openNow;

		public boolean isOpenNow() {
			return openNow;
		}

		public void setOpenNow(boolean openNow) {
			this.openNow = openNow;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getVicinity() {
		return vicinity;
	}

	public void setVicinity(String vicinity) {
		this.vicinity = vicinity;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public String getFormatted_address() {
		return formatted_address;
	}

	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}

	public String getFormatted_phone_number() {
		return formatted_phone_number;
	}

	public void setFormatted_phone_number(String formatted_phone_number) {
		this.formatted_phone_number = formatted_phone_number;
	}

}

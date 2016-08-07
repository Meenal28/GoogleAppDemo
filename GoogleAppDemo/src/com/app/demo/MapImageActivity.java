package com.app.demo;

import java.util.ArrayList;
import java.util.List;

import com.app.demo.adapter.ImagesAdapter;
import com.app.demo.location.LocationService;
import com.app.demo.model.PlaceModel;
import com.app.demo.model.PlaceModel.Photos;
import com.app.demo.model.PlaceDetails;
import com.app.demo.network.DownloadFile;
import com.app.demo.network.GetPlaceDetail;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.meetme.android.horizontallistview.HorizontalListView;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class MapImageActivity extends AbstractActivity implements OnMapReadyCallback {

	private GoogleMap googleMap;
	private PlaceModel placeModel;
	private double latitude = 0;
	private double longitude = 0;

	private LocationService locationService;
	private Context mContext;
	private Location location;
	private HorizontalListView horizontalListView;
	private ProgressDialog mProgressDialog;
	private ImagesAdapter imageAdapter;
	private List<Photos> photoList;
	private PlaceDetails placeDetail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			// show error dialog if GoolglePlayServices not available
			if (!isGooglePlayServicesAvailable()) {
				finish();
			}

			initView();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * method to initiliase view
	 */
	private void initView() {
		setContentView(R.layout.activity_google_places);
		mContext = this;
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.setMessage(mContext.getString(R.string.dialog_message));
		googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap)).getMap();
		horizontalListView = (HorizontalListView) findViewById(R.id.hlvCustomList);

		placeDetail = new PlaceDetails();
		photoList = new ArrayList<>();
		if (!isConnectingToInternet()) {
			showAlertDialog(mContext, mContext.getString(R.string.title_alert),
					mContext.getString(R.string.warning_internet), false);

		} else {
			locationService = LocationService.getLocationInstance(mContext);
			if (googleMap != null) {
				googleMap.setMyLocationEnabled(true);
			}
			if (getIntent().hasExtra("placeModel")) {
				placeModel = (PlaceModel) getIntent().getSerializableExtra("placeModel");
				StringBuilder googlePlacesUrl = new StringBuilder(GOOGLE_PLACE_DETAIL_URL);
				googlePlacesUrl.append("&reference=" + placeModel.getReference());
				googlePlacesUrl.append("&sensor=true");
				googlePlacesUrl.append("&key=" + GOOGLE_API_KEY);
				PlacesDetailTask placeDetailTask = new PlacesDetailTask();
				placeDetailTask.execute(googlePlacesUrl.toString());

			}
			horizontalListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub

					if (isConnectingToInternet()) {
						Photos googlePhoto = photoList.get(position);
						StringBuilder googlePlacesUrl = new StringBuilder(GOOGLE_PLACE_IMAGE_URL);
						googlePlacesUrl.append("&"+KEY_PHOTO_REFRENCE+"=" + googlePhoto.getPhotoRefrence());
						googlePlacesUrl.append("&"+KEY_MAX_WIDTH+"=400");
						googlePlacesUrl.append("&"+KEY+"=" + GOOGLE_API_KEY);
						DownloadFile downloadFile = new DownloadFile(mContext, googlePlacesUrl.toString(), position);
						downloadFile.execute();
					} else {
						displayMessage(mContext.getString(R.string.warning_internet));
					}

				}
			});
		}
	}

	// task to get place detail with images
	public class PlacesDetailTask extends AsyncTask<String, Integer, PlaceDetails> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (mProgressDialog != null && !mProgressDialog.isShowing()) {
				mProgressDialog.show();
			}
		}

		@Override
		protected PlaceDetails doInBackground(String... inputObj) {

			PlaceDetails placesDetails = new PlaceDetails();
			GetPlaceDetail getPlaceDetail = new GetPlaceDetail();
			placesDetails = getPlaceDetail.getPlacesDetail(inputObj[0]);
			return placesDetails;
		}

		@Override
		protected void onPostExecute(PlaceDetails list) {

			if (mProgressDialog != null && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}

			try {
				placeDetail = new PlaceDetails();
				photoList.clear();

				if (list != null && list.getResult() != null && list.getResult().getPhotoList().size() > 0) {
					placeDetail = list;
					photoList.addAll(placeDetail.getResult().getPhotoList());
					imageAdapter = new ImagesAdapter(mContext, photoList, GOOGLE_PLACE_IMAGE_URL);
					horizontalListView.setAdapter(imageAdapter);
					setListViewHeightBasedOnChildren(horizontalListView);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				if (locationService.canGetLocation()) {
					location = locationService.getLocation();
					latitude = locationService.getLatitude();
					longitude = locationService.getLongitude();
				} else {
					locationService.showSettingsAlert();
				}
				showCurrentLocationOnMap(location);
				if (placeModel != null) {
					showPlaceLocationOnMap(placeModel.getGeometry().getLocation().getLat(),
							placeModel.getGeometry().getLocation().getLng());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * method to check google play services
	 * 
	 * @return
	 */
	private boolean isGooglePlayServicesAvailable() {
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (ConnectionResult.SUCCESS == status) {
			return true;
		} else {
			GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
			return false;
		}
	}

	/**
	 * method to show the selected place location on map
	 * 
	 * @param latitude
	 * @param longitude
	 */
	private void showPlaceLocationOnMap(double latitude, double longitude) {
		try {

			LatLng latLng = new LatLng(latitude, longitude);
			googleMap.addMarker(new MarkerOptions().position(latLng).title(placeModel.getName())
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_blue)));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * method to show current location on map
	 * 
	 * @param location
	 */
	private void showCurrentLocationOnMap(Location location) {
		try {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			LatLng latLng = new LatLng(latitude, longitude);
			MarkerOptions marker = new MarkerOptions().position(latLng)
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_red));
			googleMap.addMarker(marker);

			googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMapReady(GoogleMap gMap) {
		googleMap = gMap;

	}

}

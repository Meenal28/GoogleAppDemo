package com.app.demo;

import java.util.ArrayList;
import java.util.List;

import com.app.demo.adapter.PlacesDataAdapter;
import com.app.demo.location.LocationService;
import com.app.demo.model.PlaceModel;
import com.app.demo.model.PlaceDetails;
import com.app.demo.model.PlacesList;
import com.app.demo.network.GetPlaceDetail;
import com.app.demo.utils.PrefrenceUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by Meenal on 8/3/2016.
 */
public class SearchNearByPlaces extends AbstractActivity {

	// List view
	private ListView listViewPlaces;

	// Listview Adapter
	private ArrayAdapter<String> placesAdapter;
	private AutoCompleteTextView autoTextViewSearch;

	private List<PlaceModel> placeList;
	private Button findButton;
	private LocationService locationService;
	private Context mContext;
	double latitude = 0;
	double longitude = 0;
	private int PROXIMITY_RADIUS = 100;
	private ProgressDialog mProgressDialog;
	private PlacesDataAdapter adapterPlacesData;
	private PrefrenceUtils pref;
	private List<String> searchList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_search_display_places);

		initView();

	}

	/**
	 * method to initiliase view
	 */
	private void initView() {
		listViewPlaces = (ListView) findViewById(R.id.list_view_place);
		autoTextViewSearch = (AutoCompleteTextView) findViewById(R.id.autoTextView_search);

		findButton = (Button) findViewById(R.id.btnFind);
		mContext = this;
		locationService = LocationService.getLocationInstance(mContext);
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.setMessage(mContext.getString(R.string.dialog_message));
		placeList = new ArrayList<>();
		// Adding items to listview
		pref = new PrefrenceUtils(mContext);
		searchList = new ArrayList<>();
		if (pref.getSearchList() != null && pref.getSearchList().size() > 0) {
			searchList.addAll(pref.getSearchList());
		}
		placesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, searchList);
		autoTextViewSearch.setAdapter(placesAdapter);
		if (!isConnectingToInternet()) {
			showAlertDialog(mContext, mContext.getString(R.string.title_alert),
					mContext.getString(R.string.warning_internet), false);

		}

		findButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					if (mProgressDialog != null && !mProgressDialog.isShowing()) {
						mProgressDialog.show();
					}
					String type = autoTextViewSearch.getText().toString();
					type = type.trim();
					List<String> idsList = new ArrayList<String>();

					// preference to save the user search result to show when user type in
					if (pref.getSearchList().size() > 0) {
						idsList.addAll(pref.getSearchList());
					}
					idsList.add(type);
					pref.saveSearchList(idsList);
					
					type = type.replaceAll(" ", "%20");
					latitude = locationService.getLatitude();
					longitude = locationService.getLongitude();
					StringBuilder googlePlacesUrl = new StringBuilder(GOOGLE_PLACE_URL);
					googlePlacesUrl.append(KEY_LOCATION + "=" + latitude + "," + longitude);
					googlePlacesUrl.append("&" + KEY_RADIUS + "=" + PROXIMITY_RADIUS);
					googlePlacesUrl.append("&" + KEY_QUERY + "=" + type);
					googlePlacesUrl.append("&" + KEY_SENSOR + "=true");
					googlePlacesUrl.append("&" + KEY + "=" + GOOGLE_API_KEY);
					
					PlacesDisplayTask placesDisplayTask = new PlacesDisplayTask();
					placesDisplayTask.execute(googlePlacesUrl.toString());

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if (mProgressDialog != null && mProgressDialog.isShowing()) {
						mProgressDialog.dismiss();
					}
				}

			}
		});
		listViewPlaces.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

				Bundle bundle = new Bundle();
				bundle.putSerializable("placeModel", placeList.get(position));
				pushActivity(SearchNearByPlaces.this, MapImageActivity.class, bundle, false);

			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// check for location service
		if (locationService != null) {
			if (locationService.canGetLocation()) {

			} else {
				locationService.showSettingsAlert();
			}
		}
	}

	// task to get search text result(places list)
	public class PlacesDisplayTask extends AsyncTask<String, Integer, PlacesList> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected PlacesList doInBackground(String... inputObj) {

			PlacesList placesList = new PlacesList();
			GetPlaceDetail getPlaceDetail = new GetPlaceDetail();
			placesList = getPlaceDetail.getPlacesList(inputObj[0]);
			return placesList;
		}

		@Override
		protected void onPostExecute(PlacesList list) {

			try {
				if (placeList != null && placeList.size() > 0) {
					placeList.clear();
				}
				if (mProgressDialog != null && mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
				}

				if (list != null && list.getResults() != null && list.getResults().size() > 0) {
					placeList.addAll(list.getResults());
					adapterPlacesData = new PlacesDataAdapter(mContext, placeList);
					listViewPlaces.setAdapter(adapterPlacesData);
				}

				if (adapterPlacesData != null) {
					adapterPlacesData.notifyDataSetChanged();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}

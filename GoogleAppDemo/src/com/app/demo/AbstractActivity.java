package com.app.demo;

import com.meetme.android.horizontallistview.HorizontalListView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.Toast;

/**
 * Created by Meenal on 8/3/2016.
 */
public class AbstractActivity extends FragmentActivity {
	protected static final String GOOGLE_API_KEY = "AIzaSyDtO3O8OmgIcsDkwmLufUMx_9DRMDuhLis";
	protected static final String GOOGLE_MAP_API_KEY = "AIzaSyB0uD6paWaEKFd_fi1YuqIx5r8vatpFrlo";
	protected String GOOGLE_PLACE_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?";
	// https://maps.googleapis.com/maps/api/place/search/json?
	protected String GOOGLE_PLACE_IMAGE_URL = "https://maps.googleapis.com/maps/api/place/photo?";
	protected String GOOGLE_PLACE_DETAIL_URL = "https://maps.googleapis.com/maps/api/place/details/json?";
	private Context mContext;
	protected String KEY_PHOTO_REFRENCE="photoreference";
	protected String KEY="key";
	protected String KEY_MAX_WIDTH="maxwidth";
	protected String KEY_LOCATION="location";
	protected String KEY_RADIUS="radius";
	protected String KEY_QUERY="query";
	protected String KEY_SENSOR="sensor";

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		mContext = this;
	}

	// method to display toast message
	public void displayMessage(String message) {
		// TODO Auto-generated method stub
		Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
		;
	}

	// method to set height width of listview based on child count
	public void setListViewHeightBasedOnChildren(HorizontalListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = (totalHeight + (1 * (listAdapter.getCount() - 1))) / listAdapter.getCount();
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	/**
	 * Function to display simple Alert Dialog
	 * 
	 * @param context
	 *            - application context
	 * @param title
	 *            - alert dialog title
	 * @param message
	 *            - alert message
	 * @param status
	 *            - success/failure (used to set icon) - pass null if you don't
	 *            want icon
	 */
	@SuppressWarnings("deprecation")
	public void showAlertDialog(final Context context, String title, String message, Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		if (status != null)
			// Setting alert dialog icon
			alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}

	/**
	 * Checking for all possible internet providers
	 **/
	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}

	// method to start new activity
	public void pushActivity(Activity activityToFinish, Class classToStart, Bundle bundle, boolean finishStatus) {
		// TODO Auto-generated method stub
		Intent in = new Intent(activityToFinish, classToStart);
		if (bundle != null) {
			in.putExtras(bundle);
		}
		startActivity(in);
		if (finishStatus) {
			activityToFinish.finish();
		}
	}
}

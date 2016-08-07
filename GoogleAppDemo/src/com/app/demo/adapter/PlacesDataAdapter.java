package com.app.demo.adapter;

import java.util.List;

import com.app.demo.R;
import com.app.demo.model.PlaceModel;
import com.app.demo.utils.RoundImageTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PlacesDataAdapter extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<PlaceModel> placesList = null;

	public PlacesDataAdapter(Context context, List<PlaceModel> placesList) {
		mContext = context;
		this.placesList = placesList;
		inflater = LayoutInflater.from(mContext);

	}

	public class ViewHolder {
		TextView placeNameText;
		TextView openNowStatus;
		TextView category;
		ImageView placeIcon;

	}

	@Override
	public int getCount() {
		return placesList.size();
	}

	@Override
	public PlaceModel getItem(int position) {
		return placesList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unchecked")
	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.list_item, null);
			// Locate the TextViews in listview_item.xml
			holder.placeNameText = (TextView) view.findViewById(R.id.textViewName);
			holder.openNowStatus = (TextView) view.findViewById(R.id.textViewOpenStatus);
			holder.category = (TextView) view.findViewById(R.id.textViewCategory);
			holder.placeIcon = (ImageView) view.findViewById(R.id.placeImageView);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		PlaceModel googlePlace = placesList.get(position);

		// Set the results into TextViews
		holder.placeNameText.setText(googlePlace.getName());
		holder.openNowStatus
				.setText(mContext.getString(R.string.text_open_now) + ":" + googlePlace.getOpeningHours().isOpenNow());
		holder.category.setText("(" + googlePlace.getCategory() + ")");
		Glide.with(mContext).load(googlePlace.getIcon()).centerCrop().bitmapTransform(new RoundImageTransform(mContext))
				.placeholder(R.drawable.ic_launcher).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL)
				.into(holder.placeIcon);
		

		return view;
	}

}

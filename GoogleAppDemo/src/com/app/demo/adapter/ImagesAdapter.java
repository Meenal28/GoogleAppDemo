package com.app.demo.adapter;

import java.util.List;

import com.app.demo.R;
import com.app.demo.model.PlaceModel.Photos;
import com.app.demo.utils.RoundImageTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImagesAdapter extends BaseAdapter {// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<Photos> photosList = null;
	private String imageUrl = "";

	public ImagesAdapter(Context context, List<Photos> photoList, String url) {
		mContext = context;
		this.photosList = photoList;
		inflater = LayoutInflater.from(mContext);
		imageUrl = url;

	}

	public class ViewHolder {

		ImageView placeIcon;

	}

	@Override
	public int getCount() {
		return photosList.size();
	}

	@Override
	public Photos getItem(int position) {
		return photosList.get(position);
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
			view = inflater.inflate(R.layout.place_image_row, null);
			// Locate the TextViews in listview_item.xml

			holder.placeIcon = (ImageView) view.findViewById(R.id.placeImageThumbnail);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		Photos googlePhoto = photosList.get(position);
		StringBuilder googlePlacesUrl = new StringBuilder(imageUrl);
		googlePlacesUrl.append("&photoreference=" + googlePhoto.getPhotoRefrence());
		googlePlacesUrl.append("&maxwidth=200");
		googlePlacesUrl.append("&key=" + mContext.getString(R.string.google_search_key));

		Glide.with(mContext).load(googlePlacesUrl.toString()).centerCrop().placeholder(R.drawable.ic_launcher)
				.override(200, 200).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.placeIcon);

		return view;
	}
}

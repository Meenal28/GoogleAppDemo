package com.app.demo.network;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import com.app.demo.MapImageActivity;
import com.app.demo.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

public class DownloadFile extends AsyncTask<Void, Integer, Long> {

	private Context mContext;
	private String urlToLoad;
	private static final String downloadFolder = "Demo";
	private int position = 0;
	private ProgressDialog mProgressDialog;

	public DownloadFile(Context mContext, String urlToLoad, int pos) {
		super();
		this.mContext = mContext;
		this.urlToLoad = urlToLoad;
		mProgressDialog = new ProgressDialog(mContext);// Change
														// Mainactivity.this
		this.position = pos; // with your activity
		// name.
	}

	String strFolderName;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mProgressDialog != null && !mProgressDialog.isShowing()) {
			mProgressDialog.setMessage(mContext.getString(R.string.text_downloading));
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setMax(100);
			mProgressDialog.setCancelable(false);
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.show();
		}
	}

	@Override
	protected Long doInBackground(Void... aurl) {
		int count;
		try {
			URL url = new URL(urlToLoad);
			URLConnection conexion = url.openConnection();
			conexion.connect();
			String targetFileName = "image_" + position + ".jpeg";
			int lenghtOfFile = conexion.getContentLength();
			String PATH = Environment.getExternalStorageDirectory() + "/" + downloadFolder + "/";
			File folder = new File(PATH);
			if (!folder.exists()) {
				folder.mkdir();// If there is no folder it will be created.
			}
			InputStream input = new BufferedInputStream(url.openStream());
			OutputStream output = new FileOutputStream(PATH + targetFileName);
			byte data[] = new byte[1024];
			long total = 0;
			while ((count = input.read(data)) != -1) {
				total += count;
				publishProgress((int) (total * 100 / lenghtOfFile));
				output.write(data, 0, count);
			}
			output.flush();
			output.close();
			input.close();
		} catch (Exception e) {
		}
		return null;
	}

	protected void onProgressUpdate(Integer... progress) {
		mProgressDialog.setProgress(progress[0]);
		if (mProgressDialog.getProgress() == mProgressDialog.getMax()) {
			mProgressDialog.dismiss();
			Toast.makeText(mContext, mContext.getString(R.string.file_success_download)+downloadFolder, Toast.LENGTH_SHORT).show();
		}
	}

	protected void onPostExecute(String result) {
	}
}

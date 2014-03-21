package com.example.asynctask;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.activity.StreetQualityActivity;
import com.example.utils.WebUtil;

import android.os.AsyncTask;

public class getDataByStreetIdTask extends AsyncTask<String, Integer, String>{
	private StreetQualityActivity mActivity;
	public getDataByStreetIdTask(StreetQualityActivity activity){
		this.mActivity = activity;
	}

	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		return WebUtil.getDataFromUrl(params);
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		try {
			JSONObject json = new JSONObject(result.toString());
			mActivity.updateCurrentData(json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	

}

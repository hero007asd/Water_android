package com.example.asynctask;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.utils.WebUtil;
import com.example.waterquality.CurrentAreaActivity;

import android.os.AsyncTask;

public class getStreetDataTask extends AsyncTask<String, Integer, String> {
	CurrentAreaActivity mActivity;
	public getStreetDataTask(CurrentAreaActivity activity){
		mActivity = activity;
	}
	
	@Override
	protected String doInBackground(String... params) {
		 return WebUtil.getDataFromUrl(params);
	}
	//获取到结果更新UI
	protected void onPostExecute(String result) {
		try {
			System.out.println(result);
			JSONObject object = new JSONObject(result);
			mActivity.updateCurrentData(object);
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

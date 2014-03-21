package com.example.asynctask;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.example.activity.BaseActivity;
import com.example.utils.WebUtil;

public class getCityDataTask extends AsyncTask<String, Integer, String> {
	BaseActivity mActivity;
	public getCityDataTask(BaseActivity activity){
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
			mActivity.updateCityData(object);
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
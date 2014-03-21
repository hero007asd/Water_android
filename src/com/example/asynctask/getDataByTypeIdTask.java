package com.example.asynctask;

import com.example.activity.TrendActivity;
import com.example.utils.WebUtil;

import android.os.AsyncTask;

public class getDataByTypeIdTask extends AsyncTask<String, Integer, String>{
	private TrendActivity mActivity;

	public getDataByTypeIdTask(TrendActivity activity){
		mActivity = activity;
	}
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		return WebUtil.getDataFromUrl(params);
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		System.out.println(result);
		mActivity.updateXYRender(result.toString());
	}
	

}

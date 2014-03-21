package com.example.asynctask;

import android.os.AsyncTask;

import com.example.activity.StreetsListActivity;
import com.example.utils.WebUtil;

public class getStreetNameTask extends AsyncTask<String, Integer, String> {
	StreetsListActivity mActivity;
	public getStreetNameTask(StreetsListActivity activity){
		mActivity = activity;
	}
	
	@Override
	protected String doInBackground(String... params) {
		 return WebUtil.getDataFromUrl(params);
	}
	//获取到结果更新UI
	protected void onPostExecute(String result) {
			System.out.println(result);
			mActivity.updateList(result);
	}

}

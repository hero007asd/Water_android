package com.example.asynctask;

import android.os.AsyncTask;

import com.example.utils.WebUtil;
import com.example.waterquality.StreetsListActivity;

public class getStreetNameTask extends AsyncTask<String, Integer, String> {
	StreetsListActivity mActivity;
	public getStreetNameTask(StreetsListActivity activity){
		mActivity = activity;
	}
	
	@Override
	protected String doInBackground(String... params) {
		 return WebUtil.getDataFromUrl(params);
	}
	//��ȡ���������UI
	protected void onPostExecute(String result) {
			System.out.println(result);
			mActivity.updateList(result);
	}

}

package com.example.waterquality;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.asynctask.getCityDataTask;
import com.example.utils.WebUtil;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.TextView;

public class BaseActivity extends Activity{
	private TextView cityZDTxt,cityPHTxt,cityYLTxt,cityDDLTxt,cityRJYTxt;
	public  String cityName;
	public void getCityData(){
		SharedPreferences sp = getSharedPreferences("city", Context.MODE_PRIVATE);
		cityName = sp.getString("cityName", "");
		
		getCityDataTask cityTask = new getCityDataTask(this);
		JSONObject json = new JSONObject();
		try {
			json.put("city", cityName);
			cityTask.execute(WebUtil.COMMON_URL + "showOverview/",
					json.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void updateCityData(JSONObject json){
		cityZDTxt = (TextView) findViewById(R.id.cityZDTxtView);
		cityPHTxt = (TextView) findViewById(R.id.cityPHTxtView);
		cityYLTxt = (TextView) findViewById(R.id.cityYLTxtView);
		cityDDLTxt = (TextView) findViewById(R.id.cityDDLTxtView);
		cityRJYTxt = (TextView) findViewById(R.id.cityRJYTxtView);
		try {
			isNormal(json.get("turbidity_status").toString(),cityZDTxt,json.get("ov_turbidity").toString());
			isNormal(json.get("ph_status").toString(),cityPHTxt,json.get("ov_ph").toString());
			isNormal(json.get("rc_status").toString(),cityYLTxt,json.get("ov_rc").toString());
			isNormal(json.get("conductivity_status").toString(),cityDDLTxt,json.get("ov_conductivity").toString());
			isNormal(json.get("do_status").toString(),cityRJYTxt,json.get("ov_DO").toString());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private void isNormal(String status,TextView tv,String value){
		if(status.equals("0")){
			tv.setTextColor(Color.RED);
		}
		else{
			tv.setTextColor(Color.GREEN);
		}
		tv.setText(value);
		
	}
}

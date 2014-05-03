package com.example.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.asynctask.getCityDataTask;
import com.example.asynctask.getStreetDataTask;
import com.example.utils.ShowUtil;
import com.example.utils.StatusUtil;
import com.example.utils.WebUtil;
import com.example.activity.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CurrentAreaActivity extends BaseActivity implements
		OnClickListener {
	private LinearLayout zdLayout, phLayout, ylLayout, ddlLayout, rjyLayout,
			orpLayout;
	private TextView curZDTxt, curPHTxt, curYLTxt, curDDLTxt, curRJYTxt,
			curORPTxt, curZDStatus, curPHStatus, curYLStatus, curDDLStatus,
			curRJYStatus, curORPStatus;
	private TextView areaNameTxt, companyNameTxt, phoneTxt, titleText;
	private Button dialBtn, backBtn;
	private String address, number, areaName, streetName;

	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	private getStreetDataTask curTask = null;
	private getCityDataTask cityTask = null;
	private boolean flag = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_current_area);

		zdLayout = (LinearLayout) findViewById(R.id.zdLayout);
		phLayout = (LinearLayout) findViewById(R.id.phLayout);
		ylLayout = (LinearLayout) findViewById(R.id.ylLayout);
		ddlLayout = (LinearLayout) findViewById(R.id.ddlLayout);
		rjyLayout = (LinearLayout) findViewById(R.id.rjyLayout);
		orpLayout = (LinearLayout) findViewById(R.id.orpLayout);
		areaNameTxt = (TextView) this.findViewById(R.id.areaNameTextView);
		companyNameTxt = (TextView) this.findViewById(R.id.companyTextView);
		phoneTxt = (TextView) this.findViewById(R.id.phoneTextView);
		dialBtn = (Button) this.findViewById(R.id.phoneButton);
		backBtn = (Button) this.findViewById(R.id.backBtn);
		backBtn.setVisibility(View.INVISIBLE);
		titleText = (TextView) this.findViewById(R.id.titleTxt);
		titleText.setText("当前自来水水质");

		curZDTxt = (TextView) this.findViewById(R.id.zdTextView);
		curPHTxt = (TextView) this.findViewById(R.id.phTextView);
		curYLTxt = (TextView) this.findViewById(R.id.ylTextView);
		curDDLTxt = (TextView) this.findViewById(R.id.ddlTextView);
		curRJYTxt = (TextView) this.findViewById(R.id.rjyTextView);
		curORPTxt = (TextView) this.findViewById(R.id.orpTextView);
		curZDStatus = (TextView) this.findViewById(R.id.zdNormalTextView);
		curPHStatus = (TextView) this.findViewById(R.id.phNormalTextView);
		curYLStatus = (TextView) this.findViewById(R.id.ylNormalTextView);
		curDDLStatus = (TextView) this.findViewById(R.id.ddlNormalTextView);
		curRJYStatus = (TextView) this.findViewById(R.id.rjyNormalTextView);
		curORPStatus = (TextView) this.findViewById(R.id.orpNormalTextView);

		zdLayout.setOnClickListener(this);
		phLayout.setOnClickListener(this);
		ylLayout.setOnClickListener(this);
		ddlLayout.setOnClickListener(this);
		rjyLayout.setOnClickListener(this);
		orpLayout.setOnClickListener(this);
		dialBtn.setOnClickListener(this);

		// getCityData();
		// location();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(StatusUtil.isNetworkConnected(CurrentAreaActivity.this)){
			location();
		}else{
			ShowUtil.showCheckNet(CurrentAreaActivity.this);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(mLocationClient!=null){
			mLocationClient.stop();
		}
	}

	private void location() {
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开GPS
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);// 禁止启用缓存定位
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		if (mLocationClient != null && mLocationClient.isStarted())
			mLocationClient.requestLocation();
		else
			Log.d("LocSDK3", "locClient is null or not started");

	}

	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			address = location.getAddrStr();
			if (address == null)
				return;
			areaName = address.substring(address.indexOf("市") + 1,
					address.indexOf("区") + 1);
			streetName = address.substring(address.indexOf("区") + 1,
					address.length());
			areaNameTxt.setText(address);

			curTask = new getStreetDataTask(CurrentAreaActivity.this);
			JSONObject json = new JSONObject();
			try {
				//json.put("address", areaName);
				json.put("address", address);
				curTask.execute(WebUtil.COMMON_URL + "showCurOverview/",
						json.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
			// 获取城市监测数据
			cityName = location.getCity();

			SharedPreferences sp = getSharedPreferences("city",
					Context.MODE_PRIVATE);
			String cityNameStr = sp.getString("cityName", "");
			Editor editor = sp.edit();
			// 第一次存放值
			if (cityNameStr == null || cityNameStr.equals("")) {
				editor.putString("cityName", cityName);
			}
			// 如果城市名称改变则重新提交
			else if (!cityNameStr.equals(cityName)) {
				editor.putString("cityName", cityName);
			}
			editor.commit();
			getCityData();

		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub

		}

	}

	public void updateCurrentData(JSONObject object) {
		try {
			
			companyNameTxt.setText("供水单位:"
					+ object.getString("water_work_name").toString());
			number = object.getString("water_work_phone").toString();
			phoneTxt.setText("联系电话:" + number);

			if (object.getString("cur_turbidity").toString().equals("0.0")
					&& object.getString("cur_ph").toString().equals("0.0")
					&& object.getString("cur_rc").toString().equals("0.0")
					&& object.getString("cur_conductivity").toString()
							.equals("0.0")
					&& object.getString("cur_DO").toString().equals("0.0")
					&& object.getString("cur_orp").toString().equals("0.0"))//六项数据全为0.0
			{
				
				flag = false;
			}
			if (flag) {
				isNormal(object.getString("turbidity_status"),curZDTxt,curZDStatus);
				isNormal(object.getString("ph_status"), curPHTxt,curPHStatus);
				isNormal(object.getString("rc_status"), curYLTxt,curYLStatus);
				isNormal(object.getString("conductivity_status"),curDDLTxt,curDDLStatus);
				isNormal(object.getString("do_status"), curRJYTxt,curRJYStatus);
				isNormal(object.getString("orp_status"),curORPTxt,curORPStatus);
				
				
				curZDTxt.setText(object.getString("cur_turbidity").toString());
				curPHTxt.setText(object.getString("cur_ph").toString());
				curYLTxt.setText(object.getString("cur_rc").toString());
				curDDLTxt.setText(object.getString("cur_conductivity").toString());
				curRJYTxt.setText(object.getString("cur_DO").toString());
				curORPTxt.setText(object.getString("cur_orp").toString());
			} else {
				curZDStatus.setVisibility(View.GONE);
				curPHStatus.setVisibility(View.GONE);
				curYLStatus.setVisibility(View.GONE);
				curDDLStatus.setVisibility(View.GONE);
				curRJYStatus.setVisibility(View.GONE);
				curORPStatus.setVisibility(View.GONE);
				
				curZDTxt.setTextSize(TypedValue.COMPLEX_UNIT_DIP ,20);
				curPHTxt.setTextSize(TypedValue.COMPLEX_UNIT_DIP ,20);
				curYLTxt.setTextSize(TypedValue.COMPLEX_UNIT_DIP ,20);
				curDDLTxt.setTextSize(TypedValue.COMPLEX_UNIT_DIP ,20);
				curRJYTxt.setTextSize(TypedValue.COMPLEX_UNIT_DIP ,20);
				curORPTxt.setTextSize(TypedValue.COMPLEX_UNIT_DIP ,20);
				
				curZDTxt.setText("尚未开通");
				curPHTxt.setText("尚未开通");
				curYLTxt.setText("尚未开通");
				curDDLTxt.setText("尚未开通");
				curRJYTxt.setText("尚未开通");
				curORPTxt.setText("尚未开通");
				
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	private void isNormal(String status, TextView tv,TextView tvStatus) {
		if (status.equals("0")) {
			tv.setTextColor(Color.RED);
			tvStatus.setTextColor(Color.RED);
			tvStatus.setText("超标");
		} else {
			tv.setTextColor(Color.BLACK);
			tvStatus.setTextColor(Color.BLACK);
			tvStatus.setText("合格");
		}

	}


	@Override
	public void onClick(View v) {
		if(!StatusUtil.isNetworkConnected(CurrentAreaActivity.this)){
			ShowUtil.showCheckNet(CurrentAreaActivity.this);
			return;
		}
		Intent i = new Intent();
		Bundle b = new Bundle();
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.zdLayout:
			i.setClass(CurrentAreaActivity.this, TrendActivity.class);
			TrendActivity.isCurrentArea = true;
			b.putString("value_type", "2");// 浊度 2
			b.putString("area_name", areaName);
			b.putString("street_name", streetName);
			i.putExtras(b);
			startActivity(i);
			break;
		case R.id.phLayout:
			i.setClass(CurrentAreaActivity.this, TrendActivity.class);
			TrendActivity.isCurrentArea = true;
			b.putString("value_type", "1");// PH 2
			b.putString("area_name", areaName);
			b.putString("street_name", streetName);
			i.putExtras(b);
			startActivity(i);
			break;
		case R.id.ylLayout:
			i.setClass(CurrentAreaActivity.this, TrendActivity.class);
			TrendActivity.isCurrentArea = true;
			b.putString("value_type", "5");// 余氯 5
			b.putString("area_name", areaName);
			b.putString("street_name", streetName);
			i.putExtras(b);
			startActivity(i);
			break;
		case R.id.ddlLayout:
			i.setClass(CurrentAreaActivity.this, TrendActivity.class);
			TrendActivity.isCurrentArea = true;
			b.putString("value_type", "3");// 电导率 3
			b.putString("area_name", areaName);
			b.putString("street_name", streetName);
			i.putExtras(b);
			startActivity(i);
			break;
		case R.id.rjyLayout:
			i.setClass(CurrentAreaActivity.this, TrendActivity.class);
			TrendActivity.isCurrentArea = true;
			b.putString("value_type", "4");// 溶解氧 4
			b.putString("area_name", areaName);
			b.putString("street_name", streetName);
			i.putExtras(b);
			startActivity(i);
			break;
		case R.id.orpLayout:
			i.setClass(CurrentAreaActivity.this, TrendActivity.class);
			TrendActivity.isCurrentArea = true;
			b.putString("value_type", "6");// ORP 5
			b.putString("area_name", areaName);
			b.putString("street_name", streetName);
			i.putExtras(b);
			startActivity(i);
			break;
		case R.id.phoneButton:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("确定呼叫" + number + "?")
					.setCancelable(false)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									Uri telUri = Uri.parse("tel:" + number);
									Intent intent = new Intent(
											Intent.ACTION_DIAL, telUri);
									startActivity(intent);
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		default:
			break;
		}

	}

	public void onDestroy() {
		mLocationClient.stop();
		super.onDestroy();
	}

}

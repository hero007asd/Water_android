package com.example.waterquality;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.asynctask.getDataByStreetIdTask;
import com.example.utils.WebUtil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StreetQualityActivity extends BaseActivity implements
		OnClickListener {

	private LinearLayout zdLayout, phLayout, ylLayout, ddlLayout, rjyLayout;
	private TextView curZDTxt, curPHTxt, curYLTxt, curDDLTxt, curRJYTxt,
			curZDStatus, curPHStatus, curYLStatus, curDDLStatus, curRJYStatus;
	private TextView areaNameTxt, companyNameTxt, phoneTxt, titleText;
	private Button dialBtn, backBtn;
	private String streetName, streetId, areaId, number;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_current_area);
		getCityData();

		zdLayout = (LinearLayout) findViewById(R.id.zdLayout);
		phLayout = (LinearLayout) findViewById(R.id.phLayout);
		ylLayout = (LinearLayout) findViewById(R.id.ylLayout);
		ddlLayout = (LinearLayout) findViewById(R.id.ddlLayout);
		rjyLayout = (LinearLayout) findViewById(R.id.rjyLayout);
		areaNameTxt = (TextView) this.findViewById(R.id.areaNameTextView);
		companyNameTxt = (TextView) this.findViewById(R.id.companyTextView);
		phoneTxt = (TextView) this.findViewById(R.id.phoneTextView);
		dialBtn = (Button) this.findViewById(R.id.phoneButton);
		backBtn = (Button) this.findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		titleText = (TextView) this.findViewById(R.id.titleTxt);
		titleText.setText("街道数据");

		curZDTxt = (TextView) this.findViewById(R.id.zdTextView);
		curPHTxt = (TextView) this.findViewById(R.id.phTextView);
		curYLTxt = (TextView) this.findViewById(R.id.ylTextView);
		curDDLTxt = (TextView) this.findViewById(R.id.ddlTextView);
		curRJYTxt = (TextView) this.findViewById(R.id.rjyTextView);
		curZDStatus = (TextView) this.findViewById(R.id.zdNormalTextView);
		curPHStatus = (TextView) this.findViewById(R.id.phNormalTextView);
		curYLStatus = (TextView) this.findViewById(R.id.ylNormalTextView);
		curDDLStatus = (TextView) this.findViewById(R.id.ddlNormalTextView);
		curRJYStatus = (TextView) this.findViewById(R.id.rjyNormalTextView);

		zdLayout.setOnClickListener(this);
		phLayout.setOnClickListener(this);
		ylLayout.setOnClickListener(this);
		ddlLayout.setOnClickListener(this);
		rjyLayout.setOnClickListener(this);
		dialBtn.setOnClickListener(this);

		streetName = getIntent().getExtras().getString("streetName").toString();
		streetId = getIntent().getExtras().getString("streetId").toString();
		areaId = getIntent().getExtras().getString("area_id").toString();
		areaNameTxt.setText("采样区域:" + streetName);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getCityData();
		// 获取当前街道监测数据
		getDataByStreetIdTask task = new getDataByStreetIdTask(this);
		JSONObject json = new JSONObject();
		try {
			json.put("street_id", streetId);
			task.execute(WebUtil.COMMON_URL + "showCurOverview/",
					json.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	public void updateCurrentData(JSONObject object) {
		try {
			curZDTxt.setText(object.getString("cur_turbidity").toString());
			curPHTxt.setText(object.getString("cur_ph").toString());
			curYLTxt.setText(object.getString("cur_rc").toString());
			curDDLTxt.setText(object.getString("cur_conductivity").toString());
			curRJYTxt.setText(object.getString("cur_DO").toString());
			companyNameTxt.setText("供水单位:"
					+ object.getString("water_work_name").toString());
			number = object.getString("water_work_phone").toString();
			phoneTxt.setText("联系电话:" + number);

			isNormal(object.getString("turbidity_status"), curZDStatus);
			isNormal(object.getString("ph_status"), curPHStatus);
			isNormal(object.getString("rc_status"), curYLStatus);
			isNormal(object.getString("conductivity_status"), curDDLStatus);
			isNormal(object.getString("do_status"), curRJYStatus);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void isNormal(String status, TextView tv) {
		if (status.equals("0")) {
			tv.setTextColor(Color.RED);
			tv.setText("不合格");
		} else {
			tv.setTextColor(Color.GREEN);
			tv.setText("合格");
		}

	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent();
		Bundle b = new Bundle();
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.zdLayout:
			i.setClass(StreetQualityActivity.this, TrendActivity.class);
			TrendActivity.isCurrentArea = false;
			b.putString("value_type", "2");// 浊度 2
			b.putString("area_id", areaId);
			b.putString("street_id", streetId);
			i.putExtras(b);
		//	finish();
			startActivity(i);
			break;
		case R.id.phLayout:
			i.setClass(StreetQualityActivity.this, TrendActivity.class);
			TrendActivity.isCurrentArea = true;
			b.putString("value_type", "1");// PH 2
			b.putString("area_id", areaId);
			b.putString("street_id", streetId);
			i.putExtras(b);
		//	finish();
			startActivity(i);
			break;
		case R.id.ylLayout:
			i.setClass(StreetQualityActivity.this, TrendActivity.class);
			TrendActivity.isCurrentArea = true;
			b.putString("value_type", "5");// 余氯 5
			b.putString("area_id", areaId);
			b.putString("street_id", streetId);
			i.putExtras(b);
		//	finish();
			startActivity(i);
			break;
		case R.id.ddlLayout:
			i.setClass(StreetQualityActivity.this, TrendActivity.class);
			TrendActivity.isCurrentArea = true;
			b.putString("value_type", "3");// 电导率 3
			b.putString("area_id", areaId);
			b.putString("street_id", streetId);
			i.putExtras(b);
		//	finish();
			startActivity(i);
			break;
		case R.id.rjyLayout:
			i.setClass(StreetQualityActivity.this, TrendActivity.class);
			TrendActivity.isCurrentArea = true;
			b.putString("value_type", "4");// 溶解氧 4
			b.putString("area_id", areaId);
			b.putString("street_id", streetId);
			i.putExtras(b);
	//		finish();
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
		case R.id.backBtn:
			/*i.setClass(StreetQualityActivity.this, StreetsListActivity.class);
			b.putString("area_id", areaId);
			i.putExtras(b);
			finish();
			startActivity(i);*/
			StreetQualityActivity.this.onBackPressed();
			break;
		default:
			break;
		}

	}

}

package com.example.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.asynctask.getDataByStreetIdTask;
import com.example.utils.ShowUtil;
import com.example.utils.StatusUtil;
import com.example.utils.WebUtil;
import com.example.activity.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StreetQualityActivity extends BaseActivity implements
		OnClickListener {

	private LinearLayout zdLayout, phLayout, ylLayout, ddlLayout, rjyLayout,
			orpLayout;
	private TextView curZDTxt, curPHTxt, curYLTxt, curDDLTxt, curRJYTxt,
			curORPTxt, curZDStatus, curPHStatus, curYLStatus, curDDLStatus,
			curRJYStatus, curORPStatus;
	private TextView areaNameTxt, companyNameTxt, phoneTxt, titleText;
	private Button dialBtn, backBtn;
	private String streetName, streetId, areaId, number, areaName;

	private boolean flag = true;

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
		orpLayout = (LinearLayout) findViewById(R.id.orpLayout);
		areaNameTxt = (TextView) this.findViewById(R.id.areaNameTextView);
		companyNameTxt = (TextView) this.findViewById(R.id.companyTextView);
		phoneTxt = (TextView) this.findViewById(R.id.phoneTextView);
		dialBtn = (Button) this.findViewById(R.id.phoneButton);
		backBtn = (Button) this.findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		titleText = (TextView) this.findViewById(R.id.titleTxt);
		titleText.setText("自来水水质");

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

		streetName = getIntent().getExtras().getString("streetName").toString();
		streetId = getIntent().getExtras().getString("streetId").toString();
		areaId = getIntent().getExtras().getString("area_id").toString();
		areaName = getIntent().getExtras().getString("area_name").toString();
		areaNameTxt.setText(streetName);

	}

	@Override
	protected void onResume() {
		super.onResume();
		getCityData();
		// 获取当前街道监测数据
		getDataByStreetIdTask task = new getDataByStreetIdTask(this);
		JSONObject json = new JSONObject();
		try {
			json.put("street_id", streetId);
			task.execute(WebUtil.COMMON_URL + "showStreetOverview/",
					json.toString());
		} catch (JSONException e) {
			e.printStackTrace();
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
		if(!StatusUtil.isNetworkConnected(StreetQualityActivity.this)){
			ShowUtil.showCheckNet(StreetQualityActivity.this);
			return;
		}
		Intent i = new Intent();
		Bundle b = new Bundle();
		switch (v.getId()) {
		case R.id.zdLayout:
			i.setClass(StreetQualityActivity.this, TrendActivity.class);
			TrendActivity.isCurrentArea = false;
			b.putString("value_type", "2");// 浊度 2
			b.putString("area_id", areaId);
			b.putString("street_id", streetId);
			b.putString("area_name", areaName);
			b.putString("street_name", streetName);
			i.putExtras(b);
			startActivity(i);
			break;
		case R.id.phLayout:
			i.setClass(StreetQualityActivity.this, TrendActivity.class);
			TrendActivity.isCurrentArea = false;
			b.putString("value_type", "1");// PH 2
			b.putString("area_id", areaId);
			b.putString("street_id", streetId);
			b.putString("area_name", areaName);
			b.putString("street_name", streetName);
			i.putExtras(b);
			startActivity(i);
			break;
		case R.id.ylLayout:
			i.setClass(StreetQualityActivity.this, TrendActivity.class);
			TrendActivity.isCurrentArea = false;
			b.putString("value_type", "5");// 余氯 5
			b.putString("area_id", areaId);
			b.putString("street_id", streetId);
			b.putString("area_name", areaName);
			b.putString("street_name", streetName);
			i.putExtras(b);
			startActivity(i);
			break;
		case R.id.ddlLayout:
			i.setClass(StreetQualityActivity.this, TrendActivity.class);
			TrendActivity.isCurrentArea = false;
			b.putString("value_type", "3");// 电导率 3
			b.putString("area_id", areaId);
			b.putString("street_id", streetId);
			b.putString("area_name", areaName);
			b.putString("street_name", streetName);
			i.putExtras(b);
			startActivity(i);
			break;
		case R.id.rjyLayout:
			i.setClass(StreetQualityActivity.this, TrendActivity.class);
			TrendActivity.isCurrentArea = false;
			b.putString("value_type", "4");// 溶解氧 4
			b.putString("area_id", areaId);
			b.putString("street_id", streetId);
			b.putString("area_name", areaName);
			b.putString("street_name", streetName);
			i.putExtras(b);
			startActivity(i);
			break;
		case R.id.orpLayout:
			i.setClass(StreetQualityActivity.this, TrendActivity.class);
			TrendActivity.isCurrentArea = false;
			b.putString("value_type", "6");// ORP 6
			b.putString("area_id", areaId);
			b.putString("street_id", streetId);
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
		case R.id.backBtn:
			StreetQualityActivity.this.onBackPressed();
			break;
		default:
			break;
		}

	}

}

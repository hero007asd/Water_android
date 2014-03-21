package com.example.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.asynctask.getStreetNameTask;
import com.example.utils.WebUtil;
import com.example.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class StreetsListActivity extends BaseActivity implements
		OnItemClickListener, OnClickListener {
	private ListView streetList;
	public List<Map<String, Object>> listItems;
	private String areaId;
	private Button backBtn;
	private TextView titleText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_streets_list);
		streetList = (ListView) this.findViewById(R.id.streetsList);
		backBtn = (Button) this.findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		titleText = (TextView) this.findViewById(R.id.titleTxt);
		titleText.setText("Ω÷µ¿¡–±Ì");
		streetList.setOnItemClickListener(this);

		Bundle b = getIntent().getExtras();
		areaId = b.getString("area_id");

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getCityData();

		listItems = new ArrayList<Map<String, Object>>();
		getStreetNameTask task = new getStreetNameTask(this);
		JSONObject json = new JSONObject();
		try {
			json.put("area_id", areaId);
			task.execute(WebUtil.COMMON_URL + "getStreets/", json.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateList(String result) {
		try {
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject json = array.getJSONObject(i);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("streetId", json.get("street_id").toString());
				map.put("streetName", json.get("street_name").toString());
				listItems.add(map);
			}
			SimpleAdapter adapter = new SimpleAdapter(this, listItems,
					R.layout.street_list_item, new String[] { "streetName" },
					new int[] { R.id.streetName });
			streetList.setAdapter(adapter);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		String IdStr = null;
		// TODO Auto-generated method stub
		IdStr = listItems.get(arg2).get("streetId").toString();
		String nameStr = listItems.get(arg2).get("streetName").toString();
		Intent i = new Intent();
		i.setClass(StreetsListActivity.this, StreetQualityActivity.class);
		Bundle b = new Bundle();
		b.putString("streetId", IdStr);
		b.putString("streetName", nameStr);
		b.putString("area_id", areaId);
		i.putExtras(b);
		startActivity(i);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backBtn) {
			/*
			 * Intent i = new Intent(); i.setClass(StreetsListActivity.this,
			 * OtherAreaActivity.class); startActivity(i);
			 */
			StreetsListActivity.this.onBackPressed();
		}
	}

}

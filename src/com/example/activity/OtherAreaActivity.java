package com.example.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.asynctask.getCityDataTask;
import com.example.asynctask.getStreetNameTask;
import com.example.utils.ShowUtil;
import com.example.utils.StatusUtil;
import com.example.utils.WebUtil;
import com.example.activity.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class OtherAreaActivity extends BaseActivity implements OnClickListener{
	private getCityDataTask cityTask;
	private Button[] btnArr = new Button[17];
	private int BASE_NUM = 310102;
	private int buttonNum = 17;
	private Button backBtn;
	private TextView titleText;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_area);
		initButton();
		backBtn = (Button) this.findViewById(R.id.backBtn);
		backBtn.setVisibility(View.INVISIBLE);
		titleText = (TextView) this.findViewById(R.id.titleTxt);
		titleText.setText("∆‰À˚«¯”Ú");
		backBtn.setVisibility(View.INVISIBLE);
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		if(StatusUtil.isNetworkConnected(OtherAreaActivity.this)){
			getCityData();
		}else{
			ShowUtil.showCheckNet(OtherAreaActivity.this);
		}	
	}
	private void initButton(){
		btnArr[0] = (Button) this.findViewById(R.id.hpBtn);
		btnArr[1] = (Button) this.findViewById(R.id.lwBtn);
		btnArr[2] = (Button) this.findViewById(R.id.xhBtn);
		btnArr[3] = (Button) this.findViewById(R.id.cnBtn);
		btnArr[4] = (Button) this.findViewById(R.id.jaBtn);
		btnArr[5] = (Button) this.findViewById(R.id.ptBtn);
		btnArr[6] = (Button) this.findViewById(R.id.zbBtn);
		btnArr[7] = (Button) this.findViewById(R.id.hkBtn);
		btnArr[8] = (Button) this.findViewById(R.id.ypBtn);
		btnArr[9] = (Button) this.findViewById(R.id.mhBtn);
		btnArr[10] = (Button) this.findViewById(R.id.bsBtn);
		btnArr[11] = (Button) this.findViewById(R.id.jdBtn);
		btnArr[12] = (Button) this.findViewById(R.id.pdBtn);
		btnArr[13] = (Button) this.findViewById(R.id.jsBtn);
		btnArr[14] = (Button) this.findViewById(R.id.sjBtn);
		btnArr[15] = (Button) this.findViewById(R.id.qpBtn);
		btnArr[16] = (Button) this.findViewById(R.id.fxBtn);
		for(int i=0; i<buttonNum; i++){
			btnArr[i].setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		if(!StatusUtil.isNetworkConnected(OtherAreaActivity.this)){
			ShowUtil.showCheckNet(OtherAreaActivity.this);
			return;
		}
		int area_id = 0;
		String area_name = null;
		int id = v.getId();
		for(int i=0; i<buttonNum; i++){
			if(id == btnArr[i].getId()){
				area_id = BASE_NUM + i;
				area_name = btnArr[i].getText().toString();
				break;
			}
		}
		Intent i = new Intent();
		i.setClass(OtherAreaActivity.this, StreetsListActivity.class);
		Bundle b = new Bundle();
		b.putString("area_id", area_id+"");
		b.putString("area_name",area_name);
		i.putExtras(b);
		startActivity(i);
	}
	
	

}

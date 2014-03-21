package com.example.activity;

import com.example.activity.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;


public class MainActivity extends TabActivity implements OnCheckedChangeListener {
	TabHost tabHost;
	TabHost.TabSpec tabSpec;
	RadioGroup radioGroup;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        tabHost = getTabHost();
		tabHost.addTab(tabHost.newTabSpec("currentArea").setIndicator("currentArea")
				.setContent(new Intent(this, CurrentAreaActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("otherArea").setIndicator("otherArea")
				.setContent(new Intent(this, OtherAreaActivity.class)));
		
		radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
		radioGroup.setOnCheckedChangeListener(this);
        
    }
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.radio_current:
			tabHost.setCurrentTabByTag("currentArea");
			break;
		case R.id.radio_other:
			tabHost.setCurrentTabByTag("otherArea");
			break;
		}
	}

}

package com.example.activity;

import java.util.Timer;
import java.util.TimerTask;

import com.example.activity.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class WelcomeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        goToMainActivity(this);
    }
    public static void goToMainActivity(final Activity context){
    	final Timer timer = new Timer();
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				Intent intent = new Intent(context,
						MainActivity.class);
				context.startActivity(intent);
				timer.cancel();
				context.finish();
			}
		};
		
		timer.schedule(timerTask, 3000);
    }
}

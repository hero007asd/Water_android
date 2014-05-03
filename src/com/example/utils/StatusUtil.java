package com.example.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author ASUS
 * @description ×´Ì¬°ïÖúÀà
 */
public class StatusUtil{
	/**
	 * ÅÐ¶ÏÍøÂç×´Ì¬
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
}

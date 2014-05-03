/**
 * 
 */
package com.example.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @author ASUS
 *
 */
public class ShowUtil {
	public static void toastLong(Context ctx,String msg){
		Toast.makeText(ctx,msg, Toast.LENGTH_LONG).show();
	}
	
	public static void toastShort(Context ctx,String msg){
		Toast.makeText(ctx,msg, Toast.LENGTH_SHORT).show();
	}
	
	public static void showCheckNet(Context ctx){
		Toast.makeText(ctx,"Çë¼ì²éÍøÂç", Toast.LENGTH_SHORT).show();
	}
}

package com.example.activity;

import java.util.Timer;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.asynctask.getDataByTypeIdTask;
import com.example.utils.WebUtil;
import com.example.activity.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TrendActivity extends Activity implements OnClickListener {
	private String title = "当日趋势";
	private XYSeries series;
	private XYMultipleSeriesRenderer render;
	private XYMultipleSeriesDataset dataset;
	private View chart;
	private Timer timer = new Timer();
	public static boolean isCurrentArea;
	private LinearLayout layout;
	private String areaId, streetId, valueType, areaName, streetName;
	private Button backBtn;
	private TextView titleText,addressText,normalTitleText,normalValueText,currentValueText,passNameText,averageText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trend);
		layout = (LinearLayout) findViewById(R.id.achartLayout);
		backBtn = (Button) this.findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		titleText = (TextView) this.findViewById(R.id.titleTxt);
		addressText  = (TextView) this.findViewById(R.id.addressText);
		normalTitleText = (TextView) this.findViewById(R.id.normalTitleText);
		normalValueText = (TextView) this.findViewById(R.id.normalValueText);
		currentValueText = (TextView) this.findViewById(R.id.currentValueText);
		passNameText = (TextView) this.findViewById(R.id.nameText);
		averageText = (TextView) this.findViewById(R.id.averageText);
		
		LinearLayout includeLayout = (LinearLayout) findViewById(R.id.layout_top);
		layout.setBackgroundColor(Color.WHITE);
		series = new XYSeries(title);

		Bundle b = getIntent().getExtras();
		valueType = b.getString("value_type").toString();
		switch (Integer.parseInt(valueType)){
		case 1:
			titleText.setText("自来水PH值");
			normalTitleText.setText("PH正常值范围");
			normalValueText.setText("6.5-8.5");
			break;
		case 2:
			titleText.setText("自来水浊度");
			normalTitleText.setText("浊度正常值范围");
			normalValueText.setText("小于0.5 NTU");
			break;
		case 3:
			titleText.setText("自来水电导率");
			normalTitleText.setText("电导率正常值范围");
			normalValueText.setText("125-1250 us/cm");
			break;
		case 4:
			titleText.setText("自来水溶解氧");
			normalTitleText.setText("溶解氧正常值范围");
			normalValueText.setText("大于6 mg/L");
			break;
		case 5:
			titleText.setText("自来水余氯");
			normalTitleText.setText("余氯正常值范围");
			normalValueText.setText("0.3-0.5 mg/L");
			break;
		case 6:
			titleText.setText("ORP");
			normalTitleText.setText("ORP正常值范围");
			normalValueText.setText("150-550 mv");
			break;
		default:
			break;
		
		}
		// 选择街道后跳转过来的
		if (!isCurrentArea) {
			areaId = b.getString("area_id").toString();
			streetId = b.getString("street_id").toString();
			areaName = b.getString("area_name").toString();
		}

		areaName = b.getString("area_name").toString();
		streetName = b.getString("street_name").toString();
		addressText.setText("上海市"+areaName+streetName);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 选择街道后跳转过来的
		if (!isCurrentArea) {
			JSONObject json = new JSONObject();
			try {
				json.put("value_type", valueType);
				json.put("area_id", areaId);
				json.put("street_id", streetId);
				getDataByTypeIdTask task = new getDataByTypeIdTask(this);
				task.execute(WebUtil.COMMON_URL + "showStreetValue/",
						json.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		// 定位页面跳转过来的
		else {
			JSONObject json = new JSONObject();
			try {
				json.put("value_type", valueType);
				json.put("area_name", areaName);
				//json.put("area_name", "浦东新区");
				String streetStr = streetName.substring(0,streetName.indexOf("路")+1);
			//	String streetStr = streetName.substring(0,streetName.length());
				json.put("street_name", streetStr);
				//json.put("street_name", "五洲大道");
				getDataByTypeIdTask task = new getDataByTypeIdTask(this);
				task.execute(WebUtil.COMMON_URL + "showCurStreetValue/",
						json.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
    int yMin,yMax;
	public void updateXYRender(String result) {
		try {
			JSONObject json = new JSONObject(result);
			JSONArray array = json.getJSONArray("value");
			int length = array.length();
			int[] xSer = new int[length];
			double[] ySer = new double[length];
			String Ytitle = null;
			String type = json.getString("type").toString();
			String averageData = null;

			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				xSer[i] = Integer.valueOf(object.getString("time")
						.toString()).intValue();
				
			}
			switch (Integer.valueOf(type).intValue()) {
			case 1:
				Ytitle = "PH值";
				averageData = json.getJSONArray("avgValue").getJSONObject(0).getString("ph").toString();
				for (int i = 0; i < array.length(); i++) {
					JSONObject object = array.getJSONObject(i);
					ySer[i] = Double.parseDouble(object.getString("ph")
							.toString());
					if(i == length -1){
						currentValueText.setText(object.getString("ph")
								.toString());
					}
				}
				yMin = 0;
				yMax = 14;
				break;
			case 2:
				Ytitle = "浊度(NTU)";
				averageData = json.getJSONArray("avgValue").getJSONObject(0).getString("turbidity").toString();
				for (int i = 0; i < array.length(); i++) {
					JSONObject object = array.getJSONObject(i);
					ySer[i] = Double.parseDouble(object.getString("turbidity")
							.toString());
					if(i == length -1){
						currentValueText.setText(object.getString("turbidity")
								.toString());
					}
				}
				yMin = 0;
				yMax = 20;
				break;
			case 3:
				Ytitle = "电导率(us/cm)";
				averageData = json.getJSONArray("avgValue").getJSONObject(0).getString("conductivity").toString();
				for (int i = 0; i < array.length(); i++) {
					JSONObject object = array.getJSONObject(i);
					ySer[i] = Double.parseDouble(object.getString(
							"conductivity").toString());
					if(i == length -1){
						currentValueText.setText(object.getString("conductivity")
								.toString());
					}
				}
				yMin = 1;
				yMax = 2000;
				break;
			case 4:
				Ytitle = "溶解氧(mg/L)";
				averageData = json.getJSONArray("avgValue").getJSONObject(0).getString("DO").toString();
				for (int i = 0; i < array.length(); i++) {
					JSONObject object = array.getJSONObject(i);
					ySer[i] = Double.parseDouble(object.getString("DO")
							.toString());
					if(i == length -1){
						currentValueText.setText(object.getString("DO")
								.toString());
					}
				}
				yMin = 0;
				yMax = 20;
				break;
			case 5:
				Ytitle = "余氯(mg/L)";
				averageData = json.getJSONArray("avgValue").getJSONObject(0).getString("rc").toString();
				for (int i = 0; i < array.length(); i++) {
					JSONObject object = array.getJSONObject(i);
					ySer[i] = Double.parseDouble(object.getString("rc")
							.toString());
					if(i == length -1){
						currentValueText.setText(object.getString("rc")
								.toString());
					}
				}
				yMin = 0;
				yMax = 1;
				break;
			case 6:
				Ytitle = "ORP(mv)";
				averageData = json.getJSONArray("avgValue").getJSONObject(0).getString("orp").toString();
				for (int i = 0; i < array.length(); i++) {
					JSONObject object = array.getJSONObject(i);
					ySer[i] = Double.parseDouble(object.getString("orp")
							.toString());
					if(i == length -1){
						currentValueText.setText(object.getString("orp")
								.toString());
					}
				}
				yMin = -2000;
				yMax = 2000;
				break;
			default:
				break;
			}
			averageText.setText(averageData);
			for (int i = 0; i < length; i++) {
				series.add(xSer[i], ySer[i]);
			}
			dataset = new XYMultipleSeriesDataset();
			dataset.addSeries(series);
			int color = Color.GREEN;
			PointStyle style = PointStyle.CIRCLE;
			render = buildRender(color, style, true);
			this.setChartSettings(render, "时间(时)", Ytitle, 0,  24, yMin, yMax,
					Color.BLACK, Color.BLACK);
			chart = ChartFactory.getLineChartView(this, dataset, render);
			layout.addView(chart, new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		timer.cancel();
		super.onDestroy();
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	protected XYMultipleSeriesRenderer buildRender(int color, PointStyle style,
			boolean flag) {
		XYMultipleSeriesRenderer render = new XYMultipleSeriesRenderer();
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(color);
		r.setPointStyle(style);
		r.setFillPoints(flag);
		r.setLineWidth(3);
		render.addSeriesRenderer(r);
		return render;
	}

	protected void setChartSettings(XYMultipleSeriesRenderer render,
			String xTitle, String yTitile, double xMin, double xMax,
			double yMin, double yMax, int axesColor, int lablesColor) {
		render.setChartTitle(title);
		render.setChartTitleTextSize(24);
		render.setXTitle(xTitle);
		render.setYTitle(yTitile);
		render.setXAxisMin(xMin);
		render.setXAxisMax(xMax);
		render.setYAxisMin(yMin);
		render.setYAxisMax(yMax);
		render.setAxesColor(lablesColor);
		render.setShowGrid(true);
		render.setAxisTitleTextSize(22);
		render.setLabelsTextSize(22);
		render.setXLabels(12);
		render.setYLabels(12);
		render.setLabelsColor(Color.BLACK);
		render.setZoomEnabled(false, false);
		render.setMargins(new int[] { 40, 40, 60, 20 });// 上左下右
		render.setShowLegend(false);
		render.setPanEnabled(false, false);
		render.setBackgroundColor(Color.WHITE);
		render.setMarginsColor(Color.WHITE);
	}

	/*
	 * private void updateChart222(){ java.util.Random r=new java.util.Random();
	 * addX = (int)(r.nextInt()); addY = (int)(r.nextInt());
	 * dataset.removeSeries(series); int length = series.getItemCount() ;
	 * for(int i=0; i<length; i++){ xSer[i] = (int) (series.getX(i) + addX);
	 * if(xSer[i] > 23){ xSer[i] = 23; } ySer[i] = (int) (series.getY(i) +
	 * addY); if(ySer[i] > 110){ ySer[i] = 110; } } series.clear(); for(int k=0;
	 * k<length; k++){ series.add(xSer[k], ySer[k]); }
	 * dataset.addSeries(series); chart.invalidate(); }
	 */

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backBtn) {
			/*
			 * Intent i = new Intent(); if(isCurrentArea){
			 * i.setClass(TrendActivity.this, CurrentAreaActivity.class);
			 * finish(); startActivity(i); } else{
			 * i.setClass(TrendActivity.this, StreetQualityActivity.class);
			 * Bundle b = new Bundle(); b.putString("streetId", streetId);
			 * b.putString("streetName", streetName); b.putString("area_id",
			 * areaId); i.putExtras(b); finish(); startActivity(i); }
			 */
			TrendActivity.this.onBackPressed();
		}

	}
}

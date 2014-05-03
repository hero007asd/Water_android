package com.example.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
/**
 * 
 * @author zhenghong
 * @version 1.0
 * @created 2014-3-2 
 */
public class WebUtil {
	public static final String COMMON_URL = "http://www.wetouching.com:8000/hardsocket/";
//	public static final String COMMON_URL = "http://192.168.0.109:8000/hardsocket/";
	public static String getDataFromUrl(String... params){
		String httpurl = params[0].toString();
		String str = null;  
		// HttpPost���Ӷ���
		try {
			HttpPost httpRequest = new HttpPost(httpurl);
			// ���ò���
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("info", params[1].toString()));
			// �����ַ���,�Լ����뷽ʽ
			HttpEntity httpEntity = new UrlEncodedFormEntity(param, "utf-8");
			// ����HttpRequest
			httpRequest.setEntity(httpEntity);
			// ȡ��Ĭ�ϵ�HttpClient
			HttpClient httpClient = new DefaultHttpClient();    
			// ȡ��HttpResponse
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			// HttpStatus.SC_OK��ʾ���ӳɹ�
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				str = EntityUtils.toString(httpResponse.getEntity());
			}else{
				str = "";
			}
		} catch (Exception ex) {
			str = "";
			ex.printStackTrace();
		}

		return str;
	}
}

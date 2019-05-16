package com.crawl.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sourceforge.tess4j.util.LoggHelper;

@SuppressWarnings("deprecation")
public class HttpUtils {
	private static final Logger logger = LoggerFactory.getLogger(new LoggHelper().toString());
	@SuppressWarnings("resource")
	public static String excutePost(String personid,String url){
	    HttpPost post = null;
	    try {
	        HttpClient httpClient = new DefaultHttpClient();

	        post = new HttpPost(url);
	        // 构造消息头
	        post.addHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
	        post.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
	        // 构建消息实体
	        List<NameValuePair> data=new ArrayList<NameValuePair>();
	        data.add(new BasicNameValuePair("personid", personid));
	        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(data);
	        // 发送Json格式的数据请求
	        entity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
	        post.setEntity(entity);
	            
	        HttpResponse response = httpClient.execute(post);
	        // 检验返回码
	        int statusCode = response.getStatusLine().getStatusCode();
	        if(statusCode != HttpStatus.SC_OK){
	        	
	        	HttpEntity responseEntity = response.getEntity();
            	String jsonString = EntityUtils.toString(responseEntity);
            	logger.info("请求出错: "+statusCode);
            	logger.info("请求出错: "+jsonString);
	            return null;
	        }else{
	        	HttpEntity responseEntity = response.getEntity();
            	String jsonString = EntityUtils.toString(responseEntity);
	        	return jsonString;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }finally{
	        if(post != null){
	            try {
	                post.releaseConnection();
	                Thread.sleep(500);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    }
		return null;
	}
	// 从服务器获得一个输入流(本例是指从服务器获得一个image输入流)
	public static InputStream getInputStream(String URL_PATH) {
		InputStream inputStream = null;
		HttpURLConnection httpURLConnection = null;

		try {
			URL url = new URL(URL_PATH);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			// 设置网络连接超时时间
			httpURLConnection.setConnectTimeout(3000);
			// 设置应用程序要从网络连接读取数据
			httpURLConnection.setDoInput(true);

			httpURLConnection.setRequestMethod("GET");
			int responseCode = httpURLConnection.getResponseCode();
			if (responseCode == 200) {
				// 从服务器返回一个输入流
				inputStream = httpURLConnection.getInputStream();

			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return inputStream;

	}
}

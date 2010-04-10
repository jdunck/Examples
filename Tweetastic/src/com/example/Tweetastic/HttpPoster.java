package com.example.Tweetastic;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class HttpPoster { 
	
	private ArrayList<NameValuePair> pairs; 
	DefaultHttpClient httpclient = new DefaultHttpClient(); 
	HttpPost httppost; 
	
	public HttpPoster(String url) {
		pairs = new ArrayList<NameValuePair>();
		httppost = new HttpPost(url);
	}
	
	public void AddValue(String key, String val) {
		pairs.add(new BasicNameValuePair(key, val));
	}
	
	public void AddHeader(String key, String val) {
		httppost.addHeader(key, val);
	}

    public HttpResponse doPost() 
    throws ClientProtocolException, IOException {
    	HttpParams params = new BasicHttpParams();
    	// fix twitter http error 417 
    	HttpProtocolParams.setUseExpectContinue(params, false);
    	httpclient.setParams(params);
		httppost.setEntity(new UrlEncodedFormEntity(pairs));  
		return httpclient.execute(httppost); 
	} 
}

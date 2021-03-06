package me.anonim1133.zacja.utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

//class SendPostReqAsyncTask extends AsyncTask<String, Void, String>{
public class Api {
	HttpClient httpclient;

	private String ip;
	private String port;

	private String url;
	private String api_key;

	private List<NameValuePair> fields;

	private static final long CONN_MGR_TIMEOUT = 10000;
	private static final int CONN_TIMEOUT = 50000;
	private static final int SO_TIMEOUT = 50000;

	public Api(String ip, String port){
		//Set ip, port
		this.ip = ip;
		this.port = port;

		//Initialize HTTPClient
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 2048; //waiting for connection
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		int timeoutSocket = 4096; //waiting for data
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		httpclient = new DefaultHttpClient(httpParameters);


		fields = new ArrayList<>();
		api_key = "";
	}

	public void setApiKey(String key){
		api_key = key;
	}

	public boolean checkApiKey(String key){
		if(api_key.length() < 0){
			return false;
		}else{
			if(get("LoginCheck") == "false") return false;
			else return true;
		}
	}

	public Api addField(String name, String value){
		fields.add(new BasicNameValuePair(name, value));
		return this;
	}

	public String post(String action){

		HttpPost http = new HttpPost("http://" + ip + ":" + port + "/api/" + action);

		//Header with apikey
		if(api_key.length() > 0)
			http.setHeader("apikey", api_key);

		try {
			http.setEntity(new UrlEncodedFormEntity(fields));
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}

		fields.clear();

		try {
			HttpResponse response = httpclient.execute(http);
			return EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) {
			// Log exception
			e.printStackTrace();
		} catch (IOException e) {
			// Log exception
			e.printStackTrace();
		}

		//if http query didnt return anything, then return "false"
		return "false";
	}

	public String get(String action){

		String url = "http://" + ip + ":" + port + "/api/" + action;

		int count = fields.size();

		if(count > 0){
			for(int i = 0; i < count; i++){
				if(i==0) url += "?" + fields.get(i).getName() + "=" + fields.get(i).getValue();
				else url += "&" + fields.get(i).getName() + "=" + fields.get(i).getValue();
			}
		}

		fields.clear();

		HttpGet http = new HttpGet(url);

		//Header with apikey
		if(api_key.length() > 0)
			http.setHeader("apikey", api_key);

		try {
			HttpResponse response = httpclient.execute(http);
			return EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) {
			// Log exception
			e.printStackTrace();
		} catch (IOException e) {
			// Log exception
			e.printStackTrace();
		}

		//if http query didnt return anything, then return "false"
		return "false";
	}
}
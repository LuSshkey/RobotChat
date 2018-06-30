package com.java.robotchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import android.os.AsyncTask;
import android.util.Log;

/**
 * 异步通信类
 * @author mingyue
 *
 */
public class HttpData extends AsyncTask<String, Void, String> {

	private HttpClient httpClient;
	private HttpGet httpGet;
	private HttpResponse httpResponse; //获取请求返回的数据
	private HttpEntity httpEntity;//创建http实体
	private InputStream in; //将获取到的数据转化为流文件
	
	private HttpGetDataListener listener;//实现自定义的HttpGetDataListener接口,并且构造化传递参数
	
	private String url;
	public HttpData(String url ,HttpGetDataListener listener) {
		this.url = url;
		this.listener = listener;
	}
	
	@Override
	protected String doInBackground(String... params) {//实现接口后重写此方法,此方法的作用是：发送get请求后，获取数据
		try {
			httpClient = new DefaultHttpClient();//实例化客户端
			httpGet =  new HttpGet(url);//使用get方式，通过发送URL来请求
			httpResponse = httpClient.execute(httpGet); //通过客户端发送请求
			httpEntity = httpResponse.getEntity();//通过httpResponse对象获取数据
			
			Log.i("haha1", "----------"+httpEntity.toString());
			
			in = httpEntity.getContent();//获取实体的具体内容
			BufferedReader br = new BufferedReader(new InputStreamReader(in));//获取到具体内容后，通过缓冲区进行读取
			
			Log.i("haha2", "----------"+br.toString());
			
			String line = null; //读取数据
			StringBuffer sb = new StringBuffer();//储存所有数据
			while((line  = br.readLine()) != null){ //读取缓冲区的数据
				sb.append(line); //存储数据到StringBuffer中
			}
			
			Log.i("haha3", "----------"+sb.toString());
			
			return sb.toString();//转换为String类型
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	/**？？
	 * 重写此方法，通过这方法获取数据
	 */
	@Override
	protected void onPostExecute(String result) {
		listener.getDataUrl(result);//返回数据
		
		Log.i("haha4", "----------"+result);
		
		super.onPostExecute(result);
	}
	
	

}

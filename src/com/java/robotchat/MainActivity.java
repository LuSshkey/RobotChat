package com.java.robotchat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity implements
		HttpGetDataListener,OnClickListener {

	private HttpData httpData;// 异步请求对象
	private List<ListData> lists;
	private ListView lv;
	private EditText et_sendText;
	private Button btn_send;
	private String content_str; // 存儲在EditText獲取到的數據
	private TextAdapter adapter;
	private String[] welcome_arry;//欢迎语
	private double currentTime,oldTime = 0;//对话时间
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.i("haha7", "----------7");	
		initView();
		Log.i("haha8", "----------8");
	}

	/**
	 * 实例化方法
	 */
	private void initView(){
		lists = new ArrayList<ListData>();
		lv = (ListView) findViewById(R.id.lv);
		et_sendText = (EditText) findViewById(R.id.et_sendText);
		btn_send = (Button) findViewById(R.id.btn_send);
		btn_send.setOnClickListener(this);
		adapter = new TextAdapter(lists, this);
		lv.setAdapter(adapter);//绑定adapter
		ListData listData;
		listData = new ListData(getRandomWelcomeTips(),ListData.RECEIVE, getTime());
		lists.add(listData);//添加欢迎语
	}

	/**
	 * 设置欢迎语
	 */
	public String getRandomWelcomeTips(){
		String welcome_tip = null;
		welcome_arry = this.getResources().getStringArray(R.array.welcome_tips);//从string.xml中获取名为welcome_tips的字符串数组
		int index = (int)(Math.random()*(welcome_arry.length - 1));//获取一个随机数
		welcome_tip = welcome_arry[index];
		return welcome_tip;
	}
	
	/**
	 * 设置时间
	 * @return 
	 */
	private String getTime(){
		currentTime = System.currentTimeMillis();
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
		Date curDate = new Date();
		String str = format.format(curDate);
		if(currentTime - oldTime >= 5*60*1000){//如果超过5分钟，显示时间
			oldTime = currentTime;
			return str;
		}else{
			return "";
		}
	}
	
	
	@Override
	public void getDataUrl(String data) {
		Log.i("haha---data=","------" + data);
		parseText(data);
	}
	
	public void parseText(String str){//解析json
		try {
			JSONObject jb = new JSONObject(str);
			ListData listData;
			listData = new ListData(jb.getString("text"),ListData.RECEIVE, getTime());
			lists.add(listData);
			adapter.notifyDataSetChanged();//重新適配？？
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		getTime();//调用时间
		content_str = et_sendText.getText().toString();//獲取EditText內容
		et_sendText.setText("");
		String dropk = content_str.replace(" ", "");//去掉空格
		String droph = dropk.replace("\n", "");//去掉回车
		ListData listData;
		listData = new ListData(content_str, ListData.SEND, getTime());
		lists.add(listData);
		Log.i("haha9", "----------content_str="+content_str);
		adapter.notifyDataSetChanged();//刷新
		
		Log.i("haha6", "----------content_str="+content_str);	
		
		httpData = (HttpData) new HttpData(
				"http://www.tuling123.com/openapi/api?key=02dfb86de93f8a3e81dabd214a50c8fa&info=" + droph + "&userid=15602229049",
				this).execute();// 发送已经去掉空格和回车content_str的数据droph   ; execute()启动异步通信

		Log.i("haha5", "----------"+httpData);		
		
		if(lists.size() > 30){//如果屏幕上的对话数据多于30，则移除前面的数据
			for (int i = 0; i < lists.size(); i++) {
				lists.remove(i);		
			}
		}
	}

}

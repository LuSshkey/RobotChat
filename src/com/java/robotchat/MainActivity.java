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

	private HttpData httpData;// �첽�������
	private List<ListData> lists;
	private ListView lv;
	private EditText et_sendText;
	private Button btn_send;
	private String content_str; // �惦��EditText�@ȡ���Ĕ���
	private TextAdapter adapter;
	private String[] welcome_arry;//��ӭ��
	private double currentTime,oldTime = 0;//�Ի�ʱ��
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.i("haha7", "----------7");	
		initView();
		Log.i("haha8", "----------8");
	}

	/**
	 * ʵ��������
	 */
	private void initView(){
		lists = new ArrayList<ListData>();
		lv = (ListView) findViewById(R.id.lv);
		et_sendText = (EditText) findViewById(R.id.et_sendText);
		btn_send = (Button) findViewById(R.id.btn_send);
		btn_send.setOnClickListener(this);
		adapter = new TextAdapter(lists, this);
		lv.setAdapter(adapter);//��adapter
		ListData listData;
		listData = new ListData(getRandomWelcomeTips(),ListData.RECEIVE, getTime());
		lists.add(listData);//��ӻ�ӭ��
	}

	/**
	 * ���û�ӭ��
	 */
	public String getRandomWelcomeTips(){
		String welcome_tip = null;
		welcome_arry = this.getResources().getStringArray(R.array.welcome_tips);//��string.xml�л�ȡ��Ϊwelcome_tips���ַ�������
		int index = (int)(Math.random()*(welcome_arry.length - 1));//��ȡһ�������
		welcome_tip = welcome_arry[index];
		return welcome_tip;
	}
	
	/**
	 * ����ʱ��
	 * @return 
	 */
	private String getTime(){
		currentTime = System.currentTimeMillis();
		SimpleDateFormat format = new SimpleDateFormat("yyyy��MM��dd��   HH:mm:ss");
		Date curDate = new Date();
		String str = format.format(curDate);
		if(currentTime - oldTime >= 5*60*1000){//�������5���ӣ���ʾʱ��
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
	
	public void parseText(String str){//����json
		try {
			JSONObject jb = new JSONObject(str);
			ListData listData;
			listData = new ListData(jb.getString("text"),ListData.RECEIVE, getTime());
			lists.add(listData);
			adapter.notifyDataSetChanged();//�����m�䣿��
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		getTime();//����ʱ��
		content_str = et_sendText.getText().toString();//�@ȡEditText����
		et_sendText.setText("");
		String dropk = content_str.replace(" ", "");//ȥ���ո�
		String droph = dropk.replace("\n", "");//ȥ���س�
		ListData listData;
		listData = new ListData(content_str, ListData.SEND, getTime());
		lists.add(listData);
		Log.i("haha9", "----------content_str="+content_str);
		adapter.notifyDataSetChanged();//ˢ��
		
		Log.i("haha6", "----------content_str="+content_str);	
		
		httpData = (HttpData) new HttpData(
				"http://www.tuling123.com/openapi/api?key=02dfb86de93f8a3e81dabd214a50c8fa&info=" + droph + "&userid=15602229049",
				this).execute();// �����Ѿ�ȥ���ո�ͻس�content_str������droph   ; execute()�����첽ͨ��

		Log.i("haha5", "----------"+httpData);		
		
		if(lists.size() > 30){//�����Ļ�ϵĶԻ����ݶ���30�����Ƴ�ǰ�������
			for (int i = 0; i < lists.size(); i++) {
				lists.remove(i);		
			}
		}
	}

}

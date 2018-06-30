package com.java.robotchat;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 适配
 * @author mingyue
 *
 */
public class TextAdapter extends BaseAdapter{
	
	private List<ListData> lists;//集合的数据内容
	private Context mContext;//承接上下文的Context
	
	private RelativeLayout layout;
	
	public TextAdapter(List<ListData> lists, Context mContext) {
		this.lists = lists;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {//返回lists所承载的条数
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		if(lists.get(position).getFlag() == ListData.RECEIVE){
			layout = (RelativeLayout) inflater.inflate(R.layout.leftitem, null);
		}
		if(lists.get(position).getFlag() == ListData.SEND){
			layout = (RelativeLayout) inflater.inflate(R.layout.rightitem, null);
		}
		TextView tv = (TextView) layout.findViewById(R.id.tv);//对话textView
		TextView tv_time = (TextView) layout.findViewById(R.id.tv_time);//时间textView
		tv.setText(lists.get(position).getContent());  //將數據內容放進TextView中
		tv_time.setText(lists.get(position).getTime());//将事件写进TextView
		return layout;
	}

}

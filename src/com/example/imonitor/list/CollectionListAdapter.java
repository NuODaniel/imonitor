package com.example.imonitor.list;


import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imonitor.R;

public class CollectionListAdapter extends BaseAdapter{
	private List<Map<String,Object>> data;
	private Context mContext;
	private CollectionClickListener settingListener;
	private CollectionClickListener playListener;
	
	public List<Map<String, Object>> getData() {
		return data;
	}
	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}
	public CollectionListAdapter(List<Map<String, Object>> data,Context context, 
			CollectionClickListener settingListener,CollectionClickListener playListener){   
        this.data=data;  
        this.mContext=context;
        this.settingListener = settingListener;
        this.playListener = playListener;
    }  
	@Override  
    public int getCount() { 
		if(data!=null)
			return data.size();  
		return 0;
	}  

	@Override  
    public Map<String,Object> getItem(int position) {  
		if(data!=null)
			return data.get(position);  
		return null;
    }  

	@Override  
    public long getItemId(int position) {
        return position;  
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(parent.getContext()).
				 		inflate(R.layout.list_item_custom,null);
		 ImageView itemPreview = (ImageView)convertView.findViewById(R.id.iv_item_collection_preview);
		 TextView itemCid = (TextView)convertView.findViewById(R.id.txt_item_collection_cid);
		 TextView itemUsername = (TextView)convertView.findViewById(R.id.txt_item_collection_username);
		 ImageButton btnSetting = (ImageButton)convertView.findViewById(R.id.btn_item_collection_setting);
		 ImageButton btnPlay = (ImageButton)convertView.findViewById(R.id.btn_item_collection_play);
		 
		 if(!data.isEmpty() && data != null){
			itemPreview.setImageResource(R.drawable.ic_device);
		 	itemCid.setText("cid:"+data.get(position).get("cid").toString());
		 	itemUsername.setText("username:"+data.get(position).get("username").toString());
		 	btnSetting.setTag(position);
		 	btnSetting.setOnClickListener(settingListener);
		 	btnPlay.setTag(position);
		 	btnPlay.setOnClickListener(playListener);
		 }
		 return convertView;
	}
	/**
     * 用于回调的抽象类
     * @author Ivan Xu
     * 2014-11-26
     */
    public static abstract class CollectionClickListener implements OnClickListener {
        /**
         * 基类的onClick方法
         */
        @Override
        public void onClick(View v) {
            OnClick((Integer) v.getTag(), v);
        }
        public abstract void OnClick(int position, View v);
    }
}

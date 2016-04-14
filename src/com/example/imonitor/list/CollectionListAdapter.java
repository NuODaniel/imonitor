package com.example.imonitor.list;


import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.imonitor.R;

public class CollectionListAdapter extends BaseAdapter{
	private List<Map<String,Object>> data;
	
	public CollectionListAdapter(List<Map<String, Object>> data){   
        this.data=data;  
    }  
	@Override  
    public int getCount() {  
        return data.size();  
    }  

	@Override  
    public Object getItem(int position) {  
        return data.get(position);  
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
		 
		 
		 itemPreview.setImageResource(R.drawable.ic_launcher);
		 itemCid.setText(data.get(position).get("cid").toString());
		 itemUsername.setText(data.get(position).get("username").toString());
		 
		 return convertView;
	}

}

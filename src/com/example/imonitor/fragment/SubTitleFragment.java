package com.example.imonitor.fragment;

import com.example.imonitor.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SubTitleFragment extends Fragment{
	private ImageView icon;
	private TextView title;
	private ImageView action;
	
	private String mTitle;
	private int mIconId;
	private int mActionId;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_sub_title, container, false);
		icon = (ImageView)view.findViewById(R.id.iv_sub_title_icon);
		title = (TextView)view.findViewById(R.id.txt_sub_title_content);
		action = (ImageView)view.findViewById(R.id.btn_sub_title_action);
		
		
		return view;
	}
}

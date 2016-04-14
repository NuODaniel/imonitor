package com.example.imonitor.fragment;

import com.example.imonitor.R;
import com.example.imonitor.R.layout;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ShoppingFragment extends Fragment{
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_shopping, container, false);
		
		return view;
	}
}

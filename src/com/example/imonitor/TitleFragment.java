package com.example.imonitor;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class TitleFragment extends Fragment implements OnClickListener,OnTouchListener
{

	private RelativeLayout btnMonitor;
	private RelativeLayout btnAccount;
	private RelativeLayout btnShopping;
	
	private MonitorFragment  mMonitorFragment;
	private AccountFragment mAccountFragment;
	private ShoppingFragment mShoppingFragment;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_title, container, false);
		
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		btnMonitor = (RelativeLayout)this.getActivity().findViewById(R.id.rl_monitor_title);
		btnAccount = (RelativeLayout)this.getActivity().findViewById(R.id.rl_account_title);
		btnShopping = (RelativeLayout)this.getActivity().findViewById(R.id.rl_shopping_title);
		
		btnMonitor.setClickable(true);
		btnAccount.setClickable(true);
		btnShopping.setClickable(true);
		
		
		btnMonitor.setOnClickListener(this);
		btnMonitor.setOnTouchListener(this);
		btnAccount.setOnClickListener(this);
		btnAccount.setOnTouchListener(this);
		btnShopping.setOnClickListener(this);
		btnShopping.setOnTouchListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		Log.i("id", ""+v.getId());
		switch(v.getId()){
		case R.id.rl_monitor_title:
		case R.id.img_monitor:
		case R.id.text_monitor:
			if(mMonitorFragment == null)
				mMonitorFragment = new MonitorFragment();
			transaction.replace(R.id.id_fragment_content, mMonitorFragment);
			
			break;
		case R.id.rl_account_title:
		case R.id.img_account:
		case R.id.text_account:
			if(mAccountFragment == null)
				mAccountFragment = new AccountFragment();
			transaction.replace(R.id.id_fragment_content, mAccountFragment);
			break;
		case R.id.rl_shopping_title:
		case R.id.img_shopping:
		case R.id.text_shopping:
			if(mShoppingFragment == null)
				mShoppingFragment = new ShoppingFragment();
			transaction.replace(R.id.id_fragment_content, mShoppingFragment);
			break;
			
		}
		transaction.commit();
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub 
		RelativeLayout rl = null;
		switch(v.getId()){
		case R.id.rl_monitor_title:
		case R.id.img_monitor:
		case R.id.text_monitor:
			rl = btnMonitor;
			break;
		case R.id.rl_account_title:
		case R.id.img_account:
		case R.id.text_account:
			rl = btnAccount;
			break;
		case R.id.rl_shopping_title:
		case R.id.img_shopping:
		case R.id.text_shopping:
			rl = btnShopping;
			break;
			
		}
		if(rl == null) return false;
        if(event.getAction()==MotionEvent.ACTION_DOWN) 
        { 
            rl.setBackgroundColor(Color.rgb(127,0,0)); 
        } 
        else if(event.getAction()==MotionEvent.ACTION_UP) 
        { 
            rl.setBackgroundColor(Color.parseColor("#FFF5EE")); 
        } 
        return false; 
	} 
}

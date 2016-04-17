package com.example.imonitor.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.imonitor.AddCollectionActivity;
import com.example.imonitor.R;

public class TitleFragment extends Fragment implements OnClickListener,OnTouchListener
{

	private RelativeLayout btnMonitor;
	private RelativeLayout btnAccount;
	private RelativeLayout btnShopping;
	
	private CollectionListFragment mCollectionListFragment;
	private AccountFragment mAccountFragment;
	private ShoppingFragment mShoppingFragment;
	
	//private SubTitleFragment subtitleFragment;
	private TextView txtSubTitle;
	private ImageButton btnSubAction;
	private ImageView ivSubIcon;
	private ImageButton btnSubRefresh;
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
		
		txtSubTitle = (TextView)this.getActivity().findViewById(R.id.txt_sub_title_content);
		btnSubAction = (ImageButton)this.getActivity().findViewById(R.id.btn_sub_title_action);
		ivSubIcon = (ImageView)this.getActivity().findViewById(R.id.iv_sub_title_icon);
		btnSubRefresh = (ImageButton)this.getActivity().findViewById(R.id.btn_sub_title_refresh);
		
		btnSubAction.setClickable(true);
		btnSubAction.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getActivity(), AddCollectionActivity.class);
				startActivity(intent);
			}
		});
		btnSubRefresh.setClickable(true);
		
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
			if(mCollectionListFragment == null)
				mCollectionListFragment = new CollectionListFragment();
			transaction.replace(R.id.id_fragment_content, mCollectionListFragment);
			updateSubtitle(1);
			break;
		case R.id.rl_account_title:
		case R.id.img_account:
		case R.id.text_account:
			if(mAccountFragment == null)
				mAccountFragment = new AccountFragment();
			transaction.replace(R.id.id_fragment_content, mAccountFragment);
			updateSubtitle(2);
			break;
		case R.id.rl_shopping_title:
		case R.id.img_shopping:
		case R.id.text_shopping:
			if(mShoppingFragment == null)
				mShoppingFragment = new ShoppingFragment();
			updateSubtitle(3);
			transaction.replace(R.id.id_fragment_content, mShoppingFragment);
			break;
			
		}
		transaction.commit();
	}
	public void updateSubtitle(int i){
		switch(i){
		case 1:
		txtSubTitle.setText("我的设备");
		ivSubIcon.setImageResource(R.drawable.monitor_title);
		btnSubAction.setImageResource(android.R.drawable.ic_input_add);
		btnSubAction.setColorFilter(0);;
		ivSubIcon.setColorFilter(0);
		btnSubAction.setVisibility(android.view.View.VISIBLE);
		btnSubRefresh.setVisibility(android.view.View.VISIBLE);
		break;
		case 2:
		txtSubTitle.setText("我的账号");
		ivSubIcon.setImageResource(R.drawable.user_title);
		btnSubAction.setColorFilter(0);;
		ivSubIcon.setColorFilter(0);
		btnSubAction.setVisibility(android.view.View.GONE);
		btnSubRefresh.setVisibility(android.view.View.GONE);
		break;
		case 3:
		txtSubTitle.setText("商城");
		ivSubIcon.setImageResource(R.drawable.shopping_title);
		btnSubAction.setColorFilter(0);;
		ivSubIcon.setColorFilter(0);
		btnSubAction.setVisibility(android.view.View.GONE);
		btnSubRefresh.setVisibility(android.view.View.GONE);
		break;
		}
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
            rl.setBackgroundColor(Color.parseColor("#000000")); 
        } 
        else if(event.getAction()==MotionEvent.ACTION_UP) 
        { 
            rl.setBackgroundColor(Color.parseColor("#FFFFFF")); 
        } 
        return false; 
	} 
}

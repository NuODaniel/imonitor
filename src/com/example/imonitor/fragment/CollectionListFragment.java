package com.example.imonitor.fragment;

import java.util.ArrayList;
import java.util.Map;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.imonitor.MonitorActivity;
import com.example.imonitor.R;
import com.example.imonitor.entity.Account;
import com.example.imonitor.list.CollectionListAdapter;
import com.example.imonitor.list.CollectionListAdapter.CollectionClickListener;
import com.example.imonitor.net.NetThread;
import com.example.imonitor.net.thread.BeginMonitorThread;
import com.example.imonitor.net.thread.GetCMThread;
import com.example.imonitor.util.AccountInfoUtil;

public class CollectionListFragment extends Fragment
{
	private ListView collectionListView;
	private CollectionListAdapter listAdapter;
	private View mCmListStatusView;
	private ImageButton btnRefresh;
	private GetCMThread mGetCMth = null;
	private ArrayList<Map<String, Object>> mCMList;
	private View rootView = null;
	private static final String TAG = "CollectionListFragment";  
	private BeginMonitorThread beginThread;
	private int mCollectionId;
	private int mAccountId;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		if(rootView==null){
			rootView = inflater.inflate(R.layout.fragment_monitor, container, false);
			initView(rootView);
        }
		//缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        } 
		return rootView;
	}
	
	private void initView(View view) {
		collectionListView = (ListView) rootView.findViewById(R.id.monitor_collection_list);
		mCmListStatusView = rootView.findViewById(R.id.cm_list_status);
		
		CollectionClickListener settingListener = new CollectionClickListener() {
			@Override
			public void OnClick(int position, View v) {
				Toast toast = Toast.makeText(getActivity(), "setting", Toast.LENGTH_LONG);
				toast.show();
			}
		};
		CollectionClickListener playListener = new CollectionClickListener() {
			@Override
			public void OnClick(int position, View v) {
				Map<String,Object> item = listAdapter.getItem(position);
				mCollectionId = Integer.parseInt(item.get("id").toString());
				Account accountinfo = new AccountInfoUtil(getActivity()).getAccountFromPre();
				mAccountId = accountinfo.getAccountid();
				if(beginThread == null){
					
					BeginMonitorThread beginThread = new BeginMonitorThread(mCollectionId,mAccountId,beginHandler);
					new Thread(beginThread).start();
				}
			}
		};
		listAdapter = new CollectionListAdapter(mCMList,getActivity(),
				settingListener,playListener);
				
			
		collectionListView.setAdapter(listAdapter); 
	}
	
	/**
	 * handle the begin transform request result
	 */
	Handler beginHandler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case 0://collection is offline
				Toast.makeText(getActivity(), "can't begin to retrieve data", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Intent intent = new Intent();
				intent.putExtra("collectionid",mCollectionId);
				intent.putExtra("accountid",mAccountId);
				intent.setClass(getActivity(), MonitorActivity.class);
				
				startActivity(intent);
				break;
			}
			beginThread = null;
		}
	};
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate((savedInstanceState));
		btnRefresh = (ImageButton) this.getActivity().findViewById(R.id.btn_sub_title_refresh);
		btnRefresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateListItem();
				showProgress(true);
			}
		});
	};
	Handler cmHandler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			mCMList = (ArrayList<Map<String, Object>>) msg.obj;
			listAdapter.setData(mCMList);
			listAdapter.notifyDataSetChanged();
			showProgress(false);
		}
	};
	public void updateListItem(){
		Account ac = new AccountInfoUtil(this.getActivity()).getAccountFromPre();
		showProgress(true);
		mGetCMth = new GetCMThread("MANAGE"+"##"+NetThread.GET_COLLECTION_MANAGE+"##"+ac.getAccountid(),cmHandler);
		new Thread(mGetCMth).start();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mCmListStatusView.setVisibility(View.VISIBLE);
			mCmListStatusView.animate().setDuration(shortAnimTime)
			.alpha(show ? 1 : 0)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mCmListStatusView.setVisibility(show ? View.VISIBLE
							: View.GONE);
				}
			});

			collectionListView.setVisibility(View.VISIBLE);
			collectionListView.animate().setDuration(shortAnimTime)
			.alpha(show ? 0 : 1)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					collectionListView.setVisibility(show ? View.GONE
							: View.VISIBLE);
				}
			});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mCmListStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			collectionListView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
}

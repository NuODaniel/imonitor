package com.example.imonitor.fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.imonitor.R;
import com.example.imonitor.entity.Account;
import com.example.imonitor.list.CollectionListAdapter;
import com.example.imonitor.net.NetThread;
import com.example.imonitor.util.AccountInfoUtil;

public class CollectionListFragment extends Fragment
{
	
	private AsyncTask mGetCMTask = null;
	private ListView collectionListView;
	private CollectionListAdapter listAdapter;
	private List<Map<String,Object>> mCMList;
	private View mCmListStatusView;
	private ImageButton btnRefresh;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_monitor, container, false);
		collectionListView = (ListView) view.findViewById(R.id.monitor_collection_list);
		mCmListStatusView = view.findViewById(R.id.cm_list_status);
		mGetCMTask = new GetCMTask();
	
		mCMList = new ArrayList<Map<String,Object>>();
		testupdateListItem();
		listAdapter = new CollectionListAdapter(mCMList);
		collectionListView.setAdapter(listAdapter); 
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate((savedInstanceState));
		btnRefresh = (ImageButton) this.getActivity().findViewById(R.id.btn_sub_title_refresh);
		btnRefresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				testupdateListItem();
				listAdapter.notifyDataSetChanged();
			}
		});
	};
	public void testupdateListItem(){
		mCMList = new ArrayList<Map<String,Object>>();
		for(int i=0;i<5;++i){
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("id", i);  
            map.put("cid", "00000-cid");  
            map.put("username", "user1111"+i);  
            mCMList.add(map);
		}
	}
	public void updateListItem(){
		Account ac = new AccountInfoUtil(this.getActivity()).getAccountFromPre();
		mGetCMTask.execute(new Integer(ac.getAccountid()).toString());
	}
	public class GetCMTask extends AsyncTask<String, Void, Boolean> {
		private String mServerUrl = "192.168.253.1";
		private int mServerPort = 6789;
		private String side = "MANAGE";
		private String logmsg = null;
		@Override
		protected Boolean doInBackground(String... params) {

			String accountid = params[0];
			String message = side+"##"+NetThread.GET_COLLECTION_MANAGE+"##"
							+accountid;
			try {
				Socket socket=new Socket(mServerUrl,mServerPort);
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				BufferedReader bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				out.println(message);
				out.flush();
				
				String result = bReader.readLine();
				String[] msg = result.split("##");
				if(msg[0].equals(side)){
					if(msg[1].equals(NetThread.GET_COLLECTION_MANAGE)){
						if(msg[2].equals("SUCCESS")){
							mCMList = new ArrayList<Map<String,Object>>();
							String[] args = msg[3].split("\\$");
							
							for(int i=0;i<msg.length;i+=3){
								Map<String, Object> map=new HashMap<String, Object>();
								map.put("id", msg[i]);  
					            map.put("cid", msg[i+1]);  
					            map.put("username", msg[i+2]);  
					            mCMList.add(map);
							}
							return true;
						}else{
							logmsg = "no cm exists";
						}
					}
				}
				out.close();
				socket.close();
				return false;
			} catch (UnknownHostException e) {
				logmsg = "Can't find the server";
				return false;
			} catch (IOException e) {
				logmsg = "IOException";
				return false;
			} 
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mGetCMTask = null;
			showProgress(false);
		}

		@Override
		protected void onCancelled() {
			mGetCMTask = null;
			showProgress(false);
		}
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

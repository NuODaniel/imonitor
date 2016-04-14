package com.example.imonitor;

import com.example.imonitor.entity.Account;
import com.example.imonitor.net.NetThread;
import com.example.imonitor.net.thread.AddCollectionThread;
import com.example.imonitor.util.AccountInfoUtil;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AddCollectionActivity extends Activity {
	private View mAddCollectionForm;
	private View mAddCollectionStatus;
	
	private Button btnAddQRcode;
	private Button btnAddByInfo;
	private EditText edtxtCid;
	private EditText edtxtUsername;
	private EditText edtxtPassword;
	
	private String mCid;
	private String mUsername;
	private String mPassword;
	
	Handler addHandler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			showProgress(false);
			Toast toast = null;
			switch(msg.arg1){
			case 2:
				toast = Toast.makeText(getApplicationContext(),
					     "已经绑定过了", Toast.LENGTH_LONG);
				break;
			case 1:
				toast = Toast.makeText(getApplicationContext(),
					     "绑定成功", Toast.LENGTH_LONG);
				break;
			case 0:
				toast = Toast.makeText(getApplicationContext(),
					     "错误的cid,用户名与密码", Toast.LENGTH_LONG);
				break;
			}
			if(toast!=null){
			   toast.setGravity(Gravity.CENTER, 0, 0);
			   toast.show();
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_collection);
		btnAddQRcode = (Button)findViewById(R.id.btn_add_qrcode);
		btnAddByInfo = (Button)findViewById(R.id.btn_add_basic_info);
		edtxtCid = (EditText)findViewById(R.id.edittxt_add_cid);
		edtxtUsername = (EditText)findViewById(R.id.edittxt_add_username);
		edtxtPassword = (EditText)findViewById(R.id.edittxt_add_password);
		mAddCollectionForm = findViewById(R.id.form_add_collection);
		mAddCollectionStatus = findViewById(R.id.status_add_collection);
		
		btnAddByInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mCid = edtxtCid.getText().toString();
				mUsername = edtxtUsername.getText().toString();
				mPassword = edtxtPassword.getText().toString();
				Account ac = new AccountInfoUtil(getSharedPreferences("account_info", Context.MODE_PRIVATE)).getAccountFromPre();
				AddCollectionThread addthread = 
						new AddCollectionThread("MANAGE##"+NetThread.ADD_COLLECTION
											+"##"
											+ac.getAccountid()+"$"
											+mCid+"$"
											+mUsername+"$"
											+mPassword
											, addHandler);
				new Thread(addthread).start();
				showProgress(true);
			}
		});
		btnAddQRcode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(AddCollectionActivity.this, CaptureActivity.class);
				startActivityForResult(intent, 0);
			}
		});
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (0 == requestCode) {
			if (0 == resultCode) {
				Bundle bundle = data.getBundleExtra("collection_info");
				String mCid = bundle.getString("cid");
				String mUsername = bundle.getString("username");
				String mPassword = bundle.getString("password");
				
				Account ac = new AccountInfoUtil(getSharedPreferences("account_info", Context.MODE_PRIVATE)).getAccountFromPre();
				AddCollectionThread addthread = 
						new AddCollectionThread("MANAGE##"+NetThread.ADD_COLLECTION
											+"##"
											+ac.getAccountid()+"$"
											+mCid+"$"
											+mUsername+"$"
											+mPassword
											, addHandler);
				new Thread(addthread).start();
				showProgress(true);
			}else if(1 == resultCode){
				
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);		
			mAddCollectionStatus.setVisibility(View.VISIBLE);
			mAddCollectionStatus.animate().setDuration(shortAnimTime)
			.alpha(show ? 1 : 0)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mAddCollectionStatus.setVisibility(show ? View.VISIBLE
							: View.GONE);
				}
			});

			mAddCollectionForm.setVisibility(View.VISIBLE);
			mAddCollectionForm.animate().setDuration(shortAnimTime)
			.alpha(show ? 0 : 1)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mAddCollectionForm.setVisibility(show ? View.GONE
							: View.VISIBLE);
				}
			});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mAddCollectionStatus.setVisibility(show ? View.VISIBLE : View.GONE);
			mAddCollectionForm.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
	
}

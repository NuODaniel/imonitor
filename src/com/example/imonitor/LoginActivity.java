package com.example.imonitor;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imonitor.net.NetThread;


/**
 * @author chao11.ma
 * <b>一个用户登录验证的demo
 */
public class LoginActivity extends Activity implements OnClickListener{


	/**
	 * 一个用户登录的后台验证异步任务
	 */
	private AccountLoginTask mAuthTask = null;

	// 用户名（邮箱）
	private String mEmail;
	// 密码
	private String mPassword;

	// 用户名框
	private EditText mEmailView;
	//　密码框
	private EditText mPasswordView;
	// 包含整个用户登录的form框
	private View mLoginFormView;
	// 包含的进度条和消息验证文本框
	private View mLoginStatusView;
	// 消息验证提示框
	private TextView mLoginStatusMessageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		// 获取默认的初期化文字
		mEmailView = (EditText) findViewById(R.id.email);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id,
					KeyEvent keyEvent) {
				if (id == R.id.login || id == EditorInfo.IME_NULL) {
					attemptLogin();
					return true;
				}
				return false;
			}
		});
		// 包含整个用户登录的form框
		mLoginFormView = findViewById(R.id.login_form);
		// 包含的进度条和消息验证文本框
		mLoginStatusView = findViewById(R.id.login_status);
		// 消息验证提示框
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);
		// 点击登录按钮
		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
		// 点击注册按钮
		findViewById(R.id.sign_up_button).setOnClickListener(this);
	}
	/**
	 * going to the register activity
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,RegisterActivity.class);
		startActivity(intent);
	}

	/**
	 * <h1>关于用户登录验证的操作
	 */
	public void attemptLogin() {
		// 建立的用户登录验证任务
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.　重置消息
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// 获取界面输入数据
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 6) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (mEmail.length()<6) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// 如果check没有通过   重新定位文本框焦点
			focusView.requestFocus();
		} else {
			// 如果单项目check通过 就开始 进行后台登录验证
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			// 开始显示进度条
			showProgress(true);
			// 建立一个后台验证任务
			mAuthTask = new AccountLoginTask();
			// 传入输入的用户名和密码
			mAuthTask.execute(mEmail,mPassword);
		}
	}

	/**
	 * 显示进度条
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
			.alpha(show ? 1 : 0)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mLoginStatusView.setVisibility(show ? View.VISIBLE
							: View.GONE);
				}
			});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
			.alpha(show ? 0 : 1)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mLoginFormView.setVisibility(show ? View.GONE
							: View.VISIBLE);
				}
			});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}


	/**
	 * @author chao11.ma
	 * 验证用户的正确性
	 */
	public class AccountLoginTask extends AsyncTask<String, Void, Boolean> {
		private String mServerUrl = "192.168.253.1";
		private int mServerPort = 6789;
		private String side = "MANAGE";
		private String logmsg = null;
		@Override
		protected Boolean doInBackground(String... params) {

			String email=params[0];
			String password =params[1];
			String message = side+"##"+NetThread.ACCOUNT_LOGIN+"##"+
							email+"$"+
							password;
			// 从数据库获得account
			try {
				Socket socket=new Socket(mServerUrl,mServerPort);
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				BufferedReader bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				out.println(message);
				String result = bReader.readLine();
				String[] msg = result.split("##");
				if(msg[0].equals("SERVER")){
					if(msg[1].equals(NetThread.ACCOUNT_LOGIN)){
						if(msg[2].equals("SUCCESS")){
							return true;
						}else{
							logmsg = "账号/密码错误！";
						}
					}
				}
				
				out.flush();
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
			mAuthTask = null;
			showProgress(false);

			if (success) {
				LoginActivity.this.finish();
				// 跳转到成功登录页面
				Toast.makeText(getApplicationContext(), "登录成功",Toast.LENGTH_LONG).show();
				Intent intent = new Intent(LoginActivity.this,MainActivity.class);
				startActivity(intent);
			} else {
				mPasswordView.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}


	
}

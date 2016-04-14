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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.imonitor.entity.Account;
import com.example.imonitor.net.NetThread;
import com.example.imonitor.util.AccountInfoUtil;

public class LoginActivity extends Activity implements OnClickListener{
	private SharedPreferences prefs;
	private AccountLoginTask mAuthTask = null;
	private String mEmail;
	private String mPassword;
	
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
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
		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);
		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
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

	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}
		mEmailView.setError(null);
		mPasswordView.setError(null);

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
			focusView.requestFocus();
		} else {
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new AccountLoginTask();
			mAuthTask.execute(mEmail,mPassword);
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
			try {
				Socket socket=new Socket(mServerUrl,mServerPort);
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				BufferedReader bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				out.println(message);
				out.flush();
				
				String result = bReader.readLine();
				String[] msg = result.split("##");
				if(msg[0].equals(side)){
					if(msg[1].equals(NetThread.ACCOUNT_LOGIN)){
						if(msg[2].equals("SUCCESS")){
							String[] args = msg[3].split("\\$");
							int accountid = Integer.parseInt(args[0]);
							String accountname = args[1];
							Account account = new Account(accountid,accountname,mEmail,mPassword);
							
							AccountInfoUtil acUtil = new AccountInfoUtil(getSharedPreferences("account_info", Context.MODE_PRIVATE));
							acUtil.editAccountPre(account);
							
							return true;
						}else{
							logmsg = "wrong account/password";
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
			mAuthTask = null;
			showProgress(false);

			if (success) {
				LoginActivity.this.finish();
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

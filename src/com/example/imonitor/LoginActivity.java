package com.example.imonitor;


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
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @author chao11.ma
 * <b>һ���û���¼��֤��demo
 */
public class LoginActivity extends Activity {


	/**
	 * һ���û���¼�ĺ�̨��֤�첽����
	 */
	private UserLoginTask mAuthTask = null;

	// �û��������䣩
	private String mEmail;
	// ����
	private String mPassword;

	// �û�����
	private EditText mEmailView;
	//�������
	private EditText mPasswordView;
	// ���������û���¼��form��
	private View mLoginFormView;
	// �����Ľ���������Ϣ��֤�ı���
	private View mLoginStatusView;
	// ��Ϣ��֤��ʾ��
	private TextView mLoginStatusMessageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		// ��ȡĬ�ϵĳ��ڻ�����
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
		// ���������û���¼��form��
		mLoginFormView = findViewById(R.id.login_form);
		// �����Ľ���������Ϣ��֤�ı���
		mLoginStatusView = findViewById(R.id.login_status);
		// ��Ϣ��֤��ʾ��
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);
		// �����¼��ť
		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
	}


	/**
	 * <h1>�����û���¼��֤�Ĳ���
	 */
	public void attemptLogin() {
		// �������û���¼��֤����
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.��������Ϣ
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// ��ȡ������������
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
			// ���checkû��ͨ��   ���¶�λ�ı��򽹵�
			focusView.requestFocus();
		} else {
			// �������Ŀcheckͨ�� �Ϳ�ʼ ���к�̨��¼��֤
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			// ��ʼ��ʾ������
			showProgress(true);
			// ����һ����̨��֤����
			mAuthTask = new UserLoginTask();
			// ����������û���������
			mAuthTask.execute(mEmail,mPassword);
		}
	}

	/**
	 * ��ʾ������
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
	 * ��֤�û�����ȷ��
	 */
	public class UserLoginTask extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {

			String email=params[0];
			String password =params[1];
			// �����ݿ���user
			
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				LoginActivity.this.finish();
				// ��ת���ɹ���¼ҳ��
				Toast.makeText(getApplicationContext(), "��¼�ɹ�",Toast.LENGTH_LONG).show();
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

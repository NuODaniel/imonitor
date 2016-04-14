package com.example.imonitor.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.imonitor.entity.Account;

public class AccountInfoUtil {
	private Account mAccount;
	SharedPreferences prefs; 
	
	public AccountInfoUtil(Context context) {
		prefs = context.getSharedPreferences("account_info", Context.MODE_PRIVATE);
	}
	public AccountInfoUtil(SharedPreferences prefs) {
		this.prefs = prefs;
	}
	public Account getAccountFromPre(){
		mAccount = new Account();
		mAccount.setAccountid(prefs.getInt("id", -1));
		mAccount.setAccountname(prefs.getString("accountname", null));
		mAccount.setPassword(prefs.getString("password", null));
		mAccount.setEmail(prefs.getString("email", null));
		
		return mAccount;
	}
	public void editAccountPre(Account account){
		mAccount = account;
		Editor editor = prefs.edit();
		editor.putInt("id",mAccount.getAccountid());
		editor.putString("accountname", mAccount.getAccountname());
		editor.putString("email", mAccount.getEmail());
		editor.putString("password", mAccount.getPassword());
		editor.commit();
	}
}

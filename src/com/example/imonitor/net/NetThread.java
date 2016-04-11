package com.example.imonitor.net;

public abstract class NetThread implements Runnable{
	/**
	 * manage client send these orders to server
	 */
	public static final String ACCOUNT_LOGIN = "ACCOUNT_LOGIN";
	public static final String ACCOUNT_REGISTER = "ACCOUNT_REGISTER";
	
	protected String message;
	protected String mServerUrl = "192.168.253.1";
	protected int mServerPort = 6789;
	public NetThread(String msg) {
		message = msg;
	}
	public NetThread(String msg, String serverUrl, int serverPort) {
		message = msg;
		mServerUrl = serverUrl;
		mServerPort = serverPort;
	}
	
}

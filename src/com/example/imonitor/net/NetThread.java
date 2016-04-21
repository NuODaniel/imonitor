package com.example.imonitor.net;

public abstract class NetThread implements Runnable{
	/**
	 * manage client send or get these orders i/o server
	 */
	
	public static final int ACCOUNT_LOGIN = 100006;
	public static final int ACCOUNT_REGISTER = 100007;
	public static final int ADD_COLLECTION = 100008;
	public static final int START_TRANSFORM_VIDEO = 100009;
	public static final int TRANSFROM_DATA = 100010;
	public static final int GET_COLLECTION_MANAGE = 100011;
	public static final int END_TRANSFORM_VIDEO = 100012;
	
	public String side = "MANAGE";
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
	public NetThread() {
		// TODO Auto-generated constructor stub
	}

	
}

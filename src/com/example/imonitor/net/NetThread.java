package com.example.imonitor.net;

public abstract class NetThread implements Runnable{
	/**
	 * manage client send these orders to server
	 */
	
	public static final String ACCOUNT_LOGIN = "ACCOUNT_LOGIN";
	public static final String ACCOUNT_REGISTER = "ACCOUNT_REGISTER";
	public static final String ADD_COLLECTION = "ADD_COLLECTION_CID_NAME_PWD";
	public static final String START_TRANSFORM_VIDEO = "START_TRANSFORM_VIDEO";
	public static final String GET_COLLECTION_MANAGE = "GET_CM_LIST";
	
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
	
}

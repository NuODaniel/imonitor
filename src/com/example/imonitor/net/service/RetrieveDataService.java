package com.example.imonitor.net.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Messenger;

import com.example.imonitor.net.NetStateUtil;
import com.example.imonitor.net.thread.RetriDataThread;

public class RetrieveDataService extends IntentService {
	private ServerSocket ss;
	public static boolean isListened;
	
	public RetrieveDataService() {
		super("RetrieveDataService");
		try {
			ss = new ServerSocket(9999);
			isListened = true;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle bundle = intent.getExtras();
		Messenger messenger = (Messenger)bundle.get("MonitorActivity");
		
		Socket fromServer;
		NetStateUtil netUtil = new NetStateUtil(this);
		if(netUtil.isNetworkConnected()){
			while(isListened){
				try {
					fromServer = ss.accept();
					new Thread(new RetriDataThread(fromServer,messenger)).start();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}

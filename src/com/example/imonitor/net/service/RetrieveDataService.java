package com.example.imonitor.net.service;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.example.imonitor.net.NetStateUtil;
import com.example.imonitor.net.NetThread;

public class RetrieveDataService extends IntentService {
	private ServerSocket ss;
	private boolean isListened;
	private byte[] data;
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
		Socket fromServer;
		NetStateUtil netUtil = new NetStateUtil(this);
		if(netUtil.isNetworkConnected()){
			while(isListened){
				try {
					fromServer = ss.accept();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(fromServer.getInputStream()));
					String[] msg = reader.readLine().split("##");
					if(msg[0].equals("COLLECTION")){
						int command = Integer.parseInt(msg[1]);
						
						if(command == NetThread.TRANSFROM_DATA){
							DataInputStream datain = new DataInputStream(fromServer.getInputStream());
							
							int length = datain.readInt();
							
							datain.read(data);
							
							Bundle bundle = intent.getExtras();
							Messenger messenger = (Messenger)bundle.get("MainActivity");
							Message message = Message.obtain();
							message.what = 1;
							message.obj = data;
							messenger.send(message);
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}

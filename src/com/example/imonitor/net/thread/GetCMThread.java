package com.example.imonitor.net.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Handler;
import android.os.Message;

import com.example.imonitor.net.NetThread;

public class GetCMThread extends NetThread{

	private ArrayList<Map<String, Object>> mCMList;
	private String logmsg;
	private Handler mHandler;

	public GetCMThread(String msg,Handler handler) {
		super(msg);
		mHandler = handler;
	}

	@Override
	public void run() {
		try {
			Socket socket=new Socket(mServerUrl,mServerPort);
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			BufferedReader bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			out.println(message);
			out.flush();
			
			String result = bReader.readLine();
			String[] msg = result.split("##");
			if(msg[0].equals(side)){
				if(Integer.parseInt(msg[1])==NetThread.GET_COLLECTION_MANAGE){
					if(msg[2].equals("SUCCESS")){
						mCMList = new ArrayList<Map<String,Object>>();
						String[] args = msg[3].split("\\$");
						
						for(int i=0;i<args.length;i+=3){
							Map<String, Object> map=new HashMap<String, Object>();
							map.put("id", args[i]);  
				            map.put("cid", args[i+1]);  
				            map.put("username", args[i+2]);  
				            mCMList.add(map);
						}
						
						
					}else{
						logmsg = "no cm exists";
					}
				}
			}
			Message message = mHandler.obtainMessage();
			message.obj = mCMList;
			mHandler.sendMessage(message);
			
			out.close();
			socket.close();
		} catch (UnknownHostException e) {
			logmsg = "Can't find the server";
		} catch (IOException e) {
			logmsg = "IOException";
		} 
	}

}

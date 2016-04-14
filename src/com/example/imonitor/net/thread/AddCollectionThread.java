package com.example.imonitor.net.thread;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.os.Handler;
import android.os.Message;

import com.example.imonitor.net.NetThread;


public class AddCollectionThread extends NetThread {
	private Handler mHandler;
	private int resultType = 0;
	public AddCollectionThread(String msg,Handler handler) {
		super(msg);
		mHandler = handler;
	}

	@Override
	public void run() {
		try{
			Socket socket=new Socket(mServerUrl,mServerPort);
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			BufferedReader bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			out.println(message);
			out.flush();
			String result = bReader.readLine();
			String[] msg = result.split("##");
			if(msg[0].equals(side)){
				if(msg[1].equals(NetThread.ADD_COLLECTION)){
					if(msg[2].equals("SUCCESS")){
						resultType = 1;
					}
					else if(msg[2].equals("EXIST")){
						resultType = 2;
					}
					else if(msg[2].equals("FAIL")){
						resultType = 0;
					}
				}
			}
			Message handlermsg = mHandler.obtainMessage();
			handlermsg.arg1 = resultType;
			mHandler.sendMessage(handlermsg);
			out.close();
			socket.close();
		} catch (Exception e){
			e.printStackTrace();
		} 
	}
}
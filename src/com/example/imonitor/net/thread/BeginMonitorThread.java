package com.example.imonitor.net.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Handler;
import android.os.Message;

import com.example.imonitor.net.NetThread;

public class BeginMonitorThread extends NetThread{

	
	private Handler mHandler;
	private String logmsg;

	public BeginMonitorThread(String msg,Handler handler) {
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
			Message handlerMessage = mHandler.obtainMessage();
			String[] msg = result.split("##");
			if(msg[0].equals(side)){
				if(Integer.parseInt(msg[1])==NetThread.START_TRANSFORM_VIDEO){
					if(msg[2].equals("SUCCESS")){
						handlerMessage.arg1 = 1;
					}else{
						handlerMessage.arg1 = 0;
						logmsg = "can't start";
					}
				}
			}
			

			mHandler.sendMessage(handlerMessage);
			
			out.close();
			socket.close();
		} catch (UnknownHostException e) {
			logmsg = "Can't find the server";
		} catch (IOException e) {
			logmsg = "IOException";
		} 
	}

}

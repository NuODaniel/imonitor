package com.example.imonitor.net.thread;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.os.Handler;
import android.os.Message;

import com.example.imonitor.net.NetThread;

public class RegisterThread extends NetThread{
	Handler mHandler;
	public RegisterThread(String msg, Handler handler) {
		super(msg);
		mHandler = handler;
	}
	public RegisterThread(String msg, String serverUrl, int serverPort, Handler handler) {
		super(msg, serverUrl, serverPort);
		mHandler = handler;
	}

	@Override
	public void run() {
		try {
			Socket socket=new Socket(mServerUrl,mServerPort);
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			BufferedReader bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			out.println(message);
			String result = bReader.readLine();
			boolean regResult = false;
			String[] msg = result.split("##");
			if(msg[0].equals("SERVER")){
				if(msg[1].equals(NetThread.ACCOUNT_REGISTER)){
					if(msg[2].equals("SUCCESS")){
						regResult = true;
					}
					
				}
			}
			Message handlermsg = mHandler.obtainMessage();
			if(regResult){
				handlermsg.arg1 = 1;
			}else{
				handlermsg.arg1 = 0;
			}
			mHandler.sendMessage(handlermsg);
			
			out.flush();
			out.close();
			socket.close();
		} catch (Exception e){
			e.printStackTrace();
		} 
	}
}
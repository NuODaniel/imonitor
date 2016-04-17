package com.example.imonitor.net.thread;

import java.io.DataInputStream;
import java.net.Socket;

import android.os.Handler;
import android.os.Message;

import com.example.imonitor.net.NetThread;

public class RetrieveDataThread extends NetThread{
	Handler mHandler;
	private byte[] mBuffer;
	public RetrieveDataThread(String msg, Handler handler) {
		super(msg);
		mHandler = handler;
	}
	public RetrieveDataThread(String msg, String serverUrl, int serverPort, Handler handler) {
		super(msg, serverUrl, serverPort);
		mHandler = handler;
	}

	@Override
	public void run() {
		try {
			Socket socket=new Socket(mServerUrl,mServerPort);
			DataInputStream in = new DataInputStream(socket.getInputStream());
						
			int type = in.readInt();
			if(type == NetThread.TRANSFROM_DATA){
				int length = in.readInt();
				mBuffer = new byte[length];
				in.read(mBuffer,0,length);
			}
			Message handlermsg = mHandler.obtainMessage();
			handlermsg.obj = mBuffer;
			mHandler.sendMessage(handlermsg);
			socket.close();
		} catch (Exception e){
			e.printStackTrace();
		} 
	}
}

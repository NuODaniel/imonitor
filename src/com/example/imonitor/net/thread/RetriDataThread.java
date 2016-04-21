package com.example.imonitor.net.thread;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.example.imonitor.net.NetThread;

public class RetriDataThread extends NetThread{
	Handler mHandler;
	Socket socket;
	DataInputStream ins;
	private Messenger messenger;
	private byte[] data;
	public RetriDataThread(Socket s,Messenger messenger) {
		super();
		socket = s;
		this.messenger = messenger;
		try {
			ins = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int BufferSize = 1024;
	@Override
	public synchronized void run() {
		try {
			
			String[] msg = ins.readLine().split("##");
			Log.v("retri","data in");
			if(msg[0].equals("SERVER")){
				int command = Integer.parseInt(msg[1]);	
				String[] arguments = msg[2].split("\\$");
				if(command == NetThread.TRANSFROM_DATA){
					int collectionid = Integer.parseInt(arguments[0]);
					int codeway = Integer.parseInt(arguments[1]);
					
					int length = ins.readInt();
					int width = ins.readInt();
					int height = ins.readInt();

					data = new byte[length];
					
					byte[] buffer = new byte[BufferSize];
					int bytes = 1;
					for (int i=0;(bytes=ins.read(buffer,0,BufferSize)) != -1&&i<length;i+=1024){
						for(int j=0;i+j<BufferSize;j++)
							data[i+j] = buffer[j];
					}
//					int[] buffer = new int[BufferSize];
//					int bytes = 1;
//					for (int i=0;(bytes=ins.read(buffer,0,BufferSize)) != -1&&i<length;i+=1024){
//						for(int j=0;i+j<BufferSize;j++)
//							data[i+j] = buffer[j];
//					}
					
					if(data!=null){
						Message message = Message.obtain();
						message.what = 1;
						message.arg1 = width;
						message.arg2 = height;
						message.obj = data;
						
//						Bundle bundle = new Bundle();
//						bundle.putInt("codeway", codeway);
//						bundle.putInt("collectionid",collectionid);
//						message.setData(bundle);
						
						messenger.send(message);
					}
				}
			}
			ins.close();
			socket.close();
		} catch (Exception e){
			e.printStackTrace();
		} 
	}
}
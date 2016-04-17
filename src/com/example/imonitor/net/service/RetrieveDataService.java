package com.example.imonitor.net.service;

import com.example.imonitor.net.thread.RetrieveDataThread;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

public class RetrieveDataService extends Service {
	public RetrieveDataThread mRDThread = null;
	public static final String ACTION = "com.example.imonitor.net.service.RetrieveDataService";  
	private Handler mHandler ;
    public class SimpleBinder extends Binder{
        /**
         * 获取 Service 实例
         * @return
         */
        public RetrieveDataService getService(){
            return RetrieveDataService.this;
        }
         
        public int add(int a, int b){
            return a + b;
        }	
        public byte[] getData(){
			return null;
        }
    }
    public SimpleBinder sBinder;
    @Override
    public void onCreate() {
        super.onCreate();
        // 创建 SimpleBinder
        sBinder = new SimpleBinder();
        
    }
     
    @Override
    public IBinder onBind(Intent intent) {
        // 返回 SimpleBinder 对象
        return sBinder;
    }
    
}

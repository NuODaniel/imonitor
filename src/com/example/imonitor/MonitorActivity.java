package com.example.imonitor;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;

import com.example.imonitor.net.service.RetrieveDataService;
import com.example.imonitor.util.image.SporeRender;
import com.example.imonitor.util.image.jni.ImageUtilEngine;

public class MonitorActivity extends Activity {
    GLSurfaceView mProcessView;
    LinearLayout mProcessView_Layout;
    static SporeRender mRender;
    static ImageUtilEngine imageEngine;
    
    int mWidth = 0, mHeight = 0;
    private byte[] mTempData; 
    public static final String TAG = "com.example.imonitor.MonitorActivity";  
    
    private static Object INSTANCE_LOCK = new Object();
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_monitor_panel);
        
        imageEngine = new ImageUtilEngine();
        
        mProcessView_Layout = (LinearLayout) findViewById(R.id.monitor_view_layout);
        
        mProcessView = new GLSurfaceView(this);
        mProcessView_Layout.addView(mProcessView);
        mRender = new SporeRender(this);
        mProcessView.setRenderer(mRender);
        bindService(new Intent(RetrieveDataService.ACTION), conn, Context.BIND_AUTO_CREATE);  
    }
    
    public static SporeRender getRender(){
        return mRender;
    }
    
    public static ImageUtilEngine getImageEngine(){
        return imageEngine;
    }
    ServiceConnection conn = new ServiceConnection() {  
        public void onServiceConnected(ComponentName name, IBinder service) {  
        	RetrieveDataService.SimpleBinder sBinder = (RetrieveDataService.SimpleBinder)service;
            Log.v(TAG, "3 + 5 = " + sBinder.add(3, 5));
            Log.v(TAG, sBinder.getService().toString());
            mTempData = sBinder.getData();
        }  
        public void onServiceDisconnected(ComponentName name) {  
            Log.v(TAG, "onServiceDisconnected");  
        }  
    }; 
    Handler beginHandler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			switch(msg.arg1){
			case 0:
				break;
			case 1:

				break;
			}
		}
	};
    
    class ProcessThread extends Thread { 
        public Handler mHandler;
		
        
        public void run() { 
            Looper.prepare(); //创建本线程的Looper并创建一个MessageQueue
     
            mHandler = new Handler() { 
                public void handleMessage(Message msg) { 
                    // process incoming messages here 
                    switch (msg.what) {
                        case 0:
                            processData();
                            break;
                        default:
                            break;
                    }
                } 
            }; 
       
            Looper.loop(); //开始运行Looper,监听Message Queue 
        } 
        public void processData(){
            synchronized (INSTANCE_LOCK){
                int[] buf = MonitorActivity.getImageEngine().decodeYUV420SP(mTempData, mWidth, mHeight);
                MonitorActivity.getRender().mFrameBuf = buf;
                MonitorActivity.getRender().update(buf, mWidth, mHeight);
            }
        }
    } 
}
package com.example.imonitor;


import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
    private byte[] mPreviData;
    public static final String TAG = "com.example.imonitor.MonitorActivity";  
    
    private static Intent retriIntent;
    
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
        
        runRetriService();
    }
    private void runRetriService(){
    	if(retriIntent== null){
    		retriIntent = new Intent();
    		retriIntent.setClass(this, RetrieveDataService.class);
			
			startActivity(retriIntent);
    	}
    }
    Handler retriHandler = new Handler(){
    	@Override
    	public void handleMessage(Message msg) {
    		if(msg.what==1){
    			mPreviData = mTempData;
    			mTempData = (byte[]) msg.obj;
    		}
    	};
    };
    
    public static SporeRender getRender(){
        return mRender;
    }
    
    public static ImageUtilEngine getImageEngine(){
        return imageEngine;
    }
    
    
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
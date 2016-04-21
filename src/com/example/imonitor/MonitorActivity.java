package com.example.imonitor;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.LinearLayout;

import com.example.imonitor.net.service.RetrieveDataService;
import com.example.imonitor.net.thread.EndMonitorThread;
import com.example.imonitor.util.image.SporeRender;
import com.example.imonitor.util.image.jni.ImageUtilEngine;

public class MonitorActivity extends Activity {
    GLSurfaceView mProcessView;
    LinearLayout mProcessView_Layout;
    static SporeRender mRender;
    static ImageUtilEngine imageEngine;
    //monitoring cm id
    private int mCollectionId;
	private int mAccountId;
    
    int mWidth = 0, mHeight = 0;
    private static byte[][] mTempData = new byte[3][]; 
    public static final String TAG = "com.example.imonitor.MonitorActivity";  
    
    private static Intent retriIntent;
    
    private static Object INSTANCE_LOCK = new Object();
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = this.getIntent();
        mCollectionId = i.getIntExtra("collectionid", 0);
        mAccountId = i.getIntExtra("accountid", 0);
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
    		retriIntent.putExtra("MonitorActivity", new Messenger(retriHandler));
    		retriIntent.setClass(this, RetrieveDataService.class);
			
			startService(retriIntent);
    	}
    }
    Handler retriHandler = new Handler(){
    	@Override
    	public void handleMessage(Message msg) {
    		if(msg.what==1){
    			mTempData[2] = mTempData[1];
    			mTempData[1] = mTempData[0];
    			mTempData[0] = (byte[]) msg.obj;
    			mWidth = msg.arg1;
    			mHeight = msg.arg2;
    			//Bundle bundle = 
    			if(mTempData[0]!=null)
    				processData();
    		}
    	};
    };
    
    public static SporeRender getRender(){
        return mRender;
    }
    
    public static ImageUtilEngine getImageEngine(){
        return imageEngine;
    }
    
    

    public void processData(){
        synchronized (INSTANCE_LOCK){
            int[] buf = MonitorActivity.getImageEngine().decodeYUV420SP(mTempData[0], mWidth, mHeight);
            MonitorActivity.getRender().mFrameBuf = buf;
            MonitorActivity.getRender().update(buf, mWidth, mHeight);
        }
    }
    @SuppressWarnings("deprecation")
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event)  
    {  
        if (keyCode == KeyEvent.KEYCODE_BACK )  
        {  
            // 创建退出对话框  
            AlertDialog isExit = new AlertDialog.Builder(this).create();  
            // 设置对话框标题  
            isExit.setTitle("系统提示");  
            // 设置对话框消息  
            isExit.setMessage("确定要退出吗");  
            // 添加选择按钮并注册监听  
            isExit.setButton("确定", new  DialogInterface.OnClickListener(){

				

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					 switch (which)  
			            {  
			            case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
			            	new Thread(new EndMonitorThread(mCollectionId,mAccountId,MonitorActivity.this)).start();
			                finish();  
			                break;  
			            case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框  
			                break;  
			            default:  
			                break;  
			            }  
				}
            	
            });  
            isExit.setButton2("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});  
            // 显示对话框  
            isExit.show();  
  
        }  
          
        return false;  
          
    }  
}
package com.example.imonitor;

import java.io.IOException;

import android.app.Fragment;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MonitorFragment extends Fragment implements Callback,Camera.PreviewCallback
{
	private SurfaceHolder sh;
	private SurfaceView sv;
	private Camera mCamera;
	private int cameraId;
	private boolean flag = true;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		
		return inflater.inflate(R.layout.fragment_monitor, container, false);
		
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sv = (SurfaceView)this.getActivity().findViewById(R.id.surfaceView1);
		sh = sv.getHolder();
		sh.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		sh.addCallback(this);
		
        Button photoButton = (Button) this.getActivity().findViewById(R.id.button1);
        
        photoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	mCamera.stopPreview();
				mCamera.release();
				if (cameraId == 0) {
					cameraId = 1;
				} else {
					cameraId = 0;
				}
				OpenCameraAndSetSurfaceviewSize(cameraId);
				// the surfaceview is ready after the first launch
				SetAndStartPreview(sh);
            }
        });
	};
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		cameraId = 0;// default id
		OpenCameraAndSetSurfaceviewSize(cameraId);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		kill_camera();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		kill_camera();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		SetAndStartPreview(holder);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
	}
	private Void SetAndStartPreview(SurfaceHolder holder) {
		try {
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private Void OpenCameraAndSetSurfaceviewSize(int cameraId) {
		mCamera = Camera.open(cameraId);

		if(flag){
			Log.d("hw",mCamera.getParameters().getPreviewSize().height+"__"+
					mCamera.getParameters().getPreviewSize().width);
			flag = false;
		}
		sv.getLayoutParams().height = mCamera.getParameters().getPreviewSize().height/2;
		sv.getLayoutParams().width = mCamera.getParameters().getPreviewSize().width/2;
		return null;
	}

	private Void kill_camera() {
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
		return null;
	}
	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		
	
	}
}

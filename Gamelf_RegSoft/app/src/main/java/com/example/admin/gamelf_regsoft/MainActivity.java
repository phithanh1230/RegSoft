package com.example.admin.gamelf_regsoft;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;


@SuppressWarnings("ALL")
public class MainActivity extends Activity {
    private Camera mCamera = null;
    private CameraPreview mCameraView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try{
            mCamera = Camera.open();
        } catch (Exception e){
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }
        if(mCamera != null) {
            mCameraView = new CameraPreview(this, mCamera);
            FrameLayout camera_view = (FrameLayout)findViewById(R.id.layout_main);
            camera_view.addView(mCameraView);
        }

    }
}

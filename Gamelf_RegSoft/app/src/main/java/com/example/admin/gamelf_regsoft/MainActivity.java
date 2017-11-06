package com.example.admin.gamelf_regsoft;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;


@SuppressWarnings("ALL")
public class MainActivity extends Activity implements View.OnClickListener {
    private Camera mCamera = null;
    private CameraPreview mCameraView = null;
    Animation anim,anim2,anim3;
    ImageButton btnSetting,btnSound,btnText,btnRealtime,btnExit,btnChup,btnCustom,btnHelp,btnUpload;
    ImageView imgAnh;
    TextView txtObject,txtConfident,txtOther,txtInfo;
    Button btnOK;
    LinearLayout layout_Chup;
    private boolean isClickedSetting=false;
    private boolean isClickedRealtime=false;
    private boolean isClickedText=false;
    private boolean isClickedSound=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = new Intent(this,load.class);
        startActivity(i);
        initSystem();
        anim= AnimationUtils.loadAnimation(this,R.anim.floatout);
        anim2=AnimationUtils.loadAnimation(this,R.anim.flyout);
        anim3=AnimationUtils.loadAnimation(this,R.anim.flyin);
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
/*
    @Override
    protected void onDestroy() {
        mCameraView.close();
    }
*/
    void hideSetting(boolean c)
    {
        if(c)
        {
            btnText.setVisibility(View.INVISIBLE);
            btnRealtime.setVisibility(View.INVISIBLE);
            btnSound.setVisibility(View.INVISIBLE);
            btnExit.setVisibility(View.INVISIBLE);
            btnCustom.setVisibility(View.INVISIBLE);
            btnHelp.setVisibility(View.INVISIBLE);
        }
        else
        {
            btnExit.setVisibility(View.VISIBLE);
            btnRealtime.setVisibility(View.VISIBLE);
            btnCustom.setVisibility(View.VISIBLE);
            btnHelp.setVisibility(View.VISIBLE);
            if(!isClickedRealtime) {
                btnSound.setVisibility(View.VISIBLE);
                btnText.setVisibility(View.VISIBLE);
            }
        }
    }
    private void initSystem() {
        btnSetting=findViewById(R.id.btnSetting);
        btnExit=findViewById(R.id.btnExit);
        btnSound=findViewById(R.id.btnSound);
        btnRealtime=findViewById(R.id.btnRealtime);
        btnText=findViewById(R.id.btnText);
        btnChup=findViewById(R.id.btnChup);
        btnOK=findViewById(R.id.btnOK);
        btnCustom=findViewById(R.id.btnCustom);
        btnUpload=findViewById(R.id.btnUpload);
        btnHelp=findViewById(R.id.btnHelp);
        layout_Chup=findViewById(R.id.layout_show_info);
        txtOther=findViewById(R.id.txtOthers);
        txtInfo=findViewById(R.id.txtInfo);
        txtConfident=findViewById(R.id.txtConfident);
        txtObject=findViewById(R.id.txtObject);
        imgAnh=findViewById(R.id.imgObject);
        //-------------------------------------------
        btnChup.setVisibility(View.GONE);
        btnUpload.setVisibility(View.GONE);
        layout_Chup.setVisibility(View.GONE);
        //-------------------------------------------
        btnSetting.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        btnSound.setOnClickListener(this);
        btnText.setOnClickListener(this);
        btnRealtime.setOnClickListener(this);
        btnOK.setOnClickListener(this);
        btnChup.setOnClickListener(this);
        btnCustom.setOnClickListener(this);
        btnHelp.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //String s;
        switch (view.getId()){
            case R.id.btnSetting:
                isClickedSetting=!isClickedSetting;
                hideSetting(isClickedSetting);
                if(isClickedSetting) {
                    btnSetting.setImageResource(R.drawable.plus);
                }
                else {
                    btnSetting.setImageResource(R.drawable.less);
                    if(!isClickedRealtime) {
                        btnSound.startAnimation(anim);
                        btnText.startAnimation(anim);
                    }
                    btnCustom.setAnimation(anim2);
                    btnHelp.setAnimation(anim2);
                    btnRealtime.startAnimation(anim);
                    btnExit.startAnimation(anim);
                }
                break;
            case R.id.btnRealtime:
                isClickedRealtime=!isClickedRealtime;
                mCameraView.setIsStop(isClickedRealtime);
                if(isClickedRealtime) {
                  // s="Realtime mode turned OFF";
                    btnRealtime.setImageResource(R.drawable.eyeoff);
                    btnSound.setVisibility(View.INVISIBLE);
                    btnText.setVisibility(View.INVISIBLE);
                    btnChup.setVisibility(View.VISIBLE);
                    btnUpload.setVisibility(View.VISIBLE);
                }
                else{
                   // s="Realtime mode turned ON";
                    btnRealtime.setImageResource(R.drawable.eyeon);
                    btnSound.setVisibility(View.VISIBLE);
                    btnText.setVisibility(View.VISIBLE);
                    btnChup.setVisibility(View.INVISIBLE);
                    layout_Chup.setVisibility(View.GONE);
                    btnUpload.setVisibility(View.GONE);
                }
                //Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnExit:
                finish();
                break;
            case R.id.btnText:
                isClickedText=!isClickedText;
                mCameraView.setIsText(!isClickedText);
                if(isClickedText) {
                    //s="Text field has been turned OFF";
                    btnText.setImageResource(R.drawable.text_off);
                }
                else{
                    //s="Text field has been turned ON";
                    btnText.setImageResource(R.drawable.text_on);
                }
                //Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnSound:
                isClickedSound=!isClickedSound;
                mCameraView.setIsSound(!isClickedSound);
                if(isClickedSound) {
                    //s="Sound turned OFF";
                    btnSound.setImageResource(R.drawable.sound_off);
                }
                else{
                    //s="Sound turned ON";
                    btnSound.setImageResource(R.drawable.sound_on);
                }
                //Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnChup:
                layout_Chup.setVisibility(View.VISIBLE);
                layout_Chup.startAnimation(anim3);
                txtObject.setText("");
                txtConfident.setText("");
                txtOther.setText("");
                mCameraView.processTake(imgAnh,txtObject,txtConfident,txtOther);
                break;
            case R.id.btnOK:
                layout_Chup.setVisibility(View.INVISIBLE);
                break;
            case R.id.btnCustom:
                    doOpenSettingAdvanced();
                break;
            case R.id.btnHelp:
                doOpenHelp();
                break;
            case R.id.btnUpload:
                doUploadButton();
        }

    }

    /**/
    private void doUploadButton() {
        Intent i = new Intent(this, reginlinkActivity.class);
        startActivity(i);
    }
    /**/
    private void doOpenHelp() {
        Intent intent = new Intent(MainActivity.this, GuideActivity.class);
        startActivity(intent);
    }


    private void doOpenSettingAdvanced() {
            Intent intent = new Intent(MainActivity.this, Main2Activity.class);
            startActivityForResult(intent,90);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==90 && resultCode==RESULT_OK)
        {
            Bundle bundle = new Bundle();
            bundle=data.getExtras();
            int a=bundle.getInt("ValueConfident");
            int b=bundle.getInt("ValueToastTime");
            boolean c=bundle.getBoolean("ValueUS");
            mCameraView.setConfident(a);
            mCameraView.setTimetoast(b);
            mCameraView.setUS(c);
            String s ="";
            if(c){
                s+="Voice: US | ";
            }else s+="Voice: UK | ";
            s+="Time: "+b+"ms | Confident: "+a+"%";
            txtInfo.setText(s);

        }
    }
}

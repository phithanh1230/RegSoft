package com.example.admin.gamelf_regsoft;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.Image;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.renderscript.Type;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Admin on 24/10/2017.
 */

@SuppressWarnings("ALL")
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback,Camera.PreviewCallback{
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Context mContext;
    int index=0;
    String prevRes=null;
    private boolean isStop=false,isText=true,isSound=true;
    private boolean isProcessingFrame = false;
    Bitmap bitmap;
    /**/
    Recognize_Class reg;
    /**/

    TextToSpeech textToSpeech;
    private boolean isUS=true;
    int timetoast=500;
    int confident=0;
    /**/
    public void setIsStop(boolean c)
    {
        isStop=c;
    }
    public void setIsText(boolean c)
    {
        isText=c;
    }
    public void setIsSound(boolean c)
    {
        isSound=c;
    }

    public void setTimetoast(int a)
    {
        timetoast=a;
    }
    public void setConfident(int a)
    {
        confident=a;
    }

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mContext=context;
        mCamera = camera;
        mCamera.setDisplayOrientation(90);
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }
    public void setUS(boolean c)
    {
        isUS=c;
    }

    public void setChangeVoice(boolean c)
    {
        if(c)
            textToSpeech.setLanguage(Locale.US);
        else
            textToSpeech.setLanguage(Locale.UK);
    }
    private void initSpeakText()
    {
        textToSpeech = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i!=TextToSpeech.ERROR){
                    setChangeVoice(isUS);
                    textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String s) {
                            isProcessingFrame=true;
                        }

                        @Override
                        public void onDone(String s) {
                            isProcessingFrame=false;
                        }

                        @Override
                        public void onError(String s) {
                        }
                    });
                }
            }
        });
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if(mHolder.getSurface() == null)
            return;

        try{
            mCamera.stopPreview();
        } catch (Exception e){
        }
        try{
            mCamera.setPreviewDisplay(mHolder);
            reg=new Recognize_Class(mContext);
            reg.initTensorFlowAndLoadModel();
            initSpeakText();
            mCamera.setPreviewCallback(this);
            mCamera.startPreview();
        } catch (IOException e) {
            mCamera.release();
            mCamera = null;
            Log.d("ERROR", "Camera error on surfaceChanged " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
    @Override
    public void onPreviewFrame(final byte[] bytes, final Camera camera) {
        if(isProcessingFrame) return;
        /**/
            Camera.Parameters parameters = camera.getParameters();
            int width = parameters.getPreviewSize().width;
            int height = parameters.getPreviewSize().height;
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Allocation bmData = reg.renderScriptNV21ToRGBA888(
                    mContext,
                    width,
                    height,
                    bytes);
            bmData.copyTo(bitmap);
            bitmap = Bitmap.createScaledBitmap(bitmap, reg.INPUT_SIZE, reg.INPUT_SIZE, false);
        /**/
        if(!isStop) {
            processImage(bitmap);
        }
    }
    public void processTake(ImageView img, TextView txtObject, TextView txtConfident,TextView txtOther)
    {
        img.setImageBitmap(bitmap);
        try {
            final List<Classifier.Recognition> results = reg.classifier.recognizeImage(bitmap);
            if(results.size()>0)
            {
                txtObject.setText(results.get(0).toString());
                txtConfident.setText((results.get(0).getConfidence()*100)+"%");
                if(results.size()>1)
                {
                    String s="";
                    for(int i=1;i<results.size();i++)
                    {
                        s+=results.get(i).getTitle()+", ";
                    }
                    txtOther.setText(s);
                }

            }
            else
            {
                txtObject.setText("Cant Regconize objects");
                txtConfident.setText("0%");
                txtOther.setText("Please get camera closer to objects");
            }
        }catch (Exception e)
        {
            Log.d("DEBUG",e.toString());
        }
    }

    private void processImage(Bitmap bitmap) {

        try {
            final List<Classifier.Recognition> results = reg.classifier.recognizeImage(bitmap);
            if(results.size()>0 && results.get(0).getConfidence()>confident)
            {
                String res= results.get(0).toString();
                if(!res.equals(prevRes)) {
                   // Log.d("DEBUG", res+"/"+prevRes);
                    String toSpeak = res;
                    if(isSound) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "messageID");
                        textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, map);
                    }
                    prevRes = toSpeak;
                    if(isText) {
                        final Toast t = Toast.makeText(mContext, prevRes, Toast.LENGTH_SHORT);
                        t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        t.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                t.cancel();
                            }
                        },timetoast);
                    }
                }
            }
        }catch (Exception e)
        {
            Log.d("DEBUG",e.toString());
        }
    }
}

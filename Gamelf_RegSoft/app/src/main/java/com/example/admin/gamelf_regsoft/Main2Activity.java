package com.example.admin.gamelf_regsoft;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Main2Activity extends Activity {

    EditText etxtConfident,etxtToastTime;
    Button btnApply,btnCancel;
    RadioGroup rdbGroup;
    RadioButton rdbUS,rdbUK;
    private static boolean isUS=true;//false = UK - true = US
    private static int confident=0,timetoast=500;
    private boolean isApply=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        init();
        addEvents();
    }

    private void addEvents() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isApply){
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("ValueConfident",confident);
                    bundle.putInt("ValueToastTime",timetoast);
                    bundle.putBoolean("ValueUS",isUS);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK,intent);
                }
                finish();
            }
        });
        rdbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.rdbUS) isUS=true;
                else isUS=false;
            }
        });
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etxtConfident.length()>0)
                {
                    confident=Integer.parseInt(etxtConfident.getText().toString());
                    if(confident>100){
                        confident=100;
                        etxtConfident.setText(String.valueOf(confident));
                    }
                }else
                {
                    Toast.makeText(Main2Activity.this, "Bạn chưa nhập giá trị vào Confident", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(etxtToastTime.length()>0){
                    timetoast=Integer.parseInt(etxtToastTime.getText().toString());
                }else{
                    Toast.makeText(Main2Activity.this, "Bạn chưa nhập thời gian hiển thị văn bản", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Toast.makeText(Main2Activity.this, confident+"/"+timetoast+"/"+isUS, Toast.LENGTH_SHORT).show();
                isApply=true;
                btnApply.setEnabled(false);
            }
        });
    }


    private void init() {
        etxtConfident= (EditText) findViewById(R.id.etxtConfident);
        etxtToastTime= (EditText) findViewById(R.id.etxttimetoast);
        btnApply= (Button) findViewById(R.id.btnApply);
        btnCancel= (Button) findViewById(R.id.btnCancel);
        rdbGroup= (RadioGroup) findViewById(R.id.rdbGroup);
        rdbUS= (RadioButton) findViewById(R.id.rdbUS);
        rdbUK= (RadioButton) findViewById(R.id.rdbUK);
        etxtConfident.setText(confident+"");
        etxtToastTime.setText(timetoast+"");
        if(isUS)
            rdbUS.setChecked(true);
        else rdbUK.setChecked(true);
    }


}

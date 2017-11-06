package com.example.admin.gamelf_regsoft;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class reginlinkActivity extends Activity {
    EditText etxtUrl;
    ImageView imgRilAnh;
    Button btnSubmit,btnRilClose,btnClear,btnDemo1,btnDemo2;
    String link1="https://kids.nationalgeographic.com/content/dam/kids/photos/articles/Other%20Explore%20Photos/R-Z/Wacky%20Weekend/Wild%20Cats/ww-wild-cats-tiger.adapt.945.1.jpg";
    String link2="https://i.pinimg.com/736x/bc/bb/99/bcbb994b11173de5c0a19525b5192a70--zebra-makeup-animal-makeup.jpg";
    TextView txtAnh;
    boolean error = false;
    ProgressDialog dialog;
    Recognize_Class reg=new Recognize_Class(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reginlink);
        inits();
    }

    private void inits() {
        reg.initTensorFlowAndLoadModel();
        etxtUrl=findViewById(R.id.etxtUrl);
        btnSubmit=findViewById(R.id.btnSubmit);
        btnRilClose=findViewById(R.id.btnRilClose);
        imgRilAnh=findViewById(R.id.imgRilAnh);
        btnDemo1=findViewById(R.id.btnDemo1);
        btnDemo2=findViewById(R.id.btnDemo2);
        btnClear=findViewById(R.id.btnClear);
        txtAnh=findViewById(R.id.txtTenAnh);
        dialog=new ProgressDialog(this);
        dialog.setTitle("Thông báo");
        dialog.setMessage("Đang tải hình");
        dialog.setCanceledOnTouchOutside(false);
        btnDemo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etxtUrl.setText(link1);
            }
        });
        btnDemo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etxtUrl.setText(link2);
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etxtUrl.setText("");
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etxtUrl.length()==0)
                {
                    Toast.makeText(reginlinkActivity.this, "Xin hãy nhập đường dẫn !", Toast.LENGTH_SHORT).show();
                    return;

                }
                else
                {
                    String url = etxtUrl.getText().toString();
                    ImageTask imageTask=new ImageTask();
                    imageTask.execute(url);
                }
            }
        });
        btnRilClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reg.close();

    }

    class ImageTask extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(!error) {
                imgRilAnh.setImageBitmap(bitmap);
                bitmap = Bitmap.createScaledBitmap(bitmap, reg.INPUT_SIZE, reg.INPUT_SIZE, false);
                final List<Classifier.Recognition> results = reg.classifier.recognizeImage(bitmap);
                if (results.size() > 0) {
                    txtAnh.setText(results.get(0).getTitle().toUpperCase());
                }
                dialog.dismiss();
            }
            else
            {
                dialog.setMessage("Không thể tải hình");
                dialog.dismiss();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String link=strings[0];
            try {
                Bitmap bitmap= BitmapFactory.decodeStream((InputStream) new URL(link).getContent());
                return bitmap;
            } catch (IOException e) {
                error=true;
                e.printStackTrace();
            }
            return null;
        }
    }
}

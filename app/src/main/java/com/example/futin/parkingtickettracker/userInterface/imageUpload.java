package com.example.futin.parkingtickettracker.userInterface;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.futin.parkingtickettracker.R;
import com.example.futin.parkingtickettracker.RESTService.RestService;
import com.example.futin.parkingtickettracker.RESTService.interfaces.AsyncTaskReturnData;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class imageUpload extends Activity implements View.OnClickListener,AsyncTaskReturnData

{

    private static final int CAMERA_REQUEST=1000;
    Button btnChoose;
    Button btnSubmit;
    ImageView imgForUpload;
    RestService restService;

    String fileName="";
    String filePath="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        restService=new RestService(this);
        btnChoose = (Button) findViewById(R.id.btnChoose);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        imgForUpload = (ImageView) findViewById(R.id.imgForUpload);
        setImageSize();
        btnChoose.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    void setImageSize(){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int height=metrics.heightPixels-50;
        int width=metrics.widthPixels-metrics.widthPixels/3;

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        imgForUpload.setLayoutParams(layoutParams);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnChoose:
                Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,CAMERA_REQUEST);
                break;
            case R.id.btnSubmit:
                restService.uploadImage(fileName,filePath);
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Log.i("test", data.getData().getEncodedPath());
            filePath=data.getData().getEncodedPath();
            fileName="ticketPhoto";
            imgForUpload.setImageBitmap(photo);


        }
    }

    @Override
    public void returnDataOnPostExecute(Object obj) {

    }


}


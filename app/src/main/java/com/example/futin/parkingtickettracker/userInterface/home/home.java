package com.example.futin.parkingtickettracker.userInterface.home;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.futin.parkingtickettracker.R;
import com.example.futin.parkingtickettracker.RESTService.RestService;
import com.example.futin.parkingtickettracker.RESTService.interfaces.AsyncTaskReturnData;
import com.example.futin.parkingtickettracker.RESTService.loader.LoadFiles;
import com.example.futin.parkingtickettracker.RESTService.response.RSUploadImageResponse;
import com.example.futin.parkingtickettracker.userInterface.gallery.Gallery;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Home extends Activity implements View.OnClickListener, AsyncTaskReturnData

{

    RSUploadImageResponse response;
    RestService restService;
    LoadFiles fileLoader;

    private static final int CAMERA_REQUEST = 1000;
    Button btnChoose;
    Button btnSubmit;
    ImageView imgForUpload;
    ProgressDialog progressDialog;

    Uri savedImage = null;
    String fileName = "";
    String filePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        progressDialog = new ProgressDialog(this);
        restService = new RestService(this);
        btnChoose = (Button) findViewById(R.id.btnChoose);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        imgForUpload = (ImageView) findViewById(R.id.imgForUpload);
        btnChoose.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        setImageSize();
        readWritePermission();
    }

    void setImageSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int height = metrics.heightPixels - 50;
        int width = metrics.widthPixels - metrics.widthPixels / 3;

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        imgForUpload.setLayoutParams(layoutParams);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnChoose:
                
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    savedImage = createFolder();
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, savedImage);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
                /*
                Intent i = new Intent(Home.this, Gallery.class);
                startActivity(i);
                finish();
                */
                break;
            case R.id.btnSubmit:
                progressDialog.setMessage("Loading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setProgress(0);
                progressDialog.setCancelable(false);
                progressDialog.show();

                restService.uploadImage(fileName, filePath);
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Log.i("URI: ", savedImage.getPath() + "");

            Bitmap img = BitmapFactory.decodeFile(savedImage.getPath());
            filePath = savedImage.getPath();
            imgForUpload.setImageBitmap(img);

        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void readWritePermission() {
        if (shouldAskPermission()) {
            String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
            int permsRequestCode = 200;
            requestPermissions(perms, permsRequestCode);
        }

    }

    private Uri createFolder() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "ParkingTicketImages");
        if(!imagesFolder.exists()) {
            imagesFolder.mkdirs();
        }

        File image = new File(imagesFolder, "PTT_" + timeStamp + ".jpg");
        fileName = image.getName();
        return Uri.fromFile(image);
    }

    @Override
    public void returnDataOnPostExecute(Object obj) {
        progressDialog.dismiss();
        response = (RSUploadImageResponse) obj;
        if(!response.getFileName().equalsIgnoreCase("")) {
            makeToast(response.getFileName() + " successfully uploaded");
        }
        else{
            if(!response.getError().equalsIgnoreCase("")){
                Log.i("ImageUpload",response.getError());
                makeToast(response.getError());
            }else{
                Log.i("ImageUpload",response.getStatus()+" "+response.getStatusName());
                makeToast(response.getStatus()+" "+response.getStatusName());

            }
        }
    }

    private boolean shouldAskPermission() {

        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);

    }

    private void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }


}


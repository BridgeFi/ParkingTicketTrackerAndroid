package com.example.futin.parkingtickettracker.userInterface.home;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.futin.parkingtickettracker.R;
import com.example.futin.parkingtickettracker.RESTService.RestService;
import com.example.futin.parkingtickettracker.RESTService.listeners.AsyncTaskReturnData;
import com.example.futin.parkingtickettracker.RESTService.response.RSUploadImageResponse;
import com.example.futin.parkingtickettracker.userInterface.gallery.Gallery;
import com.example.futin.parkingtickettracker.userInterface.util.Util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Home extends AppCompatActivity implements View.OnClickListener, AsyncTaskReturnData {

    RestService restService;
    RSUploadImageResponse response;

    Button btnChoose;
    Button btnSubmit;
    ImageView imgForUpload;
    ProgressDialog progressDialog;

    private static final int CAMERA_REQUEST = 1000;
    private final String TAG="Home";
    private Uri savedImage = null;
    private String fileName = "";
    private String filePath = "";
    private String android_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        progressDialog = new ProgressDialog(this);
        restService = new RestService(this);
        btnChoose = (Button) findViewById(R.id.btnChoose);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        imgForUpload = (ImageView) findViewById(R.id.imgForUpload);

        btnChoose.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        setImageSize();
        readWritePermission();
//        getUniqueAndroidId();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_gallery:
                Intent i = new Intent(Home.this, Gallery.class);
                startActivity(i);
                break;
        }
                return super.onOptionsItemSelected(item);
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
                break;
            case R.id.btnSubmit:
                progressDialog.setMessage("Loading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setProgress(0);
                progressDialog.setCancelable(false);
                progressDialog.show();

                restService.uploadImage(fileName, filePath);
                break;
//               restService.sendSms("Hello Twilio App");
        }
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
    /*
    * Setting image size for different screen sizes
    * */
    void setImageSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(height, width);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        imgForUpload.setLayoutParams(layoutParams);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Log.i(TAG, savedImage.getPath() + " is img path");

            Bitmap img = BitmapFactory.decodeFile(savedImage.getPath());
            filePath = savedImage.getPath();

            Util.getOInstance().checkOrientation(filePath,imgForUpload);
            imgForUpload.setImageBitmap(img);
        }
    }
    /*
    * For newer version(6.0+) there is new permission system that need to be called in runtime
    * */
    @TargetApi(Build.VERSION_CODES.M)
    private void readWritePermission() {
        if (shouldAskPermission()) {
            String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
            int permsRequestCode = 200;
            requestPermissions(perms, permsRequestCode);
        }
    }
    /*
    * Creating folder for storing captured images and giving them unique name
    * */
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
    /*
    * Another check for device SDK version
    * */
    private boolean shouldAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
    /*
    * Might need later for getting unique ID and storing files using this ID
    * */
    public void getUniqueAndroidId(){
          android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.i(TAG,"Android ID: "+android_id);
    }
}


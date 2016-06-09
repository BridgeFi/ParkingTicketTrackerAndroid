package com.example.futin.parkingtickettracker.userInterface.gallery.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.futin.parkingtickettracker.R;
import com.example.futin.parkingtickettracker.RESTService.RestService;
import com.example.futin.parkingtickettracker.RESTService.listeners.AsyncTaskReturnData;
import com.example.futin.parkingtickettracker.RESTService.listeners.FileChangeListener;
import com.example.futin.parkingtickettracker.RESTService.loader.LoadFiles;
import com.example.futin.parkingtickettracker.RESTService.response.RSUploadImageResponse;
import com.example.futin.parkingtickettracker.userInterface.util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;


/**
 * Created by Futin on 6/7/16.
 */
public class ImagePagerAdapter extends PagerAdapter  implements AsyncTaskReturnData{

    FileChangeListener listener;
    LoadFiles loadFiles;
    RestService restService;
    RSUploadImageResponse response;

    Context context;
    LayoutInflater inflater;
    ViewPager viewPager;
    Button btnSubmitFromGallery;
    ProgressDialog progressDialog;

    private final String TAG="ImagePagerAdapter";

    public ImagePagerAdapter(Context context, ViewPager viewPager, LoadFiles loadFiles) {
        this.context = context;
        this.viewPager = viewPager;
        this.loadFiles=loadFiles;
        restService=new RestService(this);
        progressDialog=new ProgressDialog(context);
        listener= (FileChangeListener) context;
    }

    @Override
    public int getCount() {
        return loadFiles.getListSize();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container,final int position) {
        ImageView imageView;

        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.single_image_pager, container, false);

        imageView= (ImageView) viewLayout.findViewById(R.id.singleImagePagerView);
        btnSubmitFromGallery= (Button) viewLayout.findViewById(R.id.btnSubmitFromGallery);

        btnSubmitFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"on image click");
                progressDialog.setMessage("Loading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setProgress(0);
                progressDialog.setCancelable(false);
                progressDialog.show();

                restService.uploadImage(loadFiles.getFileFromList(position).getName(),
                        loadFiles.getFileFromList(position).getPath());
            }
        });
        //decode file from disc
        Bitmap bitmap = decodeFile(loadFiles.getFileFromList(position));
        //Using util class for checking orientation of saved file
        Util.getOInstance().checkOrientation(loadFiles.getFileFromList(position).getPath(),imageView);

        imageView.setImageBitmap(bitmap);
        container.addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }

    void makeToast(String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public Bitmap decodeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1 = new FileInputStream(f);
            BitmapFactory.decodeStream(stream1, null, o);
            stream1.close();

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 128;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            FileInputStream stream2 = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;
        } catch (Throwable e) {
            e.printStackTrace();
            if (e instanceof OutOfMemoryError){
                Log.i(TAG,"Out of memory");
            }
            return null;
        }
    }
    /*
    * Response from upload action
    * */
    @Override
    public void returnDataOnPostExecute(Object obj) {
        progressDialog.dismiss();
        listener.closeViewPager();
        response = (RSUploadImageResponse) obj;
        if (!response.getFileName().equalsIgnoreCase("")) {
            makeToast(response.getFileName() + " successfully uploaded");
        } else {
            if (!response.getError().equalsIgnoreCase("")) {
                Log.i("ImageUpload", response.getError());
                makeToast(response.getError());
            } else {
                Log.i("ImageUpload", response.getStatus() + " " + response.getStatusName());
                makeToast(response.getStatus() + " " + response.getStatusName());
            }
        }
    }
}

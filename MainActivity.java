package com.example.filepickerdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.filepickerdemo.databinding.ActivityMainBinding;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mBinding;
    MainActivity ctx = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(ctx,R.layout.activity_main);
    }

    public void onClickPickImage(View view) {

        if(ContextCompat.checkSelfPermission(ctx, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(ctx, new String[]{Manifest.permission.CAMERA}, 1);
        }
        else{
            imagePicker();
        }
    }

    private void imagePicker() {

        Intent intent = new Intent(ctx, FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setShowImages(true)
                .setShowVideos(false)
                .enableImageCapture(true)
                .setMaxSelection(1)
                .setSkipZeroSizeFiles(true)
                .build());

        startActivityForResult(intent, 101);
    }

    public void onClickPickFile(View view) {

        Intent intent = new Intent(ctx, FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setShowFiles(true)
                .setSuffixes("txt","pdf","doc","docs")
                .setShowImages(false)
                .setShowVideos(false)
                .setMaxSelection(1)
                .setSkipZeroSizeFiles(true)
                .build());

        startActivityForResult(intent, 102);
    }

    public void onClickPickAudio(View view) {

        Intent intent = new Intent(ctx, FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setShowAudios(true)
                .setShowImages(false)
                .setShowVideos(false)
                .setMaxSelection(1)
                .setSkipZeroSizeFiles(true)
                .build());

        startActivityForResult(intent, 103);
    }

    public void onClickPickVideo(View view) {

        if(ContextCompat.checkSelfPermission(ctx, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(ctx, new String[]{Manifest.permission.CAMERA}, 2);
        }
        else{
            videoPicker();
        }
    }

    private void videoPicker() {Intent intent = new Intent(ctx, FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setShowVideos(true)
                .setShowImages(false)
                .enableVideoCapture(true)
                .setMaxSelection(1)
                .setSkipZeroSizeFiles(true)
                .build());

        startActivityForResult(intent, 104);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if((grantResults.length>0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED))
        {
            if(requestCode == 1){
                imagePicker();
            }
            else{   // when request code is 2.
                videoPicker();
            }
        }
        else{
            Toast.makeText(ctx, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && data != null)
        {
            ArrayList<MediaFile> mediaFiles = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);

            String path = mediaFiles.get(0).getPath();

            Log.d("SHIV", path);

            switch (requestCode){

                case 101:
                    Toast.makeText(ctx, "Image Path : " + path, Toast.LENGTH_LONG).show();
                    Glide.with(ctx).load(path).apply(new RequestOptions().override(200, 200)).centerCrop().into(mBinding.ivImage);
                    break;
                case 102:
                    Toast.makeText(ctx, "File Path : " + path, Toast.LENGTH_LONG).show();
                    break;
                case 103:
                    Toast.makeText(ctx, "Audio Path : " + path, Toast.LENGTH_LONG).show();
                    break;
                case 104:
                    Toast.makeText(ctx, "Video Path : " + path, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}

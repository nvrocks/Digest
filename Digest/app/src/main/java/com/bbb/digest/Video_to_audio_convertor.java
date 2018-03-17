package com.bbb.digest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.madhavanmalolan.ffmpegandroidlibrary.Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.IConvertCallback;
import cafe.adriel.androidaudioconverter.model.AudioFormat;

public class Video_to_audio_convertor extends AppCompatActivity {


    public static final int PICK_VIDEO_REQUEST= 100;
    private Uri filePath_video;
    ImageView thumbnail;
    java.util.Date date= new java.util.Date();
    String ad = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date.getTime());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_to_audio_convertor);
        RequestRunTimePermission();
        //Util.requestPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        //Util.requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Button openVideo=(Button)findViewById(R.id.open);
        Button one=(Button)findViewById(R.id.one);
        thumbnail = (ImageView) findViewById(R.id.thumbnail);
        openVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("video/mp4");
                startActivityForResult(Intent.createChooser(intent, "Select a video"), PICK_VIDEO_REQUEST);

            }
        });



        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                filePath_video = data.getData();
                String path = FilePath.getPath(this, filePath_video);
                System.out.println("Path: "+path);
                MediaMetadataRetriever media = new MediaMetadataRetriever();
                media.setDataSource(getApplicationContext(),filePath_video);
                Bitmap extractedImage = media.getFrameAtTime(100000);

                final String filein = path;
                final String fileoutaudio = getExternalFilesDir(null).getAbsoluteFile()+"/out"+ad+".aac";

                Bitmap ThumbVideo = ThumbnailUtils.extractThumbnail(extractedImage, 200, 200);

                thumbnail.setImageBitmap(ThumbVideo);

                Controller.getInstance().run(new String[]{
                        "-y",
                        "-i",
                        filein,
                        "-vn",
                        "-acodec",
                        "copy",
                        fileoutaudio
                });

                System.out.println("Path: "+path);
                Toast.makeText(this,"Video imported",Toast.LENGTH_LONG).show();

            }
            else if (resultCode == RESULT_OK && data != null && data.getData() != null) {
               Toast.makeText(this,"Corrupt file",Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    public void RequestRunTimePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(Video_to_audio_convertor.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
        } else {
            ActivityCompat.requestPermissions(Video_to_audio_convertor.this,new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(Video_to_audio_convertor.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        } else {
            ActivityCompat.requestPermissions(Video_to_audio_convertor.this,new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] Result) {
        switch (RC) {
            case 1:
                if (Result.length > 0 && Result[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                break;
        }
    }

    public String getRealPathFromURI(Uri contentUri) {

        // can post image
        String [] proj={MediaStore.Video.Media.DATA};
        Cursor cursor = managedQuery( contentUri,
                proj, // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    public void convertAudio(View v){
        /**
         *  Update with a valid audio file!
         *  Supported formats: {@link AndroidAudioConverter.AudioFormat}
         */

        InputStream in = null;

        String path=getExternalFilesDir(null).getAbsoluteFile()+"/out"+ad+".aac";
        File wavFile = new File(path);
        IConvertCallback callback = new IConvertCallback() {
            @Override
            public void onSuccess(File convertedFile) {
                Toast.makeText(Video_to_audio_convertor.this, "SUCCESS: " + convertedFile.getPath(), Toast.LENGTH_LONG).show();

                Intent i = new Intent(Video_to_audio_convertor.this, Audio_to_text.class);
                i.putExtra("path", ad);
                startActivity(i);
            }
            @Override
            public void onFailure(Exception error) {
                Toast.makeText(Video_to_audio_convertor.this, "ERROR: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };
        Toast.makeText(this, "Converting audio file...", Toast.LENGTH_SHORT).show();

        AndroidAudioConverter.with(this)
                .setFile(wavFile)
                .setFormat(AudioFormat.MP3)
                .setCallback(callback)
                .convert();


    }
}

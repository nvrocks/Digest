package com.bbb.testt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.madhavanmalolan.ffmpegandroidlibrary.Controller;

public class VidToText extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String filein = getExternalFilesDir(null).getAbsoluteFile()+"/in.mp4";
        final String fileoutaudio = getExternalFilesDir(null).getAbsoluteFile()+"/out.aac";
        final String fileoutvideo = getExternalFilesDir(null).getAbsoluteFile()+"/outv.mp3";

        setContentView(R.layout.activity_vid_to_text);

//        Button one=(Button)findViewById(R.id.one);
  //      Button two=(Button)findViewById(R.id.two);

        //one.setOnClickListener(new View.OnClickListener() {
          //  @Override
         //   public void onClick(View v) {
                Controller.getInstance().run(new String[]{
                        "-y",
                        "-i",
                        filein,
                        "-vn",
                        "-acodec",
                        "copy",
                        fileoutaudio
                });

         //   }
       // });

    }
}

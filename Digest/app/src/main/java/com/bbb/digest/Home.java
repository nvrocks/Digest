package com.bbb.digest;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    ImageView stt, ocr, vtt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        stt = (ImageView) findViewById(R.id.stt);
        ocr = (ImageView) findViewById(R.id.ocr);
        vtt = (ImageView) findViewById(R.id.vtt);
        TextView head=(TextView)findViewById(R.id.digest);

        final Animation anim,revanim,animback,revback,vib;

        anim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.front_trans);
        revanim=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.reverse_trans);
        animback=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.front_back);
        revback=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.reverse_back);
        vib=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.vibr);
        Typeface tf = Typeface.createFromAsset(this.getAssets(),"FFF_Tusj.ttf");
        head.setTypeface(tf);
        ocr.startAnimation(anim);
        vtt.startAnimation(anim);
        stt.startAnimation(revanim);

       /* vib.setStartOffset(500);
        ocr.startAnimation(vib);
        vtt.startAnimation(vib);
        stt.startAnimation(vib);*/

        stt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ocr.startAnimation(animback);
                vtt.startAnimation(animback);
                stt.startAnimation(revback);
                Intent i1 = new Intent(Home.this, Audio_to_text.class);
                i1.putExtra("path","0");
                startActivity(i1);
            }
        });

        ocr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2 = new Intent(Home.this, Ocr.class);
                startActivity(i2);
            }
        });

        vtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i3 = new Intent(Home.this, Video_to_audio_convertor.class);
                startActivity(i3);
            }
        });
    }
}

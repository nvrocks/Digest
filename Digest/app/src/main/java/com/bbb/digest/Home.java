package com.bbb.digest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    Button stt, ocr, vtt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        stt = (Button) findViewById(R.id.stt);
        ocr = (Button) findViewById(R.id.ocr);
        vtt = (Button) findViewById(R.id.vtt);

        stt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(Home.this, Audio_to_text.class);
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

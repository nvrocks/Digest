package com.bbb.digest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class Ocr extends AppCompatActivity {

    ImageView ocrimg;
    Button ocrbutton;
    TextView ocrtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        ocrimg = (ImageView) findViewById(R.id.ocrimg);
        ocrtext = (TextView) findViewById(R.id.ocrtext);
        ocrbutton = (Button) findViewById(R.id.ocrbutton);

        final Bitmap bitmap = BitmapFactory.decodeResource(
                getApplicationContext().getResources(), R.drawable.text4
        );

        ocrimg.setImageBitmap(bitmap);

        ocrbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if(!textRecognizer.isOperational())
                    Log.e("ERROR", "Detector dependencies are not yet available!");
                else{
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = textRecognizer.detect(frame);
                    StringBuilder stringBuilder = new StringBuilder();

                    for(int i = 0; i < items.size(); ++i){
                        TextBlock item = items.valueAt(i);
                        stringBuilder.append(item.getValue());
                        stringBuilder.append("\n");
                    }

                    ocrtext.setText(stringBuilder.toString());
                }
            }
        });
    }
}

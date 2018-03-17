package com.bbb.testt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

public class Ocr extends AppCompatActivity {

    ImageView ocrimg;
    Button ocrbutton;
    TextView ocrtext;
    public static final int PICK_IMAGE_REQUEST = 123;
    private Uri filePath_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        ocrimg = (ImageView) findViewById(R.id.ocrimg);
        ocrtext = (TextView) findViewById(R.id.ocrtext);
        ocrbutton = (Button) findViewById(R.id.ocrbutton);

        /*final Bitmap bitmap = BitmapFactory.decodeResource(
                getApplicationContext().getResources(), R.drawable.text4
        );

        ocrimg.setImageBitmap(bitmap);*/

        ocrbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent;
                intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select an audio"), PICK_IMAGE_REQUEST);

                /*TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
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
                }*/
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                filePath_photo = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath_photo);
                    ocrimg.setImageBitmap(bitmap);

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

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}

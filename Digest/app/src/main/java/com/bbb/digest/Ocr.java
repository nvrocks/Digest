package com.bbb.digest;

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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

import okhttp3.OkHttpClient;

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
                        printt(stringBuilder.toString());
                        //ocrtext.setText(stringBuilder.toString());
                       /* Intent i=new Intent(this,summarize.class);
                        i.putExtra("text",stringBuilder.toString());
                        startActivity(i);*/
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

    public void printt(String s){

        AndroidNetworking.initialize(getApplicationContext());
        OkHttpClient okHttpClient = new OkHttpClient() .newBuilder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        AndroidNetworking.initialize(getApplicationContext(),okHttpClient);

        //s = "Cricket is a bat-and-ball game played between two teams of eleven players each on a cricket field, at the centre of which is a rectangular 22-yard-long (20 metres) pitch with a target at each end called the wicket (a set of three wooden stumps upon which two bails sit). Each phase of play is called an innings, during which one team bats, attempting to score as many runs as possible, whilst their opponents bowl and field, attempting to minimise the number of runs scored. When each innings ends, the teams usually swap roles for the next innings (i.e. the team that previously batted will bowl/field, and vice versa). The teams each bat for one or two innings, depending on the type of match. The winning team is the one that scores the most runs, including any extras gained (except when the result is not a win/loss result).";
        s = "http://192.168.43.124:8000/run/?str="+s;


        AndroidNetworking.get(s)
                .addPathParameter("pageNumber", "0")
                .addQueryParameter("limit", "3")
                .addHeaders("token", "1234")
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String s) {
                        Log.d("CHECK",s);
                        int  i, start = 0, end = 0;
                        for(i = 0; i < s.length(); i++){
                            if(s.charAt(i) == '<' && s.charAt(i+1) == 'p' & s.charAt(i+2) == '>')
                                start = i+3;
                            else if(s.charAt(i) == '<' && s.charAt(i+1) == '/' && s.charAt(i+2) == 'p' && s.charAt(i+3) == '>')
                                end = i-1;
                        }
                        s = s.substring(start, end);
                        ocrtext.setText(s);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("CHECKKKK","EROOR OCCURED");

                    }
                });
    }
}

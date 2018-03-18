package com.bbb.digest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import okhttp3.OkHttpClient;

public class summarize extends AppCompatActivity {

    TextView tv;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summarize);

        tv = (TextView)findViewById(R.id.textView);
        String s = getIntent().getStringExtra("text");

        progressDialog=new ProgressDialog(summarize.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Generating the summary...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        AndroidNetworking.initialize(getApplicationContext());
        OkHttpClient okHttpClient = new OkHttpClient() .newBuilder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        AndroidNetworking.initialize(getApplicationContext(),okHttpClient);

     //   s = "Cricket is a bat-and-ball game played between two teams of eleven players each on a cricket field, at the centre of which is a rectangular 22-yard-long (20 metres) pitch with a target at each end called the wicket (a set of three wooden stumps upon which two bails sit). Each phase of play is called an innings, during which one team bats, attempting to score as many runs as possible, whilst their opponents bowl and field, attempting to minimise the number of runs scored. When each innings ends, the teams usually swap roles for the next innings (i.e. the team that previously batted will bowl/field, and vice versa). The teams each bat for one or two innings, depending on the type of match. The winning team is the one that scores the most runs, including any extras gained (except when the result is not a win/loss result).";
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
                        tv.setText(Html.fromHtml(s));
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.d("CHECKKKK","EROOR OCCURED");

                    }
                });
    }

}

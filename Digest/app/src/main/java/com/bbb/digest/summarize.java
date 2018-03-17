package com.bbb.digest;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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

public class summarize extends AppCompatActivity {

    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summarize);

        tv = (TextView)findViewById(R.id.textView);
        String s = getIntent().getStringExtra("text");
        summarize(s);
    }

    void summarize(final String s){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {

                String st = getServerResponse(s);
                return st;
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);

                if(response != null)
                tv.setText(response);
            }
        }.execute();
    }

    private String getServerResponse(String s) {
        try {
            URL url = new URL("http://192.168.43.124:8000/run/?str="+s);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);

/*            OutputStream os = con.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write(s);

            bw.flush();
            bw.close();
            os.close();
*/
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String data = "", line;

            while ((line = in.readLine()) != null) {
                data+=line;
            }
            Log.d("CHECK", data);
            in.close();
            return data;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package com.bbb.digest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.RecognizeCallback;
//import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Audio_to_text extends AppCompatActivity {

    private RecyclerView recyclerView;
    //    private ChatAdapter mAdapter;
    private ArrayList messageArrayList;
    private TextView inputMessage;
    private Button btnSend;
    private TextView btnRecord;
    //private Map<String,Object> context = new HashMap<>();
    com.ibm.watson.developer_cloud.conversation.v1.model.Context context = null;
   // StreamPlayer streamPlayer;
    private boolean initialRequest;
    private boolean permissionToRecordAccepted = false;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String TAG = "MainActivity";
    private static final int RECORD_REQUEST_CODE = 101;
    private boolean listening = false;
    private SpeechToText speechService;
   // private MicrophoneInputStream capture;
    //    private SpeakerLabelsDiarization.RecoTokens recoTokens;
   // private MicrophoneHelper microphoneHelper;
    public static final int PICK_AUDIO_REQUEST = 234, PICK_FILE_REQUEST = 123;
    private Uri filePath_audio;
    InputStream in = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_to_text);

        inputMessage = (TextView) findViewById(R.id.rcd_msg2);
        final Button summarize=(Button)findViewById(R.id.ok);
        String flag=getIntent().getStringExtra("path");

        if(!flag.equalsIgnoreCase("0")){
            try {
             //   btnRecord.setVisibility(View.INVISIBLE);
                final String path = getExternalFilesDir(null).getAbsoluteFile()+"/out"+flag+".mp3";
                //File theFile =new File(path);
                in = new FileInputStream(path);
                recordMessage();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else{
            btnRecord= (TextView) findViewById(R.id.btn_record2);

            btnRecord.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {

                    Intent intent;
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("audio/*");
                    startActivityForResult(Intent.createChooser(intent, "Select an audio"), PICK_AUDIO_REQUEST);
                }
            });
        }

        summarize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Audio_to_text.this,summarize.class);
                i.putExtra("text",inputMessage.getText().toString());
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == PICK_AUDIO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                filePath_audio = data.getData();
//                String filePath = data.getData().getPath();

                try {
                    in = getContentResolver().openInputStream(filePath_audio);
                    recordMessage();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    // Speech-to-Text Record Audio permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
            case RECORD_REQUEST_CODE: {
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission has been denied by user");
                } else {
                    Log.i(TAG, "Permission has been granted by user");
                }
                return;
            }
        }
        if (!permissionToRecordAccepted ) finish();
    }

    //Record a message via Watson Speech to Text
    private void recordMessage() {
        //mic.setEnabled(false);
        /*
        Watson credentials:
        url: ttps://stream.watsonplatform.net/speech-to-text/api
        username: a1db2e54-8972-48dc-81c5-7adc9f39b5c3
        password: McoMYPdkDsjF
        */
        speechService = new SpeechToText();
        speechService.setUsernameAndPassword("beccabc7-a6d6-4aaf-bad7-f7560feb0b61", "i4haiTjew7xJ");

        RecognizeOptions options = new RecognizeOptions.Builder()
                .model("en-US_BroadbandModel").contentType("audio/mp3")
                .interimResults(true).maxAlternatives(3)
                .keywords(new String[]{"colorado", "tornado", "tornadoes"})
                .keywordsThreshold(0.5).build();

        BaseRecognizeCallback callback = new BaseRecognizeCallback() {
            @Override
            public void onTranscription(SpeechResults speechResults) {
                String text = speechResults.getResults().get(0).getAlternatives().get(0).getTranscript();
                if(speechResults.getResults().get(0).isFinal()) {
                    showMicText(text);
                }
                System.out.println(speechResults);
            }

        };

        /*try {
            in = getAssets().open("speech.mp3");
        }
        catch (Exception e){
            e.printStackTrace();
        }*/

        speechService.recognizeUsingWebSocket(in, options, callback);
    }

    //Private Methods - Speech to Text
   /* private RecognizeOptions getRecognizeOptions() {
        return new RecognizeOptions.Builder()
                .contentType(ContentType.OPUS.toString())
                //.model("en-UK_NarrowbandModel")
                .interimResults(true)
                .inactivityTimeout(2000)
                .build();
    }*/
    //Watson Speech to Text Methods.
    private class MicrophoneRecognizeDelegate implements RecognizeCallback {
        @Override
        public void onTranscription(SpeechResults speechResults) {
            System.out.println(speechResults);
            if(speechResults.getResults() != null && !speechResults.getResults().isEmpty()) {
                String text = speechResults.getResults().get(0).getAlternatives().get(0).getTranscript();
                if(speechResults.isFinal()) {
                    showMicText(text);
                }
            }
        }
        @Override public void onConnected() {
        }
        @Override public void onError(Exception e) {
            showError(e);
            enableMicButton();
        }
        @Override public void onDisconnected() {
            enableMicButton();
        }

        @Override
        public void onInactivityTimeout(RuntimeException runtimeException) {

        }

        @Override
        public void onListening() {

        }

        @Override
        public void onTranscriptionComplete() {

        }
    }
    private void showMicText(final String text) {
        runOnUiThread(new Runnable() {
            @Override public void run() {
                String ftext=inputMessage.getText().toString()+text+". ";
                inputMessage.setText(ftext);
            }
        });
    }
    private void enableMicButton() {
        runOnUiThread(new Runnable() {
            @Override public void run() {
                btnRecord.setEnabled(true);
            }
        });
    }
    private void showError(final Exception e) {
        runOnUiThread(new Runnable() {
            @Override public void run() {
                Toast.makeText(Audio_to_text.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }
}

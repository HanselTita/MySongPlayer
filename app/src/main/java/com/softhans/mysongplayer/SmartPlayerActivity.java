package com.softhans.mysongplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class SmartPlayerActivity extends AppCompatActivity {


    private RelativeLayout parentRelativeLayout;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private String keeper = "";


    private ImageButton previousbtn, playpausebtn, nextbtn;
    private Button voicebtn;
    private TextView songNameTxt;

    private ImageView imageView;
    private RelativeLayout lower;

    private String mode = "ON";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_player);

        checkVoiceCommandPermission();

        parentRelativeLayout = findViewById(R.id.parentRelativeLayout);
        speechRecognizer = speechRecognizer.createSpeechRecognizer(SmartPlayerActivity.this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());


        previousbtn = findViewById(R.id.previous_btn);
        playpausebtn = findViewById(R.id.play_pause_btn);
        nextbtn = findViewById(R.id.next_btn);
        voicebtn = findViewById(R.id.voice_enabled_btn);
        songNameTxt = findViewById(R.id.songName);
        imageView = findViewById(R.id.logo);
        lower = findViewById(R.id.lower);


        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {

                ArrayList<String> matchesFound = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if( matchesFound != null)
                {
                    keeper = matchesFound.get(0);

                    Toast.makeText(SmartPlayerActivity.this, "Result: " + keeper, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });



        parentRelativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent)
            {

                switch (motionEvent.getAction())

                {
                    case MotionEvent.ACTION_DOWN://removing your finger from the screen
                        speechRecognizer.startListening(speechRecognizerIntent);
                        keeper = "";
                        break;

                    case MotionEvent.ACTION_UP: //long press of  finger on anywhere on the screen
                        speechRecognizer.stopListening();
                        break;
                }
                return false;
            }
        });


        voicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(mode.equals("ON"))
                {
                    mode = "OFF";
                    voicebtn.setText(R.string.voice_enabled_mode_off);
                    lower.setVisibility(View.VISIBLE);
                }
                else
                {
                    mode = "ON";
                    voicebtn.setText(R.string.voice_enabled_mode_on);
                    lower.setVisibility(View.GONE);
                }
            }
        });
    }

    private void checkVoiceCommandPermission()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(!(ContextCompat.checkSelfPermission(SmartPlayerActivity.this, Manifest.permission.RECORD_AUDIO)
            == PackageManager.PERMISSION_GRANTED))
            {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" +
                        getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }
}

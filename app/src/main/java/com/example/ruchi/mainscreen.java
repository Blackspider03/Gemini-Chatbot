package com.example.ruchi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import kotlin.Unit;

public class mainscreen extends AppCompatActivity {

     EditText editText;
     Button button;
     TextView textView,textView1;
     ProgressBar progressBar,progressBar1;
    TextToSpeech textToSpeech;
    ImageView  imageView;
    CardView cardView;

    String getinput = "indias national song lyrics";

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private SpeechRecognizer speechRecognizer;
    private Intent recognizerIntent;

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mainscreen);

        textView = findViewById(R.id.textView2);
        progressBar1 = findViewById(R.id.progressBar2);
        imageView = findViewById(R.id.imageView);
        cardView = findViewById(R.id.ruchicard);
        textView1 = findViewById(R.id.showq);
            linearLayout = findViewById(R.id.mylayout);

///////////text to speech chagne
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                if (i == TextToSpeech.SUCCESS) {
                    Set<String> a = new HashSet<>();
                    a.add("male"); // Here you can give "male" if you want to select a male voice.

                    // You can change the voice here, make sure the voice ID exists on your device
                    Voice v = new Voice("en-us-x-sfg#male_2-local", new Locale("en", "US"), 400, 200, true, a);
                    textToSpeech.setVoice(v);
                    textToSpeech.setSpeechRate(0.8f);

                    // Check if the voice was set correctly
                    int result = textToSpeech.setVoice(v);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language or Voice is not supported");
                    } else {
                        // btnSpeak.setEnabled(true);
                        // speakOut(mMessageVoice);
                    }

                } else {
                    Log.e("TTS", "Initialization Failed!");
                }
            }
        });





        int permissionCheck = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
            return;
        }
        setupSpeechRecognizer();

        //////////////permission of recorder

        if (!isConnectedToInternet()) {
            showexit();
        } else {
            String data1 = "Hi i am ruchi , i am your personal ai assitant";
            // Do something if internet is connected, if needed

            introd(data1);
        }

/////////////////to check internet connection





        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechRecognizer.startListening(recognizerIntent);
                textView.setText("");
                linearLayout.setBackgroundColor(Color.RED);

            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.stop();
            }
        });

    }

    private void introd(String data1) {
        textToSpeech.speak(data1.toString(),TextToSpeech.QUEUE_FLUSH,null);
        Toast.makeText(this, "connection check", Toast.LENGTH_SHORT).show();
    }

    private void showexit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainscreen.this);
        builder.setMessage("Your Internet not connected...");
        builder.setTitle("Trying to Connect..");
        builder.setCancelable(false);
        builder.setPositiveButton("Exit", (DialogInterface.OnClickListener) (dialog, which) -> {
            finish();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }

        return false;
    }

    private void setupSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
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
                textView1.setText("Error: " + error);
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    textView1.setText(matches.get(0));
                    linearLayout.setBackgroundColor(Color.WHITE);
                    String getinput69 = textView1.getText().toString();
                    if (getinput69.isEmpty()){

                    }else {
                        searchdata(getinput69);
                    }
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
            }
            @Override
            public void onEvent(int eventType, Bundle params) {
            }
        });

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                textView.setText("Permission Denied!");

            } else {
                setupSpeechRecognizer();
            }
        }
    }

    private void searchdata(String getinput69) {
        GeminiPro model = new GeminiPro();
        String query = getinput69.toString();
        progressBar1.setVisibility(View.VISIBLE);
        //textView.setText("");
        //editText.setText("");
        model.getResponse(query, new ResponseCallback() {
            @Override
            public void onResponse(String response) {
               // textView.setText(response);
                String redmydata = response;
                textView.setText(response);

                progressBar1.setVisibility(View.GONE);
                String response1 = textView.getText().toString();
                if (response1.isEmpty()){
                }else {
                    textToSpeech.speak(redmydata.toString(),TextToSpeech.QUEUE_FLUSH,null);
                }
            }
            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(mainscreen.this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar1.setVisibility(View.GONE);
            }
        });
    }
}
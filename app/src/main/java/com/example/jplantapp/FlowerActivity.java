package com.example.jplantapp;

import androidx.appcompat.app.AppCompatActivity;
import java.text.DecimalFormat;
import java.util.Locale;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import com.bumptech.glide.Glide;
import com.example.jplantapp.Response.FlowerResponse;

public class FlowerActivity extends BasicActivity {

    private boolean isLoggedIn;
    private TextView flowerNameTextView;
    private TextView accuracyTextView;
    private TextView floweringTextView;
    private TextView growingTextView;
    private TextView usageTextView;
    private TextView notesTextView;
    private TextView winterizingTextView;
    private ImageView photoImage;
    private FlowerResponse selectedFlower;
    private SharedPreferences userPreferences;
    private TextToSpeech textToSpeech;
    private TextView textView;

    private ImageButton backButton;

    // ------------------------------------- //
    // ------------------------------------- //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower);

        selectedFlower = getIntent().getParcelableExtra("flower");

        flowerNameTextView = findViewById(R.id.flowerName);
        accuracyTextView = findViewById(R.id.accuracy);
        floweringTextView = findViewById(R.id.flowering);
        growingTextView = findViewById(R.id.growing);
        usageTextView = findViewById(R.id.usage);
        notesTextView = findViewById(R.id.notes);
        winterizingTextView = findViewById(R.id.winterizing);
        photoImage = findViewById(R.id.flowerImage);

        flowerNameTextView.setText(selectedFlower.getName());

        double prediction = selectedFlower.getPrediction();
        DecimalFormat formatPrediction = new DecimalFormat("#.##");
        String formattedPrediction = formatPrediction.format(prediction);

        accuracyTextView.setText(formattedPrediction+"%");
        floweringTextView.setText(selectedFlower.getFlowering());
        growingTextView.setText(selectedFlower.getGrowing());
        usageTextView.setText(selectedFlower.getUsage());
        notesTextView.setText(selectedFlower.getNotes());
        winterizingTextView.setText(selectedFlower.getWinterizing());
        String imagePath = selectedFlower.getImagePath();
        Glide.with(this).load(imagePath).into(photoImage);


        userPreferences = getSharedPreferences("UserCreds", MODE_PRIVATE);
        isLoggedIn = userPreferences.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            Intent loginIntent = new Intent(FlowerActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int langResult = textToSpeech.setLanguage(Locale.getDefault());

                    if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                    }
                }
            }
        });
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
            }

            @Override
            public void onDone(String utteranceId) {
            }

            @Override
            public void onError(String utteranceId) {
            }
        });

        flowerNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakText(flowerNameTextView.getText().toString());
            }
        });

        accuracyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakText(accuracyTextView.getText().toString());
            }
        });

        floweringTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakText(floweringTextView.getText().toString());
            }
        });

        growingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakText(growingTextView.getText().toString());
            }
        });

        usageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakText(usageTextView.getText().toString());
            }
        });

        notesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakText(notesTextView.getText().toString());
            }
        });

        winterizingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakText(winterizingTextView.getText().toString());
            }
        });
    }

    private void speakText(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
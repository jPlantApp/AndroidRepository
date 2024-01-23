package com.example.jplantapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BasicActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(ThemeKeeper.getCurrentTheme(this));
        super.onCreate(savedInstanceState);
    }
}

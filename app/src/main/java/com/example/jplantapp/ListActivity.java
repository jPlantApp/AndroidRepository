package com.example.jplantapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jplantapp.Api.ApiCallback;
import com.example.jplantapp.Api.ApiManager;
import com.example.jplantapp.Response.FlowerResponse;
import com.example.jplantapp.ListAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends BasicActivity {

    private ListAdapter listAdapter;
    private RecyclerView recyclerView;
    private boolean isLoggedIn;
    private BottomNavigationView bottomNavigationView;
    private SharedPreferences userPreferences;
    private ApiManager apiManager;

    // ------------------------------------- //
    // ------------------------------------- //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listAdapter = new ListAdapter(new ArrayList<>());
        recyclerView.setAdapter(listAdapter);

        apiManager = new ApiManager();

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_list);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.bottom_list) {
                return true;
            } else if (itemId == R.id.bottom_classification) {
                startActivity(new Intent(getApplicationContext(), ClassificationActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.bottom_user) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });


        userPreferences = getSharedPreferences("UserCreds", MODE_PRIVATE);
        isLoggedIn = userPreferences.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            Intent loginIntent = new Intent(ListActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }

        getFlowerList();
    }

    public void getFlowerList() {

        String userToken = userPreferences.getString("userToken", "");
        String userEmail = userPreferences.getString("userEmail", "");

        apiManager.getFlowerList(userToken, userEmail, new ApiCallback<List<FlowerResponse>>() {
            @Override
            public void onSuccess(List<FlowerResponse> flowers) {
                listAdapter.setFlowerList(flowers);
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {
                Log.d("ListActivity", "Error: " + errorMessage);
            }
        });
    }
}

package com.example.jplantapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jplantapp.Api.ApiCallback;
import com.example.jplantapp.Api.ApiManager;
import com.example.jplantapp.Response.UserReponse;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


public class LoginActivity extends AppCompatActivity {

    private SignInClient oneTapClient;
    private BeginSignInRequest signUpRequest;
    private Button SignInButton;
    private boolean isLoggedIn;
    private boolean showOneTabUI = true;
    private SharedPreferences userPreferences;
    private ApiManager apiManager;

    // ------------------------------------- //
    // ------------------------------------- //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userPreferences = getSharedPreferences("UserCreds", MODE_PRIVATE);
        isLoggedIn = userPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            Intent intent = new Intent(LoginActivity.this, ClassificationActivity.class);
            startActivity(intent);
            finish();
        }

        apiManager = new ApiManager();
        SignInButton = findViewById(R.id.signInButton);
        oneTapClient = Identity.getSignInClient(this);
        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

        ActivityResultLauncher<IntentSenderRequest> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getResultCode() == Activity.RESULT_OK) {
                    try {
                        SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(o.getData());
                        String idToken = credential.getGoogleIdToken();

                        if (idToken != null) {
                            SharedPreferences.Editor editor = userPreferences.edit();
                            apiManager.loginUser(credential.getId(), idToken, credential.getGivenName(), new ApiCallback<UserReponse>() {
                                        @Override
                                        public void onSuccess(UserReponse user) {
                                            editor.putString("userToken", idToken);
                                            editor.putString("userEmail", credential.getId());
                                            editor.putString("userPhotoUrl", String.valueOf(credential.getProfilePictureUri()));
                                            editor.putString("userName", String.valueOf(credential.getGivenName()));
                                            editor.putBoolean("isLoggedIn", true);
                                            editor.apply();

                                            Toast.makeText(getApplicationContext(), "Zalogowano!", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(LoginActivity.this, ClassificationActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        @Override
                                        public void onError(String errorMessage) {
                                            Log.d("LoginActivity", "Error: " + errorMessage);
                                            Toast.makeText(getApplicationContext(), "Nie udalo się zalogować...", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    } catch (ApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneTapClient.beginSignIn(signUpRequest)
                        .addOnSuccessListener(LoginActivity.this, new OnSuccessListener<BeginSignInResult>() {
                            @Override
                            public void onSuccess(BeginSignInResult result) {
                                IntentSenderRequest intentSenderRequest =
                                        new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build();
                                activityResultLauncher.launch(intentSenderRequest);
                            }
                        })
                        .addOnFailureListener(LoginActivity.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Nie udalo się zalogować...", Toast.LENGTH_LONG).show();
                                Log.d("LoginActivity", "Error: " + e);
                            }
                        });
            }
        });

    }
}
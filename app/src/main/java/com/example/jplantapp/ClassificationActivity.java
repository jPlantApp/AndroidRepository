package com.example.jplantapp;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jplantapp.Api.ApiManager;
import com.example.jplantapp.Api.ApiCallback;
import com.example.jplantapp.Response.FlowerResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ClassificationActivity extends BasicActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private boolean isLoggedIn;

    // Button objects
    private Button btnSendRequest;
    private Button btnChoosePhoto;
    private Button btnTakePhoto;

    // Image objects
    private ImageView imageView;
    private Boolean isPhotoTaken = false;
    private String selectedImagePath;

    // Api object
    private ApiManager apiManager;

    // Nav object
    private BottomNavigationView bottomNavigation;

    // Shared objects
    private SharedPreferences photoPreferences;
    private SharedPreferences userPreferences;

    private ProgressBar progressBar;
    // ------------------------------------- //
    // ------------------------------------- //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification);
        apiManager = new ApiManager();

        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);

        btnTakePhoto = findViewById(R.id.takePhotoButton);
        btnSendRequest = findViewById(R.id.sendRequestButton);
        btnChoosePhoto = findViewById(R.id.openGalleryButton);

        btnTakePhoto.setOnClickListener(v -> openCamera());
        btnChoosePhoto.setOnClickListener(v -> openGallery());
        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPhotoTaken) {
                    sendRequest();
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "Musisz wybrać zdjęcie", Toast.LENGTH_LONG).show();
                }
            }
        });
        ;

        photoPreferences = getSharedPreferences("Photos", MODE_PRIVATE);
        userPreferences = getSharedPreferences("UserCreds", MODE_PRIVATE);


        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.bottom_classification);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_classification) {
                return true;
            } else if (itemId == R.id.bottom_user) {
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            } else if (itemId == R.id.bottom_list) {
                    startActivity(new Intent(getApplicationContext(), ListActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                    return true;
            }
            return false;
        });

        isLoggedIn = userPreferences.getBoolean("isLoggedIn", false);
        if (!isLoggedIn) {
            Intent loginIntent = new Intent(ClassificationActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }


    // Funkcja zajmująca się obsługą danych po wykonaniu czynnosci na zewnętrznych aplikacjach
    // W tym przypadku pozwala nam pobrać dane z aparatu/galerii
    // Jednoczesnie obrabiając i przygotowywując zdjęcie
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageBitmap = cropToSquare(imageBitmap);
                imageView.setImageBitmap(imageBitmap);
                isPhotoTaken = true;
                saveImage(imageBitmap);
            }
        }  else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getRealPathFromURI(selectedImageUri);
                Bitmap imageBitmap = BitmapFactory.decodeFile(selectedImagePath);
                imageBitmap = rotateImage(imageBitmap, selectedImagePath);
                imageBitmap = cropToSquare(imageBitmap);
                imageView.setImageBitmap(imageBitmap);
                isPhotoTaken = true;
                saveImage(imageBitmap);
            }
        }
    }



    // Funkcja wysyłająca request do backendu odpowiedzianego za klasyfikaje
    private void sendRequest() {
        Bitmap imageBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        String base64Image = bitmapToBase64(imageBitmap);

        String lastImagePath = photoPreferences.getString("lastImagePath", "");
        String userToken = userPreferences.getString("userToken", "");
        String userEmail = userPreferences.getString("userEmail", "");

        apiManager.sendPhoto(base64Image, lastImagePath, userToken, userEmail, new ApiCallback<FlowerResponse>() {
            @Override
            public void onSuccess(FlowerResponse flower) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(ClassificationActivity.this, FlowerActivity.class);
                        intent.putExtra("flower", flower);
                        startActivity(intent);
                    }
                });
            }
            @Override
            public void onError(String errorMessage) {
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }



    // Funkcja otwierające zewnętrzne narzędzia takie jak aparat i galeria
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY);
    }
    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }



    // Funkcja wykorzystywane do modyfikacji zdjęć
    private Bitmap cropToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int newDimension = Math.min(width, height);

        int leftOffset = (width - newDimension) / 2;
        int topOffset = (height - newDimension) / 4;

        return Bitmap.createBitmap(bitmap, leftOffset, topOffset, newDimension, newDimension);
    }
    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null)
            return contentUri.getPath();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }
    private void saveImage(Bitmap imageBitmap) {
        String folderPath = getExternalFilesDir(null) + File.separator + "YourAppFolder";
        File folder = new File(folderPath);
        folder.mkdirs();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";

        File imageFile = new File(folder, imageFileName);

        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SharedPreferences.Editor editor = photoPreferences.edit();
        editor.putString("lastImagePath", imageFile.getAbsolutePath());
        editor.apply();
    }
    private Bitmap rotateImage(Bitmap source, String imagePath) {
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int orientation = ExifInterface.ORIENTATION_NORMAL;
        if (exifInterface != null) {
            orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        }

        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) source.getWidth() / 2, (float) source.getHeight() / 2);

        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}

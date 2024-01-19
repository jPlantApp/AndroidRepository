package com.example.jplantapp.Requests;

import com.google.gson.annotations.SerializedName;

public class PhotoClassificationRequest {

    @SerializedName("imageData")
    private String imageData;

    @SerializedName("photoPath")
    private String imagePath;

    @SerializedName("userToken")
    private String userToken;

    @SerializedName("userEmail")
    private String userEmail;

    public PhotoClassificationRequest(String imageData, String imagePath, String userToken, String userEmail) {
        this.imageData = imageData;
        this.imagePath = imagePath;
        this.userToken = userToken;
        this.userEmail = userEmail;
    }

    public String getImageData() {
        return imageData;
    }
    public String getImagePath() {
        return imagePath;
    }
    public String getUserToken() {
        return userToken;
    }
    public String getUserEmail() { return userEmail; }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public void setImageData(String imageData) {
        this.imageData = imageData;
    }
    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
}

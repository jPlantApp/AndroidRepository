package com.example.jplantapp.Requests;

import com.google.gson.annotations.SerializedName;

public class UserLoginRequest {
    @SerializedName("userEmail")
    private String userEmail;
    @SerializedName("userName")
    private String userName;
    @SerializedName("userToken")
    private String userToken;

    public UserLoginRequest(String email, String givenName, String tokenId) {
        this.userEmail = email;
        this.userName = givenName;
        this.userToken = tokenId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}

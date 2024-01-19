package com.example.jplantapp.Response;

public class UserReponse {
    private String email;
    private String idToken;
    private String givenName;
    private String photoUrl;

    public UserReponse(String email, String name, String idToken, String photoUrl) {
        this.email = email;
        this.givenName = name;
        this.idToken = idToken;
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return email;
    }
    public String getIdToken() {
        return idToken;
    }
    public String getPhotoUrl() {
        return photoUrl;
    }
    public String getGivenName() {
        return givenName;
    }
}

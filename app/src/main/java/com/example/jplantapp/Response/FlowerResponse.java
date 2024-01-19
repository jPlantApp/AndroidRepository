package com.example.jplantapp.Response;

import android.os.Parcel;
import android.os.Parcelable;

public class FlowerResponse implements Parcelable {
    private String flowerName;
    private double prediction;
    private String imagePath;
    private String description;
    private String growing;
    private String usage;
    private String flowering;
    private String winterizing;
    private String notes;

    public FlowerResponse(String flowerName, double prediction, String imagePath, String description, String growing, String usage, String flowering, String winterizing, String notes) {
        this.flowerName = flowerName;
        this.prediction = prediction;
        this.imagePath = imagePath;
        this.description = description;
        this.growing = growing;
        this.usage = usage;
        this.flowering = flowering;
        this.winterizing = winterizing;
        this.notes = notes;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGrowing() {
        return growing;
    }

    public void setGrowing(String growing) {
        this.growing = growing;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getFlowering() {
        return flowering;
    }

    public void setFlowering(String flowering) {
        this.flowering = flowering;
    }

    public String getWinterizing() {
        return winterizing;
    }

    public void setWinterizing(String winterizing) {
        this.winterizing = winterizing;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getName() {
        return flowerName;
    }
    public double getPrediction() {
        return prediction;
    }

    //-----------------------//
    protected FlowerResponse(Parcel in) {
        flowerName = in.readString();
        prediction = in.readFloat();
        imagePath = in.readString();
        flowering = in.readString();
        growing = in.readString();
        usage = in.readString();
        notes = in.readString();
        winterizing = in.readString();
    }

    public static final Creator<FlowerResponse> CREATOR = new Creator<FlowerResponse>() {
        @Override
        public FlowerResponse createFromParcel(Parcel in) {
            return new FlowerResponse(in);
        }

        @Override
        public FlowerResponse[] newArray(int size) {
            return new FlowerResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(flowerName);
        dest.writeFloat((float) prediction);
        dest.writeString(imagePath);
        dest.writeString(flowering);
        dest.writeString(growing);
        dest.writeString(usage);
        dest.writeString(notes);
        dest.writeString(winterizing);
    }
}

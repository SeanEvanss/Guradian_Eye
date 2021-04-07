package com.example.eagleeye.Model;

public class ADLTask {
    String title,location,description,fee;
    ADLTask(){

    }
    public ADLTask(String title, String location, String description, String fee) {
        this.title = title;
        this.location = location;
        this.description = description;
        this.fee = fee;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }
}

package com.example.eagleeye.Model;

public class Ride {
    String location,destination,distance,price;
    public Ride(){

    }

    public Ride(String location, String destination, String distance, String price) {
        this.location = location;
        this.destination = destination;
        this.distance = distance;
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

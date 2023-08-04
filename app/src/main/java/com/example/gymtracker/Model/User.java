package com.example.gymtracker.Model;

import java.util.ArrayList;

public class User {

    public static User currentUser;
    private String benutzerName;
    private String passwort;
    private int id;
    private ArrayList<Training> trainingsOfUser = new ArrayList<>();

    public User(String benutzerName, String passwort){
        this.benutzerName = benutzerName;
        this.passwort = passwort;

    }

    public User(String benutzerName, String passwort, int id){
        this.benutzerName = benutzerName;
        this.passwort = passwort;
        this.id = id;
    }

    public String getBenutzerName() {
        return benutzerName;
    }

    public ArrayList<Training> getTrainingsOfUser() {
        return trainingsOfUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addTraining(Training training){
        trainingsOfUser.add(training);
    }
}

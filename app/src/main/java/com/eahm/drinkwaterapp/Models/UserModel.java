package com.eahm.drinkwaterapp.Models;

import java.io.Serializable;
import java.util.Map;

public class UserModel implements Serializable {
    public  String name;
    public  String secondName;
    public  String lastName;
    public  String secondLastName;
    public  String phone;
    public  String type;
    public  String profileImageUrl;
    public  String currentDevice;
    public  Map<String,String> lastConnection;

    public UserModel() {
    }


    public UserModel(String name, String secondName, String lastName, String secondLastName, String phone, String type, String profileImageUrl, String currentDevice, Map<String, String> lastConnection) {
        this.name = name;
        this.secondName = secondName;
        this.lastName = lastName;
        this.secondLastName = secondLastName;
        this.phone = phone;
        this.type = type;
        this.profileImageUrl = profileImageUrl;
        this.currentDevice = currentDevice;
        this.lastConnection = lastConnection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getCurrentDevice() {
        return currentDevice;
    }

    public void setCurrentDevice(String currentDevice) {
        this.currentDevice = currentDevice;
    }

    public Map<String, String> getLastConnection() {
        return lastConnection;
    }

    public void setLastConnection(Map<String, String> lastConnection) {
        this.lastConnection = lastConnection;
    }
}

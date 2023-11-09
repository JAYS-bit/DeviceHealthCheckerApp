package com.example.mobotestapp.firebasepdfactivity;

public class UserDetailsModel {

    String microphone_test_results;
    String camera_test_results;

    String sensor_test_results;

    String root_test_results;
    String bluetooth_test_results;


    public UserDetailsModel(String microphone_test_results, String camera_test_results, String sensor_test_results, String root_test_results, String bluetooth_test_results) {
        this.microphone_test_results = microphone_test_results;
        this.camera_test_results = camera_test_results;
        this.sensor_test_results = sensor_test_results;
        this.root_test_results = root_test_results;
        this.bluetooth_test_results = bluetooth_test_results;
    }


    public String getMicrophone_test_results() {
        return microphone_test_results;
    }

    public void setMicrophone_test_results(String microphone_test_results) {
        this.microphone_test_results = microphone_test_results;
    }

    public String getCamera_test_results() {
        return camera_test_results;
    }

    public void setCamera_test_results(String camera_test_results) {
        this.camera_test_results = camera_test_results;
    }

    public String getSensor_test_results() {
        return sensor_test_results;
    }

    public void setSensor_test_results(String sensor_test_results) {
        this.sensor_test_results = sensor_test_results;
    }

    public String getRoot_test_results() {
        return root_test_results;
    }

    public void setRoot_test_results(String root_test_results) {
        this.root_test_results = root_test_results;
    }

    public String getBluetooth_test_results() {
        return bluetooth_test_results;
    }

    public void setBluetooth_test_results(String bluetooth_test_results) {
        this.bluetooth_test_results = bluetooth_test_results;
    }
}

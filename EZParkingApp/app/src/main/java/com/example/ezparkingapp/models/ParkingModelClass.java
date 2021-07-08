package com.example.ezparkingapp.models;

public class ParkingModelClass {
    String parking_id, date, time, level_id, level_name, slot_id, slot_name, status, user_id;

    public ParkingModelClass() {
    }

    public ParkingModelClass(String parking_id, String date, String time, String level_id, String level_name, String slot_id, String slot_name, String status, String user_id) {
        this.parking_id = parking_id;
        this.date = date;
        this.time = time;
        this.level_id = level_id;
        this.level_name = level_name;
        this.slot_id = slot_id;
        this.slot_name = slot_name;
        this.status = status;
        this.user_id = user_id;
    }

    public String getParking_id() {
        return parking_id;
    }

    public void setParking_id(String parking_id) {
        this.parking_id = parking_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLevel_id() {
        return level_id;
    }

    public void setLevel_id(String level_id) {
        this.level_id = level_id;
    }

    public String getLevel_name() {
        return level_name;
    }

    public void setLevel_name(String level_name) {
        this.level_name = level_name;
    }

    public String getSlot_id() {
        return slot_id;
    }

    public void setSlot_id(String slot_id) {
        this.slot_id = slot_id;
    }

    public String getSlot_name() {
        return slot_name;
    }

    public void setSlot_name(String slot_name) {
        this.slot_name = slot_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}

package com.example.ezparkingapp.models;

public class LevelsModelClass {

    String level_id, level_name;

    public LevelsModelClass() {
    }

    public LevelsModelClass(String level_id, String level_name) {
        this.level_id = level_id;
        this.level_name = level_name;
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
}

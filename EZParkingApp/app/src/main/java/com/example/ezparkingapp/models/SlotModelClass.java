package com.example.ezparkingapp.models;

public class SlotModelClass {

    boolean is_available;
    String slot_id, slot_name;

    public SlotModelClass() {
    }

    public SlotModelClass(boolean is_available, String slot_id, String slot_name) {
        this.is_available = is_available;
        this.slot_id = slot_id;
        this.slot_name = slot_name;
    }

    public boolean isIs_available() {
        return is_available;
    }

    public void setIs_available(boolean is_available) {
        this.is_available = is_available;
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
}

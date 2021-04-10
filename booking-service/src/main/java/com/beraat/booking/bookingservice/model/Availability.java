package com.beraat.booking.bookingservice.model;



import java.time.LocalDateTime;
import java.util.List;


public class Availability {
    Object cleaner;
    List<TimeSlot> slots;

    public Availability() {
    }

    public Availability(Object cleaner, List<TimeSlot> slots) {
        this.cleaner = cleaner;
        this.slots = slots;
    }

    public Object getCleaner() {
        return cleaner;
    }

    public void setCleaner(Object cleaner) {
        this.cleaner = cleaner;
    }

    public List<TimeSlot> getSlots() {
        return slots;
    }

    public void setSlots(List<TimeSlot> slots) {
        this.slots = slots;
    }
}

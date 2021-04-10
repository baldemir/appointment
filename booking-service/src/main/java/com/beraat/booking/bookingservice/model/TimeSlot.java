package com.beraat.booking.bookingservice.model;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimeSlot implements Comparable<TimeSlot> {

    private LocalTime start;
    private LocalTime end;

    public TimeSlot() {
    }

    public TimeSlot(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    @Override
    public int compareTo(TimeSlot o) {
        return this.getStart().compareTo(o.getStart());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TimeSlot slot = (TimeSlot) obj;
        return this.start.equals(slot.getStart()) && this.end.equals(slot.getEnd());
    }

    public boolean contains(TimeSlot o) {
        if ((this.start.equals(o.getStart()) || this.start.isBefore(o.getStart())) && (this.end.equals(o.getEnd()) || this.end.isAfter(o.getEnd()))) {
            return true;
        }
        return false;
    }

    public boolean collapse(TimeSlot o) {
        if((this.start.isBefore(o.getEnd())) && (o.getStart().isBefore(this.getEnd()))){
            return true;
        }
        return false;
    }


    @Override
    public String toString() {
        return "start=" + start + ", end=" + end;
    }

}

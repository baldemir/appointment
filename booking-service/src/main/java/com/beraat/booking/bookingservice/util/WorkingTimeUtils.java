package com.beraat.booking.bookingservice.util;

import com.beraat.booking.bookingservice.model.TimeSlot;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.time.temporal.ChronoUnit.MINUTES;

public class WorkingTimeUtils {

    static List<Integer> workingDays = List.of(1, 2, 3, 4, 6, 7);
    static List<Long> allowedWorkingDurationsInMinutes = List.of(120L, 240L);

    public static List<TimeSlot> getTimeSlotsForOneDay(Integer dayOfWeek) {
        ArrayList<TimeSlot> dayTimeSlots = new ArrayList<>();

        if (workingDays.contains(dayOfWeek)) {
            dayTimeSlots.add(getWorkingHours());
        }
        return dayTimeSlots;
    }

    public static TimeSlot getWorkingHours() {
        return new TimeSlot(LocalTime.of(8, 0), LocalTime.of(22, 0));
    }

    public static boolean isValidWorkingTime(TimeSlot slot) {
        long duration = slot.getStart().until(slot.getEnd(), MINUTES);
        if (allowedWorkingDurationsInMinutes.contains(duration)) {
            return true;
        } else {
            return false;
        }
    }

}

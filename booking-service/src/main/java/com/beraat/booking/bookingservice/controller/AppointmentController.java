package com.beraat.booking.bookingservice.controller;

import com.beraat.booking.bookingservice.entity.Appointment;
import com.beraat.booking.bookingservice.model.Availability;
import com.beraat.booking.bookingservice.service.AppointmentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/appointment")
@Api(value = "Product API documentation")
public class AppointmentController {
    private AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    List<Appointment> getAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping(path = "cleaners/{year}/{month}/{day}")
    List<Availability> getAvailableHoursForCleaners(@PathVariable Integer year, @PathVariable Integer month, @PathVariable Integer day) {
        return appointmentService.getAvailableHoursForCleanersForDay(LocalDate.of(year, month, day));
    }

    @PutMapping
    Appointment updateAppointment(@RequestBody Appointment appointment) {
        return appointmentService.updateAppointment(appointment);
    }

    @PostMapping
    Appointment createAppointment(@RequestBody Appointment appointment) {
        return appointmentService.createAppointment(appointment);
    }

}

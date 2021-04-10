package com.beraat.booking.bookingservice.controller;

import com.beraat.booking.bookingservice.entity.Appointment;
import com.beraat.booking.bookingservice.entity.User;
import com.beraat.booking.bookingservice.service.AppointmentService;
import com.beraat.booking.bookingservice.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    private AppointmentService appointmentService;

    public UserController(UserService userService, AppointmentService appointmentService) {
        this.userService = userService;
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public User createUser(@RequestBody User user){
        return userService.createUser(user);
    }

    @GetMapping
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @GetMapping(path = "/{userId}/appointments")
    public List<Appointment> getUserAppointments(@PathVariable Long userId){
        return appointmentService.getUserAppointments(userId);
    }

}

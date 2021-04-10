package com.beraat.booking.bookingservice.service;

import com.beraat.booking.bookingservice.entity.Appointment;
import com.beraat.booking.bookingservice.entity.User;
import com.beraat.booking.bookingservice.repository.AppointmentRepository;
import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AppointmentService appointmentService;


    private  Appointment appointment;
    private  Optional<Appointment> optionalAppointment;
    private  List<Appointment> appointments;
    private  String appointmentId;

    private  User user;
    private  Long userId;
    private  LocalDate localDate;
    private  LocalDateTime localDateTimeDayStart;
    private  LocalDateTime localDateTimeDayEnd;
    private  LocalDateTime localDateTimeAppStart;
    private  LocalDateTime localDateTimeAppEnd;

    private  List<Object> cleanerList;
    private  Long cleanerId;
    private  Long companyId;

    private  LinkedHashMap<String, Object> cleaner;

    private  LinkedHashMap<String, Object> company;

    @BeforeEach
    public void setUp() {
        cleanerId = 1L;
        companyId = 1L;
        company = new LinkedHashMap<>();
        company.put("id", companyId);
        company.put("name", "Test Company");

        cleaner = new LinkedHashMap<>();
        cleaner.put("id", cleanerId);
        cleaner.put("fullName", "Test User");
        cleaner.put("company", company);

        cleanerList = new ArrayList<>();
        cleanerList.add(cleaner);


        user = new User();
        userId=1L;
        user.setId(userId);

        localDate = LocalDate.of(2020, 1,1);
        localDateTimeDayStart = LocalDateTime.of(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), 0, 0);
        localDateTimeDayEnd = LocalDateTime.of(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), 23, 59);

        localDateTimeAppStart = LocalDateTime.of(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), 10, 0);
        localDateTimeAppEnd = LocalDateTime.of(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), 12, 0);

        appointment = new Appointment();
        appointmentId = "2e84abca-36d7-4770-9d86-5fa231000211";
        appointment.setId(appointmentId);
        appointment.setCleanerId(cleanerId);
        appointment.setUser(user);
        appointment.setStartTime(localDateTimeAppStart);
        appointment.setEndTime(localDateTimeAppStart.plusHours(2));

        optionalAppointment = Optional.of(appointment);
        appointments = new ArrayList<>();
        appointments.add(appointment);



    }


    @Test
    void shouldGetAllAppointments() {
        when(appointmentRepository.findAll()).thenReturn(appointments);
        assertEquals(appointments, appointmentService.getAllAppointments());
    }

    @Test
    void shouldGetAppointmentsForUserForDay() {
        when(appointmentRepository.findAllByUserIdAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(userId, localDateTimeDayStart, localDateTimeDayEnd)).thenReturn(appointments);
        assertEquals(appointments, appointmentService.getAppointmentsForUserForDay(userId, localDate));
    }

    @Test
    void shouldGetAppointmentsForCleanerForDay() {
        when(appointmentRepository.findAllByCleanerIdAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(userId, localDateTimeDayStart, localDateTimeDayEnd)).thenReturn(appointments);
        assertEquals(appointments, appointmentService.getAppointmentsForCleanerForDay(userId, localDate));
    }

    @Test
    void shouldCreateAppointmentWhenAppointmentIsValid() {
        when(restTemplate.getForObject("http://company-service/cleaners/1", Object.class)).thenReturn(cleaner);
        ArgumentCaptor<Appointment> argumentCaptor = ArgumentCaptor.forClass(Appointment.class);

        appointmentService.createAppointment(appointment);

        verify(appointmentRepository, times(1)).save(argumentCaptor.capture());
    }

    @Test
    void shouldNotCreateAppointmentWhenAppointmentTimeIsOneHour() {
        when(restTemplate.getForObject("http://company-service/cleaners/1", Object.class)).thenReturn(cleaner);
        ArgumentCaptor<Appointment> argumentCaptor = ArgumentCaptor.forClass(Appointment.class);
        appointment.setEndTime(appointment.getStartTime().plusHours(1));
        assertThrows(IllegalArgumentException.class,()-> appointmentService.createAppointment(appointment));
    }

    @Test
    void shouldNotCreateAppointmentWhenAppointmentTimeIsOnFriday() {
        when(restTemplate.getForObject("http://company-service/cleaners/1", Object.class)).thenReturn(cleaner);
        ArgumentCaptor<Appointment> argumentCaptor = ArgumentCaptor.forClass(Appointment.class);
        appointment.setStartTime(LocalDateTime.of(2020, 1,3,10,0));
        appointment.setEndTime(appointment.getStartTime().plusHours(2));
        assertThrows(IllegalArgumentException.class,()-> appointmentService.createAppointment(appointment));
    }

    @Test
    void shouldGetUserAppointments() {
        when(appointmentRepository.findAllByUserId(userId)).thenReturn(appointments);
        assertEquals(appointments, appointmentService.getUserAppointments(userId));
    }
}
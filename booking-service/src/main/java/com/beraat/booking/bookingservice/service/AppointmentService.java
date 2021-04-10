package com.beraat.booking.bookingservice.service;

import com.beraat.booking.bookingservice.config.Constants;
import com.beraat.booking.bookingservice.entity.Appointment;
import com.beraat.booking.bookingservice.model.Availability;
import com.beraat.booking.bookingservice.model.TimeSlot;
import com.beraat.booking.bookingservice.repository.AppointmentRepository;
import com.beraat.booking.bookingservice.util.WorkingTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.time.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class AppointmentService {


    private RestTemplate restTemplate;

    AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, RestTemplate restTemplate) {
        this.appointmentRepository = appointmentRepository;
        this.restTemplate = restTemplate;
    }

    public List<Appointment> getAllAppointments() {
        return this.appointmentRepository.findAll();
    }

    public List<Appointment> getAppointmentsForUserForDay(Long userId, LocalDate date) {
        LocalDateTime dtStart = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), 0, 0);
        LocalDateTime dtEnd = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), 23, 59);
        return this.appointmentRepository.findAllByUserIdAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(userId, dtStart, dtEnd);
    }

    public List<Appointment> getAppointmentsForCleanerForDay(Long cleanerId, LocalDate dt) {
        LocalDateTime d1 = LocalDateTime.of(dt.getYear(), dt.getMonthValue(), dt.getDayOfMonth(), 0, 0);
        LocalDateTime d2 = LocalDateTime.of(dt.getYear(), dt.getMonthValue(), dt.getDayOfMonth(), 23, 59);
        return this.appointmentRepository.findAllByCleanerIdAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(cleanerId, d1, d2);
    }

    private List<TimeSlot> getAvailableHoursForCleanerForDay(Long cleanerId, LocalDate date) {
        List<Appointment> existingAppointments = getAppointmentsForCleanerForDay(cleanerId, date);

        List<TimeSlot> availableSlots = WorkingTimeUtils.getTimeSlotsForOneDay(date.getDayOfWeek().getValue());

        availableSlots = excludeBookedTimeSlots(availableSlots, existingAppointments);

        return availableSlots;
    }

    public List<Availability> getAvailableHoursForCleanersForDay(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("You cannot view available cleaners for past dates.");
        }
        List<Object> cleaners = restTemplate.getForObject(Constants.CLEANER_SERVICE_URL, List.class);
        List<Availability> availabilities = new ArrayList<>();
        for (Object obj : cleaners) {
            LinkedHashMap map = (LinkedHashMap) obj;
            availabilities.add(new Availability(obj, getAvailableHoursForCleanerForDay(Long.valueOf(map.get("id").toString()), date)));
        }
        return availabilities;
    }

    @Transactional
    public Appointment createAppointment(Appointment appointment) {
        //check if a cleaner exist with the given id
        if (isValidAppointment(appointment)) {
            return this.appointmentRepository.save(appointment);
        } else {
            throw new IllegalArgumentException("Appointment information is not valid");
        }
    }

    @Transactional
    public Appointment updateAppointment(Appointment appointment) {
        //check if a cleaner exist with the given id
        Appointment existingAppointment = appointmentRepository.findById(appointment.getId()).orElseThrow(() -> new IllegalArgumentException());
        existingAppointment.setEndTime(existingAppointment.getStartTime());
        appointmentRepository.save(existingAppointment);
        if (isValidAppointment(appointment)) {
            return this.appointmentRepository.save(appointment);
        } else {
            throw new IllegalArgumentException("Appointment is not valid");
        }
    }

    public List<Appointment> getUserAppointments(Long userId) {
        return this.appointmentRepository.findAllByUserId(userId);
    }

    private boolean isValidAppointment(Appointment appointment) {

        LinkedHashMap cleanerMap;
        try {
            //to avoid data inconsistency
            cleanerMap = (LinkedHashMap) restTemplate.getForObject(Constants.CLEANER_SERVICE_URL + appointment.getCleanerId(), Object.class);
        }catch (Exception e){
            throw new IllegalArgumentException("Could not find the specified cleaner id");
        }
        Long companyId = getCompanyIdFromCleaner(cleanerMap);
        return isCleanerAvailableForAppointment(appointment) && !checkIfCleanerFromOtherCompany(appointment, companyId);
    }

    private Long getCompanyIdFromCleaner(LinkedHashMap cleanerMap) {
        LinkedHashMap companyMap = (LinkedHashMap) cleanerMap.get("company");
        Long companyId = Long.valueOf(companyMap.get("id").toString());
        return companyId;
    }

    private boolean checkIfCleanerFromOtherCompany(Appointment appointment, Long companyId) {
        List<Appointment> userApps = getAppointmentsForUserForDay(appointment.getUser().getId(), appointment.getStartTime().toLocalDate());
        TimeSlot tsCheck = new TimeSlot(appointment.getStartTime().toLocalTime(), appointment.getEndTime().toLocalTime());
        for (Appointment app : userApps) {
            TimeSlot ts = new TimeSlot(app.getStartTime().toLocalTime(), app.getEndTime().toLocalTime());
            if (ts.collapse(tsCheck)) {
                LinkedHashMap cleanerMap = (LinkedHashMap) restTemplate.getForObject(Constants.CLEANER_SERVICE_URL + app.getCleanerId(), Object.class);
                Long companyId2 = getCompanyIdFromCleaner(cleanerMap);
                if (!companyId.equals(companyId2)) {
                    return true;
                }

            }
        }
        return false;
    }

    private boolean isCleanerAvailableForAppointment(Appointment appointmentToCheck) {
        //should start and finish on same day
        if (appointmentToCheck.getStartTime().getDayOfYear() == appointmentToCheck.getEndTime().getDayOfYear() && appointmentToCheck.getStartTime().getYear() == appointmentToCheck.getEndTime().getYear()) {
            TimeSlot slotToCheck = new TimeSlot(appointmentToCheck.getStartTime().toLocalTime(), appointmentToCheck.getEndTime().toLocalTime());
            if (WorkingTimeUtils.isValidWorkingTime(slotToCheck)) {
                List<TimeSlot> availableHours = getAvailableHoursForCleanerForDay(appointmentToCheck.getCleanerId(), appointmentToCheck.getStartTime().toLocalDate());
                for (TimeSlot availableSlot : availableHours) {
                    if (availableSlot.contains(slotToCheck)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private List<TimeSlot> excludeBookedTimeSlots(List<TimeSlot> total, List<Appointment> appointments) {

        List<TimeSlot> toAdd = new ArrayList();
        Collections.sort(appointments);
        for (Appointment appointment : appointments) {
            for (TimeSlot slot : total) {
                if (appointment.getEndTime().isEqual(appointment.getStartTime())) {
                    continue;
                }
                if ((appointment.getStartTime().toLocalTime().isBefore(slot.getStart()) || appointment.getStartTime().toLocalTime().equals(slot.getStart())) && appointment.getEndTime().toLocalTime().isAfter(slot.getStart()) && appointment.getEndTime().toLocalTime().isBefore(slot.getEnd())) {
                    slot.setStart(appointment.getEndTime().toLocalTime());
                }
                if (appointment.getStartTime().toLocalTime().isAfter(slot.getStart()) && appointment.getStartTime().toLocalTime().isBefore(slot.getEnd()) && appointment.getEndTime().toLocalTime().isAfter(slot.getEnd()) || appointment.getEndTime().toLocalTime().equals(slot.getEnd())) {
                    slot.setEnd(appointment.getStartTime().toLocalTime());
                }
                if (appointment.getStartTime().toLocalTime().isAfter(slot.getStart()) && appointment.getEndTime().toLocalTime().isBefore(slot.getEnd())) {
                    toAdd.add(new TimeSlot(slot.getStart(), appointment.getStartTime().toLocalTime()));
                    slot.setStart(appointment.getEndTime().toLocalTime());
                }
            }
        }
        total.addAll(toAdd);
        Collections.sort(total);
        return total;
    }


}

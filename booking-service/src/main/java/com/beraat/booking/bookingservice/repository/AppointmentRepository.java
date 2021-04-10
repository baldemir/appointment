package com.beraat.booking.bookingservice.repository;

import com.beraat.booking.bookingservice.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    List<Appointment> findAllByCleanerIdAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(Long cleanerId, LocalDateTime dtStart, LocalDateTime dtEnd);
    List<Appointment> findAllByUserId(Long userId);
    List<Appointment> findAllByUserIdAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(Long userId, LocalDateTime dtStart, LocalDateTime dtEnd);

}

package com.urbannest.backend.repository;

import com.urbannest.backend.entity.Booking;
import com.urbannest.backend.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    boolean existsByPropertyIdAndStatusAndCheckInLessThanAndCheckOutGreaterThan(
            UUID propertyId,
            BookingStatus status,
            LocalDate checkOut,
            LocalDate checkIn
    );

    List<Booking> findByClientIdOrderByCreatedAtDesc(UUID clientId);

    List<Booking> findByOwnerIdOrderByCreatedAtDesc(UUID ownerId);
}

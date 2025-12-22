package com.urbannest.backend.service;

import com.urbannest.backend.dto.BookingResponse;
import com.urbannest.backend.dto.CreateBookingRequest;
import com.urbannest.backend.entity.Booking;
import com.urbannest.backend.entity.Property;
import com.urbannest.backend.entity.User;
import com.urbannest.backend.enums.BookingStatus;
import com.urbannest.backend.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PropertyService propertyService;

    public BookingResponse createInstantBooking(User client, CreateBookingRequest request) {
        if (request == null || request.getPropertyId() == null) {
            throw new IllegalArgumentException("propertyId is required");
        }
        if (request.getCheckIn() == null || request.getCheckOut() == null) {
            throw new IllegalArgumentException("checkIn and checkOut are required");
        }

        LocalDate checkIn = request.getCheckIn();
        LocalDate checkOut = request.getCheckOut();

        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("checkOut must be after checkIn");
        }
        if (checkIn.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("checkIn cannot be in the past");
        }

        Property property = propertyService.getPropertyById(request.getPropertyId());
        if (property == null) {
            throw new IllegalArgumentException("Property not found");
        }
        if (!property.isAvailable()) {
            throw new IllegalArgumentException("Property is not available");
        }

        boolean overlaps = bookingRepository.existsByPropertyIdAndStatusAndCheckInLessThanAndCheckOutGreaterThan(
                property.getId(),
                BookingStatus.CONFIRMED,
                checkOut,
                checkIn
        );
        if (overlaps) {
            throw new IllegalStateException("Selected dates are not available");
        }

        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        BigDecimal total = null;
        if (property.getPrice() != null) {
            total = property.getPrice().multiply(BigDecimal.valueOf(Math.max(1, nights)));
        }

        Booking booking = Booking.builder()
                .propertyId(property.getId())
                .ownerId(property.getOwnerId())
                .clientId(client.getId())
                .checkIn(checkIn)
                .checkOut(checkOut)
                .status(BookingStatus.CONFIRMED)
                .totalAmount(total)
                .createdAt(LocalDateTime.now())
                .build();

        if (booking == null) {
            throw new IllegalStateException("Failed to create booking");
        }
        @SuppressWarnings("null")
        Booking saved = bookingRepository.save(booking);
        return toResponse(saved);
    }

    public List<BookingResponse> getBookingsForClient(UUID clientId) {
        return bookingRepository.findByClientIdOrderByCreatedAtDesc(clientId).stream().map(this::toResponse).toList();
    }

    public List<BookingResponse> getBookingsForOwner(UUID ownerId) {
        return bookingRepository.findByOwnerIdOrderByCreatedAtDesc(ownerId).stream().map(this::toResponse).toList();
    }

    private BookingResponse toResponse(Booking b) {
        return BookingResponse.builder()
                .id(b.getId())
                .propertyId(b.getPropertyId())
                .ownerId(b.getOwnerId())
                .clientId(b.getClientId())
                .checkIn(b.getCheckIn())
                .checkOut(b.getCheckOut())
                .status(b.getStatus())
                .totalAmount(b.getTotalAmount())
                .createdAt(b.getCreatedAt())
                .build();
    }
}

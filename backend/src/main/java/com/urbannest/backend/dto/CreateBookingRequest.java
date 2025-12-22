package com.urbannest.backend.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreateBookingRequest {
    private UUID propertyId;
    private LocalDate checkIn;
    private LocalDate checkOut;
}

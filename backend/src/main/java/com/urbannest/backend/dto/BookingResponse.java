package com.urbannest.backend.dto;

import com.urbannest.backend.enums.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class BookingResponse {
    private UUID id;
    private UUID propertyId;
    private UUID ownerId;
    private UUID clientId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private BookingStatus status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
}

package com.urbannest.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "properties")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId; // References the Supabase User ID

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String category; // e.g., LUXURY, AIRBNB, HOSTEL

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String location;

    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    @JsonIgnore
    @Column(name = "image_data", columnDefinition = "bytea")
    private byte[] imageData;

    private String imageType;

    @JsonProperty("isAvailable")
    @Column(name = "is_available")
    private boolean isAvailable = true;

    @ElementCollection
    @CollectionTable(name = "property_media", joinColumns = @JoinColumn(name = "property_id"))
    @Column(name = "media_url")
    private List<String> mediaUrls;
}

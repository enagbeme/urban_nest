package com.urbannest.backend.controller;

import com.urbannest.backend.entity.Property;
import com.urbannest.backend.entity.User;
import com.urbannest.backend.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/properties")
@CrossOrigin(origins = "http://localhost:8080") // Allow Thymeleaf UI access
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @GetMapping
    public List<Property> getAllProperties() {
        List<Property> props = propertyService.getAllProperties();
        System.out.println("Returning " + props.size() + " properties.");
        props.forEach(p -> System.out.println("Prop ID: " + p.getId() + ", Owner ID: " + p.getOwnerId()));
        return props;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable UUID id) {
        Property property = propertyService.getPropertyById(id);
        if (property != null) {
            return ResponseEntity.ok(property);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getPropertyImage(@PathVariable UUID id) {
        Property property = propertyService.getPropertyById(id);
        if (property != null && property.getImageData() != null) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, property.getImageType())
                    .body(property.getImageData());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = { "multipart/form-data" })
    public Property createProperty(
            @RequestPart("property") Property property,
            @RequestPart(value = "image", required = false) MultipartFile image,
            Authentication authentication
    ) throws IOException {
        User user = (User) authentication.getPrincipal();
        System.out.println("Creating property for user: " + user.getEmail() + " ID: " + user.getId());
        property.setOwnerId(user.getId());

        if (image != null && !image.isEmpty()) {
            property.setImageData(image.getBytes());
            property.setImageType(image.getContentType());
        }
        Property savedProperty = propertyService.createProperty(property);
        System.out.println("Property created with ID: " + savedProperty.getId() + " OwnerID: " + savedProperty.getOwnerId());
        return savedProperty;
    }

    @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
    public Property updateProperty(
            @PathVariable UUID id,
            @RequestPart("property") Property property,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        if (image != null && !image.isEmpty()) {
            property.setImageData(image.getBytes());
            property.setImageType(image.getContentType());
        }
        return propertyService.updateProperty(id, property);
    }

    @GetMapping("/owner/{ownerId}")
    public List<Property> getPropertiesByOwner(@PathVariable UUID ownerId) {
        System.out.println("Fetching properties for ownerId: " + ownerId);
        List<Property> props = propertyService.getPropertiesByOwner(ownerId);
        System.out.println("Found " + props.size() + " properties");
        return props;
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable UUID id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.ok().build();
    }
}

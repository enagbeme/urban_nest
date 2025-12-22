package com.urbannest.backend.service;

import com.urbannest.backend.entity.Property;
import com.urbannest.backend.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    public Property getPropertyById(UUID id) {
        return propertyRepository.findById(id).orElse(null);
    }

    public Property createProperty(Property property) {
        return propertyRepository.save(property);
    }

    public Property updateProperty(UUID id, Property updatedProperty) {
        return propertyRepository.findById(id).map(property -> {
            property.setTitle(updatedProperty.getTitle());
            property.setDescription(updatedProperty.getDescription());
            property.setCategory(updatedProperty.getCategory());
            property.setPrice(updatedProperty.getPrice());
            property.setLocation(updatedProperty.getLocation());
            property.setLatitude(updatedProperty.getLatitude());
            property.setLongitude(updatedProperty.getLongitude());
            property.setImageUrl(updatedProperty.getImageUrl());
            property.setAvailable(updatedProperty.isAvailable());
            if (updatedProperty.getImageData() != null) {
                property.setImageData(updatedProperty.getImageData());
                property.setImageType(updatedProperty.getImageType());
            }
            return propertyRepository.save(property);
        }).orElseThrow(() -> new RuntimeException("Property not found with id " + id));
    }

    public List<Property> getPropertiesByOwner(UUID ownerId) {
        return propertyRepository.findByOwnerId(ownerId);
    }
    
    public void deleteProperty(UUID id) {
        propertyRepository.deleteById(id);
    }
}

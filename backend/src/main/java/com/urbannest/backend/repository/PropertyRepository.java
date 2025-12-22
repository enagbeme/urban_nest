package com.urbannest.backend.repository;

import com.urbannest.backend.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PropertyRepository extends JpaRepository<Property, UUID> {
    List<Property> findByOwnerId(UUID ownerId);
    List<Property> findByCategory(String category);
}

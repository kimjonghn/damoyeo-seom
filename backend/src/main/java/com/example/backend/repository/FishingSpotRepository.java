package com.example.backend.repository;

import com.example.backend.entity.FishingSpot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FishingSpotRepository
        extends JpaRepository<FishingSpot, Long> {
}
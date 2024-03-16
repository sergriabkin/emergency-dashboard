package com.example.emergencydashboard.repository.jpa;

import com.example.emergencydashboard.model.IncidentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IncidentJpaRepository extends JpaRepository<IncidentEntity, UUID> {

}

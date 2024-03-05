package com.example.emergencydashboard.repository.jpa;

import com.example.emergencydashboard.model.IncidentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidentJpaRepository extends JpaRepository<IncidentEntity, String> {

}

package com.microservices.springproductsservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservices.springproductsservice.models.TaskStatus;

public interface TaskStatusRepository extends JpaRepository<TaskStatus,UUID>{    
}

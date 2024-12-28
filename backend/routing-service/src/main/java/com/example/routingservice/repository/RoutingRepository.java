package com.example.routingservice.repository;

import com.example.routingservice.model.Routing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutingRepository extends JpaRepository<Routing, Long> {

}
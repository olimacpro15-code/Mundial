package com.mundial.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mundial.model.Equipo;

public interface EquipoRepository extends MongoRepository<Equipo, String> {
}

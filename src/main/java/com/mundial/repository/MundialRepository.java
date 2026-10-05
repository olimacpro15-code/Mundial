package com.mundial.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mundial.model.Mundial;

public interface MundialRepository extends MongoRepository<Mundial, String> {
}

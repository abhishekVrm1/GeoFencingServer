package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.GeofencingStatus;

public interface IGeofencingStatusRepository extends CrudRepository<GeofencingStatus, Integer> {

}

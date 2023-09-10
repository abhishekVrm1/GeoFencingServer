package com.example.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.GeofencingConfiguration;
import com.example.demo.entity.RuleType;

public interface IGeofencingConfigurationRepository extends CrudRepository<GeofencingConfiguration, String> {

	List<GeofencingConfiguration> findAllByRuleType(RuleType ruleType);

}

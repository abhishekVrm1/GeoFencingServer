package com.example.demo.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import com.example.demo.dto.IpInfoResponse;
import com.example.demo.entity.RuleType;
import com.example.demo.exception.GeofencingException;

public interface IGeofencingService {

	Boolean toggleGeofencingStatus();

	Map<String, Object> getGeofencingStatusAndCountries();

	List<String> updateCountries(Map<String, Object> countries, RuleType ruleType);

	IpInfoResponse getOriginCountry(String ipAddress);

	Boolean getGeofencingStatus();

	Boolean checkCountryStatus(String countryCode) throws GeofencingException;

	ZonedDateTime getCurrentTimeInCountry(String countryCode);
}

package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.dto.IpInfoResponse;
import com.example.demo.entity.RuleType;
import com.example.demo.exception.GeofencingException;
import com.example.demo.service.IGeofencingService;

import jakarta.servlet.http.HttpServletRequest;

public class GeofencingControllerTest {

	@Mock
	private IGeofencingService geofencingService;

	@InjectMocks
	private GeofencingController geofencingController;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testToggleGeofencing() {
		when(geofencingService.toggleGeofencingStatus()).thenReturn(true);

		ResponseEntity<String> response = geofencingController.toggleGeofencing();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Geofencing Status : ACTIVE", response.getBody());
	}

	@Test
	public void testUpdateAllowedCountries() {
		Map<String, Object> allowedCountries = new HashMap<>();
		List<String> countries = new ArrayList<>();
		countries.add("CA");
		countries.add("RU");
		allowedCountries.put("countries", countries);

		when(geofencingService.updateCountries(allowedCountries, RuleType.ALLOWED)).thenReturn(countries);

		ResponseEntity<List<String>> response = geofencingController.updateAllowedCountries(allowedCountries);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(countries, response.getBody());
	}

	@Test
	public void testUpdateDeniedCountries() {
		Map<String, Object> deniedCountries = new HashMap<>();
		List<String> countries = new ArrayList<>();
		countries.add("CA");
		countries.add("RU");
		deniedCountries.put("countries", countries);

		when(geofencingService.updateCountries(deniedCountries, RuleType.DENIED)).thenReturn(countries);

		ResponseEntity<List<String>> response = geofencingController.updateDeniedCountries(deniedCountries);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(countries, response.getBody());
	}

	@Test
	public void testGetGeofencingStatus() {
		Map<String, Object> expectedResponse = new HashMap<>();
		List<String> allowedCountries = new ArrayList<>();
		allowedCountries.add("CA");
		allowedCountries.add("RU");

		List<String> deniedCountries = new ArrayList<>();
		deniedCountries.add("CA");
		deniedCountries.add("RU");

		expectedResponse.put("GEOFENCING STATUS: ", true);
		expectedResponse.put("ALLOWED COUNTRY CODES: ", allowedCountries);
		expectedResponse.put("DENIED COUNTRY CODES: ", deniedCountries);

		when(geofencingService.getGeofencingStatusAndCountries()).thenReturn(expectedResponse);

		ResponseEntity<Map<String, Object>> response = geofencingController.getGeofencingStatus();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedResponse, response.getBody());
	}

	@Test
	public void testGetCurrentTime() throws GeofencingException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getRemoteAddr()).thenReturn("127.0.0.1");

		IpInfoResponse ipInfoResponse = new IpInfoResponse();
		ipInfoResponse.setCountryCode("US");
		ipInfoResponse.setTimeZone("America/New_York");

		when(geofencingService.getOriginCountry("127.0.0.1")).thenReturn(ipInfoResponse);
		when(geofencingService.getGeofencingStatus()).thenReturn(true);
		when(geofencingService.checkCountryStatus("US")).thenReturn(true);

		ZonedDateTime currentTime = ZonedDateTime.now();
		when(geofencingService.getCurrentTimeInCountry("America/New_York")).thenReturn(currentTime);

		ResponseEntity<String> response = geofencingController.getCurrentTime(request);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Current Time in US : " + currentTime, response.getBody());
	}
}

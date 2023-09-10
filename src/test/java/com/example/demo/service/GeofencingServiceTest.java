package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import com.example.demo.entity.GeofencingConfiguration;
import com.example.demo.entity.GeofencingStatus;
import com.example.demo.entity.RuleType;
import com.example.demo.exception.GeofencingException;
import com.example.demo.repository.IGeofencingConfigurationRepository;
import com.example.demo.repository.IGeofencingStatusRepository;

public class GeofencingServiceTest {

	@InjectMocks
	private GeofencingService geofencingService;

	@Mock
	private IGeofencingConfigurationRepository geofencingConfigurationRepository;

	@Mock
	private IGeofencingStatusRepository geofencingStatusRepository;

	@Mock
	private RestTemplate restTemplate;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testToggleGeofencingStatus() {
		GeofencingStatus status = new GeofencingStatus();
		status.setActive(true);

		when(geofencingStatusRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(status));

		Boolean firstToggle = geofencingService.toggleGeofencingStatus();
		assertFalse(firstToggle);
		assertFalse(status.getActive());

		// Second toggle
		Boolean secondToggle = geofencingService.toggleGeofencingStatus();
		assertTrue(secondToggle);
		assertTrue(status.getActive());
	}

	@Test
	public void testGetGeofencingStatus() {
		GeofencingStatus status = new GeofencingStatus();
		status.setActive(true);

		when(geofencingStatusRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(status));

		Boolean geofencingStatus = geofencingService.getGeofencingStatus();
		assertTrue(geofencingStatus);
	}

	@Test
	public void testCheckCountryStatus() throws GeofencingException {
		GeofencingConfiguration configuration = new GeofencingConfiguration();
		configuration.setCountry_code("US");
		configuration.setRuleType(RuleType.ALLOWED);

		when(geofencingConfigurationRepository.findById("US")).thenReturn(Optional.of(configuration));

		Boolean countryStatus = geofencingService.checkCountryStatus("US");
		assertTrue(countryStatus);

		assertThrows(GeofencingException.class, () -> geofencingService.checkCountryStatus("INVALID"));
	}

	@Test
	public void testUpdateCountries() {
		GeofencingConfiguration configuration1 = new GeofencingConfiguration();
		configuration1.setCountry_code("US");
		configuration1.setRuleType(RuleType.ALLOWED);
		GeofencingConfiguration configuration2 = new GeofencingConfiguration();
		configuration2.setCountry_code("CN");
		configuration2.setRuleType(RuleType.ALLOWED);
		GeofencingConfiguration configuration3 = new GeofencingConfiguration();
		configuration3.setCountry_code("RU");
		configuration3.setRuleType(RuleType.DENIED);
		List<GeofencingConfiguration> existingConfigurations = new ArrayList<>();
		existingConfigurations.add(configuration1);
		existingConfigurations.add(configuration2);
		existingConfigurations.add(configuration3);

		when(geofencingConfigurationRepository.findAllByRuleType(RuleType.ALLOWED)).thenReturn(existingConfigurations);

		Map<String, Object> countriesToAdd = new HashMap<>();
		countriesToAdd.put("countries", Arrays.asList("CA", "GB", "DE"));

		List<String> updatedCountries = geofencingService.updateCountries(countriesToAdd, RuleType.ALLOWED);

		assertEquals(3, updatedCountries.size());
		assertTrue(updatedCountries.containsAll(Arrays.asList("CA", "GB", "DE")));

	}

	@Test
	public void testGetCurrentTimeInCountry() {
		String timeZone = "America/New_York";

		ZoneId expectedZoneId = ZoneId.of(timeZone);

		ZonedDateTime currentTimeInCountry = geofencingService.getCurrentTimeInCountry(timeZone);

		assertEquals(expectedZoneId, currentTimeInCountry.getZone());
	}
}

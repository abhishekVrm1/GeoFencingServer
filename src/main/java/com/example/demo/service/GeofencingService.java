package com.example.demo.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.IpInfoResponse;
import com.example.demo.entity.GeofencingConfiguration;
import com.example.demo.entity.GeofencingStatus;
import com.example.demo.entity.RuleType;
import com.example.demo.exception.GeofencingException;
import com.example.demo.repository.IGeofencingConfigurationRepository;
import com.example.demo.repository.IGeofencingStatusRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional
public class GeofencingService implements IGeofencingService {

	@Value("ipinfo.token")
	private String ipinfoToken;

	@Value("ipinfo.url")
	private String ipinfoUrl;

	private static final Integer GEOFENCING_STATUS_ID = 1000;

	@Autowired
	private IGeofencingConfigurationRepository geofencingConfigurationRepository;

	@Autowired
	private IGeofencingStatusRepository geofencingStatusRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public Boolean toggleGeofencingStatus() {
		GeofencingStatus status = geofencingStatusRepository.findById(GEOFENCING_STATUS_ID).orElse(null);
		Boolean flag = Boolean.TRUE;
		if (status == null) {
			initializegeofencingStatus();
		}
		if (status.getActive()) {
			flag = Boolean.FALSE;
			status.setActive(flag);

		} else {
			flag = Boolean.TRUE;
			status.setActive(flag);
		}
		geofencingStatusRepository.save(status);
		return flag;
	}

	private void initializegeofencingStatus() {
		GeofencingStatus status = new GeofencingStatus();
		status.setActive(true);
		status.setId(GEOFENCING_STATUS_ID);
		geofencingStatusRepository.save(status);
	}

	@Override
	public Map<String, Object> getGeofencingStatusAndCountries() {
		Map<String, Object> response = new HashMap<>();
		Boolean geofencingStatus = getGeofencingStatus();

		Set<String> allowedCountryCodes = new HashSet<>();
		Set<String> deniedCountryCodes = new HashSet<>();
		Iterable<GeofencingConfiguration> iterable = geofencingConfigurationRepository.findAll();
		if (iterable != null) {
			Iterator<GeofencingConfiguration> iterator = iterable.iterator();
			while (iterator.hasNext()) {
				GeofencingConfiguration config = iterator.next();
				if (RuleType.ALLOWED.equals(config.getRuleType()))
					allowedCountryCodes.add(config.getCountry_code());
				else
					deniedCountryCodes.add(config.getCountry_code());
			}
		}

		response.put("GEOFENCING STATUS: ", geofencingStatus ? "ACTIVE" : "INACTIVE");
		response.put("ALLOWED COUNTRY CODES: ", allowedCountryCodes);
		response.put("DENIED COUNTRY CODES: ", deniedCountryCodes);
		return response;
	}

	@Override
	public Boolean getGeofencingStatus() {
		Boolean flag = Boolean.TRUE;
		GeofencingStatus status = geofencingStatusRepository.findById(GEOFENCING_STATUS_ID).orElse(null);
		if (status == null) {
			initializegeofencingStatus();
		}
		flag = status.getActive();
		return flag;
	}

	@Override
	public Boolean checkCountryStatus(String countryCode) throws GeofencingException {
		GeofencingConfiguration config = geofencingConfigurationRepository.findById(countryCode)
				.orElseThrow(() -> new GeofencingException("Invalid Country Code"));
		return RuleType.ALLOWED.equals(config.getRuleType()) ? Boolean.TRUE : Boolean.FALSE;
	}

	@Override
	public List<String> updateCountries(Map<String, Object> countries, RuleType ruleType) {
		List<String> countryCodes = (List<String>) countries.get("countries");
		List<GeofencingConfiguration> allowedC = geofencingConfigurationRepository.findAllByRuleType(ruleType);
		geofencingConfigurationRepository.deleteAll(allowedC);

		Set<GeofencingConfiguration> updatedList = countryCodes.stream().map(x -> {
			GeofencingConfiguration config = new GeofencingConfiguration();
			config.setCountry_code(x);
			config.setRuleType(ruleType);
			return config;
		}).collect(Collectors.toSet());

		geofencingConfigurationRepository.saveAll(updatedList);
		return countryCodes;
	}

	@Override
	public IpInfoResponse getOriginCountry(String ipAddress) {
		try {
			String apiUrl = ipinfoUrl + "/" + ipAddress + "?token=" + ipinfoToken;

			ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
			if (response.getStatusCode().is2xxSuccessful()) {
				String responseBody = response.getBody();
				ObjectMapper mapper = new ObjectMapper();
				JsonNode jsonNode = mapper.readTree(responseBody);
				String countryCode = jsonNode.path("country").asText();
				String timeZone = jsonNode.path("timezone").asText();
				return new IpInfoResponse(countryCode, timeZone);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ZonedDateTime getCurrentTimeInCountry(String timeZone) {
		ZoneId timeZoneId = ZoneId.of(timeZone);
		ZonedDateTime currentTime = ZonedDateTime.now(timeZoneId);
		return currentTime;
	}

}

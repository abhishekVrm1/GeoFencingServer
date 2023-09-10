package com.example.demo.controller;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.IpInfoResponse;
import com.example.demo.entity.RuleType;
import com.example.demo.exception.GeofencingException;
import com.example.demo.service.IGeofencingService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class GeofencingController {

	@Autowired
	private IGeofencingService geofencingService;

	@PostMapping("/toggle")
	public ResponseEntity<String> toggleGeofencing() {
		boolean activeStatus = geofencingService.toggleGeofencingStatus();
		return ResponseEntity.ok("Geofencing Status : " + (activeStatus ? "ACTIVE" : "INACTIVE"));
	}

	@PutMapping("/allowed")
	public ResponseEntity<List<String>> updateAllowedCountries(@RequestBody Map<String, Object> allowedCountries) {
		return ResponseEntity.ok(geofencingService.updateCountries(allowedCountries, RuleType.ALLOWED));
	}

	@PutMapping("/denied")
	public ResponseEntity<List<String>> updateDeniedCountries(@RequestBody Map<String, Object> deniedCountries) {
		return ResponseEntity.ok(geofencingService.updateCountries(deniedCountries, RuleType.DENIED));
	}

	@GetMapping("/status")
	public ResponseEntity<Map<String, Object>> getGeofencingStatus() {
		Map<String, Object> response = geofencingService.getGeofencingStatusAndCountries();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/current-time")
	public ResponseEntity<String> getCurrentTime(HttpServletRequest request) throws GeofencingException {
		String clientIp = request.getRemoteAddr();

		IpInfoResponse ipInfoResponse = geofencingService.getOriginCountry(clientIp);
		String originCountry = ipInfoResponse.getCountryCode();
		if (!geofencingService.getGeofencingStatus() || geofencingService.checkCountryStatus(originCountry)) {
			ZonedDateTime currentTime = geofencingService.getCurrentTimeInCountry(ipInfoResponse.getTimeZone());
			return ResponseEntity.ok("Current Time in " + originCountry + " : " + currentTime);
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Request DENIED for country: " + originCountry);
		}
	}

}

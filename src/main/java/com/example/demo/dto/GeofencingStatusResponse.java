package com.example.demo.dto;

import java.util.Set;

public class GeofencingStatusResponse {
	private boolean active;
	private Set<String> allowedCountries;
	private Set<String> deniedCountries;

	public GeofencingStatusResponse() {
		// Default constructor for serialization/deserialization
	}

	public GeofencingStatusResponse(boolean active, Set<String> allowedCountries, Set<String> deniedCountries) {
		this.active = active;
		this.allowedCountries = allowedCountries;
		this.deniedCountries = deniedCountries;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Set<String> getAllowedCountries() {
		return allowedCountries;
	}

	public void setAllowedCountries(Set<String> allowedCountries) {
		this.allowedCountries = allowedCountries;
	}

	public Set<String> getDeniedCountries() {
		return deniedCountries;
	}

	public void setDeniedCountries(Set<String> deniedCountries) {
		this.deniedCountries = deniedCountries;
	}
}

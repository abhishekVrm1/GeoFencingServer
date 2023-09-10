package com.example.demo.dto;

import java.util.Set;

public class GeofencingCountryRequest {

	private Set<String> countries;

	public Set<String> getCountries() {
		return countries;
	}

	public void setCountries(Set<String> countries) {
		this.countries = countries;
	}
}

package com.example.demo.dto;

public class IpInfoResponse {

	private String countryCode;
	private String timeZone;

	public IpInfoResponse() {
		super();
	}

	public IpInfoResponse(String countryCode, String timeZone) {
		super();
		this.countryCode = countryCode;
		this.timeZone = timeZone;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

}

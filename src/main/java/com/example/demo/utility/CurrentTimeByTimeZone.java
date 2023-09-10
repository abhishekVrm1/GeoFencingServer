package com.example.demo.utility;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class CurrentTimeByTimeZone {

	public static void main(String[] args) {
		// Example: Time zone information from IPINFO response
		String timeZoneIdFromIpInfo = "Asia/Kolkata";

		// Step 1: Parse the time zone
		ZoneId timeZone = ZoneId.of(timeZoneIdFromIpInfo);

		// Step 2: Get the current time in the specified time zone
		ZonedDateTime currentTime = ZonedDateTime.now(timeZone);

		System.out.println("Current time in " + timeZoneIdFromIpInfo + ": " + currentTime);
	}
}

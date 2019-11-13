package com.pepe.sensor.persistence;

import lombok.Data;

import java.util.UUID;

/**
 * TemporaryToken
 * <p>
 * This class represents a token that can be used for user activation or
 * password reset. Tokens are UUID and have expiration time. They have OneToOne
 * relationship with person.
 */
@Data
public class TemporaryToken {

	// Expiration time set to 24h
	private static final long TOKEN_EXPIRATION_TIME_MILLIS = 24 * 60 * 60 * 1000L;

	private String token;
	private long expirationTimestamp;

	public TemporaryToken() {
		token = UUID.randomUUID().toString();
		expirationTimestamp = System.currentTimeMillis() + TOKEN_EXPIRATION_TIME_MILLIS;
	}

	public boolean hasExpired() {
		return expirationTimestamp < System.currentTimeMillis();
	}

}

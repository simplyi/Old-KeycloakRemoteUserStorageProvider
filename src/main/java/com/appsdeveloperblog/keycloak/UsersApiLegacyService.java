package com.appsdeveloperblog.keycloak;

import java.io.IOException;

import javax.ws.rs.PathParam;

import org.jboss.logging.Logger;
import org.keycloak.broker.provider.util.SimpleHttp;
import org.keycloak.models.KeycloakSession;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UsersApiLegacyService {
	KeycloakSession session;

	public UsersApiLegacyService(KeycloakSession session) {
		this.session = session;
	}

	private static final Logger LOG = Logger.getLogger(UsersApiLegacyService.class);

	User getUserByUserName(String username) {
		try {
			return SimpleHttp.doGet("http://localhost:8099/users/" + username, this.session).asJson(User.class);
		} catch (IOException e) {
			LOG.warn("Error fetching user " + username + " from external service: " + e.getMessage(), e);
		}
		return null;
	}

	VerifyPasswordResponse verifyUserPassword(@PathParam("username") String username, String password) {
		SimpleHttp simpleHttp = SimpleHttp.doPost("http://localhost:8099/users/" + username + "/verify-password",
				this.session);

		VerifyPasswordResponse verifyPasswordResponse = null;

		// Add the request parameters as a map
		simpleHttp.param("password", password);
		
		// Add any headers if needed
		simpleHttp.header("Content-Type", "application/x-www-form-urlencoded");

		try {
			String response = simpleHttp.asString();

			// Create an ObjectMapper instance
			ObjectMapper mapper = new ObjectMapper();

			// Convert the JSON string to a VerifyPasswordResponse object
			verifyPasswordResponse = mapper.readValue(response, VerifyPasswordResponse.class);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return verifyPasswordResponse;
	}
}

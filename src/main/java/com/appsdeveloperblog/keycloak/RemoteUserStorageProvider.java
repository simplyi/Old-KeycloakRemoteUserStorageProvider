package com.appsdeveloperblog.keycloak;

import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.credential.LegacyUserCredentialManager;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.SubjectCredentialManager;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.adapter.AbstractUserAdapter;
import org.keycloak.storage.user.UserLookupProvider;

public class RemoteUserStorageProvider implements UserLookupProvider, CredentialInputValidator, UserStorageProvider {
	private KeycloakSession session;
	private ComponentModel model;
	private UsersApiLegacyService usersService;

	public RemoteUserStorageProvider(KeycloakSession session, ComponentModel model,
			UsersApiLegacyService usersService) {
		this.session = session;
		this.model = model;
		this.usersService = usersService;
	}

	protected UserModel createAdapter(RealmModel realm, String username) {

		// Create a new user adapter based on the AbstractUserAdapter class
		return new AbstractUserAdapter(session, realm, model) {

			// Override the getUsername method to return the username from the remote
			// service
			@Override

			public String getUsername() {

				return username;

			}

			@Override
			public SubjectCredentialManager credentialManager() {

				// Create a new credential manager based on the LegacyUserCredentialManager
				// class
				return new LegacyUserCredentialManager(session, realm, this) {

				};

			}

		};
	}

	@Override
	public boolean supportsCredentialType(String credentialType) {

		return PasswordCredentialModel.TYPE.equals(credentialType);
	}

	@Override
	public boolean isValid(RealmModel realm, UserModel user, CredentialInput credentialInput) {
		VerifyPasswordResponse verifyPasswordResponse = usersService.verifyUserPassword(user.getUsername(),
				credentialInput.getChallengeResponse());

		if (verifyPasswordResponse == null)
			return false;

		return verifyPasswordResponse.getResult();
	}

	@Override
	public UserModel getUserById(RealmModel realm, String id) {
		StorageId storageId = new StorageId(id);
		String username = storageId.getExternalId();
		
		return getUserByUsername(realm, username);
	}

	@Override
	public UserModel getUserByUsername(RealmModel realm, String username) {
		UserModel returnValue = null;

		User user = usersService.getUserByUserName(username);

		if (user != null) {
			returnValue = createAdapter(realm, username);
		}

		return returnValue;
	}

	@Override
	public UserModel getUserByEmail(RealmModel realm, String email) {
		return getUserByUsername(realm, email);
	}

	@Override
	public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
		return user.credentialManager().isConfiguredFor(credentialType);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

}

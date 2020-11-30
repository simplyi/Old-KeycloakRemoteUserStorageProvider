package com.appsdeveloperblog.keycloak;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;

public class RemoteUserStorageProviderFactory implements UserStorageProviderFactory<RemoteUserStorageProvider>{

	public static final String PROVIDER_NAME="my-remote-mysql-user-storage-provider";
	
	@Override
	public RemoteUserStorageProvider create(KeycloakSession session, ComponentModel model) {
		 
		return new RemoteUserStorageProvider(session, 
				model, 
				buildHttpClient("http://localhost:8099"));
	}

	@Override
	public String getId() {
		return PROVIDER_NAME;
	}
	
	private UsersApiService buildHttpClient(String uri) {
		
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(uri);
		
		return target.proxyBuilder(UsersApiService.class)
				.classloader(UsersApiService.class.getClassLoader()).build();
		
	}

}

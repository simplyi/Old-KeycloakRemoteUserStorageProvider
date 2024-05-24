package com.appsdeveloperblog.keycloak;

import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.UserCredentialManager;
import org.keycloak.models.*;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageUtil;
import org.keycloak.storage.adapter.AbstractUserAdapter;
import org.keycloak.storage.federated.UserFederatedStorageProvider;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserAdapter extends AbstractUserAdapter {

    private final User user;

    public UserAdapter(KeycloakSession session, RealmModel realm, ComponentModel model, User user) {
        super(session, realm, model);
        this.storageId = new StorageId(storageProviderModel.getId(), user.getEmail());
        this.user = user;
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public String getFirstName() {
        return user.getFirstName();
    }

    @Override
    public String getLastName() {
        return user.getLastName();
    }

    @Override
    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public SubjectCredentialManager credentialManager() {
        return new UserCredentialManager(session, realm, this);
    }

    @Override
    public String getFirstAttribute(String name) {
        List<String> list = getAttributes().getOrDefault(name, List.of());
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Map<String, List<String>> getAttributes() {
        MultivaluedHashMap<String, String> attributes = new MultivaluedHashMap<>();
        attributes.add(UserModel.USERNAME, getUsername());
        attributes.add(UserModel.EMAIL, getEmail());
        attributes.add(UserModel.FIRST_NAME, getFirstName());
        attributes.add(UserModel.LAST_NAME, getLastName());
        return attributes;
    }

    @Override
    public Stream<String> getAttributeStream(String name) {
        Map<String, List<String>> attributes = getAttributes();
        return (attributes.containsKey(name)) ? attributes.get(name).stream() : Stream.empty();
    }

}
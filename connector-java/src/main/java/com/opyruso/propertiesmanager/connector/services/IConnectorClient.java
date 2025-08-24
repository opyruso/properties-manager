package com.opyruso.propertiesmanager.connector.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Path;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.opyruso.propertiesmanager.api.IConnectorResources;

import io.quarkus.oidc.client.reactive.filter.OidcClientRequestReactiveFilter;

@ApplicationScoped
@Path("/pm-api/connector")
@RegisterRestClient(configKey = "propertiesmanager-api")
@RegisterProvider(OidcClientRequestReactiveFilter.class)
public interface IConnectorClient extends IConnectorResources {

}

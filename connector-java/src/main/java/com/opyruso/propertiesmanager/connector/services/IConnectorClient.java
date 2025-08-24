package com.opyruso.propertiesmanager.connector.services;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Path;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.opyruso.propertiesmanager.api.IConnectorResources;

import io.quarkus.oidc.client.filter.OidcClientRequestFilter;

@ApplicationScoped
@Path("/pm-api/connector")
@RegisterRestClient(configKey = "propertiesmanager-api")
@RegisterProvider(OidcClientRequestFilter.class)
public interface IConnectorClient extends IConnectorResources {

}

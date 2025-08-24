package com.opyruso.propertiesmanager.connector.services;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Base64;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.opyruso.propertiesmanager.api.entity.ApiLog;
import com.opyruso.propertiesmanager.api.entity.request.ApiAddOrUpdateFileRequest;
import com.opyruso.propertiesmanager.api.entity.request.ApiGenerateConfigRequest;
import com.opyruso.propertiesmanager.api.entity.request.ApiNewApplicationRequest;
import com.opyruso.propertiesmanager.api.entity.response.ApiGenerateConfigResponse;
import com.opyruso.propertiesmanager.utils.FileUtils;

import io.quarkus.oidc.client.NamedOidcClient;
import io.quarkus.oidc.client.OidcClient;

@ApplicationScoped
public class UpdatePropertiesCommandService {

	@Inject
	@NamedOidcClient("connectoruser")
	OidcClient client;

	@Inject
    @RestClient
	public IConnectorClient apiClient;

	public void process(String projectId, String version, String env, File file, String filenameFilter) throws Exception {
		System.out.println(client);
		System.out.println(client.getTokens());
		System.out.println(client.getTokens().toString());
		if (!file.exists()) return;
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				process(projectId, version, env, f, filenameFilter);
			}
		} else if (file.isFile() && file.getName().matches(filenameFilter)) {
			System.out.println(file.getAbsolutePath() + " is a file => processing");

			ApiGenerateConfigRequest request = new ApiGenerateConfigRequest();
			request.appId = projectId;
			request.version = version;
			request.envId = env;
			request.filename = file.getName();
			request.fileContentAsBase64 = Base64.getEncoder().encodeToString(FileUtils.getFileContent(file).getBytes());
			
			ApiNewApplicationRequest application = new ApiNewApplicationRequest();
			application.appId = projectId;
			application.appLabel = projectId;
			application.productOwner = "Unknown";

			ApiAddOrUpdateFileRequest addOrUpdateFileRequest = new ApiAddOrUpdateFileRequest();
			addOrUpdateFileRequest.filename = file.getName();
			addOrUpdateFileRequest.contentAsBase64 = request.fileContentAsBase64;

			apiClient.createApplication(application);
			apiClient.addVersion(projectId, version, addOrUpdateFileRequest);
			apiClient.addInstalledVersion(projectId, version, env);
			apiClient.addOrUpdateFile(projectId, version, addOrUpdateFileRequest);
			ApiGenerateConfigResponse response = apiClient.getGeneratedConfig(request);
			if (response.logs != null) {
				for (ApiLog log : response.logs) {
					System.out.println(log.status + " : " + log.comment);
				}
			} else {
				System.out.println("WARNING : Pas de log retour...");
			}
			System.out.println("Updated : " + Files.write(file.toPath(), Base64.getDecoder().decode(response.fileContentAsBase64), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE).toAbsolutePath());
			
		} else {
			throw new Exception("File (" + file.getAbsolutePath() + ") is not a correct file");
		}
		
	}

	
	
}

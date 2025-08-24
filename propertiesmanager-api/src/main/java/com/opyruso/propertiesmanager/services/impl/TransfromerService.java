package com.opyruso.propertiesmanager.services.impl;

import java.io.File;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.WebApplicationException;

import org.eclipse.microprofile.jwt.JsonWebToken;

import com.opyruso.propertiesmanager.constants.EnvironmentConfig;
import com.opyruso.propertiesmanager.services.ITransformerService;
import com.opyruso.propertiesmanager.transformers.ITransformerFactory;
import com.opyruso.propertiesmanager.transformers.impl.DummyTransformerFactory;
import com.opyruso.propertiesmanager.transformers.impl.IBMIntegrationBusPropertiesTransformerFactory;
import com.opyruso.propertiesmanager.transformers.impl.JsonTransformerFactory;
import com.opyruso.propertiesmanager.transformers.impl.PropertiesTransformerFactory;
import com.opyruso.propertiesmanager.transformers.impl.XmlTransformerFactory;
import com.opyruso.propertiesmanager.transformers.impl.YamlTransformerFactory;
import com.opyruso.propertiesmanager.utils.FileUtils;

import io.quarkus.logging.Log;

@ApplicationScoped
public class TransfromerService implements ITransformerService {

	@Inject
	protected EnvironmentConfig environmentConfig;

	@Inject
	@Named(value = "dummyTransformerFactory")
	protected DummyTransformerFactory dummyTransformerFactory;

	@Inject
	@Named(value = "propertiesTransformerFactory")
	protected PropertiesTransformerFactory propertiesTransformerFactory;

	@Inject
	@Named(value = "ibmIntegrationBusPropertiesTransformerFactory")
	protected IBMIntegrationBusPropertiesTransformerFactory ibmIntegrationBusPropertiesTransformerFactory;

	@Inject
	@Named(value = "yamlTransformerFactory")
	protected YamlTransformerFactory yamlPropertiesTransformerFactory;

	@Inject
	@Named(value = "jsonTransformerFactory")
	protected JsonTransformerFactory jsonTransformerFactory;

	@Inject
	@Named(value = "xmlTransformerFactory")
	protected XmlTransformerFactory xmlTransformerFactory;

	@Inject
	protected JsonWebToken jwt;

	@Override
	public ITransformerFactory processTransformation(String appId, String version, String envId, String filename, File file) throws WebApplicationException {
		ITransformerFactory factory = null;
		try {
			String fileContent = FileUtils.getFileContent(file);
			if (filename.endsWith(".xml")) factory = xmlTransformerFactory.init(appId, version, envId, filename, file);
			else if (filename.toLowerCase().endsWith("web.config")) factory = xmlTransformerFactory.init(appId, version, envId, filename, file);
			else if (filename.endsWith(".json") || filename.endsWith(".json")) factory = jsonTransformerFactory.init(appId, version, envId, filename, file);
			else if (filename.endsWith(".bar.properties")) factory = ibmIntegrationBusPropertiesTransformerFactory.init(appId, version, envId, filename, file);
			else if (filename.endsWith(".yaml") || filename.endsWith(".yml")) factory = yamlPropertiesTransformerFactory.init(appId, version, envId, filename, file);
			else if (filename.endsWith(".properties")) factory = propertiesTransformerFactory.init(appId, version, envId, filename, file);
			else if (fileContent.startsWith("<")) factory = xmlTransformerFactory.init(appId, version, envId, filename, file);
			else if (fileContent.startsWith("{") || fileContent.startsWith("[")) factory = jsonTransformerFactory.init(appId, version, envId, filename, file);
			else if (fileContent.startsWith("---")) factory = yamlPropertiesTransformerFactory.init(appId, version, envId, filename, file);
			else if (fileContent.matches("\n[^\n]+=[^\n]+\n")) factory = propertiesTransformerFactory.init(appId, version, envId, filename, file);
			else factory = dummyTransformerFactory.init(appId, version, envId, filename, file);
			factory.process();
		} catch (Exception e) {
			Log.error("error during transformation : ", e);
		}
		
		
		return factory;
	}
	
}

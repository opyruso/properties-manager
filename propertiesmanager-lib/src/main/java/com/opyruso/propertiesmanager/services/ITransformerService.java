package com.opyruso.propertiesmanager.services;

import java.io.File;

import javax.ws.rs.WebApplicationException;

import com.opyruso.propertiesmanager.transformers.ITransformerFactory;

public interface ITransformerService {

	ITransformerFactory processTransformation(String appId, String version, String envId, String filename, File file) throws WebApplicationException;

}

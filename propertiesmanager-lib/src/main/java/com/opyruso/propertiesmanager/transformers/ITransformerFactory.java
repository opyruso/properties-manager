package com.opyruso.propertiesmanager.transformers;

import java.io.File;
import java.util.List;

import com.opyruso.propertiesmanager.api.entity.ApiLog;

public interface ITransformerFactory {

	void preTransform() throws Exception;

	void postTransform() throws Exception;

	List<String> transform() throws Exception;

	void process() throws Exception;

	File getResult() throws Exception;

	List<ApiLog> getLog() throws Exception;

}

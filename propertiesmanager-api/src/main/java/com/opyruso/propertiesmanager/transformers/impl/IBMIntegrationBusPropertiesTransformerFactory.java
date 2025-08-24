package com.opyruso.propertiesmanager.transformers.impl;

import java.text.DateFormat;
import java.util.Date;

import jakarta.inject.Named;
import jakarta.inject.Singleton;

import com.opyruso.propertiesmanager.api.entity.ApiLog;
import com.opyruso.propertiesmanager.constants.TransformerLogStatusEnum;
import com.opyruso.propertiesmanager.transformers.ITransformerFactory;
import com.opyruso.propertiesmanager.utils.FileUtils;

import io.quarkus.logging.Log;

@Singleton
@Named(value = "ibmIntegrationBusPropertiesTransformerFactory")
public class IBMIntegrationBusPropertiesTransformerFactory extends PropertiesTransformerFactory implements ITransformerFactory {

	@Override
	public void preTransform() throws Exception {
		Log.info("IBMIntegrationBusPropertiesTransformerFactory transformation start");
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "########################"));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "UPDATED by PropertiesManager (copyright oPyRuSo) on " + DateFormat.getInstance().format(new Date())));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "########################"));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "MODIFICATION DETAILS"));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "########################"));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "TYPE : PROPERTIES (IBM)"));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "APPLICATION" + getAppId()));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "VERSION" + getVersion()));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "ENVIRONMENT" + getEnvId()));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "FILENAME" + getFilename()));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "########################"));

		String lines[] = FileUtils.getFileContent(file).split("\n");
		String resultAsString = "";
		for (String line : lines) {
			if (line.matches(LINE_REGEX)) resultAsString += line + "\n";
		}
		file = FileUtils.createTempFile(resultAsString);
	}

	@Override
	public void postTransform() throws Exception {
	}

}

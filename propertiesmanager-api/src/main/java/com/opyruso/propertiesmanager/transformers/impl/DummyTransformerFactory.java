package com.opyruso.propertiesmanager.transformers.impl;

import java.text.DateFormat;
import java.util.Date;

import jakarta.inject.Named;
import jakarta.inject.Singleton;

import com.opyruso.propertiesmanager.api.entity.ApiLog;
import com.opyruso.propertiesmanager.constants.TransformerLogStatusEnum;
import com.opyruso.propertiesmanager.transformers.BaseSearchAndReplaceTransformerFactory;
import com.opyruso.propertiesmanager.transformers.ITransformerFactory;

import io.quarkus.logging.Log;

@Singleton
@Named(value = "dummyTransformerFactory")
public class DummyTransformerFactory extends BaseSearchAndReplaceTransformerFactory implements ITransformerFactory {

	@Override
	public void preTransform() throws Exception {
		Log.info("DummyTransformerFactory transformation start");
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "########################"));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "UPDATED by PropertiesManager (copyright oPyRuSo) on " + DateFormat.getInstance().format(new Date())));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "########################"));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "MODIFICATION DETAILS"));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "########################"));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "TYPE : DUMMY (search and replace)"));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "APPLICATION : " + getAppId()));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "VERSION : " + getVersion()));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "ENVIRONMENT : " + getEnvId()));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "FILENAME : " + getFilename()));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "########################"));
	}

	@Override
	public void postTransform() throws Exception {
	}

}

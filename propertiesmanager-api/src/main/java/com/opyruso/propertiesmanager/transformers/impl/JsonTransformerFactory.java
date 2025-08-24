package com.opyruso.propertiesmanager.transformers.impl;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.inject.Named;
import jakarta.inject.Singleton;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.opyruso.propertiesmanager.api.entity.ApiLog;
import com.opyruso.propertiesmanager.constants.TransformerLogStatusEnum;
import com.opyruso.propertiesmanager.data.entity.PropertyValue;
import com.opyruso.propertiesmanager.transformers.BaseTransformerFactory;
import com.opyruso.propertiesmanager.transformers.ITransformerFactory;
import com.opyruso.propertiesmanager.utils.FileUtils;

import io.quarkus.logging.Log;

@Singleton
@Named(value = "jsonTransformerFactory")
public class JsonTransformerFactory extends BaseTransformerFactory implements ITransformerFactory {

	@Override
	public void preTransform() throws Exception {
		Log.info("JsonTransformerFactory transformation start");
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "########################"));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "UPDATED by PropertiesManager (copyright oPyRuSo) on " + DateFormat.getInstance().format(new Date())));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "########################"));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "MODIFICATION DETAILS"));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "########################"));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "TYPE : JSON"));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "APPLICATION :" + getAppId()));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "VERSION :" + getVersion()));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "ENVIRONMENT :" + getEnvId()));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "FILENAME :" + getFilename()));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "########################"));
	}

	@Override
	public void postTransform() throws Exception {
	}

	@Override
	public List<String> transform() throws Exception {
		result = FileUtils.getFileContent(file);

		for (PropertyValue pv : getDatabaseValues().values()) {
			log.add(new ApiLog(TransformerLogStatusEnum.INFO, "# DATABASE DATA        # " + pv.getPk().getFilename() + "@" + pv.getPk().getPropertyKey()));
		}
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "########################"));
		List<String> usedCache = new ArrayList<String>();

		for (String key : getDatabaseValues().keySet()) {
			PropertyValue prop = getDatabaseValues().get(key);
			String k = prop.getPk().getPropertyKey();
			String v = prop.getNewValue();
			if (!prop.getPk().getFilename().equals(getFilename())) continue;

			try {
				result = JsonPath.parse(result).set(k, v).jsonString();
				if (!usedCache.contains(key)) usedCache.add(key);
				log.add(new ApiLog(TransformerLogStatusEnum.valueOf("UPDATED_" + prop.getStatus()), "" + prop.getPk().getPropertyKey() + "=" + prop.getNewValue()));
		    } catch (PathNotFoundException e) {
				if (!usedCache.contains(key)) usedCache.add(key);
				log.add(new ApiLog(TransformerLogStatusEnum.NOT_FOUND, "" + prop.getPk().getPropertyKey() + "=" + prop.getNewValue()));
		    }

		}
		return usedCache;
	}

}

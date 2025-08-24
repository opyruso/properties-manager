package com.opyruso.propertiesmanager.transformers.impl;

import java.nio.file.Files;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.eclipse.microprofile.jwt.JsonWebToken;

import com.opyruso.propertiesmanager.api.entity.ApiLog;
import com.opyruso.propertiesmanager.api.entity.ApiLogKeyValue;
import com.opyruso.propertiesmanager.constants.TransformerLogStatusEnum;
import com.opyruso.propertiesmanager.data.entity.PropertyValue;
import com.opyruso.propertiesmanager.transformers.BaseTransformerFactory;
import com.opyruso.propertiesmanager.transformers.ITransformerFactory;

import io.quarkus.logging.Log;

@Singleton
@Named(value = "propertiesTransformerFactory")
public class PropertiesTransformerFactory extends BaseTransformerFactory implements ITransformerFactory {
	
	public static String LINE_REGEX = "^\\s*([^#]{1}[^=]+?)\\s*=\\s*(.*?)\\s*$";

	@Inject
	protected JsonWebToken jwt;

	@Override
	public void preTransform() throws Exception {
		Log.info("PropertiesTransformerFactory transformation start");
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "########################"));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "UPDATED by PropertiesManager (copyright oPyRuSo) on " + DateFormat.getInstance().format(new Date())));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "########################"));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "MODIFICATION DETAILS"));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "########################"));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "TYPE : PROPERTIES"));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "APPLICATION : " + getAppId()));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "VERSION : " + getVersion()));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "ENVIRONMENT : " + getEnvId()));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "FILENAME : " + getFilename()));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "########################"));
	}

	@Override
	public void postTransform() throws Exception {
		Log.info("PropertiesTransformerFactory transformation end");
	}

	@Override
	public List<String> transform() throws Exception {
		List<String> lines = Files.readAllLines(file.toPath());
		String newContent = "";
		for (PropertyValue pv : getDatabaseValues().values()) {
			log.add(new ApiLog(TransformerLogStatusEnum.INFO, "# DATABASE DATA        # " + pv.getPk().getFilename() + "@" + pv.getPk().getPropertyKey()));
		}
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "########################"));
		Pattern pattern = Pattern.compile(LINE_REGEX, Pattern.DOTALL);
		List<String> usedCache = new ArrayList<String>();
		for (String line : lines) {
			Matcher m = pattern.matcher(line);
			if (m.find()) {
				String key = m.group(1);
				String value = m.group(2);
				String databaseKey = getFilename() + "@" +  key;
				if (getDatabaseValues().containsKey(databaseKey)) {
					if (!usedCache.contains(databaseKey)) usedCache.add(databaseKey);
					PropertyValue prop = getDatabaseValues().get(databaseKey);
					if (!prop.getPk().getFilename().equals(getFilename())) continue;
					switch (prop.getOperationType()) {
					case DEL:
						newContent += "# " + prop.getPk().getPropertyKey() + "=" + prop.getNewValue();
						log.add(new ApiLog(TransformerLogStatusEnum.valueOf("REMOVED_" + prop.getStatus()), "# " + prop.getPk().getPropertyKey() + "=" + secureValue(prop),
								new ApiLogKeyValue("propertyKey", prop.getPk().getPropertyKey()),
								new ApiLogKeyValue("value", prop.getNewValue())
								));
						break;
					case ADD:
						newContent += prop.getPk().getPropertyKey() + "=" + prop.getNewValue();
						log.add(new ApiLog(TransformerLogStatusEnum.valueOf("UPDATED_" + prop.getStatus()), prop.getPk().getPropertyKey() + "=" + secureValue(prop),
								new ApiLogKeyValue("propertyKey", prop.getPk().getPropertyKey()),
								new ApiLogKeyValue("value", prop.getNewValue())
								));
						break;
					}
				} else {
					newContent += key + "=" + value;
					log.add(new ApiLog(TransformerLogStatusEnum.UNCHANGED, key + "=" + value,
							new ApiLogKeyValue("propertyKey", key),
							new ApiLogKeyValue("value", value)
							));
				}
			} else {
				newContent += line;
				log.add(new ApiLog(TransformerLogStatusEnum.IGNORED, line));
			}
			newContent += "\n";
		}
		for (String key : getDatabaseValues().keySet()) {
			if (!usedCache.contains(key)) {
				usedCache.add(key);
				PropertyValue prop = getDatabaseValues().get(key);
				if (!prop.getPk().getFilename().equals(getFilename())) continue;
				switch (prop.getOperationType()) {
				case DEL:
					newContent += "# " + prop.getPk().getPropertyKey() + "=" + prop.getNewValue();
					log.add(new ApiLog(TransformerLogStatusEnum.NOT_FOUND, "# " + prop.getPk().getPropertyKey() + "=" + secureValue(prop),
							new ApiLogKeyValue("propertyKey", prop.getPk().getPropertyKey()),
							new ApiLogKeyValue("value", prop.getNewValue())
							));
					break;
				case ADD:
					switch (prop.getStatus()) {
					case VALID:
						newContent += prop.getPk().getPropertyKey() + "=" + prop.getNewValue();
						log.add(new ApiLog(TransformerLogStatusEnum.ADDED_VALID, prop.getPk().getPropertyKey() + "=" + secureValue(prop),
								new ApiLogKeyValue("propertyKey", prop.getPk().getPropertyKey()),
								new ApiLogKeyValue("value", prop.getNewValue())
								));
						break;
					case TO_VALIDATE:
						newContent += prop.getPk().getPropertyKey() + "=" + prop.getNewValue();
						log.add(new ApiLog(TransformerLogStatusEnum.ADDED_TO_VALIDATE, prop.getPk().getPropertyKey() + "=" + secureValue(prop),
								new ApiLogKeyValue("propertyKey", prop.getPk().getPropertyKey()),
								new ApiLogKeyValue("value", prop.getNewValue())
								));
						break;
					}
					break;
				}
				newContent += "\n";
			}
		}
		result = newContent;
		return usedCache;
	}

}

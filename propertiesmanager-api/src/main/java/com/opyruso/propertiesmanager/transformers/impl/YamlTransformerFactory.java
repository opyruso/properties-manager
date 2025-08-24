package com.opyruso.propertiesmanager.transformers.impl;

import java.nio.file.Files;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import org.eclipse.microprofile.jwt.JsonWebToken;

import com.opyruso.propertiesmanager.api.entity.ApiLog;
import com.opyruso.propertiesmanager.api.entity.ApiLogKeyValue;
import com.opyruso.propertiesmanager.constants.TransformerLogStatusEnum;
import com.opyruso.propertiesmanager.data.entity.PropertyValue;
import com.opyruso.propertiesmanager.transformers.BaseTransformerFactory;
import com.opyruso.propertiesmanager.transformers.ITransformerFactory;
import com.opyruso.propertiesmanager.utils.YamlUtils;
import com.opyruso.propertiesmanager.utils.entity.YamlLine;

import io.quarkus.logging.Log;

@Singleton
@Named(value = "yamlTransformerFactory")
public class YamlTransformerFactory extends BaseTransformerFactory implements ITransformerFactory {
	
	@Inject
	JsonWebToken jwt;

	@Override
	public void preTransform() throws Exception {
		Log.info("YamlTransformerFactory transformation start");
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "########################"));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "UPDATED by PropertiesManager (copyright oPyRuSo) on " + DateFormat.getInstance().format(new Date())));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "########################"));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "MODIFICATION DETAILS"));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "########################"));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "TYPE : YAML"));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "APPLICATION" + getAppId()));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "VERSION" + getVersion()));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "ENVIRONMENT" + getEnvId()));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "FILENAME" + getFilename()));
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "########################"));
	}

	@Override
	public void postTransform() throws Exception {
	}

	@Override
	public List<String> transform() throws Exception {
		List<String> lines = Files.readAllLines(file.toPath());
		List<YamlLine> extractedKeysIndex = YamlUtils.yamlReader(file);
		
		for (PropertyValue pv : getDatabaseValues().values()) {
			log.add(new ApiLog(TransformerLogStatusEnum.INFO, "# DATABASE DATA        # " + pv.getPk().getFilename() + "@" + pv.getPk().getPropertyKey()));
		}
		log.add(new ApiLog(TransformerLogStatusEnum.INFO, "########################"));
		List<String> usedCache = new ArrayList<String>();
		String newContent = "";
		for (int i = 0; i < lines.size(); i++) {
			if (i <= extractedKeysIndex.size() - 1 && extractedKeysIndex.get(i) != null) {
				YamlLine yamlLine = extractedKeysIndex.get(i);
				String lineKey = extractedKeysIndex.get(i).path;
				String lineValue = extractedKeysIndex.get(i).value;
				Log.info("found key line " + i + " : " + lineKey);
				if (getDatabaseValues().containsKey(filename + "@" + lineKey)) {
					if (!usedCache.contains(filename + "@" + lineKey)) usedCache.add(filename + "@" + lineKey);
					PropertyValue prop = getDatabaseValues().get(filename + "@" + lineKey);
					if (!prop.getPk().getFilename().equals(getFilename())) {
						newContent += lines.get(i);
					} else {
						switch (prop.getOperationType()) {
						case DEL:
							newContent += "# " + yamlLine.getLineWithoutValue() + prop.getNewValue();
							log.add(new ApiLog(TransformerLogStatusEnum.valueOf("REMOVED_" + prop.getStatus()), "# " + yamlLine.getLineWithoutValue() + secureValue(prop),
									new ApiLogKeyValue("propertyKey", lineKey),
									new ApiLogKeyValue("value", prop.getNewValue())
									));
							break;
						case ADD:
							newContent += yamlLine.getLineWithoutValue() + prop.getNewValue();
							log.add(new ApiLog(TransformerLogStatusEnum.valueOf("UPDATED_" + prop.getStatus()), yamlLine.getLineWithoutValue() + secureValue(prop),
									new ApiLogKeyValue("propertyKey", lineKey),
									new ApiLogKeyValue("value", prop.getNewValue())
									));
							break;
						}
					}
				} else {
					newContent += lines.get(i);
					log.add(new ApiLog(TransformerLogStatusEnum.UNCHANGED, lines.get(i),
							new ApiLogKeyValue("propertyKey", lineKey),
							new ApiLogKeyValue("value", lineValue)
							));
				}
			} else {
				newContent += lines.get(i);
				log.add(new ApiLog(TransformerLogStatusEnum.IGNORED, lines.get(i)
						));
			}
			newContent += "\n";
		}
		result = newContent;
		return usedCache;
	}

}

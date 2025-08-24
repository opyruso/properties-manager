package com.opyruso.propertiesmanager.transformers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.microprofile.jwt.JsonWebToken;

import com.opyruso.propertiesmanager.api.entity.ApiLog;
import com.opyruso.propertiesmanager.constants.OperationTypeEnum;
import com.opyruso.propertiesmanager.constants.TransformerLogStatusEnum;
import com.opyruso.propertiesmanager.data.entity.PropertyValue;
import com.opyruso.propertiesmanager.utils.FileUtils;

public abstract class BaseSearchAndReplaceTransformerFactory extends BaseTransformerFactory implements ITransformerFactory {

	@Inject
	protected JsonWebToken jwt;

	@Override
	public List<String> transform() throws Exception {
		String content = FileUtils.getFileContent(file);
		List<String> usedCache = new ArrayList<String>();
		String newContent = content;
		for (PropertyValue prop : databaseValues.values()) {
			if (!prop.getPk().getFilename().equals(getFilename())) continue;
			if (prop.getOperationType().equals(OperationTypeEnum.DEL)) {
				log.add(new ApiLog(TransformerLogStatusEnum.IGNORED, prop.getPk().getPropertyKey() + " was not replaced with " + secureValue(prop)));
				continue;
			}
			if (newContent.indexOf(prop.getPk().getPropertyKey()) > -1) {
				newContent = newContent.replace(prop.getPk().getPropertyKey(), prop.getNewValue());
				log.add(new ApiLog(TransformerLogStatusEnum.valueOf("UPDATED_" + prop.getStatus()), prop.getPk().getPropertyKey() + " was replaced with " + secureValue(prop)
						));
			} else {
				log.add(new ApiLog(TransformerLogStatusEnum.NOT_FOUND, prop.getPk().getPropertyKey() + " was not replaced with " + secureValue(prop)
						));
			}
		}
		result = newContent;
		return usedCache;
	}

}

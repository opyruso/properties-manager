package com.opyruso.propertiesmanager.api.entity;

import java.util.HashMap;
import java.util.Map;

import com.opyruso.propertiesmanager.constants.TransformerLogStatusEnum;

public class ApiLog {

	public TransformerLogStatusEnum status;
	public String comment;
	public Map<String, String> data = new HashMap<String, String>();
	
	public ApiLog() {
	}
	
	public ApiLog(TransformerLogStatusEnum status, String comment) {
		this.status = status;
		this.comment = comment;
	}
	
	public ApiLog(TransformerLogStatusEnum status, String comment, ApiLogKeyValue... datas) {
		this.status = status;
		this.comment = comment;
		for (ApiLogKeyValue kv : datas) {
			data.put(kv.key, kv.value);
		}
	}

}

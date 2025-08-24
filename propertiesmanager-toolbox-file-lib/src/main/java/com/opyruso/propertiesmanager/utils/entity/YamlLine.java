package com.opyruso.propertiesmanager.utils.entity;

public class YamlLine {

	public String path;

	public String spaces;
	public boolean isArrayItem;
	public String key;
	public String optionnal;
	public String mandatorySpace;
	public String value;

	public String getLine() {
		return spaces + (isArrayItem ? "- " + key : key + optionnal + mandatorySpace + value);
	}

	public String getLineWithoutValue() {
		return spaces + (isArrayItem ? "- " + key : key + optionnal + mandatorySpace);
	}

}

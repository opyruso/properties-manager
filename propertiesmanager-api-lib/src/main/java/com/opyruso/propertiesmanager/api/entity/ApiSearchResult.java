package com.opyruso.propertiesmanager.api.entity;

import java.sql.Timestamp;

/**
 * Search result for property values.
 */
public class ApiSearchResult {
    public String appId;
    public String appLabel;
    public String productOwner;
    public String numVersion;
    public String envId;
    public Timestamp deployDate;
    public String propertyKey;
    public String value;
    public boolean isProtected;
}


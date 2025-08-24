package com.opyruso.propertiesmanager.services;

import java.util.List;

import jakarta.ws.rs.WebApplicationException;

import com.opyruso.propertiesmanager.api.entity.ApiUserDetails;

public interface IAdminService {

        List<ApiUserDetails> getUsers(String filter) throws WebApplicationException;

}

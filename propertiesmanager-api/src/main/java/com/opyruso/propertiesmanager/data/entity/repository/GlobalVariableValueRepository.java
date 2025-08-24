package com.opyruso.propertiesmanager.data.entity.repository;

import jakarta.enterprise.context.ApplicationScoped;

import com.opyruso.propertiesmanager.data.entity.GlobalVariableValue;

import io.quarkus.arc.DefaultBean;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@DefaultBean
@ApplicationScoped
public class GlobalVariableValueRepository implements PanacheRepository<GlobalVariableValue> {

}

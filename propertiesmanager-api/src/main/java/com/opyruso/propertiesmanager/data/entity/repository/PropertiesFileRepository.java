package com.opyruso.propertiesmanager.data.entity.repository;

import javax.enterprise.context.ApplicationScoped;

import com.opyruso.propertiesmanager.data.entity.PropertiesFile;

import io.quarkus.arc.DefaultBean;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@DefaultBean
@ApplicationScoped
public class PropertiesFileRepository implements PanacheRepository<PropertiesFile> {

}

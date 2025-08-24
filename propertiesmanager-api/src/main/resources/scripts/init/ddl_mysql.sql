-- propertiesmanager_db.applications definition

CREATE TABLE `applications` (
  `app_id` varchar(255) NOT NULL,
  `app_label` varchar(255) NOT NULL,
  `app_product_owner` varchar(255) NOT NULL,
  `creation_date` datetime(6) NOT NULL,
  `update_date` datetime(6) NOT NULL,
  PRIMARY KEY (`app_id`),
  UNIQUE KEY `UK5nxpp9cmkb7srxvrminrvygoy` (`app_label`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- propertiesmanager_db.user_right_demand definition

CREATE TABLE `user_right_demand` (
  `app_id` varchar(255) NOT NULL,
  `env_id` varchar(255) NOT NULL,
  `level` varchar(255) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `creation_date` datetime(6) NOT NULL,
  `update_date` datetime(6) NOT NULL,
  PRIMARY KEY (`app_id`,`env_id`,`level`,`user_id`,`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- propertiesmanager_db.application_version definition

CREATE TABLE `application_version` (
  `app_id` varchar(255) NOT NULL,
  `num_version` varchar(255) NOT NULL,
  `creation_date` datetime(6) NOT NULL,
  `update_date` datetime(6) NOT NULL,
  PRIMARY KEY (`app_id`,`num_version`),
  CONSTRAINT `application_version_FK` FOREIGN KEY (`app_id`) REFERENCES `applications` (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- propertiesmanager_db.installed_version definition

CREATE TABLE `installed_version` (
  `app_id` varchar(255) NOT NULL,
  `env_id` varchar(255) NOT NULL,
  `num_version` varchar(255) NOT NULL,
  `creation_date` datetime(6) NOT NULL,
  `update_date` datetime(6) NOT NULL,
  PRIMARY KEY (`app_id`,`env_id`,`num_version`),
  KEY `installed_version_FK` (`app_id`,`num_version`),
  CONSTRAINT `installed_version_FK` FOREIGN KEY (`app_id`, `num_version`) REFERENCES `application_version` (`app_id`, `num_version`),
  CONSTRAINT `installed_version_FK_1` FOREIGN KEY (`app_id`) REFERENCES `applications` (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- propertiesmanager_db.property definition

CREATE TABLE `property` (
  `app_id` varchar(255) NOT NULL,
  `num_version` varchar(255) NOT NULL,
  `filename` varchar(255) NOT NULL,
  `property_key` varchar(255) NOT NULL,
  `property_type` varchar(255) NOT NULL,
  `creation_date` datetime(6) NOT NULL,
  `update_date` datetime(6) NOT NULL,
  PRIMARY KEY (`app_id`,`num_version`,`filename`,`property_key`),
  CONSTRAINT `property_FK` FOREIGN KEY (`app_id`, `num_version`) REFERENCES `application_version` (`app_id`, `num_version`),
  CONSTRAINT `property_FK_1` FOREIGN KEY (`app_id`) REFERENCES `applications` (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- propertiesmanager_db.property_value definition

CREATE TABLE `property_value` (
  `app_id` varchar(255) NOT NULL,
  `env_id` varchar(255) NOT NULL,
  `num_version` varchar(255) NOT NULL,
  `filename` varchar(255) NOT NULL,
  `property_key` varchar(255) NOT NULL,
  `new_value` varchar(255) NOT NULL,
  `is_protected` tinyint(1) NOT NULL,
  `operation_type` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL,
  `creation_date` datetime(6) NOT NULL,
  `update_date` datetime(6) NOT NULL,
  PRIMARY KEY (`app_id`,`env_id`,`num_version`,`filename`,`property_key`),
  KEY `property_value_FK` (`app_id`,`num_version`,`filename`,`property_key`),
  CONSTRAINT `property_value_FK` FOREIGN KEY (`app_id`, `num_version`, `filename`, `property_key`) REFERENCES `property` (`app_id`, `num_version`, `filename`, `property_key`),
  CONSTRAINT `property_value_FK_1` FOREIGN KEY (`app_id`) REFERENCES `applications` (`app_id`),
  CONSTRAINT `property_value_FK_2` FOREIGN KEY (`app_id`, `num_version`) REFERENCES `application_version` (`app_id`, `num_version`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
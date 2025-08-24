-- propertiesmanager_db.properties_file definition

CREATE TABLE `properties_file` (
  `app_id` varchar(255) NOT NULL,
  `num_version` varchar(255) NOT NULL,
  `filename` varchar(255) NOT NULL,
  `content` longtext NOT NULL,
  `creation_date` datetime(6) NOT NULL,
  `update_date` datetime(6) NOT NULL,
  PRIMARY KEY (`app_id`,`filename`,`num_version`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

insert into properties_file (app_id, num_version, filename, content, creation_date, update_date)
	(select distinct p.app_id, p.num_version, p.filename, 'Tm8gQ29udGVudA==', current_timestamp, current_timestamp from property p);

-- propertiesmanager_db.globalvariable definition

CREATE TABLE `globalvariable` (
  `globalvariable_key` varchar(255) NOT NULL,
  `creation_date` datetime(6) NOT NULL,
  `update_date` datetime(6) NOT NULL,
  PRIMARY KEY (`globalvariable_key`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- propertiesmanager_db.globalvariable_value definition

CREATE TABLE `globalvariable_value` (
  `globalvariable_key` varchar(255) NOT NULL,
  `env_id` varchar(255) NOT NULL,
  `creation_date` datetime(6) NOT NULL,
  `new_value` varchar(255) NOT NULL,
  `is_protected` bit(1) NOT NULL,
  `update_date` datetime(6) NOT NULL,
  PRIMARY KEY (`env_id`,`globalvariable_key`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
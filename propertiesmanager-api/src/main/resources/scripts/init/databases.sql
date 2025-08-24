DROP DATABASE propertiesmanager_db;

CREATE DATABASE propertiesmanager_db;
CREATE USER 'propertiesmanager_usr' IDENTIFIED BY 'Passw0rd!';
GRANT ALL privileges ON `propertiesmanager_db`.* TO 'propertiesmanager_usr'@'%';
FLUSH PRIVILEGES;



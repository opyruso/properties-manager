CREATE DATABASE propertiesmanager_dev CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'propertiesmanager_dev'@'%' IDENTIFIED BY 'propertiesmanager_dev_password';
GRANT ALL PRIVILEGES ON propertiesmanager_dev.* TO 'propertiesmanager_dev'@'%';
FLUSH PRIVILEGES;

CREATE DATABASE propertiesmanager_rec CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'propertiesmanager_rec'@'%' IDENTIFIED BY 'propertiesmanager_rec_password';
GRANT ALL PRIVILEGES ON propertiesmanager_rec.* TO 'propertiesmanager_rec'@'%';
FLUSH PRIVILEGES;

CREATE DATABASE propertiesmanager_pro CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'propertiesmanager_pro'@'%' IDENTIFIED BY 'propertiesmanager_pro_password';
GRANT ALL PRIVILEGES ON propertiesmanager_pro.* TO 'propertiesmanager_pro'@'%';
FLUSH PRIVILEGES;

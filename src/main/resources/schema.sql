CREATE TABLE IF NOT EXISTS CITY (  
`City_code` INT AUTO_INCREMENT  PRIMARY KEY,  
`city_name` VARCHAR(50) NOT NULL,  
`city_pincode` INT(8) NOT NULL
);

-- DROP TABLE IF EXISTS api_package_classes;
-- DROP TABLE IF EXISTS api_package_interfaces;
-- DROP TABLE IF EXISTS api_package;

CREATE TABLE IF NOT EXISTS api_package (
`id` INT AUTO_INCREMENT PRIMARY KEY,
`package_name` VARCHAR(100) NOT NULL UNIQUE,
`reference` VARCHAR(200) NOT NULL,
`description` VARCHAR(2000),
`version_added` INT NOT NULL,
`framework` VARCHAR (20) NOT NULL
);

CREATE TABLE IF NOT EXISTS api_package_interfaces (
`id` INT AUTO_INCREMENT PRIMARY KEY,
`name` VARCHAR(100) NOT NULL,
`reference` VARCHAR(200) NOT NULL,
`description` VARCHAR(2000),
`version_added` INT NOT NULL,
`api_package_id` INT,
foreign key (`api_package_id`) references api_package(`id`)
);

CREATE TABLE IF NOT EXISTS api_package_classes (
`id` INT AUTO_INCREMENT PRIMARY KEY,
`name` VARCHAR(100) NOT NULL,
`reference` VARCHAR(200) NOT NULL,
`description` VARCHAR(2000),
`version_added` INT NOT NULL,
`api_package_id` INT,
foreign key (`api_package_id`) references api_package(`id`)
);
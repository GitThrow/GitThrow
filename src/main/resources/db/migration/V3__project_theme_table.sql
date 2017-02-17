CREATE TABLE project_theme(
  project_id VARCHAR(32) PRIMARY KEY,
  opacity DOUBLE NOT NULL ,
  image LONGBLOB NOT NULL ,
  updated_at DATETIME NOT NULL,
   CONSTRAINT `project_theme_project_fk` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`)
);

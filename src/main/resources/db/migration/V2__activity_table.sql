CREATE TABLE activities(
  `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
  project VARCHAR(32) NOT NULL,
  `user` VARCHAR(32) NOT NULL ,
  title VARCHAR(128) NOT NULL,
  description TEXT NOT NULL ,
  created_at DATETIME NOT NULL,
  CONSTRAINT `activities_project_fk` FOREIGN KEY (`project`) REFERENCES `projects` (`project_id`),
  CONSTRAINT `activities_user_fk` FOREIGN KEY (`user`) REFERENCES `users` (`id`),
  INDEX activities_project_idx(project),
  INDEX activities_user_idx(`user`)
);

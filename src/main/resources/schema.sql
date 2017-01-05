/* ユーザー */
CREATE TABLE IF NOT EXISTS `users` (
  `id`       VARCHAR(32) PRIMARY KEY,
  `name`     VARCHAR(16)  NOT NULL,
  `password` VARCHAR(128) NOT NULL,
  `email`    VARCHAR(128) NOT NULL,
  `admin`    BOOL         NOT NULL
);

CREATE TABLE IF NOT EXISTS `user_avatar` (
  `id`     VARCHAR(32) PRIMARY KEY,
  `avatar` LONGBLOB NOT NULL,
  CONSTRAINT `user_avatar_id_fk` FOREIGN KEY (`id`) REFERENCES `users` (`id`)
);

CREATE TABLE IF NOT EXISTS `user_setting` (
  `id`       VARCHAR(32) PRIMARY KEY,
  `gravatar` BOOL NOT NULL,
  CONSTRAINT `user_setting_id_fk` FOREIGN KEY (`id`) REFERENCES `users` (`id`)
);

/* プロジェクト */
CREATE TABLE IF NOT EXISTS `projects`
(
  `project_id`   VARCHAR(32) PRIMARY KEY,
  `project_name` VARCHAR(16) NOT NULL,
  `owner`        VARCHAR(32) NOT NULL,
  CONSTRAINT `projects_owner_fk` FOREIGN KEY (`owner`) REFERENCES `users` (`id`)
);

/* プロジェクトの詳細情報 */
CREATE TABLE IF NOT EXISTS `project_profiles`
(
  `project_id`  VARCHAR(32) PRIMARY KEY,
  `description` TEXT NOT NULL, /* TEXTにデフォルト値を設定できないので、コード側で対応 */
  CONSTRAINT `project_profiles_project_fk` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`)
);

CREATE TABLE IF NOT EXISTS `project_user` (
  `user`    VARCHAR(32),
  `project` VARCHAR(32),
  CONSTRAINT `project_user_project_fk` FOREIGN KEY (`project`) REFERENCES `projects` (`project_id`),
  CONSTRAINT `project_user_user_fk` FOREIGN KEY (`user`) REFERENCES `users` (`id`),
  PRIMARY KEY (`user`, `project`)
);

/* チケットのタイプ */
CREATE TABLE IF NOT EXISTS `ticket_type` (
  `id`      INT AUTO_INCREMENT PRIMARY KEY,
  `project` VARCHAR(32) NOT NULL,
  `type`    VARCHAR(32) NOT NULL,
  CONSTRAINT `ticket_type_project_fk` FOREIGN KEY (`project`) REFERENCES `projects` (`project_id`),
  UNIQUE (`project`, `type`)
);

/* チケットの状態 */
CREATE TABLE IF NOT EXISTS `ticket_status` (
  `id`      INT PRIMARY KEY AUTO_INCREMENT,
  `project` VARCHAR(32) NOT NULL,
  `status`  VARCHAR(32) NOT NULL,
  CONSTRAINT `ticket_status_project_fk` FOREIGN KEY (`project`) REFERENCES `projects` (`project_id`),
  UNIQUE (`project`, `status`)
);

/* チケットの優先度 */
CREATE TABLE IF NOT EXISTS `ticket_priority` (
  `id`       INT PRIMARY KEY AUTO_INCREMENT,
  `project`  VARCHAR(32) NOT NULL,
  `priority` VARCHAR(32) NOT NULL,
  CONSTRAINT `ticket_priority_project_fk` FOREIGN KEY (`project`) REFERENCES `projects` (`project_id`),
  UNIQUE (`project`, `priority`)
);

/* オープンしているチケット */
CREATE TABLE IF NOT EXISTS `open_tickets` (
  `project`     VARCHAR(32)  NOT NULL,
  `ticket_num`  INT          NOT NULL, /* チケット番号 */
  `title`       VARCHAR(128) NOT NULL,
  `description` TEXT         NOT NULL, /* TEXTにデフォルト値を設定できないので、コード側で対応 */
  `assignee`    VARCHAR(32), /* 担当者 */
  `proponent`   VARCHAR(32)  NOT NULL, /* 提案者 */
  `limit`       DATETIME, /* 期日 */
  `type`        INT          NOT NULL, /*タイプ*/
  `status`      INT          NOT NULL, /*状態*/
  `priority`    INT          NOT NULL, /*優先度*/
  CONSTRAINT `open_tickets_project_fk` FOREIGN KEY (`project`) REFERENCES `projects` (`project_id`),
  CONSTRAINT `open_tickets_assignee_fk` FOREIGN KEY (`assignee`) REFERENCES `users` (`id`),
  CONSTRAINT `open_tickets_proponent_fk` FOREIGN KEY (`proponent`) REFERENCES `users` (`id`),
  CONSTRAINT `open_tickets_type_fk` FOREIGN KEY (`type`) REFERENCES `ticket_type` (`id`),
  CONSTRAINT `open_tickets_status_fk` FOREIGN KEY (`status`) REFERENCES `ticket_status` (`id`),
  CONSTRAINT `open_tickets_priority_fk` FOREIGN KEY (`priority`) REFERENCES `ticket_priority` (`id`),
  PRIMARY KEY (`project`, `ticket_num`)
);

/* チケットのコメント */
CREATE TABLE IF NOT EXISTS `ticket_comment` (
  `id`         INT PRIMARY KEY AUTO_INCREMENT,
  `project`    VARCHAR(32) NOT NULL,
  `ticket_num` INT         NOT NULL,
  `text`       TEXT        NOT NULL,
  `created_at` DATETIME    NOT NULL,
  `user`       VARCHAR(32) NOT NULL,
  CONSTRAINT `ticket_comment_project_fk` FOREIGN KEY (`project`) REFERENCES `projects` (`project_id`),
  CONSTRAINT `ticket_comment_user_fk` FOREIGN KEY (`user`) REFERENCES `users` (`id`),
  INDEX ticket_comment_project(project),
  INDEX ticket_comment_ticket_num(ticket_num)
);

/* チケットの個数を数えるためのテーブル */
CREATE TABLE IF NOT EXISTS `ticket_number` (
  `project`      VARCHAR(32) PRIMARY KEY,
  `ticket_count` INT NOT NULL DEFAULT 0, /* プロジェクトに存在するチケットの個数 */
  CONSTRAINT `ticket_number_project_fk` FOREIGN KEY (`project`) REFERENCES `projects` (`project_id`)
);

CREATE TABLE IF NOT EXISTS `pr_number` (
  `project`      VARCHAR(32) PRIMARY KEY,
  `pr_count` INT NOT NULL DEFAULT 0, /* プロジェクトに存在するチケットの個数 */
  CONSTRAINT `pr_number_project_fk` FOREIGN KEY (`project`) REFERENCES `projects` (`project_id`)
);

/*開いているプルリクエスト*/
CREATE TABLE IF NOT EXISTS `open_pull_request` (
  `project`       VARCHAR(32)  NOT NULL,
  `pr_num`        INT          NOT NULL,
  `title`         VARCHAR(128) NOT NULL,
  `description`   TEXT         NOT NULL, /* TEXTにデフォルト値を設定できないので、コード側で対応 */
  `reviewer`      VARCHAR(32)  NOT NULL,
  `proponent`     VARCHAR(32)  NOT NULL,
  `base_branch`   VARCHAR(256) NOT NULL,
  `target_branch` VARCHAR(256) NOT NULL,
  CONSTRAINT `open_pull_request_project_fk` FOREIGN KEY (`project`) REFERENCES `projects` (`project_id`),
  PRIMARY KEY (`project`, `pr_num`)
);

/*閉じているプルリクエスト*/
CREATE TABLE IF NOT EXISTS `closed_pull_request` (
  `project`       VARCHAR(32)  NOT NULL,
  `pr_num`        INT          NOT NULL,
  `title`         VARCHAR(128) NOT NULL,
  `description`   TEXT         NOT NULL, /* TEXTにデフォルト値を設定できないので、コード側で対応 */
  `reviewer`      VARCHAR(32)  NOT NULL,
  `proponent`     VARCHAR(32)  NOT NULL,
  `base_commit`   VARCHAR(256) NOT NULL,
  `target_commit` VARCHAR(256) NOT NULL,
  CONSTRAINT `closed_pull_request_project_fk` FOREIGN KEY (`project`) REFERENCES `projects` (`project_id`),
  PRIMARY KEY (`project`, `pr_num`)
);

/* 成果物 */
CREATE TABLE IF NOT EXISTS `artifactTable` (
  `id`        INT AUTO_INCREMENT PRIMARY KEY,
  `project`   VARCHAR(32)  NOT NULL,
  `file_name` VARCHAR(512) NOT NULL,
  CONSTRAINT `artifact_project_fk` FOREIGN KEY (`project`) REFERENCES `projects` (`project_id`),
  INDEX artifact_project(project)
);

CREATE TABLE IF NOT EXISTS `test_log` (
  `id`         INT AUTO_INCREMENT PRIMARY KEY,
  `project`    VARCHAR(32)  NOT NULL,
  `log`        TEXT         NOT NULL,
  `created_at` DATETIME     NOT NULL,
  `status`     VARCHAR(128) NOT NULL,
  `commit`     VARCHAR(128) NOT NULL,
  CONSTRAINT `test_log_project_fk` FOREIGN KEY (`project`) REFERENCES `projects` (`project_id`),
  INDEX test_log_project(project)
);

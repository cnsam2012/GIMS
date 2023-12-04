DROP TABLE IF EXISTS `departments`;

CREATE TABLE `departments` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `type` int(11) DEFAULT NULL COMMENT '1-Academic_Department-院系部门; 2-companies-实习单位',
    `name` VARCHAR(255),
    `content` text,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 810001 DEFAULT CHARSET = utf8;


DROP TABLE IF EXISTS `user`;

SET
    character_set_client = utf8mb4;

CREATE TABLE `user` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `username` varchar(50) DEFAULT NULL,
    `password` varchar(50) DEFAULT NULL,
    `salt` varchar(50) DEFAULT NULL,
    `email` varchar(100) DEFAULT NULL,
    `phone` varchar(100) DEFAULT NULL,
    `type` int(11) DEFAULT NULL COMMENT '1-students-学生; 2-instructors-指导老师; 3-companies-实习单位; 9-admin-院系管理员; 99-superuser-超级用户',
    `status` int(11) DEFAULT NULL COMMENT '0-未激活; 1-已激活;',
    `activation_code` varchar(100) DEFAULT NULL,
    `header_url` varchar(200) DEFAULT NULL,
    `create_time` timestamp NULL DEFAULT NULL,
    `wechat_open_id` varchar(100) null default null,
    `department_id` INT NOT NULL,
    -- FOREIGN KEY (`department_id`) REFERENCES departments(`department_id`),
    PRIMARY KEY (`id`),
    KEY `index_username` (`username`(20)),
    KEY `index_email` (`email`(20))
) ENGINE = InnoDB AUTO_INCREMENT = 910001 DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `report`;

SET
    character_set_client = utf8mb4;

CREATE TABLE `report` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `user_id` int(11) NOT NULL,
    `title` varchar(100) NOT NULL,
    `content` text,
    `type` int(11) DEFAULT NULL COMMENT '1-周记; 2-月记; 3-总结',
    `status` int(11) DEFAULT NULL COMMENT '0-正常; 1-精华; 2-拉黑;',
    `create_time` timestamp NULL DEFAULT NULL,
    `comment` text DEFAULT NULL,
    `score` double DEFAULT NULL,
    `is_deleted` int(1) DEFAULT 0,
    -- FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
    PRIMARY KEY (`id`),
    KEY `index_user_id` (`user_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 110001 DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `message`;

SET
    character_set_client = utf8mb4;

CREATE TABLE `message` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `from_id` int(11) DEFAULT NULL,
    `to_id` int(11) DEFAULT NULL,
    `conversation_id` varchar(45) NOT NULL,
    `content` text,
    `status` int(11) DEFAULT NULL COMMENT '0-未读; 1-已读; 2-删除;',
    `create_time` timestamp NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `index_from_id` (`from_id`),
    KEY `index_to_id` (`to_id`),
    KEY `index_conversation_id` (`conversation_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 210001 DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `comment`;

SET
    character_set_client = utf8mb4;

CREATE TABLE `comment` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `user_id` int(11) DEFAULT NULL,
    `entity_type` int(11) DEFAULT NULL comment '1-周记; 2-月记; 3-总结; 4-学生; 5-指导老师; 6-实习单位;',
    `entity_id` int(11) DEFAULT NULL,
    `target_id` int(11) DEFAULT NULL,
    `content` text,
    `status` int(11) DEFAULT NULL,
    `create_time` timestamp NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `index_user_id` (`user_id`),
    KEY `index_entity_id` (`entity_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 310001 DEFAULT CHARSET = utf8;
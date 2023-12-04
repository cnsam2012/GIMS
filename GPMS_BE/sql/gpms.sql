/*
 Navicat Premium Data Transfer

 Source Server         : ubd
 Source Server Type    : MySQL
 Source Server Version : 80032 (8.0.32)
 Source Host           : ubd:3306
 Source Schema         : gpms

 Target Server Type    : MySQL
 Target Server Version : 80032 (8.0.32)
 File Encoding         : 65001

 Date: 13/11/2023 19:46:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL,
  `entity_type` int NULL DEFAULT NULL COMMENT '1-周记; 2-月记; 3-总结; 4-学生; 5-指导老师; 6-实习单位;',
  `entity_id` int NULL DEFAULT NULL,
  `target_id` int NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL,
  `status` int NULL DEFAULT NULL COMMENT '0-未读; 1-已读; 2-删除;',
  `create_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_user_id`(`user_id` ASC) USING BTREE,
  INDEX `index_entity_id`(`entity_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 310001 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment
-- ----------------------------

-- ----------------------------
-- Table structure for departments
-- ----------------------------
DROP TABLE IF EXISTS `departments`;
CREATE TABLE `departments`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` int NULL DEFAULT NULL COMMENT '1-Academic_Department-院系部门; 2-companies-实习单位',
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 810001 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of departments
-- ----------------------------
INSERT INTO `departments` VALUES (810000, 1, 'superuser', '管理用户、管理员等');

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `from_id` int NULL DEFAULT NULL,
  `to_id` int NULL DEFAULT NULL,
  `conversation_id` varchar(45) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL,
  `status` int NULL DEFAULT NULL COMMENT '0-未读; 1-已读; 2-删除;',
  `create_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_from_id`(`from_id` ASC) USING BTREE,
  INDEX `index_to_id`(`to_id` ASC) USING BTREE,
  INDEX `index_conversation_id`(`conversation_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 210001 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message
-- ----------------------------

-- ----------------------------
-- Table structure for report
-- ----------------------------
DROP TABLE IF EXISTS `report`;
CREATE TABLE `report`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `title` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL,
  `type` int NULL DEFAULT NULL COMMENT '1-周记; 2-月记; 3-总结',
  `status` int NULL DEFAULT NULL COMMENT '0-正常; 1-精华; 2-拉黑;',
  `create_time` timestamp NULL DEFAULT NULL,
  `comment` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL,
  `score` double NULL DEFAULT NULL,
  `is_deleted` int NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 110001 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of report
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `password` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `salt` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `email` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `phone` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `type` int NULL DEFAULT NULL COMMENT '1-students-学生; 2-instructors-指导老师; 3-companies-实习单位; 9-admin-院系管理员; 99-superuser-超级用户',
  `status` int NULL DEFAULT NULL COMMENT '0-未激活; 1-已激活;',
  `activation_code` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `header_url` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `wechat_open_id` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `department_id` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_username`(`username`(20) ASC) USING BTREE,
  INDEX `index_email`(`email`(20) ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 910004 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (910000, '1', 'SYSTEM', 'SYSTEM', 'SYSTEM', 'system@banbang.com', 0, 1, NULL, 'http://static.nowcoder.com/images/head/notify.png', '2020-01-13 02:11:03', NULL, 810000);
INSERT INTO `user` VALUES (910002, '2', 'admin', 'd2d5a85cc78dc580aaa8d5d0dafa0a79', '0c7b1', 'admin@banbang.com', 9, 1, '671f3fdb898e455997aec3d2bcb695a6', 'http://qnvxyvq1p.hd-bkt.clouddn.com/e531e57c933b4c71912fe5396b1881f0', '2021-02-10 22:27:00', NULL, 810000);
INSERT INTO `user` VALUES (910003, '3', 'master', '852e951a7398e9853feef981c22e15e7', '3d196', 'master@banbang.com', 9, 1, '3563733cece74ee4b61b86b0b375987c', 'http://qnvxyvq1p.hd-bkt.clouddn.com/71616bd74fd9418b8b27a67b059ee9e3', '2021-02-10 22:29:03', NULL, 810000);

SET FOREIGN_KEY_CHECKS = 1;

/*
 Navicat Premium Data Transfer

 Source Server         : acemake
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : localhost:3306
 Source Schema         : websocket

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 21/12/2020 19:51:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for friend_relation
-- ----------------------------
DROP TABLE IF EXISTS `friend_relation`;
CREATE TABLE `friend_relation`  (
  `user_id` int(11) NOT NULL,
  `friend_id` int(11) NOT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`, `friend_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of friend_relation
-- ----------------------------
INSERT INTO `friend_relation` VALUES (1, 2, '2020-12-20 10:21:49');
INSERT INTO `friend_relation` VALUES (1, 3, '2020-12-20 10:22:07');
INSERT INTO `friend_relation` VALUES (1, 5, '2020-12-20 10:22:22');
INSERT INTO `friend_relation` VALUES (2, 3, '2020-12-20 10:22:44');
INSERT INTO `friend_relation` VALUES (2, 4, '2020-12-20 10:23:01');
INSERT INTO `friend_relation` VALUES (2, 5, '2020-12-20 10:23:40');
INSERT INTO `friend_relation` VALUES (4, 1, '2020-12-20 12:05:16');

-- ----------------------------
-- Table structure for room
-- ----------------------------
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room`  (
  `room_id` int(11) NOT NULL,
  `room_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of room
-- ----------------------------
INSERT INTO `room` VALUES (1, '亚索专用群');
INSERT INTO `room` VALUES (2, '凯皇群');

-- ----------------------------
-- Table structure for room_user
-- ----------------------------
DROP TABLE IF EXISTS `room_user`;
CREATE TABLE `room_user`  (
  `room_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `type` tinyint(4) NOT NULL COMMENT '1.群主 2 管理员 3群成员',
  PRIMARY KEY (`room_id`, `user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of room_user
-- ----------------------------
INSERT INTO `room_user` VALUES (1, 1, 1);
INSERT INTO `room_user` VALUES (1, 2, 2);
INSERT INTO `room_user` VALUES (1, 3, 3);
INSERT INTO `room_user` VALUES (1, 4, 3);
INSERT INTO `room_user` VALUES (1, 5, 3);
INSERT INTO `room_user` VALUES (2, 1, 3);
INSERT INTO `room_user` VALUES (2, 5, 1);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `pwd` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'yasuo', '123456', '2020-12-20 10:16:47');
INSERT INTO `user` VALUES (2, '盖伦', '123456', '2020-12-20 10:17:46');
INSERT INTO `user` VALUES (3, '劲夫', '123456', '2020-12-20 10:18:29');
INSERT INTO `user` VALUES (4, '拉克丝', '123456', '2020-12-20 10:19:13');
INSERT INTO `user` VALUES (5, '凯隐', '123456', '2020-12-20 10:19:56');

SET FOREIGN_KEY_CHECKS = 1;

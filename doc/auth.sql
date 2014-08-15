/*
Navicat MySQL Data Transfer

Source Server         : local_db
Source Server Version : 50018
Source Host           : localhost:3306
Source Database       : auth

Target Server Type    : MYSQL
Target Server Version : 50018
File Encoding         : 65001

Date: 2013-07-24 09:37:18
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `auth_menu`
-- ----------------------------
DROP TABLE IF EXISTS `auth_menu`;
CREATE TABLE `auth_menu` (
  `MENU_ID` varchar(50) NOT NULL default '' COMMENT '记录号',
  `MENU_PARENT_ID` varchar(50) default NULL COMMENT '父菜单',
  `RES_ID` varchar(50) default NULL COMMENT '关联资源',
  `MENU_NAME` varchar(20) NOT NULL COMMENT '菜单名称',
  `MENU_DESC` varchar(100) default NULL COMMENT '菜单描述',
  `TREE_LEVEL` int(11) default NULL COMMENT '菜单级别',
  `IS_LEAF` tinyint(4) default NULL COMMENT '是否叶子节点',
  `MENU_CSS` varchar(20) default NULL COMMENT '菜单样式',
  `CREATE_USER_ID` varchar(50) default NULL COMMENT '创建人',
  `CREATE_TIME` datetime default NULL COMMENT '创建时间',
  PRIMARY KEY  (`MENU_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of auth_menu
-- ----------------------------
INSERT INTO `auth_menu` VALUES ('M01', null, 'R01', '权限管理', '', '1', '0', 'qxgl', null, null);
INSERT INTO `auth_menu` VALUES ('M0101', 'M01', 'R0102', '菜单管理', '', '2', '1', null, null, null);
INSERT INTO `auth_menu` VALUES ('M0102', 'M01', 'R0101', '资源管理', '', '2', '1', null, null, null);
INSERT INTO `auth_menu` VALUES ('M0103', 'M01', 'R0103', '用户管理', '', '2', '1', null, null, null);
INSERT INTO `auth_menu` VALUES ('M0104', 'M01', 'R0104', '角色管理', '', '2', '1', null, null, null);
INSERT INTO `auth_menu` VALUES ('M02', null, 'R02', '个人资料', '', '1', '0', 'grzl', null, null);
INSERT INTO `auth_menu` VALUES ('M0201', 'M02', 'R0201', '用户信息', '', '2', '1', null, null, null);
INSERT INTO `auth_menu` VALUES ('M0202', 'M02', 'R0202', '修改密码', '', '2', '1', null, null, null);
INSERT INTO `auth_menu` VALUES ('M03', null, 'R03', '商品管理', '', '1', '0', 'spgl', null, null);
INSERT INTO `auth_menu` VALUES ('M0301', 'M03', 'R0301', '商品管理', '', '2', '1', '', null, null);
INSERT INTO `auth_menu` VALUES ('M0302', 'M03', 'R0302', '商品分类管理', '', '2', '1', '', null, null);
INSERT INTO `auth_menu` VALUES ('M0303', 'M03', 'R0303', '我的商品', '', '2', '1', '', null, null);
INSERT INTO `auth_menu` VALUES ('M0304', 'M03', 'R0201', 'fdasaaaaaaa', 'fdsaf', '2', '0', 'fdfsa', null, null);
INSERT INTO `auth_menu` VALUES ('M030401', 'M0304', 'R03', 'fdsa', 'fdsafasf', '3', '1', 'fdsa', null, null);

-- ----------------------------
-- Table structure for `auth_resource`
-- ----------------------------
DROP TABLE IF EXISTS `auth_resource`;
CREATE TABLE `auth_resource` (
  `RES_ID` varchar(50) NOT NULL default '' COMMENT '记录号',
  `RES_PARENT_ID` varchar(50) default NULL COMMENT '父资源',
  `RES_NAME` varchar(50) NOT NULL COMMENT '资源名称',
  `RES_URL` varchar(100) NOT NULL COMMENT '资源路径',
  `RES_DESC` varchar(100) default NULL COMMENT '资源描述',
  `TREE_LEVEL` int(2) default NULL COMMENT '资源级别',
  `IS_LEAF` int(11) default NULL COMMENT '是否叶子节点',
  `CREATE_USER_ID` varchar(50) default NULL COMMENT '创建人',
  `CREATE_TIME` datetime default NULL COMMENT '创建时间',
  PRIMARY KEY  (`RES_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of auth_resource
-- ----------------------------
INSERT INTO `auth_resource` VALUES ('R01', null, '权限管理', '1', '', '1', '0', null, null);
INSERT INTO `auth_resource` VALUES ('R0101', 'R01', '资源管理', 'view/auth/resource/resource_list.jsp', '', '2', '1', null, null);
INSERT INTO `auth_resource` VALUES ('R0102', 'R01', '菜单管理', 'view/auth/menu/menu_list.jsp', '', '2', '1', null, null);
INSERT INTO `auth_resource` VALUES ('R0103', 'R01', '用户管理', 'view/auth/user/user_list.jsp', '', '2', '1', null, null);
INSERT INTO `auth_resource` VALUES ('R0104', 'R01', '角色管理', 'view/auth/role/role_list.jsp', '', '2', '1', null, null);
INSERT INTO `auth_resource` VALUES ('R02', null, '个人资料', '1', '', '1', '0', null, null);
INSERT INTO `auth_resource` VALUES ('R0201', 'R02', '用户信息', 'MemberController.do?method=toMemberCenterView', '', '2', '1', null, null);
INSERT INTO `auth_resource` VALUES ('R0202', 'R02', '修改密码', 'view/auth/member/member_update_password.jsp', '', '2', '1', null, null);
INSERT INTO `auth_resource` VALUES ('R05', null, '非菜单资源', '1', '', '1', '0', null, null);
INSERT INTO `auth_resource` VALUES ('R0501', 'R05', '管理中心页面', 'view/auth/sys/desktop/index.jsp', '', '2', '1', null, null);
INSERT INTO `auth_resource` VALUES ('R0502', 'R05', '管理中心', 'MemberController.do?method=toManageCenterView', '', '2', '1', null, null);

-- ----------------------------
-- Table structure for `auth_role`
-- ----------------------------
DROP TABLE IF EXISTS `auth_role`;
CREATE TABLE `auth_role` (
  `ROLE_ID` varchar(50) NOT NULL COMMENT '记录号',
  `ROLE_NAME` varchar(50) NOT NULL COMMENT '角色名称',
  `ROLE_CH_NAME` varchar(50) default NULL COMMENT '角色中文名称',
  `ROLE_DESC` varchar(100) default NULL COMMENT '角色描述',
  `CREATE_USER_ID` varchar(50) default NULL COMMENT '创建人',
  `CREATE_TIME` datetime default NULL COMMENT '创建时间',
  PRIMARY KEY  (`ROLE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of auth_role
-- ----------------------------
INSERT INTO `auth_role` VALUES ('402883c9400e400601400e4a1c5d0000', 'fdsaf', 'fdsaff', 'sadfasfsaffdsfafaf', null, '2013-07-24 09:27:52');
INSERT INTO `auth_role` VALUES ('role01', 'admin', '超级管理员', '', null, null);
INSERT INTO `auth_role` VALUES ('role02', 'default', '默认角色', null, null, null);

-- ----------------------------
-- Table structure for `auth_role_resource`
-- ----------------------------
DROP TABLE IF EXISTS `auth_role_resource`;
CREATE TABLE `auth_role_resource` (
  `ROLE_ID` varchar(50) NOT NULL default '' COMMENT '角色id',
  `RES_ID` varchar(50) NOT NULL default '' COMMENT '资源id',
  PRIMARY KEY  (`ROLE_ID`,`RES_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk ROW_FORMAT=COMPACT COMMENT='角色资源中间表';

-- ----------------------------
-- Records of auth_role_resource
-- ----------------------------
INSERT INTO `auth_role_resource` VALUES ('2c93a0c238b2ad660138b2cf5fc90001', 'R0101');
INSERT INTO `auth_role_resource` VALUES ('2c93a0c238b2ad660138b2cf5fc90001', 'R0102');
INSERT INTO `auth_role_resource` VALUES ('2c93a0c238b2ad660138b2cf5fc90001', 'R0103');
INSERT INTO `auth_role_resource` VALUES ('2c93a0c238b2ad660138b2cf5fc90001', 'R02');
INSERT INTO `auth_role_resource` VALUES ('2c93a0c238b2ad660138b2cf5fc90001', 'R0201');
INSERT INTO `auth_role_resource` VALUES ('2c93a0c238b2ad660138b2cf5fc90001', 'R0202');
INSERT INTO `auth_role_resource` VALUES ('2c93a0c238b2ad660138b2cf5fc90001', 'R03');
INSERT INTO `auth_role_resource` VALUES ('2c93a0c238b2ad660138b2cf5fc90001', 'R0301');
INSERT INTO `auth_role_resource` VALUES ('2c93a0c238b2ad660138b2cf5fc90001', 'R0303');
INSERT INTO `auth_role_resource` VALUES ('role01', 'R01');
INSERT INTO `auth_role_resource` VALUES ('role01', 'R0101');
INSERT INTO `auth_role_resource` VALUES ('role01', 'R0102');
INSERT INTO `auth_role_resource` VALUES ('role01', 'R0103');
INSERT INTO `auth_role_resource` VALUES ('role01', 'R0104');
INSERT INTO `auth_role_resource` VALUES ('role01', 'R03');
INSERT INTO `auth_role_resource` VALUES ('role01', 'R0301');
INSERT INTO `auth_role_resource` VALUES ('role01', 'R0302');
INSERT INTO `auth_role_resource` VALUES ('role01', 'R04');
INSERT INTO `auth_role_resource` VALUES ('role01', 'R0401');
INSERT INTO `auth_role_resource` VALUES ('role01', 'R0402');
INSERT INTO `auth_role_resource` VALUES ('role01', 'R0403');
INSERT INTO `auth_role_resource` VALUES ('role01', 'R0502');
INSERT INTO `auth_role_resource` VALUES ('role02', 'R0101');
INSERT INTO `auth_role_resource` VALUES ('role02', 'R0103');
INSERT INTO `auth_role_resource` VALUES ('role02', 'R0104');
INSERT INTO `auth_role_resource` VALUES ('role02', 'R0201');
INSERT INTO `auth_role_resource` VALUES ('role02', 'R0301');
INSERT INTO `auth_role_resource` VALUES ('role02', 'R0501');
INSERT INTO `auth_role_resource` VALUES ('role02', 'R0502');

-- ----------------------------
-- Table structure for `auth_user`
-- ----------------------------
DROP TABLE IF EXISTS `auth_user`;
CREATE TABLE `auth_user` (
  `USER_ID` varchar(50) NOT NULL COMMENT '记录号',
  `USER_NAME` varchar(20) NOT NULL COMMENT '用户名',
  `USER_PASSWORD` varchar(50) default NULL COMMENT '密码',
  `USER_REAL_NAME` varchar(20) default NULL COMMENT '真实姓名',
  `user_email` varchar(20) default NULL,
  `USER_SEX` char(1) default NULL COMMENT '性别',
  `USER_AGE` int(3) default NULL COMMENT '年龄',
  `USER_PHOTO` varchar(50) default NULL COMMENT '照片',
  `USER_IS_ADMIN` char(1) default NULL,
  `USE_STATUS` char(2) default NULL COMMENT '使用状态',
  `CREATE_TIME` datetime default NULL COMMENT '创建时间',
  PRIMARY KEY  (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of auth_user
-- ----------------------------
INSERT INTO `auth_user` VALUES ('2c93a0a93aba8ea5013aba8f11c30000', 'test02', '1', null, 'fdsaf@11.com', null, null, null, null, null, '2012-11-01 14:01:09');
INSERT INTO `auth_user` VALUES ('2c93a0c238b2ad660138b2ae0be40000', 'likg', '1', null, 'likg.java@163.com', null, null, null, null, null, '2012-07-23 15:12:27');
INSERT INTO `auth_user` VALUES ('user01', 'admin', '1', null, '1171602588@qq.com', null, null, null, '1', null, '2012-06-28 06:05:37');

-- ----------------------------
-- Table structure for `auth_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `auth_user_role`;
CREATE TABLE `auth_user_role` (
  `USER_ID` varchar(50) NOT NULL COMMENT '用户id',
  `ROLE_ID` varchar(50) NOT NULL COMMENT '角色id',
  PRIMARY KEY  (`USER_ID`,`ROLE_ID`),
  KEY `fk_auth_user_role_roleid` (`ROLE_ID`),
  CONSTRAINT `fk_auth_user_role_roleid` FOREIGN KEY (`ROLE_ID`) REFERENCES `auth_role` (`ROLE_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=gbk ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of auth_user_role
-- ----------------------------
INSERT INTO `auth_user_role` VALUES ('user01', 'role01');

-- ----------------------------
-- Table structure for `base_attachment`
-- ----------------------------
DROP TABLE IF EXISTS `base_attachment`;
CREATE TABLE `base_attachment` (
  `ATT_ID` varchar(50) NOT NULL default '' COMMENT '主键',
  `FILE_NAME` varchar(200) default NULL COMMENT '文件名',
  `FILE_SIZE` int(11) default NULL COMMENT '文件大小',
  `FILE_TYPE` varchar(10) default NULL COMMENT '文件类型',
  `FILE_DESC` varchar(500) default NULL COMMENT '文件描述',
  `PATH` varchar(200) default NULL COMMENT '路径',
  `UPLOAD_IP` varchar(50) default NULL COMMENT '上传IP',
  `CREATE_USER_ID` varchar(50) default NULL COMMENT '创建人',
  `CREATE_TIME` datetime default NULL COMMENT '创建时间',
  PRIMARY KEY  (`ATT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of base_attachment
-- ----------------------------
INSERT INTO `base_attachment` VALUES ('2c93a0a93b06b77e013b06b91e0d0000', '22222.jpg', '8449', 'pic', null, 'view/resource/upload/img/3e82372f-6f8a-47f2-a2df-34ac29c46f44.jpg', '0:0:0:0:0:0:0:1', 'user01', '2012-11-16 08:58:13');
INSERT INTO `base_attachment` VALUES ('2c93a0a93b06b77e013b06bb45f60001', '250元.jpg', '38406', 'pic', null, 'view/resource/upload/img/5c9c0b74-1108-4637-aa0a-e32ad64c45b6.jpg', '0:0:0:0:0:0:0:1', 'user01', '2012-11-16 09:00:34');
INSERT INTO `base_attachment` VALUES ('2c93a0a93b06b77e013b06bcb6750002', '22222.jpg', '8449', 'pic', null, 'view/resource/upload/img/b6572d3d-c917-4651-9249-567ed66c8d17.jpg', '0:0:0:0:0:0:0:1', 'user01', '2012-11-16 09:02:09');
INSERT INTO `base_attachment` VALUES ('2c93a0a93b06b77e013b06be69800003', '静.jpg', '81398', 'pic', null, 'view/resource/upload/img/afa0d327-39af-4475-bfe1-b67eb9a2a373.jpg', '0:0:0:0:0:0:0:1', 'user01', '2012-11-16 09:04:00');
INSERT INTO `base_attachment` VALUES ('2c93a0a93b06b77e013b06c00ef20004', '3333.txt', '1125', 'pic', null, 'view/resource/upload/img/ab0aae81-6d4c-4119-b22e-1c44064a23e0.txt', '0:0:0:0:0:0:0:1', 'user01', '2012-11-16 09:05:48');
INSERT INTO `base_attachment` VALUES ('2c93a0a93b06b77e013b06c266c10005', '723ed04795a41805510ffed2.jpg', '56114', 'pic', null, 'view/resource/upload/img/f28e90a1-19f7-4a36-88b8-04f25e0d8ca7.jpg', '0:0:0:0:0:0:0:1', 'user01', '2012-11-16 09:08:21');
INSERT INTO `base_attachment` VALUES ('2c93a0a93b06b77e013b06cb4d470006', 'g.gif', '15724', 'pic', null, 'view/resource/upload/img/1b63835f-947d-418b-9936-632ea424287e.gif', '0:0:0:0:0:0:0:1', 'user01', '2012-11-16 09:18:05');
INSERT INTO `base_attachment` VALUES ('2c93a0a93b072da1013b073294b60000', '11.gif', '1959929', 'picture', null, 'view/resource/upload/img/b7e47069-f17d-4e66-9d45-332681f360a6.gif', '0:0:0:0:0:0:0:1', 'user01', '2012-11-16 11:10:53');
INSERT INTO `base_attachment` VALUES ('2c93a0a93b07801d013b0784078a0000', '甘苦人生2.jpg', '28280', 'picture', null, 'view/resource/upload/img/588d2c1c-f35d-483a-b01e-7653b503599b.jpg', '0:0:0:0:0:0:0:1', 'user01', '2012-11-16 12:39:51');
INSERT INTO `base_attachment` VALUES ('2c93a0c23946e717013947248c7a0000', 'java.jpg', '1872', 'pic', null, 'lucene/fa5eab7f-36fe-4779-8347-b5df23266f3a.jpg', '0:0:0:0:0:0:0:1', '2c93a0c238b2ad660138b2ae0be40000', '2012-08-21 11:05:41');
INSERT INTO `base_attachment` VALUES ('2c93a0c239479d470139479ecb9e0000', '250元.jpg', '38406', 'pic', null, 'view/resource/upload/img/31b75dee-3670-4ea2-a9a1-d21fc2a06629.jpg', '0:0:0:0:0:0:0:1', '2c93a0c238b2ad660138b2ae0be40000', '2012-08-21 13:19:12');
INSERT INTO `base_attachment` VALUES ('2c93a0c239479d47013947a017880002', 'java.jpg', '1872', 'pic', null, 'view/resource/upload/img/03bbb282-8de2-4316-bd75-bbb5cdd62e2f.jpg', '0:0:0:0:0:0:0:1', '2c93a0c238b2ad660138b2ae0be40000', '2012-08-21 13:20:37');
INSERT INTO `base_attachment` VALUES ('2c93a0c2394822b501394823ecc00000', '250元.jpg', '38406', 'pic', null, 'view/resource/upload/img/aa8150fa-a45c-45b7-ae8c-66bb7dc35599.jpg', '0:0:0:0:0:0:0:1', '2c93a0c238b2ad660138b2ae0be40000', '2012-08-21 15:44:37');
INSERT INTO `base_attachment` VALUES ('2c93a0c23951186f0139511eab640000', '22222.jpg', '8449', 'pic', null, 'view/resource/upload/img/1acb4870-6b2c-4c9f-9625-697f39276656.jpg', '0:0:0:0:0:0:0:1', '2c93a0c238b2ad660138b2ae0be40000', '2012-08-23 09:35:28');
INSERT INTO `base_attachment` VALUES ('2c93a0c23951186f0139512ce6d90001', 'java.jpg', '1872', 'pic', null, 'view/resource/upload/img/bbc82c82-a2c4-432f-baae-152b05b9c5dd.jpg', '0:0:0:0:0:0:0:1', '2c93a0c238b2ad660138b2ae0be40000', '2012-08-23 09:51:00');
INSERT INTO `base_attachment` VALUES ('2c93a0c23951186f013951308b6a0002', '250元.jpg', '38406', 'pic', null, 'view/resource/upload/img/95fa1855-c431-406d-ab83-5921bf02306a.jpg', '0:0:0:0:0:0:0:1', '2c93a0c238b2ad660138b2ae0be40000', '2012-08-23 09:54:59');
INSERT INTO `base_attachment` VALUES ('2c93a0c23951186f013951310db90003', '22222.jpg', '8449', 'pic', null, 'view/resource/upload/img/fd33bc28-f8a4-4020-aac0-43ea71a3b964.jpg', '0:0:0:0:0:0:0:1', '2c93a0c238b2ad660138b2ae0be40000', '2012-08-23 09:55:32');
INSERT INTO `base_attachment` VALUES ('2c93a0c23951186f01395132cef90004', '1_renpinghao.jpg', '4849', 'pic', null, 'view/resource/upload/img/ce407467-e76f-4426-a5c4-8432ee06414a.jpg', '0:0:0:0:0:0:0:1', '2c93a0c238b2ad660138b2ae0be40000', '2012-08-23 09:57:27');
INSERT INTO `base_attachment` VALUES ('2c93a0c23951186f0139513342b90005', '250元.jpg', '38406', 'pic', null, 'view/resource/upload/img/7ae16cd3-ba9a-4e09-8951-ee296c064343.jpg', '0:0:0:0:0:0:0:1', '2c93a0c238b2ad660138b2ae0be40000', '2012-08-23 09:57:57');
INSERT INTO `base_attachment` VALUES ('2c93a0c239514e8d0139514fdeeb0000', '22222.jpg', '8449', 'pic', null, 'view/resource/upload/img/e9e2d197-a3d2-48ff-8a91-d70f38ba36ef.jpg', '0:0:0:0:0:0:0:1', '2c93a0c238b2ad660138b2ae0be40000', '2012-08-23 10:29:12');
INSERT INTO `base_attachment` VALUES ('2c93a0c239514e8d01395152c1a30001', '250元.jpg', '38406', 'pic', null, 'view/resource/upload/img/bee55e00-b042-4e64-90b1-92bc1f710b89.jpg', '0:0:0:0:0:0:0:1', '2c93a0c238b2ad660138b2ae0be40000', '2012-08-23 10:32:21');
INSERT INTO `base_attachment` VALUES ('2c93a0c239514e8d01395154a38f0002', '22222.jpg', '8449', 'pic', null, 'view/resource/upload/img/2155d266-83bf-4702-8afb-c4da196ab447.jpg', '0:0:0:0:0:0:0:1', '2c93a0c238b2ad660138b2ae0be40000', '2012-08-23 10:34:25');
INSERT INTO `base_attachment` VALUES ('2c93a0c239514e8d013951554d8f0004', 'm_12186094145021412.gif', '12617', 'pic', null, 'view/resource/upload/img/1e2da496-41b9-4773-857c-3bedd5d3a15a.gif', '0:0:0:0:0:0:0:1', '2c93a0c238b2ad660138b2ae0be40000', '2012-08-23 10:35:08');
INSERT INTO `base_attachment` VALUES ('ff80808139ec1fd20139ec2516ce0000', 'Hydrangeas.jpg', '595284', 'pic', null, 'view/resource/upload/img/dfe4fabb-3cc5-436b-a6b9-882dcd161d1c.jpg', '0:0:0:0:0:0:0:1', '2c93a0c238b2ad660138b2ae0be40000', '2012-09-22 12:03:37');
INSERT INTO `base_attachment` VALUES ('ff80808139ec27560139ec297be20000', 'Penguins.jpg', '777835', 'pic', null, 'view/resource/upload/img/703f552a-ff86-445d-a02d-4bfe5c0e4c74.jpg', '0:0:0:0:0:0:0:1', '2c93a0c238b2ad660138b2ae0be40000', '2012-09-22 12:08:25');

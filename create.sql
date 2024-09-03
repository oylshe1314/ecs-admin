# create database ecs_admin;
#
# use ecs_admin;
#
# create user 'ecs_admin'@'%' identified by 'f4$Fb1;*5/qR4%Xj)A@T';
#
# grant create, select, insert, update, delete on ecs_admin.* to 'ecs_admin'@'%' with grant option;

CREATE TABLE Menu
(
    id         BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    type       INT          NOT NULL comment '类型: 1.目录, 2.菜单, 3.接口',
    parentId   BIGINT       NOT NULL COMMENT '父级ID',
    name       VARCHAR(32)  NOT NULL COMMENT '名称',
    icon       VARCHAR(128) NOT NULL DEFAULT '' comment '图标',
    path       VARCHAR(255) NOT NULL COMMENT '路径',
    sortBy     INT          NOT NULL COMMENT '排序',
    state      INT          NOT NULL COMMENT '状态, 0: 禁用, 1: 启用',
    remark     TEXT         NOT NULL COMMENT '备注',
    createBy   VARCHAR(32)  NOT NULL COMMENT '操作人员',
    createTime DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateBy   VARCHAR(32)  NOT NULL DEFAULT '' COMMENT '操作人员',
    updateTime DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = INNODB,
  CHARSET = UTF8MB4, COMMENT '角色菜单表';

INSERT INTO Menu (id, type, parentId, name, icon, path, sortBy, state, remark, createBy)
VALUES (1, 1, 0, '菜单管理', 'Menu', '', 2001, 1, '菜单管理', 'admin'),
       (2, 2, 1, '菜单列表', 'Menu', '/menu/index', 1, 1, '菜单列表', 'admin'),
       (3, 3, 2, '菜单查询', '', '/menu/query', 1, 1, '菜单查询', 'admin'),
       (4, 3, 2, '菜单添加', '', '/menu/add', 2, 1, '菜单添加', 'admin'),
       (5, 3, 2, '菜单修改', '', '/menu/update', 3, 1, '菜单修改', 'admin'),
       (6, 3, 2, '菜单状态更改', '', '/menu/change/state', 4, 1, '菜单状态更改', 'admin'),
       (7, 3, 2, '菜单删除', '', '/menu/delete', 5, 1, '菜单删除', 'admin'),
       (8, 1, 0, '管理员', 'Avatar', '', 2002, 1, '管理员', 'admin'),
       (9, 2, 8, '角色列表', 'Avatar', '/admin/role/index', 1, 1, '角色列表', 'admin'),
       (10, 3, 9, '角色查询', '', '/admin/role/query', 1, 1, '角色查询', 'admin'),
       (11, 3, 9, '角色添加', '', '/admin/role/add', 2, 1, '角色添加', 'admin'),
       (12, 3, 9, '角色修改', '', '/admin/role/update', 3, 1, '角色修改', 'admin'),
       (13, 3, 9, '角色状态更改', '', '/admin/role/change/state', 4, 1, '角色状态更改', 'admin'),
       (14, 3, 9, '角色删除', '', '/admin/role/delete', 5, 1, '角色删除', 'admin'),
       (15, 3, 9, '角色菜单查询', '', '/admin/role/menu/query', 6, 1, '角色菜单查询', 'admin'),
       (16, 3, 9, '角色菜单设置', '', '/admin/role/menu/set', 7, 1, '角色菜单设置', 'admin'),
       (17, 2, 8, '管理员列表', 'UserFilled', '/admin/index', 2, 1, '管理员列表', 'admin'),
       (18, 3, 17, '管理员查询', '', '/admin/query', 1, 1, '管理员查询', 'admin'),
       (19, 3, 17, '管理员添加', '', '/admin/add', 2, 1, '管理员添加', 'admin'),
       (20, 3, 17, '管理员修改', '', '/admin/update', 3, 1, '管理员修改', 'admin'),
       (21, 3, 17, '管理员状态更改', '', '/admin/change/state', 6, 1, '管理员状态更改', 'admin'),
       (22, 3, 17, '管理员删除', '', '/admin/delete', 7, 1, '管理员删除', 'admin'),
       (23, 1, 0, '设置', 'Setting', '', 2003, 1, '设置', 'admin'),
       (24, 2, 23, '详细信息', 'User', '/setting/detail', 1, 1, '详细信息', 'admin'),
       (25, 3, 24, '修改信息', '', '/setting/change/detail', 1, 1, '更新信息', 'admin'),
       (26, 2, 23, '修改密码', 'Lock', '/setting/change/password', 2, 1, '修改密码', 'admin'),
       (27, 3, 26, '修改密码', '', '/setting/change/Password', 2, 1, '更新信息', 'admin');

DROP TABLE IF EXISTS Role;
CREATE TABLE Role
(
    id         BIGINT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(32) NOT NULL COMMENT '名称',
    state      INT         NOT NULL COMMENT '状态, 0: 禁用, 1: 启用',
    remark     TEXT        NOT NULL COMMENT '备注',
    createBy   VARCHAR(32) NOT NULL COMMENT '操作人员',
    createTime DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateBy   VARCHAR(32) NOT NULL DEFAULT '' COMMENT '操作人员',
    updateTime DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = INNODB,
  CHARSET = UTF8MB4, COMMENT '管理员角色表';

INSERT INTO Role (id, name, state, remark, createBy)
VALUES (1, '管理员', 1, '管理员', 'system');

DROP TABLE IF EXISTS RoleMenuRelation;
CREATE TABLE RoleMenuRelation
(
    id     BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    roleId BIGINT NOT NULL COMMENT '角色ID',
    menuId BIGINT NOT NULL COMMENT '菜单ID',
    UNIQUE KEY (roleId, menuId)
) ENGINE = INNODB,
  CHARSET = UTF8MB4, COMMENT '角色菜单表';

INSERT INTO RoleMenuRelation(roleId, menuId)
SELECT 1, id
FROM Menu;

DROP TABLE IF EXISTS Admin;
CREATE TABLE Admin
(
    id         BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    roleId     BIGINT       NOT NULL COMMENT '角色ID',
    username   VARCHAR(32)  NOT NULL COMMENT '用户名',
    password   VARCHAR(128) NOT NULL COMMENT '密码',
    nickname   VARCHAR(32)  NOT NULL COMMENT '昵称',
    avatar     varchar(255) NOT NULL COMMENT '头像',
    email      VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '邮箱',
    mobile     VARCHAR(32)  NOT NULL DEFAULT '' COMMENT '手机号',
    state      INT          NOT NULL COMMENT '状态, 0: 禁用, 1: 启用',
    remark     TEXT         NOT NULL COMMENT '备注',
    createBy   VARCHAR(32)  NOT NULL COMMENT '操作人员',
    createTime DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateBy   VARCHAR(32)  NOT NULL DEFAULT '' COMMENT '操作人员',
    updateTime DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = INNODB,
  CHARSET = UTF8MB4, COMMENT '管理员表';

INSERT INTO Admin (id, roleId, username, password, nickname, avatar, email, mobile, state, remark, createBy)
VALUES (1, 1, 'admin', '$100801$AqNWEzEt1eS0fGRh8XhEag==$n68WTs9Nv5wB/WCzB0IIpTfn7wKfd+LcxqxOTV4l16o=', '管理员', 'avatar7.png', '', '', 1, '管理员', 'system');

DROP TABLE IF EXISTS ServerHost;
CREATE TABLE ServerHost
(
    id         BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(32)  NOT NULL COMMENT '主机名称',
    host       VARCHAR(20)  NOT NULL COMMENT 'SSH地址',
    port       INT          NOT NULL COMMENT 'SSH端口',
    dir        VARCHAR(255) NOT NULL COMMENT '部署目录',
    user       VARCHAR(32)  NOT NULL COMMENT 'SSH用户',
    password   VARCHAR(32)  NOT NULL COMMENT '用户密码',
    `key`      VARCHAR(255) NOT NULL COMMENT 'SSH PEM KEY',
    state      INT          NOT NULL COMMENT '状态, 0: 禁用, 1: 启用',
    remark     TEXT         NOT NULL COMMENT '备注',
    createBy   VARCHAR(32)  NOT NULL COMMENT '操作人员',
    createTime DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateBy   VARCHAR(32)  NOT NULL DEFAULT '' COMMENT '操作人员',
    updateTime DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = INNODB,
  CHARSET = UTF8MB4, COMMENT '管理员表';

DROP TABLE IF EXISTS ServerConfig;
CREATE TABLE ServerConfig
(
    id         BIGINT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    appName    VARCHAR(32) NOT NULL COMMENT '服务名称',
    appId      INT         NOT NULL COMMENT '服务ID',
    hostId     BIGINT      NOT NULL COMMENT '主机ID',
    content    TEXT        NOT NULL COMMENT '配置内容',
    state      INT         NOT NULL COMMENT '状态, 0: 禁用, 1: 启用',
    remark     TEXT        NOT NULL COMMENT '备注',
    createBy   VARCHAR(32) NOT NULL COMMENT '操作人员',
    createTime DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateBy   VARCHAR(32) NOT NULL DEFAULT '' COMMENT '操作人员',
    updateTime DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = INNODB,
  CHARSET = UTF8MB4, COMMENT '管理员表';
create table `domain_config` (
	id int(11) not null auto_increment comment 'ID主键',
	scheme varchar(16) not null comment '协议，如http',
	domain_name varchar(256) not null comment '域名，逻辑判断唯一',
	port varchar(16) default null comment '端口号，非必填',
	-- TODO 域名级别的校验,认证配置
	remark varchar(256) default null comment '备注',
	is_delete bit(1) not null default b'0' comment '是否删除',
	create_time timestamp not null default current_timestamp comment '创建时间',
	update_time timestamp not null default current_timestamp on update current_timestamp comment '更新时间',
	primary key (`id`)
) engine=InnoDB auto_increment=1 default charset=utf8mb4 comment='接口域名表';

create table `path_config` (
	id int(11) not null auto_increment comment 'ID主键',
	path varchar(256) not null comment '接口请求路径',
	-- TODO 入参处理,出参处理
	remark varchar(256) default null comment '备注',
	is_delete bit(1) not null default b'0' COMMENT '是否删除',
	create_time timestamp not null default current_timestamp comment '创建时间',
	update_time timestamp not null default current_timestamp on update current_timestamp comment '更新时间',
	primary key (`id`)
) engine=InnoDB auto_increment=1 default charset=utf8mb4 comment='接口路径表';

create table `domain_path_combination` (
	id int(11) not null auto_increment comment 'ID主键',
	path_id int(11) not null comment '接口路径ID，关联path_config.id',
	domain_id int(11) not null comment '域名ID，关联domain_config.id',
	remark varchar(256) default null comment '备注',
	is_delete bit(1) not null default b'0' COMMENT '是否删除',
	create_time timestamp not null default current_timestamp comment '创建时间',
	update_time timestamp not null default current_timestamp on update current_timestamp comment '更新时间',
	primary key (`id`)
) engine=InnoDB auto_increment=1 default charset=utf8mb4 comment='接口域名与路径组合表';

create table `api_log` (
	id int(11) not null auto_increment comment 'ID主键',
	combination_id int(11) not null comment 'domain_path_combination.id',
	trace_id varchar(128) not null comment 'trace_id',
    span_id varchar(128) not null comment 'span_id',
	full_path varchar(2048) default null comment '请求路径',
	headers varchar(2048) default null comment '请求headers',
	body text default null comment '请求体',
	response text default null comment '响应数据',
	create_time timestamp not null default current_timestamp comment '创建时间',
	update_time timestamp not null default current_timestamp on update current_timestamp comment '更新时间',
	primary key (`id`),
	key `comb_id` (`combination_id`)
) engine=InnoDB auto_increment=1 default charset=utf8mb4 comment='接口请求日志表';
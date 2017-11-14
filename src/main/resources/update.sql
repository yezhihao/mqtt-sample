-- 累计值
CREATE TABLE mc_accumulated (
    device_id INT(10) NOT NULL COMMENT '设备ID',
    pulse_num INT(10) COMMENT '脉冲数量',
    work_time INT(10) COMMENT '工作时间(分钟)',
    amount INT(10) COMMENT '总金额',
    update_time DATETIME COMMENT '更新时间',
    create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (device_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 当前值
CREATE TABLE mc_current (
    id INT(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    device_id INT(10) COMMENT '设备ID',
    pulse_num INT(10) COMMENT '脉冲数量',
    count_down INT(10) COMMENT '剩余时间(秒钟)',
    create_time DATETIME COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 系统参数
CREATE TABLE mc_setting (
    device_id INT(10) NOT NULL COMMENT '设备ID',
    rate_pulse INT(10) COMMENT '费率脉冲',
    rate_minute INT(10) COMMENT '费率分钟',
    date_time DATETIME COMMENT '时间日期',
    reward_model VARCHAR(100) COMMENT '奖励模式',
    other_setting VARCHAR(100) COMMENT '其他设置',
    error_status VARCHAR(100) COMMENT '错误状态',
    terminal_firmware_version VARCHAR(15) COMMENT '终端固件版本',
    update_time DATETIME COMMENT '更新时间',
    create_time DATETIME COMMENT '创建时间',
    PRIMARY KEY (device_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 事件
CREATE TABLE mc_event (
    id INT(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    device_id INT(10) COMMENT '设备ID',
    event VARCHAR(64) COMMENT '事件',
    create_time DATETIME COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

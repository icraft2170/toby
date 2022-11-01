CREATE TABLE `users`
(
    `id`  varchar(10) NOT NULL COMMENT '유저ID',
    `name` varchar(20) NOT NULL COMMENT '유저 이름',
    `password`  varchar(20) NOT NULL COMMENT '비밀번호',
    `level`  INT NOT NULL COMMENT '비밀번호',
    `login`  INT NOT NULL COMMENT '비밀번호',
    `recommend`  INT NOT NULL COMMENT '비밀번호',
    `email`  varchar(20) NOT NULL COMMENT '이메일',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='유저';

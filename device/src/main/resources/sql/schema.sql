drop table if exists message;
drop table if exists device;
drop table if exists user;

create table `user` (
                        `id` int not null auto_increment,
                        `email` varchar(63) not null  ,
                        `username` varchar(63) not null,
                        `password` varchar(63) not null,

                        primary key (`id`),
                        unique (`email`,`username`)
) engine=innodb charset=utf8mb4;

create table `device` (
                        `id` int not null auto_increment,
                        `userId` int not null,
                        `name` varchar(63) not null,
                        `type` varchar(63) not null,
                        primary key (`id`),
                        unique (`name`),
                        foreign key (`userid`) references  `user`(`id`) on delete cascade on update cascade
) engine=innodb charset=utf8mb4;

create table `message` (
                          `deviceId` int not null,
                          `clientId` varchar(10) not null,
                          `info` varchar(63) not null,
                          `value` int not null,
                          `alert` int not null ,
                          `lng` double precision not null,
                          `lat` double precision not null,
                          `timestamp` bigint not null,
                          `time` varchar(63) not null,

                          primary key (`deviceId`, `timestamp`),
                          foreign key (`deviceId`) references `device`(`id`) on delete cascade on update cascade
) engine=innodb charset=utf8mb4;
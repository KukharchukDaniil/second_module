CREATE TABLE `tag`
(
    `id`   int unsigned NOT NULL AUTO_INCREMENT,
    `name` varchar(45)  NOT NULL,
    PRIMARY KEY (`id`)
) AUTO_INCREMENT = 1;
CREATE TABLE `gift_certificate`
(
    `id`               int unsigned NOT NULL AUTO_INCREMENT,
    `name`             varchar(45)  DEFAULT NULL,
    `description`      varchar(180) DEFAULT NULL,
    `price`            int          DEFAULT NULL,
    `duration`         int          DEFAULT NULL,
    `create_date`      varchar(45)  DEFAULT NULL,
    `last_update_date` varchar(45)  DEFAULT NULL,
    PRIMARY KEY (`id`)
) AUTO_INCREMENT = 1;
CREATE TABLE `certificate_tag`
(
    `tag_id`         int unsigned NOT NULL,
    `certificate_id` int unsigned NOT NULL,
    PRIMARY KEY (`tag_id`, `certificate_id`),
    KEY `certificate_idx` (`certificate_id`),
    CONSTRAINT `certificate` FOREIGN KEY (`certificate_id`) REFERENCES `gift_certificate` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT `tag` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) DEFAULT CHARSET = utf8;

INSERT INTO gift_certificate(name, description, price, duration, create_date, last_update_date)
values ('certificate_one', 'b_description', 1, 1, '2021-08-27T01:06:56.817', '2021-08-27T01:06:56.817');
INSERT INTO gift_certificate(name, description, price, duration, create_date, last_update_date)
values ('certificate_two', 'a_description', 1, 1, '2021-08-27T01:06:56.817', '2021-08-27T01:06:56.817');
INSERT INTO gift_certificate(name, description, price, duration, create_date, last_update_date)
values ('certificate_three', 'c_description', 1, 1, '2021-08-27T01:06:56.817', '2021-08-27T01:06:56.817');

INSERT INTO tag(NAME)
VALUES ('free');
INSERT INTO tag(NAME)
VALUES ('pro');
INSERT INTO tag(NAME)
VALUES ('advanced');

INSERT INTO certificate_tag(tag_id, certificate_id)
VALUES (1, 1);
INSERT INTO certificate_tag(tag_id, certificate_id)
VALUES (1, 2);
INSERT INTO certificate_tag(tag_id, certificate_id)
VALUES (2, 3);
INSERT INTO certificate_tag(tag_id, certificate_id)
VALUES (2, 1);

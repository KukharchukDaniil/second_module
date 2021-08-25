INSERT INTO gift_certificate(name, description, price, duration, create_date,last_update_date)
values('certificate_one', 'b_description', 1, 1, '10.02.10', '10.02.10');
INSERT INTO gift_certificate(name, description, price, duration, create_date,last_update_date)
values('certificate_two', 'a_description', 1, 1, '10.02.10', '10.02.10');
INSERT INTO gift_certificate(name, description, price, duration, create_date,last_update_date)
values('certificate_three', 'c_description', 1, 1, '10.02.10', '10.02.10');

INSERT INTO tag(NAME) VALUES('free');
INSERT INTO tag(NAME) VALUES('pro');
INSERT INTO tag(NAME) VALUES('advanced');

INSERT certificate_tag(tag_id, certificate_id) VALUES(1,1);
INSERT certificate_tag(tag_id, certificate_id) VALUES(1,2);
INSERT certificate_tag(tag_id, certificate_id) VALUES(2,3);
INSERT certificate_tag(tag_id, certificate_id) VALUES(2,1);
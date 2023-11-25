CREATE SCHEMA IF NOT EXISTS uamed_db;
CREATE TABLE uamed_db.user (
  user_id int NOT NULL,
  username varchar(20) NOT NULL,
  password varchar(40) NOT NULL,
  firstname varchar(32) NOT NULL,
  lastname varchar(32) NOT NULL
);

INSERT INTO uamed_db.user (user_id, username, password, firstname, lastname) VALUES
(1, 'admin', 'f0e790819b045182a5fe8de176eb274e12014922', 'Admin', 'Adminov'),
(2, 'alex', '519204f4927492cb043d47b2e7e0bfa852013343', 'Alexander', 'A'),
(3, 'zheka', '05117b514ee977cd8a22d2bfa3a1c2bc7a180535', 'zheka', 'z');

ALTER TABLE uamed_db.user
  ADD PRIMARY KEY (user_id);

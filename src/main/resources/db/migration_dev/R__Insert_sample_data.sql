DELETE FROM users;

ALTER TABLE users AUTO_INCREMENT = 1;

-- password is "password" for all users
INSERT INTO users(id, username, password, image_path, enabled)
VALUES(1, 'user1', '$2a$10$tv/w2Tu0zDRfuUVmYv8cxupXUFVDfDa9qeZHI7D6FLSbgbtW2X1wu', null, true)
     ,(2, 'user2', '$2a$10$aY2.7pBbLa43eAM6THhBbejLQ05Gse0pFhAJdUh5IMofzRDUjFqXC', null, true)
     ,(3, 'user3', '$2a$10$80.pbgR30h2i94ZaN.pbHuQ2.Su/mIpCSJ8iUXqTRXGsob99r6Lri', null, true)
;

DELETE FROM articles;

ALTER TABLE articles AUTO_INCREMENT = 1;

INSERT INTO articles(title, body, user_id)
VALUES('タイトルです1', '1本文です。', 1)
     ,('タイトルです2', '2本文です。', 1)
     ,('タイトルです3', '3本文です。', 2)
;
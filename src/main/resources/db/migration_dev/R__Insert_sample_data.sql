DELETE FROM articles;

ALTER TABLE articles AUTO_INCREMENT = 1;

INSERT INTO articles(title, body)
VALUES('タイトルです1', '1本文です。')
     ,('タイトルです2', '2本文です。')
     ,('タイトルです3', '3本文です。')
;

DELETE FROM users;

-- password is "password" for all users
INSERT INTO users(username, password, enabled)
VALUES('user1', '$2a$10$95AbqrwMKrDXGUe5I1/ee.gTgrdd8yKfbeQZvXZA7PVck2biuzFwC', true)
     ,('user2', '$2a$10$Ke2AAiwaMjEGDEdudTm90.qR2fRDYgVykz2gYfqmEo.7NwFjKtqIi', true)
     ,('user3', '$2a$10$ERsOkyCWnu/7GYkbvTm0LOer3/DGghA8socx2bUGiiHmwZkKtkwey', true)
;
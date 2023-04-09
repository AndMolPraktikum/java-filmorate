
INSERT INTO genres (name) VALUES ('Комедия'); -- 1
INSERT INTO genres (name) VALUES ('Драма'); -- 2
INSERT INTO genres (name) VALUES ('Мультфильм'); -- 3
INSERT INTO genres (name) VALUES ('Триллер'); -- 4
INSERT INTO genres (name) VALUES ('Документальный'); -- 5
INSERT INTO genres (name) VALUES ('Боевик'); -- 6

INSERT INTO mpas (name) VALUES ('G'); -- 1 "кино без всяких возрастных ограничений"
INSERT INTO mpas (name) VALUES ('PG'); -- 2 "маленьким детям рекомендуется просмотр с родителями"
INSERT INTO mpas (name) VALUES ('PG-13'); -- 3 "детям до 13 просмотр не желателен"
INSERT INTO mpas (name) VALUES ('R'); -- 4 "зрители до 17 лет должны присутствовать в зале с сопровождением родителей"
INSERT INTO mpas (name) VALUES ('NC-17'); -- 5 "зрители до 18 не допускаются (в названии «17» потому что включительно)"

INSERT INTO status (name) VALUES ('Confirmed'); -- 1 "Подтверждено"
INSERT INTO status (name) VALUES ('Unconfirmed'); -- 2 "Не подтверждено"
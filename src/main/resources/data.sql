
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

INSERT INTO films (name, description, release_year, duration, mpas_id)
    VALUES ('Гарри Поттер и философский камень', 'Фильм о мальчике, который выжил', '2001-11-04', 152, 2);
INSERT INTO films (name, description, release_year, duration, mpas_id)
    VALUES ('Доктор Стрэндж', 'Фильм про самодовольного колдуна', '2016-10-13', 115, 3);
INSERT INTO films (name, description, release_year, duration, mpas_id)
    VALUES ('Стражи Галактики', 'Фильм о кучке изгоев спасающих вселенную', '2014-07-31', 121, 3);
INSERT INTO films (name, description, release_year, duration, mpas_id)
    VALUES ('Побег из Шоушенка', 'Страх - это кандалы. Надежда - это свобода', '1994-09-10', 142, 4);
INSERT INTO films (name, description, release_year, duration, mpas_id)
    VALUES ('Двое: Я и моя тень', 'Семейное кино про близняшек', '1995-11-17', 101, 2);
INSERT INTO films (name, description, release_year, duration, mpas_id)
    VALUES ('Изгой', 'Как не сойти с ума в одиночестве', '2000-12-07', 143, 3);
INSERT INTO films (name, description, release_year, duration, mpas_id)
    VALUES ('Небесный замок Лапута', 'Альтернативная реальность, соответствующая началу XX века.', '1986-08-02', 125, 2);
INSERT INTO films (name, description, release_year, duration, mpas_id)
    VALUES ('Властелин колец: Братство Кольца', 'Сказания о Средиземье', '2001-04-15', 178, 3);
INSERT INTO films (name, description, release_year, duration, mpas_id)
    VALUES ('13-й воин', 'Фильп про поход против племени Пожирателей Трупов', '1999-08-13', 102, 4);
INSERT INTO films (name, description, release_year, duration, mpas_id)
    VALUES ('Бриллиантовая рука', 'Народная комедия с элементами абсурда от Леонида Гайдая', '1969-04-28', 94, 3);

INSERT INTO users (name, login, email, birthday)
    VALUES ('Стивен Спилберг', 'stiven1946', 'stiven1946@yandex.ru', '1946-12-18');
INSERT INTO users (name, login, email, birthday)
    VALUES ('Крис Коламбус', 'kris1958', 'kris1958@yandex.ru', '1958-09-10');
INSERT INTO users (name, login, email, birthday)
    VALUES ('Джеймс Кэмерон', 'james1954', 'james1954@yandex.ru', '1954-08-16');
INSERT INTO users (name, login, email, birthday)
    VALUES ('Хаяо Миядзаки', 'hayao1941', 'hayao1941@yandex.ru', '1941-01-05');
INSERT INTO users (name, login, email, birthday)
    VALUES ('Квентин Тарантино', 'justQuentin', 'quentin1963@yandex.ru', '1963-03-27');
INSERT INTO users (name, login, email, birthday)
    VALUES ('Джордж Лукас', 'georluc', 'georluc@yandex.ru', '1944-05-14');
INSERT INTO users (name, login, email, birthday)
    VALUES ('Питер Джексон', 'peter1961', 'peter1961@yandex.ru', '1961-10-31');

INSERT INTO likes (film_id, user_id, grade) VALUES (1, 1, 1);
INSERT INTO likes (film_id, user_id, grade) VALUES (3, 1, 1);
INSERT INTO likes (film_id, user_id, grade) VALUES (5, 1, 1);
INSERT INTO likes (film_id, user_id, grade) VALUES (7, 1, 1);
INSERT INTO likes (film_id, user_id, grade) VALUES (9, 1, 1);
INSERT INTO likes (film_id, user_id, grade) VALUES (2, 2, 1);
INSERT INTO likes (film_id, user_id, grade) VALUES (4, 2, 1);
INSERT INTO likes (film_id, user_id, grade) VALUES (5, 2, 1);
INSERT INTO likes (film_id, user_id, grade) VALUES (6, 2, 1);
INSERT INTO likes (film_id, user_id, grade) VALUES (8, 2, 1);
INSERT INTO likes (film_id, user_id, grade) VALUES (10, 2, 1);
INSERT INTO likes (film_id, user_id, grade) VALUES (1, 3, 1);
INSERT INTO likes (film_id, user_id, grade) VALUES (3, 3, 1);
INSERT INTO likes (film_id, user_id, grade) VALUES (9, 3, 1);
INSERT INTO likes (film_id, user_id, grade) VALUES (2, 4, 1);
INSERT INTO likes (film_id, user_id, grade) VALUES (6, 4, 1);
INSERT INTO likes (film_id, user_id, grade) VALUES (10, 4, 1);
INSERT INTO likes (film_id, user_id, grade) VALUES (4, 5, 1);
INSERT INTO likes (film_id, user_id, grade) VALUES (8, 5, 1);
INSERT INTO likes (film_id, user_id, grade) VALUES (7, 5, 1);
INSERT INTO likes (film_id, user_id, grade) VALUES (5, 6, 1);
INSERT INTO likes (film_id, user_id, grade) VALUES (9, 6, 1);
INSERT INTO likes (film_id, user_id, grade) VALUES (1, 7, 1);
INSERT INTO likes (film_id, user_id, grade) VALUES (10, 7, 1);
INSERT INTO likes (film_id, user_id, grade) VALUES (5, 7, 1);

INSERT INTO FILM_GENRES (film_id, GENRE_ID) VALUES (1, 1);
INSERT INTO FILM_GENRES (film_id, GENRE_ID) VALUES (2, 2);
INSERT INTO FILM_GENRES (film_id, GENRE_ID) VALUES (3, 6);
INSERT INTO FILM_GENRES (film_id, GENRE_ID) VALUES (4, 4);
INSERT INTO FILM_GENRES (film_id, GENRE_ID) VALUES (5, 6);
INSERT INTO FILM_GENRES (film_id, GENRE_ID) VALUES (6, 4);
INSERT INTO FILM_GENRES (film_id, GENRE_ID) VALUES (7, 4);
INSERT INTO FILM_GENRES (film_id, GENRE_ID) VALUES (8, 1);
INSERT INTO FILM_GENRES (film_id, GENRE_ID) VALUES (9, 1);
INSERT INTO FILM_GENRES (film_id, GENRE_ID) VALUES (10, 6);


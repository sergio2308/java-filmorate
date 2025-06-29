-- Инициализация пользователей
MERGE INTO Users (user_id, email, login, name, birthday) KEY (user_id) VALUES
    (1, 'ivan@example.com', 'ivan123', 'Иван Иванов', '1985-04-12'),
    (2, 'anna@example.com', 'anna_s', 'Анна Смирнова', '1990-11-05'),
    (3, 'peter@example.com', 'peter_p', 'Пётр Петров', '1978-07-20');

-- Инициализация фильмов
MERGE INTO Films (film_id, name, description, release_date, duration, rating_id) KEY (film_id) VALUES
    (1, 'Побег из Шоушенка', 'Драма о надежде и дружбе в тюрьме', '1994-09-23', 142, 4),
    (2, 'Властелин колец: Братство Кольца', 'Фэнтези-приключение в Средиземье', '2001-12-19', 178, 3),
    (3, 'Король Лев', 'Мультфильм о взрослении львенка Симбы', '1994-06-24', 88, 1);

-- Инициализация рейтингов
MERGE INTO Rating (rating_id, name) KEY (rating_id) VALUES (1, 'G');
MERGE INTO Rating (rating_id, name) KEY (rating_id) VALUES (2, 'PG');
MERGE INTO Rating (rating_id, name) KEY (rating_id) VALUES (3, 'PG-13');
MERGE INTO Rating (rating_id, name) KEY (rating_id) VALUES (4, 'R');
MERGE INTO Rating (rating_id, name) KEY (rating_id) VALUES (5, 'NC-17');

-- Инициализация жанров
MERGE INTO Genres (genre_id, name) KEY (genre_id) VALUES (1, 'Комедия');
MERGE INTO Genres (genre_id, name) KEY (genre_id) VALUES (2, 'Драма');
MERGE INTO Genres (genre_id, name) KEY (genre_id) VALUES (3, 'Мультфильм');
MERGE INTO Genres (genre_id, name) KEY (genre_id) VALUES (4, 'Триллер');
MERGE INTO Genres (genre_id, name) KEY (genre_id) VALUES (5, 'Документальный');
MERGE INTO Genres (genre_id, name) KEY (genre_id) VALUES (6, 'Боевик');

-- Инициализация лайков
MERGE INTO Likes (user_id, film_id) KEY (user_id, film_id) VALUES
    (1, 1),
    (2, 1),
    (3, 2),
    (1, 3);

-- Инициализация друзей
MERGE INTO Friends (user_id, friend_id, status) KEY (user_id, friend_id) VALUES
  (1, 2, TRUE),
  (2, 1, TRUE),
  (1, 3, TRUE);
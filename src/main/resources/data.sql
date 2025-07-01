-- Инициализация рейтингов (Mpa) с фиксированными ID
INSERT INTO Mpa (mpa_id, name) VALUES
  (1, 'G'),
  (2, 'PG'),
  (3, 'PG-13'),
  (4, 'R'),
  (5, 'NC-17');

-- Обновляем автоинкремент для Mpa, чтобы новые записи начинались с 6
ALTER TABLE Mpa ALTER COLUMN mpa_id RESTART WITH 6;

-- Инициализация жанров с фиксированными ID
INSERT INTO Genres (genre_id, name) VALUES
  (1, 'Комедия'),
  (2, 'Драма'),
  (3, 'Мультфильм'),
  (4, 'Триллер'),
  (5, 'Документальный'),
  (6, 'Боевик');

-- Обновляем автоинкремент для Genres
ALTER TABLE Genres ALTER COLUMN genre_id RESTART WITH 7;

-- Инициализация пользователей (без указания user_id, чтобы автоинкремент сработал)
INSERT INTO Users (email, login, name, birthday) VALUES
  ('ivan@example.com', 'ivan123', 'Иван Иванов', '1985-04-12'),
  ('anna@example.com', 'anna_s', 'Анна Смирнова', '1990-11-05'),
  ('peter@example.com', 'peter_p', 'Пётр Петров', '1978-07-20');

-- Инициализация фильмов (без указания film_id)
INSERT INTO Films (name, description, release_date, duration, mpa_id) VALUES
  ('Побег из Шоушенка', 'Драма о надежде и дружбе в тюрьме', '1994-09-23', 142, 4),
  ('Властелин колец: Братство Кольца', 'Фэнтези-приключение в Средиземье', '2001-12-19', 178, 3),
  ('Король Лев', 'Мультфильм о взрослении львенка Симбы', '1994-06-24', 88, 1);

INSERT INTO Film_likes (film_id, user_id) VALUES
  (1, 1),
  (1, 2),
  (2, 3),
  (3, 1);

INSERT INTO Friends (user_id, friend_id, status) VALUES
  (1, 2, TRUE),
  (2, 1, TRUE),
  (1, 3, TRUE);
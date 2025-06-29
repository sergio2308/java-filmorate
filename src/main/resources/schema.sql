CREATE TABLE IF NOT EXISTS Users (
  user_id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR NOT NULL,
  login VARCHAR NOT NULL,
  name VARCHAR NULL,
  birthday DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS Rating (
  rating_id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR NOT NULL
);

INSERT INTO Rating (name) VALUES ('G'), ('PG'), ('PG-13'), ('R'), ('NC-17');

CREATE TABLE IF NOT EXISTS Films (
  film_id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR NOT NULL,
  description VARCHAR NOT NULL,
  release_date DATE NOT NULL,
  duration INTEGER NOT NULL,
  rating_id INTEGER NOT NULL,
  CONSTRAINT fk_films_rating FOREIGN KEY (rating_id) REFERENCES Rating(rating_id)
);

CREATE TABLE IF NOT EXISTS Genres (
  genre_id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR NOT NULL
);

INSERT INTO Genres (name) VALUES
('Комедия'), ('Драма'), ('Мультфильм'), ('Триллер'), ('Документальный'), ('Боевик');

CREATE TABLE IF NOT EXISTS Film_genre (
  film_id INTEGER NOT NULL,
  genre_id INTEGER NOT NULL,
  PRIMARY KEY (film_id, genre_id),
  CONSTRAINT fk_fg_film FOREIGN KEY (film_id) REFERENCES Films(film_id) ON DELETE CASCADE,
  CONSTRAINT fk_fg_genre FOREIGN KEY (genre_id) REFERENCES Genres(genre_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Friends (
  user_id INTEGER NOT NULL,
  friend_id INTEGER NOT NULL,
  status BOOLEAN NOT NULL,
  PRIMARY KEY (user_id, friend_id),
  CONSTRAINT fk_friends_user FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
  CONSTRAINT fk_friends_friend FOREIGN KEY (friend_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Likes (
  film_id INTEGER NOT NULL,
  user_id INTEGER NOT NULL,
  PRIMARY KEY (film_id, user_id),
  CONSTRAINT fk_likes_film FOREIGN KEY (film_id) REFERENCES Films(film_id) ON DELETE CASCADE,
  CONSTRAINT fk_likes_user FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);
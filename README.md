# java-filmorate
Template repository for Filmorate project.

Описание схемы базы данных
src/main/java/ru/yandex/practicum/filmorate/images/DBDiagram.png

Данная схема базы данных разработана для приложения Filmorate, позволяющего пользователям:
Регистрироваться и добавлять друзей
Оставлять отзывы и оценки фильмам
Просматривать информацию о фильмах и их рейтингах
Структура табли
Основные таблицы
Users - хранит информацию о пользователях
Films - содержит данные о фильмах
Rating - справочник рейтингов (MPAA)
Таблицы связей
Friends - отношения дружбы между пользователями
Likes - лайки пользователей фильмам
Genres - справочник жанров
Film_genre - связи фильмов с жанрами
Примеры SQL-запросов:
CREATE TABLE "Users" (
"user_id" integer   NOT NULL,
"email" varchar   NOT NULL,
"login" varchar   NOT NULL,
"name" varchar   NULL,
"birthday" date   NOT NULL,
CONSTRAINT "pk_Users" PRIMARY KEY (
"user_id"
)
);

CREATE TABLE "Films" (
"film_id" integer   NOT NULL,
"name" varchar   NOT NULL,
"description" varchar   NOT NULL,
"release_date" date   NOT NULL,
"duration" integer   NOT NULL,
"rating_id" integer   NOT NULL,
CONSTRAINT "pk_Films" PRIMARY KEY (
"film_id"
)
);

CREATE TABLE "Friends" (
"user_id" integer   NOT NULL,
"friend_id" integer   NOT NULL,
"status" boolean   NOT NULL
);

CREATE TABLE "Likes" (
"film_id" integer   NOT NULL,
"user_id" integer   NOT NULL
);

CREATE TABLE "Genres" (
"genre_id" integer   NOT NULL,
"name" varchar   NOT NULL,
CONSTRAINT "pk_Genres" PRIMARY KEY (
"genre_id"
)
);

CREATE TABLE "Film_genre" (
"film_id" integer   NOT NULL,
"genre_id" integer   NOT NULL,
CONSTRAINT "pk_Film_genre" PRIMARY KEY (
"genre_id"
)
);

CREATE TABLE "Rating" (
"rating_id" integer   NOT NULL,
"name" varchar   NOT NULL,
CONSTRAINT "pk_Rating" PRIMARY KEY (
"rating_id"
)
);

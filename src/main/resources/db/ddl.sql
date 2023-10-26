CREATE TABLE "user_friend"(
    "user_id" BIGINT NOT NULL,
    "friend_id" BIGINT NOT NULL,
    "is_confirmed" BOOLEAN NOT NULL
);
ALTER TABLE
    "user_friend" ADD PRIMARY KEY("user_id");
ALTER TABLE
    "user_friend" ADD PRIMARY KEY("friend_id");
CREATE TABLE "film_genre"(
    "film_id" BIGINT NOT NULL,
    "genre" VARCHAR(255) CHECK
        ("genre" IN('')) NOT NULL
);
ALTER TABLE
    "film_genre" ADD PRIMARY KEY("film_id");
ALTER TABLE
    "film_genre" ADD PRIMARY KEY("genre");
CREATE TABLE "user"(
    "user_id" BIGINT NOT NULL,
    "email" VARCHAR(255) NOT NULL,
    "login" VARCHAR(255) NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    "birthday" DATE NOT NULL
);
ALTER TABLE
    "user" ADD PRIMARY KEY("user_id");
CREATE TABLE "like"(
    "user_id" BIGINT NOT NULL,
    "film_id" BIGINT NOT NULL
);
ALTER TABLE
    "like" ADD PRIMARY KEY("user_id");
ALTER TABLE
    "like" ADD PRIMARY KEY("film_id");
CREATE TABLE "rating_mpa"(
    "rating_mpa_id" BIGINT NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    "description" VARCHAR(255) NOT NULL
);
ALTER TABLE
    "rating_mpa" ADD PRIMARY KEY("rating_mpa_id");
CREATE TABLE "film"(
    "film_id" BIGINT NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    "description" VARCHAR(255) NOT NULL,
    "release_date" DATE NOT NULL,
    "duration_min" INTEGER NOT NULL,
    "rating_mpa_id" BIGINT NOT NULL
);
ALTER TABLE
    "film" ADD PRIMARY KEY("film_id");
ALTER TABLE
    "film_genre" ADD CONSTRAINT "film_genre_film_id_foreign" FOREIGN KEY("film_id") REFERENCES "film"("film_id");
ALTER TABLE
    "user" ADD CONSTRAINT "user_user_id_foreign" FOREIGN KEY("user_id") REFERENCES "user_friend"("friend_id");
ALTER TABLE
    "film" ADD CONSTRAINT "film_rating_mpa_id_foreign" FOREIGN KEY("rating_mpa_id") REFERENCES "rating_mpa"("rating_mpa_id");
ALTER TABLE
    "like" ADD CONSTRAINT "like_user_id_foreign" FOREIGN KEY("user_id") REFERENCES "user"("user_id");
ALTER TABLE
    "user" ADD CONSTRAINT "user_user_id_foreign" FOREIGN KEY("user_id") REFERENCES "user_friend"("user_id");
ALTER TABLE
    "like" ADD CONSTRAINT "like_film_id_foreign" FOREIGN KEY("film_id") REFERENCES "film"("film_id");
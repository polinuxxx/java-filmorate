create table if not exists RATING_MPA
(
    ID          BIGINT auto_increment,
    NAME        CHARACTER VARYING(255),
    DESCRIPTION CHARACTER VARYING(255),
    constraint RATING_MPA_PK
        primary key (ID)
);

create table if not exists FILMS
(
    ID            BIGINT auto_increment,
    NAME          CHARACTER VARYING(255) not null,
    DESCRIPTION   CHARACTER VARYING(200),
    RELEASE_DATE  DATE,
    DURATION_MIN  INTEGER,
    RATING_MPA_ID BIGINT,
    RATE          DOUBLE,
    constraint FILM_PK
        primary key (ID),
    constraint "FILM_RATING_MPA_ID_fk"
        foreign key (RATING_MPA_ID) references RATING_MPA
);

create table if not exists GENRES
(
    ID   BIGINT auto_increment,
    NAME CHARACTER VARYING(255),
    constraint GENRE_PK
        primary key (ID)
);

create table if not exists FILM_GENRES
(
    FILM_ID  BIGINT not null,
    GENRE_ID BIGINT not null,
    constraint FILM_GENRE_PK
        primary key (FILM_ID, GENRE_ID),
    constraint "film_genre_FILM_ID_fk"
        foreign key (FILM_ID) references FILMS
            on delete cascade,
    constraint "film_genre_GENRE_ID_fk"
        foreign key (GENRE_ID) references GENRES
            on delete restrict
);

create table if not exists DIRECTORS
(
    ID   BIGINT auto_increment,
    NAME CHARACTER VARYING(255) not null,
    constraint DIRECTOR_PK
        primary key (ID)
);

create table if not exists FILM_DIRECTORS
(
    FILM_ID  BIGINT not null,
    DIRECTOR_ID BIGINT not null,
    constraint FILM_DIRECTOR_PK
        primary key (FILM_ID, DIRECTOR_ID),
    constraint "film_director_FILM_ID_fk"
        foreign key (FILM_ID) references FILMS
            on delete cascade,
    constraint "film_director_DIRECTOR_ID_fk"
        foreign key (DIRECTOR_ID) references DIRECTORS
            on delete cascade
);

create table if not exists USERS
(
    ID            BIGINT auto_increment,
    EMAIL         CHARACTER VARYING(255) not null,
    LOGIN         CHARACTER VARYING(200) not null,
    NAME          CHARACTER VARYING(255),
    BIRTHDAY      DATE,
    constraint USER_PK
        primary key (ID)
);

create table if not exists FRIENDS
(
    USER_ID   BIGINT not null,
    FRIEND_ID BIGINT not null,
    constraint "FRIENDS_pk"
        primary key (USER_ID, FRIEND_ID),
    constraint "FRIENDS_USERS_ID_fk"
        foreign key (USER_ID) references USERS
            on delete cascade,
    constraint "FRIENDS_USERS_ID_fk2"
        foreign key (FRIEND_ID) references USERS
            on delete cascade
);

create table if not exists EVENTS
(
    ID BIGINT auto_increment primary key,
    CREATED_AT TIMESTAMP not null,
    USER_ID BIGINT not null,
    TYPE INTEGER not null,
    OPERATION INTEGER not null,
    ENTITY_ID BIGINT not null,
    constraint "EVENTS_USERS_ID_fk"
        foreign key (USER_ID) references USERS
            on delete cascade
);

create table if not exists FILM_MARKS
(
    USER_ID BIGINT  not null,
    FILM_ID BIGINT  not null,
    MARK    INTEGER not null,
    constraint "FILMS_MARKS_pk"
        primary key (USER_ID, FILM_ID),
    constraint "FILMS_MARKS_FILMS_ID_fk"
        foreign key (FILM_ID) references FILMS
            on delete cascade,
    constraint "FILMS_MARKS_USERS_ID_fk"
        foreign key (USER_ID) references USERS
            on delete cascade
);

create table if not exists REVIEWS
(
    ID          BIGINT auto_increment,
    CONTENT     CHARACTER VARYING,
    IS_POSITIVE BOOLEAN,
    USER_ID     BIGINT not null,
    FILM_ID     BIGINT not null,
    USEFUL      INTEGER,
    constraint "REVIEWS_pk"
        primary key (ID),
    constraint "REVIEWS_FILMS_ID_fk"
        foreign key (FILM_ID) references FILMS
            on delete cascade,
    constraint "REVIEWS_USERS_ID_fk"
        foreign key (USER_ID) references USERS
            on delete cascade
);

create table if not exists REVIEW_LIKES
(
    REVIEW_ID BIGINT not null,
    USER_ID BIGINT not null,
    REACTION INTEGER,
    constraint "REVIEW_LIKES_pk"
        primary key (REVIEW_ID, USER_ID),
    constraint "REVIEW_LIKES_REVIEWS_ID_fk"
        foreign key (REVIEW_ID) references REVIEWS
            on delete cascade,
    constraint "REVIEW_LIKES_USERS_ID_fk"
        foreign key (USER_ID) references USERS
            on delete cascade
);
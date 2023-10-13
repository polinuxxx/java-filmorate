package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;

class FilmControllerTest extends AbstractControllerTest<FilmController, Film> {

    @BeforeEach
    void init() {

        entity = Film.builder()
                .name("Oppenheimer")
                .description("Epic biographical thriller film written and directed by Christopher Nolan")
                .releaseDate(LocalDate.of(2023, 7, 11))
                .duration(153)
                .build();

        controller = new FilmController();
    }
}
package ru.yandex.practicum.filmorate.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.ResourceUtils;

/**
 * Тесты для {@link UserController}.
 */
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String PATH = "/users";

    @Autowired
    private UserController userController;

    @Test
    void createPositive() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContentFromFile("controller/request/user/user.json")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(getContentFromFile("controller/response/user.json")));
    }

    @Test
    void createWithBirthdayInTheFuture() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContentFromFile("controller/request/user/user-birthday-future.json")))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn().getResolvedException().getMessage().equals("Дата рождения не может быть в будущем");
    }

    @Test
    void createWithEmptyEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContentFromFile("controller/request/user/user-email-empty.json")))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn().getResolvedException().getMessage().equals("Электронная почта не может быть пустой");
    }

    @Test
    void createWithIncorrectEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContentFromFile("controller/request/user/user-email-incorrect-format.json")))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn().getResolvedException().getMessage().equals("Электронная почта не соответствует формату");
    }

    @Test
    void createWithEmptyLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContentFromFile("controller/request/user/user-login-empty.json")))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn().getResolvedException().getMessage().equals("Логин не может быть пустым");
    }

    @Test
    void createWithLoginWithSpaces() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContentFromFile("controller/request/user/user-login-with-spaces.json")))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn().getResolvedException().getMessage().equals("Логин не может содержать пробелы");
    }

    private String getContentFromFile(String fileName) {
        try {
            return Files.readString(ResourceUtils.getFile("classpath:" + fileName).toPath(),
                    StandardCharsets.UTF_8);
        } catch (IOException exception) {
            return "";
        }
    }
}
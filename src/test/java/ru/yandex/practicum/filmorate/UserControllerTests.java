package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.controller.ExceptionApiHandler;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTests {

    private static ObjectMapper mapper;
    private MockMvc mockMvc;
    private User user;

    @InjectMocks
    private UserController userController;

    @BeforeAll
    public static void prepare() {
        mapper = new ObjectMapper();
    }

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new ExceptionApiHandler()).build();
        user = new User(0L, "user1", "test", "user1@user1.com", LocalDate.parse("1945-05-09"));
        user.setName("u1");
    }

    @Test
    public void getUsersSuccessTest() throws Exception {
        User user2 = new User(0L, "user2", "test2", "user2@user2.com",
                LocalDate.parse("1945-10-09"));
        String json = mapper.writeValueAsString(user);
        String json2 = mapper.writeValueAsString(user2);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(json));
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(json2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name", Matchers.equalTo("u1")))
                .andExpect(jsonPath("$[0].id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$[0].login", Matchers.equalTo("test")))
                .andExpect(jsonPath("$[0].birthday", Matchers.equalTo("1945-05-09")))
                .andExpect(jsonPath("$[1].name", Matchers.equalTo("user2")))
                .andExpect(jsonPath("$[1].id", Matchers.equalTo(2)))
                .andExpect(jsonPath("$[1].login", Matchers.equalTo("test2")))
                .andExpect(jsonPath("$[1].birthday", Matchers.equalTo("1945-10-09")));
    }

    @Test
    public void postUsersCorrectDataSuccessTest() throws Exception {
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isOk());
    }

    @Test
    public void postUsersWithDuplicatedEmailBadRequestTest() throws Exception {
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(json));
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isConflict());
    }

    @Test
    public void postUsersFailEmailBadRequestTest() throws Exception {
        user.setEmail("user1user1.com");
        String json = mapper.writeValueAsString(user);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void postUsersEmptyEmailBadRequestTest() throws Exception {
        user.setEmail("");
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void postUsersFutureBirthdayDateBadRequestTest() throws Exception {
        user.setBirthday(LocalDate.now().plusDays(1));
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void postUsersTodayBirthdayDateSuccessTest() throws Exception {
        user.setBirthday(LocalDate.now());
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void postUsersFailLoginBadRequestTest() throws Exception {
        user.setLogin("user 1");
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void postUsersEmptyLoginBadRequestTest() throws Exception {
        user.setLogin("");
        String json = mapper.writeValueAsString(user);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void putUsersCorrectDataSuccessTest() throws Exception {
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(json));
        user.setName("Test");
        user.setLogin("TestLogin");
        user.setId(1L);
        json = mapper.writeValueAsString(user);
        mockMvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isOk());
    }

    @Test
    public void putWrongUsersCorrectBadRequestTest() throws Exception {
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(json));
        user.setName("Test");
        user.setLogin("TestLogin");
        user.setEmail("TestLogin@qwe.rt");
        user.setId(2L);
        json = mapper.writeValueAsString(user);
        mockMvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isNotFound());
    }
}
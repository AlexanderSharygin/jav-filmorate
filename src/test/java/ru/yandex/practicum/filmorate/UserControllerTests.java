package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTests {

    private static ObjectMapper mapper;

    private User user;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeAll
    public static void prepare() {
        mapper = new ObjectMapper();
    }

    @BeforeEach
    public void setup() {
        user = new User(0L, "u1", "user1", "user1@user1.com",
                LocalDate.parse("1945-05-09"));
    }

    @Test
    public void getUsersSuccessTest() throws Exception {
        User user2 = new User(1L, "u2", "user2", "user2@user2.com",
                LocalDate.parse("1945-10-09"));
        user.setId(1L);
        user2.setId(2L);
        when(userService.getAll())
                .thenReturn(List.of(user, user2));
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name", Matchers.equalTo("u1")))
                .andExpect(jsonPath("$[0].id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$[0].login", Matchers.equalTo("user1")))
                .andExpect(jsonPath("$[0].birthday", Matchers.equalTo("1945-05-09")))
                .andExpect(jsonPath("$[1].name", Matchers.equalTo("u2")))
                .andExpect(jsonPath("$[1].id", Matchers.equalTo(2)))
                .andExpect(jsonPath("$[1].login", Matchers.equalTo("user2")))
                .andExpect(jsonPath("$[1].birthday", Matchers.equalTo("1945-10-09")));
    }

    @Test
    public void postUsersCorrectDataSuccessTest() throws Exception {
        String json = mapper.writeValueAsString(user);
        when(userService.addUser(user)).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk())
                .andExpect(jsonPath("name", Matchers.equalTo("u1")));
    }

    @Test
    public void postUsersFailEmailBadRequestTest() throws Exception {
        user.setEmail("user1user1.com");
        String json = mapper.writeValueAsString(user);
        when(userService.addUser(user)).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(json));
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
    }

    @Test
    public void postUsersEmptyEmailBadRequestTest() throws Exception {
        user.setEmail("");
        String json = mapper.writeValueAsString(user);
        when(userService.addUser(user)).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(json));
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
    }

    @Test
    public void postUsersFutureBirthdayDateBadRequestTest() throws Exception {
        user.setBirthday(LocalDate.now().plusDays(1));
        String json = mapper.writeValueAsString(user);
        when(userService.addUser(user)).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
    }

    @Test
    public void postUsersTodayBirthdayDateSuccessTest() throws Exception {
        user.setBirthday(LocalDate.now());
        String json = mapper.writeValueAsString(user);
        when(userService.addUser(user)).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
    }

    @Test
    public void postUsersFailLoginBadRequestTest() throws Exception {
        user.setLogin("user 1");
        String json = mapper.writeValueAsString(user);
        when(userService.addUser(user)).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
    }

    @Test
    public void postUsersEmptyLoginBadRequestTest() throws Exception {
        user.setLogin("");
        String json = mapper.writeValueAsString(user);
        when(userService.addUser(user)).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
    }

    @Test
    public void putUsersCorrectDataSuccessTest() throws Exception {
        String json = mapper.writeValueAsString(user);
        when(userService.addUser(user)).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(json));
        user.setName("Test");
        user.setLogin("TestLogin");
        user.setId(1L);
        json = mapper.writeValueAsString(user);
        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
    }
}
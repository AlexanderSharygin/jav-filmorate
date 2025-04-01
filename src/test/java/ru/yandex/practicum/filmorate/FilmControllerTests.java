package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
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
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.error.ExceptionApiHandler;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class FilmControllerTests {

    private static ObjectMapper mapper;
    private MockMvc mockMvc;
    private Film film;

    @InjectMocks
    private FilmController filmController;

    @BeforeAll
    public static void prepare() {
        mapper = new ObjectMapper();
    }

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(filmController).setControllerAdvice(new ExceptionApiHandler()).build();
        film = new Film(0L, "Test test", "Description", LocalDate.parse("1945-05-09"),
                100);
    }

    @Test
    public void postFilmCorrectDataSuccessTest() throws Exception {
        String json = mapper.writeValueAsString(film);

        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isOk());
    }

    @Test
    public void postFilmWithDuplicatedNameSuccessTest() throws Exception {
        Film film2 = new Film(0L, "Test test", "Description", LocalDate.parse("1945-05-10"),
                100);
        String json = mapper.writeValueAsString(film);
        String json2 = mapper.writeValueAsString(film2);

        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isOk());
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                .content(json2)).andExpect(status().isOk());
    }

    @Test
    public void postFilmWithDuplicatedReleaseDateSuccessTest() throws Exception {
        Film film2 = new Film(0L, "Test test2", "Description", LocalDate.parse("1945-05-09"),
                100);
        String json = mapper.writeValueAsString(film);
        String json2 = mapper.writeValueAsString(film2);

        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isOk());
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                .content(json2)).andExpect(status().isOk());
    }

    @Test
    public void postFilmWithDuplicatedReleaseDateAndNameConflictTest() throws Exception {
        Film film2 = new Film(0L, "Test test", "Description", LocalDate.parse("1945-05-09"),
                100);
        String json = mapper.writeValueAsString(film);
        String json2 = mapper.writeValueAsString(film2);

        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isOk());
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                .content(json2)).andExpect(status().isConflict());
    }

    @Test
    public void postFilmWith201CharsDescriptionBadRequestTest() throws Exception {
        film.setDescription(RandomStringUtils.randomAlphabetic(201));
        String json = mapper.writeValueAsString(film);
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isBadRequest());
    }

    @Test
    public void postFilmWith200CharsDescriptionSuccessTest() throws Exception {
        film.setDescription(RandomStringUtils.randomAlphabetic(200));
        String json = mapper.writeValueAsString(film);
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isOk());
    }

    @Test
    public void postFilmWithCorrectReleaseDateSuccessTest() throws Exception {
        film.setReleaseDate(LocalDate.parse("1895-12-28"));
        String json = mapper.writeValueAsString(film);
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isOk());
    }

    @Test
    public void postFilmWithInCorrectReleaseDateBadRequestTest() throws Exception {
        film.setReleaseDate(LocalDate.parse("1895-12-27"));
        String json = mapper.writeValueAsString(film);
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void postFilmWithPositiveDurationSuccessTest() throws Exception {
        film.setDuration(1);
        String json = mapper.writeValueAsString(film);
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isOk());
    }

    @Test
    public void postFilmWithZeroDurationBadRequestTest() throws Exception {
        film.setDuration(0);
        String json = mapper.writeValueAsString(film);
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isBadRequest());
    }

    @Test
    public void postFilmWithZeroNegativeDurationBadRequestTest() throws Exception {
        film.setDuration(-1);
        String json = mapper.writeValueAsString(film);
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isBadRequest());
    }

    @Test
    public void putFilmCorrectDataSuccessTest() throws Exception {
        String json = mapper.writeValueAsString(film);
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                .content(json));
        film.setDescription("Upd");
        film.setDuration(12);
        film.setId(2L);
        json = mapper.writeValueAsString(film);

        mockMvc.perform(put("/films").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isOk());
    }

    @Test
    public void putFilmSameNameDifferentReleaseDateBadRequestTest() throws Exception {
        String json = mapper.writeValueAsString(film);
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                .content(json));
        film.setDescription("Upd");
        film.setDuration(12);
        film.setReleaseDate(LocalDate.parse("1999-02-01"));
        json = mapper.writeValueAsString(film);
        mockMvc.perform(put("/films").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isNotFound());
    }

    @Test
    public void putFilmDifferentNameAndSameReleaseDateBadRequestTest() throws Exception {
        String json = mapper.writeValueAsString(film);
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                .content(json));
        film.setDescription("Upd");
        film.setDuration(12);
        film.setName("1999-02-01");
        json = mapper.writeValueAsString(film);
        mockMvc.perform(put("/films").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isNotFound());
    }

    @Test
    public void getFilmsSuccessTest() throws Exception {
        Film film2 = new Film(0L, "Test test2", "Description", LocalDate.parse("1945-05-09"),
                100);
        String json = mapper.writeValueAsString(film);
        String json2 = mapper.writeValueAsString(film2);
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                .content(json));
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                .content(json2));
        mockMvc.perform(get("/films"))
                .andExpect(status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name", Matchers.equalTo("Test test")))
                .andExpect(jsonPath("$[1].name", Matchers.equalTo("Test test2")));
    }
}

package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
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
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilmController.class)
public class FilmControllerTests {

    private static ObjectMapper mapper;

    private Film film;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilmService filmService;

    @BeforeAll
    public static void prepare() {
        mapper = new ObjectMapper();
    }

    @BeforeEach
    public void setup() {
        film = new Film(0L, "Test test", "Description",
                LocalDate.parse("1945-05-09"), 100);
    }

    @Test
    public void postFilmCorrectDataSuccessTest() throws Exception {
        String json = mapper.writeValueAsString(film);
        when(filmService.addFilm(film)).thenReturn(film);
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", Matchers.equalTo("Test test")));
    }


    @Test
    public void postFilmWith201CharsDescriptionBadRequestTest() throws Exception {
        film.setDescription(RandomStringUtils.randomAlphabetic(201));
        String json = mapper.writeValueAsString(film);
        when(filmService.addFilm(film)).thenReturn(film);
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
    }

    @Test
    public void postFilmWith200CharsDescriptionSuccessTest() throws Exception {
        film.setDescription(RandomStringUtils.randomAlphabetic(200));
        String json = mapper.writeValueAsString(film);
        when(filmService.addFilm(film)).thenReturn(film);
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
    }

    @Test
    public void postFilmWithCorrectReleaseDateSuccessTest() throws Exception {
        film.setReleaseDate(LocalDate.parse("1895-12-28"));
        String json = mapper.writeValueAsString(film);
        when(filmService.addFilm(film)).thenReturn(film);
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
    }

    @Test
    public void postFilmWithPositiveDurationSuccessTest() throws Exception {
        film.setDuration(1);
        String json = mapper.writeValueAsString(film);
        when(filmService.addFilm(film)).thenReturn(film);
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
    }

    @Test
    public void postFilmWithZeroDurationBadRequestTest() throws Exception {
        film.setDuration(0);
        String json = mapper.writeValueAsString(film);
        when(filmService.addFilm(film)).thenReturn(film);
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
    }

    @Test
    public void postFilmWithZeroNegativeDurationBadRequestTest() throws Exception {
        film.setDuration(-1);
        String json = mapper.writeValueAsString(film);
        when(filmService.addFilm(film)).thenReturn(film);
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
    }

    @Test
    public void putFilmCorrectDataSuccessTest() throws Exception {
        String json = mapper.writeValueAsString(film);
        when(filmService.addFilm(film)).thenReturn(film);
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON).content(json));
        film.setDescription("Upd");
        film.setDuration(12);
        film.setId(1L);
        json = mapper.writeValueAsString(film);
        mockMvc.perform(MockMvcRequestBuilders.put("/films")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
    }

    @Test
    public void getFilmsSuccessTest() throws Exception {
        Film film2 = new Film(1L, "Test test2", "Description", LocalDate.parse("1945-05-09"),
                100);
        when(filmService.getAll())
                .thenReturn(List.of(film, film2));
        mockMvc.perform(MockMvcRequestBuilders.get("/films"))
                .andExpect(status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name", Matchers.equalTo("Test test")))
                .andExpect(jsonPath("$[1].name", Matchers.equalTo("Test test2")));
    }
}

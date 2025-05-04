package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.serializer.LocalDateDeserializer;
import ru.yandex.practicum.filmorate.serializer.LocalDateSerializer;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Long id;
    private String name;
    @NotNull
    @NotBlank
    @Pattern(regexp = "^\\S+$")
    private String login;
    @Email
    @NotBlank
    @NotNull
    private String email;
    @NotNull
    @PastOrPresent
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate birthday;
    private Set<Long> friends;


    public User(Long id, String name, String login, String email, LocalDate birthday) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.friends = new HashSet<>();
    }
}
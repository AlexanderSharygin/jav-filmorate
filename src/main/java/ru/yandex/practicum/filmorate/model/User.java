package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.serializer.LocalDateDeserializer;
import ru.yandex.practicum.filmorate.serializer.LocalDateSerializer;

import java.time.LocalDate;

@Data
@NoArgsConstructor
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

    public User(Long id, String name, String login, String email, LocalDate birthday) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }
}
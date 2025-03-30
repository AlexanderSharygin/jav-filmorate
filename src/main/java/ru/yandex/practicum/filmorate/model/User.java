package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class User {
    private Long id;
    private String name;
    @NonNull
    @NotBlank
    @Pattern(regexp = "^\\S+$")
    private String login;
    @Email
    private String email;
    @NonNull
    @PastOrPresent
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate birthday;
}
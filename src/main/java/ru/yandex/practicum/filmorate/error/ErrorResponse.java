package ru.yandex.practicum.filmorate.error;


import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class ErrorResponse {
    String error;
    String description;

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }

}

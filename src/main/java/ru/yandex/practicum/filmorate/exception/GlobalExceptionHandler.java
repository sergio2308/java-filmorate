package ru.yandex.practicum.filmorate.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public Map<String, String> processNotFound(NotFoundException nfe) {
        HashMap<String, String> response = new HashMap<>();
        response.put("error", "Не найден");
        return response;
    }

    @ExceptionHandler(value = ValidationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Map<String, String> processNotValid(ValidationException ve) {
        HashMap<String, String> response = new HashMap<>();
        response.put("error", "Валидация не пройдена");
        return response;
    }

    @ExceptionHandler(value = Throwable.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> processAnyException(Throwable te) {
        log.error("Ошибка: {}", te.getMessage());
        HashMap<String, String> response = new HashMap<>();
        response.put("error", "Произошла неизвестная ошибка");
        return response;
    }
}

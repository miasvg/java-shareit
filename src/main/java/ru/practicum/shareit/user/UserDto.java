package ru.practicum.shareit.user;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(groups = {OnCreate.class}, message = "Имя не может быть пустым")
    private String name;

    @NotBlank(groups = {OnCreate.class}, message = "Email не может быть пустым")
    @Email(groups = {OnCreate.class, OnUpdate.class}, message = "Некорректный email")
    private String email;

    // Группы валидации
    public interface OnCreate {}
    // Для POST
    public interface OnUpdate {}  // Для PATCH
}

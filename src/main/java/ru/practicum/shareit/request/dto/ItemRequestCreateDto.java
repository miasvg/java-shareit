package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestCreateDto {
    @NotBlank(message = "Описание запроса не может быть пустым")
    private String description;
    private LocalDateTime created;
}
package ru.practicum.shareit.request.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestCreateDto {
    private String description;
    private LocalDateTime created;
}
package ru.practicum.shareit.request;

import lombok.*;
import ru.practicum.shareit.user.User;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ItemRequest {
    private Long id;
    private String description;  // Текст запроса
    private User requestor;      // Кто создал запрос
    private LocalDateTime created;  // Дата создания
}

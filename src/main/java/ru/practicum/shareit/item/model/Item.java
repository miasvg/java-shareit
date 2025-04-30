// Item.java (Модель)
package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.user.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private Long id;
    private String name;
    private String description;
    private Boolean available; // Доступна для аренды
    private User owner;       // Владелец
    private Long requestId;   // Если вещь создана по запросу
}
// Item.java (Модель)
package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
public class Item {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(length = 512)
    private String description;
    @Column(nullable = false)
    private Boolean available;
    @Column(name = "owner_id", nullable = false)// Доступна для аренды
    private Long ownerId;
}
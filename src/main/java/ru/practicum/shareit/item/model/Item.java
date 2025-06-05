// Item.java (Модель)
package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.request.ItemRequest;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @ManyToOne
    @JoinColumn(name = "request_id") // допускаем null — вещь может быть без запроса
    private ItemRequest request;

}
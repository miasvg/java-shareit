package ru.practicum.shareit.booking.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {

    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
}
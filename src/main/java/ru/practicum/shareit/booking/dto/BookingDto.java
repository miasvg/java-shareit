package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {

    @NotNull
    @FutureOrPresent
    private LocalDateTime start;

    @NotNull @Future
    private LocalDateTime end;

    @NotNull
    private Long itemId;
}
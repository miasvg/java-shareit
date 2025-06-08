package ru.practicum.shareit.item;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    @Size(min = 1, max = 255)
    private String text;
    private String authorName;
    private LocalDateTime created;
}


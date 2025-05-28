package ru.practicum.shareit.item.comments;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {

    public static CommentDto toDto(Comment comment) {
        if (comment == null) return null;

        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }

    public static Comment toEntity(String text, User author, Item item) {
        Comment comment = new Comment();
        comment.setText(text);
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());
        return comment;
    }
}


package ru.practicum.shareit.item.comments;

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
}

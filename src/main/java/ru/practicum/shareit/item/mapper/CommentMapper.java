package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

public class CommentMapper {
    private CommentMapper() {
    }

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .author(comment.getAuthor())
                .created(comment.getCreated())
                .text(comment.getText())
                .item(comment.getItem())
                .build();
    }

    public static Comment toComment(CommentDto dto) {
        return Comment.builder()
                .id(dto.getId())
                .author(dto.getAuthor())
                .created(dto.getCreated())
                .text(dto.getText())
                .item(dto.getItem())
                .build();
    }
}

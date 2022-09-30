package ru.practicum.shareit.item.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.validation.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExtendedItemDto {
    private Long id;

    @NotBlank(groups = Create.class, message = "Name should be provided")
    private String name;

    @NotBlank(groups = Create.class, message = "Description should be provided")
    private String description;

    @NotNull(groups = Create.class, message = "Availability should be provided")
    private Boolean available;

    private ItemRequest request;

    private LocalDateTime lastDate;

    private LocalDateTime nextDate;

    private List<Comment> comments;
}

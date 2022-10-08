package ru.practicum.shareit.requests.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.validation.Create;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
@Builder
public class ItemRequestDto {
    private Long id;

    @NotBlank(groups = Create.class, message = "Description should be provided")
    private String description;

    private LocalDateTime created;

    private Collection<ItemDto> items;
}
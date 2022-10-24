package ru.practicum.shareit.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.validation.Create;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
public class ItemRequestDto {
    @NotBlank(groups = Create.class, message = "Description should be provided")
    private String description;
}
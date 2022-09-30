package ru.practicum.shareit.requests;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    private Long id;
    private Item item;
    private String description;
    private User requester;
    private LocalDateTime createdDateTime = LocalDateTime.now();
}

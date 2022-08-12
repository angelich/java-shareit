package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, ItemDto itemDto, Long userId1);

    ItemDto getItem(Long userId, Long itemId);

    Collection<ItemDto> getItemsByOwner(Long userId);

    Collection<ItemDto> findItem(Long userId, String text);
}

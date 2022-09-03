package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {
    Item createItem(Long userId, ItemDto itemDto);

    Item updateItem(Long userId, ItemDto itemDto, Long userId1);

    Item getItem(Long userId, Long itemId);

    Collection<Item> getItemsByOwner(Long userId);

    Collection<Item> findItem(Long userId, String text);
}

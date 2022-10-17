package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ExtendedItemDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, ItemDto itemDto, Long itemId);

    ExtendedItemDto getItem(Long userId, Long itemId);

    Collection<ExtendedItemDto> getItemsByOwner(Long userId, int from, int size);

    Collection<ItemDto> findItem(Long userId, String text, int from, int size);

    CommentDto createComment(Long userId, Long itemId, CommentDto comment);
}

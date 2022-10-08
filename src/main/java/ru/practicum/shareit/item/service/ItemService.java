package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ExtendedItemDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, ItemDto itemDto, Long userId1);

    ExtendedItemDto getItem(Long userId, Long itemId);

    Collection<ExtendedItemDto> getItemsByOwner(Long userId, Integer from, Integer size);

    Collection<ItemDto> findItem(Long userId, String text, Integer from, Integer size);

    CommentDto createComment(Long userId, Long itemId, CommentDto comment);
}

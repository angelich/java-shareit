package ru.practicum.shareit.requests.service;

import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.util.Collection;

public interface RequestService {
    ItemRequestDto createRequest(Long userId, ItemRequestDto requestDto);

    Collection<ItemRequestDto> getMyRequests(Long userId);

    Collection<ItemRequestDto> getAllRequests(Long userId, Integer from, Integer size);

    ItemRequestDto getOneRequest(Long userId, Long requestId);
}

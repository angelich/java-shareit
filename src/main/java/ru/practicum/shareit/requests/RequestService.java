package ru.practicum.shareit.requests;

import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.util.Collection;

public interface RequestService {
    ItemRequestDto createRequest(Long userId, ItemRequestDto requestDto);

    Collection<ItemRequestDto> getMyRequests(Long userId);

    Collection<ItemRequestDto> getAllRequests(Long userId, Long from, Long size);

    ItemRequestDto getOneRequest(Long userId, Long requestId);
}

package ru.practicum.shareit.requests;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.util.Collection;

@Service
public class RequestServiceImpl implements RequestService {
    @Override
    public ItemRequestDto createRequest(Long userId, ItemRequestDto requestDto) {
        return null;
    }

    @Override
    public Collection<ItemRequestDto> getMyRequests(Long userId) {
        return null;
    }

    @Override
    public Collection<ItemRequestDto> getAllRequests(Long userId, Long from, Long size) {
        return null;
    }

    @Override
    public ItemRequestDto getOneRequest(Long userId, Long requestId) {
        return null;
    }
}

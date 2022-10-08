package ru.practicum.shareit.requests.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.ItemRequestMapper;
import ru.practicum.shareit.requests.RequestRepository;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static ru.practicum.shareit.requests.ItemRequestMapper.toItemRequestDto;

@Service
public class RequestServiceImpl implements RequestService {
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final RequestRepository requestRepository;

    public RequestServiceImpl(UserService userService, ItemRepository itemRepository, RequestRepository requestRepository) {
        this.userService = userService;
        this.itemRepository = itemRepository;
        this.requestRepository = requestRepository;
    }


    @Override
    public ItemRequestDto createRequest(Long userId, ItemRequestDto requestDto) {
        userService.checkUserExist(userId);

        ItemRequest request = new ItemRequest();
        request.setRequester(userId);
        request.setDescription(requestDto.getDescription());

        ItemRequest savedRequest = requestRepository.save(request);
        return toItemRequestDto(savedRequest);
    }

    @Override
    public Collection<ItemRequestDto> getMyRequests(Long userId) {
        userService.checkUserExist(userId);

        List<ItemRequestDto> requests = requestRepository.findAllByRequesterOrderByCreatedDateTimeDesc(userId)
                .stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());

        requests.forEach(requestDto -> requestDto.setItems(
                itemRepository.findAllByRequestId(requestDto.getId())
                        .stream()
                        .map(ItemMapper::toItemDto)
                        .collect(Collectors.toList())));

        return requests;
    }

    @Override
    public Collection<ItemRequestDto> getAllRequests(Long userId, Integer from, Integer size) {
        userService.checkUserExist(userId);

        return requestRepository.findAllByRequesterNot(userId, PageRequest.of(from / size, size))
                .stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getOneRequest(Long userId, Long requestId) {
        userService.checkUserExist(userId);
        ItemRequest request = requestRepository.findById(requestId).orElseThrow(
                () -> new NoSuchElementException("Item request id not exist"));

        ItemRequestDto requestDto = toItemRequestDto(request);
        requestDto.setItems(
                itemRepository.findAllByRequestId(requestId)
                        .stream()
                        .map(ItemMapper::toItemDto)
                        .collect(Collectors.toList()));

        return requestDto;
    }
}

package ru.practicum.shareit.request;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.ItemRequestMapper;
import ru.practicum.shareit.requests.RequestRepository;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.service.RequestService;
import ru.practicum.shareit.requests.service.RequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class RequestUnitTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserService userService;
    @Mock
    private RequestRepository requestRepository;

    private RequestService requestService;

    @BeforeEach
    void beforeEach() {
        requestService = new RequestServiceImpl(
                userService,
                itemRepository,
                requestRepository);
    }

    @Test
    void shouldCreateRequest() {
        User requester = new User(2L, "userName", "booker@email.com");

        ItemRequest request = ItemRequest.builder()
                .id(1L)
                .requester(requester.getId())
                .createdDateTime(now())
                .description("some desc")
                .build();

        Mockito
                .when(requestRepository.save(any()))
                .thenReturn(request);

        var savedRequest = requestService.createRequest(requester.getId(), ItemRequestMapper.toItemRequestDto(request));

        assertNotNull(savedRequest.getId());
        assertEquals(request.getDescription(), savedRequest.getDescription());
        assertNotNull(request.getCreatedDateTime());
    }

    @Test
    void shouldReturnUserRequest() {
        User requester = new User(2L, "userName", "booker@email.com");

        ItemRequest request = ItemRequest.builder()
                .id(1L)
                .requester(requester.getId())
                .createdDateTime(now())
                .description("some desc")
                .build();

        Mockito
                .when(requestRepository.findAllByRequesterOrderByCreatedDateTimeDesc(any()))
                .thenReturn(List.of(request));

        var savedRequest = requestService.getMyRequests(requester.getId()).stream().findFirst().get();

        assertNotNull(savedRequest.getId());
        assertEquals(request.getDescription(), savedRequest.getDescription());
        assertNotNull(request.getCreatedDateTime());
    }

    @Test
    void shouldReturnAllRequest() {
        User requester = new User(2L, "userName", "booker@email.com");

        ItemRequest request = ItemRequest.builder()
                .id(1L)
                .requester(requester.getId())
                .createdDateTime(now())
                .description("some desc")
                .build();

        Mockito
                .when(requestRepository.findAllByRequesterNot(any(), any()))
                .thenReturn(new PageImpl<>(List.of(request)));

        var savedRequest = requestService.getAllRequests(requester.getId(), 0, 10).stream().findFirst().get();

        assertNotNull(savedRequest.getId());
        assertEquals(request.getDescription(), savedRequest.getDescription());
        assertNotNull(request.getCreatedDateTime());
    }

    @Test
    void shouldReturnOneRequest() {
        User requester = new User(2L, "userName", "booker@email.com");

        ItemRequest request = ItemRequest.builder()
                .id(1L)
                .requester(requester.getId())
                .createdDateTime(now())
                .description("some desc")
                .build();

        Mockito
                .when(requestRepository.findById(any()))
                .thenReturn(Optional.ofNullable(request));

        var savedRequest = requestService.getOneRequest(requester.getId(), request.getId());

        assertNotNull(savedRequest.getId());
        assertEquals(request.getDescription(), savedRequest.getDescription());
        assertNotNull(request.getCreatedDateTime());
    }
}

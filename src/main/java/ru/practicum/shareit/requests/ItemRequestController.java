package ru.practicum.shareit.requests;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.service.RequestService;
import ru.practicum.shareit.validation.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

/**
 * Контроллер запросов вещей
 */
@RestController
@Validated
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final RequestService requestService;

    public ItemRequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    ItemRequestDto createRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @Validated(Create.class) @RequestBody ItemRequestDto requestDto) {
        return requestService.createRequest(userId, requestDto);
    }

    @GetMapping
    Collection<ItemRequestDto> getMyRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestService.getMyRequests(userId);
    }

    @GetMapping("/all")
    Collection<ItemRequestDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                              @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        return requestService.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    ItemRequestDto getOneRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable("requestId") Long requestId) {
        return requestService.getOneRequest(userId, requestId);
    }
}

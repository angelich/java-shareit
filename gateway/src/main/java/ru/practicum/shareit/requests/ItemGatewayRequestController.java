package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * Контроллер запросов вещей
 */
@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemGatewayRequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @Valid @RequestBody RequestDto requestDto) {
        log.info("Creating itemRequest={}, user {}", requestDto, userId);
        return requestClient.createRequest(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getMyRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get user requests, user={}", userId);
        return requestClient.getMyRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                                 @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Get all requests, user={}, from={}, size={}", userId, from, size);
        return requestClient.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getOneRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PathVariable("requestId") Long requestId) {
        log.info("Get requestId={}, user={}", requestId, userId);
        return requestClient.getOneRequest(userId, requestId);
    }
}

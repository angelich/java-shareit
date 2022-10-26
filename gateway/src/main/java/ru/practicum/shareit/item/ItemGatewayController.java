package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * Контроллер вещей
 */
@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/items")
public class ItemGatewayController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @Validated(Create.class) @RequestBody ItemRequestDto itemDto) {
        log.info("Creating item={}, user={}", itemDto, userId);
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @Validated(Update.class) @RequestBody ItemRequestDto itemDto,
                                             @PathVariable Long itemId) {
        log.info("Updating itemId={}, item={}, user={}", itemId, itemDto, userId);
        return itemClient.updateItem(userId, itemDto, itemId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        log.info("Get item {}, user {}", itemId, userId);
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                                  @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Get owner items, user={}, from={}, size={}", userId, from, size);
        return itemClient.getItemsByOwner(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestParam("text") String text,
                                           @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                           @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Searching for items by text={}, user={}, from={}, size={}", text, userId, from, size);
        return itemClient.findItem(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PathVariable Long itemId,
                                                @Validated(Create.class) @RequestBody CommentRequestDto comment) {
        log.info("Creating item comment={}, itemId={}, user={}", comment, itemId, userId);
        return itemClient.createComment(userId, itemId, comment);
    }
}

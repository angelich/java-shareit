package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ExtendedItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

/**
 * Контроллер вещей
 */
@RestController
@Validated
@RequestMapping(path = "/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                       @Validated(Create.class) @RequestBody ItemDto itemDto) {
        return itemService.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                       @Validated(Update.class) @RequestBody ItemDto itemDto,
                       @PathVariable Long itemId) {
        return itemService.updateItem(userId, itemDto, itemId);
    }

    @GetMapping("/{itemId}")
    ExtendedItemDto getItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        return itemService.getItem(userId, itemId);
    }

    @GetMapping
    Collection<ExtendedItemDto> getItemsByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                                @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        return itemService.getItemsByOwner(userId, from, size);
    }

    @GetMapping("/search")
    Collection<ItemDto> findItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @RequestParam("text") String text,
                                 @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                 @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        return itemService.findItem(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    CommentDto createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                             @PathVariable Long itemId,
                             @Validated(Create.class) @RequestBody CommentDto comment) {
        return itemService.createComment(userId, itemId, comment);
    }
}

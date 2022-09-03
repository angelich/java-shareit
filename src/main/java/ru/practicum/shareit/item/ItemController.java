package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.ItemMapper.toItemDto;

/**
 * Контроллер вещей
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemDto itemDto) {
        return toItemDto(itemService.createItem(userId, itemDto));
    }

    @PatchMapping("/{itemId}")
    ItemDto updateItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody ItemDto itemDto,
            @PathVariable Long itemId) {
        return toItemDto(itemService.updateItem(userId, itemDto, itemId));
    }

    @GetMapping("/{itemId}")
    ItemDto getItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        return toItemDto(itemService.getItem(userId, itemId));
    }

    @GetMapping
    Collection<ItemDto> getItemsByOwner(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItemsByOwner(userId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    Collection<ItemDto> findItem(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam("text") String text) {
        return itemService.findItem(userId, text)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}

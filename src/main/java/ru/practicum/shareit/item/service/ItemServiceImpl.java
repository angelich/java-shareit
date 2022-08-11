package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.ItemMapper.toItemDto;

/**
 * Сервис по работе с вещами
 */
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        if (itemDto.getAvailable() == null) {
            throw new IllegalArgumentException("Availability should be provided");
        }
        if (itemDto.getName() == null || itemDto.getName().isEmpty()) {
            throw new IllegalArgumentException("Name should be provided");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Description should be provided");
        }
        checkUserExist(userId);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userRepository.getUser(userId));
        return toItemDto(itemRepository.createItem(item));
    }

    @Override
    public ItemDto updateItem(Long userId, ItemDto itemDto, Long itemId) {
        if (itemDto.getName() != null && itemDto.getName().isEmpty()) {
            throw new IllegalArgumentException("Name should be provided");
        }
        if (itemDto.getDescription() != null && itemDto.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Description should be provided");
        }

        checkUserExist(userId);
        if (itemRepository.getItem(itemId) == null || !itemRepository.getItem(itemId).getOwner().getId().equals(userId)) {
            throw new NoSuchElementException("Item not exist");
        }

        Item item = ItemMapper.toItem(itemDto);
        return toItemDto(itemRepository.updateItem(item, itemId));
    }

    @Override
    public ItemDto getItem(Long userId, Long itemId) {
        checkUserExist(userId);
        if (itemRepository.getItem(itemId) == null) {
            throw new NoSuchElementException("Item not exist");
        }
        return toItemDto(itemRepository.getItem(itemId));
    }

    @Override
    public Collection<ItemDto> getItemsByOwner(Long userId) {
        checkUserExist(userId);
        return itemRepository.getItemsByOwner(userId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDto> findItem(Long userId, String text) {
        checkUserExist(userId);
        if (text.isEmpty()){
            return Collections.emptyList();
        }
        return itemRepository.findItem(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private void checkUserExist(Long userId) {
        if (userRepository.getUser(userId) == null) {
            throw new NoSuchElementException("User not exist");
        }
    }
}

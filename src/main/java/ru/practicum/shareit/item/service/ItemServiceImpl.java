package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;

import static ru.practicum.shareit.item.ItemMapper.toItem;

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
    public Item createItem(Long userId, ItemDto itemDto) {
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
        Item item = toItem(itemDto);
        item.setOwner(userRepository.getUser(userId));
        return itemRepository.createItem(item);
    }

    @Override
    public Item updateItem(Long userId, ItemDto itemDto, Long itemId) {
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
        return itemRepository.updateItem(toItem(itemDto), itemId);
    }

    @Override
    public Item getItem(Long userId, Long itemId) {
        checkUserExist(userId);
        if (itemRepository.getItem(itemId) == null) {
            throw new NoSuchElementException("Item not exist");
        }
        return itemRepository.getItem(itemId);
    }

    @Override
    public Collection<Item> getItemsByOwner(Long userId) {
        checkUserExist(userId);
        return itemRepository.getItemsByOwner(userId);
    }

    @Override
    public Collection<Item> findItem(Long userId, String text) {
        checkUserExist(userId);
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return itemRepository.findItem(text);
    }

    private void checkUserExist(Long userId) {
        if (userRepository.getUser(userId) == null) {
            throw new NoSuchElementException("User not exist");
        }
    }
}

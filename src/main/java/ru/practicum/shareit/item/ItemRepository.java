package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Хранилище вещей
 */
@Repository
public class ItemRepository {
    private static Long counter = 0L;
    private final Map<Long, Item> items = new HashMap<>();

    private static Long incrementCounter() {
        return ++counter;
    }

    public Item createItem(Item item) {
        Long newId = incrementCounter();
        item.setId(newId);
        items.put(newId, item);
        return items.get(newId);
    }

    public Item getItem(Long itemId) {
        return items.get(itemId);
    }

    public Item updateItem(Item item, Long itemId) {
        Item updatedItem =  items.get(itemId);
        if (item.getAvailable() != null) {
            updatedItem.setAvailable(item.getAvailable());
        }
        if (item.getName() != null) {
            updatedItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updatedItem.setDescription(item.getDescription());
        }
        return updatedItem;
    }

    public Collection<Item> getItemsByOwner(Long userId) {
        return items.values()
                .stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }

    public Collection<Item> findItem(String text) {
        return items.values()
                .stream()
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }
}
